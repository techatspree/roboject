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
package de.akquinet.android.robojecttest.fragments;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import de.akquinet.android.roboject.RobojectFragment;
import de.akquinet.android.roboject.annotations.InjectResource;
import de.akquinet.android.robojecttest.R;


public class InjectResourceTestFragment extends RobojectFragment {
    @InjectResource
    public String theString;

    @InjectResource
    public ColorStateList thecolorlist;

    @InjectResource
    public Integer theInteger;

    @InjectResource
    public Boolean theBoolean;

    @InjectResource
    public Animation theanimation;

    @InjectResource
    public Float theDimen;

    @InjectResource
    public Drawable icon;

    @InjectResource
    public String[] theStringArray;

    @InjectResource
    public int[] theIntArray;

    @InjectResource(type = "color", name = "theColor")
    public Integer theColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.viewinject, container, false);
        return inflate;
    }
}
