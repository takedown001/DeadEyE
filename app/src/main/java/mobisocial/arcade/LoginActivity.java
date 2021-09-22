package mobisocial.arcade;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import mobisocial.arcade.GccConfig.urlref;

import com.google.android.material.textfield.TextInputEditText;
import com.onesignal.OneSignal;
import com.scottyab.rootbeer.RootBeer;
import com.topjohnwu.superuser.Shell;
import com.yeyint.customalertdialog.CustomAlertDialog;

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;

import static mobisocial.arcade.GccConfig.urlref.Main;
import static mobisocial.arcade.GccConfig.urlref.TAG_ERROR;
import static mobisocial.arcade.GccConfig.urlref.TAG_LITE;
import static mobisocial.arcade.GccConfig.urlref.TAG_MSG;
import static mobisocial.arcade.GccConfig.urlref.TAG_ONESIGNALID;


public class LoginActivity extends AppCompatActivity {

     static{
         System.loadLibrary("sysload");
     }
    private static final String TAG_KEY = urlref.TAG_KEY;
    private boolean error,safe;
    private static final String TAG_DEVICEID = urlref.TAG_DEVICEID;
    private static final String url = Main+"login.php";
    private static final String TAG_DURATION =urlref.TAG_DURATION;
    JSONParserString jsonParserString = new JSONParserString();
    private static final String TAG_ISFIRSTSTART = "firstStart";
    TextInputEditText editTextUsername;
    Context context;
    private Boolean islite;
    private static int backbackexit = 1;
    private long getduration;
    private String key,UUID;
    TextView version;
    public  PackageInfo pInfo = null;
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUsername = findViewById(R.id.username);
        context = this ;
        OneSignal.idsAvailable((userId, registrationId) -> {
           UUID = userId;
        });
        version = findViewById(R.id.verisondisplay);
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version.setText("version "+pInfo.versionName);
        findViewById(R.id.signinbtn).setOnClickListener(view -> {
            if(Helper.checkVPN(LoginActivity.this)) {
                Toast.makeText(LoginActivity.this, "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                finish();
            }else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            if(!imgLoad.isOnline(LoginActivity.this)){
                                userLogin();
                            }else{
                                Toast.makeText(LoginActivity.this,"Internet Not Available",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();

            }
        });

        findViewById(R.id.GetkeyButton).setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://shop.gamesploit.com"));
            startActivity(i);
        });



        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            a11();
        }else {
            checkandroid();
        }
    }

    public void a11()
    {

        if(!new RootBeer(LoginActivity.this).isRooted() && !Shell.rootAccess()){
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Warning")
                    .setMessage("You Are Non-root ,We Don't Support Right Now")
                    .setCancelable(false)
                    .setPositiveButton("ok", (dialog, which) -> finish()).show();
        }
        if(new RootBeer(LoginActivity.this).isRooted()  && !Shell.rootAccess()){
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Warning")
                    .setMessage("Root Access Was Not Granted")
                    .setCancelable(false)
                    .setPositiveButton("ok", (dialog, which) -> finish()).show();

        }
        else{
            ShellUtils.SU("su");
        }
    }

    private void checkandroid(){

        CustomAlertDialog Androidcheck = new CustomAlertDialog(this,  CustomAlertDialog.DialogStyle.FILL_STYLE);
        Androidcheck.setCancelable(false);
        isStoragePermissionGranted();

        if(!new RootBeer(LoginActivity.this).isRooted() && !Shell.rootAccess()){
            Androidcheck.setDialogType(CustomAlertDialog.DialogType.WARNING);
            Androidcheck.setDialogImage(getDrawable(R.drawable.alert),0); // no tint
            Androidcheck.setImageSize(150,150);
            Androidcheck.setAlertMessage("Root Access Was Not Granted");
            Androidcheck.create();
            Androidcheck.show();
            Androidcheck.setPositiveButton("Grant", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShellUtils.SU("su");
                    Androidcheck.dismiss();
                }
            });
            Androidcheck.setNegativeButton("Exit", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        finalize();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    Androidcheck.cancel();
                }
            });
        }else{
            ShellUtils.SU("su");
        }
            new Thread(new Runnable() {

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {
                    new Handler(Looper.getMainLooper()).post(() -> {
                    try {
                        Check();
                    } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                });
                }
            }).start();
        }




     @RequiresApi(api = Build.VERSION_CODES.N)
     private  void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
       if(imgLoad.Load(LoginActivity.this).equals("1")){
           finish();
       }
     }




     public  boolean isStoragePermissionGranted() {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
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



    @Override
    public void onBackPressed() {
        CustomAlertDialog Androidcheck = new CustomAlertDialog(this,  CustomAlertDialog.DialogStyle.NO_ACTION_BAR);
        if (backbackexit >= 2) {
            Androidcheck.setCancelable(false);
            Androidcheck.setDialogType(CustomAlertDialog.DialogType.INFO);
            Androidcheck.setAlertTitle("Exit");
            Androidcheck.setImageSize(150,150);
            Androidcheck.setAlertMessage("Are you sure you want to exit back ?");
            Androidcheck.create();
            Androidcheck.show();
            Androidcheck.setPositiveButton("Yes", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, GameActivity.class));

                    Androidcheck.dismiss();
                }
            });
            Androidcheck.setNegativeButton("NO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        finalize();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    Androidcheck.cancel();
                }
            });
