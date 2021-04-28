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
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import mobisocial.arcade.AESUtils;
import mobisocial.arcade.GccConfig.urlref;
import mobisocial.arcade.Helper;
import mobisocial.arcade.JavaUrlConnectionReader;
import mobisocial.arcade.LoginActivity;
import mobisocial.arcade.PassME;
import mobisocial.arcade.R;
import mobisocial.arcade.ShellUtils;
import mobisocial.arcade.imgLoad;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;
import static android.content.Context.MODE_PRIVATE;
import static mobisocial.arcade.GccConfig.urlref.time;
public class HomeFragmentlite extends Fragment {

    View myFragment;
    private final JavaUrlConnectionReader reader = new JavaUrlConnectionReader();
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
                            if (HomeActivityLite.beta) {


                                betastartcheat();
                            }else{

                                livestartcheat();
                            }

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
                else {
                    antiban.show(getActivity().getFragmentManager(), "StopCheatGl");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            antiban.dismiss();
                            if (HomeActivityLite.beta) {

                                betastopcheat();
                            } else {
                                livestopcheat();
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
                       ShellUtils.SU("");

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
                      ShellUtils.SU("");
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
                      ShellUtils.SU("");
                    }
                },3000);
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
                   ShellUtils.SU(s);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(TAG_VERSION,version);
                params.put(TAG_DEVICEID,deviceid);
                params.put("g", AESUtils.DarKnight.getEncrypted("lite"));
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
                }).start();
                startPatcher();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(TAG_VERSION,version);
                params.put(TAG_DEVICEID,deviceid);
                params.put("g", AESUtils.DarKnight.getEncrypted("lite"));
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
                }).start();
                getActivity().stopService(new Intent(getActivity(),LiteFloatLogo.class));
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(TAG_VERSION,version);
                params.put(TAG_DEVICEID,deviceid);
                params.put("g", AESUtils.DarKnight.getEncrypted("lite"));
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
                    }).start();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(TAG_VERSION,version);
                params.put(TAG_DEVICEID,deviceid);
                params.put("g", AESUtils.DarKnight.getEncrypted("lite"));
                params.put("s", AESUtils.DarKnight.getEncrypted("stop"));
                data = AESUtils.DarKnight.getDecrypted(reader.getUrlContents(CheatB,params));
                return data;
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
        getActivity().stopService(new Intent(getActivity(),LiteFloatLogo.class));
        getActivity().startService(new Intent(getActivity(),LiteFloatLogo.class));
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




