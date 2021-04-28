package mobisocial.arcade;

import android.animation.ArgbEvaluator;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import mobisocial.arcade.Adapter.StoreAdapter;
import mobisocial.arcade.GccConfig.urlref;
import mobisocial.arcade.data.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;

public class StoreActivity extends AppCompatActivity {

    ViewPager viewPager;
    StoreAdapter adapter;
    private String deviceid;
    //user

    private static final String TAG_DEVICEID = urlref.TAG_DEVICEID;
    private static final String TAG_SUCCESS = urlref.TAG_SUCCESS;
    private static final String TAG_STORE = urlref.TAG_STORE;
    private final JSONParser jsonParser = new JSONParser();
    //plan
    private static final String TAG_TITLE = urlref.TAG_TITLE;
    private static final String TAG_DESC = urlref.TAG_DESC;
    private static final String TAG_IMG = urlref.TAG_IMG;
    private static final String TAG_KEY = urlref.TAG_KEY;
    private static final String url = urlref.Main +"store.php";
    private JSONArray jsonarray = null;
    private ArrayList<HashMap<String, String>> offersList;
    private int success;
    private List<Model> Model = new ArrayList<>();

    Integer[] colors = null;

    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    final DialogFragment lottieDialog = new LottieDialogFragment().newInstance("loadingdone.json",true);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        offersList = new ArrayList<>();
        lottieDialog.setCancelable(false);
        Model = new ArrayList<>();

        //  models.add(new Model(R.drawable.namecard, "", "Business cards are cards bearing business information about a company or individual."));

        adapter = new StoreAdapter(Model, this);
                  if(Helper.checkVPN(StoreActivity.this)){
                Toast.makeText(StoreActivity.this, "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                finish();
            }else {
        new OneLoadAllProducts().execute();
                  }
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
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
        protected void onPostExecute(String file_url) {
            lottieDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (success == 1) {

                        for (int i = 0; i < offersList.size(); i++) {
                            Model store = new Model();
                            store.setTitle(offersList.get(i).get(TAG_TITLE));
                            store.setDesc(offersList.get(i).get(TAG_DESC));
                            store.setImage(offersList.get(i).get(TAG_IMG));


                            Model.add(store);
                            adapter.notifyDataSetChanged();
                        }
                    } else {

                        Toast.makeText(StoreActivity.this, "SomeThing Went Wrong", Toast.LENGTH_LONG).show();

                    }
                }


            });
        }






        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            deviceid = AESUtils.DarKnight.getEncrypted(LoginActivity.getDeviceId(StoreActivity.this));
            lottieDialog.show(getFragmentManager(),"lol");
        }

        @Override
        protected String doInBackground(String... strings) {

            HashMap<String, String> params = new HashMap<>();
            params.put(TAG_DEVICEID, deviceid);
         //   Log.d("All jsonarray: ", TAG_DEVICEID);
            JSONObject json = jsonParser.makeHttpRequest(url, params);

        //   Log.d("All jsonarray: ", json.toString());
            try {
                success = Integer.parseInt(AESUtils.DarKnight.getDecrypted(json.getString(TAG_SUCCESS)));
             // Log.d("test", String.valueOf(success));
                jsonarray =json.getJSONArray(TAG_STORE);


                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject c = jsonarray.getJSONObject(i);

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<>();

                    map.put(TAG_IMG, c.getString(TAG_IMG));
                    map.put(TAG_TITLE, c.getString(TAG_TITLE));
                    map.put(TAG_DESC, c.getString(TAG_DESC));

                    offersList.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
