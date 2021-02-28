package com.Gcc.Deadeye.Fragment;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Gcc.Deadeye.Adapter.SectionPagerAdapter;
import com.Gcc.Deadeye.GccConfig.urlref;
import com.Gcc.Deadeye.LoginActivity;
import com.Gcc.Deadeye.R;
import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;


import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {

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
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment getInstance()    {
        return new HomeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment = inflater.inflate(R.layout.fragment_home, container, false);



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
        adapter.addFragment(new GlobalFragment(),"Global");
        adapter.addFragment(new KoreanFragment(),"Korean");
        adapter.addFragment(new TaiwanFragment(),"Taiwan");
        adapter.addFragment(new VeitnamFragment(),"Veitnam");
        viewPager.setAdapter(adapter);
    }


}

