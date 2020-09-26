package com.gss.jbc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gss.jbc.Extra.RestJsonClient;
import com.gss.jbc.Model.MypostDataModel;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Edit_user_post extends AppCompatActivity {

    String PostId;
    ArrayList<MypostDataModel> MyPostModelArrayList = new ArrayList<>();


    TextView post;
    TextView title;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView toolbar_title1, toolbar_title;
    ImageView menuButton;
    TextView optionButton;
    boolean isFragmentLoaded;
    Fragment menuFragment;

    ProgressBar postactivity_progress;

    ImageView PostImage, drawableforplay;
    EditText Posttitle, message;
    Button submit;

    String userid, imageurl = null, urlvideo;
    private static final int SELECT_VIDEO = 2;
    ImageView img, video;
    String FileType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_post);

        final android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
        userid=sharedPreferences.getString("userid","");
        ImageView GotoTop = findViewById(R.id.GotoTop);
        GotoTop.setVisibility(View.GONE);
        post = findViewById(R.id.post);
        post.setVisibility(View.INVISIBLE);
        title=(TextView) findViewById(R.id.toolbar_title);
        title.setText("Edit Posts");

        PostImage = (ImageView) findViewById(R.id.PostImage);
        drawableforplay = (ImageView) findViewById(R.id.drawableforplay);
        Posttitle = (EditText) findViewById(R.id.Posttitle);
        message = (EditText) findViewById(R.id.message);
        img = findViewById(R.id.id_post_image);
        video = findViewById(R.id.id_post_video);
        postactivity_progress = findViewById(R.id.postactivity_progress);
        submit = findViewById(R.id.submit);


        menuButton = (ImageView) findViewById(R.id.menu_icon);
        optionButton = (TextView) findViewById(R.id.menu_icon_option);

        PostId = (getIntent().getStringExtra("post_id"));

        getMyPost();

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //loadFragment();
                Intent i = new Intent(Edit_user_post.this, MenuActivity.class);
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(Edit_user_post.this, "Please enter the Title", Toast.LENGTH_SHORT).show();
                    title.setText("");
                } else if (post.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(Edit_user_post.this, "Please enter the Description", Toast.LENGTH_SHORT).show();
                    post.setText("");
                } else {
                    new api().execute(PostId, Posttitle.getText().toString(), message.getText().toString(), imageurl);
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


    public void getMyPost() {
        postactivity_progress.setVisibility(View.VISIBLE);
        String url = "http://jewelnet.in/index.php/Api/get_data_for_edit";

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("news_id", PostId)
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

                            String success = json.getString("response");

                            if (success.equalsIgnoreCase("success")) {
                                JSONArray post = json.getJSONArray("message");

                                for (int i = 0; i < post.length(); i++) {
                                    final JSONObject newJson = post.getJSONObject(i);

                                    //String base = "http://demo1.geniesoftsystem.com/newweb/jewelnetnewone/assets/upload/update/";

                                    final String imageurl = "http://jewelnet.in/assets/upload/update" + "/" + newJson.getString("file_name");



                                    if(newJson.getString("file_type").equalsIgnoreCase("img")){
                                        String image_file_path = "http://jewelnet.in/assets/upload/update" + "/" + newJson.getString("file_name").replaceAll(" ","%20");
                                        Picasso.with(Edit_user_post.this).load(image_file_path).into(PostImage);
                                    }
                                    else{
                                        drawableforplay.setVisibility(View.VISIBLE);

                                        String image_file_path = "http://jewelnet.in/assets/upload/update" + "/" + newJson.getString("file_name")+ ".png";
                                        Picasso.with(Edit_user_post.this).load(image_file_path).into(PostImage);

                                        drawableforplay.setImageDrawable(Edit_user_post.this.getResources()
                                                .getDrawable(R.drawable.ic_play_circle_outline_red_24dp));

                                        PostImage.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i = new Intent(Edit_user_post.this, VideoWebViewIframe.class);
                                                i.putExtra("url", imageurl);
                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(i);
                                            }
                                        });

                                    }

                                    Posttitle.setText(newJson.getString("title"));
                                    message.setText(newJson.getString("message"));



                                }
                                postactivity_progress.setVisibility(View.GONE);
                            }
                            else{
                                postactivity_progress.setVisibility(View.GONE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isReadStoragePermissionGranted() && isWriteStoragePermissionGranted()) {
                    selectImage();
                } else if (!isReadStoragePermissionGranted() && !isWriteStoragePermissionGranted()) {
                    Toast.makeText(Edit_user_post.this, "Please give permission first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isReadStoragePermissionGranted() && isWriteStoragePermissionGranted()) {
                    selectVideo();
                } else if (!isReadStoragePermissionGranted() && !isWriteStoragePermissionGranted()) {
                    Toast.makeText(Edit_user_post.this, "Please give permission first", Toast.LENGTH_SHORT).show();
                }
            }
        });


