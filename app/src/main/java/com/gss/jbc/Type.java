package com.gss.jbc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Type extends AppCompatActivity {

    Button diamond,gold,silver,platinum,all;

    DrawerLayout drawer;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        final android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("ID",MODE_WORLD_READABLE);
        userid=sharedPreferences.getString("userid","");

        diamond = (Button) findViewById(R.id.diamond);
        gold = (Button) findViewById(R.id.gold);
        silver = (Button) findViewById(R.id.silver);
        platinum = (Button) findViewById(R.id.platinum);
        all = (Button) findViewById(R.id.all);

        diamond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Directory.class);
                i.putExtra("type","diamond");
                startActivity(i);
            }
        });

        gold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Directory.class);
                i.putExtra("type","gold");
                startActivity(i);
            }
        });

        silver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Directory.class);
                i.putExtra("type","silver");
                startActivity(i);
            }
        });

        platinum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Directory.class);
                i.putExtra("type","platinum");
                startActivity(i);
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Directory.class);
                i.putExtra("type","all");
                startActivity(i);
            }
        });
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
/*
                else if(id==R.id.nav_profile)
                {
                    Intent intent = new Intent(Type.this,ProfileSelf.class);
                    intent.putExtra("userid",userid);
                    startActivity(intent);
                }
*/
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
                    sharedPreferences=getSharedPreferences("ID",MODE_WORLD_WRITEABLE);
                    editor=sharedPreferences.edit();
                    editor.putString("userid","");
                    editor.commit();
                    startActivity(i);
                    finish();
                }
                return true;
            }
        });
    }


}