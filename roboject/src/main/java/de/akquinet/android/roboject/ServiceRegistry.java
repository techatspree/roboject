/*

This file is part of Roboject

Copyright (c) 2010-2011 akquinet A.G.

Contact:  http://www.akquinet.de/en

GNU General Public License Usage
This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

If you are unsure which license is appropriate for your use, please contact the sales department at http://www.akquinet.de/en.

*/
package de.akquinet.android.roboject;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class ServiceRegistry {
    private static final String TAG = "ServiceRegistry";
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
        throw new RuntimeException("Unable to create instance of " + implementationClass.toString());
    }
}
