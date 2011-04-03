package de.akquinet.android.old;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import de.akquinet.android.roboject.annotations.Inject;
import de.akquinet.android.roboject.annotations.InjectExtra;
import de.akquinet.android.roboject.annotations.InjectLayout;
import de.akquinet.android.roboject.annotations.InjectView;
import de.akquinet.android.roboject.util.ReflectionUtil;


/**
 * <p>
 * Injection-enabled Android {@link Activity}.
 * 
 * <p>
 * Extend this activity to be able to use all annotations provided with
 * Roboject.
 * 
 * <p>
 * If you want to be notified when a service injected via {@link Inject} is
 * connected, overwrite {@link #onServiceConnected(ComponentName, IBinder)}.
 * 
 * @author Philipp Kumar
 */
public class OldRobojectActivity
        extends Activity
        implements ServiceConnection {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        processClassAnnotations();
        processFieldAnnotations();

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    }

    private void processClassAnnotations() {
        injectLayout();
    }

    private void processFieldAnnotations() {
        List<Field> fields = ReflectionUtil.getFields(getClass());
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Inject) {
                    injectService(field, (Inject) annotation);
                }
                else if (annotation instanceof InjectView) {
                    injectView(field, (InjectView) annotation);
                }
                else if (annotation instanceof InjectExtra) {
                    injectExtra(field, (InjectExtra) annotation);
                }
            }
        }
    }

    /**
     * Search this class and all super classes for an {@link InjectLayout}
     * annotation. If present, use its value to set the content view.
     */
    private void injectLayout() {
        InjectLayout injectLayoutAnno =
                ReflectionUtil.getAnnotation(getClass(), InjectLayout.class);
        if (injectLayoutAnno != null) {
            setContentView(injectLayoutAnno.value());
        }
    }

    private void injectService(final Field field, Inject annotation) {
        Class<? extends Service> androidServiceClass = annotation.value();
        Intent intent = new Intent(this, androidServiceClass);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                if (field.getType().isAssignableFrom(service.getClass())) {
                    try {
                        field.setAccessible(true);
                        field.set(OldRobojectActivity.this, service);
                    }
                    catch (Exception e) {
                        // TODO: better error message
                        throw new RuntimeException("Unable to inject service", e);
                    }
                }
                OldRobojectActivity.this.onServiceConnected(name, service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                OldRobojectActivity.this.onServiceDisconnected(name);
            }
        }, Service.BIND_AUTO_CREATE);
    }

    /**
     * Inject a view to the given field, using the value of the given
     * annotation as view id. If the annotation has no value, then use the field
     * name to lookup the id in the application's R class.
     */
    private void injectView(Field field, InjectView injectViewAnno) {
        int value = injectViewAnno.value();
        if (value == InjectView.DEFAULT_VALUE) {
            // No value specified in annotation. Check R class then.
            value = getIdFromR(field.getName());
        }
        try {
            View view = findViewById(value);
            if (view == null) {
                throw new RuntimeException("Could not inject a suitable view"
                        + " for field " + field.getName() + "of instance of type "
                        + field.getDeclaringClass().getCanonicalName() + "."
                        + " No such view could be found.");
            }
            field.setAccessible(true);
            field.set(this, view);
        }
        catch (Exception e) {
            throw new RuntimeException("Could not inject a suitable view"
                    + " for field " + field.getName() + "of instance of type "
                    + field.getDeclaringClass().getCanonicalName(), e);
        }
    }

    /**
     * Inject an extra to the given field, using the value of the given
     * annotation as extra id. If the annotation has no value, then use the
     * field name as extra id.
     */
    private void injectExtra(Field field, InjectExtra annotation) {
        String value = annotation.value();
        Intent intent = getIntent();

        if (InjectExtra.DEFAULT_VALUE.equals(value)) {
            value = field.getName();
        }

        Class<?> type = field.getType();
        Object extra = null;
        try {
            if (String.class.isAssignableFrom(type)) {
                extra = intent.getStringExtra(value);
            }
            if (ArrayList.class.isAssignableFrom(type)) {
                extra = intent.getStringArrayListExtra(value);
                if (extra == null) {
                    extra = intent.getParcelableArrayListExtra(value);
                }
            }
            else if (Boolean.class.isAssignableFrom(type)) {
                extra = intent.getBooleanExtra(value, false);
            }
            else if (Bundle.class.isAssignableFrom(type)) {
                extra = intent.getBundleExtra(value);
            }
            else if (Byte.class.isAssignableFrom(type)) {
                extra = intent.getByteExtra(value, (byte) 0);
            }
            else if (Character.class.isAssignableFrom(type)) {
                extra = intent.getCharExtra(value, (char) 0);
            }
            else if (CharSequence.class.isAssignableFrom(type)) {
                extra = intent.getCharSequenceExtra(value);
            }
            else if (Double.class.isAssignableFrom(type)) {
                extra = intent.getDoubleExtra(value, 0d);
            }
            else if (Float.class.isAssignableFrom(type)) {
                extra = intent.getFloatExtra(value, 0f);
            }
            else if (Integer.class.isAssignableFrom(type)) {
                extra = intent.getIntExtra(value, 0);
            }
            else if (Long.class.isAssignableFrom(type)) {
                extra = intent.getLongExtra(value, 0l);
            }
            else if (Parcelable.class.isAssignableFrom(type)) {
                extra = intent.getParcelableExtra(value);
            }
            else if (Serializable.class.isAssignableFrom(type)) {
                extra = intent.getSerializableExtra(value);
            }
            else if (Short.class.isAssignableFrom(type)) {
                extra = intent.getShortExtra(value, (short) 0);
            }
            else if (type.isArray()) {
                Class<?> componentType = type.getComponentType();
                if (String.class.isAssignableFrom(componentType)) {
                    extra = intent.getStringArrayExtra(value);
                }
                else if (Boolean.class.isAssignableFrom(componentType)) {
                    extra = getIntent().getBooleanArrayExtra(value);
                }
                else if (Byte.class.isAssignableFrom(componentType)) {
                    extra = getIntent().getByteArrayExtra(value);
                }
                else if (Character.class.isAssignableFrom(componentType)) {
                    extra = getIntent().getCharArrayExtra(value);
                }
                else if (Double.class.isAssignableFrom(componentType)) {
                    extra = getIntent().getDoubleArrayExtra(value);
                }
                else if (Float.class.isAssignableFrom(componentType)) {
                    extra = getIntent().getFloatArrayExtra(value);
                }
                else if (Integer.class.isAssignableFrom(componentType)) {
                    extra = getIntent().getIntArrayExtra(value);
                }
                else if (Long.class.isAssignableFrom(componentType)) {
                    extra = getIntent().getLongArrayExtra(value);
                }
                else if (Parcelable.class.isAssignableFrom(componentType)) {
                    extra = getIntent().getParcelableArrayExtra(value);
                }
                else if (Short.class.isAssignableFrom(componentType)) {
                    extra = intent.getShortArrayExtra(value);
                }
            }

            if (extra == null) {
                String message = "Could not inject a suitable extra"
                        + " for field " + field.getName() + "of instance of type "
                        + field.getDeclaringClass().getCanonicalName() + "."
                        + " No such extra could be found.";
                if (annotation.mandatory()) {
                    throw new RuntimeException(message);
                }
                else {
                    Log.i(getClass().getCanonicalName(), message);
                    return;
                }
            }

            field.setAccessible(true);
            field.set(this, extra);
        }
        catch (Exception e) {
            throw new RuntimeException("Could not inject a suitable extra"
                    + " for field " + field.getName() + "of instance of type "
                    + field.getDeclaringClass().getCanonicalName(), e);
        }
    }

    /**
     * Check application's R.id class and retrieve the value of the static
     * member with the given name.
     */
    private int getIdFromR(String idName) {
        Class<?> rIdClass = null;
        try {
            rIdClass = Class.forName(getPackageName() + ".R$id");
        }
        catch (ClassNotFoundException e) {
            // No R.id class? This should never happen.
            throw new RuntimeException(e);
        }

        try {
            Field rField = rIdClass.getField(idName);
            Object intValue;
            try {
                intValue = rField.get(null);
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (!(intValue instanceof Integer)) {
                throw new RuntimeException("Not an int: "
                        + rIdClass.getCanonicalName() + "." + rField.getName());
            }
            return ((Integer) intValue).intValue();
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException("There is no such id in the R class: "
                    + getPackageName() + ".R.id" + "." + idName + ")");
        }
        catch (SecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
