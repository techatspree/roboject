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
import de.akquinet.android.robojecttest.activities.DummyFragmentActivity;
import de.akquinet.android.robojecttest.fragments.InjectResourceTestFragment;

import static org.hamcrest.CoreMatchers.notNullValue;


public class InjectResourceFragmentTest extends ActivityTestCase<DummyFragmentActivity> {
    public InjectResourceFragmentTest() {
        super(DummyFragmentActivity.class);
    }

    public void testInjectResourceByMemberName() {
        InjectResourceTestFragment fragment = (InjectResourceTestFragment) getActivity().getFragmentManager().findFragmentById(R.id.resourceFragment);

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
        InjectResourceTestFragment fragment = (InjectResourceTestFragment) getActivity().getFragmentManager().findFragmentById(R.id.resourceFragment);

        assertThat(fragment.theColor, notNullValue());
    }
}
