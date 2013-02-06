package de.akquinet.android.roboject.injectors;

import android.content.Context;
import de.akquinet.android.roboject.annotations.InjectResource;
import de.akquinet.android.roboject.util.AndroidUtil;
import de.akquinet.android.roboject.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.List;


public class ResourceInjector implements Injector {
    private Context context;
    private Object managed;

    public ResourceInjector(Object managed, Context context) {
        this.context = context;
        this.managed = managed;
    }

    /**
     * Inject a resource to the given field, using the value of the given
     * annotation as resource id. If the annotation has no value, then use the field
     * name to lookup the id in the application's R class. The class is used to lookup the resource type,
     * e.g. String maps to R.values
     */
    private void injectResource(Field field, InjectResource injectResourceAnno) {
        String name = injectResourceAnno.name() == InjectResource.DEFAULT_VALUE ? field.getName() : injectResourceAnno.name();
        String type = injectResourceAnno.type() == InjectResource.DEFAULT_VALUE ? AndroidUtil.getTypeForField(field) : injectResourceAnno.type();
        int id = AndroidUtil.getIdentifierFromR(context, type, name);

        try {
            Object resource = AndroidUtil.getResource(context, field, id);

            field.setAccessible(true);
            field.set(managed, resource);
        } catch (Exception e) {
            throw new RuntimeException("Could not inject a suitable resource"
                    + " for field " + field.getName() + " of type "
                    + field.getType(), e);
        }
    }

    private void injectResources() {
        List<Field> fields = ReflectionUtil.getAnnotatedFields(managed.getClass(), InjectResource.class);
        for (Field field : fields) {
            InjectResource annotation = field.getAnnotation(InjectResource.class);
            injectResource(field, annotation);
        }
    }

    @Override
    public void inject() {
        injectResources();
    }
}
