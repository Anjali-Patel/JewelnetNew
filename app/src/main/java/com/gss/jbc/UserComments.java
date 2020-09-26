package com.gss.jbc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gss.jbc.Adapter.CommentsAdapter;
import com.gss.jbc.Extra.RestJsonClient;
import com.gss.jbc.Model.CommentsDataModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class UserComments extends AppCompatActivity {

    LinearLayout PostSlide;
    JSONObject json;
    String userid;
    SharedPreferences sharedPreferences;
    ArrayList<CommentsDataModel> CommentModelArrayList = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    TextView toolbar_title1, toolbar_title;
    ImageView menuButton;
    TextView optionButton;
    boolean isFragmentLoaded;
    Fragment menuFragment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_comments);

        PostSlide = (LinearLayout) findViewById(R.id.postslideLayout);
        sharedPreferences = this.getSharedPreferences("ID", Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "");

        mRecyclerView = findViewById(R.id.comments_listview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new CommentsAdapter(this, CommentModelArrayList);
        mRecyclerView.setAdapter(mAdapter);

        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Comments");
        sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "");
        menuButton = (ImageView) findViewById(R.id.menu_icon);
        optionButton = (TextView) findViewById(R.id.menu_icon_option);

        getComments();


        PostSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = "";
                try {
                    json = new post_status().execute().get();
                    status = json.getString("status");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (status.equalsIgnoreCase("1")) {
                    Intent i = new Intent(UserComments.this, PostActivity.class);
                    startActivity(i);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserComments.this);
                    builder.setMessage("You need to subscribe for this services. Contact Vikas Chudasama at +91-9324254765.");
                    builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String uri = "tel:9324254765";
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse(uri));
                            if (ActivityCompat.checkSelfPermission(UserComments.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(UserComments.this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                            }
                            else
                                startActivity(intent);
//                            Intent mintent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:9324254765"));
//                            startActivity(mintent);
                        }
                    });
                    AlertDialog alertdialog = builder.create();
                    alertdialog.show();
                }

            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //loadFragment();
                Intent i = new Intent(UserComments.this, MenuActivity.class);
                startActivity(i);

            }
        });

        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isFragmentLoaded) {
                    loadFragment();
                }
                else {
                    if (menuFragment != null) {
                        if (menuFragment.isAdded()) {
                            hideFragment();
                        }
                    }
                }


            }
        });


    }


    public void loadFragment(){
        FragmentManager fm = getSupportFragmentManager();
        menuFragment = fm.findFragmentById(R.id.mainview);
        if(menuFragment == null){
            menuFragment = new MenuFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_down, R.anim.slide_up);
            fragmentTransaction.add(R.id.mainview,menuFragment);
            fragmentTransaction.commit();
        }

        isFragmentLoaded = true;
    }

    public void hideFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_down, R.anim.slide_up);
        fragmentTransaction.remove(menuFragment);
        fragmentTransaction.commit();
        isFragmentLoaded = false;
    }

    private class post_status extends AsyncTask<String, String, JSONObject> {

        String http = Constant.BASE_URL+"getuserdetail?userid=" + userid;

        @Override
        protected JSONObject doInBackground(String... url) {

            try {
                json = RestJsonClient.connect(http);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        protected void onPostExecute(JSONObject params) {
            super.onPostExecute(params);
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
                    model.setComment_id(newJson.getString("comment_id"));
                    model.setCommentor_id(newJson.getString("commentor_id"));
                    model.setNews_id(newJson.getString("news_id"));
                    model.setComment(newJson.getString("comment"));
                    model.setTitle(newJson.getString("title"));
                    model.setNews_description(newJson.getString("news_description"));
                    model.setFname(newJson.getString("fname"));
                    model.setNews_file_name(newJson.getString("news_file_name"));

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
                String url= "http://jewelnet.in/index.php/Api/get_last_comment?user_id="+userid;
                json = RestJsonClient.connect(url);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }
    }

    public void refreshAdapter(){
        getComments();
    }
}
