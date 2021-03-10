package com.Gcc.Deadeye;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.Gcc.Deadeye.GccConfig.urlref;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyStoreException;
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.Gcc.Deadeye.GccConfig.urlref.TAG_MSG;
import static com.Gcc.Deadeye.GccConfig.urlref.TAG_ONESIGNALID;
import static com.Gcc.Deadeye.GccConfig.urlref.TAG_TIME;
import static com.Gcc.Deadeye.LoginActivity.getDeviceId;

public class PassME extends AsyncTask<Void, Void, String> {
    private Context instance;
    private static final String TAG_KEY = urlref.TAG_KEY;
    private static final String TAG_ERROR = urlref.TAG_ERROR;
    private static final String TAG_DEVICEID = urlref.TAG_DEVICEID;
    private static final String url = urlref.Main + "p.php";
    private static final String TAG_DURATION = urlref.TAG_DURATION;
    private boolean error, safe, brutal;
    long reqtime, restime,diff;
    //Prefrance
    Date time;
    String UUID = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();

    Handler handler = new Handler();
    JSONParserString jsonParserString = new JSONParserString();
    private String key, deviceid;
    private long getduration;

    public PassME(Context context) {
        this.instance = context;
        deviceid = getDeviceId(instance);
        time = new Date();

    }
        @Override
        protected String doInBackground(Void... voids) {
            SharedPreferences shred = instance.getSharedPreferences("userdetails", MODE_PRIVATE);
            time.setTime(System.currentTimeMillis());
            reqtime = time.getTime();
            key = shred.getString(TAG_KEY,"null");
            HashMap<String, String> params = new HashMap<>();
            params.put(TAG_DEVICEID,AESUtils.DarKnight.getEncrypted(deviceid));
            params.put(TAG_KEY,AESUtils.DarKnight.getEncrypted(key));
            params.put(TAG_ONESIGNALID, UUID);
            params.put(TAG_TIME,AESUtils.DarKnight.getEncrypted(String.valueOf(reqtime)));
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SharedPreferences shred = instance.getSharedPreferences("userdetails", MODE_PRIVATE);
            SharedPreferences.Editor editor = shred.edit();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {

                        JSONObject obj = new JSONObject(s);
                        //  Log.d("login", obj.toString());
                        //checking for error to authenticate
                        error = Boolean.parseBoolean(AESUtils.DarKnight.getDecrypted(obj.getString(TAG_ERROR)));
                        restime = Long.parseLong(String.valueOf(AESUtils.DarKnight.getDecrypted(obj.getString(TAG_TIME))));
                        diff = restime -reqtime;
                        //    Log.d("test", String.valueOf(error));
                        if (Helper.checkVPN(instance)) {
                            Toast.makeText(instance, "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                        } else {
                            if (diff == urlref.logindiff) {
                                if (!error) {
                                    getduration = Long.parseLong(AESUtils.DarKnight.getDecrypted(obj.getString(TAG_DURATION)));
                                    //    Log.d("test", String.valueOf(getduration));
                                    if (getduration == 0) {
                                        Toast.makeText(instance, "SubscriptionExpired", Toast.LENGTH_LONG).show();
                                    } else {
                                        //saving to prefrences m
                                        // editor.clear().apply();
                                        editor.putLong(TAG_DURATION, getduration).apply();
                                        editor.putString(TAG_KEY, key);
                                        editor.apply();

                                    }
                                } else {
                                    String msg = AESUtils.DarKnight.getDecrypted(obj.getString(TAG_MSG));
                                    Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, 2000);
        }
    }

