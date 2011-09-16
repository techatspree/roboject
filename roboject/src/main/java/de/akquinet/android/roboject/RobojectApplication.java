package de.akquinet.android.roboject;

import android.app.Application;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

public class RobojectApplication extends Application {
    private Map<String, Object> dataStore;

    @Override
    public void onCreate() {
        dataStore = new HashMap<String, Object>();
    }

    @Override
    public void onTerminate() {
        dataStore = null;
    }

    public void storeData(String key, Object data) {
        dataStore.put(key, data);
    }

    public Object retrieveData(String key) {
        try {
            return dataStore.get(key);
        } finally {
            dataStore.remove(key);
        }
    }
}
