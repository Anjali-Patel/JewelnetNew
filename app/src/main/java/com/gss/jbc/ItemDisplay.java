package com.gss.jbc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gss.jbc.Extra.RestJsonClient;
import com.gss.jbc.Utility.CommonUtils;
import com.gss.jbc.Utility.SharedPreferenceUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class ItemDisplay extends AppCompatActivity {
    private static long back_pressed;
    public int counter = 0, add = 1;
    boolean showingLastPage = false;
    DrawerLayout drawer;
    int positionofPage, Positioncompare = 1, PositionNow;
    VerticalViewPager verticalViewPager;
    RoundedImageView profile;
    TextView post, toolbar_title;
    private MediaPlayer mp;
    ArrayList<NewsItemStructure> newsItems = new ArrayList<NewsItemStructure>();
    ArrayList<NewsCountData> ViewsCount = new ArrayList<NewsCountData>();
    JSONObject json, countjson;
    JSONArray json2;
    int len;
    String profile_base_path, news_base_path, userid, advertisement_base_path,newsid, newsviewcount;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isNewsFetched = false;
    VerticalPageAdapter pageAdapter;
    ImageView GotoTop;
    String fcmtoken, status,NewsId;
    ImageView menuButton;
    //ImageView optionButton;
    TextView optionButton;
    Fragment menuFragment;
    TextView NewsCount;
    boolean isFragmentLoaded;
    private JSONObject jsonCount;
    private SharedPreferenceUtils preferences;
    private FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        newsItems.clear();
        mp = new MediaPlayer();
        GotoTop = findViewById(R.id.GotoTop);
        post = findViewById(R.id.post);
        menuButton = (ImageView) findViewById(R.id.menu_icon);
        optionButton = (TextView) findViewById(R.id.menu_icon_option);
        NewsCount = (TextView) findViewById(R.id.NewsCount);
        progressBarHolder = findViewById(R.id.progressBarHolder);


//          Country Name Short
//        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        String countryCode = tm.getSimCountryIso();
//        String countryCode3 = tm.getNetworkCountryIso();
//        Log.e("countryCode",countryCode.toUpperCase());
//        Log.e("getNetworkCountryIso",countryCode3.toUpperCase());

        preferences = SharedPreferenceUtils.getInstance(getApplicationContext());

        sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "");
        fcmtoken = sharedPreferences.getString("fcmtoken", "");
        status = sharedPreferences.getString("state", "");
        deleteduserCheck();
//        String status = sharedPreferences.getString("status", Constant.LOGGED_OUT);
        if (status.equals("on")||status.isEmpty()) {
            new api().execute(userid, fcmtoken, "android");
        }
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Discover JewelNet");

        verticalViewPager = findViewById(R.id.verticleViewPager);
