package mobisocial.arcade;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import mobisocial.arcade.GccConfig.urlref;
import mobisocial.arcade.free.FreeFloatLogo;
import mobisocial.arcade.lite.HomeActivityLite;

import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;

import static mobisocial.arcade.GccConfig.urlref.TAG_LITE;
import static mobisocial.arcade.GccConfig.urlref.TAG_MSG;
import static mobisocial.arcade.GccConfig.urlref.TAG_ONESIGNALID;
import static mobisocial.arcade.GccConfig.urlref.TAG_TIME;


public class SplashScreenActivity extends Activity  implements OSSubscriptionObserver{

    private static final int SPLASH_SHOW_TIME = 2000;
    private static final String TAG_KEY = urlref.TAG_KEY;
    private static final String TAG_ERROR = urlref.TAG_ERROR;
    private static final String TAG_DEVICEID = urlref.TAG_DEVICEID;
    private static final String url = urlref.Main + "login.php";
    private static final String TAG_DURATION = urlref.TAG_DURATION;
    private  boolean error,safe,brutal,Aerror;
    //Prefrance
    private String newversion;
    private String data = "data";
    private String updateurl;
    private String whatsNewData,msg;
    private boolean ismaintaince;
    private static final String TAG_APP_NEWVERSION = "newversion";
    private Boolean islite;
    long reqtime, restime,diff;
    Handler handler = new Handler();
    JSONParserString jsonParserString = new JSONParserString();
    private String key, deviceid="null",version,UUID="null";
    private long getduration;
    Date time;
    RequestHandler requestHandler = new RequestHandler();
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
        time = new Date();
        // OneSignal Initialization
        deviceid = AESUtils.DarKnight.getEncrypted(getDeviceId(SplashScreenActivity.this));
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

    }
    public String getDeviceId(Context context) {

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
            params.put(TAG_ONESIGNALID, UUID);
            Ar.put(TAG_KEY, AESUtils.DarKnight.getEncrypted(key));
            params.put(TAG_TIME, AESUtils.DarKnight.getEncrypted(String.valueOf(reqtime)));

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
                    updateurl = obj.getString("updateurl");
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
            runOnUiThread(new Runnable(){

              @Override
              public void run() {
                  boolean isFirstStart = shred.getBoolean("firstStart", true);
                  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                          WindowManager.LayoutParams.FLAG_FULLSCREEN);
                  try {
                      JSONObject obj = new JSONObject(result);
                      error = Boolean.parseBoolean(AESUtils.DarKnight.getDecrypted(obj.getString(TAG_ERROR)));
                      restime = Long.parseLong(String.valueOf(AESUtils.DarKnight.getDecrypted(obj.getString(TAG_TIME))));
                      diff = restime - reqtime;
                      // Log.d("test", String.valueOf(error));
                      if (diff == urlref.logindiff) {
                          if (!error) {
                              getduration = Long.parseLong(AESUtils.DarKnight.getDecrypted(obj.getString(TAG_DURATION)));
                              islite = Boolean.valueOf(AESUtils.DarKnight.getDecrypted(obj.getString(TAG_LITE)));
                              msg = AESUtils.DarKnight.getDecrypted(obj.getString(TAG_MSG));
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
                              intent.putExtra(data, whatsNewData);
                              intent.putExtra("updateurl", updateurl);
                              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                              startActivity(intent);
                          } else if (ismaintaince) {
                              Intent intent = new Intent(SplashScreenActivity.this, activityMaintain.class);
                              startActivity(intent);
                          } else {
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
                                  if (islite) {
                                      //saving to prefrences m
                                      editor.putLong(TAG_DURATION, getduration).apply();
                                      editor.putString(TAG_KEY, key);
                                      editor.apply();
                                      Intent intent = new Intent(SplashScreenActivity.this, HomeActivityLite.class);
                                      intent.putExtra("safe", safe);
                                      intent.putExtra("brutal", brutal);
                                      startActivity(intent);
                                  } else {
                                      //user signedin
                                      Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
                                      i.putExtra("safe", safe);
                                      i.putExtra("brutal", brutal);
                                      startActivity(i);
                                  }
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
                                  intent.putExtra(data, whatsNewData);
                                  intent.putExtra("updateurl", updateurl);
                                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                  startActivity(intent);
                              }  else {
                                  //user not signedin
                                  Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                                  startActivity(i);
                              }
                          }
                      }
                  }
                  finish();              }
          });
                }
            },SPLASH_SHOW_TIME);




            // Create a new boolean and preference and set it to true

        }
    }
}


