package com.gss.jbc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.gss.jbc.Extra.RestJsonClient;
import org.json.JSONObject;
import java.net.URLEncoder;


public class settings extends AppCompatActivity {

    DrawerLayout drawer;
    Button about, terms;
//    ToggleButton notifications;
    SwitchCompat notifications;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView post;
    String userid;
    String fcmtoken;
    String onOff;
    TextView app_version;
    float versionCode;
    String ver;
    ImageView menuButton;
    TextView optionButton;
    Fragment menuFragment;
    boolean isFragmentLoaded;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ImageView GotoTop = findViewById(R.id.GotoTop);
        GotoTop.setVisibility(View.GONE);
        notifications = findViewById(R.id.notifications);
        about = findViewById(R.id.aboutus);
        terms = findViewById(R.id.terms);
        post = (TextView) findViewById(R.id.post);
        app_version = (TextView) findViewById(R.id.app_version);
        versionCode = BuildConfig.VERSION_CODE;
        ver = String.valueOf(versionCode);
        app_version.setText("App Version : "+ver);
        View view;
        menuButton = (ImageView) findViewById(R.id.menu_icon);
        optionButton = (TextView) findViewById(R.id.menu_icon_option);


        post.setVisibility(View.GONE);

        sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
        fcmtoken = sharedPreferences.getString("fcmtoken", "");
        userid = sharedPreferences.getString("userid", "");
        onOff = sharedPreferences.getString("state", "");

        if (onOff.equalsIgnoreCase("off")) {
            notifications.setChecked(false);
        } else if (onOff.isEmpty()||onOff.equalsIgnoreCase("on")) {
            notifications.setChecked(true);
        }

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //loadFragment();
                Intent i = new Intent(settings.this, MenuActivity.class);
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


        notifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                    editor = sharedPreferences.edit();
                    editor.putString("state", "on");
                    editor.apply();
                    fcmtoken = sharedPreferences.getString("fcmtoken", "");
                    new api().execute(userid, fcmtoken, "android");
                    Toast.makeText(settings.this, "Notifications enabled", Toast.LENGTH_SHORT).show();
                } else {
                    editor = sharedPreferences.edit();
                    editor.putString("state", "off");
                    editor.commit();
                    fcmtoken="0000";
                    new api().execute(userid, fcmtoken, "android");
                    Toast.makeText(settings.this, "Notifications are Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), about.class);
                startActivity(i);
            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), terms.class);
                startActivity(i);
            }
        });
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawyer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    Intent i = new Intent(getApplicationContext(), ItemDisplay.class);
                    startActivity(i);
                    return true;
                }
                else if (id == R.id.nav_info) {
                    Intent i = new Intent(getApplicationContext(), info.class);
                    startActivity(i);
                    return true;
                }
                else if (id == R.id.nav_tell) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            getString(R.string.tellFriend));
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                    return true;

                }
/*                else if(id==R.id.nav_profile)
                {
                    Intent intent = new Intent(settings.this,ProfileSelf.class);
                    intent.putExtra("userid",userid);
                    startActivity(intent);

                }*/
                else if (id == R.id.nav_contact) {
                    Intent duaIntent = new Intent(getApplicationContext(), contact.class);
                    duaIntent.putExtra("type", "contact");
                    startActivity(duaIntent);
                    return true;

                } else if (id == R.id.nav_settings) {
                    Intent duaIntent = new Intent(getApplicationContext(), settings.class);
                    duaIntent.putExtra("type", "settings");
                    startActivity(duaIntent);
                    return true;

                } else if (id == R.id.nav_signout) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    sharedPreferences = getSharedPreferences("ID", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("state","");
                    editor.putString("userid", "");
                    editor.apply();
                    startActivity(i);
                    finish();
                }
                return true;
            }
        });

    }
    class api extends AsyncTask<String, String, JSONObject> {
        JSONObject json;

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                String page = URLEncoder.encode("{\"userid\":" + "\"" + userid + "\"" + ",");
                String page1 = URLEncoder.encode("\"devicetoken\":" + "\"" + fcmtoken + "\"" + ",");
                String page2 = URLEncoder.encode("\"type\":" + "\"" + "android" + "\"}");
                String url = Constant.BASE_URL+"updatedevicetoken?data=" + page + page1 + page2;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);

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
}
