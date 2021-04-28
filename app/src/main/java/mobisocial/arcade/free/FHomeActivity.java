package mobisocial.arcade.free;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import mobisocial.arcade.AESUtils;
import mobisocial.arcade.AppUpdaterActivity;
import mobisocial.arcade.FloatLogo;
import mobisocial.arcade.Fragment.AboutFragment;
import mobisocial.arcade.Fragment.DownloadFragment;
import mobisocial.arcade.Fragment.NewsFragment;
import mobisocial.arcade.GccConfig.urlref;
import mobisocial.arcade.Helper;
import mobisocial.arcade.HomeActivity;
import mobisocial.arcade.LoginActivity;
import mobisocial.arcade.R;
import mobisocial.arcade.RequestHandler;
import mobisocial.arcade.ResellerActivity;
import mobisocial.arcade.ShellUtils;
import mobisocial.arcade.StoreActivity;
import mobisocial.arcade.activityMaintain;
import mobisocial.arcade.imgLoad;
import com.google.android.material.navigation.NavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.security.auth.callback.CallbackHandler;

import static mobisocial.arcade.GccConfig.urlref.TAG_KEY;
import static mobisocial.arcade.GccConfig.urlref.time;

public class FHomeActivity extends AppCompatActivity {
    static {
        System.loadLibrary("sysload");
    }

    ChipNavigationBar chipNavigationBar;
    private boolean error;
    public static int REQUEST_OVERLAY_PERMISSION = 5469;
    // JSON Node names
    private static final String TAG_ERROR = urlref.TAG_ERROR;
    private String newversion;
    private final String data = "data";
    private static int backbackexit = 1;
    private String whatsNewData;
    private boolean ismaintaince;
    private static final String TAG_APP_NEWVERSION = "newversion";
    private DrawerLayout drawer;
    Handler handler = new Handler();
    private String daemonPath;
    RequestHandler requestHandler = new RequestHandler();
    public static boolean beta = false;
    ImageView rightico, leftico;
    String videourl;

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fhome);
        InitRICObverlays();
        drawer = findViewById(R.id.fdrawer_layout);
        chipNavigationBar = findViewById(R.id.fleftmenu);
        rightico = findViewById(R.id.frightico);
        leftico = findViewById(R.id.fleftico);
        Context ctx = this;
//        Log.d("test", String.valueOf(safe));
//        Log.d("test", String.valueOf(burtal));
        // OneSignal Initialization
        if (!isConfigExist()) {
            Init();
        }
        NavigationView navigationView = findViewById(R.id.fnav_view);
        navigationView.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.freeframe_container,
                    new HomeFreeFragment()).commit();
            chipNavigationBar.setItemSelected(R.id.home, true);
        }
        //startDaemon();

        //		stopService(new Intent(ctx, BrutalService.class));

        leftico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipNavigationBar.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        chipNavigationBar.setVisibility(View.GONE);
                    }
                }, 5000);


            }
        });
        rightico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.END);

            }
        });

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment;
                switch (i) {
                    case R.id.home:
                        fragment = new HomeFreeFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.downloadcenter:
                        fragment = new DownloadFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.notification:
                        fragment = new NewsFragment();
                        loadFragment(fragment);
                        break;

                    case R.id.settings:
                        fragment = new FSettingFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.logout:
                        SharedPreferences shred = getSharedPreferences("userdetails", MODE_PRIVATE);
                        shred.edit().clear().apply();
                        finish();
                        Toast.makeText(FHomeActivity.this, "Logged Out Successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(FHomeActivity.this, LoginActivity.class));
                        break;
                }

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    new FLoadMem(FHomeActivity.this).execute(urlref.MemPathPublic);
                    new FHexLoad(FHomeActivity.this).execute(urlref.HexPublicLib);


                try {
                    Check();
                    loadAssets();
                } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            });
            }
        }).start();

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if (imgLoad.Load(FHomeActivity.this).equals(time)) {
            finish();
        } else {
            new OneLoadAllProducts().execute();
        }
    }

    private void InitRICObverlays() {
        if (!Settings.canDrawOverlays(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This application requires window overlays access permission, please allow first.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface p1, int p2) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    InitRICObverlays();
                }
            }
        }
    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.freeframe_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private final NavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.videosupport:
                    Intent c = new Intent(Intent.ACTION_VIEW, Uri.parse(videourl));
                    startActivity(c);
                    return true;
                case R.id.ExtentUpgrade:
                    Intent i = new Intent(FHomeActivity.this, StoreActivity.class);
                    startActivity(i);
                    return true;
                case R.id.Reseller:
                    Intent p = new Intent(FHomeActivity.this, ResellerActivity.class);
                    startActivity(p);
                    return true;
                case R.id.About:
                    fragment = new AboutFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }


    };

    public void loadAssets() {

        String pathf = getFilesDir().toString() + "/sysexe";
        try {
            OutputStream myOutput = new FileOutputStream(pathf);
            byte[] buffer = new byte[1024];
            int length;
            InputStream myInput = getAssets().open("sysexe");
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myInput.close();
            myOutput.flush();
            myOutput.close();

        } catch (IOException e) {
        }


        daemonPath = getFilesDir().toString() + "/sysexe";


        try {
            Runtime.getRuntime().exec("chmod 777 " + daemonPath);
        } catch (IOException e) {
        }

    }
    @Override
    public void onBackPressed() {

        if (backbackexit >= 2) {

            // Creating alert Dialog with three Buttons

            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
                    FHomeActivity.this);

            // Setting Dialog Title
            alertDialog.setTitle(getResources().getString(R.string.app_name));

            // Setting Dialog Message
            alertDialog.setMessage("Are you sure you want to exit??");

            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.icon);

            // Setting Positive Yes Button
            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog,
                                            int which) {
                            finish();
                        }
                    });
            // Setting Positive Yes Button
            alertDialog.setNeutralButton("NO",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog,
                                            int which) {
                        }
                    });
            // Showing Alert Message
            alertDialog.show();
