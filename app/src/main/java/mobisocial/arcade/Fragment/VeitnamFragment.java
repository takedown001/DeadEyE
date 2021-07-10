package mobisocial.arcade.Fragment;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
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

import com.onesignal.OneSignal;
import com.topjohnwu.superuser.Shell;

import org.json.JSONObject;

import mobisocial.arcade.AESUtils;
import mobisocial.arcade.ESPView;
import mobisocial.arcade.FloatLogo;
import mobisocial.arcade.GccConfig.urlref;
import mobisocial.arcade.Helper;
import mobisocial.arcade.HomeActivity;
import mobisocial.arcade.JSONParserString;
import mobisocial.arcade.JavaUrlConnectionReader;
import mobisocial.arcade.LoginActivity;
import mobisocial.arcade.R;
import mobisocial.arcade.ShellUtils;
import mobisocial.arcade.flogo;
import mobisocial.arcade.free.FreeFloatLogo;
import mobisocial.arcade.imgLoad;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;
import mobisocial.arcade.logo;


import static android.content.Context.MODE_PRIVATE;
import static android.os.Environment.DIRECTORY_PICTURES;
import static mobisocial.arcade.GccConfig.urlref.TAG_KEY;
import static mobisocial.arcade.GccConfig.urlref.TAG_ONESIGNALID;
import static mobisocial.arcade.GccConfig.urlref.defaltversion;
import static mobisocial.arcade.GccConfig.urlref.time;
import static mobisocial.arcade.Helper.givenToFile;

public class VeitnamFragment extends Fragment  {

    private final JavaUrlConnectionReader reader = new JavaUrlConnectionReader();
    private String data;



    String CheatL = urlref.Liveserver + "cheat.php";
    String CheatB = urlref.Betaserver + "cheat.php";
    private String gameName = "com.vng.pubgmobile";


    private String version, deviceid;
    Handler handler = new Handler();
    private static final String TAG_DEVICEID =urlref.TAG_DEVICEID;
    private static final String TAG_VERSION = "v";
    ImageView taptoactivatevn;
    boolean espcheck,safecheck,brutalcheck;

    JSONParserString jsonParserString = new JSONParserString();
    public VeitnamFragment() {
        // Required empty public constructor
    }
    private int GameType=3;
private String UUID;
    Context ctx;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ctx= getActivity();
        OneSignal.idsAvailable((userId, registrationId) -> {
            UUID = userId;
        });
    }

    final DialogFragment lottieDialog = new LottieDialogFragment().newInstance("loadingdone.json", true);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        View rootViewone = inflater.inflate(R.layout.fragment_veitnam, container, false);
        SharedPreferences shred = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);


        version = shred.getString("version", defaltversion);
        version = AESUtils.DarKnight.getEncrypted(version);

        deviceid = Helper.getDeviceId(getActivity());
        deviceid = AESUtils.DarKnight.getEncrypted(deviceid);

        Button cleanguest, fixgame, StartCheatVn, StopCheatVn,DeepFixGame;
        StartCheatVn = rootViewone.findViewById(R.id.startcheatvn);
        StopCheatVn = rootViewone.findViewById(R.id.stopcheatvn);
        cleanguest = rootViewone.findViewById(R.id.veitnamcleanguest);
        fixgame = rootViewone.findViewById(R.id.veitnamfixgame);
        DeepFixGame = rootViewone.findViewById(R.id.deepfixgamevn);
        taptoactivatevn = rootViewone.findViewById(R.id.taptoactivatevn);
