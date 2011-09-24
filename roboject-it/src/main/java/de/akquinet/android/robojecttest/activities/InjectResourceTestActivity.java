/*

This file is part of Roboject

Copyright (c) 2010-2011 akquinet A.G.

Contact:  http://www.akquinet.de/en

GNU General Public License Usage
This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

If you are unsure which license is appropriate for your use, please contact the sales department at http://www.akquinet.de/en.

*/
package de.akquinet.android.robojecttest.activities;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.animation.Animation;
import de.akquinet.android.roboject.RobojectActivity;
import de.akquinet.android.roboject.annotations.InjectResource;
import de.akquinet.android.robojecttest.R;


public class InjectResourceTestActivity extends RobojectActivity
{
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.viewinject);
        super.onCreate(savedInstanceState);
    }
}
