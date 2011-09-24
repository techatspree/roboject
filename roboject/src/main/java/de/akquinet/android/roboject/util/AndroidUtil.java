/*

This file is part of Roboject

Copyright (c) 2010-2011 akquinet A.G.

Contact:  http://www.akquinet.de/en

GNU General Public License Usage
This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

If you are unsure which license is appropriate for your use, please contact the sales department at http://www.akquinet.de/en.

*/
package de.akquinet.android.roboject.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import junit.framework.Test;

import java.lang.reflect.Field;


public class AndroidUtil {
    /**
     * Check application's R.id class and retrieve the value of the static
     * member with the given name.
     */
    public static int getIdentifierFromR(Context context, String type, String name) {
        Class<?> rClass = null;
        try {
            rClass = Class.forName(context.getPackageName() + ".R$" + type);
        } catch (ClassNotFoundException e) {
            // No R.id class? This should never happen.
            throw new RuntimeException(e);
        }

        try {
            Field rField = rClass.getField(name);
            Object intValue;
            try {
                intValue = rField.get(null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (!(intValue instanceof Integer)) {
                throw new RuntimeException("Not an int: "
                        + rClass.getCanonicalName() + "." + rField.getName());
            }
            return ((Integer) intValue).intValue();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("There is no such id in the R class: "
                    + context.getPackageName() + ".R." + type + "." + name + ")");
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getTypeForField(Field field) {
        Class type = field.getType();
        // Animation
        if (type.isAssignableFrom(Animation.class))
            return "anim";
            // Color State List
        else if (type.isAssignableFrom(ColorStateList.class))
            return "color";
            // Drawable
        else if (type.isAssignableFrom(Drawable.class))
            return "drawable";
            // String
        else if (type.isAssignableFrom(String.class))
            return "string";
            // String Array
        else if (type.isArray())
            return "array";
            // TODO: Recognize plural strings if possible
//        else if (type.isAssignableFrom(String.class))
//            return "plural";
            // Boolean
        else if (type.isAssignableFrom(Boolean.TYPE) || type.isAssignableFrom(Boolean.class))
            return "bool";
            // Integer
        else if (type.isAssignableFrom(Integer.TYPE) || type.isAssignableFrom(Integer.class))
            return "integer";
            // Dimen
        else if (type.isAssignableFrom(Float.TYPE) || type.isAssignableFrom(Float.class))
            return "dimen";

        return "";
    }

    public static Object getResource(Activity activity, Field field, int value) {
        Resources resources = activity.getResources();
        Class type = field.getType();

        if (type.isAssignableFrom(Boolean.TYPE) || type.isAssignableFrom(Boolean.class))
            return resources.getBoolean(value);
        else if (type.isAssignableFrom(Integer.TYPE) || type.isAssignableFrom(Integer.class)) {
            Integer result;
            try {
                result = resources.getInteger(value);
            } catch (Resources.NotFoundException e1) {
                try {
                    result = resources.getDimensionPixelOffset(value);
                } catch (Resources.NotFoundException e2) {
                    try {
                        result = resources.getDimensionPixelSize(value);
                    } catch (Resources.NotFoundException e3) {
                        result = resources.getColor(value);
                    }
                }
            }
            return result;
        } else if (type.isAssignableFrom(ColorStateList.class))
            return resources.getColorStateList(value);
        else if (type.isAssignableFrom(XmlResourceParser.class))
            return resources.getXml(value);
        else if (type.isAssignableFrom(Float.TYPE) || type.isAssignableFrom(Float.class))
            return resources.getDimension(value);
        else if (type.isAssignableFrom(Drawable.class))
            return resources.getDrawable(value);
        else if (type.isAssignableFrom(Integer[].class))
            return resources.getIntArray(value);
        else if (type.isAssignableFrom(Animation.class))
            return AnimationUtils.loadAnimation(activity, value);
        else if (type.isAssignableFrom(Movie.class))
            return resources.getMovie(value);
        else if (type.isAssignableFrom(Movie.class))
            return resources.getMovie(value);
        else if (type.isAssignableFrom(String.class))
            return resources.getString(value);
        else if (type.isArray()) {
            if (type.getName().equals("[I")) {
                return resources.getIntArray(value);
            } else if (type.isAssignableFrom(String[].class)) {
                return resources.getStringArray(value);
            }
        }

        return null;
    }
}
