/*

This file is part of Roboject

Copyright (c) 2010-2011 akquinet A.G.

Contact:  http://www.akquinet.de/en

GNU General Public License Usage
This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

If you are unsure which license is appropriate for your use, please contact the sales department at http://www.akquinet.de/en.

*/
package de.akquinet.android.robojecttest.activities;

import android.content.ComponentName;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import de.akquinet.android.roboject.RobojectActivity;
import de.akquinet.android.roboject.annotations.InjectService;
import de.akquinet.android.robojecttest.services.IRemoteService;
import de.akquinet.android.robojecttest.services.RemoteTestService;
import de.akquinet.android.robojecttest.services.TestService;
import de.akquinet.android.robojecttest.services.TestService.AdderService;


public class InjectRemoteServiceTestActivity extends RobojectActivity {
    @InjectService(clazz = RemoteTestService.class)
    public IRemoteService adderService;

    @Override
    public void onServiceConnected(ComponentName name, IBinder serviceObject) {
        super.onServiceConnected(name, serviceObject);
        try {
            Log.v("InjectRemoteServiceTestActivity", String.valueOf(adderService.add(22,20)));
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
