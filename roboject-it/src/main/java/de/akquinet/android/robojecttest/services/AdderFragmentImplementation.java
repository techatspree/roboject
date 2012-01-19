package de.akquinet.android.robojecttest.services;

import de.akquinet.android.robojecttest.fragments.InjectNonAndroidServiceTestFragment;

public class AdderFragmentImplementation implements InjectNonAndroidServiceTestFragment.AdderInterface {
    @Override
    public int add(int... params) {
        int result = 0;
        for (int param : params) {
            result += param;
        }
        return result;
    }
}
