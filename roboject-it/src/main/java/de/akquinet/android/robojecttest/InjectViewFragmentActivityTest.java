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
import de.akquinet.android.robojecttest.activities.InjectViewTestFragmentActivity;
import de.akquinet.android.robojecttest.fragments.InjectViewTestSupportFragment;

import static org.hamcrest.CoreMatchers.notNullValue;


public class InjectViewFragmentActivityTest extends ActivityTestCase<InjectViewTestFragmentActivity>
{
    public InjectViewFragmentActivityTest() {
        super(InjectViewTestFragmentActivity.class);
    }

    public void testInjectViewsByMemberName() {
        InjectViewTestSupportFragment fragment = (InjectViewTestSupportFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.viewFragment);

        assertThat(getActivity().theTextView, notNullValue());
        assertThat(getActivity().theImageView, notNullValue());
        assertThat(getActivity().theLinearLayout, notNullValue());
    }

    public void testInjectViewsById() {
        InjectViewTestSupportFragment fragment = (InjectViewTestSupportFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.viewFragment);

        assertThat(getActivity().theTextViewExplicitId, notNullValue());
        assertThat(getActivity().theImageViewExplicitId, notNullValue());
        assertThat(getActivity().theLinearLayoutExplicitId, notNullValue());
    }
}
