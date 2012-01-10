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
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;


public class RobojectFragmentActivity extends FragmentActivity
        implements RobojectLifecycle, ServiceConnection {
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
     * injections in {@link #onCreate(android.os.Bundle)}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createContainer();

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

    /**
     * Attaches an arbitrary object to an {@link android.content.Intent}. This works like an
     * intent extra, but does not require the object to be serializable or
     * parcellable. The object is injected to the activity matching the intent,
     * if that activity uses a matching {@link de.akquinet.android.roboject.annotations.InjectObject} annotation.
     *
     * @param intent the intent matching the target activity to which we want to
     *               pass the object
     * @param key    an arbitrary String used as the intent extra key
     * @param value
     */
    protected void putObjectIntentExtra(Intent intent, String key, Object value) {
        container.putObjectIntentExtra(intent, key, value);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder serviceObject) {
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    }

    private void onSetContentView() {
        createContainer();

        this.container.invokeSetContentView();
    }

    private void createContainer() {
        if (this.container == null) {
            try {
                this.container = new Container(this, this, getClass());
            } catch (RobojectException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
