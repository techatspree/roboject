package de.akquinet.android.roboject.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;


/**
 * <p>
 * Connect to an Android service and inject its binder object to this field.
 * 
 * <p>
 * The annotated field must be of the type that the targeted service returns in
 * its {@link Service#onBind(android.content.Intent)} method.
 * {@link ServiceConnection#onServiceConnected(android.content.ComponentName, android.os.IBinder)}
 * 
 * @author Philipp Kumar
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Inject
{
    String action() default Intent.ACTION_VIEW;

    String className() default "";

    /**
     * If true, only services that are defined in the same app are injected (so
     * the service is part of the same Android package). If false, exposed
     * services from other apps can be injected. The default is false.
     */
    boolean restrictToThisPackage() default true;

    int flags() default 0;

    String type() default "";
}
