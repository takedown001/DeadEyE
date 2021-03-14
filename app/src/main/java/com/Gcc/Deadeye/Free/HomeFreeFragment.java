package com.Gcc.Deadeye.Free;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.Gcc.Deadeye.PassME;
import com.Gcc.Deadeye.R;
import com.Gcc.Deadeye.imgLoad;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;

import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;


import static android.content.Context.MODE_PRIVATE;
import static com.Gcc.Deadeye.GccConfig.urlref.time;


public class HomeFreeFragment extends Fragment {

    View myFragment;

    ViewPager viewPager;
    TabLayout tabLayout;

    public HomeFreeFragment() {
        // Required empty public constructor
    }

    public static HomeFreeFragment getInstance()    {
        return new HomeFreeFragment();
    }
    private AdView mAdView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment = inflater.inflate(R.layout.fragment_freehome, container, false);

        try {
            Check();
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        viewPager = myFragment.findViewById(R.id.viewPager);
        tabLayout = myFragment.findViewById(R.id.tabLayout);


        mAdView = myFragment.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("a9af9e3bb9e8e354")
                .build();

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        mAdView.loadAd(adRequest);


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
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new GlobalFreeFragment(),"Global");
        adapter.addFragment(new KoreanFreeFragment(),"Korean");
        adapter.addFragment(new TaiwanFreeFragment(),"Taiwan");
        adapter.addFragment(new VeitnamFreeFragment(),"Veitnam");
        viewPager.setAdapter(adapter);
    }


}

