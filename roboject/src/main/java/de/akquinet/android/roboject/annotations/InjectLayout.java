package de.akquinet.android.roboject.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.app.Activity;


/**
 * Inject a layout that is set as a content view for the annotated
 * {@link Activity}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface InjectLayout {
    /**
     * The id of the layout.
     */
    int value();
}
