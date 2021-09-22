package mobisocial.arcade.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import mobisocial.arcade.AESUtils;
import mobisocial.arcade.GccConfig.urlref;
import mobisocial.arcade.Helper;
import mobisocial.arcade.JavaUrlConnectionReader;
import mobisocial.arcade.GetFile;
import mobisocial.arcade.R;
import mobisocial.arcade.ShellUtils;

import static android.content.Context.MODE_PRIVATE;
import static mobisocial.arcade.GccConfig.urlref.defaltversion;
import static mobisocial.arcade.HomeActivity.safe;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {



    public SettingFragment() {
        // Required empty public constructor
    }
    private SeekBar seekBar;
    private TextView minview;
    private ImageView i;
    private Button logout,play32bit,play64bit;
    private String version, deviceid;

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
        SharedPreferences ga = getActivity().getSharedPreferences("game", MODE_PRIVATE);
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
        version = shred.getString("version", defaltversion);
        version = AESUtils.DarKnight.getEncrypted(version);
        deviceid = Helper.getDeviceId(getActivity());
        deviceid = AESUtils.DarKnight.getEncrypted(deviceid);
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

        final SeekBar slider =rootViewone.findViewById(R.id.seekBar);
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final String value = String.valueOf( (int)(min + total * progress) );
                minview.setText("Every "+value+" Minutes");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(safe){


                }else{
                    Toast.makeText(getContext(),"You are A Basic Plan User",Toast.LENGTH_SHORT).show();
                }

            }
        });



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://GameSploit.com"));
                startActivity(browserIntent);
            }
        });

        return rootViewone;
    }
}