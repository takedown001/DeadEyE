package mobisocial.arcade.bgmi;

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

import mobisocial.arcade.AppUpdaterActivity;
import mobisocial.arcade.GameActivity;
import mobisocial.arcade.GetFile;
import mobisocial.arcade.JSONParserString;
import mobisocial.arcade.Fragment.AboutFragment;
import mobisocial.arcade.Fragment.DownloadFragment;
import mobisocial.arcade.Fragment.NewsFragment;
import mobisocial.arcade.Fragment.SettingFragment;
import mobisocial.arcade.GccConfig.urlref;
import mobisocial.arcade.Helper;
import mobisocial.arcade.LoginActivity;
import mobisocial.arcade.PluginActivity;
import mobisocial.arcade.R;
import mobisocial.arcade.ResellerActivity;
import mobisocial.arcade.activityMaintain;
import mobisocial.arcade.imgLoad;
import com.google.android.material.navigation.NavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

import static mobisocial.arcade.GccConfig.urlref.TAG_DEVICEID;
import static mobisocial.arcade.GccConfig.urlref.TAG_KEY;
import static mobisocial.arcade.GccConfig.urlref.TAG_ONESIGNALID;
import static mobisocial.arcade.GccConfig.urlref.time;

public class HomeActivity extends AppCompatActivity {


    ChipNavigationBar chipNavigationBar;
    private boolean error;
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
    JSONParserString jsonParserString = new JSONParserString();
    ImageView rightico, leftico;
    String videourl, url,UUID;
    public static boolean safe =false;
    public static boolean burtal =false;
    static{
        System.loadLibrary("sysload");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        InitRICObverlays();
        drawer = findViewById(R.id.drawer_layout);
        chipNavigationBar = findViewById(R.id.leftmenu);
        rightico = findViewById(R.id.rightico);
        leftico = findViewById(R.id.leftico);
        OneSignal.idsAvailable((userId, registrationId) -> {
            UUID = userId;
        });

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

        if (Helper.checkVPN(HomeActivity.this)) {
            Toast.makeText(HomeActivity.this, "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
            finish();
        } else {
//            File l = new File(getFilesDir().toString() + urlref.IndiaMem);
//
//            if (!l.exists()) {
//                new GetFile(HomeActivity.this).execute(urlref.IndiaMem);
//            }
        }
        try {
            Check();
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if (imgLoad.Load(HomeActivity.this).equals(time)) {
            finish();

        } else {
            loadAssets();
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

    public void loadAssets()
    {

        new Thread()
        {
            public void run() {
                String pathf =getFilesDir().toString() + "/sysexe";
                try {
                    OutputStream myOutput = new FileOutputStream(pathf);
                    byte[] buffer = new byte[1024];
                    int length;
                    InputStream myInput =getAssets().open("sysexe");
                    while ((length = myInput.read(buffer)) > 0) {
                        myOutput.write(buffer, 0, length);
                    }
                    myInput.close();
                    myOutput.flush();
                    myOutput.close();

                } catch (IOException e) {
                }
                try {
                    Runtime.getRuntime().exec("chmod 777 " +pathf);
                } catch (IOException e) {
                }
            }
        }.start();
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
//                case R.id.betaversion:
//                    if (!beta) {
//                        item.setTitle("Live Server");
//                        beta = true;
//                        Toast.makeText(HomeActivityLite.this, "You Are In Beta Testing", Toast.LENGTH_LONG).show();
//
//                    } else {
//                        item.setTitle("Beta Version");
//                        beta = false;
//                        Toast.makeText(HomeActivityLite.this, "You Are In Live Server", Toast.LENGTH_LONG).show();
//                    }
//                    break;
                case R.id.ExtentUpgrade:
                    Intent q = new Intent(Intent.ACTION_VIEW,Uri.parse("https://shop.Gamesploit.com"));
                    startActivity(q);
                    return true;
                case R.id.Reseller:
                    Intent p = new Intent(HomeActivity.this, ResellerActivity.class);
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
            JSONObject params = new JSONObject();
            String s = null;
            try {
                params.put(TAG_DEVICEID,Helper.getDeviceId(HomeActivity.this));
                params.put(TAG_ONESIGNALID,UUID);
                params.put(TAG_KEY,shred.getString(TAG_KEY,"null"));
                s= jsonParserString.makeHttpRequest(urlref.Main+"login.php", params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return s;
        }


        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null || s.isEmpty()) {
                Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                return ;
            }
            try {
                JSONObject ack = new JSONObject(s);
                String decData = Helper.profileDecrypt(ack.get("Data").toString(), ack.get("Hash").toString());
                if (!Helper.verify(decData, ack.get("Sign").toString(), JSONParserString.publickey)) {
                    Toast.makeText(HomeActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                    return ;
                } else {
                    //converting response to json object
                    JSONObject obj = new JSONObject(decData);
                    error = obj.getBoolean(TAG_ERROR);
               //      Log.d("test",obj.toString());
                    if (error || shred.getString(TAG_KEY,"null").equals("null")) {
                        startActivity(new Intent(HomeActivity.this, GameActivity.class));
                        Toast.makeText(HomeActivity.this ,"Integrity Check Failed",Toast.LENGTH_SHORT).show();
                    }
                    newversion = obj.getString(TAG_APP_NEWVERSION);
                    whatsNewData = obj.getString(data);
                    ismaintaince = obj.getBoolean("ismain");
                    videourl = obj.getString("videourl");
                    url = obj.getString("updateurl");
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    String version = pInfo.versionName;
                    //    System.out.println("takedown" + "old:" + version + " new:" + newversion);
                    if (Float.parseFloat(version) < Float.parseFloat(newversion)) {
                        Intent intent = new Intent(HomeActivity.this, AppUpdaterActivity.class);
                        intent.putExtra(TAG_APP_NEWVERSION, newversion);
                        intent.putExtra(data, whatsNewData);
                        intent.putExtra("updateurl", url);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (ismaintaince) {
                        Intent intent = new Intent(HomeActivity.this, activityMaintain.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}