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
import de.akquinet.android.robojecttest.fragments.InjectResourceTestFragment;

import static org.hamcrest.CoreMatchers.notNullValue;


public class InjectResourceFragmentTest extends AndroidTestCase {
    public void testInjectResourceByMemberName() {
        // skip test for pre Honeycomb devices
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
            return;

        DummyFragmentActivity dummyFragmentActivity = perform().startActivity(DummyFragmentActivity.class);

        InjectResourceTestFragment fragment = (InjectResourceTestFragment) dummyFragmentActivity.getFragmentManager().findFragmentById(R.id.resourceFragment);

        assertThat(fragment.theString, notNullValue());
        assertThat(fragment.thecolorlist, notNullValue());
        assertThat(fragment.theInteger, notNullValue());
        assertThat(fragment.theanimation, notNullValue());
        assertThat(fragment.theDimen, notNullValue());
        assertThat(fragment.icon, notNullValue());
        assertThat(fragment.theStringArray, notNullValue());
        assertThat(fragment.theIntArray, notNullValue());
    }

    public void testInjectResourceById() {
        // skip test for pre Honeycomb devices
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
            return;

        DummyFragmentActivity dummyFragmentActivity = perform().startActivity(DummyFragmentActivity.class);

        InjectResourceTestFragment fragment = (InjectResourceTestFragment) dummyFragmentActivity.getFragmentManager().findFragmentById(R.id.resourceFragment);

        assertThat(fragment.theColor, notNullValue());
    }
}
