package de.akquinet.android.roboject.injectors;

import android.app.Activity;
import de.akquinet.android.roboject.annotations.InjectLayout;
import de.akquinet.android.roboject.util.AndroidUtil;
import de.akquinet.android.roboject.util.ReflectionUtil;


public class LayoutInjector implements Injector {
    private final Activity managed;

    public LayoutInjector(Activity managed) {
        this.managed = managed;
    }

    @Override
    public void inject() {
        InjectLayout layoutAnnotation = ReflectionUtil.getAnnotation(managed.getClass(), InjectLayout.class);
        if (layoutAnnotation != null) {
            int id = AndroidUtil.getIdentifierFromR(managed, "layout", layoutAnnotation.value());
            managed.setContentView(id);
        }
    }
}
