package com.Gcc.Deadeye.Fragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.Gcc.Deadeye.AESUtils;
import com.Gcc.Deadeye.ESPView;
import com.Gcc.Deadeye.FloatLogo;
import com.Gcc.Deadeye.GccConfig.urlref;
import com.Gcc.Deadeye.Helper;
import com.Gcc.Deadeye.HomeActivity;
import com.Gcc.Deadeye.JavaUrlConnectionReader;
import com.Gcc.Deadeye.LoginActivity;
import com.Gcc.Deadeye.MainActivity;
import com.Gcc.Deadeye.Overlay;
import com.Gcc.Deadeye.R;
import com.Gcc.Deadeye.SafeService;
import com.Gcc.Deadeye.ShellUtils;
import com.Gcc.Deadeye.SplashScreenActivity;

import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;

import static android.content.Context.MODE_PRIVATE;
import static com.Gcc.Deadeye.GccConfig.urlref.canary;
import static com.Gcc.Deadeye.GccConfig.urlref.netgaurd;
import static com.Gcc.Deadeye.GccConfig.urlref.pcanary;


public class GlobalFragment extends Fragment implements View.OnClickListener {

    private final JavaUrlConnectionReader reader = new JavaUrlConnectionReader();
    private String data;

    String CheatL = urlref.Liveserver + "cheat.php";
    String CheatB = urlref.Betaserver + "cheat.php";

