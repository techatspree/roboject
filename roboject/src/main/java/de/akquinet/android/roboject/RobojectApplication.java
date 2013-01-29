package de.akquinet.android.roboject;

import android.app.Application;

public class RobojectApplication extends Application {

    public RobojectApplication() {
        super();
        registerActivityLifecycleCallbacks(new RobojectContainer());
    }
}
