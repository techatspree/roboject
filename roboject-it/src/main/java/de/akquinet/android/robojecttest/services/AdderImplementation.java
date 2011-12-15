package de.akquinet.android.robojecttest.services;

import de.akquinet.android.robojecttest.activities.InjectLocalServiceTestActivity;

public class AdderImplementation implements InjectLocalServiceTestActivity.AdderInterface {
    @Override
    public int add(int... params) {
        int result = 0;
        for (int param : params) {
            result += param;
        }
        return result;
    }
}
