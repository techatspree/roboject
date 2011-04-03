package de.akquinet.android.roboject.util;

import java.lang.reflect.Field;

import android.content.Context;


public class AndroidUtil
{
    /**
     * Check application's R.id class and retrieve the value of the static
     * member with the given name.
     */
    public static int getIdFromR(Context context, String idName) {
        Class<?> rIdClass = null;
        try {
            rIdClass = Class.forName(context.getPackageName() + ".R$id");
        }
        catch (ClassNotFoundException e) {
            // No R.id class? This should never happen.
            throw new RuntimeException(e);
        }

        try {
            Field rField = rIdClass.getField(idName);
            Object intValue;
            try {
                intValue = rField.get(null);
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (!(intValue instanceof Integer)) {
                throw new RuntimeException("Not an int: "
                        + rIdClass.getCanonicalName() + "." + rField.getName());
            }
            return ((Integer) intValue).intValue();
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException("There is no such id in the R class: "
                    + context.getPackageName() + ".R.id" + "." + idName + ")");
        }
        catch (SecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
