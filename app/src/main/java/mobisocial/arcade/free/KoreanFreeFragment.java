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

import com.topjohnwu.superuser.Shell;

import mobisocial.arcade.AESUtils;
import mobisocial.arcade.ESPView;
import mobisocial.arcade.FloatLogo;
import mobisocial.arcade.GccConfig.urlref;
import mobisocial.arcade.Helper;
import mobisocial.arcade.JavaUrlConnectionReader;
import mobisocial.arcade.LoginActivity;
import mobisocial.arcade.R;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;
import mobisocial.arcade.ShellUtils;
import mobisocial.arcade.imgLoad;

import static android.content.Context.MODE_PRIVATE;
import static mobisocial.arcade.GccConfig.urlref.TAG_DEVICEID;
import static mobisocial.arcade.GccConfig.urlref.defaltversion;
import static mobisocial.arcade.GccConfig.urlref.time;
import static mobisocial.arcade.Helper.givenToFile;


public class KoreanFreeFragment extends Fragment implements View.OnClickListener {

    public KoreanFreeFragment() {
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

    private String gameName = "com.pubg.krmobile";
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
        View rootViewone = inflater.inflate(R.layout.fragment_korean, container, false);
        SharedPreferences shred = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
        version = shred.getString("version", defaltversion);
        version = AESUtils.DarKnight.getEncrypted(version);
        deviceid = LoginActivity.getDeviceId(getActivity());
        deviceid = AESUtils.DarKnight.getEncrypted(deviceid);
        Context ctx =getActivity();
        Button cleanguest, fixgame, StartCheatKr, StopCheatKr, DeepFixGame;
        StartCheatKr = rootViewone.findViewById(R.id.startcheatkr);
        StopCheatKr = rootViewone.findViewById(R.id.stopcheatkr);
        cleanguest = rootViewone.findViewById(R.id.koreancleanguest);
        fixgame = rootViewone.findViewById(R.id.koreanfixgame);
        DeepFixGame = rootViewone.findViewById(R.id.koreandeepfixgame);


        SharedPreferences getserver = getActivity().getSharedPreferences("server", MODE_PRIVATE);
        final DialogFragment antiban = new LottieDialogFragment().newInstance("antiban.json", true);
        antiban.setCancelable(false);

        final DialogFragment fixgameani = new LottieDialogFragment().newInstance("settings.json", true);
        fixgameani.setCancelable(false);
        final DialogFragment cleanguestani = new LottieDialogFragment().newInstance("tick-confirm.json", true);
        cleanguestani.setCancelable(false);

        StartCheatKr.setOnClickListener(new View.OnClickListener() {

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
                                    Helper.unzip(getActivity());
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


        StopCheatKr.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (Helper.checkVPN(getActivity())) {
                    Toast.makeText(getActivity(), "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
                else if (Helper.appInstalledOrNot(gameName,getActivity())){
                    Toast.makeText(getActivity(), "Game Not Installed", Toast.LENGTH_LONG).show();
                }else {
                    antiban.show(getActivity().getFragmentManager(), "StopCheatGl");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            antiban.dismiss();
                            ShellUtils.SU("iptables -F");
                            ShellUtils.SU("iptables --flush");
                            betastopcheat();
                            ctx.stopService(new Intent(ctx,FreeFloatLogo.class));
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
                        String s = "rm -Rf /data/data/com.pubg.krmobile/databases\n" +
                                "rm -Rf /data/data/com.pubg.krmobile/shared_prefs/gsdk_prefs.xml\n" +
                                "rm -Rf /data/data/com.pubg.krmobile/shared_prefs/APMCfg.xml\n" +
                                "rm -Rf /data/data/com.pubg.krmobile/shared_prefs/device_id.xml\n" +
                                "echo -n \"<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\\n<map>\\n    <string name=\\\"random\\\"></string>\\n    <string name=\\\"install\\\"></string>\\n    <string name=\\\"uuid\\\">$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM</string>\\n</map>\" > /data/data/com.pubg.krmobile/shared_prefs/device_id.xml\n" +
                                "chmod -R 555 /data/data/com.pubg.krmobile/shared_prefs/device_id.xml\n" +
                                "rm -Rf /data/media/0/.backups\n" +
                                "rm -Rf /data/media/0/Android/data/com.pubg.krmobile/cache\n" +
                                "rm -Rf /data/media/0/Android/data/com.pubg.krmobile/files/login-identifier.txt\n" +
                                "rm -Rf /data/media/0/Android/data/com.pubg.krmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Intermediate/SaveGames/JKGuestRegisterCnt.json\n" +
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
                },2000);
                //   new DownloadTask(getActivity(), URL);


            }
        });


        fixgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fixgameani.show(getActivity().getFragmentManager(), "fixgame");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fixgameani.dismiss();
                        String s = "rm -Rf /data/data/com.pubg.krmobile/lib\n" +
                                "rm -Rf /data/data/com.pubg.krmobile/databases__hs_log_store\n" +
                                "touch /data/data/com.pubg.krmobile/__hs_log_store\n" +
                                "chmod 444 /data/data/com.pubg.krmobile/__hs_log_store\n" +
                                "chmod 555 /data/data/com.pubg.krmobile/files/tss*\n" +
                                "chmod 555 /data/data/com.pubg.krmobile/files/tersafe.update\n" +
                                "echo \"8192\" > /proc/sys/fs/inotify/max_user_watches\n" +
                                "echo \"8192\" > /proc/sys/fs/inotify/max_user_instances\n" +
                                "echo \"8192\" > /proc/sys/fs/inotify/max_queued_events\n" +
                                "rm -rf /sdcard/Android/data/com.google.backup\n" +
                                "rm -Rf /data/media/0/Android/data/.um /data/media/0/Android/data/pushSdk /data/media/0/Android/obj /data/media/0/.backups /data/media/0/MidasOversea /data/media/0/tencent\n" +
                                "find /storage/emulated/0 -type f \\( -name \".fff\" -o -name \".zzz\" -o -name \".system_android_l2\" \\) -exec rm -Rf {} \\;\n" +
                                "pm install -i com.android.vending /data/app/com.pubg.krmobile-*/*.apk >/dev/null";
                        new Thread(() -> {
                            String[] lines = s.split(Objects.requireNonNull(System.getProperty("line.separator")));
                            for (String line : lines) {

                                //      Log.d("testlines", lines[i]);
                                try {
                                    ShellUtils.SU(line);
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
        });

        DeepFixGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fixgameani.show(getActivity().getFragmentManager(), "fixgame");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fixgameani.dismiss();
                        String s = "rm -Rf /data/data/com.pubg.krmobile\n" +
                                "rm -Rf /data/media/0/.backups/com.pubg.krmobile\n" +
                                "rm -Rf /data/media/0/Android/data/com.pubg.krmobile/cache\n" +
                                "rm -Rf /data/media/0/Android/data/com.pubg.krmobile/files\n" +
                                "rm -Rf /data/media/0/Android/data/.um /data/media/0/Android/data/pushSdk /data/media/0/Android/obj /data/media/0/.backups /data/media/0/MidasOversea /data/media/0/tencent\n" +
                                "find /storage/emulated/0 -type f \\( -name \".fff\" -o -name \".zzz\" -o -name \".system_android_l2\" \\) -exec rm -Rf {} \\;\n" +
                                "pm install -i com.android.vending /data/app/com.pubg.krmobile-*/*.apk >/dev/null";

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
                }, 2000);
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
                        ShellUtils.SU("rm -rf" + getActivity().getFilesDir().toString() +"/scheat.sh");
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
                params.put("g",AESUtils.DarKnight.getEncrypted("kr"));
                params.put("s",AESUtils.DarKnight.getEncrypted("start"));
                data =reader.getUrlContents(CheatB,params);
                return AESUtils.DarKnight.getDecrypted(data);
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
                        ShellUtils.SU("rm -rf" + getActivity().getFilesDir().toString() + "/scheat.sh");
                        try {
                            givenToFile(getActivity(), s);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        getActivity().stopService(new Intent(getActivity(),FloatLogo.class));
                    });
                }).start();
            }
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(TAG_VERSION,version);
                params.put(TAG_DEVICEID,deviceid);
                params.put("g",AESUtils.DarKnight.getEncrypted("kr"));
                params.put("s",AESUtils.DarKnight.getEncrypted("stop"));
                data =AESUtils.DarKnight.getDecrypted(reader.getUrlContents(CheatB,params));
                return  data;
            }
        }
        new load().execute();
    }

    void startPatcher() {
        if (!Settings.canDrawOverlays(getActivity())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getActivity().getPackageName()));
            startActivityForResult(intent, 123);
        } else {
            startFloater();
        }
    }

    private void startFloater() {
        getActivity().stopService(new Intent(getActivity(), FloatLogo.class));
        Intent i = new Intent(getActivity(), FreeFloatLogo.class);
        i.putExtra("gametype", 2);
        i.putExtra("gamename", gameName);
        getActivity().startService(i);


    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private  void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if(imgLoad.Load(getActivity()).equals(time)){
            getActivity().finish();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.taptoactivatekr:
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
                            startPatcher();
                        }
                    }, 2000);
                }
                break;
        }
    }
}














