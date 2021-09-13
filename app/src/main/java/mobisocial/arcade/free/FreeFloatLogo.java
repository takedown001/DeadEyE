package mobisocial.arcade.free;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.topjohnwu.superuser.Shell;

import java.io.IOException;
import java.nio.channels.ShutdownChannelGroupException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import mobisocial.arcade.AESUtils;
import mobisocial.arcade.ESPView;
import mobisocial.arcade.GccConfig.urlref;
import mobisocial.arcade.Helper;
import mobisocial.arcade.JavaUrlConnectionReader;
import mobisocial.arcade.LoginActivity;
import mobisocial.arcade.R;
import mobisocial.arcade.ShellUtils;
import mobisocial.arcade.imgLoad;

import static java.lang.System.exit;
import static java.lang.System.in;
import static mobisocial.arcade.GccConfig.urlref.FreeHexMemArg;
import static mobisocial.arcade.GccConfig.urlref.defaltversion;
import static mobisocial.arcade.GccConfig.urlref.time;
import static mobisocial.arcade.Helper.givenToFile;

public class FreeFloatLogo extends Service implements View.OnClickListener {


    private View mFloatingView;
    @SuppressLint("StaticFieldLeak")
    private static FreeFloatLogo Instance;
    private final JavaUrlConnectionReader reader = new JavaUrlConnectionReader();
    public FreeFloatLogo() {
    }
    LinearLayout player ;
    public static boolean isRunning = false;
    private WindowManager windowManager;
    private ESPView overlayView;
    private String myDaemon,hexDaemon;

    static{
        System.loadLibrary("sysload");
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
    private String version, deviceid,gameName;
    View espView,logoView;
    private int Gametype;
    private static final String TAG_DEVICEID = urlref.TAG_DEVICEID;
    private static final String TAG_VERSION = "v";
    String CheatB = urlref.Betaserver + "cheat.php";
    private String data;
    @SuppressLint("CutPasteId")
    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;
        SharedPreferences shred =getSharedPreferences("Freeuserdetails", MODE_PRIVATE);
        version = shred.getString("version", defaltversion);
        version = AESUtils.DarKnight.getEncrypted(version);
        deviceid = AESUtils.DarKnight.getEncrypted(Helper.getDeviceId(getApplicationContext()));
        windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        overlayView = new ESPView(Instance);
        SharedPreferences ga = Instance.getSharedPreferences("game", MODE_PRIVATE);
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    try {
                        Check();
                        ShellUtils.SU("setenforce 0");
                        gamerun();
//                        if (gameName.equals("com.pubg.imobile")) {
//                            hexDaemon = "." + Instance.getFilesDir().toString() + urlref.HexMem;
//                            myDaemon = "." + Instance.getFilesDir().toString() + urlref.LiteMem;
//                        }else{
                            hexDaemon = "." + Instance.getFilesDir().toString() + urlref.FreeHexMem;
                            myDaemon = "." + Instance.getFilesDir().toString() + urlref.FreeMem;
                    //    }
                        ShellUtils.SU("chmod 777 "+ Instance.getFilesDir().toString()+urlref.FreeHexMem);
                        ShellUtils.SU("chmod 777 "+ Instance.getFilesDir().toString()+ urlref.FreeMem);

                    } catch (IOException | InterruptedException | NoSuchAlgorithmException | PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                });
            }
        }).start();

    }
    private int getLayoutType() {
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        return LAYOUT_FLAG;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private  void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if(imgLoad.Load(Instance).equals(urlref.time)){
            stopSelf();
        }
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
    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        gameName = intent.getStringExtra("gamename");
        Gametype = intent.getIntExtra("gametype",1);
        return START_NOT_STICKY;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void gamerun() throws IOException, InterruptedException {
        boolean finalCheck = Helper.checkmd5(Instance, gameName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (Gametype == 1) {

                        if (finalCheck) {
                            Toast.makeText(Instance, "Daemon Load Successfully", Toast.LENGTH_LONG).show();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Start(Instance, 1, 1);
                                }
                            }).start();
                        } else {
                            Toast.makeText(Instance, "Daemon Server Error", Toast.LENGTH_LONG).show();
                            isRunning = false;
                        }
                        //  ShellUtils.SU("rm -rf /data/data/mobisocial.arcade/files/.*");
                    }

//        else if (MainActivity.gameType == 1 && MainActivity.is64) {
//            Start(ctx,1,2);
//        }
                    else if (Gametype == 2) {
                        if (finalCheck) {

                            Toast.makeText(Instance, "Daemon Load Successfully", Toast.LENGTH_LONG).show();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Start(Instance, 2, 1);
                                }
                            }).start();
                        } else {
                            isRunning = false;
                            Toast.makeText(Instance, "Daemon Server Error", Toast.LENGTH_LONG).show();
                        }
                    }

//        else if (MainActivity.gameType == 2 && MainActivity.is64) {
//            Start(ctx,2,2);
//        }
                    else if (Gametype == 3) {
                        if (finalCheck) {
                            Toast.makeText(Instance, "Daemon Load Successfully", Toast.LENGTH_LONG).show();
                            Start(Instance, 3, 1);
                        } else {
                            isRunning = false;
                            Toast.makeText(Instance, "Daemon Server Error", Toast.LENGTH_LONG).show();
                        }
                    }

//        else if (MainActivity.gameType == 3 && MainActivity.is64) {
//            Start(ctx,3,2);
//        }
                    else if (Gametype == 4) {
                        if (finalCheck) {
                            Toast.makeText(Instance, "Daemon Load Successfully", Toast.LENGTH_LONG).show();
                            Start(Instance, 4, 1);

                        } else {
                            stopSelf();
                            Toast.makeText(Instance, "Daemon Server Error", Toast.LENGTH_LONG).show();
                            isRunning = false;

                        }
                    }
                    else if (Gametype == 5) {
                        if (finalCheck) {
                            Toast.makeText(Instance, "Daemon Load Successfully", Toast.LENGTH_LONG).show();
                            Start(Instance, 5, 1);

                        } else {
                            isRunning = false;
                            Toast.makeText(Instance, "Daemon Server Error", Toast.LENGTH_LONG).show();
                        }
                    }
                                     //        else if (MainActivity.gameType == 4 && MainActivity.is64) {
