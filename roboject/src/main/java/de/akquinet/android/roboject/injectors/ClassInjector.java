package de.akquinet.android.roboject.injectors;

import java.lang.annotation.Annotation;

public abstract class ClassInjector<A extends Annotation, T> implements Injector {
    protected final Class<A> annotationType;
    protected final T managed;

    protected ClassInjector(T managed, Class<A> annotationType) {
        this.annotationType = annotationType;
        this.managed = managed;
    }

    @Override
    public final void inject() {
        A annotation = managed.getClass().getAnnotation(annotationType);
        if (annotation != null) {
            handleAnnotation(annotation);
        }
    }

    protected abstract void handleAnnotation(A annotation);

}
