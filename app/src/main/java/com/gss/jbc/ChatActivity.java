package com.gss.jbc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gss.jbc.Adapter.AllCommentsAdapter;
import com.gss.jbc.Adapter.ChatAdapter;
import com.gss.jbc.Model.ChatModal;
import com.gss.jbc.Model.CommentsDataModel;
import com.gss.jbc.webrequest.RestJsonClient;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ChatActivity extends AppCompatActivity {
    Context context;
    SharedPreferences sharedPreferences;
    private ImageView commentPic, postCommentButton;
    TextView textView ;//description;
    String msg, userid;
    private  String url="http://104.236.14.235/index.php/ChatApi/insertChat";
    EditText usercomment;
    // int NewsIdNumber;
   // private String NewNewsId;
    ArrayList<ChatModal> CommentModelArrayList = new ArrayList<>();
    JSONObject json;
    private ChatAdapter mAdapter;
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
        setContentView(R.layout.activity_chat);
        textView = findViewById(R.id.textView);
        //commentPic = findViewById(R.id.commentPic);
        postCommentButton = findViewById(R.id.postCommentButton);
        usercomment = findViewById(R.id.usercomment);

        usercomment.setSelection(0);
//        textView.setText(getIntent().getStringExtra("textView"));
        // description.setText(getIntent().getStringExtra("description"));
      // NewNewsId = String.valueOf((getIntent().getStringExtra("NewNewsId")));

        //NewsIdNumber = (getIntent().getStringExtra("NewsIdNumber"));


       // userid = (getIntent().getStringExtra("senderid"));
        sharedPreferences = getSharedPreferences("ID", MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "id");

        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Chat");
        sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "");
        menuButton = (ImageView) findViewById(R.id.menu_icon);
        optionButton = (TextView) findViewById(R.id.menu_icon_option);

       // ImgShare = (getIntent().getStringExtra("ImgShare"));

//        Picasso.with(new CommentOnPost()).load(ImgShare).into(commentPic);

        mRecyclerView = findViewById(R.id.chatlist);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ChatAdapter(ChatActivity.this, CommentModelArrayList);
        mRecyclerView.setAdapter(mAdapter);

        getComments();


        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usercomment.getText().toString().trim() == null || usercomment.getText().toString().trim().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                    builder.create();
                    builder.setMessage("Please enter some message!");

                    builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }

                else {

                    new PostCommentAsync().execute(userid,usercomment.getText().toString());

                }



            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //loadFragment();
                Intent i = new Intent(ChatActivity.this, MenuActivity.class);
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


    class PostCommentAsync extends AsyncTask<String, String, JSONObject> {
        JSONObject json;


        @Override
        protected JSONObject doInBackground(String... params) {
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("senderid", params[0]));
                nameValuePairs.add(new BasicNameValuePair("msg", params[1]));

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
            Log.d("PostCommentAsync", jsonObject.toString());

//            AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
//            builder.create();
//            builder.setMessage("Message Sent!");
//
//            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
////                    dialog.dismiss();
//                    Intent i = new Intent(ChatActivity.this, ChatActivity.class);
//                    startActivity(i);
//                }
//            });
//            builder.show();
//            usercomment.setText("");

            super.onPostExecute(jsonObject);
            usercomment.setText("");
            getComments();

        }
    }



//    public void getComments() {
//
//      CommentModelArrayList.clear();
//        try {
//
//
//            json = new ChatActivity.CommentFetchAsynk().execute().get();
//            String success = json.getString("response");
//            if (success.equalsIgnoreCase("success")) {
//                JSONArray jsonArray = null;
//                jsonArray = json.getJSONArray("data");
//                for (int i = 0; i < jsonArray.length(); i++) {
//
//                    JSONObject newJson = jsonArray.getJSONObject(i);
//                   ChatModal model = new ChatModal();
//                    model.setFname(newJson.getString("fname"));
//                    model.setMsg(newJson.getString("msg"));
//                    model.setProfile_picture(newJson.getString("profile_picture"));
//                    model.setBaseUrl(Constant.Base_Url_ProfilePic+newJson.getString("senderid")+"/"+newJson.getString("profile_picture"));
//
//                    CommentModelArrayList.add(model);
//                }
//
//                mAdapter.notifyDataSetChanged();
//                mRecyclerView.scrollToPosition(CommentModelArrayList.size()-1);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public class CommentFetchAsynk extends AsyncTask<String, String, JSONObject> {
//        private JSONObject jsonResponse;
//
//        @Override
//        protected JSONObject doInBackground(String... params) {
//            try {
//
//                json = com.gss.jbc.Extra.RestJsonClient.connect(Constant.getAllChat);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return json;
//        }
//    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, ItemDisplay.class);
        startActivity(i);
    }

    private void getComments() {


        CommentModelArrayList.clear();


        String finalUrl = Constant.getAllChat;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(finalUrl).get().build();
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
                            String success = json.getString("response");

                            if (success.equalsIgnoreCase("success")) {
                                JSONArray jsonArray = null;
                                jsonArray = json.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject newJson = jsonArray.getJSONObject(i);
                                    ChatModal model = new ChatModal();
                                    model.setFname(newJson.getString("fname"));
                                    model.setMsg(newJson.getString("msg"));
                                    model.setProfile_picture(newJson.getString("profile_picture"));
                                    model.setBaseUrl(Constant.Base_Url_ProfilePic+newJson.getString("senderid")+"/"+newJson.getString("profile_picture"));

                                    CommentModelArrayList.add(model);
                                }

                                mAdapter.notifyDataSetChanged();
                                mRecyclerView.scrollToPosition(CommentModelArrayList.size()-1);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


    }

}
