package com.Gcc.Deadeye.Fragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;

import static android.content.Context.MODE_PRIVATE;


public class TaiwanFragment extends Fragment implements View.OnClickListener {

    public TaiwanFragment() {
        // Required empty public constructor
    }
    private final JavaUrlConnectionReader reader = new JavaUrlConnectionReader();
    private String data;

    String CheatL = urlref.Liveserver + "cheat.php";
    String CheatB = urlref.Betaserver + "cheat.php";

    private String version, deviceid;
    Handler handler = new Handler();
    private static final String TAG_DEVICEID =urlref.TAG_DEVICEID;
    private static final String TAG_VERSION = "v";
    ImageView taptoactivatetw;
    boolean espcheck,safecheck,brutalcheck;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

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
        View rootViewone = inflater.inflate(R.layout.fragment_taiwan, container, false);
        SharedPreferences shred = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
        SharedPreferences ga = getActivity().getSharedPreferences("game", MODE_PRIVATE);
        SharedPreferences.Editor g = ga.edit();
        g.putString("game", "Taiwan").apply();
        version = shred.getString("version", "32");
        version = AESUtils.DarKnight.getEncrypted(version);
        final File daemon = new File(urlref.pathoflib+urlref.livelib);
        Context ctx=getActivity();
        deviceid = LoginActivity.getDeviceId(getActivity());
        deviceid = AESUtils.DarKnight.getEncrypted(deviceid);

        Button cleanguest, fixgame, StartCheatTw, StopCheatTw,DeepFixGame;
        StartCheatTw = rootViewone.findViewById(R.id.startcheattw);
        StopCheatTw = rootViewone.findViewById(R.id.stopcheattw);
        cleanguest = rootViewone.findViewById(R.id.taiwancleanguest);
        fixgame = rootViewone.findViewById(R.id.taiwanfixgame);
        DeepFixGame = rootViewone.findViewById(R.id.taiwandeepfixgame);
        taptoactivatetw = rootViewone.findViewById(R.id.taptoactivatetw);
        taptoactivatetw.setOnClickListener(this);


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

