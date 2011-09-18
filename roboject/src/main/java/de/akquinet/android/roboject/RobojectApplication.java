package de.akquinet.android.roboject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Application;
import android.content.Intent;


public class RobojectApplication extends Application
{
    private Map<Intent, Map<String, Object>> objectIntentExtras;

    @Override
    public void onCreate() {
        objectIntentExtras = new HashMap<Intent, Map<String, Object>>();
    }

    @Override
    public void onTerminate() {
        objectIntentExtras.clear();
        objectIntentExtras = null;
    }

    public Map<String, Object> getObjectIntentExtras(Intent intent) {
        for (Entry<Intent, Map<String, Object>> entry : objectIntentExtras.entrySet()) {
            Intent key = entry.getKey();
            if (key.filterEquals(intent)) {
                return entry.getValue();
            }
        }

        return Collections.<String, Object> emptyMap();
    }

    public void putObjectIntentExtras(Intent intent, Map<String, Object> extras) {
        objectIntentExtras.put(intent, extras);
    }
}
