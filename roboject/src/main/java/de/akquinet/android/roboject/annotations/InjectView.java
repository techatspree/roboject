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