//        http://demo1.geniesoftsystem.com/newweb/jewelnetnewone/index.php/Apivi/edit_news
//
//        post_id:4201
//        title:hello
//        message:demo
//        file_type:mp4


    }

    private void selectImage() {
        /*
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(i, SELECT_IMAGE);*/
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    private void selectVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
    }

    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data) {
        if (data != null) {
            if (requestcode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();
//                        imageurl = getPath(resultUri);
                img.setImageURI(resultUri);
                imageurl = resultUri.getPath();
                FileType = "img";
                Log.e("path", resultUri.getPath());

//                else if (requestcode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                    Exception error = result.getError();
            } else if (requestcode == 2) {
                Uri selectedImageUri = data.getData();
                imageurl = getPath(selectedImageUri);
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(imageurl, MediaStore.Video.Thumbnails.MINI_KIND);
                video.setImageBitmap(thumb);
                FileType = "video";

            }
        }
    }

    public String getPath(Uri uri) {

        Cursor cursor = getContentResolver().query(uri, null, null,
                null, null);
        String path = "";

        if (cursor.getCount() > 0)
            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst();
                String document_id = cursor.getString(0);
                document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
                cursor.close();

                cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
                cursor.moveToFirst();
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                cursor.close();
            }
        return path;
    }

    class api extends AsyncTask<String, String, JSONObject> {

        JSONObject json;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            postactivity_progress.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                String url = "http://jewelnet.in/index.php/Api/edit_news";
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("post_id", params[0]));
                nameValuePairs.add(new BasicNameValuePair("title", params[1]));
                if (!params[2].equals(""))
                    nameValuePairs.add(new BasicNameValuePair("message", params[2]));
                if (imageurl != null)
                    nameValuePairs.add(new BasicNameValuePair("file_type", FileType));
                nameValuePairs.add(new BasicNameValuePair("file_name", imageurl));

                Log.d("data", nameValuePairs.toString());
                json = RestJsonClient.post2(url, nameValuePairs);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        protected void onPostExecute(JSONObject params) {
            super.onPostExecute(params);


            String success = "";
            try {
                success = json.getString("response");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            postactivity_progress.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            if (success.equalsIgnoreCase("success")) {

                Toast.makeText(Edit_user_post.this, "Post saved successfully!",
                        Toast.LENGTH_LONG).show();

                Intent i = new Intent(getApplicationContext(), MyPosts.class);
                startActivity(i);
                finish();

            }

            else if (success.equalsIgnoreCase("failure")){

                Toast.makeText(Edit_user_post.this, "Opps! Some problem occured while sending your data, please try again after some time.",
                        Toast.LENGTH_LONG).show();
            }

//                PostActivity.super.onBackPressed();
        }
    }

    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("", "Permission is granted1");
                return true;
            } else {

                Log.v("", "Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("", "Permission is granted1");
            return true;
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("", "Permission is granted2");
                return true;
            } else {

                Log.v("", "Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("", "Permission is granted2");
            return true;
        }
    }

}
