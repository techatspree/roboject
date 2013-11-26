package de.akquinet.android.robojecttest.activities;

import android.os.Bundle;
import de.akquinet.android.roboject.RobojectActivity;
import de.akquinet.android.robojecttest.R;

public class DummyFragmentActivity extends RobojectActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment);
    }
}
