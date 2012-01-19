package de.akquinet.android.roboject.injectors;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import de.akquinet.android.roboject.Container;
import de.akquinet.android.roboject.RobojectException;
import de.akquinet.android.roboject.annotations.InjectLayout;
import de.akquinet.android.roboject.util.AndroidUtil;
import de.akquinet.android.roboject.util.ReflectionUtil;


public class LayoutInjector implements Injector {
    private Activity activity;
    private InjectorState state = InjectorState.CREATED;
    private Object managed;

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
        if (context instanceof Activity) {
            this.activity = (Activity) context;
        } else {
            return false;
        }

        this.managed = managed;

        if (managed instanceof Activity) {
            return true;
        }

        if (managed instanceof Fragment) {
            return true;
        }

        if (managed instanceof android.app.Fragment) {
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

    @Override
    public InjectorState getState() {
        return this.state;
    }

    @Override
    public void onSetContentView() {
    }

    @Override
    public void onCreate() {
        InjectLayout layoutAnnotation = ReflectionUtil.getAnnotation(managed.getClass(), InjectLayout.class);
        if (layoutAnnotation != null) {
            int id = AndroidUtil.getIdentifierFromR(activity, "layout", layoutAnnotation.value());

            if (managed instanceof Activity) {
                ((Activity) managed).setContentView(id);
            }

            if (managed instanceof Fragment || managed instanceof android.app.Fragment)
                throw new RuntimeException("Layout injection not Implemented for fragments.");
        }
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onStop() {
    }
}
