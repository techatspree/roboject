package de.akquinet.android.roboject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;


public class RobojectActivity extends Activity
        implements RobojectLifecycle, ServiceConnection
{
    private Container container;

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
}
