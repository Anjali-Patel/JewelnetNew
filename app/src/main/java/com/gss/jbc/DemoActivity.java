package com.gss.jbc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gss.jbc.Adapter.NewsAdapter;
import com.gss.jbc.Extra.RestJsonClient;
import com.gss.jbc.Utility.SharedPreferenceUtils;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DemoActivity extends AppCompatActivity {

    ArrayList<NewsItemStructure> NewsItems = new ArrayList<NewsItemStructure>();

    String profile_base_path, news_base_path, userid, advertisement_base_path,newsid, newsviewcount;
    String NewsId;
    SharedPreferences sharedpreferences;
    private SharedPreferenceUtils preferences;
    private String memberId;
    JSONObject json, countjson;
    JSONArray json2;
    public int counter = 0;

    RecyclerView.LayoutManager layoutmanager;
    RecyclerView newsDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        preferences = SharedPreferenceUtils.getInstance(getApplicationContext());
        sharedpreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
        userid = sharedpreferences.getString("userid", "");

        newsDetails= findViewById(R.id.newsDetails);


        getNewsDetails();

        NewsAdapter adapter = new NewsAdapter(this,NewsItems);
        layoutmanager = new LinearLayoutManager(this);
        newsDetails.setLayoutManager(layoutmanager);
        newsDetails.setAdapter(adapter);

    }


    public void getNewsDetails() {


        NewsItems.clear();

        try {

            String page = URLEncoder.encode("{\"page\":" + "\"" + counter + "\","+
                    "\"userid\":" + "\"" + userid + "\"}");

            json = new GetNewsDataAsynk().execute(page).get();

            profile_base_path = json.getString("profile_base_path");
            news_base_path = json.getString("news_base_path");
            advertisement_base_path = json.getString("advertisement_base_path");
            json2 = json.getJSONArray("newsdata");
            String success = json.getString("response");
            if (success.equalsIgnoreCase("success")) {


                for (int i = 0; i < json2.length(); i++) {

                    boolean isAdvertise = false;
                    JSONObject jsonTemp = json2.getJSONObject(i);
                    if (jsonTemp.getString("file_type").equalsIgnoreCase("adv")) {
                        isAdvertise = true;
                    }

                    NewsId = jsonTemp.getString("id");
                    NewsItems.add(new NewsItemStructure(jsonTemp, news_base_path, profile_base_path, advertisement_base_path, isAdvertise, newsviewcount));

                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


    public class GetNewsDataAsynk extends AsyncTask<String, String, JSONObject> {
        private JSONObject jsonResponse;

        @Override
        protected JSONObject doInBackground(String... urlInfo) {
            try {

                String urlString = Constant.BASE_URL+"getnews?data=" +urlInfo[0];
                jsonResponse = RestJsonClient.connect(urlString);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonResponse;
        }
    }

}
