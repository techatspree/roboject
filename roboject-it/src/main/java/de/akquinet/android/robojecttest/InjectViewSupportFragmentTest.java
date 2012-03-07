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
import de.akquinet.android.robojecttest.fragments.InjectViewTestSupportFragment;

import static org.hamcrest.CoreMatchers.notNullValue;


public class InjectViewSupportFragmentTest extends ActivityTestCase<DummySupportFragmentActivity> {
    public InjectViewSupportFragmentTest() {
        super(DummySupportFragmentActivity.class);
    }

    public void testInjectViewsByMemberName() {
        InjectViewTestSupportFragment fragment = (InjectViewTestSupportFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.viewSupportFragment);

        assertThat(fragment.theTextView, notNullValue());
        assertThat(fragment.theImageView, notNullValue());
        assertThat(fragment.theLinearLayout, notNullValue());
    }

    public void testInjectViewsById() {
        InjectViewTestSupportFragment fragment = (InjectViewTestSupportFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.viewSupportFragment);

        assertThat(fragment.theTextViewExplicitId, notNullValue());
        assertThat(fragment.theImageViewExplicitId, notNullValue());
        assertThat(fragment.theLinearLayoutExplicitId, notNullValue());
    }
}
