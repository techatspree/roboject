package de.akquinet.android.roboject.injectors;

import android.content.Context;
import de.akquinet.android.roboject.annotations.InjectResource;
import de.akquinet.android.roboject.util.AndroidUtil;

import java.lang.reflect.Field;


public class ResourceInjector extends FieldInjector<InjectResource> {
    private Context context;

    public ResourceInjector(Object managed, Context context) {
        super(managed, InjectResource.class);

        this.context = context;
    }

    @Override
    protected void injectValue(Field field, InjectResource annotation) {
        String name = annotation.name() == InjectResource.DEFAULT_VALUE ? field.getName() : annotation.name();
        String type = annotation.type() == InjectResource.DEFAULT_VALUE ? AndroidUtil.getTypeForField(field) : annotation.type();
        int id = AndroidUtil.getIdentifierFromR(context, type, name);

        try {
            Object resource = AndroidUtil.getResource(context, field, id);

            field.setAccessible(true);
            field.set(managed, resource);
        } catch (Exception e) {
            throw new RuntimeException("Could not inject a suitable resource for field " + field.getName() + " of type " + field.getType(), e);
        }
    }
}
