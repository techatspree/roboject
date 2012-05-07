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
import de.akquinet.android.roboject.injectors.*;

import java.util.ArrayList;
import java.util.List;

import static de.akquinet.android.roboject.util.ReflectionUtil.isObjectInstanceof;


public class RobojectConfiguration {

    public static List<Injector> getDefaultInjectors(Object managed) {
        List<Injector> result = new ArrayList<Injector>();

        if (managed instanceof Activity) {
            result.add(new ViewInjector());
            result.add(new ServiceInjector());
            result.add(new IntentExtraInjector());
            result.add(new LayoutInjector());
            result.add(new ObjectInjector());
            result.add(new ResourceInjector());
        }

        if (isObjectInstanceof(managed, "android.support.v4.app.Fragment")) {
            result.add(new ViewInjector());
            result.add(new ServiceInjector());
            result.add(new ResourceInjector());
            // TODO: These should work fine but are not currently tested.
            result.add(new IntentExtraInjector());
            result.add(new ObjectInjector());
        }

        if (isObjectInstanceof(managed, "android.app.Fragment")) {
            result.add(new ViewInjector());
            result.add(new ServiceInjector());
            result.add(new ResourceInjector());
            // TODO: These should work fine but are not currently tested.
            result.add(new IntentExtraInjector());
            result.add(new ObjectInjector());
        }

        return result;
    }
}
