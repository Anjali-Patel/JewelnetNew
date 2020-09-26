package com.gss.jbc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.gss.jbc.Adapter.LikeAdapter;
import com.gss.jbc.Model.LikeModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AllLikeList extends AppCompatActivity {

    String userid,NewsIdNumber,NewsId;
    //AlphaAnimation inAnimation;
    //  AlphaAnimation outAnimation;
    //  FrameLayout progressBarHolder;


    String NewNewsId;

    ArrayList<LikeModel> CommentModelArrayList = new ArrayList<>();
    JSONObject json;

    Context context;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_like_list);

        mRecyclerView=findViewById(R.id.rv);
        toolbar_title=findViewById(R.id.toolbar_title);

        toolbar_title.setText("Likes On post");


        // userid = (getIntent().getStringExtra("user_id"));
        NewNewsId =String.valueOf(getIntent().getStringExtra("news_id"));
        // progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new LikeAdapter(AllLikeList.this, CommentModelArrayList);
        mRecyclerView.setAdapter(mAdapter);

        GetLikeList();
    }
    private void GetLikeList() {
        CommentModelArrayList.clear();
        String url= "http://jewelnet.in/index.php/Api/total_likes?news_id="+NewNewsId;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url).get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                final String myResponse = responseBody.string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(myResponse);
                            String status = json.getString("status");
                            if (status.equalsIgnoreCase("1")) {
                                JSONArray jsonArray ;
                                jsonArray = json.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject newJson = jsonArray.getJSONObject(i);
                                    LikeModel model = new LikeModel();
                                    model.setFname(newJson.getString("fname"));
                                    model.setCompany_name(newJson.getString("company_name"));
                                    CommentModelArrayList.add(model);
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(AllLikeList.this,ItemDisplay.class);
        startActivity(intent);
    }

}