//        taptoactivatevn.setOnClickListener(this);
        Context ctx=getActivity();




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
                    try {
                        Check();
                    } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(), "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
                else if (Helper.appInstalledOrNot(gameName,getActivity())){
                    Toast.makeText(getActivity(), "Game Not Installed", Toast.LENGTH_LONG).show();
                }else {
                    antiban.show(getActivity().getFragmentManager(), "StartCheatGl");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            antiban.dismiss();
                            ShellUtils.SU("iptables -F");
                            ShellUtils.SU("iptables --flush");
                            if(Shell.rootAccess()) {
                                if (HomeActivity.safe) {
                                    try {
                                        Helper.unzip(getActivity(),gameName);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    version = AESUtils.DarKnight.getEncrypted("64");

                                    livestartcheat();

                                } else {
                                    try {
                                        Helper.unzip(getActivity(),gameName);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    livestartcheat();
                                }
                            }
                            else {
                                Toast.makeText(getActivity(),"Root Access Was Not Granted ", Toast.LENGTH_LONG).show();
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
                try {
                    Check();
                } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
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
                            if (HomeActivity.safe) {
                                version = AESUtils.DarKnight.getEncrypted("64");
                                livestopcheat();
                            } else {
                                livestopcheat();
                            }
                        }else{
                            Toast.makeText(getActivity()," Root Access Was Not Granted ",Toast.LENGTH_LONG).show();
                        }

                    }
                }, 3000);
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
                //  Log.d("data",data);
                if (Helper.checkVPN(getActivity())) {
                    Toast.makeText(getActivity(), "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                } else {

                    new Thread(() -> {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            ShellUtils.SU("rm -rf" + getActivity().getFilesDir().toString() + "/scheat.sh");
                            try {
                                givenToFile(getActivity(), s);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        Toast.makeText(getContext(), "Wait While We Setting Up Things", Toast.LENGTH_LONG).show();

                    }).start();
                }
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

    public void livestartcheat(){
        class load extends AsyncTask<Void, Void, String> {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (Helper.checkVPN(getActivity())) {
                    Toast.makeText(getActivity(), "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                } else {
                    new Thread(() -> {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            ShellUtils.SU("rm -rf" + getActivity().getFilesDir().toString() +"/scheat.sh");
                            try {
                                givenToFile(getActivity(),s);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                                Toast.makeText(getContext(), "Wait While We Setting Up Things", Toast.LENGTH_LONG).show();
                                startPatcher();

                        });
                    }).start();
                }
            }
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(TAG_VERSION,version);
                params.put(TAG_DEVICEID,deviceid);
                params.put("g",AESUtils.DarKnight.getEncrypted("vn"));
                params.put("s",AESUtils.DarKnight.getEncrypted("start"));
                data =AESUtils.DarKnight.getDecrypted(reader.getUrlContents(CheatL,params));
                return data;
            }
        }
        new load().execute();

    }


    public void livestopcheat(){
        Context ctx =getActivity();

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
                    });
                    if(FloatLogo.isRunning  ) {
                        ctx.stopService(new Intent(ctx, FloatLogo.class));
                        ctx.stopService(new Intent(ctx, flogo.class));
                        ctx.stopService(new Intent(ctx, logo.class));
                    }
                }).start();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(TAG_VERSION,version);
                params.put(TAG_DEVICEID,deviceid);
                params.put("g",AESUtils.DarKnight.getEncrypted("vn"));
                params.put("s",AESUtils.DarKnight.getEncrypted("stop"));
                data =AESUtils.DarKnight.getDecrypted(reader.getUrlContents(CheatL,params));
                return data;
            }
        }
        new load().execute();

    }

    public void betastopcheat(){
        Context ctx = getActivity();
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
                    });


                }).start();
                if(FloatLogo.isRunning) {
                    ctx.stopService(new Intent(ctx, FloatLogo.class));
                    ctx.stopService(new Intent(ctx, flogo.class));
                    ctx.stopService(new Intent(ctx, logo.class));
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    private  void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if(imgLoad.Load(getActivity()).equals(time)){
            getActivity().finish();
        }
    }


    void startPatcher() {
        if (!Settings.canDrawOverlays(getActivity())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" +getActivity().getPackageName ()));
            startActivityForResult(intent, 123);
        } else {
            if(!isServiceRunning()) {
                startFloater();
            }else{
                Toast.makeText(getActivity(),"Service Already Running",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (FloatLogo.class.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }
    private void startFloater() {
        SharedPreferences shred =ctx.getSharedPreferences("userdetails", MODE_PRIVATE);
        JSONObject params = new JSONObject();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    try {
                        String s = null;
                        params.put(TAG_DEVICEID,Helper.getDeviceId(ctx));
                   //     params.put(TAG_ONESIGNALID,UUID);
                        params.put(TAG_KEY,shred.getString(TAG_KEY,"null"));
                        s= jsonParserString.makeHttpRequest(urlref.Main+"login.php", params);
                        if (s == null || s.isEmpty()) {
                            Toast.makeText(ctx, "Server Error", Toast.LENGTH_LONG).show();
                            return ;
                        }
                        JSONObject ack = new JSONObject(s);
                        String decData = Helper.profileDecrypt(ack.get("Data").toString(), ack.get("Hash").toString());
                        if (!Helper.verify(decData, ack.get("Sign").toString(), JSONParserString.publickey)) {
                            Toast.makeText(ctx, "Something Went Wrong", Toast.LENGTH_LONG).show();
                            return ;
                        } else {
                            //converting response to json object
                            JSONObject obj = new JSONObject(decData);

                            if (obj.getBoolean(urlref.TAG_ERROR) || shred.getString(TAG_KEY,"null").equals("null")) {
                                startActivity(new Intent(ctx,LoginActivity.class));
                                Toast.makeText(ctx ,"Integrity Check Failed",Toast.LENGTH_SHORT).show();
                            }else{
                                Intent i = new Intent(getActivity(),FloatLogo.class);
                                i.putExtra("gametype",GameType);
                                i.putExtra("gamename",gameName);
                                getActivity().startService(i);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
            }
        }).start();
    }
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()){
//
//            case R.id.taptoactivatevn:
//                if(Helper.checkVPN(getActivity())){
//                    Toast.makeText(getActivity(), "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
//                    getActivity().finish();
//                }
//                else {
//                    lottieDialog.show(getActivity().getFragmentManager(), "loo");
//                    lottieDialog.setCancelable(false);
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            lottieDialog.dismiss();
//                            startPatcher();
//                        }
//                    }, 2000);
//                }
//                break;
//        }
//    }
}




