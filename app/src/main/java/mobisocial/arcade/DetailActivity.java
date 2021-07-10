package mobisocial.arcade;


import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import mobisocial.arcade.GccConfig.urlref;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyStoreException;
import java.util.HashMap;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;

import static mobisocial.arcade.GccConfig.urlref.TAG_DEVICEID;
import static mobisocial.arcade.GccConfig.urlref.TAG_IMG;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG_KEY = urlref.TAG_KEY;
    private static final String TAG_ERROR = urlref.TAG_ERROR;
    //store
    private static final String TAG_TITLE = urlref.TAG_TITLE;
    private static final String url = urlref.Main+"plan.php";
    JSONParserString jsonParserString = new JSONParserString();
    private String hash;
    Button ordernow,Upgraded;
    Handler handler = new Handler();
    private String key,Title,OneDay,FDay,TDay,deviceid,descrip,am,Dollar;
    RadioButton oneday, fday,tday,customupgrade;
    ImageView Img ;

    private TextView price,title,description,customtxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        SharedPreferences shred = getSharedPreferences("userdetails", MODE_PRIVATE);
        Img = findViewById(R.id.image);
        key = shred.getString(TAG_KEY, "null");
        price = findViewById(R.id.price);
        Upgraded = findViewById(R.id.Upgraded);
        customtxt = findViewById(R.id.cusomtxt);
        String img = getIntent().getExtras().getString(TAG_IMG);
        deviceid = Helper.getDeviceId(DetailActivity.this);
        Glide.with(DetailActivity.this).load(urlref.Image + img).placeholder(R.drawable.logo).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(Img);
        Title = getIntent().getExtras().getString(TAG_TITLE);
        ordernow = findViewById(R.id.ordernow);
        customupgrade = findViewById(R.id.customupgrade);



        ordernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailActivity.this,MyActivity.class);
                intent.putExtra("taxid",deviceid);
                intent.putExtra("amount", price.getText());
                intent.putExtra("product",Title);
            //    intent.putExtra("key",key);
                intent.putExtra("dollar",Dollar);

                if(oneday.isChecked()){
                    intent.putExtra("time","1");
                }else if(fday.isChecked()){
                    intent.putExtra("time","2");
                }else{
                    intent.putExtra("time","3");
                }
                startActivity(intent);
            }
        });


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
        if(Helper.checkVPN(DetailActivity.this)){
            Toast.makeText(DetailActivity.this, "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
            finish();
        }else {

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
            JSONObject params = new JSONObject();
            //creating request parameters
            String rq = null;
                try {
                    params.put(TAG_KEY,key);
                    params.put("14",Title); //store listing title
                    params.put(TAG_DEVICEID,deviceid);
                    rq = jsonParserString.makeHttpRequest(url, params);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            //returing the response
            return rq;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(lottieDialog !=null){
                        lottieDialog.dismiss();
                    }
                    if (s == null || s.isEmpty()) {
                        Toast.makeText(DetailActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        try {
                            JSONObject ack = new JSONObject(s);
                            String decData = Helper.profileDecrypt(ack.get("Data").toString(), ack.get("Hash").toString());
                            if (!Helper.verify(decData, ack.get("Sign").toString(), JSONParserString.publickey)) {
                                Toast.makeText(DetailActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                                return;
                            } else {
                                JSONObject obj = new JSONObject(decData);
                          //      Log.d("login", obj.toString());

                                boolean error =(obj.getBoolean(TAG_ERROR));
                                //      Log.d("asa", Boolean.toString(error));
                                //    Log.d("asa",AESUtils.DarKnight.getDecrypted(obj.getString(TAG_MSG)));
                                if (!error) {

                                    FDay = obj.getString("102");
                                    TDay =obj.getString("103");
                                    descrip = obj.getString("104");
                                    OneDay = obj.getString("105");
                                    Dollar = obj.getString("106");
                                    price.setText(OneDay);
                                    description.setText(descrip);
                                    if (Title.equals("Premium Plan")) {
                                        price.setText(FDay);
                                        fday.setChecked(true);
                                        oneday.setVisibility(View.GONE);
                                        tday.setVisibility(View.GONE);
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "SomeThing Went Wrong", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }


}
