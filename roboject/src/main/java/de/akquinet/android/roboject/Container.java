package de.akquinet.android.roboject;

import java.util.List;

import android.content.Context;
import de.akquinet.android.roboject.injectors.Injector;


public class Container
{
    private List<Injector> injectors;

    public Container(Context context, Object managed, Class<?> clazz) throws RobojectException {
        this.injectors = RobojectConfiguration.getDefaultInjectors(managed);

        for (Injector injector : this.injectors) {
            injector.configure(context, this, managed, clazz);
        }

        for (Injector injector : this.injectors) {
            if (injector.isValid()) {
                injector.start(context, this, managed);
            }
        }
    }
}
