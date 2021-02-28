package com.Gcc.Deadeye;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.Gcc.Deadeye.GccConfig.urlref;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyStoreException;
import java.util.HashMap;

import static com.Gcc.Deadeye.GccConfig.urlref.TAG_ONESIGNALID;


public class SplashScreenActivity extends Activity {

    private static final int SPLASH_SHOW_TIME = 2000;
    private static final String TAG_KEY = urlref.TAG_KEY;
    private static final String TAG_ERROR = urlref.TAG_ERROR;
    private static final String TAG_DEVICEID = urlref.TAG_DEVICEID;
    private static final String url = urlref.Main + "login.php";
    private static final String TAG_DURATION = urlref.TAG_DURATION;
    private  boolean error;
    //Prefrance
    String UUID;

    Handler handler = new Handler();
    JSONParserString jsonParserString = new JSONParserString();
    private String key, deviceid;
    private long getduration;
    private boolean isFirstStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences shred = getSharedPreferences("userdetails", MODE_PRIVATE);
        setContentView(R.layout.activity_splash_screen);
        key = shred.getString(TAG_KEY, "null");

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(false)
                .init();

        deviceid = AESUtils.DarKnight.getEncrypted(getDeviceId(SplashScreenActivity.this));

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                //     String text = "OneSignal UserID:\n" + userId + "\n\n";
                UUID = userId;
             //   Log.d("test",UUID);
                UUID = AESUtils.DarKnight.getEncrypted(UUID);
          //      Log.d("test",UUID);

            }
        });
        if(Helper.checkVPN(SplashScreenActivity.this)) {
            Toast.makeText(SplashScreenActivity.this, "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
            finish();
        }else {
            new BackgroundSplashTask().execute();
        }
    }

    @SuppressLint("HardwareIds")
    public  String getDeviceId(Context context) {

        String deviceId;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }

        return deviceId;
    }
    private boolean checkVPN() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getNetworkInfo(ConnectivityManager.TYPE_VPN).isConnectedOrConnecting();

    }

    /**
     * Async Task: can be used to load DB, images during which the splash screen
     * is shown to user
     */
    private class BackgroundSplashTask extends AsyncTask<Void, Void, String> {

        SharedPreferences shred = getSharedPreferences("userdetails", MODE_PRIVATE);
        SharedPreferences.Editor editor = shred.edit();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls


        }

        @Override
        protected String doInBackground(Void... arg0) {
            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put(TAG_KEY,AESUtils.DarKnight.getEncrypted(key));
            params.put(TAG_DEVICEID, deviceid);
            params.put(TAG_ONESIGNALID, "null");
      //      Log.d("test", AESUtils.DarKnight.getEncrypted(UUID));
         // Log.d("test",deviceid);
       //     Log.d("test",AESUtils.DarKnight.getEncrypted(key));
            String rq = null;
            try {
                rq = jsonParserString.makeHttpRequest(url, params);
            } catch (KeyStoreException | IOException e) {
                e.printStackTrace();
            }
            //returing the response
            return rq;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                        boolean isFirstStart = shred.getBoolean("firstStart", true);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        try {
                            JSONObject obj = new JSONObject(result);
                            error = Boolean.parseBoolean(AESUtils.DarKnight.getDecrypted(obj.getString(TAG_ERROR)));
                            // Log.d("test", String.valueOf(error));
                            if (!error) {
                                getduration = Long.parseLong(AESUtils.DarKnight.getDecrypted(obj.getString(TAG_DURATION)));
                                editor.putLong(TAG_DURATION, getduration);
                                editor.apply();

                                //    Log.d("splash", String.valueOf(getduration));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (checkVPN()) {
                            Toast.makeText(SplashScreenActivity.this, "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            // If the activity has never started before...
                            if (isFirstStart) {

                                // Launch app intro
                                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                                startActivity(i);


                            } else {

                                // Create a new boolean and preference and set it to true
                                String isSignedin = shred.getString(TAG_KEY, "null");

                                if (!isSignedin.equals("null")) {
                                    if (!(getduration == 0)) {

                                        //user signedin
                                        Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
                                        startActivity(i);
                                    } else {
                                        //   Log.d("duration", String.valueOf(getduration));
                                        Toast.makeText(SplashScreenActivity.this, "Subscription Expired ", Toast.LENGTH_LONG).show();
                                        shred.edit().clear().apply();
                                        Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                                        startActivity(i);
                                    }

                                } else {
                                    //user not signedin
                                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                                    startActivity(i);
                                }
                            }
                        }
                        finish();
                    }

            },SPLASH_SHOW_TIME);
            // Create a new boolean and preference and set it to true

        }
    }
}


