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
import de.akquinet.android.robojecttest.activities.InjectResourceTestActivity;

import static org.hamcrest.CoreMatchers.notNullValue;


public class InjectResourceTest extends ActivityTestCase<InjectResourceTestActivity> {
    public InjectResourceTest() {
        super(InjectResourceTestActivity.class);
    }

    public void testInjectResourceByMemberName() {
        assertThat(getActivity().theString, notNullValue());
        assertThat(getActivity().thecolorlist, notNullValue());
        assertThat(getActivity().theInteger, notNullValue());
        assertThat(getActivity().theanimation, notNullValue());
        assertThat(getActivity().theDimen, notNullValue());
        assertThat(getActivity().icon, notNullValue());
        assertThat(getActivity().theStringArray, notNullValue());
        assertThat(getActivity().theIntArray, notNullValue());

    }

    public void testInjectResourceById() {
        assertThat(getActivity().theColor, notNullValue());
    }
}
