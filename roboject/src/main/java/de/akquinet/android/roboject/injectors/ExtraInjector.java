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

import android.os.Bundle;
import android.os.Parcelable;
import de.akquinet.android.roboject.annotations.InjectExtra;
import de.akquinet.android.roboject.util.ReflectionUtil;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class ExtraInjector implements Injector {
    private final Bundle extras;
    private final Object managed;

    public ExtraInjector(Object managed, Bundle extras) {
        this.extras = extras;
        this.managed = managed;
    }

    /**
     * Inject an extra to the given field, using the value of the given
     * annotation as extra id. If the annotation has no value, then use the
     * field name as extra id.
     */
    private void injectExtra(Field field, InjectExtra annotation) {
        String value = annotation.value();

        if (InjectExtra.DEFAULT_VALUE.equals(value)) {
            value = field.getName();
        }

        Class<?> type = field.getType();
        Object extra = null;
        try {
            if (String.class.isAssignableFrom(type)) {
                extra = extras.getString(value);
            } else if (ArrayList.class.isAssignableFrom(type)) {
                extra = extras.getStringArrayList(value);
                if (extra == null) {
                    extra = extras.getParcelableArrayList(value);
                }
            } else if (boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type)) {
                extra = extras.getBoolean(value, false);
            } else if (Bundle.class.isAssignableFrom(type)) {
                extra = extras.getBundle(value);
            } else if (byte.class.isAssignableFrom(type) || Byte.class.isAssignableFrom(type)) {
                extra = extras.getByte(value, (byte) 0);
            } else if (char.class.isAssignableFrom(type) || Character.class.isAssignableFrom(type)) {
                extra = extras.getChar(value, (char) 0);
            } else if (CharSequence.class.isAssignableFrom(type)) {
                extra = extras.getCharSequence(value);
            } else if (double.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type)) {
                extra = extras.getDouble(value, 0d);
            } else if (float.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type)) {
                extra = extras.getFloat(value, 0f);
            } else if (int.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type)) {
                extra = extras.getInt(value, 0);
            } else if (long.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type)) {
                extra = extras.getLong(value, 0l);
            } else if (Parcelable.class.isAssignableFrom(type)) {
                extra = extras.getParcelable(value);
            } else if (Serializable.class.isAssignableFrom(type)) {
                extra = extras.getSerializable(value);
            } else if (short.class.isAssignableFrom(type) || Short.class.isAssignableFrom(type)) {
                extra = extras.getShort(value, (short) 0);
            } else if (type.isArray()) {
                Class<?> componentType = type.getComponentType();
                if (String.class.isAssignableFrom(componentType)) {
                    extra = extras.getStringArray(value);
                } else if (Parcelable.class.isAssignableFrom(componentType)) {
                    extra = extras.getParcelableArray(value);
                } else if (boolean.class.isAssignableFrom(type)
                        || Boolean.class.isAssignableFrom(componentType)) {
                    extra = extras.getBooleanArray(value);
                } else if (byte.class.isAssignableFrom(type)
                        || Byte.class.isAssignableFrom(componentType)) {
                    extra = extras.getByteArray(value);
                } else if (char.class.isAssignableFrom(type)
                        || Character.class.isAssignableFrom(componentType)) {
                    extra = extras.getCharArray(value);
                } else if (double.class.isAssignableFrom(type)
                        || Double.class.isAssignableFrom(componentType)) {
                    extra = extras.getDoubleArray(value);
                } else if (float.class.isAssignableFrom(type)
                        || Float.class.isAssignableFrom(componentType)) {
                    extra = extras.getFloatArray(value);
                } else if (int.class.isAssignableFrom(type)
                        || Integer.class.isAssignableFrom(componentType)) {
                    extra = extras.getIntArray(value);
                } else if (long.class.isAssignableFrom(type)
                        || Long.class.isAssignableFrom(componentType)) {
                    extra = extras.getLongArray(value);
                } else if (short.class.isAssignableFrom(type)
                        || Short.class.isAssignableFrom(componentType)) {
                    extra = extras.getShortArray(value);
                }
            }

            if (extra == null) {
                String message = "Could not inject a suitable extra"
                        + " for field " + field.getName() + "of instance of type "
                        + field.getDeclaringClass().getCanonicalName() + "."
                        + " No such extra could be found.";
                if (annotation.mandatory()) {
                    throw new RuntimeException(message);
                } else {
                    // Log.i(getClass().getCanonicalName(), message);
                    return;
                }
            }

            field.setAccessible(true);
            field.set(managed, extra);
        } catch (Exception e) {
            throw new RuntimeException("Could not inject a suitable extra"
                    + " for field " + field.getName() + "of instance of type "
                    + field.getDeclaringClass().getCanonicalName(), e);
        }
    }

    @Override
    public void inject() {
        List<Field> fields = ReflectionUtil.getFields(managed.getClass());
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof InjectExtra) {
                    injectExtra(field, (InjectExtra) annotation);
                }
            }
        }
    }
}
