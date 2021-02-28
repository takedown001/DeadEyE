package com.Gcc.Deadeye.Fragment;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.Gcc.Deadeye.AESUtils;
import com.Gcc.Deadeye.Adapter.NewsAdapter;
import com.Gcc.Deadeye.data.News;
import com.Gcc.Deadeye.GccConfig.urlref;
import com.Gcc.Deadeye.JSONParser;
import com.Gcc.Deadeye.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;

import static com.Gcc.Deadeye.GccConfig.urlref.TAG_ERROR;


public class NewsFragment extends Fragment {


    public NewsFragment() {
        // Required empty public constructor
    }

    private ArrayList<HashMap<String, String>> offersListNews;
    String title, descrion;
    RecyclerView recyclerView;
    JSONArray jsonArray = new JSONArray();
    private boolean error;
    JSONParser jsonParser = new JSONParser();
    Handler handler = new Handler();
    private List<News> itemList = new ArrayList<>();
    private NewsAdapter itemAdapter;
    final DialogFragment lottieDialog = new LottieDialogFragment().newInstance("loading.json",true);
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Hashmap for ListView
        offersListNews = new ArrayList<>();

        new OneLoadAllProducts().execute();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        itemList = new ArrayList();
        itemAdapter = new NewsAdapter(getActivity(),itemList);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }

        View rootViewone = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = rootViewone.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //     initData();
        recyclerView.setAdapter(itemAdapter);

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

            JSONObject json = jsonParser.makeHttpRequest(urlref.Main + "news.php", params);
            try {
                jsonArray = json.getJSONArray("19");
                error = Boolean.parseBoolean(AESUtils.DarKnight.getDecrypted(json.getString(TAG_ERROR)));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("title", c.getString("title"));
                    map.put("description", c.getString("description"));

                    offersListNews.add(map);
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
                        for (int i = 0; i < offersListNews.size(); i++) {
                            title = offersListNews.get(i).get("title");
                            descrion = offersListNews.get(i).get("description");
                            //  Log.d("title", title);
                            News newsAdapter = new News();
                            newsAdapter.setTitle(title);
                            newsAdapter.setDescription(descrion);
                            itemList.add(newsAdapter);
                            itemAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            },3000);


        }


    }
}