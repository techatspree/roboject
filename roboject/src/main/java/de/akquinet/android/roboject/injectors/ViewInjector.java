package de.akquinet.android.roboject.injectors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import de.akquinet.android.roboject.Container;
import de.akquinet.android.roboject.RobojectException;
import de.akquinet.android.roboject.annotations.InjectView;
import de.akquinet.android.roboject.util.AndroidUtil;
import de.akquinet.android.roboject.util.ReflectionUtil;


public class ViewInjector implements Injector
{
    private Activity activity;

    /**
     * Method called by the container to initialize the container.
     * 
     * @param context
     *            the android context
     * @param container
     *            the roboject container
     * @param managed
     *            the managed instance
     * @param clazz
     *            the managed class (the class of <tt>managed</tt>)
     * @return <code>true</code> if the injector wants to contribute to the
     *         management of the instance, <code>false</code> otherwise. In this
     *         latter case, the injector will be
     *         ignored for this instance.
     * @throws RobojectException
     *             if the configuration failed.
     */
    @Override
    public boolean configure(Context context, Container container, Object managed, Class<?> clazz)
            throws RobojectException {
        if (managed instanceof Activity) {
            this.activity = (Activity) context;
            return true;
        }

        return false;
    }

    /**
     * Method called by the container when all injectors are configured
     * (immediately
     * after configure). This method is called on valid injector only.
     * In this method, the injector can injects field and call callbacks
     * (however, callbacks may wait the validate call).
     * 
     * @param context
     *            the android context
     * @param container
     *            the roboject container
     * @param managed
     *            the managed instance
     */

    @Override
    public void start(Context context, Container container, Object managed) {
        List<Field> fields = ReflectionUtil.getFields(activity.getClass());
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof InjectView) {
                    injectView(field, (InjectView) annotation);
                }
            }
        }
    }

    /**
     * Method called by the container when the container is disposed. This
     * method is
     * called on valid injector only.
     * In this method, the injector can free resources
     * 
     * @param context
     *            the android context
     * @param managed
     *            the managed instance
     */
    @Override
    public void stop(Context context, Object managed) {
        this.activity = null;
    }

    /**
     * Checks whether the injector is valid or not.
     * 
     * @return <code>true</code> if the injector is valid (ready),
     *         <code>false</code> otherwise.
     */
    @Override
    public boolean isValid() {
        return this.activity != null;
    }

    /**
     * Callback called by the container when all injectors are valid.
     */
    @Override
    public void validate() {
    }

    /**
     * Callback called by the container when at least one injector becomes
     * invalid.
     */
    @Override
    public void invalidate() {
    }

    /**
     * Inject a view to the given field, using the value of the given
     * annotation as view id. If the annotation has no value, then use the field
     * name to lookup the id in the application's R class.
     */
    private void injectView(Field field, InjectView injectViewAnno) {
        int value = injectViewAnno.value();
        if (value == InjectView.DEFAULT_VALUE) {
            // No value specified in annotation. Check R class then.
            value = AndroidUtil.getIdFromR(activity, field.getName());
        }
        try {
            View view = activity.findViewById(value);
            Log.i(getClass().getName(),
                    "Injecting value '" + view + "' for field '" + field.getName() + "'...");
            field.setAccessible(true);
            field.set(activity, view);
        }
        catch (Exception e) {
            throw new RuntimeException("Could not inject a suitable view"
                    + " for field " + field.getName() + " of type "
                    + field.getType(), e);
        }
    }
}
