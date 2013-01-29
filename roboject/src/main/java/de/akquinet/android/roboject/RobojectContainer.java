package de.akquinet.android.roboject;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

public class RobojectContainer implements Application.ActivityLifecycleCallbacks {
    private Map<Activity, Container> activityMap;

    public RobojectContainer() {
        activityMap = new HashMap<Activity, Container>();
    }

    private Container getActivityContainer(Activity activity) {
        if (activity instanceof RobojectActivity)
            return null;

        Container container = activityMap.get(activity);
        if (container == null) {
            try {
                container = new Container(activity, activity, activity.getClass());
                activityMap.put(activity, container);
            } catch (RobojectException e) {
            }
        }

        return container;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Container container = getActivityContainer(activity);

        if (container != null)
            container.invokeCreatePhase();
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Container container = getActivityContainer(activity);
        if (container != null)
            container.invokeResumePhase();
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Container container = getActivityContainer(activity);
        if (container != null)
            container.invokeStopPhase();
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        activityMap.remove(activity);
    }
}
