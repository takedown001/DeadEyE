package com.Gcc.Deadeye;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.Gcc.Deadeye.GccConfig.urlref;
import com.Gcc.Deadeye.data.Download;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyStoreException;
import java.util.HashMap;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;

import static com.Gcc.Deadeye.GccConfig.urlref.TAG_DEVICEID;
import static com.Gcc.Deadeye.GccConfig.urlref.TAG_ERROR;
import static com.Gcc.Deadeye.GccConfig.urlref.TAG_IMG;
import static com.Gcc.Deadeye.GccConfig.urlref.TAG_KEY;
import static com.Gcc.Deadeye.GccConfig.urlref.TAG_MSG;
import static com.Gcc.Deadeye.GccConfig.urlref.TAG_RIMG;
import static com.Gcc.Deadeye.GccConfig.urlref.TAG_RNAME;
import static com.Gcc.Deadeye.GccConfig.urlref.TAG_TITLE;

public class RDetailActivity extends AppCompatActivity {


    private static final String url = urlref.Main+"rplan.php";
    JSONParserString jsonParserString = new JSONParserString();
    private String hash,link;
    Button ordernow;
    Handler handler = new Handler();
    private String key,Title,OneDay,FDay,TDay,deviceid,descrip,am,Dollar;
    RadioButton oneday, fday,tday;
    ImageView Img ;

    private TextView price,title,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rdetail);
        SharedPreferences shred = getSharedPreferences("userdetails", MODE_PRIVATE);
        Img = findViewById(R.id.image);
        key = shred.getString(TAG_KEY, "null");
        price = findViewById(R.id.price);

        String img = getIntent().getExtras().getString(TAG_RIMG);
        deviceid = LoginActivity.getDeviceId(RDetailActivity.this);
        Glide.with(RDetailActivity.this).load(urlref.Image + img).placeholder(R.drawable.logo).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(Img);
        Title = getIntent().getExtras().getString(TAG_RNAME);
        ordernow = findViewById(R.id.ordernow);


        tday = findViewById(R.id.tday);
        oneday = findViewById(R.id.oneday);
        fday = findViewById(R.id.fday);
        description = findViewById(R.id.desc);
        title = findViewById(R.id.title);
        title.setText(Title);
        tday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tday.setChecked(true);
                oneday.setChecked(false);
                fday.setChecked(false);
                price.setText(TDay);


            }
        });

        fday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fday.setChecked(true);
                oneday.setChecked(false);
                tday.setChecked(false);
                price.setText(FDay);
            }
        });

        oneday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price.setText(OneDay);
                oneday.setChecked(true);
                fday.setChecked(false);
                tday.setChecked(false);
            }
        });
        ordernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(link);
                Intent waIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(waIntent);
            }
        });

        if (Helper.checkVPN(RDetailActivity.this)) {
            Toast.makeText(RDetailActivity.this, "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
            finish();
        } else {
            new Oneload().execute();
        }
    }

        class Oneload extends AsyncTask<Void, Void, String> {
        final DialogFragment lottieDialog = new LottieDialogFragment().newInstance("loading_state_done.json", true);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lottieDialog.show(getFragmentManager(), "Loadingdialog");
            lottieDialog.setCancelable(false);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object


            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put(TAG_KEY,AESUtils.DarKnight.getEncrypted(key));
            params.put("14",AESUtils.DarKnight.getEncrypted(Title)); //store listing title
            params.put(TAG_DEVICEID,AESUtils.DarKnight.getEncrypted(deviceid));
//            Log.d("allarray",AESUtils.DarKnight.getEncrypted(key));
//           Log.d("allarray",AESUtils.DarKnight.getEncrypted(Title));
//          Log.d("allarray",AESUtils.DarKnight.getEncrypted(deviceid));
            String rq = null;
            if(Helper.checkVPN(RDetailActivity.this)){
                Toast.makeText(RDetailActivity.this, "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                finish();
            }else {
                try {
                    rq = jsonParserString.makeHttpRequest(url, params);
                    Log.d("allarray", rq);
                } catch (KeyStoreException | IOException e) {
                    e.printStackTrace();
                }
            }
            //returing the response
            return rq;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    lottieDialog.dismiss();
                    try {

                        JSONObject obj = new JSONObject(s);
                        //    Log.d("login", obj.toString());
                        //checking for error to authenticate
                        boolean error = Boolean.parseBoolean(AESUtils.DarKnight.getDecrypted(obj.getString(TAG_ERROR)));
                            //  Log.d("asa", Boolean.toString(error));
                           // Log.d("asa",AESUtils.DarKnight.getDecrypted(obj.getString(TAG_MSG)));
                        if(Helper.checkVPN(RDetailActivity.this)){
                            Toast.makeText(RDetailActivity.this, "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                            finish();
                        }else {
                            if (!error) {

                                FDay = AESUtils.DarKnight.getDecrypted(obj.getString("102"));
                                TDay = AESUtils.DarKnight.getDecrypted(obj.getString("103"));
                                descrip = AESUtils.DarKnight.getDecrypted(obj.getString("104"));
                                OneDay = AESUtils.DarKnight.getDecrypted(obj.getString("105"));
                                Dollar = AESUtils.DarKnight.getDecrypted(obj.getString("106"));
                                link = AESUtils.DarKnight.getDecrypted(obj.getString("200"));
                                price.setText(OneDay);
                                description.setText(descrip);

                            } else {
                                //saving to prefrences m

                                //getting the user from the response.
                                //starting the profile activity
                                Toast.makeText(getApplicationContext(), "SomeThing Went Wrong", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, 2000);
        }
    }


}
