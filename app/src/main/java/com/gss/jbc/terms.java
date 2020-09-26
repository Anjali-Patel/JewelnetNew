package com.gss.jbc;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class terms extends AppCompatActivity {

    DrawerLayout drawer;
    TextView post;
    WebView webview;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setTitle("JewelNet Terms and Conditions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
        userid=sharedPreferences.getString("userid","");
        webview = (WebView) findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(Constant.BASE_URL+"getTerms_condition");
       /* drawer = findViewById(R.id.drawyer_layout);
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
                    Intent i = new Intent(getApplicationContext(),ItemDisplay.class);
                    startActivity(i);
                    return true;

                }
                else if (id==R.id.nav_info)
                {
                    Intent i = new Intent(getApplicationContext(),info.class);
                    startActivity(i);
                    return true;

                }
                else if(id==R.id.nav_tell)
                {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            "Hey check out my app at: https://play.google.com/store/apps/details?id");
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                    return true;

                }
*//*
                else if(id==R.id.nav_profile)
                {
                    Intent intent = new Intent(terms.this,ProfileSelf.class);
                    intent.putExtra("userid",userid);
                    startActivity(intent);

                }
*//*
                else if (id == R.id.nav_contact)
                {
                    Intent duaIntent = new Intent(getApplicationContext(),contact.class);
                    duaIntent.putExtra("type","contact");
                    startActivity(duaIntent);
                    return true;

                }
                else if(id == R.id.nav_settings)
                {
                    Intent duaIntent = new Intent(getApplicationContext(),settings.class);
                    duaIntent.putExtra("type","settings");
                    startActivity(duaIntent);
                    return true;

                }
                else if(id == R.id.nav_signout)
                {
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    sharedPreferences=getSharedPreferences("ID",MODE_PRIVATE);
                    editor=sharedPreferences.edit();
                    editor.putString("userid","");
                    editor.apply();
                    startActivity(i);
                    finish();
                }
                return true;
            }
        });*/

    }
  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);


    }*/
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      return super.onCreateOptionsMenu(menu);
  }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();        //To handle navigation properly in types -subtypes screen
                // as both activity can navigate to Directory.java
                return true;
            case R.id.back:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
