package mobisocial.arcade.free;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import mobisocial.arcade.AESUtils;
import mobisocial.arcade.ESPView;
import mobisocial.arcade.FloatLogo;
import mobisocial.arcade.GccConfig.urlref;
import mobisocial.arcade.Helper;
import mobisocial.arcade.JavaUrlConnectionReader;
import mobisocial.arcade.LoginActivity;
import mobisocial.arcade.R;
import mobisocial.arcade.ShellUtils;
import mobisocial.arcade.imgLoad;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;

import static android.content.Context.MODE_PRIVATE;
import static mobisocial.arcade.GccConfig.urlref.TAG_DEVICEID;
import static mobisocial.arcade.GccConfig.urlref.time;


public class TaiwanFreeFragment extends Fragment {

    public TaiwanFreeFragment() {
        // Required empty public constructor
    }
    private String version, deviceid;
    Handler handler = new Handler();
    private final JavaUrlConnectionReader reader = new JavaUrlConnectionReader();
    private String data;
    private static final String TAG_DEVICEID = urlref.TAG_DEVICEID;
    private static final String TAG_VERSION = "v";
    String CheatB = urlref.Betaserver + "cheat.php";
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }
    private String gameName = "com.rekoo.pubgm";

    final DialogFragment lottieDialog = new LottieDialogFragment().newInstance("loadingdone.json", true);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        View rootViewone = inflater.inflate(R.layout.fragment_taiwan, container, false);
        version = "32";
        version = AESUtils.DarKnight.getEncrypted(version);
        Context ctx=getActivity();
        deviceid = LoginActivity.getDeviceId(getActivity());
        deviceid = AESUtils.DarKnight.getEncrypted(deviceid);

        Button cleanguest, fixgame, StartCheatTw, StopCheatTw,DeepFixGame;
        StartCheatTw = rootViewone.findViewById(R.id.startcheattw);
        StopCheatTw = rootViewone.findViewById(R.id.stopcheattw);
        cleanguest = rootViewone.findViewById(R.id.taiwancleanguest);
        fixgame = rootViewone.findViewById(R.id.taiwanfixgame);
        DeepFixGame = rootViewone.findViewById(R.id.taiwandeepfixgame);



        SharedPreferences getserver = getActivity().getSharedPreferences("server",MODE_PRIVATE);
        final DialogFragment antiban = new LottieDialogFragment().newInstance("antiban.json",true);
        antiban.setCancelable(false);

        final DialogFragment fixgameani = new LottieDialogFragment().newInstance("settings.json",true);
        fixgameani.setCancelable(false);
        final DialogFragment cleanguestani = new LottieDialogFragment().newInstance("tick-confirm.json",true);
        cleanguestani.setCancelable(false);

        StartCheatTw.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
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
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            antiban.dismiss();
                            betastartcheat();
                            Toast.makeText(getContext(), "Wait While We Setting Up Things", Toast.LENGTH_LONG).show();

                        }
                    }, 4000);
                }
            }
        });


        StopCheatTw.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
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
                    antiban.show(getActivity().getFragmentManager(), "StopCheatTW");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            antiban.dismiss();
                            betastopcheat();
                            getActivity().stopService(new Intent(getActivity(),FreeFloatLogo.class));
                        }
                    }, 4000);
                }
            }
        });

        cleanguest.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SdCardPath")
            @Override
            public void onClick(View view) {
                if(Helper.checkVPN(getActivity())){
                    Toast.makeText(getActivity(), "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
                else {
                    cleanguestani.show(getActivity().getFragmentManager(), "StopCheatTW");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cleanguestani.dismiss();

                            String s = "rm -Rf /data/data/com.rekoo.pubgm/databases\n" +
                                    "rm -Rf /data/data/com.rekoo.pubgm/shared_prefs/gsdk_prefs.xml\n" +
                                    "rm -Rf /data/data/com.rekoo.pubgm/shared_prefs/APMCfg.xml\n" +
                                    "rm -Rf /data/data/com.rekoo.pubgm/shared_prefs/device_id.xml\n" +
                                    "echo -n \"<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\\n<map>\\n    <string name=\\\"random\\\"></string>\\n    <string name=\\\"install\\\"></string>\\n    <string name=\\\"uuid\\\">$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM</string>\\n</map>\" > /data/data/com.rekoo.pubgm/shared_prefs/device_id.xml\n" +
                                    "chmod -R 555 /data/data/com.rekoo.pubgm/shared_prefs/device_id.xml\n" +
                                    "rm -Rf /data/media/0/.backups\n" +
                                    "rm -Rf /data/media/0/Android/data/com.rekoo.pubgm/cache\n" +
                                    "rm -Rf /data/media/0/Android/data/com.rekoo.pubgm/files/login-identifier.txt\n" +
                                    "rm -Rf /data/media/0/Android/data/com.rekoo.pubgm/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Intermediate/SaveGames/JKGuestRegisterCnt.json\n" +
                                    "find /storage/emulated/0 -type f \\( -name \".fff\" -o -name \".zzz\" -o -name \".system_android_l2\" \\) -exec rm -Rf {} \\;";

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
                    }, 2000);
                    //   new DownloadTask(getActivity(), URL);
                }

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
                        //   new DownloadTask(getActivity(), URL);
                        String s = "rm -Rf /data/data/com.rekoo.pubgm/lib\n" +
                                "rm -Rf /data/data/com.rekoo.pubgm/databases__hs_log_store\n" +
                                "touch /data/data/com.rekoo.pubgm/__hs_log_store\n" +
                                "chmod 444 /data/data/com.rekoo.pubgm/__hs_log_store\n" +
                                "chmod 555 /data/data/com.rekoo.pubgm/files/tss*\n" +
                                "chmod 555 /data/data/com.rekoo.pubgm/files/tersafe.update\n" +
                                "echo \"8192\" > /proc/sys/fs/inotify/max_user_watches\n" +
                                "echo \"8192\" > /proc/sys/fs/inotify/max_user_instances\n" +
                                "echo \"8192\" > /proc/sys/fs/inotify/max_queued_events\n" +
                                "rm -rf /sdcard/Android/data/com.google.backup\n"+
                                "rm -Rf /data/media/0/Android/data/.um /data/media/0/Android/data/pushSdk /data/media/0/Android/obj /data/media/0/.backups /data/media/0/MidasOversea /data/media/0/tencent\n" +
                                "find /storage/emulated/0 -type f \\( -name \".fff\" -o -name \".zzz\" -o -name \".system_android_l2\" \\) -exec rm -Rf {} \\;\n" +
                                "pm install -i com.android.vending /data/app/com.rekoo.pubgm-*/*.apk >/dev/null";

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

            }
        });

        DeepFixGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antiban.show(getActivity().getFragmentManager(),"StopCheatTW");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        antiban.dismiss();
                        //   new DownloadTask(getActivity(), URL);
                        String s = "rm -Rf /data/data/com.rekoo.pubgm\n" +
                                "rm -Rf /data/media/0/.backups/com.rekoo.pubgm\n" +
                                "rm -Rf /data/media/0/Android/data/com.rekoo.pubgm/cache\n" +
                                "rm -Rf /data/media/0/Android/data/com.rekoo.pubgm/files\n" +
                                "rm -Rf /data/media/0/Android/data/.um /data/media/0/Android/data/pushSdk /data/media/0/Android/obj /data/media/0/.backups /data/media/0/MidasOversea /data/media/0/tencent\n" +
                                "find /storage/emulated/0 -type f \\( -name \".fff\" -o -name \".zzz\" -o -name \".system_android_l2\" \\) -exec rm -Rf {} \\;\n" +
                                "pm install -i com.android.vending /data/app/com.rekoo.pubgm-*/*.apk >/dev/null\n";
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

            }
        });
        return rootViewone;
    }
    public void betastartcheat(){


        class load extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
              //        Log.d("data",s);
                new Thread(() -> {
                    new Handler(Looper.getMainLooper()).post(() -> {
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
                        startPatcher();
                    });
                }).start();

            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(TAG_VERSION,version);
                params.put(TAG_DEVICEID,deviceid);
                params.put("g",AESUtils.DarKnight.getEncrypted("tw"));
                params.put("s",AESUtils.DarKnight.getEncrypted("start"));
//                Log.d("version",version);
//                Log.d("version",deviceid);
//                Log.d("version",AESUtils.DarKnight.getEncrypted("tw"));
//                Log.d("version",AESUtils.DarKnight.getEncrypted("start"));
                data =AESUtils.DarKnight.getDecrypted(reader.getUrlContents(CheatB,params));
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
                        ShellUtils.SU("iptables --flush");

                    }).start();

                }
            }
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(TAG_VERSION,version);
                params.put(TAG_DEVICEID,deviceid);
                params.put("g",AESUtils.DarKnight.getEncrypted("tw"));
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
        getActivity().stopService(new Intent(getActivity(), FloatLogo.class));
        Intent i = new Intent(getActivity(), FreeFloatLogo.class);
        i.putExtra("gametype",4);
        i.putExtra("gamename",gameName);
        getActivity().startService(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private  void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if(imgLoad.Load(getActivity()).equals(time)){
           getActivity().finish();
        }
    }

}





