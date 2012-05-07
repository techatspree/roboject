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
import android.app.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * Utility class to do common reflection tasks.
 *
 * @author Philipp Kumar
 */
public class ReflectionUtil {

    public static boolean isObjectInstanceof(Object object, String classname) {
        if (object == null)
            return false;

        try {
            Class<?> aClass = Class.forName(classname);
            return aClass.isAssignableFrom(object.getClass());
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Checks the given class and all super classes for a certain class
     * annotation and returns the first one that matches. If no such annotation
     * is found, returns null.
     *
     * @param clazz           the class to check for the annotation
     * @param annotationClass the annotation type to look for
     * @return the annotation, if found, null otherwise
     */
    public static <T extends Annotation> T getAnnotation(
            Class<?> clazz, Class<T> annotationClass) {
        if (isTopMostClass(clazz)) {
            return null;
        }

        T annotation = clazz.getAnnotation(annotationClass);
        if (annotation != null) {
            return annotation;
        }
        return getAnnotation(clazz.getSuperclass(), annotationClass);
    }

    /**
     * Returns all declared fields of a class and all its super classes,
     * regardless of visibility modifier.
     */
    public static List<Field> getFields(Class<?> clazz) {
        List<Field> results = new ArrayList<Field>();
        getFields(results, clazz);
        return results;
    }

    public static List<Field> getAnnotatedFields(Class<?> clazz, Class<?> annotationType) {
        List<Field> allFields = getFields(clazz);

        List<Field> results = new ArrayList<Field>();
        for (Field field : allFields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().isAssignableFrom(annotationType)) {
                    results.add(field);
                }
            }
        }

        return results;
    }

    private static void getFields(List<Field> fields, Class<?> clazz) {
        if (isTopMostClass(clazz)) {
            return;
        }

        for (Field field : clazz.getDeclaredFields()) {
            fields.add(field);
        }
        getFields(fields, clazz.getSuperclass());
    }

    private static boolean isTopMostClass(Class<?> clazz) {
        return clazz == null || clazz.equals(Activity.class) || clazz.equals(Service.class)
                || clazz.equals(Object.class);
    }
}
