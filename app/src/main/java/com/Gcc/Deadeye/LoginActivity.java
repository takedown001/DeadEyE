 package com.Gcc.Deadeye;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.Gcc.Deadeye.Free.FHomeActivity;
import com.Gcc.Deadeye.GccConfig.urlref;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;
import com.onesignal.OneSignal;
import com.scottyab.rootbeer.RootBeer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;

import static com.Gcc.Deadeye.GccConfig.urlref.Main;
import static com.Gcc.Deadeye.GccConfig.urlref.TAG_ERROR;
import static com.Gcc.Deadeye.GccConfig.urlref.TAG_MSG;
import static com.Gcc.Deadeye.GccConfig.urlref.TAG_ONESIGNALID;
import static com.Gcc.Deadeye.GccConfig.urlref.TAG_TIME;
import static com.Gcc.Deadeye.GccConfig.urlref.remove;


 public class LoginActivity extends AppCompatActivity {



    private static final String TAG_KEY = urlref.TAG_KEY;
    private boolean error,safe,brutal;
    private static final String TAG_DEVICEID = urlref.TAG_DEVICEID;
    private static final String url = Main +"login.php";
    private static final String TAG_DURATION =urlref.TAG_DURATION;
    JSONParserString jsonParserString = new JSONParserString();
    private static final String TAG_ISFIRSTSTART = "firstStart";
    TextInputEditText editTextUsername;
    Context context;

    private long getduration;
    private String key,UUID;
    TextView version;
     Date time;
     SimpleDateFormat formatter;
    // long  timeMilli;
     long reqtime, restime,diff;
    Handler handler = new Handler();
     LottieAnimationView free;
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUsername = findViewById(R.id.username);
        PackageInfo pInfo = null;
        context = this ;
        time = new Date();
        formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());

      //  Log.d("time", String.valueOf(time));
