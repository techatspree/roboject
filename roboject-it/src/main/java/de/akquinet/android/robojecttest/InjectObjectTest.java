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
package de.akquinet.android.robojecttest;

import de.akquinet.android.marvin.ActivityTestCase;
import de.akquinet.android.robojecttest.activities.InjectObjectTestActivityA;
import de.akquinet.android.robojecttest.activities.InjectObjectTestActivityB;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;


public class InjectObjectTest extends ActivityTestCase<InjectObjectTestActivityA> {
    public InjectObjectTest() {
        super(InjectObjectTestActivityA.class);
    }

    public void testInjectObject() throws Exception {
        int someInt = 261;

        getActivity().startSecondActivity(someInt);
        InjectObjectTestActivityB activityB =
                waitForActivity(InjectObjectTestActivityB.class, 10, TimeUnit.SECONDS);

        assertThat(activityB, notNullValue());

        assertThat(activityB.objectExtra, notNullValue());

        assertThat(activityB.objectExtra.value, is(someInt));
    }
}
