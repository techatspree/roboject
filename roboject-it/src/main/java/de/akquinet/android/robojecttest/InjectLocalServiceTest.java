/*

This file is part of Roboject

Copyright (c) 2010-2011 akquinet A.G.

Contact:  http://www.akquinet.de/en

GNU General Public License Usage
This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

If you are unsure which license is appropriate for your use, please contact the sales department at http://www.akquinet.de/en.

*/
package de.akquinet.android.robojecttest;

import de.akquinet.android.marvin.ActivityTestCase;
import de.akquinet.android.robojecttest.activities.InjectServiceTestActivity;

import static org.hamcrest.CoreMatchers.*;


public class InjectLocalServiceTest extends ActivityTestCase<InjectServiceTestActivity> {
    public InjectLocalServiceTest() {
        super(InjectServiceTestActivity.class);
    }

    public void testInjectService() throws Exception {
        Thread.sleep(2000);

        assertThat(getActivity().adderService, notNullValue());
        assertThat(getActivity().adderService.add(2, 3), is(equalTo(5)));
    }
}