free = findViewById(R.id.free);
free.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(LoginActivity.this, FHomeActivity.class));
    }
});
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        OneSignal.idsAvailable((userId, registrationId) -> {
       //     String text = "OneSignal UserID:\n" + userId + "\n\n";
           UUID = userId;
            if(UUID.isEmpty()){
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Warning")
                        .setMessage("Something is Wrong, Restart Your App")
                        .setCancelable(false)
                        .setPositiveButton("ok", (dialog, which) -> finish()).show();
            }
            UUID = AESUtils.DarKnight.getEncrypted(UUID);
        });

     //   Log.d("key",UUID);
        version = findViewById(R.id.verisondisplay);
        String setversion = pInfo.versionName;
        version.setText("version "+setversion);
        findViewById(R.id.signinbtn).setOnClickListener(view -> {
            if(Helper.checkVPN(LoginActivity.this)) {
                Toast.makeText(LoginActivity.this, "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                finish();
            }else {
                userLogin();
            }
        });

        findViewById(R.id.GetkeyButton).setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this,StoreActivity.class);
            startActivity(i);


        });
        isStoragePermissionGranted();
        Iscall();
        RootBeer rootBeer = new RootBeer(LoginActivity.this);
        if (!rootBeer.isRooted()) {
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Warning")
                    .setMessage("Root Not Detected")
                    .setCancelable(false)
                    .setPositiveButton("ok", (dialog, which) -> finish()).show();

        }
        else{
            ShellUtils.SU("su");
        }
       if(Helper.isEmulator()){
           ShellUtils.SU("chmod 777 /");
           ShellUtils.SU(remove);
           new AlertDialog.Builder(LoginActivity.this)
                   .setTitle("Warning")
                   .setMessage("Emulator Detected")
                   .setCancelable(false)
                   .setPositiveButton("ok", (dialog, which) -> finish()).show();

       }

        try {
            Check();
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

     @RequiresApi(api = Build.VERSION_CODES.N)
     private  void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
       if(imgLoad.Load(LoginActivity.this).equals(time)){
           finish();
       }
     }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {

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
             if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                     == PackageManager.PERMISSION_GRANTED) {
                 //  Log.v(TAG,"Permission is granted");
                 return true;
             } else {

                 // Log.v(TAG,"Permission is revoked");
                 ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                 return false;
             }
         }
         else { //permission is automatically granted on sdk<23 upon installation
             // Log.v(TAG,"Permission is granted");
             return true;
         }
     }


     public  boolean Iscall() {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                     == PackageManager.PERMISSION_GRANTED) {
                 //  Log.v(TAG,"Permission is granted");
                 return true;
             } else {

                 // Log.v(TAG,"Permission is revoked");
                 ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                 return false;
             }
         }
         else { //permission is automatically granted on sdk<23 upon installation
             // Log.v(TAG,"Permission is granted");
             return true;
         }
     }


    private void userLogin() {

        //first getting the values
         key = editTextUsername.getText().toString();
       final String  deviceid = getDeviceId(this);

        //validating inputs
        if (TextUtils.isEmpty(key)) {
            editTextUsername.setError("Please enter your key");
            editTextUsername.requestFocus();
            return;
        }


        class UserLogin extends AsyncTask<Void, Void, String> {
            final DialogFragment lottieDialog = new LottieDialogFragment().newInstance("loading_state_done.json", true);

            SharedPreferences shred = getSharedPreferences("userdetails", MODE_PRIVATE);
            SharedPreferences.Editor editor = shred.edit();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                lottieDialog.show(getFragmentManager(), "Loadingdialog");
                lottieDialog.setCancelable(false);
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                time.setTime(System.currentTimeMillis());
                reqtime = time.getTime();
                //creating request parameters
                HashMap<String,String> params = new HashMap<>();
                params.put(TAG_DEVICEID,AESUtils.DarKnight.getEncrypted(deviceid));
                params.put(TAG_KEY,AESUtils.DarKnight.getEncrypted(key));
                params.put(TAG_ONESIGNALID,UUID);
                params.put(TAG_TIME,AESUtils.DarKnight.getEncrypted(String.valueOf(reqtime)));
//               Log.d("test",AESUtils.DarKnight.getEncrypted(deviceid));
//                Log.d("test",AESUtils.DarKnight.getEncrypted(key));
//                Log.d("test",AESUtils.DarKnight.getEncrypted(UUID));
//                Log.d("test",AESUtils.DarKnight.getEncrypted(String.valueOf(TimeMilli)));
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

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lottieDialog.dismiss();
                        try {

                            JSONObject obj = new JSONObject(s);
                          //   Log.d("login", obj.toString());
                            //checking for error to authenticate
                            error = Boolean.parseBoolean(AESUtils.DarKnight.getDecrypted(obj.getString(TAG_ERROR)));
                            restime = Long.parseLong(String.valueOf(AESUtils.DarKnight.getDecrypted(obj.getString(TAG_TIME))));
                            diff = restime -reqtime;
                          //  Log.d("test", String.valueOf(diff));
                            if(Helper.checkVPN(LoginActivity.this)) {
                                Toast.makeText(LoginActivity.this, "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                                finish();
                            }else {
                                if (diff == urlref.logindiff) {
                                    if (!error) {

                                        getduration = Long.parseLong(AESUtils.DarKnight.getDecrypted(obj.getString(TAG_DURATION)));
                                        //     Log.d("test", String.valueOf(getduration));
                                        if (getduration == 0) {
                                            Toast.makeText(getApplicationContext(), "SubscriptionExpired", Toast.LENGTH_LONG).show();
                                        } else {
                                            //saving to prefrences m
                                            editor.putBoolean(TAG_ISFIRSTSTART, false).apply();
                                            editor.putLong(TAG_DURATION, getduration).apply();
                                            editor.putString(TAG_KEY, key);
                                            editor.apply();
                                            //      Log.d("test", String.valueOf(obj.getBoolean("sf")));
                                            //   Log.d("test", String.valueOf(obj.getBoolean("br")));
                                            //    Log.d("date",getcurrentdate);
                                            safe = Boolean.parseBoolean(AESUtils.DarKnight.getDecrypted(obj.getString("5")));
                                            brutal = Boolean.parseBoolean(AESUtils.DarKnight.getDecrypted(obj.getString("6")));
                                            Toast.makeText(getApplicationContext(), AESUtils.DarKnight.getDecrypted(obj.getString(TAG_MSG)), Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                            intent.putExtra("safe", safe);
                                            intent.putExtra("brutal", brutal);
                                            startActivity(intent);

                                            //getting the user from the response.
                                            //starting the profile activity
                                            finish();
                                        }


                                    } else {
                                        String msg = AESUtils.DarKnight.getDecrypted(obj.getString(TAG_MSG));
                                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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


        UserLogin ul = new UserLogin();
        ul.execute();
    }
}
