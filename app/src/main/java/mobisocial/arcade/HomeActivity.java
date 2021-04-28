package mobisocial.arcade;
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

import mobisocial.arcade.Fragment.AboutFragment;
import mobisocial.arcade.Fragment.DownloadFragment;
import mobisocial.arcade.Fragment.HomeFragment;
import mobisocial.arcade.Fragment.NewsFragment;
import mobisocial.arcade.Fragment.SettingFragment;
import mobisocial.arcade.GccConfig.urlref;
import mobisocial.arcade.free.FHexLoad;
import mobisocial.arcade.free.FHomeActivity;
import mobisocial.arcade.free.FLoadMem;

import com.google.android.gms.common.util.Hex;
import com.google.android.material.navigation.NavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.topjohnwu.superuser.Shell;

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

import static mobisocial.arcade.GccConfig.urlref.TAG_KEY;
import static mobisocial.arcade.GccConfig.urlref.time;


public class HomeActivity extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;
    private  boolean error;
    public static int REQUEST_OVERLAY_PERMISSION = 5469;
    // JSON Node names
    private static final String TAG_ERROR = urlref.TAG_ERROR;
    private String newversion;
    private String data = "data";
    private String whatsNewData;
    private boolean ismaintaince;
    private static final String TAG_APP_NEWVERSION = "newversion";
    private DrawerLayout drawer;
    Handler handler = new Handler();
    public static boolean safe =false;
    private static int backbackexit = 1;
    public static boolean burtal =false;
    RequestHandler requestHandler = new RequestHandler();
    public static boolean beta = false;
    ImageView rightico,leftico;
    private String videourl,url,daemonPath;
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
        if (!isConfigExist()) {
            Init();
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                    new HomeFragment()).commit();
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
                        fragment = new HomeFragment();
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
                        Toast.makeText(HomeActivity.this, "Features Coming Soon", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        fragment = new SettingFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.plugin:
                        Intent intent = new Intent(HomeActivity.this, PluginActivity.class);
                        intent.putExtra("safe", safe);
                        intent.putExtra("brutal", burtal);
                        startActivity(intent);
                        break;
                    case R.id.logout:
                        SharedPreferences shred = getSharedPreferences("userdetails", MODE_PRIVATE);
                        shred.edit().clear().apply();
                        finish();
                        Toast.makeText(HomeActivity.this, "Logged Out Successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        break;
                }

            }
        });

          if(safe){
              new HexLoad(HomeActivity.this).execute(urlref.HexPathSafe);
              new MemLoad(HomeActivity.this).execute(urlref.MemPathSafe);
          }
        new Thread(() -> {
            try {

                Check();
                loadanimation();
                loadAssets();
            } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }).start();


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private  void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if (imgLoad.Load(HomeActivity.this).equals(time)) {
            finish();
        } else {
            new OneLoadAllProducts().execute();

        }
    }

    private void InitRICObverlays() {
        if (!Settings.canDrawOverlays(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This application requires window overlays access permission, please allow first.");
            builder.setPositiveButton("OK", (p1, p2) -> {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
            });
            builder.setCancelable(false);
            builder.show();
        }
    }

    @Override
    public void onBackPressed() {

        if (backbackexit >= 2) {

            // Creating alert Dialog with three Buttons

            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
                    HomeActivity.this);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (!Settings.canDrawOverlays(this)) {
                InitRICObverlays();
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
                        Toast.makeText(HomeActivity.this, "You Are In Beta Testing", Toast.LENGTH_LONG).show();

                    } else {
                        item.setTitle("Beta Version");
                        beta =false;
                        Toast.makeText(HomeActivity.this, "You Are In Live Server", Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.ExtentUpgrade:
                    Intent i = new Intent(HomeActivity.this,StoreActivity.class);
                    startActivity(i);
                    return true;
                case R.id.Reseller:
                 Intent p = new Intent(HomeActivity.this,ResellerActivity.class);
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void loadanimation() {
        String pathf = getFilesDir().toString();
        try
        {
            OutputStream myOutput = new FileOutputStream(pathf+"/animation.json");
            byte[] buffer = new byte[1024];
            int length;
            InputStream myInput =getAssets().open("animation.json");

            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myInput.close();
            myOutput.flush();
            myOutput.close();
        }

        catch (IOException e)
        {
        }

        ShellUtils.SU("chmod 755 "+ pathf+"/animation.json");

    }


    public void loadAssets()
    {

        new Thread()
        {
            public void run() {
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
        }.start();
    }

    void Init(){
        SharedPreferences sp=getApplicationContext().getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed= sp.edit();
        ed.putInt("fps",30);
        ed.putBoolean("Box",true);
        ed.putBoolean("Line",true);
        ed.putBoolean("Distance",false);
        ed.putBoolean("Health",false);
        ed.putBoolean("Name",false);
        ed.putBoolean("Head Position",false);
        ed.putBoolean("Back Mark",false);
        ed.putBoolean("Skelton",false);
        ed.putBoolean("Grenade Warning",false);
        ed.putBoolean("Enemy Weapon",false);
        ed.apply();
    }
    boolean isConfigExist(){
        SharedPreferences sp=getApplicationContext().getSharedPreferences("espValue", Context.MODE_PRIVATE);
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
                params.put(TAG_KEY,AESUtils.DarKnight.getEncrypted(shred.getString(TAG_KEY,"0")));

                return requestHandler.sendPostRequest(urlref.apkupdateurl, params);


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
                        videourl = obj.getString("videourl");
                        url = obj.getString("updateurl");
                       // Log.d("main",videourl);
                        ShellUtils.SU("su");
                        PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                        String version = pInfo.versionName;

                        //    System.out.println("takedown" + "old:" + version + " new:" + newversion);

                        if (Float.parseFloat(version) < Float.parseFloat(newversion)) {
                        Intent intent = new Intent(HomeActivity.this, AppUpdaterActivity.class);
                        intent.putExtra(TAG_APP_NEWVERSION, newversion);
                        intent.putExtra(data,whatsNewData);
                        intent.putExtra("updateurl",url);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        }
                       else if(ismaintaince){
                            Intent intent = new Intent(HomeActivity.this, activityMaintain.class);
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
