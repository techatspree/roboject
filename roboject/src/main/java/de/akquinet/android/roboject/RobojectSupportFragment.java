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

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;


public class RobojectSupportFragment extends Fragment implements RobojectLifecycle, ServiceConnection {
    private Container container;

    /**
     * Contract for subclasses: You need to call super before relying on
     * injections in {@link #onCreate(android.os.Bundle)}.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createContainer();

        this.container.invokeCreatePhase();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.container.invokeResumePhase();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.container.invokeStopPhase();
    }

    @Override
    public void onServicesConnected() {
    }

    /**
     * Attaches an arbitrary object to an {@link android.content.Intent}. This works like an
     * intent extra, but does not require the object to be serializable or
     * parcellable. The object is injected to the activity matching the intent,
     * if that activity uses a matching {@link de.akquinet.android.roboject.annotations.InjectObject} annotation.
     *
     * @param intent the intent matching the target activity to which we want to
     *               pass the object
     * @param key    an arbitrary String used as the intent extra key
     * @param value
     */
    protected void putObjectIntentExtra(Intent intent, String key, Object value) {
        container.putObjectIntentExtra(intent, key, value);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder serviceObject) {
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    }

    private void createContainer() {
        if (this.container == null) {
            try {
                // Both the Android Support Library and ActionbarSherlock define
                // getActivity() inside Fragment, but with different return types
                // (FragmentActivity vs. Activity).
                // That's why we call the method by reflection and cast the result to Activity.

                Method getActivity = getClass().getMethod("getActivity", new Class[0]);
                Object activity = getActivity.invoke(this, new Object[0]);
                if (!(activity instanceof Activity)) {
                    throw new RuntimeException("getActivity() returned null");
                }

                this.container = new Container((Activity) activity, this, getClass());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
