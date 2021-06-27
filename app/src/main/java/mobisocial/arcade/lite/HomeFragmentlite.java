package mobisocial.arcade.lite;


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
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.topjohnwu.superuser.Shell;

import mobisocial.arcade.AESUtils;
import mobisocial.arcade.FloatLogo;
import mobisocial.arcade.GameActivity;
import mobisocial.arcade.GccConfig.urlref;
import mobisocial.arcade.Helper;
import mobisocial.arcade.JavaUrlConnectionReader;
import mobisocial.arcade.LoginActivity;
import mobisocial.arcade.R;
import mobisocial.arcade.ShellUtils;
import mobisocial.arcade.flogo;
import mobisocial.arcade.imgLoad;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;
import mobisocial.arcade.logo;

import static android.content.Context.MODE_PRIVATE;
import static mobisocial.arcade.GccConfig.urlref.defaltversion;
import static mobisocial.arcade.GccConfig.urlref.time;
import static mobisocial.arcade.Helper.givenToFile;

public class HomeFragmentlite extends Fragment {

    View myFragment;
    private final JavaUrlConnectionReader reader = new JavaUrlConnectionReader();
    String CheatL = urlref.Liveserver + "cheat.php";
    Handler handler = new Handler();
    private static final String TAG_DURATION = urlref.TAG_DURATION;
    private static final String TAG_DEVICEID = urlref.TAG_DEVICEID;
    private static final String TAG_VERSION = "v";
    private TextView txtDay, txtHour, txtMinute, txtSecond;
    private long getduration;
    private String gameName = "com.pubg.imobile";
    static int GameType = 5;
    long secondsInMilli = 1000;
    long minutesInMilli = secondsInMilli * 60;
    long hoursInMilli = minutesInMilli * 60;
    long daysInMilli = hoursInMilli * 24;
    public HomeFragmentlite() {
        // Required empty public constructor
    }
    private String version,deviceid,data;
    Context ctx;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getActivity();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment = inflater.inflate(R.layout.fragment_lite, container, false);
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
        version = shred.getString("version", defaltversion);
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
                startActivity(new Intent(getActivity(), GameActivity.class));

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
                else if (Helper.appInstalledOrNot(gameName,getActivity())){
                    Toast.makeText(getActivity(), "Game Not Installed", Toast.LENGTH_LONG).show();
                }
                else {
                    antiban.show(getActivity().getFragmentManager(), "StartCheatGl");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            antiban.dismiss();
                            ShellUtils.SU("iptables -F");
                            ShellUtils.SU("iptables --flush");
                            if(Shell.rootAccess()) {
                                if (HomeActivityLite.safe) {
                                    try {
                                        Helper.unzip(getActivity());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    version = AESUtils.DarKnight.getEncrypted("64");
                                    livestartcheat();

                                } else {
                                    try {
                                        Helper.unzip(getActivity());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    livestartcheat();
                                }
                            }
                            else {
                                Toast.makeText(getActivity(),"Root Access Was Not Granted ", Toast.LENGTH_LONG).show();                            }
                        }
                    }, 3000);
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
                else if (Helper.appInstalledOrNot(gameName,getActivity())){
                    Toast.makeText(getActivity(), "Game Not Installed", Toast.LENGTH_LONG).show();
                }
                else {
                    antiban.show(getActivity().getFragmentManager(), "StopCheatGl");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            antiban.dismiss();
                            if (Shell.rootAccess()) {
                                if (HomeActivityLite.safe) {
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
                        Shell.su("rm -Rf /data/data/com.pubg.imobile/databases\\n\" +\n" +
                                "                                \"rm -Rf /data/data/com.pubg.imobile/shared_prefs/gsdk_prefs.xml\\n\" +\n" +
                                "                                \"rm -Rf /data/data/com.pubg.imobile/shared_prefs/APMCfg.xml\\n\" +\n" +
                                "                                \"rm -Rf /data/data/com.pubg.imobile/shared_prefs/device_id.xml\\n\" +\n" +
                                "                                \"echo -n \\\"<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\\\\n<map>\\\\n    <string name=\\\\\\\"random\\\\\\\"></string>\\\\n    <string name=\\\\\\\"install\\\\\\\"></string>\\\\n    <string name=\\\\\\\"uuid\\\\\\\">$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM</string>\\\\n</map>\\\" > /data/data/com.pubg.imobile/shared_prefs/device_id.xml\\n\" +\n" +
                                "                                \"chmod -R 555 /data/data/com.pubg.imobile/shared_prefs/device_id.xml\\n\" +\n" +
                                "                                \"rm -Rf /data/media/0/.backups\\n\" +\n" +
                                "                                \"rm -Rf /data/media/0/Android/data/com.pubg.imobile/cache\\n\" +\n" +
                                "                                \"rm -Rf /data/media/0/Android/data/com.pubg.imobile/files/login-identifier.txt\\n\" +\n" +
                                "                                \"rm -Rf /data/media/0/Android/data/com.pubg.imobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Intermediate/SaveGames/JKGuestRegisterCnt.json\\n\" +\n" +
                                "                                \"find /storage/emulated/0 -type f \\\\( -name \\\".fff\\\" -o -name \\\".zzz\\\" -o -name \\\".system_android_l2\\\" \\\\) -exec rm -Rf {} \\\\;").submit();
                    }
                }, 3000);
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
                      ShellUtils.SU("rm -Rf /data/data/com.pubg.imobile/lib\\n\" +\n" +
                              "                                \"rm -Rf /data/data/com.pubg.imobile/databases__hs_log_store\\n\" +\n" +
                              "                                \"touch /data/data/com.pubg.imobile/__hs_log_store\\n\" +\n" +
                              "                                \"chmod 444 /data/data/com.pubg.imobile/__hs_log_store\\n\" +\n" +
                              "                                \"chmod 555 /data/data/com.pubg.imobile/files/tss*\\n\" +\n" +
                              "                                \"chmod 555 /data/data/com.pubg.imobile/files/tersafe.update\\n\" +\n" +
                              "                                \"echo \\\"8192\\\" > /proc/sys/fs/inotify/max_user_watches\\n\" +\n" +
                              "                                \"echo \\\"8192\\\" > /proc/sys/fs/inotify/max_user_instances\\n\" +\n" +
                              "                                \"echo \\\"8192\\\" > /proc/sys/fs/inotify/max_queued_events\\n\" +\n" +
                              "                                \"rm -Rf /storage/emulated/0/Android/data/.um /storage/emulated/0/Android/data/pushSdk /storage/emulated/0/Android/obj /storage/emulated/0/.backups /storage/emulated/0/MidasOversea /storage/emulated/0/tencent\\n\" +\n" +
                              "                                \"find /storage/emulated/0 -type f \\\\( -name \\\".fff\\\" -o -name \\\".zzz\\\" -o -name \\\".system_android_l2\\\" \\\\) -exec rm -Rf {} \\\\;\\n\" +\n" +
                              "                                \"rm -rf /sdcard/Android/data/com.google.backup\\n\"+\n" +
                              "                                \"pm install -i com.android.vending /data/app/com.pubg.imobile-*/*.apk >/dev/null");
                    }
                },3000);
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
                      ShellUtils.SU("rm -Rf /data/data/com.pubg.imobile\\n\" +\n" +
                              "                                \"rm -Rf /storage/emulated/0/.backups/com.pubg.imobile\\n\" +\n" +
                              "                                \"rm -Rf /storage/emulated/0/Android/data/com.pubg.imobile/cache\\n\" +\n" +
                              "                                \"rm -Rf /storage/emulated/0/Android/data/com.pubg.imobile/files\\n\" +\n" +
                              "                                \"rm -Rf /storage/emulated/0/Android/data/.um /storage/emulated/0/Android/data/pushSdk /storage/emulated/0/Android/obj /storage/emulated/0/.backups /storage/emulated/0/MidasOversea /storage/emulated/0/tencent\\n\" +\n" +
                              "                                \"find /storage/emulated/0 -type f \\\\( -name \\\".fff\\\" -o -name \\\".zzz\\\" -o -name \\\".system_android_l2\\\" \\\\) -exec rm -Rf {} \\\\;\\n\" +\n" +
                              "                                \"pm install -i com.android.vending /data/app/com.pubg.imobile-*/*.apk >/dev/null\\n\" +\n" +
                              "                                \"#svc power reboot");
                    }
                },3000);
                //   new DownloadTask(getActivity(), URL);

            }
        });

        return myFragment;
    }

    public void livestartcheat(){
        class load extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                   // Log.d("data",data);
                new Thread(() -> {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        ShellUtils.SU("rm -rf" + getActivity().getFilesDir().toString() + "/scheat.sh");
                        try {
                            givenToFile(getActivity(), s);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    startPatcher();
               //
                }).start();
                Toast.makeText(getContext(), "Wait While We Setting Up Things", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(TAG_VERSION,version);
                params.put(TAG_DEVICEID,deviceid);
                params.put("g",AESUtils.DarKnight.getEncrypted("lite"));
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
                new Thread(() -> {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        ShellUtils.SU("rm -rf" + getActivity().getFilesDir().toString() + "/scheat.sh");
                        try {
                            givenToFile(getActivity(), s);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    ctx.stopService(new Intent(ctx,FloatLogo.class));
                    ctx.stopService(new Intent(ctx,flogo.class));
                    ctx.stopService(new Intent(ctx,logo.class));
                }).start();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(TAG_VERSION,version);
                params.put(TAG_DEVICEID,deviceid);
                params.put("g",AESUtils.DarKnight.getEncrypted("lite"));
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


    void startPatcher() {
        if (!Settings.canDrawOverlays(getActivity())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" +getActivity().getPackageName ()));
            startActivityForResult(intent, 123);
        } else {
            startFloater();
        }
    }

    private void startFloater() {
        ctx.stopService(new Intent(ctx, logo.class));
        ctx.stopService(new Intent(getActivity(), flogo.class));
       ctx.stopService(new Intent(getActivity(), FloatLogo.class));
        Intent i = new Intent(getActivity(),FloatLogo.class);
        i.putExtra("gametype",GameType);
        i.putExtra("gamename",gameName);
        getActivity().startService(i);
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