//					super.onBackPressed();
        } else {
            backbackexit++;
            Toast.makeText(getBaseContext(), "Press again to Exit", Toast.LENGTH_SHORT).show();
        }
    }


    private void userLogin() {

        //first getting the values
         key = editTextUsername.getText().toString();
       final String  deviceid = Helper.getDeviceId(this);

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
                //creating request parameters
                JSONObject params = new JSONObject();

                String rq = null;
                try {
                    params.put(TAG_DEVICEID,deviceid);
                    params.put(TAG_KEY,key);
                    params.put(TAG_ONESIGNALID,UUID);
                    rq = jsonParserString.makeHttpRequest(url, params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //returing the response
                return rq;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (lottieDialog != null) {
                    lottieDialog.dismiss();
                }
                if (s == null || s.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                    return;
                }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject ack = new JSONObject(s);
                               // Log.d("test", String.valueOf(ack));
                                String decData = Helper.profileDecrypt(ack.get("Data").toString(), ack.get("Hash").toString());
                                if (!Helper.verify(decData, ack.get("Sign").toString(), JSONParserString.publickey)) {
                                    Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                                    return;
                                } else {
                                    JSONObject obj = new JSONObject(decData);
                                  //  Log.d("test",obj.toString());
                                    error = obj.getBoolean(TAG_ERROR);

                                    if (Helper.checkVPN(LoginActivity.this)) {
                                        Toast.makeText(LoginActivity.this, "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        if (!error) {
                                            safe = obj.getBoolean("5");
                                            getduration = obj.getLong(TAG_DURATION);
                                          //  Log.d("test", String.valueOf(getduration));
                                            if (getduration == 0) {
                                                Toast.makeText(getApplicationContext(), obj.getString(TAG_MSG), Toast.LENGTH_LONG).show();
                                            } else {
                                                    //saving to prefrences m
                                                    editor.putBoolean(TAG_ISFIRSTSTART, false).apply();
                                                    editor.putLong(TAG_DURATION, getduration).apply();
                                                    editor.putString(TAG_KEY, key);
                                                    editor.apply();
                                                    Toast.makeText(getApplicationContext(), obj.getString(TAG_MSG), Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                    intent.putExtra("safe", safe);
                                                    startActivity(intent);
                                            }
                                            finish();
                                            finalize();
                                        } else {
                                            Toast.makeText(getApplicationContext(), obj.getString(TAG_MSG), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }

                    });

            }
        }
        UserLogin ul = new UserLogin();

        ul.execute();
    }
}