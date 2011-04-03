package de.akquinet.android.roboject;

import android.app.Activity;
import android.os.Bundle;


public class RobojectActivity extends Activity
{
    /**
     * Contract for subclasses:
     * You need to call super before relying on injections in
     * {@link #onCreate(Bundle)}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            new Container(this, this, getClass());
        }
        catch (RobojectException e) {
            throw new RuntimeException(e);
        }
    }
}
