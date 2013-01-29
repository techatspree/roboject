package de.akquinet.android.roboject;

import android.app.Application;
import android.os.Build;

public class RobojectApplication extends Application {

    public RobojectApplication() {
        super();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(new RobojectContainer());
        }
    }
}
