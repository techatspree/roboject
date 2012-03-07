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
package de.akquinet.android.roboject;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class ServiceRegistry {
    private static final String TAG = ServiceRegistry.class.getSimpleName();
    private static Map<Class, Class> registry = new HashMap<Class, Class>();

    public static <Type> void registerService(Class<Type> interfaceClass, Class<? extends Type> implementationClass) {
        if (registry.get(interfaceClass) != null)
            Log.w(TAG, "An implementation for service " + interfaceClass.toString() + " is already registered.");
        registry.put(interfaceClass, implementationClass);
    }

    public static <Type> Type getService(Class<Type> interfaceClass) {
        Class<? extends Type> implementationClass = registry.get(interfaceClass);
        if (implementationClass != null) {
            try {
                return implementationClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Unable to create instance of " + implementationClass.toString(), e);
            }
        }

        return null;
    }
}
