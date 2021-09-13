package mobisocial.arcade.free;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import mobisocial.arcade.GccConfig.urlref;
import mobisocial.arcade.R;
import mobisocial.arcade.ShellUtils;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FSettingFragment extends Fragment {



    public FSettingFragment() {
        // Required empty public constructor
    }

    private TextView minview;
    private ImageView i;
    private Button logout,play32bit,play64bit;
    private Handler handler = new Handler();


    // TODO: Rename and change types and number of parameters
    public static FSettingFragment newInstance(String param1, String param2) {
        FSettingFragment fragment = new FSettingFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




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
        SharedPreferences shred = getActivity().getSharedPreferences("Freeuserdetails", MODE_PRIVATE);

        SharedPreferences.Editor editor = shred.edit();
        minview = rootViewone.findViewById(R.id.everymin);
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
                Toast.makeText(getActivity(),"Currently We Do Not Support 64 Bit ",Toast.LENGTH_SHORT).show();
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
                minview.setText("Every "+progress+" Minutes");
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

                ShellUtils.SU("rm -rf "+ getActivity().getFilesDir().toString()+urlref.FreeMem);
                ShellUtils.SU("rm -rf "+ getActivity().getFilesDir().toString()+urlref.FreeHexMem);
                //Log.d("check","rm -rf "+ getActivity().getFilesDir().toString()+urlref.livelib);
                new FLoadMem(getActivity()).execute(urlref.MemPathPublic);
                new FHexLoad(getActivity()).execute(urlref.HexPublicLib);
                ShellUtils.SU("chmod 777 "+ getActivity().getFilesDir().toString()+urlref.FreeHexMem);
                ShellUtils.SU("chmod 777 "+ getActivity().getFilesDir().toString()+urlref.FreeMem);
            }
        });



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://Deadeye.Gcc-org.com"));
                startActivity(browserIntent);
            }
        });

        return rootViewone;
    }

}