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
import android.os.Bundle;

import static de.akquinet.android.roboject.Roboject.*;


public class RobojectActivity extends Activity implements ServicesConnector {

    /**
     * Contract for subclasses: You need to call super before relying on
     * injections in {@link #onCreate(Bundle)}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injectLayout(this);
        injectExtras(this, getIntent().getExtras());
        injectResources(this, this);
        injectServices(this, this, this);
    }

    @Override
    public void onContentChanged() {
        injectViews(this, findViewById(android.R.id.content));
    }

    @Override
    public void onServicesConnected() {
    }
}
