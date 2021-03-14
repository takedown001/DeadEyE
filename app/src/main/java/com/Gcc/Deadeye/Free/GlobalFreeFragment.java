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
import com.Gcc.Deadeye.SafeService;
import com.Gcc.Deadeye.imgLoad;

import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;

import static android.content.Context.MODE_PRIVATE;
import static com.Gcc.Deadeye.GccConfig.urlref.time;

public class GlobalFreeFragment extends Fragment {

    private final JavaUrlConnectionReader reader = new JavaUrlConnectionReader();

    private String version, deviceid;
    Handler handler = new Handler();

    ImageView taptoactivategl;

    public GlobalFreeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        SharedPreferences ga = getActivity().getSharedPreferences("game", MODE_PRIVATE);
        SharedPreferences.Editor g = ga.edit();
        g.putString("game", "Global").apply();
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
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            antiban.dismiss();
                            PackageManager pm = getContext().getPackageManager();
                            if (Helper.isPackageInstalled("com.tencent.ig", pm)) {
                                Intent i = new Intent(getActivity(),EspFreeMainActivity.class);
                                i.putExtra("game",1);
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    private  void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if(imgLoad.Load(getActivity()).equals(time)){
            getActivity().finish();
        }
    }


}