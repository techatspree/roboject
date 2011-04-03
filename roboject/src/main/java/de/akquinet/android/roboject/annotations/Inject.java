package de.akquinet.android.roboject.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.app.Service;
import android.content.ServiceConnection;


/**
 * <p>
 * Connect to an Android service and inject its binder object to this field.
 * 
 * <p>
 * You must pass the class name of the Android service as annotation value.
 * 
 * <p>
 * The annotated field must be of the type that the targeted service returns in
 * its {@link Service#onBind(android.content.Intent)} method.
 * {@link ServiceConnection#onServiceConnected(android.content.ComponentName, android.os.IBinder)}
 * 
 * @author Philipp Kumar
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface Inject {
    /**
     * The class name of the targeted service
     */
    Class<? extends Service> value();
}
