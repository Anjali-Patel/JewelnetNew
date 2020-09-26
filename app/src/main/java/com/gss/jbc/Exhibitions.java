package com.gss.jbc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gss.jbc.Model.ExhibitionEventDataModel;

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

public class Exhibitions extends AppCompatActivity {

    TextView post;
    TextView title;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String userid;

    TextView toolbar_title1, toolbar_title;
    ImageView menuButton;
    TextView optionButton;
    boolean isFragmentLoaded;
    Fragment menuFragment;

    RelativeLayout jan,feb,mar,apr,may,jun,jul,aug,sept,oct,nov,dec;

    ArrayList<ExhibitionEventDataModel> CalenderEvents = new ArrayList<ExhibitionEventDataModel>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibitions);

        final android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
        userid=sharedPreferences.getString("userid","");
        ImageView GotoTop = findViewById(R.id.GotoTop);
        GotoTop.setVisibility(View.GONE);
        post = findViewById(R.id.post);
        post.setVisibility(View.INVISIBLE);
        title=(TextView) findViewById(R.id.toolbar_title);
        title.setText("JewelNet - Exhibitions");

        menuButton = (ImageView) findViewById(R.id.menu_icon);
        optionButton = (TextView) findViewById(R.id.menu_icon_option);
        optionButton.setVisibility(View.GONE);

        jan = (RelativeLayout) findViewById(R.id.jan);
        feb = (RelativeLayout) findViewById(R.id.feb);
        mar = (RelativeLayout) findViewById(R.id.mar);
        apr = (RelativeLayout) findViewById(R.id.apr);
        may = (RelativeLayout) findViewById(R.id.may);
        jun = (RelativeLayout) findViewById(R.id.jun);
        jul = (RelativeLayout) findViewById(R.id.jul);
        aug = (RelativeLayout) findViewById(R.id.aug);
        sept = (RelativeLayout) findViewById(R.id.sept);
        oct = (RelativeLayout) findViewById(R.id.oct);
        nov = (RelativeLayout) findViewById(R.id.nov);
        dec = (RelativeLayout) findViewById(R.id.dec);

        GetAllEvents();

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //loadFragment();
                Intent i = new Intent(Exhibitions.this, MenuActivity.class);
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

        jan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Exhibitions.this, CalenderEvents.class);
                i.putExtra("month","jan");
                startActivity(i);

            }
        });
        feb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Exhibitions.this, CalenderEvents.class);
                i.putExtra("month","feb");
                startActivity(i);

            }
        });
        mar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Exhibitions.this, CalenderEvents.class);
                i.putExtra("month","mar");
                startActivity(i);

            }
        });
        apr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Exhibitions.this, CalenderEvents.class);
                i.putExtra("month","apr");
                startActivity(i);

            }
        });
        may.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Exhibitions.this, CalenderEvents.class);
                i.putExtra("month","may");
                startActivity(i);

            }
        });
        jun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Exhibitions.this, CalenderEvents.class);
                i.putExtra("month","jun");
                startActivity(i);

            }
        });
        jul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Exhibitions.this, CalenderEvents.class);
                i.putExtra("month","jul");
                startActivity(i);

            }
        });
        aug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Exhibitions.this, CalenderEvents.class);
                i.putExtra("month","aug");
                startActivity(i);

            }
        });
        sept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Exhibitions.this, CalenderEvents.class);
                i.putExtra("month","sept");
                startActivity(i);

            }
        });
        oct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Exhibitions.this, CalenderEvents.class);
                i.putExtra("month","oct");
                startActivity(i);

            }
        });
        nov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Exhibitions.this, CalenderEvents.class);
                i.putExtra("month","nov");
                startActivity(i);

            }
        });
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Exhibitions.this, CalenderEvents.class);
                i.putExtra("month","dec");
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


    private void GetAllEvents() {

        CalenderEvents.clear();
        String urlString = "http://demo1.geniesoftsystem.com/newweb/jewelnetnewone/index.php/Api/exhibitions";

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
                            String success = json.getString("response");
                            if (success.equalsIgnoreCase("success")) {
                                JSONArray jsonArray = null;
                                jsonArray = json.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject newJson = jsonArray.getJSONObject(i);
                                    ExhibitionEventDataModel model = new ExhibitionEventDataModel();
                                    model.setEventTitle(newJson.getString("comment_id"));
                                    model.setEventVenue(newJson.getString("commentor_id"));
                                    model.setEventDate(newJson.getString("news_id"));
                                    model.setEventLink(newJson.getString("comment"));
                                    model.setEventLogo(newJson.getString("title"));


                                    CalenderEvents.add(model);
                                }

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
