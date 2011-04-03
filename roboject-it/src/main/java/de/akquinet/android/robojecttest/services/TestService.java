package de.akquinet.android.robojecttest.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


public class TestService extends Service
{
    public static class AdderService extends Binder
    {
        public int add(int... numbers) {
            int sum = 0;
            for (int number : numbers) {
                sum += number;
            }
            return sum;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new AdderService();
    }
}
