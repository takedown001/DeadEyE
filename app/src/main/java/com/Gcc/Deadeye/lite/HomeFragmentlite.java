package com.Gcc.Deadeye.lite;


import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.Gcc.Deadeye.AESUtils;
import com.Gcc.Deadeye.ESPMainActivity;
import com.Gcc.Deadeye.ESPView;
import com.Gcc.Deadeye.FloatLogo;
import com.Gcc.Deadeye.GccConfig.urlref;
import com.Gcc.Deadeye.Helper;
import com.Gcc.Deadeye.HomeActivity;
import com.Gcc.Deadeye.JavaUrlConnectionReader;
import com.Gcc.Deadeye.LoginActivity;
import com.Gcc.Deadeye.Overlay;
import com.Gcc.Deadeye.PassME;
import com.Gcc.Deadeye.R;
import com.Gcc.Deadeye.SafeService;
import com.Gcc.Deadeye.ShellUtils;
import com.Gcc.Deadeye.imgLoad;
import com.google.android.material.tabs.TabLayout;

import java.io.DataOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;

import static android.content.Context.MODE_PRIVATE;
import static com.Gcc.Deadeye.GccConfig.urlref.time;


public class HomeFragmentlite extends Fragment {

    View myFragment;
    private final JavaUrlConnectionReader reader = new JavaUrlConnectionReader();
    ViewPager viewPager;
    TabLayout tabLayout;
    String CheatL = urlref.Liveserver + "cheat.php";
    String CheatB = urlref.Betaserver + "cheat.php";
    Handler handler = new Handler();
    private static final String TAG_DURATION = urlref.TAG_DURATION;
    private static final String TAG_DEVICEID = urlref.TAG_DEVICEID;
    private static final String TAG_VERSION = "v";
    private TextView txtDay, txtHour, txtMinute, txtSecond;
    private long getduration;
    long secondsInMilli = 1000;
    long minutesInMilli = secondsInMilli * 60;
    long hoursInMilli = minutesInMilli * 60;
    long daysInMilli = hoursInMilli * 24;
    public HomeFragmentlite() {
        // Required empty public constructor
    }
    private String version,deviceid,data;
    boolean safecheck,brutalcheck;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment = inflater.inflate(R.layout.fragment_lite, container, false);

