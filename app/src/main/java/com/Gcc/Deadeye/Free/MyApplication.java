package com.Gcc.Deadeye.Free;

import android.app.Application;

import com.Gcc.Deadeye.R;
import com.google.android.gms.ads.MobileAds;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize the AdMob app
        MobileAds.initialize(this, getString(R.string.admob_app_id));
    }
}