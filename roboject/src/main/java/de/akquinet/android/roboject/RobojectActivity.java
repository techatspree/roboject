package de.akquinet.android.roboject;

import android.app.Activity;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.IBinder;

public class RobojectActivity extends Activity {
	/**
	 * Contract for subclasses: You need to call super before relying on
	 * injections in {@link #onCreate(Bundle)}.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			new Container(this, this, getClass());
		} catch (RobojectException e) {
			throw new RuntimeException(e);
		}
	}

	public void onServiceConnected(ComponentName name, IBinder service) {
	}
}
