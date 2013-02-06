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

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import de.akquinet.android.roboject.ServiceRegistry;
import de.akquinet.android.roboject.ServicesConnector;
import de.akquinet.android.roboject.annotations.InjectService;
import de.akquinet.android.roboject.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.*;


public class ServiceInjector implements Injector {
    private final Context context;
    private final Object managed;
    private final ServicesConnector callback;

    private Map<Field, Boolean> fieldInjections = new HashMap<Field, Boolean>();
    private List<ServiceConnection> serviceConnections =
            Collections.synchronizedList(new ArrayList<ServiceConnection>());

    public ServiceInjector(Object managed, Context context, ServicesConnector callback) {
        this.callback = callback;
        this.managed = managed;
        this.context = context;
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
                try {
                    field.set(managed, null);
                } catch (IllegalArgumentException e) {
                } catch (IllegalAccessException e) {
                }
            }
        };
        serviceConnections.add(serviceConnection);
        context.getApplicationContext().bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
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
        if (callback != null)
            callback.onServicesConnected();
    }


    private void done() {
        invokeServicesConnectedLifeCycle();
    }

    @Override
    public void inject() {
        List<Field> fields = ReflectionUtil.getAnnotatedFields(managed.getClass(), InjectService.class);
        for (Field field : fields) {
            fieldInjections.put(field, false);
        }

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
}