//            Start(ctx,4,2);
//        }
                });

            }
        }).start();

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
                //    Log.d("game", String.valueOf(game));
                startDaemon(game);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (Init() < 0) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(Instance, "Game Not Detected!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(Instance, "Rescanning...", Toast.LENGTH_SHORT).show();
                        Rescan(ctx,game,bit);
                    });
                    //     Rescan(ctx,game,bit);
                } else {
                    isRunning = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                PremiumValue(608,true);
                                PremiumValue(599, true);
                                DrawCanvas();
                                ShellUtils.SU(hexDaemon + urlref.HexMemArg);
                                createOver();
                                CInit();
                            });
                        }
                    }).start();

                }
            }
        }).start();
    }
    private void startDaemon(int mode){
        new Thread(() -> {
            String cmd = getFilesDir() + "/sysexe " + mode;
            //    Log.d("test", String.valueOf(mode));
            if(!Shell.rootAccess()){
                Shell.sh(cmd).submit();
            } else {
                ShellUtils.SU("setenforce 0");
                ShellUtils.SU(cmd);

            }
        }).start();
    }
    private void Rescan(Context ctx,int game ,int bit) {

        new Thread(() -> {
            if (!isRunning) {
                /*
                 * PUG Global = 1
                 * PUG KR = 2
                 * PUG VNG = 3
                 * PUG Taiwan = 4
                 */
                //       Log.d("game", String.valueOf(game));
                startDaemon(game);
                try {
                    Thread.sleep(6500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (Init() < 0) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(Instance, "Have A Patience...", Toast.LENGTH_SHORT).show();
                        Rescan2(ctx,game,bit);
                    });
                } else {
                    isRunning = true;
                    new Handler(Looper.getMainLooper()).post(() -> {
                        PremiumValue(608,true);
                        PremiumValue(599, true);
                        DrawCanvas();
                        ShellUtils.SU(hexDaemon + urlref.HexMemArg);
                        createOver();
                        CInit();
                    });
                }
            }
        }).start();
    }
    private void Rescan2(Context ctx,int game ,int bit) {

        new Thread(() -> {
            if (!isRunning) {
                /*
                 * PUG Global = 1
                 * PUG KR = 2
                 * PUG VNG = 3
                 * PUG Taiwan = 4
                 */
                //            Log.d("game", String.valueOf(game));
                startDaemon(game);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (Init() < 0) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(Instance, "Game Not Detected!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(Instance, "Service Stopped", Toast.LENGTH_SHORT).show();
                        stopSelf();
                    });
                } else {
                    isRunning = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                PremiumValue(608,true);
                                PremiumValue(599, true);
                                DrawCanvas();
                                ShellUtils.SU(hexDaemon + urlref.HexMemArg);
                                createOver();
                                CInit();

                            });
                        }
                    }).start();

                }
            }
        }).start();
    }


    @SuppressLint("InflateParams")
    void createOver(){
        //getting the widget layout from xml using layout inflater
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.freefloatlogo, null);

        player =mFloatingView.findViewById(R.id.players);
        logoView = mFloatingView.findViewById(R.id.relativeLayoutParent);
        espView = mFloatingView.findViewById(R.id.espView);

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


        final GestureDetector gestureDetector = new GestureDetector(this, new SingleTapConfirm());
        //window function
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
      Stop();
      stopSelf();
      isRunning = false;
        if(overlayView != null){
            windowManager.removeView(overlayView);
            overlayView = null;
        }
        if (mFloatingView != null) {
            windowManager.removeView(mFloatingView);
            mFloatingView = null;
        }
        super.onDestroy();
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

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public void ipstartcheat(){
        class load extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //    Log.d("data",data);
                new Thread(() -> {
                    new Handler(Looper.getMainLooper()).post(() -> {
                    //    ShellUtils.SU("rm -rf" + Instance.getFilesDir().toString() + "/scheat.sh");
                        try {
                            givenToFile(Instance, s);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }).start();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(TAG_VERSION,version);
                params.put(TAG_DEVICEID,deviceid);
                params.put("g",AESUtils.DarKnight.getEncrypted("ip"));
                params.put("s",AESUtils.DarKnight.getEncrypted("start"));
                data =AESUtils.DarKnight.getDecrypted(reader.getUrlContents(CheatB,params));
                return data;
            }
        }
        new load().execute();

    }

    public void ipstopcheat(){
        class load extends AsyncTask<Void, Void, String> {
            @Override

            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //    Log.d("data",data);
                new Thread(() -> {
                    new Handler(Looper.getMainLooper()).post(() -> {
                //        ShellUtils.SU("rm -rf" + Instance.getFilesDir().toString() + "/scheat.sh");
                        try {
                            givenToFile(Instance, s);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }).start();
            }
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(TAG_VERSION,version);
                params.put(TAG_DEVICEID,deviceid);
                params.put("g",AESUtils.DarKnight.getEncrypted("ip"));
                params.put("s",AESUtils.DarKnight.getEncrypted("stop"));
                data =AESUtils.DarKnight.getDecrypted(reader.getUrlContents(CheatB,params));
                return data;
            }
        }
        new load().execute();
    }


    void CInit() {
        //vehicals

        final CheckBox Buggy = mFloatingView.findViewById(R.id.Buggy);
        Buggy.setChecked(getConfig((String) Buggy.getText()));
        PremiumVehicalValue(20,Buggy.isChecked());
        Buggy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Buggy.getText()), Buggy.isChecked());
                PremiumVehicalValue(20,Buggy.isChecked());
            }
        });
        final CheckBox UAZ = mFloatingView.findViewById(R.id.UAZ);
        UAZ.setChecked(getConfig((String) UAZ.getText()));
        PremiumVehicalValue(21,UAZ.isChecked());
        UAZ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(UAZ.getText()), UAZ.isChecked());
                PremiumVehicalValue(21,UAZ.isChecked());
            }
        });
        final CheckBox Trike = mFloatingView.findViewById(R.id.Trike);
        Trike.setChecked(getConfig((String) Trike.getText()));
        PremiumVehicalValue(31,Trike.isChecked());
        Trike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Trike.getText()), Trike.isChecked());
                PremiumVehicalValue(31,Trike.isChecked());
            }
        });
        final CheckBox Bike = mFloatingView.findViewById(R.id.Bike);
        Bike.setChecked(getConfig((String) Bike.getText()));
        PremiumVehicalValue(25,Bike.isChecked());
        Bike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Bike.getText()), Bike.isChecked());
                PremiumVehicalValue(25,Bike.isChecked());
            }
        });
        final CheckBox Dacia = mFloatingView.findViewById(R.id.Dacia);
        Dacia.setChecked(getConfig((String) Dacia.getText()));
        PremiumVehicalValue(22,Dacia.isChecked());
        Dacia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Dacia.getText()), Dacia.isChecked());
                PremiumVehicalValue(22,Dacia.isChecked());
            }
        });
        final CheckBox Jet = mFloatingView.findViewById(R.id.Jet);
        PremiumVehicalValue(27,Jet.isChecked());
        Jet.setChecked(getConfig((String) Jet.getText()));
        Jet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Jet.getText()), Jet.isChecked());
                PremiumVehicalValue(27,Jet.isChecked());
            }
        });
        final CheckBox Boat = mFloatingView.findViewById(R.id.Boat);
        Boat.setChecked(getConfig((String) Boat.getText()));
        PremiumVehicalValue(26,Boat.isChecked());
        Boat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Boat.getText()), Boat.isChecked());
                PremiumVehicalValue(26,Boat.isChecked());

            }
        });
        final CheckBox Scooter = mFloatingView.findViewById(R.id.Scooter);
        Scooter.setChecked(getConfig((String) Scooter.getText()));
        PremiumVehicalValue(30,Scooter.isChecked());
        Scooter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Scooter.getText()), Scooter.isChecked());
                PremiumVehicalValue(30,Scooter.isChecked());
            }
        });
        final CheckBox Bus = mFloatingView.findViewById(R.id.Bus);
        Bus.setChecked(getConfig((String) Bus.getText()));
        PremiumVehicalValue(28,Bus.isChecked());
        Bus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Bus.getText()), Bus.isChecked());
                PremiumVehicalValue(28,Bus.isChecked());}
        });
        final CheckBox Mirado = mFloatingView.findViewById(R.id.Mirado);
        Mirado.setChecked(getConfig((String) Mirado.getText()));
        PremiumVehicalValue(34,Mirado.isChecked());
        Mirado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Mirado.getText()), Mirado.isChecked());
                PremiumVehicalValue(34,Mirado.isChecked());
            }
        });


        final CheckBox Rony = mFloatingView.findViewById(R.id.Rony);
        Rony.setChecked(getConfig((String) Rony.getText()));
        PremiumVehicalValue(24,Rony.isChecked());
        Rony.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Rony.getText()), Rony.isChecked());
                PremiumVehicalValue(24,Rony.isChecked());
            }
        });
        final CheckBox Snowbike = mFloatingView.findViewById(R.id.Snowbike);
        Snowbike.setChecked(getConfig((String) Snowbike.getText()));
        PremiumVehicalValue(36,Snowbike.isChecked());
        Snowbike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Snowbike.getText()), Snowbike.isChecked());
                PremiumVehicalValue(36,Snowbike.isChecked());
            }
        });
        final CheckBox Snowmobile = mFloatingView.findViewById(R.id.Snowmobile);
        Snowmobile.setChecked(getConfig((String) Snowmobile.getText()));
        PremiumVehicalValue(36,Snowmobile.isChecked());
        Snowmobile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Snowmobile.getText()), Snowmobile.isChecked());
                PremiumVehicalValue(36,Snowmobile.isChecked());

            }
        });
        final CheckBox Tempo = mFloatingView.findViewById(R.id.Tempo);
        Tempo.setChecked(getConfig((String) Tempo.getText()));
        PremiumVehicalValue(23,Tempo.isChecked());
        Tempo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Tempo.getText()), Tempo.isChecked());
                PremiumVehicalValue(23,Tempo.isChecked());
            }
        });
        final CheckBox Truck = mFloatingView.findViewById(R.id.Truck);
        Truck.setChecked(getConfig((String) Truck.getText()));
        PremiumVehicalValue(37,Truck.isChecked());
        Truck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Truck.getText()), Truck.isChecked());
                PremiumVehicalValue(37,Truck.isChecked());
            }
        });
        final CheckBox MonsterTruck = mFloatingView.findViewById(R.id.MonsterTruck);
        MonsterTruck.setChecked(getConfig((String) MonsterTruck.getText()));
        PremiumVehicalValue(29,MonsterTruck.isChecked());
        MonsterTruck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(MonsterTruck.getText()), MonsterTruck.isChecked());
                PremiumVehicalValue(29,MonsterTruck.isChecked());
            }
        });
        final CheckBox BRDM = mFloatingView.findViewById(R.id.BRDM);
        BRDM.setChecked(getConfig((String) BRDM.getText()));
        PremiumVehicalValue(33,BRDM.isChecked());
        BRDM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(BRDM.getText()), BRDM.isChecked());
                PremiumVehicalValue(33,BRDM.isChecked());
            }
        });
        final CheckBox LadaNiva = mFloatingView.findViewById(R.id.LadaNiva);
        LadaNiva.setChecked(getConfig((String) LadaNiva.getText()));
        PremiumVehicalValue(32,LadaNiva.isChecked());
        LadaNiva.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(LadaNiva.getText()), LadaNiva.isChecked());
                PremiumVehicalValue(32,LadaNiva.isChecked());
            }
        });
