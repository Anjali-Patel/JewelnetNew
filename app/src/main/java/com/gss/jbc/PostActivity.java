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
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gss.jbc.Extra.RestJsonClient;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PostActivity extends AppCompatActivity {

    TextView title;
    ProgressBar postactivity_progress;
    ImageView img, video;
    JSONObject json;
    Button submit, mypost;
    Toolbar toolbar;
    String userid, imageurl = null, urlvideo;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    String selectedPath;
    private static final int SELECT_VIDEO = 2;
    private static final int SELECT_IMAGE = 203;
    String url = Constant.BASE_URL + "updatenews";
    LinearLayout CommentSlide;
    EditText post;

    TextView toolbar_title1, toolbar_title;
    ImageView menuButton;
    TextView optionButton;
    boolean isFragmentLoaded;
    Fragment menuFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        //toolbar = findViewById(R.id.toolbar1);
        TextView toolbar_title1 = findViewById(R.id.toolbar_title1);

        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //toolbar_title1.setText(R.string.jewelnetpost);
        context = this;
        title = findViewById(R.id.title);
        post = findViewById(R.id.message);
        img = findViewById(R.id.id_post_image);
        video = findViewById(R.id.id_post_video);
        submit = findViewById(R.id.submit);
        mypost = findViewById(R.id.mypost);
        CommentSlide = (LinearLayout) findViewById(R.id.commentLayout);
        postactivity_progress = findViewById(R.id.postactivity_progress);
        sharedPreferences = getSharedPreferences("ID", MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "id");

        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Post");
        sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "");
        menuButton = (ImageView) findViewById(R.id.menu_icon);
        optionButton = (TextView) findViewById(R.id.menu_icon_option);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //loadFragment();
                Intent i = new Intent(PostActivity.this, MenuActivity.class);
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

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isReadStoragePermissionGranted() && isWriteStoragePermissionGranted()) {
                    selectImage();
                } else if (!isReadStoragePermissionGranted() && !isWriteStoragePermissionGranted()) {
                    Toast.makeText(context, "Please give permission first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isReadStoragePermissionGranted() && isWriteStoragePermissionGranted()) {
                    selectVideo();
                } else if (!isReadStoragePermissionGranted() && !isWriteStoragePermissionGranted()) {
                    Toast.makeText(context, "Please give permission first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(PostActivity.this, "Please enter the Title", Toast.LENGTH_SHORT).show();
                    title.setText("");
                } else if (post.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(PostActivity.this, "Please enter the Description", Toast.LENGTH_SHORT).show();
                    post.setText("");
                } else if (imageurl == null) {
                    Toast.makeText(PostActivity.this, "Please Select Image or Video", Toast.LENGTH_SHORT).show();
                } else {
                    new api().execute(userid, title.getText().toString(), post.getText().toString(), imageurl);
                }
            }
        });

        CommentSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostActivity.this, UserComments.class);
                startActivity(i);
            }
        });

        mypost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostActivity.this, MyPosts.class);
                startActivity(i);
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
                Log.e("path", resultUri.getPath());

//                else if (requestcode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                    Exception error = result.getError();
            } else if (requestcode == 2) {
                Uri selectedImageUri = data.getData();
                imageurl = getPath(selectedImageUri);
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(imageurl, MediaStore.Video.Thumbnails.MINI_KIND);
                video.setImageBitmap(thumb);
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
                String url = Constant.BASE_URL + "updatenews";
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("userid", params[0]));
                nameValuePairs.add(new BasicNameValuePair("title", params[1]));
                if (!params[2].equals(""))
                    nameValuePairs.add(new BasicNameValuePair("message", params[2]));
                if (imageurl != null)
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
            postactivity_progress.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Intent i = new Intent(getApplicationContext(), ItemDisplay.class);
            startActivity(i);
            finish();
//                PostActivity.super.onBackPressed();
        }
    }

    public void myFunction(String imageUrlZoom) {
        /*
                File file = new File(imageurl);
                Bitmap bitmap = BitmapFactory.decodeFile(imageurl);
                img.setImageBitmap(bitmap);
*/
        Intent intZoomImage = new Intent(this, ZoomGalleryImageActivity.class);
        intZoomImage.putExtra("Img", imageUrlZoom);

        startActivityForResult(intZoomImage, 3);
    }

    /*
        public Bitmap convertStringToBitMap(String encodedString) {
            try {
                byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return bitmap;
            } catch (Exception e) {
                e.getMessage();
                return null;
            }
        }
    */
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int x = item.getItemId();

        switch (item.getItemId()) {

            case R.id.back:
                Intent i = new Intent(getApplicationContext(), ItemDisplay.class);
                startActivity(i);
                finish();
                break;
            case R.id.homeAsUp:
                Intent j = new Intent(getApplicationContext(), ItemDisplay.class);
                startActivity(j);
                finish();
                break;
            case android.R.id.home:
                Intent k = new Intent(getApplicationContext(), ItemDisplay.class);
                startActivity(k);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent k = new Intent(getApplicationContext(), ItemDisplay.class);
        startActivity(k);
        finish();
    }
}
