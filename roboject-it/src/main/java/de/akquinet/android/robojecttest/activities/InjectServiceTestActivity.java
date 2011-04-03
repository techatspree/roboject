package de.akquinet.android.robojecttest.activities;

import de.akquinet.android.roboject.RobojectActivity;
import de.akquinet.android.roboject.annotations.Inject;
import de.akquinet.android.robojecttest.services.TestService;
import de.akquinet.android.robojecttest.services.TestService.AdderService;


public class InjectServiceTestActivity extends RobojectActivity
{
    @Inject(TestService.class)
    public AdderService adderService;
}
