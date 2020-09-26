package com.gss.jbc;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gss.jbc.Adapter.AllCommentsAdapter;
import com.gss.jbc.Adapter.CommentsAdapter;
import com.gss.jbc.Extra.RestJsonClient;
import com.gss.jbc.Model.CommentsDataModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DetailedComment extends AppCompatActivity {

    TextView CommentTitle, CommentDescription;
    String title, description, post_id, image_file_path;
    private ImageView CommentDetailPic;
    ArrayList<CommentsDataModel> CommentModelArrayList = new ArrayList<>();
    JSONObject json;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_comment);

        CommentTitle = findViewById(R.id.CommentTitle);
        CommentDescription = findViewById(R.id.CommentDescription);
        CommentDetailPic = findViewById(R.id.CommentDetailPic);

        post_id = (getIntent().getStringExtra("post_id"));
        image_file_path = (getIntent().getStringExtra("image_file_path"));

        CommentTitle.setText(getIntent().getStringExtra("title"));
        CommentDescription.setText(getIntent().getStringExtra("description"));
        Picasso.with(new CommentOnPost()).load(image_file_path).into(CommentDetailPic);

        mRecyclerView = findViewById(R.id.allcomments_listview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AllCommentsAdapter(DetailedComment.this, CommentModelArrayList);
        mRecyclerView.setAdapter(mAdapter);
        getComments();


    }


    public void getComments() {

        CommentModelArrayList.clear();
        try {


            json = new CommentFetchAsynk().execute().get();
            String success = json.getString("status");
            if (success.equalsIgnoreCase("1")) {
                JSONArray jsonArray = null;
                jsonArray = json.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject newJson = jsonArray.getJSONObject(i);
                    CommentsDataModel model = new CommentsDataModel();
                    model.setFname(newJson.getString("fname"));
                    model.setComment(newJson.getString("comment"));
                    CommentModelArrayList.add(model);
                }

                mAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public class CommentFetchAsynk extends AsyncTask<String, String, JSONObject> {
        private JSONObject jsonResponse;

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                String url= "http://jewelnet.in/index.php/Api/my_news_comments?post_id="+post_id;
                json = RestJsonClient.connect(url);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }
    }
}
