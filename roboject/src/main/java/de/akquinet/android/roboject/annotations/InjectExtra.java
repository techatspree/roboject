package de.akquinet.android.roboject.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>
 * Inject an intent extra to this field.
 * 
 * <p>
 * The extra must be of the same type (or a subtype) as the annotated field.
 * 
 * <p>
 * You can specify a value: The string key of the extra. If not provided, the
 * name of the annotated field is used as key.
 * 
 * @author Philipp Kumar
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface InjectExtra {
    String DEFAULT_VALUE = "";

    /**
     * The key of the extra. If not specified, the name of the annotated field
     * is used instead.
     */
    String value() default DEFAULT_VALUE;

    /**
     * Specifies if the extra must have a value. If so, the lack of the extra
     * will yield a {@link RuntimeException}.
     */
    boolean mandatory() default false;
}
