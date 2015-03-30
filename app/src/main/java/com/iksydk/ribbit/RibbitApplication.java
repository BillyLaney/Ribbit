package com.iksydk.ribbit;

import android.app.Application;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.iksydk.ribbit.ui.MainActivity;
import com.iksydk.ribbit.utils.ParseConstants;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

/**
 * Created by Billy on 3/24/2015.
 */
public class RibbitApplication extends Application
{
    private static final String TAG = RibbitApplication.class.getSimpleName();

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "Dh6Vi5zmPLvWQJgaCGzaIG2PbthfiekHsvuLAYJg", "PuCCi8Ojt01mvj3JwzYz60AzwEu538kIculjIvfj");

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();

        installation.saveInBackground(new SaveCallback()
        {
            @Override
            public void done(ParseException e)
            {
                if(e == null)
                {
                    PushService.setDefaultPushCallback(RibbitApplication.this, MainActivity.class, R.mipmap.ic_stat_ic_launcher);
                    Log.e(TAG, "Parse registration success (launch)");
                }
                else
                {
                    Log.e(TAG, "Parse registration failed (launch): " + e.getMessage());
                }
            }
        });
    }

    public static void updateParseInstallation(ParseUser user)
    {
        Log.i(TAG, "Attempting to register android installation");
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(ParseConstants.KEY_USER_ID, user.getObjectId());

        installation.put("UniqueId", Settings.Secure.ANDROID_ID);

        installation.saveInBackground(new SaveCallback()
        {
            @Override
            public void done(ParseException e)
            {
                if(e != null)
                {
                    Log.e(TAG, "Parse registration failed (update): " + e.getMessage());
                }
                else
                {
                    Log.i(TAG, "Parse registration success");
                }
            }
        });
    }
}
