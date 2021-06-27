package mobisocial.arcade.free;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import mobisocial.arcade.Adapter.SectionPagerAdapter;
import mobisocial.arcade.GccConfig.urlref;
import mobisocial.arcade.LoginActivity;
import mobisocial.arcade.R;
import mobisocial.arcade.imgLoad;
import com.google.android.material.tabs.TabLayout;

import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;


import static android.content.Context.MODE_PRIVATE;
import static mobisocial.arcade.GccConfig.urlref.time;


public class HomeFreeFragment extends Fragment {

    View myFragment;

    ViewPager viewPager;
    TabLayout tabLayout;
    private static final String TAG_DURATION = urlref.TAG_DURATION;
    private TextView txtDay, txtHour, txtMinute, txtSecond;
    private long getduration;

    long secondsInMilli = 1000;
    long minutesInMilli = secondsInMilli * 60;
    long hoursInMilli = minutesInMilli * 60;
    long daysInMilli = hoursInMilli * 24;
    public HomeFreeFragment() {
        // Required empty public constructor
    }

    public static HomeFreeFragment getInstance()    {
        return new HomeFreeFragment();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment = inflater.inflate(R.layout.fragment_home, container, false);
        try {
            Check();
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        viewPager = myFragment.findViewById(R.id.viewPager);
        tabLayout = myFragment.findViewById(R.id.tabLayout);
        SharedPreferences shred = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
        getduration = shred.getLong(TAG_DURATION,0);
        txtDay =  myFragment.findViewById(R.id.viewdays);
        txtHour =  myFragment.findViewById(R.id.viewhours);
        txtMinute =  myFragment.findViewById(R.id.viewminutes);
        txtSecond = myFragment.findViewById(R.id.viewseconds);


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


        return myFragment;
    }

    //Call onActivity Create method
    @RequiresApi(api = Build.VERSION_CODES.N)
    private  void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if(imgLoad.Load(getActivity()).equals(time)){
            getActivity().finish();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new GlobalFreeFragment(),"Global");
        adapter.addFragment(new KoreanFreeFragment(),"Korean");
        adapter.addFragment(new TaiwanFreeFragment(),"Taiwan");
        adapter.addFragment(new VeitnamFreeFragment(),"Vietnam");
        viewPager.setAdapter(adapter);
    }


}

