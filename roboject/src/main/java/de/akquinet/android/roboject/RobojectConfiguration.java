package de.akquinet.android.roboject;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import de.akquinet.android.roboject.injectors.Injector;
import de.akquinet.android.roboject.injectors.IntentExtraInjector;
import de.akquinet.android.roboject.injectors.ServiceInjector;
import de.akquinet.android.roboject.injectors.ViewInjector;


public class RobojectConfiguration
{
    public static List<Injector> getDefaultInjectors(Object managed) {
        List<Injector> result = new ArrayList<Injector>();

        if (managed instanceof Activity) {
            result.add(new ViewInjector());
            result.add(new ServiceInjector());
            result.add(new IntentExtraInjector());
        }

        return result;
    }
}
