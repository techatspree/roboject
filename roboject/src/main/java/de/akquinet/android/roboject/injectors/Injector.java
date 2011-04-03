package de.akquinet.android.roboject.injectors;

import de.akquinet.android.roboject.Container;
import de.akquinet.android.roboject.RobojectException;
import android.content.Context;


public interface Injector
{
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
    boolean configure(Context context, Container container, Object managed,
            Class<?> clazz) throws RobojectException;

    /**
     * Method called by the container when all injectors are configured
     * (immediately
     * after configure). This method is called on valid injector only.
     * In this method, the injector can injects field and call callbacks
     * (however,
     * callbacks may wait the validate call).
     * 
     * @param context
     *            the android context
     * @param container
     *            the roboject container
     * @param managed
     *            the managed instance
     */
    void start(Context context, Container container, Object managed);

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
    void stop(Context context, Object managed);

    /**
     * Checks whether the injector is valid or not.
     * 
     * @return <code>true</code> if the injector is valid (ready),
     *         <code>false</code> otherwise.
     */
    boolean isValid();

    /**
     * Callback called by the container when all injectors are valid.
     */
    void validate();

    /**
     * Callback called by the container when at least one injector becomes
     * invalid.
     */
    void invalidate();
    // TODO Do we need the Android lifecycle callbacks ?
}