// Heath

        final CheckBox Medkit = mFloatingView.findViewById(R.id.Medkit);
        Medkit.setChecked(getConfig((String) Medkit.getText()));
        PremiumItemValue(50,Medkit.isChecked());
        Medkit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Medkit.getText()), Medkit.isChecked());
                PremiumItemValue(50,Medkit.isChecked());
            }
        });

        final CheckBox Bandage = mFloatingView.findViewById(R.id.Bandage);
        Bandage.setChecked(getConfig((String) Bandage.getText()));
        PremiumItemValue(51,Bandage.isChecked());
        Bandage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Bandage.getText()), Bandage.isChecked());
                PremiumItemValue(51,Bandage.isChecked());
            }
        });

        final CheckBox FirstAid = mFloatingView.findViewById(R.id.FirstAidKit);
        FirstAid.setChecked(getConfig((String) FirstAid.getText()));
        PremiumItemValue(52,FirstAid.isChecked());
        FirstAid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(FirstAid.getText()), FirstAid.isChecked());
                PremiumItemValue(52,FirstAid.isChecked());
            }
        });
        final CheckBox Injection = mFloatingView.findViewById(R.id.Adrenaline);
        Injection.setChecked(getConfig((String) Injection.getText()));
        PremiumItemValue(53,Injection.isChecked());
        Injection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Injection.getText()), Injection.isChecked());
                PremiumItemValue(53,Injection.isChecked());
            }
        });


        final CheckBox EnergyDrink = mFloatingView.findViewById(R.id.EnergyDrink);
        EnergyDrink.setChecked(getConfig((String) EnergyDrink.getText()));
        PremiumItemValue(55,EnergyDrink.isChecked());
        EnergyDrink.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(EnergyDrink.getText()), EnergyDrink.isChecked());
                PremiumItemValue(55,EnergyDrink.isChecked());
            }
        });
        final CheckBox Painkiller = mFloatingView.findViewById(R.id.Painkiller);
        Painkiller.setChecked(getConfig((String) Painkiller.getText()));
        PremiumItemValue(54,Painkiller.isChecked());
        Painkiller.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Painkiller.getText()), Painkiller.isChecked());
                PremiumItemValue(54,Painkiller.isChecked());
            }
        });

        /*
         *
         *
         *
         *
         *    Weapons
         *
         *
         *
         *
         *  */



        final CheckBox canted = mFloatingView.findViewById(R.id.canted);
        canted.setChecked(getConfig((String) canted.getText()));
        PremiumItemValue(97,canted.isChecked());
        canted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(canted.getText()),canted.isChecked());
                PremiumItemValue(97,canted.isChecked());
            }
        });

        final CheckBox reddot = mFloatingView.findViewById(R.id.reddot);
        reddot.setChecked(getConfig((String) reddot.getText()));
        PremiumItemValue(98,reddot.isChecked());
        reddot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(reddot.getText()),reddot.isChecked());
                PremiumItemValue(98,reddot.isChecked());
            }
        });

        final CheckBox hollow = mFloatingView.findViewById(R.id.hollow);
        hollow.setChecked(getConfig((String) hollow.getText()));
        PremiumItemValue(96,hollow.isChecked());
        hollow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(hollow.getText()),hollow.isChecked());
                PremiumItemValue(96,hollow.isChecked());
            }
        });

        final CheckBox twox = mFloatingView.findViewById(R.id.twox);
        twox.setChecked(getConfig((String) twox.getText()));
        PremiumItemValue(101,twox.isChecked());
        twox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(twox.getText()),twox.isChecked());
                PremiumItemValue(101,twox.isChecked());
            }
        });

        final CheckBox threex = mFloatingView.findViewById(R.id.threex);
        threex.setChecked(getConfig((String) threex.getText()));
        PremiumItemValue(102,threex.isChecked());
        threex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(threex.getText()),threex.isChecked());
                PremiumItemValue(102,threex.isChecked());
            }
        });

        final CheckBox fourx = mFloatingView.findViewById(R.id.fourx);
        fourx.setChecked(getConfig((String) fourx.getText()));
        PremiumItemValue(100,fourx.isChecked());
        fourx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(fourx.getText()),fourx.isChecked());
                PremiumItemValue(100,fourx.isChecked());
            }
        });

        final CheckBox sixx = mFloatingView.findViewById(R.id.sixx);
        sixx.setChecked(getConfig((String) sixx.getText()));
        PremiumItemValue(103,sixx.isChecked());
        sixx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(sixx.getText()),sixx.isChecked());
                PremiumItemValue(103,sixx.isChecked());
            }
        });

        final CheckBox eightx = mFloatingView.findViewById(R.id.eightx);
        eightx.setChecked(getConfig((String) eightx.getText()));
        PremiumItemValue(99,eightx.isChecked());
        eightx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(eightx.getText()),eightx.isChecked());
                PremiumItemValue(99,eightx.isChecked());

            }
        });

        final CheckBox AWM = mFloatingView.findViewById(R.id.AWM);
        AWM.setChecked(getConfig((String) AWM.getText()));
        PremiumItemValue(76,AWM.isChecked());
        AWM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(AWM.getText()),AWM.isChecked());
                PremiumItemValue(76,AWM.isChecked());
            }
        });

        final CheckBox QBU = mFloatingView.findViewById(R.id.QBU);
        QBU.setChecked(getConfig((String) QBU.getText()));
        PremiumItemValue(77,QBU.isChecked());
        QBU.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(QBU.getText()),QBU.isChecked());
                PremiumItemValue(77,QBU.isChecked());
            }
        });

        final CheckBox SLR = mFloatingView.findViewById(R.id.SLR);
        SLR.setChecked(getConfig((String) SLR.getText()));
        PremiumItemValue(78,SLR.isChecked());
        SLR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(SLR.getText()),SLR.isChecked());
                PremiumItemValue(78,SLR.isChecked());
            }
        });

        final CheckBox SKS = mFloatingView.findViewById(R.id.SKS);
        SKS.setChecked(getConfig((String) SKS.getText()));
        PremiumItemValue(61,SKS.isChecked());

        SKS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(SKS.getText()),SKS.isChecked());
                PremiumItemValue(61,SKS.isChecked());
            }
        });

        final CheckBox Mini14 = mFloatingView.findViewById(R.id.Mini14);
        Mini14.setChecked(getConfig((String) Mini14.getText()));
        PremiumItemValue(79,Mini14.isChecked());
        Mini14.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Mini14.getText()),Mini14.isChecked());
                PremiumItemValue(79,Mini14.isChecked());
            }
        });

        final CheckBox M24 = mFloatingView.findViewById(R.id.M24);
        M24.setChecked(getConfig((String) M24.getText()));
        PremiumItemValue(80,M24.isChecked());
        M24.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(M24.getText()),M24.isChecked());
                PremiumItemValue(80,M24.isChecked());
            }
        });

        final CheckBox Kar98k = mFloatingView.findViewById(R.id.Kar98k);
        Kar98k.setChecked(getConfig((String) Kar98k.getText()));
        PremiumItemValue(63,Kar98k.isChecked());
        Kar98k.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Kar98k.getText()),Kar98k.isChecked());
                PremiumItemValue(63,Kar98k.isChecked());
            }
        });

        final CheckBox VSS = mFloatingView.findViewById(R.id.VSS);
        VSS.setChecked(getConfig((String) VSS.getText()));
        PremiumItemValue(81,VSS.isChecked());
        VSS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(VSS.getText()),VSS.isChecked());
                PremiumItemValue(81,VSS.isChecked());
            }
        });

        final CheckBox Win94 = mFloatingView.findViewById(R.id.Win94);
        Win94.setChecked(getConfig((String) Win94.getText()));
        PremiumItemValue(82,Win94.isChecked());
        Win94.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Win94.getText()),Win94.isChecked());
                PremiumItemValue(82,Win94.isChecked());
            }
        });

        final CheckBox AUG = mFloatingView.findViewById(R.id.AUG);
        AUG.setChecked(getConfig((String) AUG.getText()));
        PremiumItemValue(58,AUG.isChecked());
        AUG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(AUG.getText()),AUG.isChecked());
                PremiumItemValue(58,AUG.isChecked());
            }
        });

        final CheckBox M762 = mFloatingView.findViewById(R.id.M762);
        M762.setChecked(getConfig((String) M762.getText()));
        PremiumItemValue(112,M762.isChecked());
        M762.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(M762.getText()),M762.isChecked());
                PremiumItemValue(112,M762.isChecked());
            }
        });

        final CheckBox SCARL = mFloatingView.findViewById(R.id.SCARL);
        SCARL.setChecked(getConfig((String) SCARL.getText()));
        PremiumItemValue(62,SCARL.isChecked());
        SCARL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(SCARL.getText()),SCARL.isChecked());
                PremiumItemValue(62,SCARL.isChecked());
            }
        });

        final CheckBox M416 = mFloatingView.findViewById(R.id.M416);
        M416.setChecked(getConfig((String) M416.getText()));
        PremiumItemValue(57,M416.isChecked());
        M416.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(M416.getText()),M416.isChecked());
                PremiumItemValue(57,M416.isChecked());
            }
        });

        final CheckBox M16A4 = mFloatingView.findViewById(R.id.M16A4);
        M16A4.setChecked(getConfig((String) M16A4.getText()));
        PremiumItemValue(64,M16A4.isChecked());
        M16A4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(M16A4.getText()),M16A4.isChecked());
                PremiumItemValue(64,M16A4.isChecked());
            }
        });

        final CheckBox Mk47Mutant = mFloatingView.findViewById(R.id.Mk47Mutant);
        Mk47Mutant.setChecked(getConfig((String) Mk47Mutant.getText()));
        PremiumItemValue(60,Mk47Mutant.isChecked());
        Mk47Mutant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Mk47Mutant.getText()),Mk47Mutant.isChecked());
                PremiumItemValue(60,Mk47Mutant.isChecked());
            }
        });

        final CheckBox G36C = mFloatingView.findViewById(R.id.G36C);
        G36C.setChecked(getConfig((String) G36C.getText()));
        PremiumItemValue(65,G36C.isChecked());
        G36C.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(G36C.getText()),G36C.isChecked());
                PremiumItemValue(65,G36C.isChecked());
            }
        });

        final CheckBox QBZ = mFloatingView.findViewById(R.id.QBZ);
        QBZ.setChecked(getConfig((String) QBZ.getText()));
        PremiumItemValue(66,QBZ.isChecked());
        QBZ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(QBZ.getText()),QBZ.isChecked());
                PremiumItemValue(66,QBZ.isChecked());
            }
        });

        final CheckBox AKM = mFloatingView.findViewById(R.id.AKM);
        AKM.setChecked(getConfig((String) AKM.getText()));
        PremiumItemValue(56,AKM.isChecked());
        AKM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(AKM.getText()),AKM.isChecked());
                PremiumItemValue(56,AKM.isChecked());
            }
        });

        final CheckBox Groza = mFloatingView.findViewById(R.id.Groza);
        Groza.setChecked(getConfig((String) Groza.getText()));
        PremiumItemValue(67,Groza.isChecked());
        Groza.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Groza.getText()),Groza.isChecked());
                PremiumItemValue(67,Groza.isChecked());
            }
        });

        final CheckBox S12K = mFloatingView.findViewById(R.id.S12K);
        S12K.setChecked(getConfig((String) S12K.getText()));
        S12K.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(S12K.getText()),S12K.isChecked());
            }
        });

        final CheckBox DBS = mFloatingView.findViewById(R.id.DBS);
        DBS.setChecked(getConfig((String) DBS.getText()));
        DBS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(DBS.getText()),DBS.isChecked());
            }
        });

        final CheckBox S686 = mFloatingView.findViewById(R.id.S686);
        S686.setChecked(getConfig((String) S686.getText()));
        S686.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(S686.getText()),S686.isChecked());
            }
        });

        final CheckBox S1897 = mFloatingView.findViewById(R.id.S1897);
        S1897.setChecked(getConfig((String) S1897.getText()));
        S1897.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(S1897.getText()),S1897.isChecked());
            }
        });

        final CheckBox SawedOff = mFloatingView.findViewById(R.id.SawedOff);
        SawedOff.setChecked(getConfig((String) SawedOff.getText()));
        SawedOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(SawedOff.getText()),SawedOff.isChecked());
            }
        });

        final CheckBox TommyGun = mFloatingView.findViewById(R.id.TommyGun);
        TommyGun.setChecked(getConfig((String) TommyGun.getText()));
        PremiumItemValue(117,TommyGun.isChecked());
        TommyGun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(TommyGun.getText()),TommyGun.isChecked());
                PremiumItemValue(117,TommyGun.isChecked());
            }
        });

        final CheckBox MP5K = mFloatingView.findViewById(R.id.MP5K);
        MP5K.setChecked(getConfig((String) MP5K.getText()));
        PremiumItemValue(70,MP5K.isChecked());
        MP5K.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(MP5K.getText()),MP5K.isChecked());
                PremiumItemValue(70,MP5K.isChecked());
            }
        });

        final CheckBox Vector = mFloatingView.findViewById(R.id.Vector);
        Vector.setChecked(getConfig((String) Vector.getText()));
        PremiumItemValue(74,Vector.isChecked());
        Vector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Vector.getText()),Vector.isChecked());
                PremiumItemValue(74,Vector.isChecked());
            }
        });

        final CheckBox Uzi = mFloatingView.findViewById(R.id.Uzi);
        Uzi.setChecked(getConfig((String) Uzi.getText()));
        PremiumItemValue(69,Uzi.isChecked());
        Uzi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Uzi.getText()),Uzi.isChecked());
                PremiumItemValue(69,Uzi.isChecked());
            }
        });

        final CheckBox R1895 = mFloatingView.findViewById(R.id.R1895);
        R1895.setChecked(getConfig((String) R1895.getText()));
        R1895.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(R1895.getText()),R1895.isChecked());
            }
        });

        final CheckBox Vz61 = mFloatingView.findViewById(R.id.Vz61);
        Vz61.setChecked(getConfig((String) Vz61.getText()));
        Vz61.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Vz61.getText()),Vz61.isChecked());
            }
        });

        final CheckBox P92 = mFloatingView.findViewById(R.id.P92);
        P92.setChecked(getConfig((String) P92.getText()));
        P92.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(P92.getText()),P92.isChecked());
            }
        });

        final CheckBox P18C = mFloatingView.findViewById(R.id.P18C);
        P18C.setChecked(getConfig((String) P18C.getText()));
        P18C.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(P18C.getText()),P18C.isChecked());
            }
        });

        final CheckBox R45 = mFloatingView.findViewById(R.id.R45);
        R45.setChecked(getConfig((String) R45.getText()));
        R45.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(R45.getText()),R45.isChecked());
            }
        });

        final CheckBox P1911 = mFloatingView.findViewById(R.id.P1911);
        P1911.setChecked(getConfig((String) P1911.getText()));
        P1911.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(P1911.getText()),P1911.isChecked());
            }
        });

        final CheckBox DesertEagle = mFloatingView.findViewById(R.id.DesertEagle);
        DesertEagle.setChecked(getConfig((String) DesertEagle.getText()));
        DesertEagle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(DesertEagle.getText()),DesertEagle.isChecked());
            }
        });

        final CheckBox Sickle = mFloatingView.findViewById(R.id.Sickle);
        Sickle.setChecked(getConfig((String) Sickle.getText()));
        Sickle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Sickle.getText()),Sickle.isChecked());
            }
        });

        final CheckBox Machete = mFloatingView.findViewById(R.id.Machete);
        Machete.setChecked(getConfig((String) Machete.getText()));
        Machete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Machete.getText()),Machete.isChecked());
            }
        });

        final CheckBox Pan = mFloatingView.findViewById(R.id.Pan);
        Pan.setChecked(getConfig((String) Pan.getText()));
        Pan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Pan.getText()),Pan.isChecked());
            }
        });

        final CheckBox Mk14 = mFloatingView.findViewById(R.id.Mk14);
        Mk14.setChecked(getConfig((String) Mk14.getText()));
        Mk14.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Mk14.getText()),Mk14.isChecked());
            }
        });

        final CheckBox sst = mFloatingView.findViewById(R.id.sst);
        sst.setChecked(getConfig((String) sst.getText()));
        PremiumItemValue(83,sst.isChecked());
        sst.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(sst.getText()),sst.isChecked());
                PremiumItemValue(83,sst.isChecked());
            }
        });

        final CheckBox ffACP = mFloatingView.findViewById(R.id.ffACP);
        ffACP.setChecked(getConfig((String) ffACP.getText()));
        PremiumItemValue(85,ffACP.isChecked());
        ffACP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(ffACP.getText()),ffACP.isChecked());
                PremiumItemValue(85,ffACP.isChecked());
            }
        });

        final CheckBox ffs = mFloatingView.findViewById(R.id.ffs);
        ffs.setChecked(getConfig((String) ffs.getText()));
        PremiumItemValue(84,ffs.isChecked());
        ffs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(ffs.getText()),ffs.isChecked());
                PremiumItemValue(84,ffs.isChecked());
            }
        });

        final CheckBox nmm = mFloatingView.findViewById(R.id.nmm);
        nmm.setChecked(getConfig((String) nmm.getText()));
        PremiumItemValue(86,nmm.isChecked());
        nmm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(nmm.getText()),nmm.isChecked());
                PremiumItemValue(86,nmm.isChecked());
            }
        });

        final CheckBox tzzMagnum = mFloatingView.findViewById(R.id.tzzMagnum);
        tzzMagnum.setChecked(getConfig((String) tzzMagnum.getText()));
        PremiumItemValue(87,tzzMagnum.isChecked());
        tzzMagnum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(tzzMagnum.getText()),tzzMagnum.isChecked());
                PremiumItemValue(87,tzzMagnum.isChecked());
            }
        });

        final CheckBox otGuage = mFloatingView.findViewById(R.id.otGuage);
        otGuage.setChecked(getConfig((String) otGuage.getText()));
        PremiumItemValue(89,otGuage.isChecked());
        otGuage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(otGuage.getText()),otGuage.isChecked());
                PremiumItemValue(89,otGuage.isChecked());
            }
        });

        final CheckBox Choke = mFloatingView.findViewById(R.id.Choke);
        Choke.setChecked(getConfig((String) Choke.getText()));
        Choke.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Choke.getText()),Choke.isChecked());
            }
        });

        final CheckBox SniperCompensator = mFloatingView.findViewById(R.id.SniperCompensator);
        SniperCompensator.setChecked(getConfig((String) SniperCompensator.getText()));
        SniperCompensator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(SniperCompensator.getText()),SniperCompensator.isChecked());
            }
        });

        final CheckBox DP28 = mFloatingView.findViewById(R.id.DP28);
        DP28.setChecked(getConfig((String) DP28.getText()));
        PremiumItemValue(75,DP28.isChecked());
        DP28.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(DP28.getText()),DP28.isChecked());
                PremiumItemValue(75,DP28.isChecked());
            }
        });

        final CheckBox M249 = mFloatingView.findViewById(R.id.M249);
        M249.setChecked(getConfig((String) M249.getText()));
        PremiumItemValue(73,M249.isChecked());
        M249.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(M249.getText()),M249.isChecked());
                PremiumItemValue(73,M249.isChecked());
            }
        });

        final CheckBox Grenade = mFloatingView.findViewById(R.id.Grenade);
        Grenade.setChecked(getConfig((String) Grenade.getText()));
        PremiumItemValue(113,Grenade.isChecked());
        Grenade.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Grenade.getText()),Grenade.isChecked());
                PremiumItemValue(113,Grenade.isChecked());
            }
        });

        final CheckBox Smoke = mFloatingView.findViewById(R.id.Smoke);
        Smoke.setChecked(getConfig((String) Smoke.getText()));
        PremiumItemValue(114,Smoke.isChecked());
        Smoke.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Smoke.getText()),Smoke.isChecked());
                PremiumItemValue(114,Smoke.isChecked());
            }
        });

        final CheckBox Molotov = mFloatingView.findViewById(R.id.Molotov);
        Molotov.setChecked(getConfig((String) Molotov.getText()));
        PremiumItemValue(115,Molotov.isChecked());
        Molotov.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Molotov.getText()),Molotov.isChecked());
                PremiumItemValue(115,Molotov.isChecked());
            }
        });



        final CheckBox FlareGun = mFloatingView.findViewById(R.id.FlareGun);
        FlareGun.setChecked(getConfig((String) FlareGun.getText()));
        PremiumItemValue(104,FlareGun.isChecked());
        FlareGun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(FlareGun.getText()),FlareGun.isChecked());
                PremiumItemValue(104,FlareGun.isChecked());
            }
        });

        final CheckBox GullieSuit = mFloatingView.findViewById(R.id.GullieSuit);
        GullieSuit.setChecked(getConfig((String) GullieSuit.getText()));
        PremiumItemValue(105,GullieSuit.isChecked());
        GullieSuit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(GullieSuit.getText()),GullieSuit.isChecked());
                PremiumItemValue(105,GullieSuit.isChecked());
            }
        });

        final CheckBox UMP = mFloatingView.findViewById(R.id.UMP);
        UMP.setChecked(getConfig((String) UMP.getText()));
        PremiumItemValue(71,UMP.isChecked());
        UMP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(UMP.getText()),UMP.isChecked());
                PremiumItemValue(71,UMP.isChecked());
            }
        });

        final CheckBox Bizon = mFloatingView.findViewById(R.id.Bizon);
        Bizon.setChecked(getConfig((String) Bizon.getText()));
        PremiumItemValue(68,Bizon.isChecked());
        Bizon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Bizon.getText()),Bizon.isChecked());
                PremiumItemValue(68,Bizon.isChecked());
            }
        });

        final CheckBox CompensatorSMG = mFloatingView.findViewById(R.id.CompensatorSMG);
        CompensatorSMG.setChecked(getConfig((String) CompensatorSMG.getText()));
        CompensatorSMG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(CompensatorSMG.getText()),CompensatorSMG.isChecked());
            }
        });

        final CheckBox FlashHiderSMG = mFloatingView.findViewById(R.id.FlashHiderSMG);
        FlashHiderSMG.setChecked(getConfig((String) FlashHiderSMG.getText()));
        FlashHiderSMG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(FlashHiderSMG.getText()),FlashHiderSMG.isChecked());
            }
        });

        final CheckBox FlashHiderAr = mFloatingView.findViewById(R.id.FlashHiderAr);
        FlashHiderAr.setChecked(getConfig((String) FlashHiderAr.getText()));
        FlashHiderAr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(FlashHiderAr.getText()),FlashHiderAr.isChecked());
            }
        });

        final CheckBox ArCompensator = mFloatingView.findViewById(R.id.ArCompensator);
        ArCompensator.setChecked(getConfig((String) ArCompensator.getText()));
        ArCompensator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(ArCompensator.getText()),ArCompensator.isChecked());
            }
        });

        final CheckBox TacticalStock = mFloatingView.findViewById(R.id.TacticalStock);
        TacticalStock.setChecked(getConfig((String) TacticalStock.getText()));
        TacticalStock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(TacticalStock.getText()),TacticalStock.isChecked());
            }
        });

        final CheckBox Duckbill = mFloatingView.findViewById(R.id.Duckbill);
        Duckbill.setChecked(getConfig((String) Duckbill.getText()));
        Duckbill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Duckbill.getText()),Duckbill.isChecked());
            }
        });

        final CheckBox FlashHiderSniper = mFloatingView.findViewById(R.id.FlashHiderSniper);
        FlashHiderSniper.setChecked(getConfig((String) FlashHiderSniper.getText()));
        FlashHiderSniper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(FlashHiderSniper.getText()),FlashHiderSniper.isChecked());
            }
        });

        final CheckBox SuppressorSMG = mFloatingView.findViewById(R.id.SuppressorSMG);
        SuppressorSMG.setChecked(getConfig((String) SuppressorSMG.getText()));
        SuppressorSMG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(SuppressorSMG.getText()),SuppressorSMG.isChecked());
            }
        });

        final CheckBox HalfGrip = mFloatingView.findViewById(R.id.HalfGrip);
        HalfGrip.setChecked(getConfig((String) HalfGrip.getText()));
        HalfGrip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(HalfGrip.getText()),HalfGrip.isChecked());
            }
        });

        final CheckBox StockMicroUZI = mFloatingView.findViewById(R.id.StockMicroUZI);
        StockMicroUZI.setChecked(getConfig((String) StockMicroUZI.getText()));
        StockMicroUZI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(StockMicroUZI.getText()),StockMicroUZI.isChecked());
            }
        });

        final CheckBox SuppressorSniper = mFloatingView.findViewById(R.id.SuppressorSniper);
        SuppressorSniper.setChecked(getConfig((String) SuppressorSniper.getText()));
        SuppressorSniper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(SuppressorSniper.getText()),SuppressorSniper.isChecked());
            }
        });

        final CheckBox SuppressorAr = mFloatingView.findViewById(R.id.SuppressorAr);
        SuppressorAr.setChecked(getConfig((String) SuppressorAr.getText()));
        SuppressorAr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(SuppressorAr.getText()),SuppressorAr.isChecked());
            }
        });

        final CheckBox ExQdSniper = mFloatingView.findViewById(R.id.ExQdSniper);
        ExQdSniper.setChecked(getConfig((String) ExQdSniper.getText()));
        ExQdSniper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(ExQdSniper.getText()),ExQdSniper.isChecked());
            }
        });

        final CheckBox QdSMG = mFloatingView.findViewById(R.id.QdSMG);
        QdSMG.setChecked(getConfig((String) QdSMG.getText()));
        QdSMG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(QdSMG.getText()),QdSMG.isChecked());
            }
        });

        final CheckBox ExSMG = mFloatingView.findViewById(R.id.ExSMG);
        ExSMG.setChecked(getConfig((String) ExSMG.getText()));
        ExSMG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(ExSMG.getText()),ExSMG.isChecked());
            }
        });

        final CheckBox QdSniper = mFloatingView.findViewById(R.id.QdSniper);
        QdSniper.setChecked(getConfig((String) QdSniper.getText()));
        QdSniper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(QdSniper.getText()),QdSniper.isChecked());
            }
        });

        final CheckBox ExSniper = mFloatingView.findViewById(R.id.ExSniper);
        ExSniper.setChecked(getConfig((String) ExSniper.getText()));
        ExSniper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(ExSniper.getText()),ExSniper.isChecked());
            }
        });

        final CheckBox ExAr = mFloatingView.findViewById(R.id.ExAr);
        ExAr.setChecked(getConfig((String) ExAr.getText()));
        ExAr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(ExAr.getText()),ExAr.isChecked());
            }
        });

        final CheckBox ExQdAr = mFloatingView.findViewById(R.id.ExQdAr);
        ExQdAr.setChecked(getConfig((String) ExQdAr.getText()));
        ExQdAr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(ExQdAr.getText()),ExQdAr.isChecked());
            }
        });

        final CheckBox QdAr = mFloatingView.findViewById(R.id.QdAr);
        QdAr.setChecked(getConfig((String) QdAr.getText()));
        QdAr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(QdAr.getText()),QdAr.isChecked());
            }
        });

        final CheckBox ExQdSMG = mFloatingView.findViewById(R.id.ExQdSMG);
        ExQdSMG.setChecked(getConfig((String) ExQdSMG.getText()));
        ExQdSMG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(ExQdSMG.getText()),ExQdSMG.isChecked());
            }
        });

        final CheckBox QuiverCrossBow = mFloatingView.findViewById(R.id.QuiverCrossBow);
        QuiverCrossBow.setChecked(getConfig((String) QuiverCrossBow.getText()));
        QuiverCrossBow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(QuiverCrossBow.getText()),QuiverCrossBow.isChecked());
            }
        });

        final CheckBox BulletLoop = mFloatingView.findViewById(R.id.BulletLoop);
        BulletLoop.setChecked(getConfig((String) BulletLoop.getText()));
        BulletLoop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(BulletLoop.getText()),BulletLoop.isChecked());
            }
        });

        final CheckBox ThumbGrip = mFloatingView.findViewById(R.id.ThumbGrip);
        ThumbGrip.setChecked(getConfig((String) ThumbGrip.getText()));
        ThumbGrip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(ThumbGrip.getText()),ThumbGrip.isChecked());
            }
        });

        final CheckBox LaserSight = mFloatingView.findViewById(R.id.LaserSight);
        LaserSight.setChecked(getConfig((String) LaserSight.getText()));
        LaserSight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(LaserSight.getText()),LaserSight.isChecked());
            }
        });

        final CheckBox AngledGrip = mFloatingView.findViewById(R.id.AngledGrip);
        AngledGrip.setChecked(getConfig((String) AngledGrip.getText()));
        AngledGrip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(AngledGrip.getText()),AngledGrip.isChecked());
            }
        });

        final CheckBox LightGrip = mFloatingView.findViewById(R.id.LightGrip);
        LightGrip.setChecked(getConfig((String) LightGrip.getText()));
        LightGrip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(LightGrip.getText()),LightGrip.isChecked());
            }
        });

        final CheckBox VerticalGrip = mFloatingView.findViewById(R.id.VerticalGrip);
        VerticalGrip.setChecked(getConfig((String) VerticalGrip.getText()));
        VerticalGrip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(VerticalGrip.getText()),VerticalGrip.isChecked());
            }
        });

        final CheckBox GasCan = mFloatingView.findViewById(R.id.GasCan);
        GasCan.setChecked(getConfig((String) GasCan.getText()));
        GasCan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(GasCan.getText()),GasCan.isChecked());
            }
        });

        final CheckBox Arrow = mFloatingView.findViewById(R.id.Arrow);
        Arrow.setChecked(getConfig((String) Arrow.getText()));
        Arrow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Arrow.getText()),Arrow.isChecked());
            }
        });

        final CheckBox CrossBow = mFloatingView.findViewById(R.id.CrossBow);
        CrossBow.setChecked(getConfig((String) CrossBow.getText()));
        CrossBow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(CrossBow.getText()),CrossBow.isChecked());
            }
        });

        /*


        Armors

        */
        final CheckBox Baglvl1 = mFloatingView.findViewById(R.id.Baglvl1);
        Baglvl1.setChecked(getConfig((String) Baglvl1.getText()));
        PremiumItemValue(90,Baglvl1.isChecked());
        Baglvl1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Baglvl1.getText()),Baglvl1.isChecked());
                PremiumItemValue(90,Baglvl1.isChecked());
            }
        });

        final CheckBox Baglvl2 = mFloatingView.findViewById(R.id.Baglvl2);
        Baglvl2.setChecked(getConfig((String) Baglvl2.getText()));
        PremiumItemValue(91,Baglvl2.isChecked());
        Baglvl2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Baglvl2.getText()),Baglvl2.isChecked());
                PremiumItemValue(91,Baglvl2.isChecked());
            }
        });

        final CheckBox Baglvl3 = mFloatingView.findViewById(R.id.Baglvl3);
        Baglvl3.setChecked(getConfig((String) Baglvl3.getText()));
        PremiumItemValue(91,Baglvl3.isChecked());
        Baglvl3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Baglvl3.getText()),Baglvl3.isChecked());
                PremiumItemValue(91,Baglvl3.isChecked());
            }
        });

        final CheckBox Helmetlvl1 = mFloatingView.findViewById(R.id.Helmetlvl1);
        Helmetlvl1.setChecked(getConfig((String) Helmetlvl1.getText()));
        PremiumItemValue(93,Helmetlvl1.isChecked());
        Helmetlvl1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Helmetlvl1.getText()),Helmetlvl1.isChecked());
                PremiumItemValue(93,Helmetlvl1.isChecked());
            }
        });

        final CheckBox Helmetlvl2 = mFloatingView.findViewById(R.id.Helmetlvl2);
        Helmetlvl2.setChecked(getConfig((String) Helmetlvl2.getText()));
        PremiumItemValue(94,Helmetlvl1.isChecked());
        Helmetlvl2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Helmetlvl2.getText()),Helmetlvl2.isChecked());
                PremiumItemValue(94,Helmetlvl2.isChecked());
            }
        });

        final CheckBox Helmetlvl3 = mFloatingView.findViewById(R.id.Helmetlvl3);
        Helmetlvl3.setChecked(getConfig((String) Helmetlvl3.getText()));
        PremiumItemValue(95,Helmetlvl3.isChecked());
        Helmetlvl3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Helmetlvl3.getText()),Helmetlvl3.isChecked());
                PremiumItemValue(95,Helmetlvl3.isChecked());
            }
        });

        final CheckBox Vestlvl1 = mFloatingView.findViewById(R.id.Vestlvl1);
        Vestlvl1.setChecked(getConfig((String) Vestlvl1.getText()));
        PremiumItemValue(109,Vestlvl1.isChecked());
        Vestlvl1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Vestlvl1.getText()),Vestlvl1.isChecked());
                PremiumItemValue(109,Vestlvl1.isChecked());
            }
        });

        final CheckBox Vestlvl2 = mFloatingView.findViewById(R.id.Vestlvl2);
        Vestlvl2.setChecked(getConfig((String) Vestlvl2.getText()));
        PremiumItemValue(110,Vestlvl2.isChecked());
        Vestlvl2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Vestlvl2.getText()),Vestlvl2.isChecked());
                PremiumItemValue(110,Vestlvl2.isChecked());
            }
        });

        final CheckBox Vestlvl3 = mFloatingView.findViewById(R.id.Vestlvl3);
        Vestlvl3.setChecked(getConfig((String) Vestlvl3.getText()));
        PremiumItemValue(111,Vestlvl3.isChecked());
        Vestlvl3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Vestlvl3.getText()),Vestlvl3.isChecked());
                PremiumItemValue(111,Vestlvl3.isChecked());
            }
        });

        final CheckBox Stung = mFloatingView.findViewById(R.id.Stung);
        Stung.setChecked(getConfig((String) Stung.getText()));
        Stung.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Stung.getText()),Stung.isChecked());
            }
        });

        final CheckBox Crowbar = mFloatingView.findViewById(R.id.Crowbar);
        Crowbar.setChecked(getConfig((String) Crowbar.getText()));
        Crowbar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Crowbar.getText()),Crowbar.isChecked());
            }
        });


        final CheckBox Crate = mFloatingView.findViewById(R.id.Crate);
        Crate.setChecked(getConfig((String) Crate.getText()));
        PremiumItemValue(108,Crate.isChecked());
        Crate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Crate.getText()),Crate.isChecked());
                PremiumItemValue(108,Crate.isChecked());
            }
        });

        final CheckBox AirDrop = mFloatingView.findViewById(R.id.AirDrop);
        AirDrop.setChecked(getConfig((String) AirDrop.getText()));
        PremiumItemValue(106,AirDrop.isChecked());
        AirDrop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(AirDrop.getText()),AirDrop.isChecked());
                PremiumItemValue(106,AirDrop.isChecked());
            }
        });

        final CheckBox DropPlane = mFloatingView.findViewById(R.id.DropPlane);
        DropPlane.setChecked(getConfig((String) DropPlane.getText()));
        PremiumItemValue(107,DropPlane.isChecked());
        DropPlane.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(DropPlane.getText()),DropPlane.isChecked());
                PremiumItemValue(107,DropPlane.isChecked());
            }
        });
        final CheckBox isGrenadeWarning = mFloatingView.findViewById(R.id.isGrenadeWarning);
        isGrenadeWarning.setChecked(getConfig((String) isGrenadeWarning.getText()));
        PremiumItemValue(116,isGrenadeWarning.isChecked());
        isGrenadeWarning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isGrenadeWarning.getText()),isGrenadeWarning.isChecked());
                PremiumItemValue(116,isGrenadeWarning.isChecked());
            }
        });

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
        final CheckBox isSkelton = mFloatingView.findViewById(R.id.isSkelton);
        isSkelton.setChecked(getConfig((String) isSkelton.getText()));
        PremiumValue(614, getConfig((String) isSkelton.getText()));
        isSkelton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isSkelton.getText()), isSkelton.isChecked());
                PremiumValue(614, isSkelton.isChecked());
            }
        });
