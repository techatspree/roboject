/*

This file is part of Roboject

Copyright (c) 2010-2011 akquinet A.G.

Contact:  http://www.akquinet.de/en

GNU General Public License Usage
This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

If you are unsure which license is appropriate for your use, please contact the sales department at http://www.akquinet.de/en.

*/
package de.akquinet.android.roboject;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import de.akquinet.android.roboject.injectors.*;


public class RobojectConfiguration
{
    public static List<Injector> getDefaultInjectors(Object managed) {
        List<Injector> result = new ArrayList<Injector>();

        if (managed instanceof Activity) {
            result.add(new ViewInjector());
            result.add(new ServiceInjector());
            result.add(new IntentExtraInjector());
            result.add(new LayoutInjector());
            result.add(new ObjectInjector());
            result.add(new ResourceInjector());
        }

        return result;
    }
}
