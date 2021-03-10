package com.Gcc.Deadeye;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.topjohnwu.superuser.Shell;

import java.security.NoSuchAlgorithmException;

import static com.Gcc.Deadeye.FloatLogo.SettingValue;
import static com.Gcc.Deadeye.GccConfig.urlref.canary;
import static com.Gcc.Deadeye.GccConfig.urlref.netgaurd;
import static com.Gcc.Deadeye.GccConfig.urlref.pcanary;
import static com.Gcc.Deadeye.GccConfig.urlref.time;

public class Overlay extends Service {
    public static boolean isRunning = false;

    private WindowManager windowManager;
    private View mFloatingView;
    private ESPView overlayView;
    static Context ctx;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        super.onCreate();

        ctx=this;
//           Log.d("test", String.valueOf(MainActivity.gameType));
//           Log.d("test", String.valueOf(MainActivity.is32));
        if (MainActivity.gameType == 1 && MainActivity.is32) {
            Start(ctx,1,1);

        }
//        else if (MainActivity.gameType == 1 && MainActivity.is64) {
//            Start(ctx,1,2);
//        }
        else if (MainActivity.gameType == 2 && MainActivity.is32) {
            Start(ctx,2,1);
        }
//        else if (MainActivity.gameType == 2 && MainActivity.is64) {
//            Start(ctx,2,2);
//        }
        else if (MainActivity.gameType == 3 && MainActivity.is32) {
            Start(ctx,3,1);
        }
//        else if (MainActivity.gameType == 3 && MainActivity.is64) {
//            Start(ctx,3,2);
//        }
        else if (MainActivity.gameType == 4 && MainActivity.is32) {
            Start(ctx,4,1);
        }
//        else if (MainActivity.gameType == 4 && MainActivity.is64) {
//            Start(ctx,4,2);
//        }

        windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        overlayView = new ESPView(ctx);
        try {
            Check();
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        DrawCanvas();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private  void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if(imgLoad.Load(ctx).equals(time)){
           stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        Stop();
        isRunning = false;
        if (mFloatingView != null) {
            windowManager.removeView(mFloatingView);
            mFloatingView = null;
        }
        if (overlayView != null) {
            windowManager.removeView(overlayView);
            overlayView = null;
        }
        super.onDestroy();
    }

    private void DrawCanvas() {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                getLayoutType(),
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        windowManager.addView(overlayView, params);
    }

    private void Start(Context ctx,int game ,int bit) {



        new Thread(() -> {
            if (!isRunning) {
                /*
                 * PUG Global = 1
                 * PUG KR = 2
                 * PUG VNG = 3
                 * PUG Taiwan = 4
                 */
                startDaemon(game);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Init() < 0) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(Overlay.this, "Game Not Running!", Toast.LENGTH_SHORT).show();
                        Overlay.this.stopSelf();
                    });
                } else {
                    isRunning = true;
                    startService(new Intent(this, FloatLogo.class));
                    SettingValue(0,true);
                    SettingValue(8,true);
                }
            }
        }).start();
    }


    private void startDaemon(int mode){
        new Thread(() -> {
            String cmd = getFilesDir() + "/xvpn " + mode;
                    Log.d("log",cmd);
            if(Shell.rootAccess()){
                Shell.su(cmd).submit();
            } else {
                Shell.sh(cmd).submit();
            }
        }).start();
    }


//    private int getProcessID(String pkg) {
//        int pid = -1;
//        if (Shell.rootAccess()) {
//            String cmd = "for p in /proc/[0-9]*; do [[ $(<$p/cmdline) = " + pkg + " ]] && echo ${p##*/}; done";
//            List<String> outs = new ArrayList<>();
//            Shell.su(cmd).to(outs).exec();
//            if (outs.size() > 0) {
//                pid = Integer.parseInt(outs.get(0));
//            }
//        } else {
//            Shell.Result out = Shell.sh("/system/bin/ps -A | grep \"" + pkg + "\"").exec();
//            List<String> output = out.getOut();
//            Log.d("PTEST", "TEST: " + Arrays.toString(output.toArray()));
//            if (output.isEmpty() || output.get(0).contains("bad pid")) {
//                out = Shell.sh("/system/bin/ps | grep \"" + pkg + "\"").exec();
//                output = out.getOut();
//                if (!output.isEmpty() && !output.get(0).contains("bad pid")) {
//                    for (int i = 0; i < output.size(); i++) {
//                        String[] results = output.get(i).trim().replaceAll("( )+", ",").replaceAll("(\n)+", ",").split(",");
//                        if (results[8].equals(pkg)) {
//                            pid = Integer.parseInt(results[1]);
//                        }
//                    }
//                }
//            } else {
//                for (int i = 0; i < output.size(); i++) {
//                    String[] results = output.get(i).trim().replaceAll("( )+", ",").replaceAll("(\n)+", ",").split(",");
//                    for (int j = 0; j < results.length; j++) {
//                        if (results[j].equals(pkg)) {
//                            pid = Integer.parseInt(results[j - 7]);
//                        }
//                    }
//                }
//            }
//        }
//        return pid;
//    }

    //Native Funcs
    public static native int Init();

    public static native void DrawOn(ESPView espView, Canvas canvas);


    public static native void Size(int num, float size);

    public static native void Stop();


    private boolean isTablet() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
        return diagonalInches >= 6.5;
    }


    private int getLayoutType() {
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        return LAYOUT_FLAG;
    }

    private int getResID(String name, String type) {
        return getResources().getIdentifier(name, type, getPackageName());
    }

    private int getID(String name) {
        return getResID(name, "id");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
