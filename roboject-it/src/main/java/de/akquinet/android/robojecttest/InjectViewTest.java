/*
 * Copyright 2010 akquinet
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.akquinet.android.robojecttest;

import static org.hamcrest.CoreMatchers.notNullValue;
import de.akquinet.android.marvin.ActivityTestCase;
import de.akquinet.android.robojecttest.activities.InjectViewTestActivity;


public class InjectViewTest extends ActivityTestCase<InjectViewTestActivity>
{
    public InjectViewTest() {
        super(InjectViewTestActivity.class);
    }

    public void testInjectViewsByMemberName() {
        assertThat(getActivity().theTextView, notNullValue());
        assertThat(getActivity().theImageView, notNullValue());
        assertThat(getActivity().theLinearLayout, notNullValue());
    }

    public void testInjectViewsById() {
        assertThat(getActivity().theTextViewExplicitId, notNullValue());
        assertThat(getActivity().theImageViewExplicitId, notNullValue());
        assertThat(getActivity().theLinearLayoutExplicitId, notNullValue());
    }
}
