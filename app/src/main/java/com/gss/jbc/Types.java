package com.gss.jbc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Types extends AppCompatActivity {

    ListView lvTypes;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String userid;
    String[] strTypes = {"MANUFACTURER", "WHOLESALER", "RETAILER", "ALLIED"};
    String selectedType = "";

    TextView toolbar_title1, toolbar_title;
    ImageView menuButton;
    TextView optionButton;
    boolean isFragmentLoaded;
    Fragment menuFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_types);
        final android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("Jewelnet-Info Directory");

        menuButton = (ImageView) findViewById(R.id.menu_icon);
        optionButton = (TextView) findViewById(R.id.menu_icon_option);

        sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
        userid=sharedPreferences.getString("userid","");

        lvTypes = (ListView) findViewById(R.id.types_list);

        final ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.center_list_item, R.id.list_text , strTypes);

        lvTypes.setAdapter(adapter);
        lvTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectedType = adapter.getItem(position).toString();

                if (selectedType.equalsIgnoreCase("ALLIED")) {
                    Intent i = new Intent(getApplicationContext(), Directory.class);
                    i.putExtra("type", selectedType);
                    i.putExtra("subtype", "OTHERS");
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(getApplicationContext(), InfoDirSubtypes.class);
                    i.putExtra("type", selectedType);
                    startActivity(i);
                }

            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //loadFragment();
                Intent i = new Intent(Types.this, MenuActivity.class);
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
//                    Intent intent = new Intent(Types.this,ProfileSelf.class);
//                    intent.putExtra("userid",userid);
//                    startActivity(intent);
//
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
//                    finish();
//
//                }
//                return true;
//            }
//        });


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
