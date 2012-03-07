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

        throw new RuntimeException("No suitable type found for " + field.getName() + "of class " + type.getName());
    }

    public static Object getResource(Activity activity, Field field, int value) {
        Resources resources = activity.getResources();
        Class type = field.getType();

        if (type.isAssignableFrom(Boolean.TYPE) || type.isAssignableFrom(Boolean.class))
            return resources.getBoolean(value);
        else if (type.isAssignableFrom(Integer.TYPE) || type.isAssignableFrom(Integer.class)) {
            return resources.getInteger(value);
        } else if (type.isAssignableFrom(ColorStateList.class))
            return resources.getColorStateList(value);
        else if (type.isAssignableFrom(XmlResourceParser.class))
            return resources.getXml(value);
        else if (type.isAssignableFrom(Float.TYPE) || type.isAssignableFrom(Float.class))
            return resources.getDimension(value);
        else if (type.isAssignableFrom(Drawable.class))
            return resources.getDrawable(value);
        else if (type.isAssignableFrom(Animation.class))
            return AnimationUtils.loadAnimation(activity, value);
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