    private String version, deviceid;
    Handler handler = new Handler();
    private static final String TAG_DEVICEID =urlref.TAG_DEVICEID;
    private static final String TAG_VERSION = "v";
    ImageView taptoactivategl;
    boolean espcheck,safecheck,brutalcheck;
    public GlobalFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Check();
    }
    private void Check(){
        if(Helper.checkVPN(getActivity())) {
            Toast.makeText(getActivity(), "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
        if(Helper.isXposedActive()){
            getActivity().finish();
        }
        if(Helper.isXposedInstallerAvailable(getActivity())){
            getActivity().finish();
        }
        if (Helper.isAppRunning(getActivity(),netgaurd)){
            getActivity().finish();
        }
        if (Helper.isAppRunning(getActivity(),canary)){
            getActivity().finish();
        }
        if (Helper.isAppRunning(getActivity(),pcanary)){
            getActivity().finish();
        }
    }

    final DialogFragment lottieDialog = new LottieDialogFragment().newInstance("loadingdone.json", true);
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        Context ctx;
        ctx =getActivity();
        View rootViewone = inflater.inflate(R.layout.fragment_global, container, false);
        SharedPreferences shred = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
        SharedPreferences ga = getActivity().getSharedPreferences("game", MODE_PRIVATE);
        SharedPreferences.Editor g = ga.edit();
        g.putString("game", "Global").apply();
        version = shred.getString("version", "32");
        version = AESUtils.DarKnight.getEncrypted(version);
        final File daemon = new File(urlref.pathoflib+urlref.livelib);

        deviceid = LoginActivity.getDeviceId(getActivity());
        deviceid = AESUtils.DarKnight.getEncrypted(deviceid);

        Button cleanguest, fixgame, StartCheatGl, StopCheatGl,DeepFixGame;
        StartCheatGl = rootViewone.findViewById(R.id.startcheatgl);
        StopCheatGl = rootViewone.findViewById(R.id.stopcheatgl);
        cleanguest = rootViewone.findViewById(R.id.globalcleanguest);
        fixgame = rootViewone.findViewById(R.id.globalfixgame);
        DeepFixGame = rootViewone.findViewById(R.id.globaldeepfixclean);
        taptoactivategl = rootViewone.findViewById(R.id.taptoactivategl);
        taptoactivategl.setOnClickListener(this);


        espcheck = ga.getBoolean("espcheck",false);
        safecheck = ga.getBoolean("safecheck",false);
        brutalcheck = ga.getBoolean("brutalcheck",false);



        SharedPreferences getserver = getActivity().getSharedPreferences("server",MODE_PRIVATE);
        final DialogFragment antiban = new LottieDialogFragment().newInstance("antiban.json",true);
        antiban.setCancelable(false);

        final DialogFragment fixgameani = new LottieDialogFragment().newInstance("settings.json",true);
        fixgameani.setCancelable(false);
        final DialogFragment cleanguestani = new LottieDialogFragment().newInstance("tick-confirm.json",true);
        cleanguestani.setCancelable(false);

        StartCheatGl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(Helper.checkVPN(getActivity())){
                    Toast.makeText(getActivity(), "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                  getActivity().finish();
                }
                else {
                    antiban.show(getActivity().getFragmentManager(), "StartCheatGl");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            antiban.dismiss();
                            if (HomeActivity.beta) {

                                if (safecheck) {
                                    getActivity().startService(new Intent(getContext(), SafeService.class));
                                    ShellUtils.SU("chmod 777 " + getActivity().getFilesDir().toString()+"/liberror.so");
                                    PackageManager pm = getContext().getPackageManager();
                                    if (Helper.isPackageInstalled("com.tencent.ig", pm)) {
                                        Intent i = new Intent(getContext(), MainActivity.class);
                                        i.putExtra("game", "Global");
                                        startActivity(i);
                                        Toast.makeText(getContext(), "Wait While We Setting Up Things", Toast.LENGTH_LONG).show();
                                        ShellUtils.SU(
                                                "am start -n com.tencent.ig/com.epicgames.ue4.SplashActivity");

                                    } else {
                                        Toast.makeText(getContext(), "Game Not Installed", Toast.LENGTH_LONG).show();
                                    }
                                }
                                if (brutalcheck) {

                                }
                                else{
                                    PackageManager pm = getContext().getPackageManager();
                                    if (Helper.isPackageInstalled("com.tencent.ig", pm)) {
                                        Intent i = new Intent(getContext(), MainActivity.class);
                                        i.putExtra("game", "Global");
                                        startActivity(i);
                                        Toast.makeText(getContext(), "Wait While We Setting Up Things", Toast.LENGTH_LONG).show();
                                        ShellUtils.SU(
                                                "am start -n com.tencent.ig/com.epicgames.ue4.SplashActivity");

                                    } else {
                                        Toast.makeText(getContext(), "Game Not Installed", Toast.LENGTH_LONG).show();
                                    }
                                }
                                //            Log.d("betastartcheat", String.valueOf(HomeActivity.beta));
                                betastartcheat();
                            } else {
                                //Log.d("lolll", String.valueOf(espcheck));
                                if (safecheck) {
                                    getActivity().startService(new Intent(getContext(), SafeService.class));
                                    ShellUtils.SU("chmod 777 " + getActivity().getFilesDir().toString()+"/libtakedown.so");
                                    PackageManager pm = getContext().getPackageManager();
                                    if (Helper.isPackageInstalled("com.tencent.ig", pm)) {
                                        Intent i = new Intent(getContext(), MainActivity.class);
                                        i.putExtra("game", "Global");
                                        startActivity(i);
                                        Toast.makeText(getContext(), "Wait While We Setting Up Things", Toast.LENGTH_LONG).show();
                                        ShellUtils.SU(
                                                "am start -n com.tencent.ig/com.epicgames.ue4.SplashActivity");

                                    } else {
                                        Toast.makeText(getContext(), "Game Not Installed", Toast.LENGTH_LONG).show();
                                    }
                                }
                                if (brutalcheck) {

                                }else{
                                    PackageManager pm = getContext().getPackageManager();
                                    if (Helper.isPackageInstalled("com.tencent.ig", pm)) {
                                        Intent i = new Intent(getContext(), MainActivity.class);
                                        i.putExtra("game", "Global");
                                        startActivity(i);
                                        Toast.makeText(getContext(), "Wait While We Setting Up Things", Toast.LENGTH_LONG).show();
                                        ShellUtils.SU(
                                                "am start -n com.tencent.ig/com.epicgames.ue4.SplashActivity");

                                    } else {
                                        Toast.makeText(getContext(), "Game Not Installed", Toast.LENGTH_LONG).show();
                                    }
                                }
                                //   Log.d("livestartcheat", String.valueOf(HomeActivity.beta));
                                livestartcheat();
                            }

                        }
                    }, 4000);
                }
            }
        });


        StopCheatGl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Helper.checkVPN(getActivity())){
                    Toast.makeText(getActivity(), "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
                else {
                    antiban.show(getActivity().getFragmentManager(), "StopCheatGl");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            antiban.dismiss();
                            if (HomeActivity.beta) {


                                if (safecheck) {
                                    getActivity().stopService(new Intent(getActivity(), SafeService.class));
                                   getActivity().stopService(new Intent(ctx, Overlay.class));
                                    ctx.stopService(new Intent(ctx, ESPView.class));
                                    ctx.stopService(new Intent(ctx, FloatLogo.class));
                                   MainActivity.isDisplay = false;
                                    //startDaemon();
                                  MainActivity.isDaemon = false;
                                    Overlay.isRunning=false;
                                }
                                if (brutalcheck) {

                                }
                                betastopcheat();
                            } else {


                                if (safecheck) {
                                    getActivity().stopService(new Intent(getActivity(), SafeService.class));
                                    getActivity().stopService(new Intent(ctx, Overlay.class));
                                    getActivity().stopService(new Intent(ctx, SafeService.class));
                                    ctx.stopService(new Intent(ctx, ESPView.class));
                                    ctx.stopService(new Intent(ctx, FloatLogo.class));
                                    MainActivity.isDisplay = false;
                                    //startDaemon();
                                    MainActivity.isDaemon = false;
                                    Overlay.isRunning=false;
                                }
                                if (brutalcheck) {

                                }
                                livestopcheat();
                            }

                        }
                    }, 4000);
                }
            }
        });


        cleanguest.setOnClickListener(new View.OnClickListener() {

                                          @SuppressLint("SdCardPath")
                                          @Override
                                          public void onClick(View view) {
                                              cleanguestani.show(getActivity().getFragmentManager(),"cleanguest");
                                              handler.postDelayed(new Runnable() {
                                                  @Override
                                                  public void run() {
                                                      cleanguestani.dismiss();
                                                      try {
                                                          Process su = Runtime.getRuntime().exec("su");
                                                          DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                                                          outputStream.writeBytes("GUEST=\"/data/data/com.tencent.ig/shared_prefs/device_id.xml\"\n" +
                                                                  "kill com.tencent.ig\n" +
                                                                  "rm -rf $GUEST\n" +
                                                                  "echo \"<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\n" +
                                                                  "<map>\n" +
                                                                  "    <string name=\\\"random\\\"></string>\n" +
                                                                  "    <string name=\\\"install\\\"></string>\n" +
                                                                  "    <string name=\\\"uuid\\\">$RANDOM$RANDOM-$RANDOM-$RANDOM-$RANDOM-$RANDOM$RANDOM$RANDOM</string>\n" +
                                                                  "</map>\" > $GUEST\n" +
                                                                  "rm -rf /data/media/0/Android/data/com.tencent.ig/files/login-identifier.txt\n"
                                                                 );
                                                          outputStream.flush();
                                                          outputStream.writeBytes("exit\n");
                                                          outputStream.flush();
                                                          su.waitFor();
                                                      } catch (IOException | InterruptedException e) {
                                                          try {
                                                              throw new Exception(e);
                                                          } catch (Exception exception) {
                                                              exception.printStackTrace();
                                                          }
                                                      }


                                                  }
                                              }, 4000);
                                              //   new DownloadTask(getActivity(), URL)
                                          }
        });

        fixgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fixgameani.show(getActivity().getFragmentManager(),"fixgame");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fixgameani.dismiss();
                        try {
                            Process su = Runtime.getRuntime().exec("su");
                            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                            outputStream.writeBytes("rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/ProgramBinaryCache &>/dev/null\\n\"" +
                                    "                                    \"rm -rf /storage/emulated/0/Android/data/com.tencent.ig\\n\"" +
                                    "                                    \"chmod 755 /data/media/0/Android/data/com.tencent.ig\\n\"" +
                                    "                                    \"chmod 755 /data/media/0/Android/data/com.tencent.ig/*\\n\"" +
                                    "                                    \"chmod 755 /data/media/0/Android/data/com.tencent.ig/*/*\\n\"" +
                                    "                                    \"chmod 755 /data/media/0/Android/data/com.tencent.ig/*/*/*\\n\" +\""+
                                    "                                    \"chmod 755 /data/media/0/Android/data/com.tencent.ig/*/*/*/*\\n\"" +
                                    "                                    \"chmod 755 /data/media/0/Android/data/com.tencent.ig/*/*/*/*/*\\n\"" +
                                    "                                    \"chmod 755 /data/media/0/Android/data/com.tencent.ig/*/*/*/*/*/*\\n\"" +
                                    "                                    \"chmod 755 /data/media/0/Android/data/com.tencent.ig/*/*/*/*/*/*/*\\n\" " +
                                    "                                    \"chmod 755 /data/media/0/Android/data/com.tencent.ig/*/*/*/*/*/*/*/*\\n\"" +
                                    "                                    \"rm -rf /storage/emulated/0/Android/data/com.tencent.ig\\n\"" +
                                    "                                    \"chmod 755 /data/data/com.tencent.ig\\n\"" +
                                    "                                    \"chmod 755 /data/data/com.tencent.ig/*\\n\"" +
                                    "                                    \"chmod 755 /data/data/com.tencent.ig/*/*\\n\"" +
                                    "                                    \"chmod 755 /data/data/com.tencent.ig/*/*/*\\n\"" +
                                    "                                    \"chmod 755 /data/data/com.tencent.ig/*/*/*/*\\n\"" +
                                    "                                    \"chmod 755 /data/data/com.tencent.ig/*/*/*/*/*\\n\"" +
                                    "                                    \"chmod 755 /data/data/com.tencent.ig/*/*/*/*/*/*\\n\"" +
                                    "                                    \"chmod 755 /data/data/com.tencent.ig/*/*/*/*/*/*/*\\n\"" +
                                    "                                    \"chmod 755 /data/data/com.tencent.ig/*/*/*/*/*/*/*/*\\n\"" +
                                    "                                    \"rm -rf /data/data/com.tencent.ig\\n\"" +
                                    "                                    \"pm install -r /data/app/com.tencent.ig*/base.apk\\n");
                            outputStream.flush();
                            outputStream.writeBytes("exit\n");
                            outputStream.flush();
                            su.waitFor();
                        } catch (IOException | InterruptedException e) {
                            try {
                                throw new Exception(e);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }

                    }
                },4000);
                //   new DownloadTask(getActivity(), URL);

            }
        });

        DeepFixGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fixgameani.show(getActivity().getFragmentManager(),"Deepfixgame");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fixgameani.dismiss();
                        try {
                            Process su = Runtime.getRuntime().exec("su");
                            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                            outputStream.writeBytes("rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/ProgramBinaryCache &>/dev/null\\n\" +\n" +
                                    "                                    \"rm -rf /storage/emulated/0/Android/data/com.tencent.ig\\n\" +\n" +
                                    "                                    \"chmod 755 /data/media/0/Android/data/com.tencent.ig\\n\" +\n" +
                                    "                                    \"chmod 755 /data/media/0/Android/data/com.tencent.ig/*\\n\" +\n" +
                                    "                                    \"chmod 755 /data/media/0/Android/data/com.tencent.ig/*/*\\n\" +\n" +
                                    "                                    \"chmod 755 /data/media/0/Android/data/com.tencent.ig/*/*/*\\n\" +\n" +
                                    "                                    \"chmod 755 /data/media/0/Android/data/com.tencent.ig/*/*/*/*\\n\" +\n" +
                                    "                                    \"chmod 755 /data/media/0/Android/data/com.tencent.ig/*/*/*/*/*\\n\" +\n" +
                                    "                                    \"chmod 755 /data/media/0/Android/data/com.tencent.ig/*/*/*/*/*/*\\n\" +\n" +
                                    "                                    \"chmod 755 /data/media/0/Android/data/com.tencent.ig/*/*/*/*/*/*/*\\n\" +\n" +
                                    "                                    \"chmod 755 /data/media/0/Android/data/com.tencent.ig/*/*/*/*/*/*/*/*\\n\" +\n" +
                                    "                                    \"rm -rf /storage/emulated/0/Android/data/com.tencent.ig\\n\" +\n" +
                                    "                                    \"chmod 755 /data/data/com.tencent.ig\\n\" +\n" +
                                    "                                    \"chmod 755 /data/data/com.tencent.ig/*\\n\" +\n" +
                                    "                                    \"chmod 755 /data/data/com.tencent.ig/*/*\\n\" +\n" +
                                    "                                    \"chmod 755 /data/data/com.tencent.ig/*/*/*\\n\" +\n" +
                                    "                                    \"chmod 755 /data/data/com.tencent.ig/*/*/*/*\\n\" +\n" +
                                    "                                    \"chmod 755 /data/data/com.tencent.ig/*/*/*/*/*\\n\" +\n" +
                                    "                                    \"chmod 755 /data/data/com.tencent.ig/*/*/*/*/*/*\\n\" +\n" +
                                    "                                    \"chmod 755 /data/data/com.tencent.ig/*/*/*/*/*/*/*\\n\" +\n" +
                                    "                                    \"chmod 755 /data/data/com.tencent.ig/*/*/*/*/*/*/*/*\\n\" +\n" +
                                    "                                    \"rm -rf /data/data/com.tencent.ig\\n\" +\n" +
                                    "                                    \"pm install -r /data/app/com.tencent.ig*/base.apk\\n");
                            outputStream.flush();
                            outputStream.writeBytes("exit\n");
                            outputStream.flush();
                            su.waitFor();
                        } catch (IOException | InterruptedException e) {
                            try {
                                throw new Exception(e);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }

                    }
                },4000);
                //   new DownloadTask(getActivity(), URL);

            }
        });

        return rootViewone;
    }

    public void betastartcheat(){
        class load extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            //    Log.d("data",data);
                try
                {
                    Process su = Runtime.getRuntime().exec("su");
                    DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                    outputStream.writeBytes(data + "\n");
                    outputStream.flush();
                    outputStream.writeBytes("exit\n");
                    outputStream.flush();
                    su.waitFor();


                } catch(IOException |
                        InterruptedException e)

                {
                    try {
                        throw new Exception(e);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(TAG_VERSION,version);
                params.put(TAG_DEVICEID,deviceid);
                params.put("g",AESUtils.DarKnight.getEncrypted("gl"));
                params.put("s",AESUtils.DarKnight.getEncrypted("start"));
                data =AESUtils.DarKnight.getDecrypted(reader.getUrlContents(CheatB,params));
                return data;
            }
        }
        new load().execute();
    }

    public void livestartcheat(){
        class load extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
         //       Log.d("data",data);
                try
                {
                    Process su = Runtime.getRuntime().exec("su");
                    DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                    outputStream.writeBytes(data + "\n");
                    outputStream.flush();
                    outputStream.writeBytes("exit\n");
                    outputStream.flush();
                    su.waitFor();


                } catch(IOException |
                        InterruptedException e)

                {
                    try {
                        throw new Exception(e);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(TAG_VERSION,version);
                params.put(TAG_DEVICEID,deviceid);
                params.put("g",AESUtils.DarKnight.getEncrypted("gl"));
                params.put("s",AESUtils.DarKnight.getEncrypted("start"));
                data =AESUtils.DarKnight.getDecrypted(reader.getUrlContents(CheatL,params));
                return data;
            }
        }
        new load().execute();

    }


    public void livestopcheat(){
        class load extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            //    Log.d("data",data);
                try
                {
                    Process su = Runtime.getRuntime().exec("su");
                    DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                    outputStream.writeBytes(data + "\n");
                    outputStream.flush();
                    outputStream.writeBytes("exit\n");
                    outputStream.flush();
                    su.waitFor();


                } catch(IOException |
                        InterruptedException e)

                {
                    try {
                        throw new Exception(e);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(TAG_VERSION,version);
                params.put(TAG_DEVICEID,deviceid);
                params.put("g",AESUtils.DarKnight.getEncrypted("gl"));
                params.put("s",AESUtils.DarKnight.getEncrypted("stop"));
                data =AESUtils.DarKnight.getDecrypted(reader.getUrlContents(CheatL,params));
                return data;
            }
        }
        new load().execute();

    }

    public void betastopcheat(){


        class load extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (Helper.checkVPN(getActivity())) {
                    Toast.makeText(getActivity(), "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                } else {
                    //    Log.d("data",data);
                    try {
                        Process su = Runtime.getRuntime().exec("su");
                        DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                        outputStream.writeBytes(data + "\n");
                        outputStream.flush();
                        outputStream.writeBytes("exit\n");
                        outputStream.flush();
                        su.waitFor();


                    } catch (IOException |
                            InterruptedException e) {
                        try {
                            throw new Exception(e);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(TAG_VERSION,version);
                params.put(TAG_DEVICEID,deviceid);
                params.put("g",AESUtils.DarKnight.getEncrypted("gl"));
                params.put("s",AESUtils.DarKnight.getEncrypted("stop"));
                data =AESUtils.DarKnight.getDecrypted(reader.getUrlContents(CheatB,params));
                return data;
            }
        }
        new load().execute();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.taptoactivategl:
                if(Helper.checkVPN(getActivity())){
                    Toast.makeText(getActivity(), "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
                else {
                    lottieDialog.show(getActivity().getFragmentManager(), "loo");
                    lottieDialog.setCancelable(false);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lottieDialog.dismiss();

                            PackageManager pm = getContext().getPackageManager();
                            if (Helper.isPackageInstalled("com.tencent.ig", pm)) {
                                Intent i = new Intent(getContext(), MainActivity.class);
                                i.putExtra("game", "Global");
                                startActivity(i);
                                Toast.makeText(getContext(), "Wait While We Setting Up Things", Toast.LENGTH_LONG).show();
                                ShellUtils.SU(
                                        "am start -n com.tencent.ig/com.epicgames.ue4.SplashActivity");

                            } else {
                                Toast.makeText(getContext(), "Game Not Installed", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, 2000);
                }
                    break;
        }
    }
}


