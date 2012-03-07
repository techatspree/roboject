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

/**
 * <p>
 * Inject a resource to the annotated field. For this to work, you must either use
 * <p>
 * You can specify the id of the resource to inject as annotation value name. If not
 * supplied, R.id.X will be used, where X is the name of the annotated field.
 * You can also supply the type of the resource.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface InjectResource {
    String DEFAULT_VALUE = "";

    /**
     * Defines the id of the resource to inject. If not supplied,
     * R.X.Y will be used, where X is a type string according to the field type and Y the name of the annotated field.
     */
    String name() default DEFAULT_VALUE;

    /**
     * Overrides the type detection by specifying the concrete type of this resource, e.g. drawable or string
     */
    String type() default DEFAULT_VALUE;
}
