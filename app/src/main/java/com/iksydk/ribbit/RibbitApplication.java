package com.iksydk.ribbit;

import android.app.Application;

import com.iksydk.ribbit.ui.MainActivity;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

/**
 * Created by Billy on 3/24/2015.
 */
public class RibbitApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "Dh6Vi5zmPLvWQJgaCGzaIG2PbthfiekHsvuLAYJg", "PuCCi8Ojt01mvj3JwzYz60AzwEu538kIculjIvfj");

        PushService.setDefaultPushCallback(this, MainActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
