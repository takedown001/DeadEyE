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
        deviceid = LoginActivity.getDeviceId(DetailActivity.this);
        Glide.with(DetailActivity.this).load(urlref.Image + img).placeholder(R.drawable.logo).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(Img);
        Title = getIntent().getExtras().getString(TAG_TITLE);
        ordernow = findViewById(R.id.ordernow);
        customupgrade = findViewById(R.id.customupgrade);


        if(!key.equals("null")) {

            if (Title.equals("Premium Plan") && !HomeActivity.safe) {
                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage("If You Are Upgrading Your Remaining Time Will Be Lost, According To Our Policy")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
            }
            if (Title.equals("Basci Plan") && HomeActivity.safe || HomeActivity.burtal) {
                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage("If You Are Downgrading Your Time Will Be Extended and Added Features Are Limited To Your Selected Plan")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
            } else if (Title.equals("Ultra Premium Plan") && !HomeActivity.burtal) {
                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage("If You Are Upgrading Your Remaining Time Will Be Lost, According To Our Policy")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

        }
        Upgraded.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           Toast.makeText(DetailActivity.this," Product Can't Buy ",Toast.LENGTH_LONG).show();
       }
   });


        ordernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailActivity.this,MyActivity.class);
                intent.putExtra("taxid",deviceid);
                intent.putExtra("amount", price.getText());
                intent.putExtra("product",Title);
                intent.putExtra("key",key);
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


            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put(TAG_KEY,AESUtils.DarKnight.getEncrypted(key));
            params.put("14",AESUtils.DarKnight.getEncrypted(Title)); //store listing title
            params.put(TAG_DEVICEID,AESUtils.DarKnight.getEncrypted(deviceid));
//            Log.d("allarray",AESUtils.DarKnight.getEncrypted(key));
//            Log.d("allarray",AESUtils.DarKnight.getEncrypted(Title));
//            Log.d("allarray",AESUtils.DarKnight.getEncrypted(deviceid));
            String rq = null;

                try {
                    rq = jsonParserString.makeHttpRequest(url, params);
                    // Log.d("allarray",rq);
                } catch (KeyStoreException | IOException e) {
                    e.printStackTrace();
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
                        if(Helper.checkVPN(DetailActivity.this)){
                            Toast.makeText(DetailActivity.this, "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                            finish();
                        }else {

                            boolean error = Boolean.parseBoolean(AESUtils.DarKnight.getDecrypted(obj.getString(TAG_ERROR)));
                            //      Log.d("asa", Boolean.toString(error));
                            //    Log.d("asa",AESUtils.DarKnight.getDecrypted(obj.getString(TAG_MSG)));
                            if (!error) {

                                FDay = AESUtils.DarKnight.getDecrypted(obj.getString("102"));
                                TDay = AESUtils.DarKnight.getDecrypted(obj.getString("103"));
                                descrip = AESUtils.DarKnight.getDecrypted(obj.getString("104"));
                                OneDay = AESUtils.DarKnight.getDecrypted(obj.getString("105"));
                                Dollar = AESUtils.DarKnight.getDecrypted(obj.getString("106"));

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
