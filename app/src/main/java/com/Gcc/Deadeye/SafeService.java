package com.Gcc.Deadeye;

import android.app.Service;
import android.content.Intent;
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

import com.Gcc.Deadeye.GccConfig.urlref;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

public class SafeService extends Service {

    private WindowManager mWindowManager;
    private View mFloatingView;

    public SafeService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private String myDaemon = "./"+ urlref.pathoflib+ urlref.nameoflib;
        private boolean less=true,head=true,cross=true,aim=true,magic=true;
    private String Arrayname[] = {"Less Recoil","AimBot","CrossHair","Magic Bullet","HeadShot"};
    @Override
    public void onCreate() {


        super.onCreate();
        //Inflate the floating view layout we created
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);


        CircleMenu circleMenu = mFloatingView.findViewById(R.id.circle_menu);

        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.drawable.shield, R.drawable.closeij)
                .addSubMenu(Color.parseColor("#258CFF"), R.drawable.gun)
                .addSubMenu(Color.parseColor("#30A400"), R.drawable.aimbot)
                .addSubMenu(Color.parseColor("#FF4B32"), R.drawable.crosshair)
                .addSubMenu(Color.parseColor("#8A39FF"), R.drawable.bullet)
                .addSubMenu(Color.parseColor("#FFFFFF"), R.drawable.headshot)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {

                    @Override
                    public void onMenuSelected(int index) {
                        if(index==0) {
                            if (less) {
                                ShellUtils.SU(myDaemon + " 1");
                                Toast.makeText(getApplicationContext(), "Activated " + Arrayname[index], Toast.LENGTH_SHORT).show();
                                less=false;
                            } else {
                               ShellUtils.SU(myDaemon + " 2");
                                Toast.makeText(getApplicationContext(), "Dectivated " + Arrayname[index], Toast.LENGTH_SHORT).show();
                                less=true;
                            }
                        }
                           else if (index == 1) {
                                if (aim) {
                                    ShellUtils.SU(myDaemon + " 3");
                                    Toast.makeText(getApplicationContext(), "Activated " + Arrayname[index], Toast.LENGTH_SHORT).show();
                                    aim=false;
                                } else {
                                    ShellUtils.SU(myDaemon + " 4");
                                    Toast.makeText(getApplicationContext(), "Dectivated " + Arrayname[index], Toast.LENGTH_SHORT).show();
                                    aim=true;
                                }
                            }
                        else if (index == 2) {
                            if (cross) {
                                ShellUtils.SU(myDaemon + " 5");
                                Toast.makeText(getApplicationContext(), "Activated " + Arrayname[index], Toast.LENGTH_SHORT).show();
                                cross=false;
                            } else {
                                ShellUtils.SU(myDaemon + " 6");
                                Toast.makeText(getApplicationContext(), "Dectivated " + Arrayname[index], Toast.LENGTH_SHORT).show();
                                cross=true;
                            }
                        }
                        else if (index == 3) {
                            if (magic) {
                                ShellUtils.SU(myDaemon + " 7");
                                Toast.makeText(getApplicationContext(), "Activated " + Arrayname[index], Toast.LENGTH_SHORT).show();
                                magic=false;
                            } else {
                                ShellUtils.SU(myDaemon + " 8");
                                Toast.makeText(getApplicationContext(), "Dectivated " + Arrayname[index], Toast.LENGTH_SHORT).show();
                                magic=true;
                            }
                        }
                        else if (index == 4) {
                            if (head) {
                               ShellUtils.SU(myDaemon + " 8");
                                Toast.makeText(getApplicationContext(), "Activated " + Arrayname[index], Toast.LENGTH_SHORT).show();
                                head=false;
                            } else {
                               ShellUtils.SU(myDaemon + " 9");
                                Toast.makeText(getApplicationContext(), "Dectivated " + Arrayname[index], Toast.LENGTH_SHORT).show();
                                head=true;
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


}