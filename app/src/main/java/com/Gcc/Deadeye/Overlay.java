package com.Gcc.Deadeye;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.DisplayMetrics;
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

import com.topjohnwu.superuser.Shell;

import static com.Gcc.Deadeye.FloatLogo.SettingValue;

public class Overlay extends Service {
    public static boolean isRunning = false;

    private WindowManager windowManager;
    private View mFloatingView;
    private LinearLayout patches;
    private TextView initText;
    private ESPView overlayView;
    Context ctx;
    @Override
    public void onCreate() {
        super.onCreate();

        ctx=this;
//           Log.d("test", String.valueOf(MainActivity.gameType));
//           Log.d("test", String.valueOf(MainActivity.is32));
        if (MainActivity.gameType == 1 && MainActivity.is32) {
            Start(ctx,1,1);

        }
        else if (MainActivity.gameType == 1 && MainActivity.is64) {
            Start(ctx,1,2);
        }
        else if (MainActivity.gameType == 2 && MainActivity.is32) {
            Start(ctx,2,1);
        }
        else if (MainActivity.gameType == 2 && MainActivity.is64) {
            Start(ctx,2,2);
        }
        else if (MainActivity.gameType == 3 && MainActivity.is32) {
            Start(ctx,3,1);
        }
        else if (MainActivity.gameType == 3 && MainActivity.is64) {
            Start(ctx,3,2);
        }
        else if (MainActivity.gameType == 4 && MainActivity.is32) {
            Start(ctx,4,1);
        }
        else if (MainActivity.gameType == 4 && MainActivity.is64) {
            Start(ctx,4,2);
        }
        windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        overlayView = new ESPView(ctx);
        DrawCanvas();

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
                    Thread.sleep(1300);
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
                }
            }
        }).start();
    }


    private void startDaemon(int mode){
        new Thread(() -> {
            String cmd = getFilesDir() + "/xcode " + mode;
            //        Log.d("log",cmd);
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

    //UI Elements
    private void addSpace(int space) {
        View separator = new View(this);
        LinearLayout.LayoutParams params = setParams();
        params.height = space;
        separator.setLayoutParams(params);
        separator.setBackgroundColor(Color.TRANSPARENT);
        patches.addView(separator);
    }

    private void addSwitch(String name, CompoundButton.OnCheckedChangeListener listener) {
        final Switch sw = new Switch(this);
        sw.setText(name);
        sw.setTextSize(dipToPixels());
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
        patches.addView(sw);
        addSpace(12);
    }

    private void addSeekbar(int max, int def, final SeekBar.OnSeekBarChangeListener listener) {
        SeekBar sb = new SeekBar(this);
        sb.setMax(max);
        sb.setProgress(def);
        sb.setLayoutParams(setParams());
        sb.setOnSeekBarChangeListener(listener);
        patches.addView(sb);
        addSpace(12);
    }

    private TextView addText(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(getBestTextSize());
        tv.setTextColor(Color.WHITE);
        tv.setLayoutParams(setParams());
        patches.addView(tv);
        addSpace(12);
        return tv;
    }

    private LinearLayout.LayoutParams setParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        return params;
    }

    private boolean isTablet() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
        return diagonalInches >= 6.5;
    }

    private float getBestTextSize() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float d = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, metrics);
        if (isTablet())
            d += 7.f;
        return (d > 20 && !isTablet()) ? 20 : d;
    }

    private float dipToPixels() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, metrics);
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
