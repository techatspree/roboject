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
import de.akquinet.android.robojecttest.activities.DummySupportFragmentActivity;
import de.akquinet.android.robojecttest.fragments.InjectResourceTestSupportFragment;

import static org.hamcrest.CoreMatchers.notNullValue;


public class InjectResourceSupportFragmentTest extends ActivityTestCase<DummySupportFragmentActivity> {
    public InjectResourceSupportFragmentTest() {
        super(DummySupportFragmentActivity.class);
    }

    public void testInjectResourceByMemberName() {
        InjectResourceTestSupportFragment fragment = (InjectResourceTestSupportFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.resourceSupportFragment);

        assertThat(fragment.theString, notNullValue());
        assertThat(fragment.thecolorlist, notNullValue());
        assertThat(fragment.theInteger, notNullValue());
        assertThat(fragment.theanimation, notNullValue());
        assertThat(fragment.theDimen, notNullValue());
        assertThat(fragment.icon, notNullValue());
        assertThat(fragment.theStringArray, notNullValue());
        assertThat(fragment.theIntArray, notNullValue());
    }

    public void testInjectResourceById() {
        InjectResourceTestSupportFragment fragment = (InjectResourceTestSupportFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.resourceSupportFragment);
        
        assertThat(fragment.theColor, notNullValue());
    }
}
