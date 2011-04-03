package de.akquinet.android.roboject.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.app.Activity;


/**
 * <p>
 * Inject a view to the annotated field. For this to work, you must either use
 * {@link InjectLayout} on your activity or call
 * {@link Activity#setContentView(int)} during onCreate..() *before* calling
 * super.onCreate(..).
 * 
 * <p>
 * You can specify the id of the view to inject as annotation value. If not
 * supplied, R.id.X will be used, where X is the name of the annotated field.
 * 
 * @author Philipp Kumar
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface InjectView {
    int DEFAULT_VALUE = -1;

    /**
     * Defines the id of the view to inject. If not supplied,
     * R.id.X will be used, where X is the name of the annotated field.
     */
    int value() default DEFAULT_VALUE;
}
