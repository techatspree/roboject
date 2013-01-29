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
package de.akquinet.android.roboject;

import android.content.Context;
import android.content.Intent;
import de.akquinet.android.roboject.annotations.InjectObject;
import de.akquinet.android.roboject.injectors.Injector;
import de.akquinet.android.roboject.injectors.Injector.InjectorState;
import de.akquinet.android.roboject.util.IntentRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Container
{
    public enum LifeCyclePhase {
        CREATE, RESUME, STOP, SERVICES_CONNECTED, READY
    }

    private LifeCyclePhase currentPhase;

    private List<Injector> injectors;

    public Container(Context context, Object managed, Class<?> clazz) throws RobojectException {
        this.currentPhase = LifeCyclePhase.CREATE;
        this.injectors = RobojectConfiguration.getDefaultInjectors(managed);

        for (Injector injector : this.injectors) {
            injector.configure(context, this, managed, clazz);
        }

        for (Injector injector : this.injectors) {
            if (injector.isValid()) {
                injector.start(context, this, managed);
            }
        }
    }

    protected void invokeCreatePhase() {
        this.currentPhase = LifeCyclePhase.CREATE;

        for (Injector injector : this.injectors) {
            injector.onCreate();
        }
    }

    protected void invokeResumePhase() {
        this.currentPhase = LifeCyclePhase.RESUME;

        for (Injector injector : this.injectors) {
            injector.onResume();
        }
    }

    protected void invokeStopPhase() {
        this.currentPhase = LifeCyclePhase.STOP;

        for (Injector injector : this.injectors) {
            injector.onStop();
        }
    }

    /**
     * Attaches an arbitrary object to an {@link Intent}. This works like an
     * intent extra, but does not require the object to be serializable or
     * parcellable. The object is injected to the activity matching the intent,
     * if that activity uses a matching {@link InjectObject} annotation.
     *
     * @param intent the intent matching the target activity to which we want to
     *               pass the object
     * @param key    an arbitrary String used as the intent extra key
     * @param value
     */
    protected void putObjectIntentExtra(Intent intent, String key, Object value) {
        Map<String, Object> objectIntentExtras = IntentRegistry.getObjectIntentExtras(intent);
        if (objectIntentExtras == null || objectIntentExtras.isEmpty()) {
            objectIntentExtras = new HashMap<String, Object> ();
            IntentRegistry.putObjectIntentExtras(intent, objectIntentExtras);
        }

        objectIntentExtras.put(key, value);
    }

    public LifeCyclePhase getCurrentLifecyclePhase() {
        return this.currentPhase;
    }
}
