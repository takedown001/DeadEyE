package com.Gcc.Deadeye.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.Gcc.Deadeye.DownloadFile;
import com.Gcc.Deadeye.GccConfig.urlref;
import com.Gcc.Deadeye.LoadBeta;
import com.Gcc.Deadeye.R;
import com.Gcc.Deadeye.ShellUtils;
import com.ramotion.fluidslider.FluidSlider;

import java.io.DataOutputStream;
import java.io.IOException;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

import static android.content.Context.MODE_PRIVATE;
import static com.Gcc.Deadeye.HomeActivity.safe;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {



    public SettingFragment() {
        // Required empty public constructor
    }
    private FluidSlider seekBar;
    private TextView minview;
    private ImageView i;
    private Button logout,play32bit,play64bit;
    private Handler handler = new Handler();


    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }


    }

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




        View rootViewone = inflater.inflate(R.layout.fragment_setting, container, false);
        SharedPreferences shred = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);

        SharedPreferences.Editor editor = shred.edit();

        minview = rootViewone.findViewById(R.id.everymin);
        seekBar = rootViewone.findViewById(R.id.seekBar);
        logout = rootViewone.findViewById(R.id.logoutbtn);
        i = rootViewone.findViewById(R.id.updateCheat);
        play32bit = rootViewone.findViewById(R.id.playstore32version);
        play64bit = rootViewone.findViewById(R.id.playstore64version);
        SharedPreferences cshred = getActivity().getSharedPreferences("storecolor", MODE_PRIVATE);
        String white;
        String orange;
        int bit32;
        int bit64;
        orange = cshred.getString("bg32color",getResources().getResourceEntryName(R.drawable.version32bitback));
        white = cshred.getString("bg64color", getResources().getResourceEntryName(R.drawable.playstore64back));
        bit32 = getResources().getIdentifier(white,"drawable",getActivity().getPackageName());
        bit64 = getResources().getIdentifier(orange,"drawable",getActivity().getPackageName());

        play64bit.setTextColor(cshred.getInt("text64color",getResources().getColor((R.color.black))));
        play64bit.setBackgroundResource(bit32);
        play32bit.setTextColor(cshred.getInt("textcolor32",getResources().getColor((R.color.white))));
        play32bit.setBackgroundResource(bit64);


        play32bit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences cshred = getActivity().getSharedPreferences("storecolor", MODE_PRIVATE);
                SharedPreferences.Editor ceditor = cshred.edit();

                play32bit.setBackgroundDrawable(getResources().getDrawable((R.drawable.version32bitback)));
                play32bit.setTextColor(getResources().getColor((R.color.white)));
                play64bit.setBackgroundDrawable(getResources().getDrawable((R.drawable.playstore64back)));
                play64bit.setTextColor(getResources().getColor(R.color.black));
                ceditor.putInt("textcolor32",getResources().getColor(R.color.orange));
                ceditor.putString("bg64color", getResources().getResourceEntryName(R.drawable.playstore64back));
                ceditor.putInt("text64color",getResources().getColor((R.color.white)));
                ceditor.putString("bg32color",getResources().getResourceEntryName(R.drawable.version32bitback));
                ceditor.apply();
                editor.putString("version","32" );
                editor.apply();

            }
        });
        play64bit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences cshred = getActivity().getSharedPreferences("storecolor", MODE_PRIVATE);
                SharedPreferences.Editor ceditor = cshred.edit();
                play64bit.setBackgroundDrawable(getResources().getDrawable((R.drawable.version32bitback)));
                play64bit.setTextColor(getResources().getColor(R.color.white));
                play32bit.setBackgroundDrawable(getResources().getDrawable((R.drawable.playstore64back)));
                play32bit.setTextColor(getResources().getColor((R.color.black)));
                ceditor.putInt("textcolor32",getResources().getColor(R.color.black));
                ceditor.putString("bg32color", getResources().getResourceEntryName(R.drawable.version32bitback));
                ceditor.putInt("text64color",getResources().getColor((R.color.white)));
                ceditor.putString("bg64color", getResources().getResourceEntryName((R.drawable.playstore64back)));
                ceditor.apply();
                editor.putString("version","64");
                editor.apply();
            }
        });

        final int max = 10;
        final int min = 1;
        final int total = max - min;

        final FluidSlider slider =rootViewone.findViewById(R.id.seekBar);
        slider.setBeginTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        });

        slider.setEndTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        });

        // Java 8 lambda
        slider.setPositionListener(pos -> {
            final String value = String.valueOf( (int)(min + total * pos) );
            slider.setBubbleText(value);
            minview.setText("Every "+value+" Minutes");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {


                    try {
                        Process su = Runtime.getRuntime().exec("su");
                        DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                        outputStream.writeBytes("#!/bin/bash\n" +
                                "rm -rf /sdcard/Android/data/com.*/cache\n" +
                                "rm -rf /sdcard/Android/data/bin.*/cache\n" +
                                "rm -rf /sdcard/Android/data/by.*/cache\n" +
                                "rm -rf /sdcard/Android/data/ru.*/cache\n" +
                                "rm -rf /sdcard/Android/data/eu.*/cache\n" +
                                "rm -rf /sdcard/Android/data/org.*/cache\n" +
                                "rm -rf /sdcard/Android/data/tv.*/cache\n" +
                                "rm -rf /sdcard/Android/data/net.*/cache\n" +
                                "rm -rf /sdcard/Android/data/*/cache\n" +
                                "rm -rf /data/data/com.*/cache\n" +
                                "rm -rf /data/data/bin.*/cache\n" +
                                "rm -rf /data/data/by.*/cache\n" +
                                "rm -rf /data/data/ru.*/cache\n" +
                                "rm -rf /data/data/eu.*/cache\n" +
                                "rm -rf /data/data/org.*/cache\n" +
                                "rm -rf /data/data/tv.*/cache\n" +
                                "rm -rf /data/data/net.*/cache\n" +
                                "rm -rf /data/data/*/cache\n" +
                                "if [ $(pidof com.tencent.ig) ]; then\n" +
                                "  rm -rf /data/system/graphicsstats/*/com.tencent.ig\n" +
                                "  rm -rf /data/user_de/0/com.tencent.*\n" +
                                "  rm -rf /data/data/com.tencent.ig/app_*\n" +
                                "  rm -rf /config/sdcardfs/com.tencent.*\n" +
                                "  rm -rf /storage/emulated/0/.backups\n" +
                                "  rm -rf /storage/emulated/0/tencent\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Logs\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/TGPA\n" +
                                "  rm -rf '/storage/emulated/0/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/Epic Games'\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Intermediate/SaveGames\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/LightData\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/PufferEifs0\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/PufferEifs1\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/PufferTmpDir\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/SaveGames/UIElemLayout_*\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/*.json\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/*.flist\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/*.res\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/puffer*\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/Puffer*\n" +
                                "elif [ $(pidof com.pubg.krmobile) ]; then\n" +
                                "  rm -rf /data/system/graphicsstats/*/com.pubg.krmobile\n" +
                                "  rm -rf /data/user_de/0/com.tencent.*\n" +
                                "  rm -rf /data/data/com.pubg.krmobile/app_*\n" +
                                "  rm -rf /config/sdcardfs/com.tencent.*\n" +
                                "  rm -rf /storage/emulated/0/.backups\n" +
                                "  rm -rf /storage/emulated/0/tencent\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.pubg.krmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Logs\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.pubg.krmobile/files/TGPA\n" +
                                "  rm -rf '/storage/emulated/0/Android/data/com.pubg.krmobile/files/UE4Game/ShadowTrackerExtra/Epic Games'\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.pubg.krmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Intermediate/SaveGames\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.pubg.krmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/LightData\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.pubg.krmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/PufferEifs0\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.pubg.krmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/PufferEifs1\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.pubg.krmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/PufferTmpDir\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.pubg.krmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/SaveGames/UIElemLayout_*\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.pubg.krmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/*.json\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.pubg.krmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/*.flist\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.pubg.krmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/*.res\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.pubg.krmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/puffer*\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.pubg.krmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/Puffer*\n" +
                                "  elif [ $(pidof com.vng.pubgmobile) ]; then\n" +
                                "  rm -rf /data/system/graphicsstats/*/com.vng.pubgmobile\n" +
                                "  rm -rf /data/user_de/0/com.tencent.*\n" +
                                "  rm -rf /data/data/com.vng.pubgmobile/app_*\n" +
                                "  rm -rf /config/sdcardfs/com.tencent.*\n" +
                                "  rm -rf /storage/emulated/0/.backups\n" +
                                "  rm -rf /storage/emulated/0/tencent\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.vng.pubgmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Logs\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.vng.pubgmobile/files/TGPA\n" +
                                "  rm -rf '/storage/emulated/0/Android/data/com.vng.pubgmobile/files/UE4Game/ShadowTrackerExtra/Epic Games'\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.vng.pubgmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Intermediate/SaveGames\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.vng.pubgmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/LightData\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.vng.pubgmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/PufferEifs0\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.vng.pubgmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/PufferEifs1\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.vng.pubgmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/PufferTmpDir\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.vng.pubgmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/SaveGames/UIElemLayout_*\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.vng.pubgmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/*.json\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.vng.pubgmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/*.flist\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.vng.pubgmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/*.res\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.vng.pubgmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/puffer*\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.vng.pubgmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/Puffer*\n" +
                                "  elif [ $(pidof com.rekoo.pubgm) ]; then\n" +
                                "  rm -rf /data/system/graphicsstats/*/com.rekoo.pubgm\n" +
                                "  rm -rf /data/user_de/0/com.tencent.*\n" +
                                "  rm -rf /data/data/com.rekoo.pubgm/app_*\n" +
                                "  rm -rf /config/sdcardfs/com.tencent.*\n" +
                                "  rm -rf /storage/emulated/0/.backups\n" +
                                "  rm -rf /storage/emulated/0/tencent\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.rekoo.pubgm/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Logs\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.rekoo.pubgm/files/TGPA\n" +
                                "  rm -rf '/storage/emulated/0/Android/data/com.rekoo.pubgm/files/UE4Game/ShadowTrackerExtra/Epic Games'\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.rekoo.pubgm/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Intermediate/SaveGames\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.rekoo.pubgm/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/LightData\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.rekoo.pubgm/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/PufferEifs0\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.rekoo.pubgm/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/PufferEifs1\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.rekoo.pubgm/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/PufferTmpDir\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.rekoo.pubgm/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/SaveGames/UIElemLayout_*\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.rekoo.pubgm/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/*.json\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.rekoo.pubgm/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/*.flist\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.rekoo.pubgm/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/*.res\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.rekoo.pubgm/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/puffer*\n" +
                                "  rm -rf /storage/emulated/0/Android/data/com.rekoo.pubgm/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/Puffer*\n" +
                                "  else\n" +
                                "  exit\n" +
                                "fi\n");
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
            },(int)(min + total * pos)*1000*60);
            //Log.d("slider", value);
            return Unit.INSTANCE;
        });


        slider.setPosition(0.3f);
        slider.setStartText(String.valueOf(min));
        slider.setEndText(String.valueOf(max));

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(safe){


                ShellUtils.SU("rm -rf "+ getActivity().getFilesDir().toString()+urlref.livelib);
                ShellUtils.SU("rm -rf "+ getActivity().getFilesDir().toString()+ urlref.Betalib);
                //Log.d("check","rm -rf "+ getActivity().getFilesDir().toString()+urlref.livelib);
                new DownloadFile(getActivity()).execute(urlref.downloadpathLive);
                new LoadBeta(getActivity()).execute(urlref.downloadpathBeta);
                ShellUtils.SU("chmod 777 "+ getActivity().getFilesDir().toString()+urlref.livelib);
                ShellUtils.SU("chmod 777 "+ getActivity().getFilesDir().toString()+urlref.Betalib);
                }else{
                    Toast.makeText(getContext(),"You are A Basic Plan User",Toast.LENGTH_SHORT).show();
                }

            }
        });



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://Deadeye.Gcc-org.com"));
                startActivity(browserIntent);
            }
        });

        return rootViewone;
    }

}