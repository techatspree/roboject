package de.akquinet.android.roboject;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import sun.java2d.SunGraphicsEnvironment;

import java.util.HashMap;
import java.util.Map;

public class RobojectApplication extends Application {
    private Map<Class, Map<String, Object>> dataStore;

    @Override
    public void onCreate() {
        dataStore = new HashMap<Class, Map<String, Object>>();
    }

    @Override
    public void onTerminate() {
        dataStore = null;
    }

    public void storeData(Class target, String key, Object data) {
        Map<String, Object> dataSet = dataStore.get(target);
        if (dataSet == null) {
            dataSet = new HashMap<String, Object>();
            dataStore.put(target, dataSet);
        }
        dataSet.put(key, data);
    }

    public Object getDataSet(Class target) {
        Map<String, Object> dataSet = dataStore.get(target);
        dataStore.remove(dataSet);
        return dataSet;
    }
}
