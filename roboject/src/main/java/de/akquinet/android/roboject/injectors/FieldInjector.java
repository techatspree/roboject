package de.akquinet.android.roboject.injectors;

import de.akquinet.android.roboject.util.ReflectionUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public abstract class FieldInjector<A extends Annotation> implements Injector {
    protected final Object managed;
    protected final Class<A> annotationType;

    protected FieldInjector(Object managed, Class<A> annotationType) {
        this.managed = managed;
        this.annotationType = annotationType;
    }

    @Override
    public final void inject() {
        List<Field> fields = ReflectionUtil.getFields(managed.getClass());
        for (Field field : fields) {
            Annotation annotation = field.getAnnotation(annotationType);
            if (annotation != null)
                injectValue(field, (A) annotation);
        }
    }

    protected abstract void injectValue(Field field, A annotation);
}