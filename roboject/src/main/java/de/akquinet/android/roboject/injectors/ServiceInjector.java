package de.akquinet.android.roboject.injectors;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import de.akquinet.android.roboject.Container;
import de.akquinet.android.roboject.RobojectException;
import de.akquinet.android.roboject.RobojectLifecycle;
import de.akquinet.android.roboject.annotations.Inject;
import de.akquinet.android.roboject.util.ReflectionUtil;


public class ServiceInjector implements Injector
{
    private Context context;
    private Container container;
    private Object managed;

    private InjectorState state = InjectorState.CREATED;

    private Map<Field, Boolean> fieldInjections = new HashMap<Field, Boolean>();
    private List<ServiceConnection> serviceConnections = new ArrayList<ServiceConnection>();

    /**
     * Method called by the container to initialize the container.
     * 
     * @param context
     *            the android context
     * @param container
     *            the roboject container
     * @param managed
     *            the managed instance
     * @param clazz
     *            the managed class (the class of <tt>managed</tt>)
     * @return <code>true</code> if the injector wants to contribute to the
     *         management of the instance, <code>false</code> otherwise. In this
     *         latter case, the injector will be ignored for this instance.
     * @throws RobojectException
     *             if the configuration failed.
     */
    @Override
    public boolean configure(Context context, Container container,
            Object managed, Class<?> clazz) throws RobojectException {
        this.context = context;
        this.container = container;
        this.managed = managed;
        return true;
    }

    /**
     * Method called by the container when all injectors are configured
     * (immediately after configure). This method is called on valid injector
     * only. In this method, the injector can injects field and call callbacks
     * (however, callbacks may wait the validate call).
     * 
     * @param context
     *            the android context
     * @param container
     *            the roboject container
     * @param managed
     *            the managed instance
     */

    @Override
    public void start(Context context, Container container, Object managed) {
        this.state = InjectorState.STARTED;
        List<Field> fields = ReflectionUtil.getAnnotatedFields(managed.getClass(), Inject.class);

        if (fields.isEmpty()) {
            // Nothing to inject. This means all services (zero) are bound.
            invokeServicesConnectedLifeCycle();
            return;
        }

        for (Field field : fields) {
            fieldInjections.put(field, false);
        }

        for (Field field : fields) {
            injectService(field);
        }
    }

    /**
     * Method called by the container when the container is disposed. This
     * method is called on valid injector only. In this method, the injector can
     * free resources
     * 
     * @param context
     *            the android context
     * @param managed
     *            the managed instance
     */
    @Override
    public void stop(Context context, Object managed) {
        this.managed = null;
    }

    /**
     * Checks whether the injector is valid or not.
     * 
     * @return <code>true</code> if the injector is valid (ready),
     *         <code>false</code> otherwise.
     */
    @Override
    public boolean isValid() {
        return this.managed != null;
    }

    /**
     * Callback called by the container when all injectors are valid.
     */
    @Override
    public void validate() {
    }

    /**
     * Callback called by the container when at least one injector becomes
     * invalid.
     */
    @Override
    public void invalidate() {
    }

    private void injectService(final Field field) {
        Inject annotation = field.getAnnotation(Inject.class);
        if (annotation == null) {
            throw new IllegalArgumentException("Given field " + field.getName()
                    + " is not annotated with " + Inject.class.getName());
        }

        String action = annotation.action();
        boolean restrictToThisPackage = annotation.restrictToThisPackage();
        String className = annotation.className();
        int flags = annotation.flags();
        String type = annotation.type();

        Intent intent = new Intent();
        if (action != null && !"".equals(action.trim())) {
            intent.setAction(action);
        }
        if (restrictToThisPackage) {
            intent.setPackage(context.getPackageName());
        }
        if (className != null && !"".equals(className.trim())) {
            intent.setClassName(context, className);
        }
        if (type != null && !"".equals(type.trim())) {
            intent.setType(type);
        }
        if (flags != 0) {
            intent.setFlags(flags);
        }

        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                if (field.getType().isAssignableFrom(service.getClass())) {
                    try {
                        field.setAccessible(true);
                        field.set(managed, service);
                        fieldInjections.put(field, true);
                    }
                    catch (Exception e) {
                        // TODO: better error message
                        throw new RuntimeException("Unable to inject service", e);
                    }

                    for (Boolean injected : fieldInjections.values()) {
                        if (!Boolean.TRUE.equals(injected)) {
                            return;
                        }
                    }

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... arg0) {
                            invokeServicesConnectedLifeCycle();
                            return null;
                        }

                        protected void onPostExecute(Void result) {
                            state = InjectorState.READY;
                            container.update();
                        };
                    }.execute();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                try {
                    field.set(managed, null);
                }
                catch (IllegalArgumentException e) {
                }
                catch (IllegalAccessException e) {
                }
                ServiceInjector.this.managed = null;
            }
        };
        serviceConnections.add(serviceConnection);
        context.bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
    }

    private void invokeServicesConnectedLifeCycle() {
        if (managed instanceof RobojectLifecycle) {
            ((RobojectLifecycle) managed).onServicesConnected();
        }
    }

    @Override
    public InjectorState getState() {
        return this.state;
    }
}
