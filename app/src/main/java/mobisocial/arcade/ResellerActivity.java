package mobisocial.arcade;

import android.animation.ArgbEvaluator;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import mobisocial.arcade.Adapter.ResellerAdapter;
import mobisocial.arcade.GccConfig.urlref;
import mobisocial.arcade.data.Reseller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;

import static mobisocial.arcade.GccConfig.urlref.TAG_RESELLERURL;
import static mobisocial.arcade.GccConfig.urlref.TAG_RIMG;

public class ResellerActivity extends AppCompatActivity {

    ViewPager viewPager;
    ResellerAdapter adapter;
    private String deviceid;
    //user

    private static final String TAG_DEVICEID = urlref.TAG_DEVICEID;
    private static final String TAG_SUCCESS = urlref.TAG_SUCCESS;
    private static final String TAG_RESELLER = urlref.TAG_RESELLER;
    private final JSONParserString jsonParser = new JSONParserString();
    //plan

    private static final String TAG_RNAME = urlref.TAG_RNAME;
    private static final String TAG_RDESC = urlref.TAG_RDESC;


    private static final String url = urlref.Main +"reseller.php";

    private JSONArray jsonarray = null;
    private ArrayList<HashMap<String, String>> offersList;
    private int success;
    private List<Reseller> ResellerList = new ArrayList<>();

    Integer[] colors = null;

    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    final DialogFragment lottieDialog = new LottieDialogFragment().newInstance("loadingdone.json",true);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reseller);
        offersList = new ArrayList<>();

        lottieDialog.setCancelable(false);
        ResellerList = new ArrayList<>();

        //  models.add(new Model(R.drawable.namecard, "", "Business cards are cards bearing business information about a company or individual."));

        adapter = new ResellerAdapter(ResellerList, this);

        if(Helper.checkVPN(ResellerActivity.this)){
            Toast.makeText(ResellerActivity.this, "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
            finish();
        }else {
            new OneLoadAllProducts().execute();
        }
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.white),
                getResources().getColor(R.color.color4)
        };

        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position < (adapter.getCount() -1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor(

                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                }

                else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    class OneLoadAllProducts extends AsyncTask<String, String, String> {
        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(String s) {
            if (lottieDialog != null) {
                lottieDialog.dismiss();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                        if (success == 1) {

                            for (int i = 0; i < offersList.size(); i++) {
                                Reseller reseller = new Reseller();
                                reseller.setTitle(offersList.get(i).get(TAG_RNAME));
                                reseller.setDesc(offersList.get(i).get(TAG_RDESC));
                                reseller.setImage(offersList.get(i).get(TAG_RIMG));
                                reseller.setResellerurl(offersList.get(i).get(TAG_RESELLERURL));
                                ResellerList.add(reseller);
                                adapter.notifyDataSetChanged();

                            }
                        } else {

                            Toast.makeText(ResellerActivity.this, "SomeThing Went Wrong", Toast.LENGTH_LONG).show();

                        }
                    }


            });
        }






        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            deviceid =  Helper.getDeviceId(ResellerActivity.this);
            lottieDialog.show(getFragmentManager(),"lol");
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONObject params = new JSONObject();
            try {
                params.put(TAG_DEVICEID,deviceid);
                String s=null;
                s = jsonParser.makeHttpRequest(url, params);
                JSONObject ack = new JSONObject(s);
                String decData = Helper.profileDecrypt(ack.get("Data").toString(), ack.get("Hash").toString());
                if (!Helper.verify(decData, ack.get("Sign").toString(), JSONParserString.publickey)) {
                    Toast.makeText(ResellerActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                    return null;
                } else {
                    JSONObject json = new JSONObject(decData);
                   Log.d("login", json.toString());

                    success = json.getInt(TAG_SUCCESS);
                    jsonarray = json.getJSONArray(TAG_RESELLER);

                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject c = jsonarray.getJSONObject(i);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<>();
                        map.put(TAG_RESELLERURL,c.getString(TAG_RESELLERURL));
                        map.put(TAG_RIMG, c.getString(TAG_RIMG));
                        map.put(TAG_RNAME, c.getString(TAG_RNAME));
                        map.put(TAG_RDESC, c.getString(TAG_RDESC));

                        offersList.add(map);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
