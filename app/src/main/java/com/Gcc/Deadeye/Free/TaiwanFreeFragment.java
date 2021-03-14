package com.Gcc.Deadeye.Free;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import com.Gcc.Deadeye.GccConfig.urlref;
import com.Gcc.Deadeye.Helper;
import com.Gcc.Deadeye.JavaUrlConnectionReader;
import com.Gcc.Deadeye.LoginActivity;
import com.Gcc.Deadeye.Overlay;
import com.Gcc.Deadeye.R;
import com.Gcc.Deadeye.imgLoad;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;

import static android.content.Context.MODE_PRIVATE;
import static com.Gcc.Deadeye.GccConfig.urlref.time;


public class TaiwanFreeFragment extends Fragment {

    public TaiwanFreeFragment() {
        // Required empty public constructor
    }
    private final JavaUrlConnectionReader reader = new JavaUrlConnectionReader();
    private String data;

    private String version, deviceid;
    Handler handler = new Handler();
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

        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        View rootViewone = inflater.inflate(R.layout.fragment_taiwan, container, false);
        SharedPreferences ga = getActivity().getSharedPreferences("game", MODE_PRIVATE);
        SharedPreferences.Editor g = ga.edit();
        g.putString("game", "Taiwan").apply();
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
                            PackageManager pm = getContext().getPackageManager();
                            if (Helper.isPackageInstalled("com.rekoo.pubgm", pm)) {
                                Intent i = new Intent(getContext(), EspFreeMainActivity.class);
                                i.putExtra("game", 4);
                                startActivity(i);
                                Toast.makeText(getContext(), "Wait While We Setting Up Things", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(getContext(), "Game Not Installed", Toast.LENGTH_LONG).show();
                            }

                        }
                    }, 4000);
                }
            }
        });


        StopCheatTw.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {if(Helper.checkVPN(getActivity())){
                try {
                    Check();
                } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                antiban.show(getActivity().getFragmentManager(), "StopCheatGl");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        antiban.dismiss();
                        getActivity().stopService(new Intent(getActivity(), FreeService.class));
                        getActivity().stopService(new Intent(getActivity(), ESPView.class));
                        getActivity().stopService(new Intent(getActivity(), Overlay.class));
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
                                outputStream.writeBytes("GUEST=\"/data/data/com.rekoo.pubgm/shared_prefs/device_id.xml\"\n" +
                                        "kill com.rekoo.pubgm\n" +
                                        "rm -rf $GUEST\n" +
                                        "echo \"<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\n" +
                                        "<map>\n" +
                                        "    <string name=\\\"random\\\"></string>\n" +
                                        "    <string name=\\\"install\\\"></string>\n" +
                                        "    <string name=\\\"uuid\\\">$RANDOM$RANDOM-$RANDOM-$RANDOM-$RANDOM-$RANDOM$RANDOM$RANDOM</string>\n" +
                                        "</map>\" > $GUEST\n" +
                                        "rm -rf /data/media/0/Android/data/com.rekoo.pubgm/files/login-identifier.txt\n");
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
                        } catch (IOException | InterruptedException e) {
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private  void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if(imgLoad.Load(getActivity()).equals(time)){
           getActivity().finish();
        }
    }

}





