package mobisocial.arcade.Fragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


import mobisocial.arcade.AESUtils;
import mobisocial.arcade.FloatLogo;
import mobisocial.arcade.GccConfig.urlref;
import mobisocial.arcade.Helper;
import mobisocial.arcade.HomeActivity;
import mobisocial.arcade.JavaUrlConnectionReader;
import mobisocial.arcade.LoginActivity;
import mobisocial.arcade.R;
import mobisocial.arcade.ShellUtils;
import mobisocial.arcade.imgLoad;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Environment.DIRECTORY_PICTURES;
import static mobisocial.arcade.GccConfig.urlref.time;


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
    public GlobalFragment() {
        // Required empty public constructor
    }
    private String gameName = "com.tencent.ig";
    static int GameType = 1;
    Context ctx;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getActivity();
    }
    private String  myDaemon;
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

        View rootViewone = inflater.inflate(R.layout.fragment_global, container, false);
        SharedPreferences shred = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);

        version = shred.getString("version", "32");
        version = AESUtils.DarKnight.getEncrypted(version);

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
                try {
                    Check();
                } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                if(Helper.checkVPN(getActivity())){
                    Toast.makeText(getActivity(), "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                  getActivity().finish();
                }
                else {
                    antiban.show(getActivity().getFragmentManager(), "StartCheatGl");
                    handler.postDelayed(() -> {
                        antiban.dismiss();
                        ShellUtils.SU("chmod 777 " + getActivity().getFilesDir().toString() + "/libPNR.so");
                        ShellUtils.SU("chmod 777 " + getActivity().getFilesDir().toString() + "/libtakedown.so");
                        if (HomeActivity.beta) {
//                            ShellUtils.SU("mkdir -p /sdcard/Android/data/com.google.backup/cache");
//                            File check = new File("/sdcard/Android/data/com.google.backup/cache/unknown_index");
//                      //      File cubecheck = new File("/sdcard/Android/data/com.google.backup/cache/known_index");
//                            if(!check.exists()) {
//                                ShellUtils.SU("cp -Rf /data/data/"+gameName+"/lib/libBugly.so /storage/emulated/0/Android/data/com.google.backup/cache/unknown_index ");
//                     //           ShellUtils.SU("cp -Rf /data/data/"+gameName+"/lib/libcubehawk.so /storage/emulated/0/Android/data/com.google.backup/cache/known_index");
//                            }
//
//                            try {
//                                Helper.unzip(getActivity());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                            //            Log.d("betastartcheat", String.valueOf(HomeActivity.beta));
                            betastartcheat();
                        } else {
                                        try {
                                            ShellUtils.SU("mkdir -p /sdcard/Android/data/com.google.backup/cache");
                                            File check = new File("/sdcard/Android/data/com.google.backup/cache/unknown_index");
                                            //        File cubecheck = new File("/sdcard/Android/data/com.google.backup/cache/known_index");
                                            if(!check.exists()) {
                                                ShellUtils.SU("cp -Rf /data/data/"+gameName+"/lib/libBugly.so /storage/emulated/0/Android/data/com.google.backup/cache/unknown_index");
                                                //              ShellUtils.SU("cp -Rf /data/data/"+gameName+"/lib/libcubehawk.so /storage/emulated/0/Android/data/com.google.backup/cache/known_index");
                                            }
                                            Helper.unzip(getActivity());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                            livestartcheat();

                    }, 4000);
                }
            }
        });


        StopCheatGl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Check();
                } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
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
                                betastopcheat();
                            } else {
                                livestopcheat();
                            }

                        }
                    }, 2000);
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

                                                      String s = "rm -Rf /data/data/com.tencent.ig/databases\n" +
                                                            "rm -Rf /data/data/com.tencent.ig/shared_prefs/gsdk_prefs.xml\n" +
                                                            "rm -Rf /data/data/com.tencent.ig/shared_prefs/APMCfg.xml\n" +
                                                            "rm -Rf /data/data/com.tencent.ig/shared_prefs/device_id.xml\n" +
                                                            "echo -n \"<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\\n<map>\\n    <string name=\\\"random\\\"></string>\\n    <string name=\\\"install\\\"></string>\\n    <string name=\\\"uuid\\\">$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM</string>\\n</map>\" > /data/data/com.tencent.ig/shared_prefs/device_id.xml\n" +
                                                            "chmod -R 555 /data/data/com.tencent.ig/shared_prefs/device_id.xml\n" +
                                                            "rm -Rf /data/media/0/.backups\n" +
                                                            "rm -Rf /data/media/0/Android/data/com.tencent.ig/cache\n" +
                                                            "rm -Rf /data/media/0/Android/data/com.tencent.ig/files/login-identifier.txt\n" +
                                                            "rm -Rf /data/media/0/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Intermediate/SaveGames/JKGuestRegisterCnt.json\n" +
                                                            "find /storage/emulated/0 -type f \\( -name \".fff\" -o -name \".zzz\" -o -name \".system_android_l2\" \\) -exec rm -Rf {} \\;";

                                                      new Thread(() -> {
                                                          String[] lines = s.split(Objects.requireNonNull(System.getProperty("line.separator")));
                                                          for (int i = 0; i < lines.length; i++) {

                                                              //      Log.d("testlines", lines[i]);
                                                              try {
                                                                  ShellUtils.SU(lines[i]);
                                                                  TimeUnit.MILLISECONDS.sleep(20);
                                                              } catch (InterruptedException e) {
                                                                  e.printStackTrace();
                                                              }
                                                          }
                                                      }).start();
                                                  }
                                              }, 2000);
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
                    String s = "rm -Rf /data/data/com.tencent.ig/lib\n" +
                            "rm -Rf /data/data/com.tencent.ig/databases__hs_log_store\n" +
                            "touch /data/data/com.tencent.ig/__hs_log_store\n" +
                            "chmod 444 /data/data/com.tencent.ig/__hs_log_store\n" +
                            "chmod 555 /data/data/com.tencent.ig/files/tss*\n" +
                            "chmod 555 /data/data/com.tencent.ig/files/tersafe.update\n" +
                            "echo \"8192\" > /proc/sys/fs/inotify/max_user_watches\n" +
                            "echo \"8192\" > /proc/sys/fs/inotify/max_user_instances\n" +
                            "echo \"8192\" > /proc/sys/fs/inotify/max_queued_events\n" +
                            "rm -Rf /storage/emulated/0/Android/data/.um /storage/emulated/0/Android/data/pushSdk /storage/emulated/0/Android/obj /storage/emulated/0/.backups /storage/emulated/0/MidasOversea /storage/emulated/0/tencent\n" +
                            "find /storage/emulated/0 -type f \\( -name \".fff\" -o -name \".zzz\" -o -name \".system_android_l2\" \\) -exec rm -Rf {} \\;\n" +
                            "rm -rf /sdcard/Android/data/com.google.backup\n"+
                            "pm install -i com.android.vending /data/app/com.tencent.ig-*/*.apk >/dev/null";
                        new Thread(() -> {
                            String[] lines = s.split(Objects.requireNonNull(System.getProperty("line.separator")));
                            for (int i = 0; i < lines.length; i++) {

                                //      Log.d("testlines", lines[i]);
                                try {
                                    ShellUtils.SU(lines[i]);
                                    TimeUnit.MILLISECONDS.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                },2000);
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
                        String s = "rm -Rf /data/data/com.tencent.ig\n" +
                                "rm -Rf /storage/emulated/0/.backups/com.tencent.ig\n" +
                                "rm -Rf /storage/emulated/0/Android/data/com.tencent.ig/cache\n" +
                                "rm -Rf /storage/emulated/0/Android/data/com.tencent.ig/files\n" +
                                "rm -Rf /storage/emulated/0/Android/data/.um /storage/emulated/0/Android/data/pushSdk /storage/emulated/0/Android/obj /storage/emulated/0/.backups /storage/emulated/0/MidasOversea /storage/emulated/0/tencent\n" +
                                "find /storage/emulated/0 -type f \\( -name \".fff\" -o -name \".zzz\" -o -name \".system_android_l2\" \\) -exec rm -Rf {} \\;\n" +
                                "pm install -i com.android.vending /data/app/com.tencent.ig-*/*.apk >/dev/null\n" +
                                "svc power reboot";
                        new Thread(() -> {
                            String[] lines = s.split(Objects.requireNonNull(System.getProperty("line.separator")));
                            for (int i = 0; i < lines.length; i++) {

                                //      Log.d("testlines", lines[i]);
                                try {
                                    ShellUtils.SU(lines[i]);
                                    TimeUnit.MILLISECONDS.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                },2000);
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
        //       Log.d("data",data);
                new Thread(() -> {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        String[] lines = s.split(Objects.requireNonNull(System.getProperty("line.separator")));
                        for (int i = 0; i < lines.length; i++) {

                            //      Log.d("testlines", lines[i]);
                            try {
                                ShellUtils.SU(lines[i]);
                                TimeUnit.MILLISECONDS.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                            //   betastartcheat();


                    });
                }).start();

            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(TAG_VERSION,version);
                params.put(TAG_DEVICEID,deviceid);
                params.put("g",AESUtils.DarKnight.getEncrypted("gl"));
                params.put("s",AESUtils.DarKnight.getEncrypted("start"));
                data =reader.getUrlContents(CheatB,params);
                return data;
            }
        }
        new load().execute();
    }

    public  void livestartcheat(){


        class load extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
               // Log.d("data", data);
                new Thread() {
                    @Override
                    public void run() {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            String[] lines = s.split(Objects.requireNonNull(System.getProperty("line.separator")));
                            for (int i = 0; i < lines.length; i++) {
                                //      Log.d("testlines", lines[i]);
                                try {
                                    ShellUtils.SU(lines[i]);
                                    TimeUnit.MILLISECONDS.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                                ShellUtils.SU("mv -f " + getActivity().getExternalFilesDir(DIRECTORY_PICTURES).getAbsolutePath() + "/1 /data/data/" + gameName + "/lib/libBugly.so");
                                ShellUtils.SU("chmod -R 777 "+ "/data/data/" + gameName + "/lib/*");
                                startPatcher();
                                Toast.makeText(getContext(), "Wait While We Setting Up Things", Toast.LENGTH_LONG).show();

                        });
                    }

                }.start();

            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(TAG_VERSION,version);
                params.put(TAG_DEVICEID,deviceid);
                params.put("g",AESUtils.DarKnight.getEncrypted("gl"));
                params.put("s",AESUtils.DarKnight.getEncrypted("start"));
                data =AESUtils.DarKnight.getDecrypted(reader.getUrlContents(CheatL,params));
            //    Log.d("test",data);
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
         //       Log.d("data",data);
            new Thread() {
                @Override
                public void run() {

                    String[] lines = s.split(Objects.requireNonNull(System.getProperty("line.separator")));
                    for (int i = 0; i < lines.length; i++) {

                        //      Log.d("testlines", lines[i]);
                        try {
                            ShellUtils.SU(lines[i]);
                            TimeUnit.MILLISECONDS.sleep(80);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    ctx.stopService(new Intent(ctx,FloatLogo.class));

                }
            }.start();

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
    @RequiresApi(api = Build.VERSION_CODES.N)
    private  void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if(imgLoad.Load(getActivity()).equals(time)){
          getActivity().finish();
        }
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
                    new Thread(() -> {
                        String[] lines = s.split(Objects.requireNonNull(System.getProperty("line.separator")));
                        for (int i = 0; i < lines.length; i++) {
                            //      Log.d("testlines", lines[i]);
                            try {
                                ShellUtils.SU(lines[i]);
                                TimeUnit.MILLISECONDS.sleep(80);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }).start();

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

    void startPatcher() {
        if (!Settings.canDrawOverlays(getActivity())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" +getActivity().getPackageName ()));
            startActivityForResult(intent, 123);
        } else {
            startFloater();
        }
    }

    private void startFloater() {
       getActivity().stopService(new Intent(getActivity(),FloatLogo.class));
        Intent i = new Intent(getActivity(),FloatLogo.class);
        i.putExtra("gametype",1);
        i.putExtra("gamename",gameName);
        getActivity().startService(i);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.taptoactivategl:
                try {
                    Check();
                } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
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

                        }
                    }, 2000);
                }
                    break;
        }
    }
}


