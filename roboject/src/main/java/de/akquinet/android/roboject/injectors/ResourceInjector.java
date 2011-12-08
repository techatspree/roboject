package de.akquinet.android.roboject.injectors;

import java.lang.reflect.Field;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import de.akquinet.android.roboject.Container;
import de.akquinet.android.roboject.RobojectException;
import de.akquinet.android.roboject.annotations.InjectResource;
import de.akquinet.android.roboject.util.AndroidUtil;
import de.akquinet.android.roboject.util.ReflectionUtil;


public class ResourceInjector implements Injector {
    private Activity activity;
    private InjectorState state = InjectorState.CREATED;

    /**
     * Method called by the container to initialize the container.
     *
     * @param context   the android context
     * @param container the roboject container
     * @param managed   the managed instance
     * @param clazz     the managed class (the class of <tt>managed</tt>)
     * @return <code>true</code> if the injector wants to contribute to the
     *         management of the instance, <code>false</code> otherwise. In this
     *         latter case, the injector will be
     *         ignored for this instance.
     * @throws de.akquinet.android.roboject.RobojectException
     *          if the configuration failed.
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
     * @param context   the android context
     * @param container the roboject container
     * @param managed   the managed instance
     */

    @Override
    public void start(Context context, Container container, Object managed) {
        this.state = InjectorState.READY;
    }

    /**
     * Method called by the container when the container is disposed. This
     * method is
     * called on valid injector only.
     * In this method, the injector can free resources
     *
     * @param context the android context
     * @param managed the managed instance
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
     * Inject a resource to the given field, using the value of the given
     * annotation as resource id. If the annotation has no value, then use the field
     * name to lookup the id in the application's R class. The class is used to lookup the resource type,
     * e.g. String maps to R.values
     */
    private void injectResource(Field field, InjectResource injectResourceAnno) {
        String name = injectResourceAnno.name() == InjectResource.DEFAULT_VALUE ? field.getName() : injectResourceAnno.name();
        String type = injectResourceAnno.type() == InjectResource.DEFAULT_VALUE ? AndroidUtil.getTypeForField(field) : injectResourceAnno.type();
        int id = AndroidUtil.getIdentifierFromR(activity, type, name);

        try {
            Object resource = AndroidUtil.getResource(activity, field, id);

            field.setAccessible(true);
            field.set(activity, resource);
        } catch (Exception e) {
            throw new RuntimeException("Could not inject a suitable resource"
                    + " for field " + field.getName() + " of type "
                    + field.getType(), e);
        }
    }

    @Override
    public InjectorState getState() {
        return this.state;
    }

    @Override
    public void onSetContentView() {
        List<Field> fields = ReflectionUtil.getAnnotatedFields(activity.getClass(), InjectResource.class);
        for (Field field : fields) {
            InjectResource annotation = field.getAnnotation(InjectResource.class);
            injectResource(field, annotation);
        }
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onStop() {
    }
}
