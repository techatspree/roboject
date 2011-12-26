/*

This file is part of Roboject

Copyright (c) 2010-2011 akquinet A.G.

Contact:  http://www.akquinet.de/en

GNU General Public License Usage
This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

If you are unsure which license is appropriate for your use, please contact the sales department at http://www.akquinet.de/en.

*/
package de.akquinet.android.robojecttest.activities;

import android.os.Bundle;
import android.util.Log;
import de.akquinet.android.roboject.RobojectActivity;
import de.akquinet.android.roboject.ServiceRegistry;
import de.akquinet.android.roboject.annotations.InjectService;
import de.akquinet.android.robojecttest.services.AdderImplementation;

public class InjectNonAndroidServiceTestActivity extends RobojectActivity {
    static {
        ServiceRegistry.registerService(AdderInterface.class, AdderImplementation.class);
   }

    @InjectService
    public AdderInterface adder1;

    public AdderInterface adder2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adder2 = ServiceRegistry.getService(AdderInterface.class);
    }

    public interface AdderInterface {
        int add(int... params);
    }
}
