package com.gss.jbc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gss.jbc.Adapter.AllCommentsAdapter;
import com.gss.jbc.Model.CommentsDataModel;
import com.gss.jbc.webrequest.RestJsonClient;


import com.gss.jbc.Utility.CommonUtils;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CommentOnPost extends AppCompatActivity {

    private ImageView commentPic, postCommentButton;
    TextView textView, description;
    String ImgShare, userid;
    private  String url="http://jewelnet.in/index.php/Api/add_comment";
    EditText usercomment;
    int NewsIdNumber;
    private String NewNewsId;
    ArrayList<CommentsDataModel> CommentModelArrayList = new ArrayList<>();
    JSONObject json;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    ProgressBar progress_load;

    TextView toolbar_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_on_post);

        textView = findViewById(R.id.textView);
        description = findViewById(R.id.description);
        commentPic = findViewById(R.id.commentPic);
        postCommentButton = findViewById(R.id.postCommentButton);
        usercomment = findViewById(R.id.usercomment);
        progress_load = findViewById(R.id.progress_load);


        toolbar_title=findViewById(R.id.toolbar_title);
        toolbar_title.setText("Comments On post");

        textView.setText(getIntent().getStringExtra("textView"));
        description.setText(getIntent().getStringExtra("description"));
        NewNewsId = String.valueOf((getIntent().getStringExtra("NewNewsId")));

        //NewsIdNumber = (getIntent().getStringExtra("NewsIdNumber"));
        userid = (getIntent().getStringExtra("userid"));
        ImgShare = (getIntent().getStringExtra("ImgShare"));

        Picasso.with(new CommentOnPost()).load(ImgShare).into(commentPic);

        mRecyclerView = findViewById(R.id.allcomments_listview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AllCommentsAdapter(CommentOnPost.this, CommentModelArrayList);
        mRecyclerView.setAdapter(mAdapter);
        getComments();


        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (description.getText().toString().trim() == null ||  description.getText().toString().trim().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CommentOnPost.this);
                    builder.create();
                    builder.setMessage("Plese enter Comment!");

                    builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }

                else {
                    SendComment(userid, NewNewsId,usercomment.getText().toString());

                    //new PostCommentAsync().execute(userid, NewNewsId,usercomment.getText().toString());

                }



            }
        });


    }


    class PostCommentAsync extends AsyncTask<String, String, JSONObject> {
        JSONObject json;


        @Override
        protected JSONObject doInBackground(String... params) {
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("user_id", params[0]));
                nameValuePairs.add(new BasicNameValuePair("news_id", params[1]));
                nameValuePairs.add(new BasicNameValuePair("description", params[2]));

                Log.d("datap", nameValuePairs.toString());
                json = RestJsonClient.postAdv(url, nameValuePairs);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            super.onPostExecute(jsonObject);
            String success = "";
            try {
                success = json.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (success.equalsIgnoreCase("1")) {

                Toast.makeText(CommentOnPost.this, "Comment posted successfully!",
                        Toast.LENGTH_LONG).show();

                Intent i = new Intent(CommentOnPost.this, UserComments.class);
                startActivity(i);
                finish();

            }

            else {

                Toast.makeText(CommentOnPost.this, "Opps! Some problem occured while sending your comment, please try again after some time.",
                        Toast.LENGTH_LONG).show();
            }

        }
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
                String url= "http://jewelnet.in/index.php/Api/my_news_comments?post_id="+NewNewsId;
                json = com.gss.jbc.Extra.RestJsonClient.connect(url);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, ItemDisplay.class);
        startActivity(i);
    }


    public void SendComment(String UserId, String MemberId, String Decription) {

        progress_load.setVisibility(View.VISIBLE);

       String url="http://jewelnet.in/index.php/Api/add_comment";

        OkHttpClient client = new OkHttpClient();


        RequestBody formBody = new FormBody.Builder()
                .add("user_id", UserId)
                .add("news_id", MemberId)
                .add("description", Decription)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();



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
                        JSONObject json = null;
                        try {
                            json = new JSONObject(myResponse);

                            String success = json.getString("status");

                            if (success.equalsIgnoreCase("1")) {

                                progress_load.setVisibility(View.GONE);

                                Toast.makeText(CommentOnPost.this, "Comment posted successfully!",
                                        Toast.LENGTH_LONG).show();

                                Intent i = new Intent(CommentOnPost.this, UserComments.class);
                                startActivity(i);
                                finish();

                            }

                            else {

                                progress_load.setVisibility(View.GONE);

                                Toast.makeText(CommentOnPost.this, "Opps! Some problem occured while sending your comment, please try again after some time.",
                                        Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });




    }
}