        StartCheatTw.setOnClickListener(new View.OnClickListener() {

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

                                }
                                if (brutalcheck) {

                                }
                                //      Log.d("betastartcheat", String.valueOf(HomeActivity.beta));
                                betastartcheat();
                            } else {
                                //    Log.d("lolll", String.valueOf(espcheck));
                                if (safecheck) {
                                    getActivity().startService(new Intent(getContext(), SafeService.class));
                                    ShellUtils.SU("chmod 777 " + getActivity().getFilesDir().toString()+"/libtakedown.so");
                                }
                                if (brutalcheck) {

                                }
                                //       Log.d("livestartcheat", String.valueOf(HomeActivity.beta));
                                livestartcheat();
                            }

                        }
                    }, 4000);
                }
            }
        });


        StopCheatTw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {if(Helper.checkVPN(getActivity())){
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

                            try {
                                Process su = Runtime.getRuntime().exec("su");
                                DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                                outputStream.writeBytes("Target=\"/data/data/com.rekoo.pubgm/shared_prefs/device_id.xml\"\n" +
                                        "if [ \"$(pidof com.rekoo.pubgm)\" != \"\" ]\n" +
                                        "then\n" +
                                        "su -c killall com.rekoo.pubgm\n" +
                                        "fi\n" +
                                        " rm -rf $Target\n" +
                                        " touch $Target\n" +
                                        " chmod 777 $Target\n" +
                                        "echo \"\"\n" +
                                        "echo \"<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\n" +
                                        "<map>\n" +
                                        "    <string name=\\\"random\\\"></string>\n" +
                                        "    <string name=\\\"install\\\"></string>\n" +
                                        "    <string name=\\\"uuid\\\">$(tr -dc a-z0-9 </dev/urandom | head -c 32)</string>\n" +
                                        "</map> \" >> $Target\n" +
                                        "rm -rf /data/data/com.rekoo.pubgm/databases\n" +
                                        "rm -rf /data/media/0/Android/data/com.rekoo.pubgm/files/login-identifier.txt\n" +
                                        "chmod 644 $Target\n");
                                outputStream.flush();
                                outputStream.writeBytes("exit\n");
                                outputStream.flush();
                                su.waitFor();
                            } catch (IOException e) {
                                try {
                                    throw new Exception(e);
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                            } catch (InterruptedException e) {
                                try {
                                    throw new Exception(e);
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                            }


                        }
                    }, 4000);
                    //   new DownloadTask(getActivity(), URL);
                }

            }
        });

        fixgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antiban.show(getActivity().getFragmentManager(),"StopCheatTW");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        antiban.dismiss();
                        //   new DownloadTask(getActivity(), URL);
                        try {
                            Process su = Runtime.getRuntime().exec("su");
                            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                            outputStream.writeBytes("rm -rf /storage/emulated/0/Android/data/com.rekoo.pubgm/files/ProgramBinaryCache &>/dev/null\n" +
                                    "rm -rf /storage/emulated/0/Android/data/com.rekoo.pubgm\n" +
                                    "chmod 755 /data/media/0/Android/data/com.rekoo.pubgm\n" +
                                    "chmod 755 /data/media/0/Android/data/com.rekoo.pubgm/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.rekoo.pubgm/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.rekoo.pubgm/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.rekoo.pubgm/*/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.rekoo.pubgm/*/*/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.rekoo.pubgm/*/*/*/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.rekoo.pubgm/*/*/*/*/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.rekoo.pubgm/*/*/*/*/*/*/*/*\n" +
                                    "rm -rf /storage/emulated/0/Android/data/com.rekoo.pubgm\n" +
                                    "chmod 755 /data/data/com.rekoo.pubgm\n" +
                                    "chmod 755 /data/data/com.rekoo.pubgm/*\n" +
                                    "chmod 755 /data/data/com.rekoo.pubgm/*/*\n" +
                                    "chmod 755 /data/data/com.rekoo.pubgm/*/*/*\n" +
                                    "chmod 755 /data/data/com.rekoo.pubgm/*/*/*/*\n" +
                                    "chmod 755 /data/data/com.rekoo.pubgm/*/*/*/*/*\n" +
                                    "chmod 755 /data/data/com.rekoo.pubgm/*/*/*/*/*/*\n" +
                                    "chmod 755 /data/data/com.rekoo.pubgm/*/*/*/*/*/*/*\n" +
                                    "chmod 755 /data/data/com.rekoo.pubgm/*/*/*/*/*/*/*/*\n" +
                                    "rm -rf /data/data/com.rekoo.pubgm\n" +
                                    "pm install -r /data/app/com.rekoo.pubgm*/base.apk\n");
                            outputStream.flush();
                            outputStream.writeBytes("exit\n");
                            outputStream.flush();
                            su.waitFor();
                        } catch (IOException e) {
                            try {
                                throw new Exception(e);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        } catch (InterruptedException e) {
                            try {
                                throw new Exception(e);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }

                    }
                },4000);

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
                        try {
                            Process su = Runtime.getRuntime().exec("su");
                            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                            outputStream.writeBytes("rm -rf /storage/emulated/0/Android/data/com.rekoo.pubgm/files/ProgramBinaryCache &>/dev/null\n" +
                                    "rm -rf /storage/emulated/0/Android/data/com.rekoo.pubgm\n" +
                                    "chmod 755 /data/media/0/Android/data/com.rekoo.pubgm\n" +
                                    "chmod 755 /data/media/0/Android/data/com.rekoo.pubgm/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.rekoo.pubgm/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.rekoo.pubgm/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.rekoo.pubgm/*/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.rekoo.pubgm/*/*/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.rekoo.pubgm/*/*/*/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.rekoo.pubgm/*/*/*/*/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.rekoo.pubgm/*/*/*/*/*/*/*/*\n" +
                                    "rm -rf /storage/emulated/0/Android/data/com.rekoo.pubgm\n" +
                                    "chmod 755 /data/data/com.rekoo.pubgm\n" +
                                    "chmod 755 /data/data/com.rekoo.pubgm/*\n" +
                                    "chmod 755 /data/data/com.rekoo.pubgm/*/*\n" +
                                    "chmod 755 /data/data/com.rekoo.pubgm/*/*/*\n" +
                                    "chmod 755 /data/data/com.rekoo.pubgm/*/*/*/*\n" +
                                    "chmod 755 /data/data/com.rekoo.pubgm/*/*/*/*/*\n" +
                                    "chmod 755 /data/data/com.rekoo.pubgm/*/*/*/*/*/*\n" +
                                    "chmod 755 /data/data/com.rekoo.pubgm/*/*/*/*/*/*/*\n" +
                                    "chmod 755 /data/data/com.rekoo.pubgm/*/*/*/*/*/*/*/*\n" +
                                    "rm -rf /data/data/com.rekoo.pubgm\n" +
                                    "pm install -r /data/app/com.rekoo.pubgm*/base.apk\n");
                            outputStream.flush();
                            outputStream.writeBytes("exit\n");
                            outputStream.flush();
                            su.waitFor();
                        } catch (IOException e) {
                            try {
                                throw new Exception(e);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        } catch (InterruptedException e) {
                            try {
                                throw new Exception(e);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }

                    }
                },4000);

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
                params.put("g",AESUtils.DarKnight.getEncrypted("tw"));
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
                //              Log.d("data",data);
                if (Helper.checkVPN(getActivity())) {
                    Toast.makeText(getActivity(), "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                } else {
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
                params.put("g",AESUtils.DarKnight.getEncrypted("tw"));
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
                //     Log.d("data",data);
                if (Helper.checkVPN(getActivity())) {
                    Toast.makeText(getActivity(), "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                } else {
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
                params.put("g",AESUtils.DarKnight.getEncrypted("tw"));
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
                //      Log.d("data",data);
                if (Helper.checkVPN(getActivity())) {
                    Toast.makeText(getActivity(), "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                } else {
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
                params.put("g",AESUtils.DarKnight.getEncrypted("tw"));
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

            case R.id.taptoactivatetw:
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
                            if (Helper.isPackageInstalled("com.rekoo.pubgm", pm)) {
                                Intent i = new Intent(getContext(), MainActivity.class);
                                i.putExtra("game", "Taiwan");
                                startActivity(i);
                                Toast.makeText(getContext(), "Wait While We Setting Up Things", Toast.LENGTH_LONG).show();
                                ShellUtils.SU(
                                        "am start -n com.rekoo.pubgm/com.epicgames.ue4.SplashActivity");

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





