package com.iksydk.ribbit;

import android.app.Application;

import com.parse.Parse;

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
    }
}
