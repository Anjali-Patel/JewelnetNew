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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    RelativeLayout JhomeRelative,JinfoRelative,JexhibitionsRelative,JcontactRelative,JSettingsRelative,JlogoutRelative;
    ImageView Jhome, Jinfo, Jexhibitions, Jcontact, JSettings, Jlogout;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    TextView optionButton;
    Fragment menuFragment;
    boolean isFragmentLoaded;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);

        Jhome = (ImageView) findViewById(R.id.Jhome);
        Jinfo = (ImageView) findViewById(R.id.Jinfo);
        Jexhibitions = (ImageView) findViewById(R.id.Jexhibitions);
        Jcontact = (ImageView) findViewById(R.id.Jcontact);
        JSettings = (ImageView) findViewById(R.id.JSettings);
        Jlogout = (ImageView) findViewById(R.id.Jlogout);

        JhomeRelative = (RelativeLayout) findViewById(R.id.JhomeRelative);
        JinfoRelative = (RelativeLayout) findViewById(R.id.JinfoRelative);
        JexhibitionsRelative = (RelativeLayout) findViewById(R.id.JexhibitionsRelative);
        JcontactRelative = (RelativeLayout) findViewById(R.id.JcontactRelative);
        JSettingsRelative = (RelativeLayout) findViewById(R.id.JSettingsRelative);
        JlogoutRelative = (RelativeLayout) findViewById(R.id.JlogoutRelative);



        optionButton = (TextView) findViewById(R.id.menu_icon_option);




        JhomeRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ItemDisplay.class);
                startActivity(i);

            }
        });


        JinfoRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), info.class);
                startActivity(i);

            }
        });


        JexhibitionsRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Exhibitions.class);
                startActivity(i);


            }
        });


        JcontactRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent duaIntent = new Intent(getApplicationContext(), contact.class);
                duaIntent.putExtra("type", "contact");
                startActivity(duaIntent);

            }
        });

        JSettingsRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent duaIntent = new Intent(getApplicationContext(), settings.class);
                duaIntent.putExtra("type", "settings");
                startActivity(duaIntent);

            }
        });


        JlogoutRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("state","");
                editor.putString("userid", "");
                editor.apply();
                startActivity(i);
                finish();

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
}
