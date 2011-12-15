package de.akquinet.android.roboject.util;

import android.app.Application;
import android.content.Intent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class IntentRegistry {
    private static Map<Intent.FilterComparison, Map<String, Object>> objectIntentExtras = new HashMap<Intent.FilterComparison, Map<String, Object>>();

    public static Map<String, Object> getObjectIntentExtras(Intent intent) {
        Map<String, Object> intentExtras = objectIntentExtras.get(new Intent.FilterComparison(intent));
        if (intentExtras != null) {
            return intentExtras;
        }

        return Collections.<String, Object>emptyMap();
    }

    public static void putObjectIntentExtras(Intent intent, Map<String, Object> extras) {
        objectIntentExtras.put(new Intent.FilterComparison(intent), extras);
    }
}
