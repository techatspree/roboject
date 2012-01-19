/*

This file is part of Roboject

Copyright (c) 2010-2011 akquinet A.G.

Contact:  http://www.akquinet.de/en

GNU General Public License Usage
This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

If you are unsure which license is appropriate for your use, please contact the sales department at http://www.akquinet.de/en.

*/
package de.akquinet.android.robojecttest.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.akquinet.android.roboject.RobojectSupportFragment;
import de.akquinet.android.roboject.annotations.InjectView;
import de.akquinet.android.robojecttest.R;


public class InjectViewTestSupportFragment extends RobojectSupportFragment {
    @InjectView
    public TextView theTextView;

    @InjectView
    public ImageView theImageView;

    @InjectView
    public LinearLayout theLinearLayout;

    @InjectView("theTextView")
    public TextView theTextViewExplicitId;

    @InjectView("theImageView")
    public ImageView theImageViewExplicitId;

    @InjectView("theLinearLayout")
    public LinearLayout theLinearLayoutExplicitId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.viewinject, container, false);
        return inflate;
    }
}
