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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.topjohnwu.superuser.Shell;

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
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;

import static android.content.Context.MODE_PRIVATE;
import static mobisocial.arcade.GccConfig.urlref.TAG_DEVICEID;
import static mobisocial.arcade.GccConfig.urlref.defaltversion;
import static mobisocial.arcade.GccConfig.urlref.time;
import static mobisocial.arcade.Helper.givenToFile;

public class VeitnamFreeFragment extends Fragment{
    private String version, deviceid;
    Handler handler = new Handler();
    private final JavaUrlConnectionReader reader = new JavaUrlConnectionReader();
    private String data;
    private static final String TAG_DEVICEID = urlref.TAG_DEVICEID;
    private static final String TAG_VERSION = "v";
    String CheatB = urlref.Betaserver + "cheat.php";


    public VeitnamFreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }
    private String gameName = "com.vng.pubgmobile";
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
        View rootViewone = inflater.inflate(R.layout.fragment_veitnam, container, false);
        SharedPreferences shred = getActivity().getSharedPreferences("Freeuserdetails", MODE_PRIVATE);
        Context ctx=getActivity();
        version = shred.getString("version", defaltversion);
        version = AESUtils.DarKnight.getEncrypted(version);
        final File daemon = new File(urlref.pathoflib+urlref.SafeMem);

        deviceid = Helper.getDeviceId(getActivity());
        deviceid = AESUtils.DarKnight.getEncrypted(deviceid);

        Button cleanguest, fixgame, StartCheatVn, StopCheatVn,DeepFixGame;
        StartCheatVn = rootViewone.findViewById(R.id.startcheatvn);
        StopCheatVn = rootViewone.findViewById(R.id.stopcheatvn);
        cleanguest = rootViewone.findViewById(R.id.veitnamcleanguest);
        fixgame = rootViewone.findViewById(R.id.veitnamfixgame);
        DeepFixGame = rootViewone.findViewById(R.id.deepfixgamevn);


        SharedPreferences getserver = getActivity().getSharedPreferences("server",MODE_PRIVATE);
        final DialogFragment antiban = new LottieDialogFragment().newInstance("antiban.json",true);
        antiban.setCancelable(false);

        final DialogFragment fixgameani = new LottieDialogFragment().newInstance("settings.json",true);
        fixgameani.setCancelable(false);
        final DialogFragment cleanguestani = new LottieDialogFragment().newInstance("tick-confirm.json",true);
        cleanguestani.setCancelable(false);

        StartCheatVn.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (Helper.checkVPN(getActivity())) {
                    Toast.makeText(getActivity(), "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
                else if (Helper.appInstalledOrNot(gameName,getActivity())){
                    Toast.makeText(getActivity(), "Game Not Installed", Toast.LENGTH_LONG).show();
                }
                else {
                    antiban.show(getActivity().getFragmentManager(), "StartCheatGl");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            antiban.dismiss();
                            if  (Shell.rootAccess()) {
                                try {
                                    Helper.unzip(getActivity(),gameName);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                betastartcheat();
                                Toast.makeText(getContext(), "Wait While We Setting Up Things", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(), "Root Access Was Not Granted", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, 4000);

                }
            }
        });


        StopCheatVn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {   if(Helper.checkVPN(getActivity())){
                Toast.makeText(getActivity(), "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
            else if (Helper.appInstalledOrNot(gameName,getActivity())){
                Toast.makeText(getActivity(), "Game Not Installed", Toast.LENGTH_LONG).show();
            }
            else {

                antiban.show(getActivity().getFragmentManager(), "StopCheatGl");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        antiban.dismiss();
                        ShellUtils.SU("iptables -F");
                        ShellUtils.SU("iptables --flush");
                        if (Shell.rootAccess()) {
                            betastopcheat();
                        }else {
                            Toast.makeText(getActivity(),"Root Access Was Not Granted ",Toast.LENGTH_LONG).show();
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
                        String s ="rm -Rf /data/data/com.vng.pubgmobile/databases\\n\" +\n" +
                                "                          \"rm -Rf /data/data/com.vng.pubgmobile/shared_prefs/gsdk_prefs.xml\\n\" +\n" +
                                "                          \"rm -Rf /data/data/com.vng.pubgmobile/shared_prefs/APMCfg.xml\\n\" +\n" +
                                "                          \"rm -Rf /data/data/com.vng.pubgmobile/shared_prefs/device_id.xml\\n\" +\n" +
                                "                          \"echo -n \\\"<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\\\\n<map>\\\\n    <string name=\\\\\\\"random\\\\\\\"></string>\\\\n    <string name=\\\\\\\"install\\\\\\\"></string>\\\\n    <string name=\\\\\\\"uuid\\\\\\\">$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM</string>\\\\n</map>\\\" > /data/data/com.vng.pubgmobile/shared_prefs/device_id.xml\\n\" +\n" +
                                "                          \"chmod -R 555 /data/data/com.vng.pubgmobile/shared_prefs/device_id.xml\\n\" +\n" +
                                "                          \"rm -Rf /data/media/0/.backups\\n\" +\n" +
                                "                          \"rm -Rf /data/media/0/Android/data/com.vng.pubgmobile/cache\\n\" +\n" +
                                "                          \"rm -Rf /data/media/0/Android/data/com.vng.pubgmobile/files/login-identifier.txt\\n\" +\n" +
                                "                          \"rm -Rf /data/media/0/Android/data/com.vng.pubgmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Intermediate/SaveGames/JKGuestRegisterCnt.json\\n\" +\n" +
                                "                          \"find /storage/emulated/0 -type f \\\\( -name \\\".fff\\\" -o -name \\\".zzz\\\" -o -name \\\".system_android_l2\\\" \\\\) -exec rm -Rf {} \\\\;";

                        new Thread(() -> {
                            String[] lines = s.split("\n");
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

        fixgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fixgameani.show(getActivity().getFragmentManager(),"fixgame");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fixgameani.dismiss();
                        String s ="rm -Rf /data/data/com.vng.pubgmobile/lib\n" +
                                "rm -Rf /data/data/com.vng.pubgmobile/databases__hs_log_store\n" +
                                "touch /data/data/com.vng.pubgmobile/__hs_log_store\n" +
                                "chmod 444 /data/data/com.vng.pubgmobile/__hs_log_store\n" +
                                "chmod 555 /data/data/com.vng.pubgmobile/files/tss*\n" +
                                "chmod 555 /data/data/com.vng.pubgmobile/files/tersafe.update\n" +
                                "echo \"8192\" > /proc/sys/fs/inotify/max_user_watches\n" +
                                "echo \"8192\" > /proc/sys/fs/inotify/max_user_instances\n" +
                                "echo \"8192\" > /proc/sys/fs/inotify/max_queued_events\n" +
                                "rm -rf /sdcard/Android/data/com.google.backup\n"+
                                "rm -Rf /data/media/0/Android/data/.um /data/media/0/Android/data/pushSdk /data/media/0/Android/obj /data/media/0/.backups /data/media/0/MidasOversea /data/media/0/tencent\n" +
                                "find /storage/emulated/0 -type f \\( -name \".fff\" -o -name \".zzz\" -o -name \".system_android_l2\" \\) -exec rm -Rf {} \\;\n" +
                                "pm install -i com.android.vending /data/app/com.vng.pubgmobile-*/*.apk >/dev/null";

                        new Thread(() -> {
                            String[] lines = s.split("\n");
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
        DeepFixGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fixgameani.show(getActivity().getFragmentManager(),"fixgame");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fixgameani.dismiss();
                        String s = "rm -Rf /data/data/com.vng.pubgmobile\n" +
                                "rm -Rf /data/media/0/.backups/com.vng.pubgmobile\n" +
                                "rm -Rf /data/media/0/Android/data/com.vng.pubgmobile/cache\n" +
                                "rm -Rf /data/media/0/Android/data/com.vng.pubgmobile/files\n" +
                                "rm -Rf /data/media/0/Android/data/.um /data/media/0/Android/data/pushSdk /data/media/0/Android/obj /data/media/0/.backups /data/media/0/MidasOversea /data/media/0/tencent\n" +
                                "find /storage/emulated/0 -type f \\( -name \".fff\" -o -name \".zzz\" -o -name \".system_android_l2\" \\) -exec rm -Rf {} \\;\n" +
                                "pm install -i com.android.vending /data/app/com.vng.pubgmobile-*/*.apk >/dev/null";
                        new Thread(() -> {
                            String[] lines = s.split("\n");
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

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //      Log.d("data",s);
                new Thread(() -> {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        try {
                            givenToFile(getActivity(),s);

                        } catch (IOException e) {
                            e.printStackTrace();
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
                params.put("g",AESUtils.DarKnight.getEncrypted("vn"));
                params.put("s",AESUtils.DarKnight.getEncrypted("start"));
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
                //    Log.d("data",data);
                new Thread(() -> {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        try {
                            givenToFile(getActivity(), s);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }).start();
               if(FreeFloatLogo.isRunning) {
                    getActivity().stopService(new Intent(getActivity(), FreeFloatLogo.class));
                }
            }
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(TAG_VERSION,version);
                params.put(TAG_DEVICEID,deviceid);
                params.put("g",AESUtils.DarKnight.getEncrypted("vn"));
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
        i.putExtra("gametype",3);
        i.putExtra("gamename",gameName);
        getActivity().startService(i);

    }
}






