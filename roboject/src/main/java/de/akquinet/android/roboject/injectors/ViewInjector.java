package de.akquinet.android.roboject.injectors;

import android.content.Context;
import android.view.View;
import de.akquinet.android.roboject.annotations.InjectView;
import de.akquinet.android.roboject.util.AndroidUtil;

import java.lang.reflect.Field;


public class ViewInjector extends FieldInjector<InjectView> {
    private final View rootView;

    public ViewInjector(Object managed, View rootView) {
        super(managed, InjectView.class);

        this.rootView = rootView;
    }

    @Override
    protected void injectValue(Field field, InjectView annotation) {
        Context context = rootView.getContext();
        String value = annotation.value();
        int id = (value == InjectView.DEFAULT_VALUE) ? AndroidUtil.getIdentifierFromR(context, "id", field.getName())
                : AndroidUtil.getIdentifierFromR(context, "id", value);
        try {
            View view = rootView.findViewById(id);
            field.setAccessible(true);
            field.set(managed, view);
        } catch (Exception e) {
            throw new RuntimeException("Could not inject a suitable rootView" + " for field " + field.getName() + " of type " + field.getType(), e);
        }
    }
}
