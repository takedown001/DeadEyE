package com.Gcc.Deadeye.Fragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.Gcc.Deadeye.AESUtils;
import com.Gcc.Deadeye.GccConfig.urlref;
import com.Gcc.Deadeye.Helper;
import com.Gcc.Deadeye.HomeActivity;
import com.Gcc.Deadeye.JavaUrlConnectionReader;
import com.Gcc.Deadeye.LoginActivity;
import com.Gcc.Deadeye.MainActivity;
import com.Gcc.Deadeye.R;
import com.Gcc.Deadeye.SafeService;
import com.Gcc.Deadeye.ShellUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import burakustun.com.lottieprogressdialog.LottieDialogFragment;
import static android.content.Context.MODE_PRIVATE;

public class KoreanFragment extends Fragment implements View.OnClickListener {

    public KoreanFragment() {
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
    ImageView taptoactivatekr;
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
        View rootViewone = inflater.inflate(R.layout.fragment_korean, container, false);
        SharedPreferences shred = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
        SharedPreferences ga = getActivity().getSharedPreferences("game", MODE_PRIVATE);
        SharedPreferences.Editor g = ga.edit();
        g.putString("game", "Korea").apply();
        version = shred.getString("version", "32");
        version = AESUtils.DarKnight.getEncrypted(version);
        final File daemon = new File(urlref.pathoflib+urlref.nameoflib);

        deviceid = LoginActivity.getDeviceId(getActivity());
        deviceid = AESUtils.DarKnight.getEncrypted(deviceid);

        Button cleanguest, fixgame, StartCheatKr, StopCheatKr,DeepFixGame;
        StartCheatKr = rootViewone.findViewById(R.id.startcheatkr);
        StopCheatKr = rootViewone.findViewById(R.id.stopcheatkr);
        cleanguest = rootViewone.findViewById(R.id.koreancleanguest);
        fixgame = rootViewone.findViewById(R.id.koreanfixgame);
        DeepFixGame = rootViewone.findViewById(R.id.koreandeepfixgame);
        taptoactivatekr = rootViewone.findViewById(R.id.taptoactivatekr);
        taptoactivatekr.setOnClickListener(this);


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

        StartCheatKr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Helper.checkVPN(getActivity())) {
                    Toast.makeText(getActivity(), "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                } else {
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
                                //     Log.d("betastartcheat", String.valueOf(HomeActivity.beta));
                                betastartcheat();
                            } else {
                                //     Log.d("lolll", String.valueOf(espcheck));
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


        StopCheatKr.setOnClickListener(new View.OnClickListener() {
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
                                    getActivity().startService(new Intent(getContext(), SafeService.class));
                                    ShellUtils.SU("chmod 777 " + getActivity().getFilesDir().toString()+"/liberror.so");
                                }
                                if (brutalcheck) {

                                }
                                betastopcheat();
                            } else {


                                if (safecheck) {
                                    getActivity().startService(new Intent(getContext(), SafeService.class));
                                    ShellUtils.SU("chmod 777 " + getActivity().getFilesDir().toString()+"/libtakedown.so");
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
                            outputStream.writeBytes("Target=\"/data/data/com.pubg.krmobile/shared_prefs/device_id.xml\"\n" +
                                    "if [ \"$(pidof com.pubg.krmobile)\" != \"\" ]\n" +
                                    "then\n" +
                                    "su -c killall com.pubg.krmobile\n" +
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
                                    "rm -rf /data/data/com.pubg.krmobile/databases\n" +
                                    "rm -rf /data/media/0/Android/data/com.pubg.krmobile/files/login-identifier.txt\n" +
                                    "chmod 644 $Target\n");
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
                            outputStream.writeBytes("rm -rf /storage/emulated/0/Android/data/com.pubg.krmobile/files/ProgramBinaryCache &>/dev/null\n" +
                                    "rm -rf /storage/emulated/0/Android/data/com.pubg.krmobile\n" +
                                    "chmod 755 /data/media/0/Android/data/com.pubg.krmobile\n" +
                                    "chmod 755 /data/media/0/Android/data/com.pubg.krmobile/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.pubg.krmobile/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.pubg.krmobile/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.pubg.krmobile/*/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.pubg.krmobile/*/*/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.pubg.krmobile/*/*/*/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.pubg.krmobile/*/*/*/*/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.pubg.krmobile/*/*/*/*/*/*/*/*\n" +
                                    "rm -rf /storage/emulated/0/Android/data/com.pubg.krmobile\n" +
                                    "chmod 755 /data/data/com.pubg.krmobile\n" +
                                    "chmod 755 /data/data/com.pubg.krmobile/*\n" +
                                    "chmod 755 /data/data/com.pubg.krmobile/*/*\n" +
                                    "chmod 755 /data/data/com.pubg.krmobile/*/*/*\n" +
                                    "chmod 755 /data/data/com.pubg.krmobile/*/*/*/*\n" +
                                    "chmod 755 /data/data/com.pubg.krmobile/*/*/*/*/*\n" +
                                    "chmod 755 /data/data/com.pubg.krmobile/*/*/*/*/*/*\n" +
                                    "chmod 755 /data/data/com.pubg.krmobile/*/*/*/*/*/*/*\n" +
                                    "chmod 755 /data/data/com.pubg.krmobile/*/*/*/*/*/*/*/*\n" +
                                    "rm -rf /data/data/com.pubg.krmobile\n" +
                                    "pm install -r /data/app/com.pubg.krmobile*/base.apk\n");
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
                fixgameani.show(getActivity().getFragmentManager(),"fixgame");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fixgameani.dismiss();
                        try {
                            Process su = Runtime.getRuntime().exec("su");
                            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                            outputStream.writeBytes("rm -rf /storage/emulated/0/Android/data/com.pubg.krmobile/files/ProgramBinaryCache &>/dev/null\n" +
                                    "rm -rf /storage/emulated/0/Android/data/com.pubg.krmobile\n" +
                                    "chmod 755 /data/media/0/Android/data/com.pubg.krmobile\n" +
                                    "chmod 755 /data/media/0/Android/data/com.pubg.krmobile/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.pubg.krmobile/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.pubg.krmobile/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.pubg.krmobile/*/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.pubg.krmobile/*/*/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.pubg.krmobile/*/*/*/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.pubg.krmobile/*/*/*/*/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.pubg.krmobile/*/*/*/*/*/*/*/*\n" +
                                    "rm -rf /storage/emulated/0/Android/data/com.pubg.krmobile\n" +
                                    "chmod 755 /data/data/com.pubg.krmobile\n" +
                                    "chmod 755 /data/data/com.pubg.krmobile/*\n" +
                                    "chmod 755 /data/data/com.pubg.krmobile/*/*\n" +
                                    "chmod 755 /data/data/com.pubg.krmobile/*/*/*\n" +
                                    "chmod 755 /data/data/com.pubg.krmobile/*/*/*/*\n" +
                                    "chmod 755 /data/data/com.pubg.krmobile/*/*/*/*/*\n" +
                                    "chmod 755 /data/data/com.pubg.krmobile/*/*/*/*/*/*\n" +
                                    "chmod 755 /data/data/com.pubg.krmobile/*/*/*/*/*/*/*\n" +
                                    "chmod 755 /data/data/com.pubg.krmobile/*/*/*/*/*/*/*/*\n" +
                                    "rm -rf /data/data/com.pubg.krmobile\n" +
                                    "pm install -r /data/app/com.pubg.krmobile*/base.apk\n");
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
                //       Log.d("data",data);
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
                params.put("g",AESUtils.DarKnight.getEncrypted("kr"));
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
                params.put("g",AESUtils.DarKnight.getEncrypted("kr"));
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
                params.put("g",AESUtils.DarKnight.getEncrypted("kr"));
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
                //            Log.d("data",data);
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
                params.put("g",AESUtils.DarKnight.getEncrypted("kr"));
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

            case R.id.taptoactivatekr:
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
                            if (Helper.isPackageInstalled("com.pubg.krmobile", pm)) {
                                Intent i = new Intent(getContext(), MainActivity.class);
                                i.putExtra("game", "Korea");
                                startActivity(i);
                                Toast.makeText(getContext(), "Wait While We Setting Up Things", Toast.LENGTH_LONG).show();
                                ShellUtils.SU(
                                        "am start -n com.pubg.krmobile/com.epicgames.ue4.SplashActivity");

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





