/*

This file is part of Roboject

Copyright (c) 2010-2011 akquinet A.G.

Contact:  http://www.akquinet.de/en

GNU General Public License Usage
This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

If you are unsure which license is appropriate for your use, please contact the sales department at http://www.akquinet.de/en.

*/
package de.akquinet.android.roboject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup.LayoutParams;


public class RobojectActivity extends Activity
        implements RobojectLifecycle, ServiceConnection
{
    private Container container;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onSetContentView();
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    /**
     * Contract for subclasses: You need to call super before relying on
     * injections in {@link #onCreate(Bundle)}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (this.container == null) {
            try {
                this.container = new Container(this, this, getClass());
            }
            catch (RobojectException e) {
                throw new RuntimeException(e);
            }
        }

        this.container.invokeCreatePhase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.container.invokeResumePhase();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.container.invokeStopPhase();
    }

    @Override
    public void onServicesConnected() {
    }

    @Override
    public void onReady() {
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder serviceObject) {
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    }

    private void onSetContentView() {
        if (this.container == null) {
            try {
                this.container = new Container(this, this, getClass());
            }
            catch (RobojectException e) {
                throw new RuntimeException(e);
            }
        }

        this.container.invokeSetContentView();
    }

}
