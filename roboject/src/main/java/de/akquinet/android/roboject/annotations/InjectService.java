/*

This file is part of Roboject

Copyright (c) 2010-2011 akquinet A.G.

Contact:  http://www.akquinet.de/en

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
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
public @interface InjectService
{
    String intentAction() default Intent.ACTION_VIEW;

    Class<?> clazz() default Object.class;

    /**
     * If true, only services that are defined in the same app are injected (so
     * the service is part of the same Android package). If false, exposed
     * services from other apps can be injected. The default is false.
     */
    boolean packagePrivate() default true;

    int intentFlags() default 0;

    String intentType() default "";
}
