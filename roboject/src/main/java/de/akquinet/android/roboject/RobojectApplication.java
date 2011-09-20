package de.akquinet.android.roboject;

import android.app.Application;
import android.content.Intent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class RobojectApplication extends Application {
    private Map<Intent.FilterComparison, Map<String, Object>> objectIntentExtras;

    @Override
    public void onCreate() {
        objectIntentExtras = new HashMap<Intent.FilterComparison, Map<String, Object>>();
    }

    @Override
    public void onTerminate() {
        objectIntentExtras.clear();
        objectIntentExtras = null;
    }

    public Map<String, Object> getObjectIntentExtras(Intent intent) {
        Map<String, Object> intentExtras = objectIntentExtras.get(new Intent.FilterComparison(intent));
        if (intentExtras != null) {
            return intentExtras;
        }

        return Collections.<String, Object>emptyMap();
    }

    public void putObjectIntentExtras(Intent intent, Map<String, Object> extras) {
        objectIntentExtras.put(new Intent.FilterComparison(intent), extras);
    }
}
