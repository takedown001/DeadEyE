package com.Gcc.Deadeye.Free;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.Gcc.Deadeye.ESPView;
import com.Gcc.Deadeye.R;
import com.Gcc.Deadeye.ShellUtils;
import com.Gcc.Deadeye.imgLoad;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

import static com.Gcc.Deadeye.GccConfig.urlref.time;

//import android.support.v7.app.*;


public class EspFreeMainActivity extends AppCompatActivity {

    String gameName = "com.tencent.ig";
    static int gameType = 1;
    // Used to load the 'native-lib' library on application startup.


    static{
        System.loadLibrary("vxposed");
    }


    public static boolean  isDisplay = false;
    public static boolean isMenuDis = false;
    //WindowManager.LayoutParams params;
    Context ctx;

    public static String socket;

    Process process;

    public String daemonPath;

    public String daemonPath64;



    static boolean is32 = false;
    static boolean is64 = false;
    static boolean bitMod = false;

    private String  version;
    public static boolean isDaemon = false;
    Button back;
    InterstitialAd mInterstitialAd;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckFloatViewPermission();
        mInterstitialAd = new InterstitialAd(this);
        back = findViewById(R.id.stopesp);
        // set the ad unit ID
        gameType = getIntent().getIntExtra("game",1);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ShellUtils.SU("am force-stop "+gameName);
                stopService(new Intent(ctx, FreeOverlay.class));
                stopService(new Intent(ctx, FreeService.class));
                stopService(new Intent(ctx, ESPView.class));
                isDisplay = false;
                ShellUtils.SU("rm -rf "+ctx.getFilesDir().toString()+"/libsys.so");
                //startDaemon();
                isDaemon = false;
                FreeOverlay.isRunning=false;
                //		stopService(new Intent(ctx, BrutalService.class));
                finish();			}
        });
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });

        ctx = this;
        SharedPreferences shred = getSharedPreferences("userdetails", MODE_PRIVATE);
        if (!isConfigExist()) {
            Init();
        }

        version = shred.getString("version", "32");



        if (version.equals("32")) {
            is32 = true;
            is64 = false;
            bitMod = true;
        } else {
            is64 = true;
            is32 = false;
            bitMod = true;
        }
//			Log.d("bit",version);
		Log.d("game", String.valueOf(gameType));
        ExecuteElf("su -c");
        ShellUtils.SU("setenforce 0");
        ShellUtils.SU("chmod 777 "+ctx.getFilesDir().toString()+"/libsys.so");

        loadAssets();
    //    loadAssets64();

        socket = daemonPath;
        cheat();

        //Log.d("1","herer");
    }
    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private  void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if(imgLoad.Load(EspFreeMainActivity.this).equals(time)){
            finish();
        }
    }
    public void cheat() {


        if (isDisplay == false && isMenuDis == false) {


            if (isDisplay == false && isMenuDis == false) {

                if (is32) {

                    socket = "su -c " + daemonPath;
                    //			MemHack = "su -c " + daemonPathMEM;
                } else if (is64) {

                    socket = "su -c " + daemonPath64;
                    //		MemHack = "su -c " + daemonPath64;


                }
            }


            startPatcher();
            startService(new Intent(this, FreeOverlay.class));

            // ShowFloatWindow();
            //Log.d("test","patch");
            isDisplay = true;
            //startDaemon();
            isDaemon = true;
            //Toast.makeText(MainActivity.this,socket,Toast.LENGTH_LONG).show();


        } else {

            Toast.makeText(EspFreeMainActivity.this, "Already Started !!", Toast.LENGTH_LONG).show();

        }


        // FloatButton();
        //  startPatcher();
    }



    private void ExecuteElf(String shell)
    {
        String s=shell;

        try
        {
            Runtime.getRuntime().exec(s,null,null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    public void CheckFloatViewPermission()
    {
        if (!Settings.canDrawOverlays(this))
        {
            Toast.makeText(this,"Enable Floating Permission ",Toast.LENGTH_LONG).show();
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
        }
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (FreeOverlay.class.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }




    void startPatcher() {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 123);
        } else {
            startFloater();
        }
    }

    private void startFloater() {
        if (!isServiceRunning()) {
            startService(new Intent(this, FreeOverlay.class));
        } else {
            Toast.makeText(this, "Service Already Running!", Toast.LENGTH_SHORT).show();
        }
    }




    public void Shell(String str) {

        DataOutputStream dataOutputStream = null;
        try {
            process = Runtime.getRuntime().exec(str);
        } catch (IOException e) {
            e.printStackTrace();
            process = null;
        }
        if (process != null) {
            dataOutputStream = new DataOutputStream(process.getOutputStream());
        }
        try {
            dataOutputStream.flush();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            process.waitFor();
        } catch (InterruptedException e3) {
            e3.printStackTrace();
        }
    }

    public void startDaemon(){
        new Thread()
        {
            public void run()
            {
                Shell(socket);

            }
        }.start();
    }
    public void loadAssets()
    {

        String pathf = getFilesDir().toString()+"/xvpn";
        try
        {
            OutputStream myOutput = new FileOutputStream(pathf);
            byte[] buffer = new byte[1024];
            int length;
            InputStream myInput = getAssets().open("xvpn");
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myInput.close();
            myOutput.flush();
            myOutput.close();

        }

        catch (IOException e)
        {
        }


        daemonPath = getFilesDir().toString()+"/xvpn";


        try{
            Runtime.getRuntime().exec("chmod 777 "+daemonPath);
        }
        catch (IOException e)
        {
        }

    }

//    public void loadAssets64()
//    {
//
//        String pathf = getFilesDir().toString()+"/sock64";
//        try
//        {
//            OutputStream myOutput = new FileOutputStream(pathf);
//            byte[] buffer = new byte[1024];
//            int length;
//            InputStream myInput = getAssets().open("sock64");
//            while ((length = myInput.read(buffer)) > 0) {
//                myOutput.write(buffer, 0, length);
//            }
//            myInput.close();
//            myOutput.flush();
//            myOutput.close();
//
//        }
//
//        catch (IOException e)
//        {
//        }
//
//        daemonPath64 = getFilesDir().toString()+"/sock64";
//
//
//        try{
//            Runtime.getRuntime().exec("chmod 777 "+daemonPath64);
//        }
//        catch (IOException e)
//        {
//        }
//
//    }

    void Init(){
        SharedPreferences sp=getApplicationContext().getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed= sp.edit();
        ed.putInt("fps",30);
        ed.putBoolean("Box",true);
        ed.putBoolean("Line",true);
        ed.putBoolean("Distance",false);
        ed.putBoolean("Health",false);
        ed.putBoolean("Name",false);
        ed.putBoolean("Head Position",false);
        ed.putBoolean("Back Mark",false);
        ed.putBoolean("Skelton",false);
        ed.putBoolean("Grenade Warning",false);
        ed.putBoolean("Enemy Weapon",false);
        ed.apply();
    }
    boolean isConfigExist(){
        SharedPreferences sp=getApplicationContext().getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.contains("fps");
    }

}
