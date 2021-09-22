package mobisocial.arcade;

import static mobisocial.arcade.FloatLogo.myDaemon;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import mobisocial.arcade.GccConfig.urlref;

public class flogo extends Service{

    private WindowManager windowManager;
    private static flogo Instance;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
   private  int Chech=0;
    private View mFloatingView;
    private ImageView flash;
    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;
        createOver();

    }



    @SuppressLint("InflateParams")

    void createOver() {
        //getting the widget layout from xml using layout inflater
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.flogoservice, null);

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
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(mFloatingView, params);

       flash = mFloatingView.findViewById(R.id.flash);
        final GestureDetector gestureDetector = new GestureDetector(this, new SingleTapConfirm());
        gestureDetector.setIsLongpressEnabled(true);
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
                    gestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {
                            Run();
                            return false;
                        }

                        @Override
                        public boolean onDoubleTap(MotionEvent e) {
                            BRun();
                            return false;
                        }

                        @Override
                        public boolean onDoubleTapEvent(MotionEvent e) {
                            return false;
                        }
                    });

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
                            windowManager.updateViewLayout(mFloatingView, params);
                            return true;
                    }
                    return false;
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        if (mFloatingView != null) {
            windowManager.removeView(mFloatingView);
            mFloatingView = null;
        }
        stopSelf();
        super.onDestroy();
    }

    public void Run(){

    switch (Chech){
        case 0:
            flash.setImageDrawable(getResources().getDrawable((R.drawable.flash)));
            ShellUtils.SU(myDaemon+" 200020");
            ShellUtils.SU(myDaemon+" 200017");
            Toast.makeText(Instance," Player Speed Increased To 20M/s",Toast.LENGTH_SHORT).show();
            Chech=1;
            break;
        case 1:
            flash.setImageDrawable(getResources().getDrawable((R.drawable.flashstart)));
            ShellUtils.SU(myDaemon+" 200019");
            ShellUtils.SU(myDaemon+" 200020");
            Toast.makeText(Instance,"Player Speed Restored To Normal",Toast.LENGTH_SHORT).show();
            Chech =0;
            break;
    }
}

    public void BRun(){

        switch (Chech){
            case 0:
                flash.setImageDrawable(getResources().getDrawable((R.drawable.stopflash)));
                ShellUtils.SU(myDaemon+" 200018");
                ShellUtils.SU(myDaemon+" 200019");
                Toast.makeText(Instance,"Player Speed Increased To 60M/s",Toast.LENGTH_SHORT).show();
                Chech=1;
                break;
            case 1:
                flash.setImageDrawable(getResources().getDrawable((R.drawable.flashstart)));
                ShellUtils.SU(myDaemon+" 200020");
                ShellUtils.SU(myDaemon+" 200019");
                Toast.makeText(Instance,"Player Speed Restored To Normal",Toast.LENGTH_SHORT).show();
                Chech =0;
                break;
        }
    }

}
