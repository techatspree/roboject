/*

This file is part of Roboject

Copyright (c) 2010-2011 akquinet A.G.

Contact:  http://www.akquinet.de/en

GNU General Public License Usage
This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

If you are unsure which license is appropriate for your use, please contact the sales department at http://www.akquinet.de/en.

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
