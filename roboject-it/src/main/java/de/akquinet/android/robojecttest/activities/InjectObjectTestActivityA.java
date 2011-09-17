/*

This file is part of Roboject

Copyright (c) 2010-2011 akquinet A.G.

Contact:  http://www.akquinet.de/en

GNU General Public License Usage
This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

If you are unsure which license is appropriate for your use, please contact the sales department at http://www.akquinet.de/en.

*/
package de.akquinet.android.robojecttest.activities;

import android.content.Intent;
import android.os.Bundle;
import de.akquinet.android.roboject.RobojectActivity;
import de.akquinet.android.roboject.RobojectApplication;
import de.akquinet.android.robojecttest.R;

import java.util.HashSet;
import java.util.Set;


public class InjectObjectTestActivityA extends RobojectActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.viewinject);
        super.onCreate(savedInstanceState);
    }

    public void startSecondActivity() {
        RobojectApplication application = (RobojectApplication) getApplication();

        Intent intent = new Intent();
        intent.setClass(this, InjectObjectTestActivityB.class);

        Set<String> mySet = new HashSet<String>();
        mySet.add("Hello Android");
        application.storeData(InjectObjectTestActivityB.class, "mySet", mySet);
        application.storeData(InjectObjectTestActivityB.class, "mySet2", mySet);
        startActivity(intent);
    }
}
