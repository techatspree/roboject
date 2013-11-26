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

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import de.akquinet.android.marvin.ActivityTestCase;
import de.akquinet.android.roboject.annotations.InjectResource;
import de.akquinet.android.robojecttest.activities.InjectResourceTestActivity;

import static de.akquinet.android.roboject.Roboject.injectResources;
import static org.hamcrest.CoreMatchers.notNullValue;


public class InjectResourceCustomTest extends ActivityTestCase<InjectResourceTestActivity> {
    public InjectResourceCustomTest() {
        super(InjectResourceTestActivity.class);
    }

    public void testInjectResourceByMemberName() {
        ResourceObject resourceObject = new ResourceObject();
        injectResources(resourceObject, getActivity());

        assertThat(resourceObject.theString, notNullValue());
        assertThat(resourceObject.thecolorlist, notNullValue());
        assertThat(resourceObject.theInteger, notNullValue());
        assertThat(resourceObject.theanimation, notNullValue());
        assertThat(resourceObject.theDimen, notNullValue());
        assertThat(resourceObject.icon, notNullValue());
        assertThat(resourceObject.theStringArray, notNullValue());
        assertThat(resourceObject.theIntArray, notNullValue());

    }

    public void testInjectResourceById() {
        ResourceObject resourceObject = new ResourceObject();
        injectResources(resourceObject, getActivity());

        assertThat(resourceObject.theColor, notNullValue());
    }
}

class ResourceObject {
    @InjectResource String theString;
    @InjectResource ColorStateList thecolorlist;
    @InjectResource Integer theInteger;
    @InjectResource Boolean theBoolean;
    @InjectResource Animation theanimation;
    @InjectResource Float theDimen;
    @InjectResource Drawable icon;
    @InjectResource String[] theStringArray;
    @InjectResource int[] theIntArray;
    @InjectResource(type = "color", name = "theColor") Integer theColor;
}