//        final CheckBox isHead = mFloatingView.findViewById(R.id.isHead);
//        isHead.setChecked(getConfig((String) isHead.getText()));
//        PremiumValue(6,getConfig((String) isHead.getText()));
//        isHead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                setValue(String.valueOf(isHead.getText()), isHead.isChecked());
//                PremiumValue(6,isHead.isChecked());
//            }
//        });

        final CheckBox clientside = mFloatingView.findViewById(R.id.clientside);
        final CheckBox serverside =mFloatingView.findViewById(R.id.serverside);
        final RadioGroup serversidegroup= mFloatingView.findViewById(R.id.serversidegroup);

        clientside.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ShellUtils.SU(myDaemon +" 132002");
                }else{
                    ShellUtils.SU(myDaemon +" 200213");
                }
            }
        });

        serverside.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    serversidegroup.setVisibility(View.VISIBLE);
                }else{
                   serversidegroup.setVisibility(View.GONE);
                }
            }
        });
       serversidegroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(RadioGroup group, int checkedId) {
               switch (checkedId){
                   case R.id.serversideon:
                       new Thread(new Runnable() {
                           @Override
                           public void run() {
                               ipstartcheat();
                         //      Log.d("ccheat","sd");
                           }
                       }).start();
                       break;
                   case R.id.serversideoff:
                       new Thread(new Runnable() {
                           @Override
                           public void run() {
                               ShellUtils.SU("iptables -F");
                               ShellUtils.SU("iptables --flush");
                               ShellUtils.SU("svc data disable");
                               ShellUtils.SU("svc wifi disable");
                               ShellUtils.SU("svc wifi enable");
                               ShellUtils.SU("svc data enable");
                               ipstopcheat();
                           }
                       }).start();
                       break;
               }
           }
       });



        final LinearLayout memorycheatlayout = mFloatingView.findViewById(R.id.memorycheatlayout);
        final Switch Memorycheats = mFloatingView.findViewById(R.id.memorycheat);
        Memorycheats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Memorycheats.isChecked()) {
                    ShellUtils.SU(myDaemon +" 131222002");
                    memorycheatlayout.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ipstartcheat();
                        }
                    }).start();

                }else{
                    memorycheatlayout.setVisibility(View.GONE);
                    ShellUtils.SU(myDaemon+" 10006");
                    PremiumValue(597,false);
                    ShellUtils.SU(myDaemon+" 10002");
                    ShellUtils.SU(myDaemon+" 10008");
                    PremiumValue(598,false);
                    ShellUtils.SU(myDaemon+" 10004");
                    ShellUtils.SU(myDaemon +" 12132002");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ShellUtils.SU("iptables -F");
                            ShellUtils.SU("iptables --flush");
                            ipstopcheat();
                        }
                    }).start();
                }
            }
        });

        final Switch magicbullet = mFloatingView.findViewById(R.id.magicbullet);
        magicbullet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(magicbullet.isChecked()) {
                    ShellUtils.SU(myDaemon+" 20004");
                    Toast.makeText(Instance," Magic Bullet Activated",Toast.LENGTH_SHORT).show();
                }else{
                    ShellUtils.SU(myDaemon+" 20005");
                    Toast.makeText(Instance,"Magic Bullet Deactivated",Toast.LENGTH_SHORT).show();
                }
            }
        });

        final RadioButton crossoff = mFloatingView.findViewById(R.id.crossoff);
        final RadioButton graphicross = mFloatingView.findViewById(R.id.graphicross);
        final RadioButton MemCross = mFloatingView.findViewById(R.id.memorycross);
        crossoff.setChecked(true);
        crossoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PremiumValue(597,false);
                ShellUtils.SU(myDaemon+" 10006");
                Toast.makeText(Instance,"Crosshair Deactivated",Toast.LENGTH_SHORT).show();
            }
        });
        graphicross.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PremiumValue(597,true);
                ShellUtils.SU(myDaemon +" 10006");
                Toast.makeText(Instance,"Grapical Crosshair Activated",Toast.LENGTH_SHORT).show();
            }
        });

        MemCross.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PremiumValue(597,false);
                ShellUtils.SU(myDaemon +" 10005");
                Toast.makeText(Instance," Memory Crosshair Activated",Toast.LENGTH_SHORT).show();
            }
        });
        final Switch Recoil = mFloatingView.findViewById(R.id.recoil);
        Recoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Recoil.isChecked()) {
                    Toast.makeText(Instance,"Recoil Compensation Activated",Toast.LENGTH_SHORT).show();
                   ShellUtils.SU(myDaemon+" 10001");
                }else{
                    Toast.makeText(Instance,"Recoil Compensation Deactivated",Toast.LENGTH_SHORT).show();
                  ShellUtils.SU(myDaemon+" 10002");
                }
            }
        });
        final Switch Midnight = mFloatingView.findViewById(R.id.midnight);
        Midnight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Midnight.isChecked()) {
                    ShellUtils.SU(myDaemon+" 10007");
                    Toast.makeText(Instance,"Night Sky Patch Applied",Toast.LENGTH_SHORT).show();
                }else{
                    ShellUtils.SU(myDaemon+" 10008");
                    Toast.makeText(Instance,"Restored Graphics To Normal",Toast.LENGTH_SHORT).show();
                }

            }
        });

        Memorycheats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Memorycheats.isChecked()) {
                    memorycheatlayout.setVisibility(View.VISIBLE);
                }else{
                    memorycheatlayout.setVisibility(View.GONE);
                }
            }
        });
        final RadioButton aimoff = mFloatingView.findViewById(R.id.Aimoff);
        final RadioButton aimon = mFloatingView.findViewById(R.id.Aimon);
        aimoff.setChecked(true);
        aimoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PremiumValue(598,false);
                ShellUtils.SU(myDaemon+" 10004");
                Toast.makeText(Instance,"Sticky Aim Activated On 100m Range Targets",Toast.LENGTH_SHORT).show();
                Toast.makeText(Instance,"Sticky Aim Deactivated",Toast.LENGTH_SHORT).show();

            }
        });
        aimon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PremiumValue(598,true);
                ShellUtils.SU(myDaemon+" 10003");
                Toast.makeText(Instance,"Sticky Aim Activated On 100m Range Targets",Toast.LENGTH_SHORT).show();
         //      Log.d("shell ",myDaemon+" 10003");
            }
        });


        final CheckBox isteamid = mFloatingView.findViewById(R.id.isteamid);
        isteamid.setChecked(getConfig((String) isteamid.getText()));
        PremiumValue(616, getConfig((String) isteamid.getText()));
        isteamid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isteamid.getText()), isteamid.isChecked());
                PremiumValue(616, isteamid.isChecked());
            }
        });

        final RadioGroup linegroup = mFloatingView.findViewById(R.id.linegroup);
        linegroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.lineoff:
                        PremiumValue(605,false);
                        PremiumValue(615,false);
                        PremiumValue(611,false);
                        break;
                    case R.id.linecenter:
                        PremiumValue(605,true);
                        PremiumValue(615,true);
                        PremiumValue(611,false);
                        break;
                    case R.id.linedown:
                        PremiumValue(605,true);
                        PremiumValue(611,true);
                        PremiumValue(615,false);

                        break;
                    default:
                        PremiumValue(611,false);
                        PremiumValue(615,false);
                        PremiumValue(605,true);
                        break;
                }
            }
        });


        final RadioGroup BoxGroup = mFloatingView.findViewById(R.id.boxgroup);
        BoxGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){

                    case R.id.box2d :
                        PremiumValue(612,false);
                        PremiumValue(606,true);
                        break;
                    case R.id.box3d:
                        PremiumValue(612,true);
                        PremiumValue(606,false);
                        break;
                    default:
                        PremiumValue(612,false);
                        PremiumValue(606,false);
                        break;

                }
            }
        });

        final CheckBox isBack = mFloatingView.findViewById(R.id.isBack);
        isBack.setChecked(getConfig((String) isBack.getText()));
        PremiumValue(607, getConfig((String) isBack.getText()));
        isBack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isBack.getText()), isBack.isChecked());
                PremiumValue(607, isBack.isChecked());
            }
        });

        final RadioGroup helathGroup = mFloatingView.findViewById(R.id.healthgroup);
        helathGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){

                    case R.id.healthhori :
                        PremiumValue(617,false);
                        PremiumValue(602,true);
                        break;
                    case R.id.healthverti:
                        PremiumValue(617,true);
                        PremiumValue(602,false);
                        break;
                    default:
                        PremiumValue(602,false);
                        PremiumValue(617,false);
                        break;

                }
            }
        });


        final CheckBox isName = mFloatingView.findViewById(R.id.isName);
        isName.setChecked(getConfig((String) isName.getText()));
        PremiumValue(601, getConfig((String) isName.getText()));
        isName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isName.getText()), isName.isChecked());
                PremiumValue(601, isName.isChecked());
            }
        });
        final CheckBox isDist = mFloatingView.findViewById(R.id.isDist);
        isDist.setChecked(getConfig((String) isDist.getText()));
        PremiumValue(603, getConfig((String) isDist.getText()));
        isDist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isDist.getText()), isDist.isChecked());
                PremiumValue(603, isDist.isChecked());
            }
        });
        final CheckBox itemname = mFloatingView.findViewById(R.id.itemname);
        itemname.setChecked(getConfig((String) itemname.getText()));
        PremiumValue(609, getConfig((String) itemname.getText()));
        itemname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(itemname.getText()), itemname.isChecked());
                PremiumValue(609, itemname.isChecked());
            }
        });
        final CheckBox itemdistance = mFloatingView.findViewById(R.id.itemDistance);
        itemdistance.setChecked(getConfig((String) itemdistance.getText()));
        PremiumValue(610, getConfig((String) itemdistance.getText()));
        itemdistance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(itemdistance.getText()), itemdistance.isChecked());
                PremiumValue(610, itemdistance.isChecked());
            }
        });

        final CheckBox Granade = mFloatingView.findViewById(R.id.Granade);
        Granade.setChecked(getConfig((String) Granade.getText()));
        PremiumItemValue(113, getConfig((String) Granade.getText()));
        Granade.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(Granade.getText()), Granade.isChecked());
                PremiumItemValue(113, Granade.isChecked());
            }
        });




        final Switch showall = mFloatingView.findViewById(R.id.showall);
        showall.setChecked(getConfig((String) showall.getText()));
        PremiumValue(609,getConfig((String)showall.getText()));
        PremiumValue(610,getConfig((String) showall.getText()));
        PremiumValue(613,getConfig((String) showall.getText()));
        showall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(showall.getText()), showall.isChecked());
                PremiumValue(609, getConfig((String)showall.getText()));
                PremiumValue(610, getConfig((String)showall.getText()));
                PremiumValue(613, getConfig((String)showall.getText()));
            }
        });


        final TextView playersize = mFloatingView.findViewById(R.id.txtplayer);
        final TextView itemsize = mFloatingView.findViewById(R.id.txtitem);

        final SeekBar slider = mFloatingView.findViewById(R.id.playersize);
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                playersize.setText(String.valueOf(progress));
                Size(999, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        final SeekBar itemslider = mFloatingView.findViewById(R.id.itemsize);
        itemslider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                itemsize.setText(String.valueOf(progress));
                Size(1000, progress);;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    public native int Init();

    public native void Stop();

    public native void Size(int setting_code, float value);

    public native void PremiumValue(int setting_code, boolean value);

    public native void PremiumVehicalValue(int setting_code, boolean value);

    public native void PremiumItemValue(int setting_code, boolean value);

    private LinearLayout.LayoutParams setParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        return params;
    }


}