//        verticalViewPager.setOffscreenPageLimit(1);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String http = Constant.BASE_URL + "getuserdetail?userid=" + userid;
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(http).get().build();
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
                                    String  status = json.getString("status");

                                    if (status.equalsIgnoreCase("1")) {
                                        Intent i = new Intent(ItemDisplay.this, PostActivity.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ItemDisplay.this);
                                        builder.setMessage("You need to subscribe for this services. Contact Vikas Chudasama at +91-9324254765.");
                                        builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                String uri = "tel:9324254765";
                                                Intent intent = new Intent(Intent.ACTION_CALL);
                                                intent.setData(Uri.parse(uri));
                                                if (ActivityCompat.checkSelfPermission(ItemDisplay.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(ItemDisplay.this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                                                } else
                                                    startActivity(intent);
                                            }
                                        });
                                        AlertDialog alertdialog = builder.create();
                                        alertdialog.show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        });

        fetchNewsFromServer();
        pageAdapter = new VerticalPageAdapter(this, getApplicationContext(), newsItems);
        verticalViewPager.setAdapter(pageAdapter);

        verticalViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mp != null) {
                    if (mp.isPlaying())
                        mp.stop();
                    mp.reset();
                    mp.release();
                    mp = null;
                }
                Log.e("log","logggg");
                if (position == newsItems.size() - 2) {
                    Log.e("log","logs");

                    if (add < 5){
                        add = add+1;
                    }
                    else if (add == 5){
                        add = 1;
                    }

                    counter = counter + 1;

                    String page = URLEncoder.encode("{\"page\":" + "\"" + counter + "\"," + "\"userid\":" + "\"" + userid + "\" ," + "\"ad_no\":" + "\"" + add + "\"}");
//                    String page = URLEncoder.encode("{\"page\":" + "\"" + counter + "\"}");
                    String urlString = Constant.BASE_URL + "getnewsand?data=" + page;

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlString).get().build();
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
                                        profile_base_path = json.getString("profile_base_path");
                                        news_base_path = json.getString("news_base_path");
                                        advertisement_base_path = json.getString("advertisement_base_path");
                                        json2 = json.getJSONArray("newsdata");
                                        Log.e("json", json2.toString());

                                        len = json2.length();

                                        for (int i = 0; i < len; i++) {
                                            boolean isAdvertise = false;
                                            JSONObject jsonTemp = json2.getJSONObject(i);

                                            if (jsonTemp.getString("file_type").equalsIgnoreCase("adv")) {
                                                isAdvertise = true;
                                                NewsCount.setText("0");

                                            }

                                            else if(jsonTemp.has("total_views")) {
                                                String count = jsonTemp.getString("total_views");
                                                newsviewcount = count;
                                                //GetViewCount();
                                            }
                                            newsItems.add(new NewsItemStructure(jsonTemp, news_base_path, profile_base_path, advertisement_base_path, isAdvertise, newsviewcount));
                                            pageAdapter.notifyDataSetChanged();
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



            @Override
            public void onPageSelected(int position) {
                positionofPage = position;
                if (position > PositionNow){
                    preferences.setValue(CommonUtils.POSITION, "1");
                }
                else if (position < PositionNow){
                    preferences.setValue(CommonUtils.POSITION, "0");
                }
                PositionNow = positionofPage;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                GetViewCount();
            }
        });

        GotoTop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (verticalViewPager.getCurrentItem() == 0) {
                    newsItems.clear();
                    fetchNewsFromServerup();
                    counter=0;
                    verticalViewPager.setAdapter(pageAdapter);
                } else
                    verticalViewPager.setCurrentItem(0, true);
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //loadFragment();
                Intent i = new Intent(ItemDisplay.this, MenuActivity.class);
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


