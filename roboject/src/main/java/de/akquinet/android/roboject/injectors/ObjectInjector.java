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
import android.app.Application;
import android.content.Context;
import de.akquinet.android.roboject.Container;
import de.akquinet.android.roboject.RobojectApplication;
import de.akquinet.android.roboject.RobojectException;
import de.akquinet.android.roboject.annotations.InjectObject;
import de.akquinet.android.roboject.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class ObjectInjector implements Injector {

    private Activity activity;
    private RobojectApplication application;
    private InjectorState state = InjectorState.CREATED;

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
        if (managed instanceof Activity) {
            this.activity = (Activity) context;
            if (this.activity.getApplication() instanceof RobojectApplication) {
                this.application = (RobojectApplication) this.activity.getApplication();
                return true;
            }
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
        return this.activity != null && this.application != null;
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
        List<Field> fields = ReflectionUtil.getAnnotatedFields(activity.getClass(), InjectObject.class);
        for (Field field : fields) {
            InjectObject annotation = field.getAnnotation(InjectObject.class);
            String key = annotation.value();
            if (key == null || key == "")
                key = field.getName();

            Object value = application.retrieveData(key);
            if (value == null)
                throw new RuntimeException("Error initializing field " + field.getName() + ". No matching value found.");

            try {
                field.setAccessible(true);
                field.set(activity, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Could not inject a suitable object"
                        + " for field " + field.getName() + " of type "
                        + field.getType(), e);
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
