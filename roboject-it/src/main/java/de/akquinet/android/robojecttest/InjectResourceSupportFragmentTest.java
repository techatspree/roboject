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
import de.akquinet.android.robojecttest.activities.DummySupportFragmentActivity;
import de.akquinet.android.robojecttest.fragments.InjectResourceTestSupportFragment;

import static org.hamcrest.CoreMatchers.notNullValue;


public class InjectResourceSupportFragmentTest extends ActivityTestCase<DummySupportFragmentActivity> {
    public InjectResourceSupportFragmentTest() {
        super(DummySupportFragmentActivity.class);
    }

    public void testInjectResourceByMemberName() {
        InjectResourceTestSupportFragment fragment = (InjectResourceTestSupportFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.resourceSupportFragment);

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
        InjectResourceTestSupportFragment fragment = (InjectResourceTestSupportFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.resourceSupportFragment);
        
        assertThat(fragment.theColor, notNullValue());
    }
}
