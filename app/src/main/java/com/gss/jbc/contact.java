package com.gss.jbc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class contact extends AppCompatActivity {

    DrawerLayout drawer;

    TextView post;
    WebView webview;
    TextView title;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String userid;

    TextView toolbar_title1, toolbar_title;
    ImageView menuButton;
    TextView optionButton;
    boolean isFragmentLoaded;
    Fragment menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        final android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
        userid=sharedPreferences.getString("userid","");
        ImageView GotoTop = findViewById(R.id.GotoTop);
        GotoTop.setVisibility(View.GONE);
        post = findViewById(R.id.post);
        post.setVisibility(View.INVISIBLE);
        title=(TextView) findViewById(R.id.toolbar_title);
        title.setText("JewelNet - Contact us");
        webview = (WebView) findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(Constant.BASE_URL+"getContactus");
        webview.getSettings().setJavaScriptEnabled(true);
        menuButton = (ImageView) findViewById(R.id.menu_icon);
        optionButton = (TextView) findViewById(R.id.menu_icon_option);

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
//                    Intent i = new Intent(getApplicationContext(),ItemDisplay.class);
//                    startActivity(i);
//                    return true;
//
//                }
//                else if (id==R.id.nav_info)
//                {
//                    Intent i = new Intent(getApplicationContext(),info.class);
//                    startActivity(i);
//                    return true;
//
//                }
//                else if(id==R.id.nav_tell)
//                {
//                    Intent sendIntent = new Intent();
//                    sendIntent.setAction(Intent.ACTION_SEND);
//                    sendIntent.putExtra(Intent.EXTRA_TEXT,
//                            getString(R.string.tellFriend));
//                    sendIntent.setType("text/plain");
//                    startActivity(sendIntent);
//                    return true;
//
//                }
///*
//                else if(id==R.id.nav_profile)
//                {
//                    Intent intent = new Intent(contact.this,ProfileSelf.class);
//                    intent.putExtra("userid",userid);
//                    startActivity(intent);
//                }
//*/
//                else if (id == R.id.nav_contact)
//                {
//                    Intent duaIntent = new Intent(getApplicationContext(),contact.class);
//                    duaIntent.putExtra("type","contact");
//                    startActivity(duaIntent);
//                    return true;
//
//                }
//                else if(id == R.id.nav_settings)
//                {
//                    Intent duaIntent = new Intent(getApplicationContext(),settings.class);
//                    duaIntent.putExtra("type","settings");
//                    startActivity(duaIntent);
//                    return true;
//
//                }
//                else if(id == R.id.nav_signout)
//                {
//                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
//                    sharedPreferences = getSharedPreferences("ID", MODE_PRIVATE);
//                    editor=sharedPreferences.edit();
//                    editor.putString("state","");
//                    editor.putString("userid","");
//                    editor.commit();
//                    startActivity(i);
//
//                }
//                return true;
//            }
//        });


        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //loadFragment();
                Intent i = new Intent(contact.this, MenuActivity.class);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
