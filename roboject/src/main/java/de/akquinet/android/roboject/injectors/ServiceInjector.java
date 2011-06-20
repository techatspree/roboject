/*

This file is part of Roboject

Copyright (c) 2010-2011 akquinet A.G.

Contact:  http://www.akquinet.de/en

GNU General Public License Usage
This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

If you are unsure which license is appropriate for your use, please contact the sales department at http://www.akquinet.de/en.

*/
package de.akquinet.android.roboject.injectors;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
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
    private List<ServiceConnection> serviceConnections =
            Collections.synchronizedList(new ArrayList<ServiceConnection>());

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

        for (Field field : fields) {
            fieldInjections.put(field, false);
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

                    done();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                serviceConnections.remove(this);
                state = InjectorState.CREATED;

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
        context.getApplicationContext().bindService(intent, serviceConnection,
                Service.BIND_AUTO_CREATE);
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

    private void done() {
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

    @Override
    public void onSetContentView() {
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onResume() {
        this.serviceConnections.clear();

        if (fieldInjections.isEmpty()) {
            // Nothing to inject. Proceed.
            done();
            return;
        }

        for (Field field : fieldInjections.keySet()) {
            fieldInjections.put(field, false);
        }

        for (Field field : fieldInjections.keySet()) {
            injectService(field);
        }
    }

    @Override
    public void onStop() {
        state = InjectorState.STARTED;
        Context appContext = context.getApplicationContext();
        for (ServiceConnection connection : serviceConnections) {
            unbindSafely(appContext, connection);
        }
        serviceConnections.clear();
    }

    private void unbindSafely(Context appContext, ServiceConnection connection) {
        try {
            appContext.unbindService(connection);
        }
        catch (Exception e) {
            // We were unable to unbind, e.g. because no such service binding
            // exists. This should be rare, but is possible, e.g. if the
            // service was killed by Android in the meantime.
            // We ignore this.
        }
    }
}
