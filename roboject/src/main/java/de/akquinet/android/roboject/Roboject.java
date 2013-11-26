package de.akquinet.android.roboject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import de.akquinet.android.roboject.injectors.*;

public class Roboject {
    public static void injectViews(Object managed, View parent) {
        new ViewInjector(managed, parent).inject();
    }

    public static void injectServices(Object managed, Context context, ServicesConnector callback) {
        new ServiceInjector(managed, context, callback).inject();
    }

    public static void injectExtras(Object managed, Bundle extras) {
        new ExtraInjector(managed, extras).inject();
    }

    public static void injectLayout(Activity activity) {
        new LayoutInjector(activity).inject();
    }

    public static void injectResources(Object managed, Context context) {
        new ResourceInjector(managed, context).inject();
    }

}
