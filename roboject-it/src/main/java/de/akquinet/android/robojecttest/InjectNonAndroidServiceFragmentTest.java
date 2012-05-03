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

import android.os.Build;
import de.akquinet.android.marvin.ActivityTestCase;
import de.akquinet.android.marvin.AndroidTestCase;
import de.akquinet.android.robojecttest.activities.DummyFragmentActivity;
import de.akquinet.android.robojecttest.fragments.InjectNonAndroidServiceTestFragment;

import static org.hamcrest.CoreMatchers.*;


public class InjectNonAndroidServiceFragmentTest extends AndroidTestCase {
    public void testInjectLocalService() throws Exception {
        // skip test for pre Honeycomb devices
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
            return;

        DummyFragmentActivity dummyFragmentActivity = startActivity(DummyFragmentActivity.class);

        Thread.sleep(2000);

        InjectNonAndroidServiceTestFragment fragment = (InjectNonAndroidServiceTestFragment) dummyFragmentActivity.getFragmentManager().findFragmentById(R.id.nonAndroidServiceFragment);

        assertThat(fragment.adder1, notNullValue());
        assertThat(fragment.adder1.add(2, 3), is(equalTo(5)));

        assertThat(fragment.adder2, notNullValue());
        assertThat(fragment.adder2.add(2, 3), is(equalTo(5)));
    }
}
