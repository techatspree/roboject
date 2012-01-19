/*

This file is part of Roboject

Copyright (c) 2010-2011 akquinet A.G.

Contact:  http://www.akquinet.de/en

GNU General Public License Usage
This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

If you are unsure which license is appropriate for your use, please contact the sales department at http://www.akquinet.de/en.

*/
package de.akquinet.android.roboject.injectors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import de.akquinet.android.roboject.Container;
import de.akquinet.android.roboject.RobojectException;
import de.akquinet.android.roboject.annotations.InjectExtra;
import de.akquinet.android.roboject.util.ReflectionUtil;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class IntentExtraInjector implements Injector {
    private Activity activity;
    private InjectorState state = InjectorState.CREATED;
    private Object managed;

    /**
     * Method called by the container to initialize the container.
     *
     * @param context   the android context
     * @param container the roboject container
     * @param managed   the managed instance
     * @param clazz     the managed class (the class of <tt>managed</tt>)
     * @return <code>true</code> if the injector wants to contribute to the
     *         management of the instance, <code>false</code> otherwise. In this
     *         latter case, the injector will be
     *         ignored for this instance.
     * @throws RobojectException if the configuration failed.
     */
    @Override
    public boolean configure(Context context, Container container, Object managed, Class<?> clazz)
            throws RobojectException {
        if (context instanceof Activity) {
            this.activity = (Activity) context;
        } else {
            return false;
        }

        this.managed = managed;

        if (managed instanceof Activity) {
            return true;
        }

        if (managed instanceof Fragment) {
            return true;
        }

        if (managed instanceof android.app.Fragment) {
            return true;
        }

        return false;
    }

    /**
     * Method called by the container when all injectors are configured
     * (immediately
     * after configure). This method is called on valid injector only.
     * In this method, the injector can injects field and call callbacks
     * (however, callbacks may wait the validate call).
     *
     * @param context   the android context
     * @param container the roboject container
     * @param managed   the managed instance
     */

    @Override
    public void start(Context context, Container container, Object managed) {
        this.state = InjectorState.STARTED;

        List<Field> fields = ReflectionUtil.getFields(activity.getClass());
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof InjectExtra) {
                    injectExtra(field, (InjectExtra) annotation);
                }
            }
        }

        this.state = InjectorState.READY;
    }

    /**
     * Method called by the container when the container is disposed. This
     * method is
     * called on valid injector only.
     * In this method, the injector can free resources
     *
     * @param context the android context
     * @param managed the managed instance
     */
    @Override
    public void stop(Context context, Object managed) {
        this.activity = null;
    }

    /**
     * Checks whether the injector is valid or not.
     *
     * @return <code>true</code> if the injector is valid (ready),
     *         <code>false</code> otherwise.
     */
    @Override
    public boolean isValid() {
        return this.activity != null;
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

    /**
     * Inject an extra to the given field, using the value of the given
     * annotation as extra id. If the annotation has no value, then use the
     * field name as extra id.
     */
    private void injectExtra(Field field, InjectExtra annotation) {
        String value = annotation.value();
        Intent intent = activity.getIntent();

        if (InjectExtra.DEFAULT_VALUE.equals(value)) {
            value = field.getName();
        }

        Class<?> type = field.getType();
        Object extra = null;
        try {
            if (String.class.isAssignableFrom(type)) {
                extra = intent.getStringExtra(value);
            } else if (ArrayList.class.isAssignableFrom(type)) {
                extra = intent.getStringArrayListExtra(value);
                if (extra == null) {
                    extra = intent.getParcelableArrayListExtra(value);
                }
            } else if (boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type)) {
                extra = intent.getBooleanExtra(value, false);
            } else if (Bundle.class.isAssignableFrom(type)) {
                extra = intent.getBundleExtra(value);
            } else if (byte.class.isAssignableFrom(type) || Byte.class.isAssignableFrom(type)) {
                extra = intent.getByteExtra(value, (byte) 0);
            } else if (char.class.isAssignableFrom(type) || Character.class.isAssignableFrom(type)) {
                extra = intent.getCharExtra(value, (char) 0);
            } else if (CharSequence.class.isAssignableFrom(type)) {
                extra = intent.getCharSequenceExtra(value);
            } else if (double.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type)) {
                extra = intent.getDoubleExtra(value, 0d);
            } else if (float.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type)) {
                extra = intent.getFloatExtra(value, 0f);
            } else if (int.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type)) {
                extra = intent.getIntExtra(value, 0);
            } else if (long.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type)) {
                extra = intent.getLongExtra(value, 0l);
            } else if (Parcelable.class.isAssignableFrom(type)) {
                extra = intent.getParcelableExtra(value);
            } else if (Serializable.class.isAssignableFrom(type)) {
                extra = intent.getSerializableExtra(value);
            } else if (short.class.isAssignableFrom(type) || Short.class.isAssignableFrom(type)) {
                extra = intent.getShortExtra(value, (short) 0);
            } else if (type.isArray()) {
                Class<?> componentType = type.getComponentType();
                if (String.class.isAssignableFrom(componentType)) {
                    extra = intent.getStringArrayExtra(value);
                } else if (Parcelable.class.isAssignableFrom(componentType)) {
                    extra = intent.getParcelableArrayExtra(value);
                } else if (boolean.class.isAssignableFrom(type)
                        || Boolean.class.isAssignableFrom(componentType)) {
                    extra = intent.getBooleanArrayExtra(value);
                } else if (byte.class.isAssignableFrom(type)
                        || Byte.class.isAssignableFrom(componentType)) {
                    extra = intent.getByteArrayExtra(value);
                } else if (char.class.isAssignableFrom(type)
                        || Character.class.isAssignableFrom(componentType)) {
                    extra = intent.getCharArrayExtra(value);
                } else if (double.class.isAssignableFrom(type)
                        || Double.class.isAssignableFrom(componentType)) {
                    extra = intent.getDoubleArrayExtra(value);
                } else if (float.class.isAssignableFrom(type)
                        || Float.class.isAssignableFrom(componentType)) {
                    extra = intent.getFloatArrayExtra(value);
                } else if (int.class.isAssignableFrom(type)
                        || Integer.class.isAssignableFrom(componentType)) {
                    extra = intent.getIntArrayExtra(value);
                } else if (long.class.isAssignableFrom(type)
                        || Long.class.isAssignableFrom(componentType)) {
                    extra = intent.getLongArrayExtra(value);
                } else if (short.class.isAssignableFrom(type)
                        || Short.class.isAssignableFrom(componentType)) {
                    extra = intent.getShortArrayExtra(value);
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
    public InjectorState getState() {
        return this.state;
    }

    @Override
    public void onSetContentView() {
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onStop() {
    }
}
