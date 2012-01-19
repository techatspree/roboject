package de.akquinet.android.robojecttest.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import de.akquinet.android.robojecttest.R;

public class DummySupportFragmentActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supportfragment);
    }
}