//        drawer = findViewById(R.id.drawyer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setItemIconTintList(null);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int id = item.getItemId();
//
//                if (id == R.id.nav_home) {
//                    Intent i = new Intent(getApplicationContext(), ItemDisplay.class);
//                    startActivity(i);
//                    return true;
//
//                } else if (id == R.id.nav_info) {
//                    Intent i = new Intent(getApplicationContext(), info.class);
//                    startActivity(i);
//                    return true;
//
//                } else if (id == R.id.nav_tell) {
//                    Intent sendIntent = new Intent();
//                    sendIntent.setAction(Intent.ACTION_SEND);
//                    sendIntent.putExtra(Intent.EXTRA_TEXT,
//                            getString(R.string.tellFriend));
//                    sendIntent.setType("text/plain");
//                    startActivity(sendIntent);
//                    return true;
//
//               /* } else if (id == R.id.nav_profile) {
//                    Intent intent = new Intent(ItemDisplay.this, ProfileSelf.class);
//                    intent.putExtra("userid", userid);
//                    startActivity(intent);
//*/
//                } else if (id == R.id.nav_contact) {
//                    Intent duaIntent = new Intent(getApplicationContext(), contact.class);
//                    duaIntent.putExtra("type", "contact");
//                    startActivity(duaIntent);
//                    return true;
//
//                } else if (id == R.id.nav_settings) {
//                    Intent duaIntent = new Intent(getApplicationContext(), settings.class);
//                    duaIntent.putExtra("type", "settings");
//                    startActivity(duaIntent);
//                    return true;
//
//                } else if (id == R.id.nav_signout) {
//                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                    sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
//                    editor = sharedPreferences.edit();
//                    editor.putString("state","");
//                    editor.putString("userid", "");
//                    editor.apply();
//                    startActivity(i);
//                    finish();
//                }
//                return true;
//            }
//        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void deleteduserCheck(){

        String http = Constant.BASE_URL + "getuserdetail?userid=" + userid;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(http).get().build();
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
                            if (json.getString("response").equalsIgnoreCase("failure")) {
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
                                editor = sharedPreferences.edit();
                                editor.putString("state", "");
                                editor.putString("userid", "");
                                editor.apply();
                                startActivity(i);
                                finish();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    public void loadFragment(){
        FragmentManager fm = getSupportFragmentManager();
        menuFragment = fm.findFragmentById(R.id.mainview);
        //optionButton.setImageResource(R.drawable.sleepingmenu);
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
        //optionButton.setImageResource(R.drawable.standingmenu);
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

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setMessage("Are you sure you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void fetchNewsFromServer() {

        newsItems.clear();
        String page = URLEncoder.encode("{\"page\":" + "\"" + counter + "\"," + "\"userid\":" + "\"" + userid + "\"}");
        String urlString = Constant.BASE_URL + "getnewsand?data=" + page;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlString).get().build();
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
                            profile_base_path = json.getString("profile_base_path");
                            news_base_path = json.getString("news_base_path");
                            advertisement_base_path = json.getString("advertisement_base_path");
                            json2 = json.getJSONArray("newsdata");
                            len = json2.length();
                            for (int i = 0; i < len; i++) {
                                boolean isAdvertise = false;
                                JSONObject jsonTemp = json2.getJSONObject(i);

                                if (jsonTemp.getString("file_type").equalsIgnoreCase("adv")) {
                                    isAdvertise = true;
                                    NewsCount.setText("0");
                                }

                                else if(jsonTemp.has("total_views")) {
                                    String count = jsonTemp.getString("total_views");
                                    newsviewcount = count;
                                }

                                newsItems.add(new NewsItemStructure(jsonTemp, news_base_path, profile_base_path, advertisement_base_path, isAdvertise, newsviewcount));
                            }
                            pageAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


    }



    public void fetchNewsFromServerup() {
       // http://jewelnet.in/index.php/Api/getnews?data={"page":"0","userid":"15063","ad_no":"4"}
//        URLEncoder.encode("{\"page\":" + "\"" + "0" + "\"," + "\"userid\":" + "\"" + userid + "\"}");
        if (add < 5){
            add = add+1;
        }
        else if (add == 5){
            add = 1;
        }

        String page = URLEncoder.encode("{\"page\":" + "\"" + "0" + "\"," + "\"userid\":" + "\"" + userid + "\" ," + "\"ad_no\":" + "\"" + add + "\"}");
        String urlString = Constant.BASE_URL + "getnewsand?data=" + page;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlString).get().build();
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
                            profile_base_path = json.getString("profile_base_path");
                            news_base_path = json.getString("news_base_path");
                            advertisement_base_path = json.getString("advertisement_base_path");
                            json2 = json.getJSONArray("newsdata");
                            len = json2.length();
                            for (int i = 0; i < len; i++) {
                                boolean isAdvertise = false;
                                JSONObject jsonTemp = json2.getJSONObject(i);
                                if (jsonTemp.getString("file_type").equalsIgnoreCase("adv")) {
                                    isAdvertise = true;
                                    NewsCount.setText("0");
                                }

                                else if(jsonTemp.has("total_views")) {
                                    String count = jsonTemp.getString("total_views");
                                    newsviewcount = count;
                                }

                                newsItems.add(new NewsItemStructure(jsonTemp, news_base_path, profile_base_path, advertisement_base_path, isAdvertise, newsviewcount));
                            }
                            pageAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }



    private void GetViewCount() {
        try {
            String count= preferences.getStringValue(CommonUtils.NEWSCOUNT,"");
            NewsCount.setText(count);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class api extends AsyncTask<String, String, JSONObject> {
        JSONObject json;

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                String page = URLEncoder.encode("{\"userid\":" + "\"" + userid + "\"" + ",");
//                String page = URLEncoder.encode("{\"userid\":" + "\"" + "655" + "\"" + ",");
                String page1 = URLEncoder.encode("\"devicetoken\":" + "\"" + fcmtoken + "\"" + ",");
                String page2 = URLEncoder.encode("\"type\":" + "\"" + "android" + "\"}");

                String url = Constant.BASE_URL+"updatedevicetoken?data=" + page + page1 + page2;
//                String url = "http://demo1.geniesoftsystem.com/newweb/jewelnetnew/index.php/Api/updatedevicetoken?data=" + page + page1 + page2;
                try {
                    json = RestJsonClient.connect(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        protected void onPostExecute(JSONObject params) {
            super.onPostExecute(params);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", positionofPage);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int newposition = savedInstanceState.getInt("position");
        verticalViewPager.setCurrentItem(newposition);
    }


    public void StartLoading() {
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

    public void StopLoading() {

        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.GONE);
    }
  /*  @Override
    protected void onRestart() {
        super.onRestart();
        Intent i = new Intent(ItemDisplay.this,ItemDisplay.class);
        startActivity(i);
        finish();
    }*/

   /* @Override
    protected void onResume() {
        super.onResume();

    }*/

}