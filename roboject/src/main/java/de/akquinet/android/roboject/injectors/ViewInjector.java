package de.akquinet.android.roboject.injectors;

import android.content.Context;
import android.view.View;
import de.akquinet.android.roboject.annotations.InjectView;
import de.akquinet.android.roboject.util.AndroidUtil;
import de.akquinet.android.roboject.util.ReflectionUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;


public class ViewInjector implements Injector {
    private final View rootView;
    private final Object managed;

    public ViewInjector(Object managed, View rootView) {
        this.rootView = rootView;
        this.managed = managed;
    }

    /**
     * Inject a rootView to the given field, using the value of the given
     * annotation as rootView id. If the annotation has no value, then use the field
     * name to lookup the id in the application's R class.
     */
    private void injectView(Field field, InjectView injectViewAnno) {
        Context context = rootView.getContext();
        String value = injectViewAnno.value();
        int id = (value == InjectView.DEFAULT_VALUE) ? AndroidUtil.getIdentifierFromR(context, "id", field.getName())
                : AndroidUtil.getIdentifierFromR(context, "id", value);
        try {
            View view = rootView.findViewById(id);
            field.setAccessible(true);
            field.set(managed, view);
        } catch (Exception e) {
            throw new RuntimeException("Could not inject a suitable rootView"
                    + " for field " + field.getName() + " of type "
                    + field.getType(), e);
        }
    }

    private void injectViews() {
        List<Field> fields = ReflectionUtil.getFields(managed.getClass());
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof InjectView) {
                    injectView(field, (InjectView) annotation);
                }
            }
        }
    }

    @Override
    public void inject() {
        injectViews();
    }
}
