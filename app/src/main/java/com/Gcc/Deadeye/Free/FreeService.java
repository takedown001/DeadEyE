package com.Gcc.Deadeye.Free;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.Gcc.Deadeye.ESPView;
import com.Gcc.Deadeye.R;
import com.Gcc.Deadeye.ShellUtils;
import com.Gcc.Deadeye.imgLoad;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

import java.security.NoSuchAlgorithmException;

import static com.Gcc.Deadeye.Free.FreeOverlay.SettingValue;
import static com.Gcc.Deadeye.GccConfig.urlref.time;

public class FreeService extends Service {

    private WindowManager mWindowManager;
    private View mFloatingView;

    public FreeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private String myDaemon;

    private boolean less=true, ItemESP =true, Vehical =true,aim=true, PlayerEsp =true;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {


        super.onCreate();
        //Inflate the floating view layout we created
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);

        try {
            Check();
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //  Log.d("lol",myDaemon);
        CircleMenu circleMenu = mFloatingView.findViewById(R.id.circle_menu);

        circleMenu.setMainMenu(Color.parseColor("#ee4f08"), R.mipmap.safeicon, R.drawable.closeij)
                .addSubMenu(Color.parseColor("#258CFF"), R.drawable.player)//playrt
                .addSubMenu(Color.parseColor("#30A400"), R.drawable.itemimg)//item
                .addSubMenu(Color.parseColor("#FF4B32"), R.drawable.vehicalimg)//vehical
                .addSubMenu(Color.parseColor("#8A39FF"), R.drawable.gun)
                .addSubMenu(Color.parseColor("#FFFFFF"), R.drawable.aimbot)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {

                    @Override
                    public void onMenuSelected(int index) {
                        if(index==0) {
                            if (PlayerEsp) {
                               SettingValue(86,true);
                               SettingValue(87,true);
                               SettingValue(88,true);
                               SettingValue(91,true);
                               SettingValue(93,true);
                               SettingValue(99,true);

                               Toast.makeText(getApplicationContext(), "Player ESP Activated ", Toast.LENGTH_SHORT).show();
                                PlayerEsp =false;
                            } else {
                                SettingValue(86,false);
                                SettingValue(87,false);
                                SettingValue(88,false);
                                SettingValue(91,false);
                                SettingValue(93,false);
                                SettingValue(99,false);
                                Toast.makeText(getApplicationContext(), "Player ESP Deactivated ", Toast.LENGTH_SHORT).show();
                                PlayerEsp =true;
                            }
                        }
                        else if (index == 1) {
                            if (ItemESP) {
                                SettingValue(92,true);
                                SettingValue(95,true);
                                SettingValue(97,true);
                                Toast.makeText(getApplicationContext(), "Item Esp Activated " , Toast.LENGTH_SHORT).show();
                                ItemESP =false;
                            } else {
                                SettingValue(92,false);
                                SettingValue(95,false);
                                SettingValue(97,false);
                                Toast.makeText(getApplicationContext(), "Item Esp DeActivated " , Toast.LENGTH_SHORT).show();
                                ItemESP =true;
                            }
                        }
                        else if (index == 2) {
                            if (Vehical) {
                                SettingValue(96,true);
                                Toast.makeText(getApplicationContext(), "Vehical Esp Activated "  , Toast.LENGTH_SHORT).show();
                                Vehical =false;

                            } else {
                                SettingValue(96,false);
                                Toast.makeText(getApplicationContext(), "Vehical Esp Deactivated " , Toast.LENGTH_SHORT).show();
                                Vehical =true;
                            }
                        }
                        else if (index == 3) {
                            if (less) {
                                ShellUtils.SU(myDaemon + " LESSCHALU");
                                Toast.makeText(getApplicationContext(), "Recoil Compansation Activated " , Toast.LENGTH_SHORT).show();
                                less=false;
                            } else {
                                ShellUtils.SU(myDaemon + " LESSBAND");
                                Toast.makeText(getApplicationContext(), "Recoil Compansation Deactivated ", Toast.LENGTH_SHORT).show();
                                less=true;
                            }
                        }
                        else if (index == 4) {
                            if (aim) {
                                ShellUtils.SU(myDaemon + "AIMCHALU");
                               SettingValue(101,true);
                                Toast.makeText(getApplicationContext(), "Fov Aimbot Activated " , Toast.LENGTH_SHORT).show();
                                aim=false;

                            } else {
                                SettingValue(101,false);
                                ShellUtils.SU(myDaemon + " AIMBAND");
                                Toast.makeText(getApplicationContext(), "Fov Aimbot Deactivated", Toast.LENGTH_SHORT).show();
                                aim=true;


                            }
                        }

                    }

                }).setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {

            @Override
            public void onMenuOpened() {}

            @Override
            public void onMenuClosed() {}

        });

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        //Add the view to the window.
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);


        //Drag and move floating view using user's touch action.
        mFloatingView.findViewById(R.id.circle_menu).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;


                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private  void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if(imgLoad.Load(getApplicationContext()).equals(time)){
          stopSelf();
        }
    }

}