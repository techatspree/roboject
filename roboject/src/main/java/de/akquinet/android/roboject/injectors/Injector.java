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

import android.content.Context;
import de.akquinet.android.roboject.Container;
import de.akquinet.android.roboject.RobojectException;


public interface Injector {
    enum InjectorState {
        CREATED, STARTED, READY
    }

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
    boolean configure(Context context, Container container, Object managed,
                      Class<?> clazz) throws RobojectException;

    /**
     * Method called by the container when all injectors are configured
     * (immediately
     * after configure). This method is called on valid injector only.
     * In this method, the injector can injects field and call callbacks
     * (however,
     * callbacks may wait the validate call).
     *
     * @param context   the android context
     * @param container the roboject container
     * @param managed   the managed instance
     */
    void start(Context context, Container container, Object managed);

    void onSetContentView();

    void onCreate();

    void onResume();

    void onStop();

    /**
     * Method called by the container when the container is disposed. This
     * method is
     * called on valid injector only.
     * In this method, the injector can free resources
     *
     * @param context the android context
     * @param managed the managed instance
     */
    void stop(Context context, Object managed);

    /**
     * Checks whether the injector is valid or not.
     *
     * @return <code>true</code> if the injector is valid (ready),
     *         <code>false</code> otherwise.
     */
    boolean isValid();

    /**
     * Callback called by the container when all injectors are valid.
     */
    void validate();

    /**
     * Callback called by the container when at least one injector becomes
     * invalid.
     */
    void invalidate();

    // TODO Do we need the Android lifecycle callbacks ?

    InjectorState getState();
}