//					super.onBackPressed();
        } else {
            backbackexit++;
            Toast.makeText(getBaseContext(), "Press again to Exit", Toast.LENGTH_SHORT).show();
        }
    }
    void Init() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("fps", 30);
        ed.putBoolean("Box", true);
        ed.putBoolean("Line", true);
        ed.putBoolean("Distance", false);
        ed.putBoolean("Health", false);
        ed.putBoolean("Name", false);
        ed.putBoolean("Head Position", false);
        ed.putBoolean("Back Mark", false);
        ed.putBoolean("Skelton", false);
        ed.putBoolean("Grenade Warning", false);
        ed.putBoolean("Enemy Weapon", false);
        ed.apply();
    }

    boolean isConfigExist() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.contains("fps");
    }


    class OneLoadAllProducts extends AsyncTask<Void, Void, String> {

        SharedPreferences shred = getSharedPreferences("userdetails", MODE_PRIVATE);

        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            HashMap<String, String> params = new HashMap<>();
            params.put(TAG_KEY, AESUtils.DarKnight.getEncrypted(shred.getString(TAG_KEY, "0")));

            return requestHandler.sendPostRequest(urlref.Freeapkupdateurl, params);


        }


        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        //converting response to json object
                        JSONObject obj = new JSONObject(s);
                        error = Boolean.parseBoolean(AESUtils.DarKnight.getDecrypted(obj.getString(TAG_ERROR)));

                        if (!error) {
                            newversion = obj.getString(TAG_APP_NEWVERSION);
                            whatsNewData = obj.getString(data);
                            ismaintaince = obj.getBoolean("ismain");
                    //        Log.d("test", String.valueOf(ismaintaince));
                            videourl = obj.getString("videourl");
                            String url = obj.getString("updateurl");
                            // Log.d("main",videourl);
                            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                            String version = pInfo.versionName;
                            //    System.out.println("takedown" + "old:" + version + " new:" + newversion);
                            if (Float.parseFloat(version) < Float.parseFloat(newversion)) {
                                Intent intent = new Intent(FHomeActivity.this, AppUpdaterActivity.class);
                                intent.putExtra(TAG_APP_NEWVERSION, newversion);
                                intent.putExtra(data, whatsNewData);
                                intent.putExtra("updateurl", url);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else if (ismaintaince) {
                                Intent intent = new Intent(FHomeActivity.this, activityMaintain.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }

                    } catch (JSONException | PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        }


    }
}