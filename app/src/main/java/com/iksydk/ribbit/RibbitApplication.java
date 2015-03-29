package com.iksydk.ribbit;

import android.app.Application;

import com.iksydk.ribbit.ui.MainActivity;
import com.iksydk.ribbit.utils.ParseConstants;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
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

        PushService.setDefaultPushCallback(this, MainActivity.class, R.mipmap.ic_stat_ic_launcher);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public static void updateParseInstallation(ParseUser user)
    {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(ParseConstants.KEY_USER_ID, user.getObjectId());
        installation.saveInBackground();
    }
}
