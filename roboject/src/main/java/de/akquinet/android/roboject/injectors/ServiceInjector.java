/*

This file is part of Roboject

Copyright (c) 2010-2011 akquinet A.G.

Contact:  http://www.akquinet.de/en

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package de.akquinet.android.roboject.injectors;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import de.akquinet.android.roboject.Container;
import de.akquinet.android.roboject.RobojectException;
import de.akquinet.android.roboject.RobojectLifecycle;
import de.akquinet.android.roboject.ServiceRegistry;
import de.akquinet.android.roboject.annotations.InjectService;
import de.akquinet.android.roboject.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.*;

import static de.akquinet.android.roboject.util.ReflectionUtil.isObjectInstanceof;


public class ServiceInjector implements Injector {
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
     * @param context   the android context
     * @param container the roboject container
     * @param managed   the managed instance
     * @param clazz     the managed class (the class of <tt>managed</tt>)
     * @return <code>true</code> if the injector wants to contribute to the
     *         management of the instance, <code>false</code> otherwise. In this
     *         latter case, the injector will be ignored for this instance.
     * @throws RobojectException if the configuration failed.
     */
    @Override
    public boolean configure(Context context, Container container,
                             Object managed, Class<?> clazz) throws RobojectException {
        this.context = context;
        this.managed = managed;
        this.container = container;

        if (managed instanceof Activity) {
            return true;
        }

        if (isObjectInstanceof(managed, "android.support.v4.app.Fragment")) {
            return true;
        }

        if (isObjectInstanceof(managed, "android.app.Fragment")) {
            return true;
        }

        return false;
    }

    /**
     * Method called by the container when all injectors are configured
     * (immediately after configure). This method is called on valid injector
     * only. In this method, the injector can injects field and call callbacks
     * (however, callbacks may wait the validate call).
     *
     * @param context   the android context
     * @param container the roboject container
     * @param managed   the managed instance
     */

    @Override
    public void start(Context context, Container container, Object managed) {
        this.state = InjectorState.STARTED;
        List<Field> fields = ReflectionUtil.getAnnotatedFields(managed.getClass(), InjectService.class);

        for (Field field : fields) {
            fieldInjections.put(field, false);
        }
    }

    /**
     * Method called by the container when the container is disposed. This
     * method is called on valid injector only. In this method, the injector can
     * free resources
     *
     * @param context the android context
     * @param managed the managed instance
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
        InjectService annotation = field.getAnnotation(InjectService.class);
        if (annotation == null) {
            throw new IllegalArgumentException("Given field " + field.getName()
                    + " is not annotated with " + InjectService.class.getName());
        }

        // Try to inject a non-android service first...
        {
            Class<?> clazz = annotation.clazz();
            if (Object.class.equals(clazz)) {
                // Clazz annotation parameter not set.
                // Use the type of the field.

                clazz = field.getType();
            }

            Object service = ServiceRegistry.getService(clazz);
            if (service != null) {
                assign(field, service);
                return;
            }
        }

        String action = annotation.intentAction();
        boolean restrictToThisPackage = annotation.packagePrivate();
        String className = annotation.clazz().getName();
        int flags = annotation.intentFlags();
        String type = annotation.intentType();

        Intent intent = new Intent();
        if (action != null && !"".equals(action.trim())) {
            intent.setAction(action);
        }
        if (restrictToThisPackage) {
            intent.setPackage(context.getPackageName());
        }
        if (className != null
                && !className.equals(Object.class.getName())
                && !"".equals(className.trim())) {
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
                assign(field, service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                serviceConnections.remove(this);
                state = InjectorState.CREATED;

                try {
                    field.set(managed, null);
                } catch (IllegalArgumentException e) {
                } catch (IllegalAccessException e) {
                }
                ServiceInjector.this.managed = null;
            }
        };
        serviceConnections.add(serviceConnection);
        context.getApplicationContext().bindService(intent, serviceConnection,
                Service.BIND_AUTO_CREATE);
    }

    private void assign(Field field, Object service) {
        Object binder = service;

        if (!field.getType().isAssignableFrom(binder.getClass())) {
            try {
                Class<?>[] classes = field.getType().getClasses();
                binder = classes[0].getDeclaredMethod("asInterface", IBinder.class).invoke(null, service);
            } catch (Exception e) {
                throw new RuntimeException("Unable to inject service", e);
            }
        }

        if (field.getType().isAssignableFrom(binder.getClass())) {
            Log.v("ServiceInjector", "Field is assignable");

            try {
                field.setAccessible(true);
                field.set(managed, binder);
                fieldInjections.put(field, true);
            } catch (Exception e) {
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
                Log.v("ServiceInjector", "Invoking services connected life cycle");
                invokeServicesConnectedLifeCycle();
                return null;
            }

            protected void onPostExecute(Void result) {
                state = InjectorState.READY;
                container.update();
            }

            ;
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
        } catch (Exception e) {
            // We were unable to unbind, e.g. because no such service binding
            // exists. This should be rare, but is possible, e.g. if the
            // service was killed by Android in the meantime.
            // We ignore this.
        }
    }
}
