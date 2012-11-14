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
package de.akquinet.android.robojecttest.activities;

import android.os.Bundle;
import de.akquinet.android.roboject.RobojectActivity;
import de.akquinet.android.roboject.ServiceRegistry;
import de.akquinet.android.roboject.annotations.InjectService;
import de.akquinet.android.robojecttest.services.AdderImplementation;
import de.akquinet.android.robojecttest.services.AdderInterface;

public class InjectLocalServiceViaClassTestActivity extends RobojectActivity {
    static {
        ServiceRegistry.registerService(AdderInterface.class, AdderImplementation.class);
    }

    @InjectService
    public AdderInterface adder1;

    public AdderInterface adder2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adder2 = ServiceRegistry.getService(AdderInterface.class);
    }
}