        new PassME(getActivity()).execute();
        try {
            Check();
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        viewPager = myFragment.findViewById(R.id.viewPager);
//        tabLayout = myFragment.findViewById(R.id.tabLayout);
        SharedPreferences shred = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
        Context ctx = getActivity();
        getduration = shred.getLong(TAG_DURATION,0);
        txtDay =  myFragment.findViewById(R.id.viewdays);
        txtHour =  myFragment.findViewById(R.id.viewhours);
        txtMinute =  myFragment.findViewById(R.id.viewminutes);
        txtSecond = myFragment.findViewById(R.id.viewseconds);
        version = shred.getString("version", "32");
        version = AESUtils.DarKnight.getEncrypted(version);
        Button cleanguest, fixgame, StartCheat, StopCheat,DeepFixGame;
        StartCheat = myFragment.findViewById(R.id.startcheatlite);
        StopCheat = myFragment.findViewById(R.id.stopcheatlite);
        cleanguest = myFragment.findViewById(R.id.litecleanguest);
        fixgame = myFragment.findViewById(R.id.litefixgame);
        DeepFixGame = myFragment.findViewById(R.id.litedeepfixclean);
        deviceid = LoginActivity.getDeviceId(getActivity());
        deviceid = AESUtils.DarKnight.getEncrypted(deviceid);

        new CountDownTimer(getduration,1000){

            @Override
            public void onTick(long millisUntilFinished) {

                NumberFormat f = new DecimalFormat("00");
                long days = (millisUntilFinished / daysInMilli);
                millisUntilFinished = millisUntilFinished % daysInMilli;
                long hour = (millisUntilFinished / hoursInMilli);
                millisUntilFinished = millisUntilFinished % hoursInMilli;
                long min = (millisUntilFinished / minutesInMilli);
                millisUntilFinished = millisUntilFinished % minutesInMilli;
                long sec = (millisUntilFinished / secondsInMilli);


                txtDay.setText(f.format(days));
                txtHour.setText(f.format(hour));
                txtMinute.setText(f.format(min));
                txtSecond.setText(f.format(sec));
            }

            @Override
            public void onFinish() {
                Toast.makeText(getActivity(),"Subcription Expired",Toast.LENGTH_SHORT).show();
                shred.edit().clear().apply();
                startActivity(new Intent(getActivity(), LoginActivity.class));

            }
        }.start();

        SharedPreferences getserver = getActivity().getSharedPreferences("server",MODE_PRIVATE);
        final DialogFragment antiban = new LottieDialogFragment().newInstance("antiban.json",true);
        antiban.setCancelable(false);

        final DialogFragment fixgameani = new LottieDialogFragment().newInstance("settings.json",true);
        fixgameani.setCancelable(false);
        final DialogFragment cleanguestani = new LottieDialogFragment().newInstance("tick-confirm.json",true);
        cleanguestani.setCancelable(false);

        StartCheat.setOnClickListener(new View.OnClickListener() {

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
                            if (HomeActivity.beta) {

                                if (safecheck) {
                                    getActivity().startService(new Intent(getContext(), SafeService.class));
                                    ShellUtils.SU("chmod 777 " + getActivity().getFilesDir().toString()+"/liberror.so");
                                    PackageManager pm = getContext().getPackageManager();
                                    if (Helper.isPackageInstalled("com.tencent.iglite", pm)) {
                                        Intent i = new Intent(getContext(), ESPMainActivity.class);
                                        i.putExtra("game", "Lite");
                                        startActivity(i);
                                        Toast.makeText(getContext(), "Wait While We Setting Up Things", Toast.LENGTH_LONG).show();
                                        ShellUtils.SU(
                                                "am start -n com.tencent.iglite/com.epicgames.ue4.SplashActivity");

                                    } else {
                                        Toast.makeText(getContext(), "Game Not Installed", Toast.LENGTH_LONG).show();
                                    }
                                }
                                if (brutalcheck) {

                                }
                                else{
                                    PackageManager pm = getContext().getPackageManager();
                                    if (Helper.isPackageInstalled("com.tencent.ig", pm)) {
                                        Intent i = new Intent(getContext(), ESPMainActivity.class);
                                        i.putExtra("game", "Lite");
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
                                    if (Helper.isPackageInstalled("com.tencent.iglite", pm)) {
                                        Intent i = new Intent(getContext(), ESPMainActivity.class);
                                        i.putExtra("game", "Lite");
                                        startActivity(i);
                                        Toast.makeText(getContext(), "Wait While We Setting Up Things", Toast.LENGTH_LONG).show();
                                        ShellUtils.SU(
                                                "am start -n com.tencent.iglite/com.epicgames.ue4.SplashActivity");

                                    } else {
                                        Toast.makeText(getContext(), "Game Not Installed", Toast.LENGTH_LONG).show();
                                    }
                                }
                                if (brutalcheck) {

                                }else{
                                    PackageManager pm = getContext().getPackageManager();
                                    if (Helper.isPackageInstalled("com.tencent.iglite", pm)) {
                                        Intent i = new Intent(getContext(), ESPMainActivity.class);
                                        i.putExtra("game", "Lite");
                                        startActivity(i);
                                        Toast.makeText(getContext(), "Wait While We Setting Up Things", Toast.LENGTH_LONG).show();
                                        ShellUtils.SU(
                                                "am start -n com.tencent.iglite/com.epicgames.ue4.SplashActivity");

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


        StopCheat.setOnClickListener(new View.OnClickListener() {
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


                                if (safecheck) {
                                    getActivity().stopService(new Intent(getActivity(), SafeService.class));
                                    getActivity().stopService(new Intent(ctx, Overlay.class));
                                    ctx.stopService(new Intent(ctx, ESPView.class));
                                    ctx.stopService(new Intent(ctx, FloatLogo.class));
                                    ESPMainActivity.isDisplay = false;
                                    //startDaemon();
                                    ESPMainActivity.isDaemon = false;
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
                                    ESPMainActivity.isDisplay = false;
                                    //startDaemon();
                                    ESPMainActivity.isDaemon = false;
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
                            outputStream.writeBytes("GUEST=\"/data/data/com.tencent.iglite/shared_prefs/device_id.xml\"\n" +
                                    "kill com.tencent.iglite\n" +
                                    "rm -rf $GUEST\n" +
                                    "echo \"<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\n" +
                                    "<map>\n" +
                                    "    <string name=\\\"random\\\"></string>\n" +
                                    "    <string name=\\\"install\\\"></string>\n" +
                                    "    <string name=\\\"uuid\\\">$RANDOM$RANDOM-$RANDOM-$RANDOM-$RANDOM-$RANDOM$RANDOM$RANDOM</string>\n" +
                                    "</map>\" > $GUEST\n" +
                                    "rm -rf /data/media/0/Android/data/com.tencent.iglite/files/login-identifier.txt\n"
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

        return myFragment;
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
                params.put("g", AESUtils.DarKnight.getEncrypted("gl"));
                params.put("s", AESUtils.DarKnight.getEncrypted("start"));
                data = AESUtils.DarKnight.getDecrypted(reader.getUrlContents(CheatB,params));
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
                params.put("g", AESUtils.DarKnight.getEncrypted("gl"));
                params.put("s", AESUtils.DarKnight.getEncrypted("start"));
                data = AESUtils.DarKnight.getDecrypted(reader.getUrlContents(CheatL,params));
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
                params.put("g", AESUtils.DarKnight.getEncrypted("gl"));
                params.put("s", AESUtils.DarKnight.getEncrypted("stop"));
                data = AESUtils.DarKnight.getDecrypted(reader.getUrlContents(CheatL,params));
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
                params.put("g", AESUtils.DarKnight.getEncrypted("gl"));
                params.put("s", AESUtils.DarKnight.getEncrypted("stop"));
                data = AESUtils.DarKnight.getDecrypted(reader.getUrlContents(CheatB,params));
                return data;
            }
        }
        new load().execute();
    }


}


//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        setUpViewPager(viewPager);
//        tabLayout.setupWithViewPager(viewPager);
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//    }
//
//    private void setUpViewPager(ViewPager viewPager) {
//        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());
//        adapter.addFragment(new GlobalFragment(),"Global");
//        adapter.addFragment(new KoreanFragment(),"Korean");
//        adapter.addFragment(new TaiwanFragment(),"Taiwan");
//        adapter.addFragment(new VeitnamFragment(),"Veitnam");
//        viewPager.setAdapter(adapter);
//    }




