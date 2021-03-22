package com.Gcc.Deadeye;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

import androidx.annotation.RequiresApi;

import com.Gcc.Deadeye.GccConfig.urlref;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;

import static com.Gcc.Deadeye.GccConfig.urlref.TAG_ONESIGNALID;
import static com.Gcc.Deadeye.GccConfig.urlref.TAG_TIME;
import static com.Gcc.Deadeye.GccConfig.urlref.canary;
import static com.Gcc.Deadeye.GccConfig.urlref.netgaurd;
import static com.Gcc.Deadeye.GccConfig.urlref.pcanary;
import static com.Gcc.Deadeye.LoginActivity.getDeviceId;


public class SplashScreenActivity extends Activity {

    private static final int SPLASH_SHOW_TIME = 2000;
    private static final String TAG_KEY = urlref.TAG_KEY;
    private static final String TAG_ERROR = urlref.TAG_ERROR;
    private static final String TAG_DEVICEID = urlref.TAG_DEVICEID;
    private static final String url = urlref.Main + "test.php";
    private static final String TAG_DURATION = urlref.TAG_DURATION;
    private  boolean error,safe,brutal,Aerror;
    //Prefrance
    private String newversion;
    private String data = "data";
    private String whatsNewData;
    private boolean ismaintaince;
    private static final String TAG_APP_NEWVERSION = "newversion";

    long reqtime, restime,diff;
    Handler handler = new Handler();
    JSONParserString jsonParserString = new JSONParserString();
    private String key, deviceid,version;
    private long getduration;
    Date time;
    RequestHandler requestHandler = new RequestHandler();
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences shred = getSharedPreferences("userdetails", MODE_PRIVATE);
        setContentView(R.layout.activity_splash_screen);
        key = shred.getString(TAG_KEY, "null");
        time = new Date();
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(false)
                .init();

        deviceid = AESUtils.DarKnight.getEncrypted(getDeviceId(SplashScreenActivity.this));

        try {
            Check();
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private  void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if(imgLoad.Load(SplashScreenActivity.this).equals("1")){
            finish();
        }else{
            new BackgroundSplashTask().execute();
        }
    }


    private boolean checkVPN() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
    //    return cm.getNetworkInfo(ConnectivityManager.TYPE_VPN).isConnectedOrConnecting();
        return false;
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
            time.setTime(System.currentTimeMillis());
            reqtime = time.getTime();
            HashMap<String, String> params = new HashMap<>();
            HashMap<String, String> Ar = new HashMap<>();
            params.put(TAG_KEY, AESUtils.DarKnight.getEncrypted(key));
            params.put(TAG_DEVICEID, deviceid);
            params.put(TAG_ONESIGNALID, "null");
            Ar.put(TAG_KEY, AESUtils.DarKnight.getEncrypted(key));
            params.put(TAG_TIME, AESUtils.DarKnight.getEncrypted(String.valueOf(reqtime)));
            //      Log.d("test", AESUtils.DarKnight.getEncrypted(UUID));
            // Log.d("test",deviceid);
            //     Log.d("test",AESUtils.DarKnight.getEncrypted(key));
            String rq = null;
            String sq = null;
            try {
                rq = jsonParserString.makeHttpRequest(url, params);
                sq = requestHandler.sendPostRequest(urlref.apkupdateurl, Ar);
                JSONObject obj = null;
                obj = new JSONObject(sq);
                Aerror = Boolean.parseBoolean(AESUtils.DarKnight.getDecrypted(obj.getString(TAG_ERROR)));
                if (!Aerror) {
                    newversion = obj.getString(TAG_APP_NEWVERSION);
                    whatsNewData = obj.getString(data);
                    ismaintaince = obj.getBoolean("ismain");
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    version = pInfo.versionName;
               //     Log.d("new Version",newversion);
                }
            } catch (KeyStoreException | IOException | JSONException | PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            //converting response to json object

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
                        restime = Long.parseLong(String.valueOf(AESUtils.DarKnight.getDecrypted(obj.getString(TAG_TIME))));
                        diff = restime -reqtime;
                        // Log.d("test", String.valueOf(error));
                        if (diff == urlref.logindiff) {
                            if (!error) {
                                getduration = Long.parseLong(AESUtils.DarKnight.getDecrypted(obj.getString(TAG_DURATION)));
                                safe = Boolean.parseBoolean(AESUtils.DarKnight.getDecrypted(obj.getString("5")));
                                brutal = Boolean.parseBoolean(AESUtils.DarKnight.getDecrypted(obj.getString("6")));
                                editor.putLong(TAG_DURATION, getduration);
                                editor.apply();

                                //    Log.d("splash", String.valueOf(getduration));
                            }
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
                            if (Float.parseFloat(version) < Float.parseFloat(newversion)) {
                                Intent intent = new Intent(SplashScreenActivity.this, AppUpdaterActivity.class);
                                intent.putExtra(TAG_APP_NEWVERSION, newversion);
                                intent.putExtra(data,whatsNewData);
                                intent.putExtra("updateurl",url);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else if(ismaintaince){
                                Intent intent = new Intent(SplashScreenActivity.this, activityMaintain.class);
                                startActivity(intent);
                            }
                            else {
                                //user not signedin
                                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                                startActivity(i);
                            }
                            // Launch app intro

                        } else {

                            // Create a new boolean and preference and set it to true
                            String isSignedin = shred.getString(TAG_KEY, "null");

                            if (!isSignedin.equals("null")) {
                                if (!(getduration == 0)) {
                                    //user signedin
                                    Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
                                    i.putExtra("safe", safe);
                                    i.putExtra("brutal", brutal);
                                    startActivity(i);
                                } else {
                                    //   Log.d("duration", String.valueOf(getduration));
                                    Toast.makeText(SplashScreenActivity.this, "Subscription Expired ", Toast.LENGTH_LONG).show();
                                    shred.edit().clear().apply();
                                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                                    startActivity(i);
                                }

                            } else {
                                if (Float.parseFloat(version) < Float.parseFloat(newversion)) {
                                    Intent intent = new Intent(SplashScreenActivity.this, AppUpdaterActivity.class);
                                    intent.putExtra(TAG_APP_NEWVERSION, newversion);
                                    intent.putExtra(data,whatsNewData);
                                    intent.putExtra("updateurl",url);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                                else if(ismaintaince){
                                    Intent intent = new Intent(SplashScreenActivity.this, activityMaintain.class);
                                    startActivity(intent);
                                }
                                else {
                                    //user not signedin
                                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                                    startActivity(i);
                                }
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


