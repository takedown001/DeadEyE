package com.Gcc.Deadeye.lite;

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
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
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

import com.Gcc.Deadeye.AESUtils;
import com.Gcc.Deadeye.AppUpdaterActivity;
import com.Gcc.Deadeye.BetaLoadMem;
import com.Gcc.Deadeye.DownloadFile;
import com.Gcc.Deadeye.Fragment.AboutFragment;
import com.Gcc.Deadeye.Fragment.DownloadFragment;
import com.Gcc.Deadeye.Fragment.NewsFragment;
import com.Gcc.Deadeye.Fragment.SettingFragment;
import com.Gcc.Deadeye.GccConfig.urlref;
import com.Gcc.Deadeye.Helper;
import com.Gcc.Deadeye.LoadBeta;
import com.Gcc.Deadeye.LoadMem;
import com.Gcc.Deadeye.LoginActivity;
import com.Gcc.Deadeye.PluginActivity;
import com.Gcc.Deadeye.R;
import com.Gcc.Deadeye.RequestHandler;
import com.Gcc.Deadeye.ResellerActivity;
import com.Gcc.Deadeye.StoreActivity;
import com.Gcc.Deadeye.activityMaintain;
import com.Gcc.Deadeye.imgLoad;
import com.google.android.material.navigation.NavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static com.Gcc.Deadeye.GccConfig.urlref.TAG_KEY;
import static com.Gcc.Deadeye.GccConfig.urlref.time;

public class HomeActivityLite extends AppCompatActivity {


    ChipNavigationBar chipNavigationBar;
    private  boolean error;
    public static int REQUEST_OVERLAY_PERMISSION = 5469;
    // JSON Node names
    private static final String TAG_ERROR = urlref.TAG_ERROR;
    private static final String TAG_USERID = "userid";
    private String newversion;
    private String data = "data";
    private String whatsNewData;
    private boolean ismaintaince;
    private static final String TAG_APP_NEWVERSION = "newversion";
    private DrawerLayout drawer;
    Handler handler = new Handler();
    public static boolean safe =false;
    public static boolean burtal =false;
    RequestHandler requestHandler = new RequestHandler();
    public static boolean beta = false;
    ImageView rightico,leftico;
    String videourl,url;
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        InitRICObverlays();
        drawer = findViewById(R.id.drawer_layout);
        chipNavigationBar = findViewById(R.id.leftmenu);
        rightico = findViewById(R.id.rightico);
        leftico = findViewById(R.id.leftico);

        safe = getIntent().getBooleanExtra("safe", false);
        burtal = getIntent().getBooleanExtra("brutal", false);
//        Log.d("test", String.valueOf(safe));
//        Log.d("test", String.valueOf(burtal));
        // OneSignal Initialization

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                    new HomeFragmentlite()).commit();
            chipNavigationBar.setItemSelected(R.id.home, true);
        }

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
                        fragment = new HomeFragmentlite();
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
                    case R.id.gamecenter:
                        Toast.makeText(HomeActivityLite.this, "Features Coming Soon", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        fragment = new SettingFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.plugin:
                        Intent intent = new Intent(HomeActivityLite.this, PluginActivity.class);
                        intent.putExtra("safe", safe);
                        intent.putExtra("brutal", burtal);
                        startActivity(intent);
                        break;
                    case R.id.logout:
                        SharedPreferences shred = getSharedPreferences("userdetails", MODE_PRIVATE);
                        shred.edit().clear().apply();
                        finish();
                        Toast.makeText(HomeActivityLite.this, "Logged Out Successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(HomeActivityLite.this, LoginActivity.class));
                        break;
                }

            }
        });

        if(Helper.checkVPN(HomeActivityLite.this)){
            Toast.makeText(HomeActivityLite.this, "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
            finish();
        }else {
            File l = new File(getFilesDir().toString() + urlref.livelib);
            File b = new File(getFilesDir().toString() + urlref.Betalib);
            File m = new File(getFilesDir().toString() + urlref.libmem);
            File n = new File(getFilesDir().toString() + urlref.libbetamem);
            if (!l.exists() && safe && !b.exists() && !m.exists() && !n.exists()) {
                new DownloadFile(HomeActivityLite.this).execute(urlref.downloadpathLive);
                new LoadBeta(HomeActivityLite.this).execute(urlref.downloadpathBeta);
                new LoadMem(HomeActivityLite.this).execute(urlref.downloadpathmem);
                new BetaLoadMem(HomeActivityLite.this).execute(urlref.downloadpathBetamem);

            }

        }
        try {
            Check();
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private  void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if(imgLoad.Load(HomeActivityLite.this).equals(time)){
            finish();
        }else{
            new OneLoadAllProducts().execute();
        }
    }

    private void InitRICObverlays() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        transaction.replace(R.id.frame_container, fragment);
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
                case R.id.betaversion:
                    if (!beta) {
                        item.setTitle("Live Server");
                        beta = true;
                        Toast.makeText(HomeActivityLite.this, "You Are In Beta Testing", Toast.LENGTH_LONG).show();

                    } else {
                        item.setTitle("Beta Version");
                        beta =false;
                        Toast.makeText(HomeActivityLite.this, "You Are In Live Server", Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.ExtentUpgrade:
                    Intent i = new Intent(HomeActivityLite.this, StoreActivity.class);
                    startActivity(i);
                    return true;
                case R.id.Reseller:
                    Intent p = new Intent(HomeActivityLite.this, ResellerActivity.class);
                    startActivity(p);
                    return true;
                case R.id.customersupport:
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/DeadEyeTg_Bot"));
                    startActivity(browserIntent);
                    return true;
                case R.id.About:
                    fragment = new AboutFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }


    };

    @Override
    public void onBackPressed() {
        //Checks if the navigation drawer is open -- If so, close it
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        // If drawer is already close -- Do not override original functionality
        else {
            super.onBackPressed();
        }
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
            params.put(TAG_KEY, AESUtils.DarKnight.getEncrypted(shred.getString(TAG_KEY,"0")));

            return requestHandler.sendPostRequest(urlref.apkupdateurl, params);


        }


        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try {
                //converting response to json object
                JSONObject obj = new JSONObject(s);
                error = Boolean.parseBoolean(AESUtils.DarKnight.getDecrypted(obj.getString(TAG_ERROR)));

                if (!error) {
                    newversion = obj.getString(TAG_APP_NEWVERSION);
                    whatsNewData = obj.getString(data);
                    ismaintaince = obj.getBoolean("ismain");
                    videourl = obj.getString("videourl");
                    url = obj.getString("updateurl");
                    // Log.d("main",videourl);
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    String version = pInfo.versionName;

                    //    System.out.println("takedown" + "old:" + version + " new:" + newversion);

                    if (Float.parseFloat(version) < Float.parseFloat(newversion)) {
                        Intent intent = new Intent(HomeActivityLite.this, AppUpdaterActivity.class);
                        intent.putExtra(TAG_APP_NEWVERSION, newversion);
                        intent.putExtra(data,whatsNewData);
                        intent.putExtra("updateurl",url);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else if(ismaintaince){
                        Intent intent = new Intent(HomeActivityLite.this, activityMaintain.class);
                        startActivity(intent);
                    }
                }

            } catch (JSONException | PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

    }


}

