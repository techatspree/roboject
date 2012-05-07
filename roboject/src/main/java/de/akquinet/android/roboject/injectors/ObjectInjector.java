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
import android.content.Context;
import de.akquinet.android.roboject.Container;
import de.akquinet.android.roboject.RobojectException;
import de.akquinet.android.roboject.annotations.InjectObject;
import de.akquinet.android.roboject.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static de.akquinet.android.roboject.util.IntentRegistry.getObjectIntentExtras;
import static de.akquinet.android.roboject.util.IntentRegistry.putObjectIntentExtras;
import static de.akquinet.android.roboject.util.ReflectionUtil.isObjectInstanceof;

public class ObjectInjector implements Injector {

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
     * @throws de.akquinet.android.roboject.RobojectException
     *          if the configuration failed.
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

    @Override
    public InjectorState getState() {
        return this.state;
    }

    @Override
    public void onSetContentView() {
    }

    @Override
    public void onCreate() {
        List<Field> fields = ReflectionUtil.getAnnotatedFields(managed.getClass(), InjectObject.class);
        for (Field field : fields) {
            InjectObject annotation = field.getAnnotation(InjectObject.class);
            String key = annotation.value();
            if (key == null || "".equals(key))
                key = field.getName();

            Map<String, Object> objectIntentExtras = getObjectIntentExtras(activity.getIntent());
            Object value = objectIntentExtras.get(key);
            if (value == null)
                throw new RuntimeException("Could not inject a suitable object"
                        + " for field " + field.getName() + " of type "
                        + field.getType());

            try {
                field.setAccessible(true);
                field.set(managed, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Could not inject a suitable object"
                        + " for field " + field.getName() + " of type "
                        + field.getType(), e);
            } finally {
                objectIntentExtras.remove(value);
                putObjectIntentExtras(activity.getIntent(), objectIntentExtras);
            }
        }
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onStop() {
    }
}
