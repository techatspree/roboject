package de.akquinet.android.roboject.injectors;

import android.app.Activity;
import de.akquinet.android.roboject.annotations.InjectLayout;
import de.akquinet.android.roboject.util.AndroidUtil;


public class LayoutInjector extends ClassInjector<InjectLayout, Activity> {
    private final Activity managed;

    public LayoutInjector(Activity managed) {
        super(managed, InjectLayout.class);
        this.managed = managed;
    }

    @Override
    protected void handleAnnotation(InjectLayout annotation) {
        int id = AndroidUtil.getIdentifierFromR(managed, "layout", annotation.value());
        managed.setContentView(id);
    }
}
