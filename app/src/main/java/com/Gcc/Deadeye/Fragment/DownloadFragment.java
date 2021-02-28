package com.Gcc.Deadeye.Fragment;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Gcc.Deadeye.AESUtils;
import com.Gcc.Deadeye.Adapter.DownloadAdapter;
import com.Gcc.Deadeye.GccConfig.urlref;
import com.Gcc.Deadeye.JSONParser;
import com.Gcc.Deadeye.R;
import com.Gcc.Deadeye.data.Download;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;

import static com.Gcc.Deadeye.GccConfig.urlref.TAG_ERROR;

public class DownloadFragment extends Fragment {
    private static final String TAG_ERROR = urlref.TAG_ERROR;
    private static final String TAG_TITLE = urlref.TAG_DTITLE;
    private static final String TAG_DESC = urlref.TAG_DDESC;
    private static final String TAG_IMG = urlref.TAG_DIMG;
    private static final String TAG_URL = urlref.TAG_DURL;






    Handler handler = new Handler();
    RecyclerView recyclerView;
    private ArrayList<HashMap<String, String>> offersListitem;
    private String title, descripton,downurl,img;
    private boolean error;
    JSONParser jsonParser = new JSONParser();
    JSONArray jsonArray = new JSONArray();
    private List<Download> item = new ArrayList<>();
    private DownloadAdapter downloadAdapter;
    final DialogFragment lottieDialog = new LottieDialogFragment().newInstance("loading.json",true);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Hashmap for ListView
        offersListitem = new ArrayList<>();

        new OneLoadAllProducts().execute();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        item = new ArrayList();
        downloadAdapter = new DownloadAdapter(getActivity(),item);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }

        View rootViewone = inflater.inflate(R.layout.fragment_download, container, false);
        recyclerView = rootViewone.findViewById(R.id.recyclerdownload);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //     initData();
        recyclerView.setAdapter(downloadAdapter);


        return rootViewone;
    }

    class OneLoadAllProducts extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lottieDialog.show(getActivity().getFragmentManager(), "Loadingdialog");
            lottieDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... strings) {

            HashMap<String, String> params = new HashMap<>();
            params.put("", "");

            JSONObject json = jsonParser.makeHttpRequest(urlref.Main + "download.php", params);
        //    Log.d("All jsonarray: ", json.toString());
            try {
                jsonArray = json.getJSONArray("18");
                error = Boolean.parseBoolean(AESUtils.DarKnight.getDecrypted(json.getString(TAG_ERROR)));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<>();
                    map.put(TAG_TITLE, c.getString(TAG_TITLE));
                    map.put(TAG_DESC, c.getString(TAG_DESC));
                    map.put(TAG_IMG,c.getString(TAG_IMG));
                    map.put(TAG_URL,c.getString(TAG_URL));
                    offersListitem.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    lottieDialog.dismiss();

                    if (!error) {
                        for (int i = 0; i < offersListitem.size(); i++) {

                            title = offersListitem.get(i).get(TAG_TITLE);
                            descripton = offersListitem.get(i).get(TAG_DESC);
                            img = offersListitem.get(i).get(TAG_IMG);
                            downurl = offersListitem.get(i).get(TAG_URL);
                    //          Log.d("title", title);
                            //  ;
                            Download download = new Download();
                            download.setTitle(title);
                            download.setDescription(descripton);
                            download.setImg(img);
                            download.setDownurl(downurl);
                            item.add(download);
                            downloadAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT);
                    }
                }
            },3000);


        }


    }

}
