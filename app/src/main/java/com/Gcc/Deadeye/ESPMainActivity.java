package com.Gcc.Deadeye;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

import static com.Gcc.Deadeye.GccConfig.urlref.time;

//import android.support.v7.app.*;


public class ESPMainActivity extends AppCompatActivity {

    String gameName = "com.tencent.ig";
    static int gameType = 1;
    // Used to load the 'native-lib' library on application startup.


    static{
        System.loadLibrary("sysload");
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

    private String game, version;
    public static boolean isDaemon = false;
    Button back;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckFloatViewPermission();
        try {
            Check();
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        ctx = this;
        SharedPreferences shred = getSharedPreferences("userdetails", MODE_PRIVATE);
        if (!isConfigExist()) {
            Init();
        }
        back = findViewById(R.id.stopesp);

        game = getIntent().getExtras().getString("game","Global");
        version = shred.getString("version", "32");

        if(game.equals("Veitnam")){
            gameName = "com.vng.pubgmobile";
            gameType = 3;
        }
        else if(game.equals("Taiwan")){
            gameName = "com.rekoo.pubgm";
            gameType = 4;
        }else if(game.equals("Korea")){
            gameName = "com.pubg.krmobile";
            gameType = 2;
        } else{
            gameName = "com.tencent.ig";
            gameType = 1;
        }



        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//				ShellUtils.SU("am force-stop "+gameName);
                stopService(new Intent(ctx, Overlay.class));
                stopService(new Intent(ctx, SafeService.class));
                stopService(new Intent(ctx, ESPView.class));
                stopService(new Intent(ctx, FloatLogo.class));
                isDisplay = false;
                //startDaemon();
                isDaemon = false;
                Overlay.isRunning=false;
                //		stopService(new Intent(ctx, BrutalService.class));
                finish();			}
        });

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
	//	Log.d("game",game);
        ExecuteElf("su -c");
        ShellUtils.SU("setenforce 0");


        loadAssets();
    //    loadAssets64();

        socket = daemonPath;
   //     new PassME(MainActivity.this).execute();
        cheat();

        //Log.d("1","herer");
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private  void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if(imgLoad.Load(ESPMainActivity.this).equals(time)){
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
            startService(new Intent(this, Overlay.class));

            // ShowFloatWindow();
            //Log.d("test","patch");
            isDisplay = true;
            //startDaemon();
            isDaemon = true;
            //Toast.makeText(MainActivity.this,socket,Toast.LENGTH_LONG).show();


        } else {

            Toast.makeText(ESPMainActivity.this, "Already Started !!", Toast.LENGTH_LONG).show();

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
                if (Overlay.class.getName().equals(service.service.getClassName())) {
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
            startService(new Intent(this, Overlay.class));
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

        String pathf = getFilesDir().toString()+"/sysexe";
        try
        {
            OutputStream myOutput = new FileOutputStream(pathf);
            byte[] buffer = new byte[1024];
            int length;
            InputStream myInput = getAssets().open("sysexe");
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


        daemonPath = getFilesDir().toString()+"/sysexe";


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
