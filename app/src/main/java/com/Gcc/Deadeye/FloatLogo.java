package com.Gcc.Deadeye;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;


import com.ramotion.fluidslider.FluidSlider;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

import static com.Gcc.Deadeye.Overlay.Size;
import static java.lang.System.exit;

public class FloatLogo extends Service implements View.OnClickListener {

    private WindowManager mWindowManager;
    private View mFloatingView;

    @SuppressLint("StaticFieldLeak")
    private static FloatLogo Instance;

    public FloatLogo() {
    }
    static {
        System.loadLibrary("vxposed");
    }
     LinearLayout player ;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    View espView,logoView;

    @SuppressLint("CutPasteId")
    @Override
    public void onCreate() {
        super.onCreate();
        Instance=this;
        createOver();
        player =mFloatingView.findViewById(R.id.players);
        logoView = mFloatingView.findViewById(R.id.relativeLayoutParent);
        espView = mFloatingView.findViewById(R.id.espView);
        Init();

    }

    @SuppressLint("InflateParams")
    void createOver(){
        //getting the widget layout from xml using layout inflater
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.float_logo, null);
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        //setting the layout parameters
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.RGBA_8888);


        //getting windows services and adding the floating view to it
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);


        final GestureDetector gestureDetector = new GestureDetector(this, new SingleTapConfirm());

        //window funclion
        ImageView closeBtn=mFloatingView.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                espView.setVisibility(View.GONE);
                logoView.setVisibility(View.VISIBLE);
            }
        });



        final LinearLayout items =mFloatingView.findViewById(R.id.items);
        final LinearLayout vehicle =mFloatingView.findViewById(R.id.vehicles);
        final ImageView playerBtn=mFloatingView.findViewById(R.id.playerBtn);
        final ImageView itemBtn=mFloatingView.findViewById(R.id.itemBtn);
        final ImageView vehicleBtn=mFloatingView.findViewById(R.id.vehicleBtn);
        final LinearLayout specialTab = mFloatingView.findViewById(R.id.specialTab);
        final TextView Special = mFloatingView.findViewById(R.id.Special);
        final LinearLayout HealthTab = mFloatingView.findViewById(R.id.HealthTab);
        final TextView Health = mFloatingView.findViewById(R.id.Health);
        final LinearLayout ArmorsTab = mFloatingView.findViewById(R.id.ArmorsTab);
        final TextView Armors = mFloatingView.findViewById(R.id.Armors);
        final LinearLayout MiscTab = mFloatingView.findViewById(R.id.MiscTab);
        final TextView Misc = mFloatingView.findViewById(R.id.Misc);
        final LinearLayout ScopesTab = mFloatingView.findViewById(R.id.ScopesTab);
        final TextView Scopes = mFloatingView.findViewById(R.id.scops);
        final LinearLayout WeaponTab = mFloatingView.findViewById(R.id.WeaponTab);
        final TextView Weapon = mFloatingView.findViewById(R.id.weapons);
        final LinearLayout choice = mFloatingView.findViewById(R.id.choice);
        final LinearLayout AmmoTab = mFloatingView.findViewById(R.id.AmmoTab);
        final TextView Ammo = mFloatingView.findViewById(R.id.Ammo);
        Weapon.setBackgroundColor(getResources().getColor((R.color.color4)));

        Ammo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Ammo.setBackgroundColor(getResources().getColor((R.color.color4)));
                Weapon.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Armors.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Scopes.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Health.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Misc.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Special.setBackgroundColor(getResources().getColor((R.color.blueij)));
                AmmoTab.setVisibility(View.VISIBLE);
                ScopesTab.setVisibility(View.GONE);
                WeaponTab.setVisibility(View.GONE);
                HealthTab.setVisibility(View.GONE);
                MiscTab.setVisibility(View.GONE);
                ArmorsTab.setVisibility(View.GONE);
                specialTab.setVisibility(View.GONE);
            }
        });



        Scopes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Scopes.setBackgroundColor(getResources().getColor((R.color.color4)));
                Weapon.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Armors.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Health.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Misc.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Special.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Ammo.setBackgroundColor(getResources().getColor((R.color.blueij)));

                ScopesTab.setVisibility(View.VISIBLE);
                WeaponTab.setVisibility(View.GONE);
                HealthTab.setVisibility(View.GONE);
                MiscTab.setVisibility(View.GONE);
                AmmoTab.setVisibility(View.GONE);
                ArmorsTab.setVisibility(View.GONE);
                specialTab.setVisibility(View.GONE);
            }
        });



        Weapon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Weapon.setBackgroundColor(getResources().getColor((R.color.color4)));
                Armors.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Health.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Misc.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Special.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Ammo.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Scopes.setBackgroundColor(getResources().getColor((R.color.blueij)));


                WeaponTab.setVisibility(View.VISIBLE);
                ScopesTab.setVisibility(View.GONE);
                HealthTab.setVisibility(View.GONE);
                MiscTab.setVisibility(View.GONE);
                AmmoTab.setVisibility(View.GONE);
                ArmorsTab.setVisibility(View.GONE);
                specialTab.setVisibility(View.GONE);
            }
        });

        Scopes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Weapon.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Armors.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Health.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Misc.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Special.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Ammo.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Scopes.setBackgroundColor(getResources().getColor((R.color.color4)));



                ScopesTab.setVisibility(View.VISIBLE);
                HealthTab.setVisibility(View.GONE);
                MiscTab.setVisibility(View.GONE);
                ArmorsTab.setVisibility(View.GONE);
                WeaponTab.setVisibility(View.GONE);
                AmmoTab.setVisibility(View.GONE);
                specialTab.setVisibility(View.GONE);
            }
        });


        Misc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Weapon.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Armors.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Health.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Misc.setBackgroundColor(getResources().getColor((R.color.color4)));
                Special.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Ammo.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Scopes.setBackgroundColor(getResources().getColor((R.color.blueij)));



                MiscTab.setVisibility(View.VISIBLE);
                WeaponTab.setVisibility(View.GONE);
                HealthTab.setVisibility(View.GONE);
                ArmorsTab.setVisibility(View.GONE);
                AmmoTab.setVisibility(View.GONE);
                ScopesTab.setVisibility(View.GONE);
                specialTab.setVisibility(View.GONE);
            }
        });



        Armors.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Weapon.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Armors.setBackgroundColor(getResources().getColor((R.color.color4)));
                Health.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Misc.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Special.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Ammo.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Scopes.setBackgroundColor(getResources().getColor((R.color.blueij)));


                ArmorsTab.setVisibility(View.VISIBLE);
                WeaponTab.setVisibility(View.GONE);
                MiscTab.setVisibility(View.GONE);
                AmmoTab.setVisibility(View.GONE);
                HealthTab.setVisibility(View.GONE);
                ScopesTab.setVisibility(View.GONE);
                specialTab.setVisibility(View.GONE);
            }
        });



        Health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Weapon.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Armors.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Health.setBackgroundColor(getResources().getColor((R.color.color4)));
                Misc.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Special.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Ammo.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Scopes.setBackgroundColor(getResources().getColor((R.color.blueij)));


                HealthTab.setVisibility(View.VISIBLE);
                WeaponTab.setVisibility(View.GONE);
                ArmorsTab.setVisibility(View.GONE);
                MiscTab.setVisibility(View.GONE);
                AmmoTab.setVisibility(View.GONE);
                ScopesTab.setVisibility(View.GONE);
                specialTab.setVisibility(View.GONE);
            }
        });

        Special.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Weapon.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Armors.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Health.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Misc.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Special.setBackgroundColor(getResources().getColor((R.color.color4)));
                Ammo.setBackgroundColor(getResources().getColor((R.color.blueij)));
                Scopes.setBackgroundColor(getResources().getColor((R.color.blueij)));


                specialTab.setVisibility(View.VISIBLE);
                WeaponTab.setVisibility(View.GONE);
                ArmorsTab.setVisibility(View.GONE);
                MiscTab.setVisibility(View.GONE);
                AmmoTab.setVisibility(View.GONE);
                ScopesTab.setVisibility(View.GONE);
                HealthTab.setVisibility(View.GONE);
            }
        });

        playerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice.setVisibility(View.GONE);
                items.setVisibility(View.GONE);
                player.setVisibility(View.VISIBLE);
                vehicle.setVisibility(View.GONE);
            }
        });

        itemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WeaponTab.setVisibility(View.VISIBLE);
                items.setVisibility(View.VISIBLE);
                player.setVisibility(View.GONE);
                vehicle.setVisibility(View.GONE);
                choice.setVisibility(View.VISIBLE);
            }
        });

        vehicleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice.setVisibility(View.GONE);
                items.setVisibility(View.GONE);
                player.setVisibility(View.GONE);
                vehicle.setVisibility(View.VISIBLE);
            }
        });




        //floating window setting
        mFloatingView.findViewById(R.id.relativeLayoutParent).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    espView.setVisibility(View.VISIBLE);
                    logoView.setVisibility(View.GONE);
                    return true;
                } else {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = params.x;
                            initialY = params.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;


                        case MotionEvent.ACTION_MOVE:
                            //this code is helping the widget to move around the screen with fingers
                            params.x = initialX + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY + (int) (event.getRawY() - initialTouchY);

                            mWindowManager.updateViewLayout(mFloatingView, params);
                            return true;
                    }
                    return false;
                }
            }
        });




    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    @Override
    public void onClick(View v) {
       /* switch (v.getId()) {
            case R.id.floatLogo:
                //switching views
                espView.setVisibility(View.VISIBLE);
                logoView.setVisibility(View.GONE);
                break;

            case R.id.closeBtn:
                espView.setVisibility(View.GONE);
                logoView.setVisibility(View.VISIBLE);
                break;
        }*/
    }





    private String getType(){
        SharedPreferences sp=this.getSharedPreferences("espValue",Context.MODE_PRIVATE);
        return sp.getString("type","1");
    }
    private void  setValue(String key,boolean b) {
        SharedPreferences sp=this.getSharedPreferences("espValue",Context.MODE_PRIVATE);
        SharedPreferences.Editor ed= sp.edit();
        ed.putBoolean(key,b);
        ed.apply();

    }

    boolean getConfig(String key){
        SharedPreferences sp=this.getSharedPreferences("espValue",Context.MODE_PRIVATE);
        return  sp.getBoolean(key,false);
        // return !key.equals("");
    }
    void setFps(int fps){
        SharedPreferences sp=this.getSharedPreferences("espValue",Context.MODE_PRIVATE);
        SharedPreferences.Editor ed= sp.edit();
        ed.putInt("fps",fps);
        ed.apply();
    }
    int getFps(){
        SharedPreferences sp=this.getSharedPreferences("espValue",Context.MODE_PRIVATE);
        return sp.getInt("fps",100);
    }

    public static void HideFloat() {

        if (Instance != null)
        {
            Instance.Hide();
        }
    }
    public void Hide(){
        stopSelf();
        exit(-1);
        /*Instance = null;
        try {
            mWindowManager.removeView(mFloatingView);
        }catch (Exception e){
            System.out.println("-->"+e);
        }
        try {
        stopSelf();
        }catch (Exception e){
            System.out.println("-->"+e);
        }
            try {
        this.onDestroy();
            }catch (Exception e) {
                System.out.println("-->" + e);
            }*/
    }

    void Init(){
//        final Switch isEnemyWeapon = mFloatingView.findViewById(R.id.isEnemyWeapon);
//        isEnemyWeapon.setChecked(getConfig((String) isEnemyWeapon.getText()));
//        SettingValue(10,getConfig((String) isEnemyWeapon.getText()));
//        isEnemyWeapon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                setValue(String.valueOf(isEnemyWeapon.getText()), isEnemyWeapon.isChecked());
//                SettingValue(10,isEnemyWeapon.isChecked());
//            }
//        });
//        final Switch isGrenadeWarning = mFloatingView.findViewById(R.id.isGrenadeWarning);
//        isGrenadeWarning.setChecked(getConfig((String) isGrenadeWarning.getText()));
//        SettingValue(0,getConfig((String) isGrenadeWarning.getText()));
//        isGrenadeWarning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                setValue(String.valueOf(isGrenadeWarning.getText()), isGrenadeWarning.isChecked());
//                SettingValue(0,isGrenadeWarning.isChecked());
//            }
//        });
        final Switch isSkelton = mFloatingView.findViewById(R.id.isSkelton);
        isSkelton.setChecked(getConfig((String) isSkelton.getText()));
        PremiumValue(614,getConfig((String) isSkelton.getText()));
        isSkelton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isSkelton.getText()), isSkelton.isChecked());
                PremiumValue(614,isSkelton.isChecked());
            }
        });
        final Switch isHead = mFloatingView.findViewById(R.id.isHead);
        isHead.setChecked(getConfig((String) isHead.getText()));
        PremiumValue(6,getConfig((String) isHead.getText()));
        isHead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isHead.getText()), isHead.isChecked());
                PremiumValue(6,isHead.isChecked());
            }
        });
        final Switch isBox = mFloatingView.findViewById(R.id.isBox);
        isBox.setChecked(getConfig((String) isBox.getText()));
        PremiumValue(606,getConfig((String) isBox.getText()));
        isBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isBox.getText()), isBox.isChecked());
                PremiumValue(606,isBox.isChecked());
            }
        });
        final Switch isLine = mFloatingView.findViewById(R.id.isLine);
        isLine.setChecked(getConfig((String) isLine.getText()));
        PremiumValue(605,getConfig((String) isLine.getText()));
        isLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isLine.getText()), isLine.isChecked());
                PremiumValue(605,isLine.isChecked());
            }
        });
        final Switch isBack = mFloatingView.findViewById(R.id.isBack);
        isBack.setChecked(getConfig((String) isBack.getText()));
        PremiumValue(607,getConfig((String) isBack.getText()));
        isBack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isBack.getText()), isBack.isChecked());
                PremiumValue(607,isBack.isChecked());
            }
        });

        final Switch isHealth = mFloatingView.findViewById(R.id.isHealth);
        isHealth.setChecked(getConfig((String) isHealth.getText()));
        PremiumValue(602,getConfig((String) isHealth.getText()));
        isHealth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isHealth.getText()), isHealth.isChecked());
                PremiumValue(602,isHealth.isChecked());
            }
        });

        final Switch isName = mFloatingView.findViewById(R.id.isName);
        isName.setChecked(getConfig((String) isName.getText()));
        PremiumValue(601,getConfig((String) isName.getText()));
        isName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isName.getText()), isName.isChecked());
                PremiumValue(601,isName.isChecked());
            }
        });
        final Switch isDist = mFloatingView.findViewById(R.id.isDist);
        isDist.setChecked(getConfig((String) isDist.getText()));
        PremiumValue(603,getConfig((String) isDist.getText()));
        isDist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isDist.getText()), isDist.isChecked());
                PremiumValue(603,isDist.isChecked());
            }
        });


        addSeekbar(30, 10, new SeekBar.OnSeekBarChangeListener() {
            TextView sizetext = addText("PlayerESP Size: 15");
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int pindex = (progress + 4);
                float psize = (progress + 4.0f);
                String tsize = "PlayerESP Size: " + pindex;
                sizetext.setText(tsize);
                Size(999, psize);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        addSwitch("Item Name", (buttonView, isChecked) -> PremiumValue(609, isChecked));
        addSwitch("Item Distance", (buttonView, isChecked) -> PremiumValue(610, isChecked));
        addSwitch("Vehicles", (buttonView, isChecked) -> PremiumValue(611, isChecked));
        addSwitch("LootBox", (buttonView, isChecked) -> PremiumValue(612, isChecked));
        addSwitch("Airdrop", (buttonView, isChecked) -> PremiumValue(615, isChecked));
        addSwitch("Special Items", (buttonView, isChecked) -> PremiumValue(613, isChecked));

        addSeekbar(30, 10, new SeekBar.OnSeekBarChangeListener() {
            TextView sizetext = addText("ItemsESP Size: 15");
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int pindex = (progress + 4);
                float psize = (progress + 4.0f);
                String tsize = "ItemsESP Size: " + pindex;
                sizetext.setText(tsize);
                Size(1000, psize);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        final int max = 90;
        final int min = 30;
        final int total = max - min;

        final FluidSlider slider = mFloatingView.findViewById(R.id.fps);
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
        //    ESPView.ChangeFps(Integer.parseInt(value));
            //Log.d("slider", value);
            return Unit.INSTANCE;
        });


        slider.setPosition(0.3f);
        slider.setStartText(String.valueOf(min));
        slider.setEndText(String.valueOf(max));
    }

//        final SeekBar fps = mFloatingView.findViewById(R.id.fps);
    // fps.setProgress(getFps());
//        ESPView.ChangeFps(getFps());
//        fps.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                int fpsms=fps.getProgress();
//                setFps(fpsms);
//                ESPView.ChangeFps(fpsms);
//            }
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                //write custom code to on start progress
//            }
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//
//    }

    public static native void PremiumValue(int setting_code, boolean value);

    private void addSwitch(String name, CompoundButton.OnCheckedChangeListener listener) {
        final Switch sw = new Switch(this);
        sw.setText(name);
        sw.setTextSize(15);
        sw.setTextColor(Color.WHITE);
        sw.getThumbDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        sw.setOnClickListener(view -> {
            if (sw.isChecked()) {
                sw.getThumbDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
            } else {
                sw.getThumbDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            }
        });
        sw.setOnCheckedChangeListener(listener);
        sw.setLayoutParams(setParams());
        player.addView(sw);
        addSpace(12);
    }

    private void addSeekbar(int max, int def, final SeekBar.OnSeekBarChangeListener listener) {
        SeekBar sb = new SeekBar(this);
        sb.setMax(max);
        sb.setProgress(def);
        sb.setLayoutParams(setParams());
        sb.setOnSeekBarChangeListener(listener);
        player.addView(sb);
        addSpace(12);
    }
    private LinearLayout.LayoutParams setParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        return params;
    }
    //UI Elements
    private void addSpace(int space) {
        View separator = new View(this);
        LinearLayout.LayoutParams params = setParams();
        params.height = space;
        separator.setLayoutParams(params);
        separator.setBackgroundColor(Color.TRANSPARENT);
        player.addView(separator);
    }
    private TextView addText(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(15);
        tv.setTextColor(Color.WHITE);
        tv.setLayoutParams(setParams());
        player.addView(tv);
        addSpace(15);
        return tv;
    }

    private boolean isTablet() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
        return diagonalInches >= 6.5;
    }



}

