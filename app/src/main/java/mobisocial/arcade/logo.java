package mobisocial.arcade;

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

import com.google.android.gms.common.internal.Objects;

import mobisocial.arcade.GccConfig.urlref;

public class logo  extends Service{

    private WindowManager windowManager;
    private static  logo Instance;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private  int Chech=0;
    private String myDaemon;
    private View mFloatingView;
    private ImageView carfly;
    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;
        myDaemon =  "."+Instance.getFilesDir().toString()+ urlref.SafeMem;
        createOver();

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
    @SuppressLint("InflateParams")

    void createOver() {
        //getting the widget layout from xml using layout inflater
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.logoservice, null);

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

             carfly = mFloatingView.findViewById(R.id.carfly);

           final GestureDetector gestureDetector = new GestureDetector(this, new SingleTapConfirm());

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
                    Run();
                    return true;
                }
                else {
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
public void Run(){

    switch (Chech){
        case 0:
            carfly.setImageDrawable(getResources().getDrawable((R.drawable.carflyon)));
            ShellUtils.SU(myDaemon+" 200021");
            Toast.makeText(Instance,"Car Fly Activated",Toast.LENGTH_SHORT).show();
            Chech=1;
            break;

        case 1:
            carfly.setImageDrawable(getResources().getDrawable(R.drawable.carlyoff));
            ShellUtils.SU(myDaemon+" 200022");
            Toast.makeText(Instance,"Car Fly Deactivated",Toast.LENGTH_SHORT).show();
            Chech =0;
            break;
    }
}

}
