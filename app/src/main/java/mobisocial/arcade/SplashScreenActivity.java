package mobisocial.arcade;


import android.Manifest;
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
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import mobisocial.arcade.GccConfig.urlref;
import mobisocial.arcade.free.FreeFloatLogo;

import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

import static mobisocial.arcade.GccConfig.urlref.TAG_LITE;
import static mobisocial.arcade.GccConfig.urlref.TAG_MSG;
import static mobisocial.arcade.GccConfig.urlref.TAG_ONESIGNALID;


public class SplashScreenActivity extends Activity implements OSSubscriptionObserver {

    private static final int SPLASH_SHOW_TIME = 2000;
    private static final String TAG_KEY = urlref.TAG_KEY;
    private static final String TAG_ERROR = urlref.TAG_ERROR;
    private static final String TAG_DEVICEID = urlref.TAG_DEVICEID;
    private static final String url = urlref.Main + "login.php";
    private static final String TAG_DURATION = urlref.TAG_DURATION;
    private boolean error, safe;

    //Prefrance
    static {
        System.loadLibrary("sysload");
    }

    private String newversion;
    private String data = "data";


    private String updateurl;
    private String whatsNewData, msg;
    private boolean ismaintaince;
    private static final String TAG_APP_NEWVERSION = "newversion";
    private Boolean islite;
    JSONParserString jsonParserString = new JSONParserString();
    private String key, deviceid = "null", version;
    public static String UUID = "null";
    private long getduration;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(false)
                .init();
        OneSignal.addSubscriptionObserver(this);
        SharedPreferences shred = getSharedPreferences("userdetails", MODE_PRIVATE);
        setContentView(R.layout.activity_splash_screen);
        key = shred.getString(TAG_KEY, "null");


        // Logcat.Save(SplashScreenActivity.this);
        // OneSignal Initialization
        deviceid = Helper.getDeviceId(SplashScreenActivity.this);
        OneSignal.idsAvailable((userId, registrationId) -> {
            //     String text = "OneSignal UserID:\n" + userId + "\n\n";
            UUID = userId;
            //    Log.d("UUID",UUID);
            UUID = AESUtils.DarKnight.getEncrypted(UUID);


        });
        try {
            Check();
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        S();

    }
    public native void S();
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                //  Log.v(TAG,"Permission is granted");
                return true;
            } else {
                // Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            // Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private  void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if(imgLoad.Load(SplashScreenActivity.this).equals("1")){
            finish();
        }
        else{
            isStoragePermissionGranted();
            stopService(new Intent(SplashScreenActivity.this,FloatLogo.class));
            stopService(new Intent(SplashScreenActivity.this, FreeFloatLogo.class));
            stopService(new Intent(SplashScreenActivity.this,logo.class));
            stopService(new Intent(SplashScreenActivity.this, flogo.class));
            new BackgroundSplashTask().execute();

        }
    }


    public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {
        if (!stateChanges.getFrom().getUserSubscriptionSetting() && !stateChanges.getTo().getUserSubscriptionSetting()) {
            // get player ID
         UUID = stateChanges.getTo().getUserId();
        }
     //   Log.i("Debug", "onOSPermissionChanged: " + stateChanges);
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
            JSONObject params = new JSONObject();
            String rq = null;
            try {
                params.put(TAG_KEY, key);
                params.put(TAG_DEVICEID, deviceid);
                params.put(TAG_ONESIGNALID, UUID);
                rq = jsonParserString.makeHttpRequest(url, params);
                //converting response to json object
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rq;
        }
            @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    boolean isFirstStart = shred.getBoolean("firstStart", true);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    if (s == null || s.isEmpty()) {
                        Toast.makeText(SplashScreenActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                        return;
                    }
                    try {
                        JSONObject ack = new JSONObject(s);
                        String decData = Helper.profileDecrypt(ack.get("Data").toString(), ack.get("Hash").toString());
                        if (!Helper.verify(decData, ack.get("Sign").toString(), JSONParserString.publickey)) {
                            Toast.makeText(SplashScreenActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            JSONObject obj = new JSONObject(decData);
                         //   Log.d("login", obj.toString());
                            error = obj.getBoolean(TAG_ERROR);
                            if (!error) {
                                getduration = obj.getLong(TAG_DURATION);
                                msg = obj.getString(TAG_MSG);
                                safe = obj.getBoolean("5");
                                editor.putLong(TAG_DURATION, getduration);
                                editor.apply();
                                //    Log.d("splash", String.valueOf(getduration));
                            }
                            newversion = obj.getString(TAG_APP_NEWVERSION);
                            whatsNewData = obj.getString(data);
                            updateurl = obj.getString("updateurl");
                            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                            version = pInfo.versionName;
                            // If the activity has never started before...
                            if (isFirstStart) {
                                if (Float.parseFloat(version) < Float.parseFloat(newversion)) {
                                    Intent intent = new Intent(SplashScreenActivity.this, AppUpdaterActivity.class);
                                    intent.putExtra(TAG_APP_NEWVERSION, newversion);
                                    intent.putExtra(data, whatsNewData);
                                    intent.putExtra("force",obj.getBoolean("force"));
                                    intent.putExtra("updateurl", updateurl);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else if (ismaintaince) {
                                    Intent intent = new Intent(SplashScreenActivity.this, activityMaintain.class);
                                    startActivity(intent);
                                } else {
                                    //user not signedin
                                    Intent i = new Intent(SplashScreenActivity.this, GameActivity.class);
                                    startActivity(i);
                                }
                                // Launch app intro

                            } else {
                                // Create a new boolean and preference and set it to true
                                String isSignedin = shred.getString(TAG_KEY, "null");

                                if (!isSignedin.equals("null")) {
                                    if (!(getduration == 0)) {
                                                                                    //saving to prefrences m
                                            editor.putLong(TAG_DURATION, getduration).apply();
                                            editor.putString(TAG_KEY, key);
                                            editor.apply();
                                            Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                                            intent.putExtra("safe", safe);
                                            startActivity(intent);

                                    } else {
                                        //   Log.d("duration", String.valueOf(getduration));
                                        Toast.makeText(SplashScreenActivity.this, "Subscription Expired ", Toast.LENGTH_LONG).show();
                                        shred.edit().clear().apply();
                                        Intent i = new Intent(SplashScreenActivity.this, GameActivity.class);
                                        startActivity(i);
                                    }

                                } else {
                                    if (Float.parseFloat(version) < Float.parseFloat(newversion)) {
                                        Intent intent = new Intent(SplashScreenActivity.this, AppUpdaterActivity.class);
                                        intent.putExtra(TAG_APP_NEWVERSION, newversion);
                                        intent.putExtra(data, whatsNewData);
                                        intent.putExtra("force",obj.getBoolean("force"));
                                        intent.putExtra("updateurl", updateurl);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        //user not signedin
                                        Intent i = new Intent(SplashScreenActivity.this, GameActivity.class);
                                        startActivity(i);
                                    }
                                }
                            }

                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

                }
        }
    }



