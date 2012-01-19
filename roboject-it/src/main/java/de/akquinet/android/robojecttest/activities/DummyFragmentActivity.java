package de.akquinet.android.robojecttest.activities;

import android.app.Activity;
import android.os.Bundle;
import de.akquinet.android.robojecttest.R;

public class DummyFragmentActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment);
    }
}
