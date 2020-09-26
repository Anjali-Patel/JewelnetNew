package com.gss.jbc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class InfoDirSubtypes extends AppCompatActivity {
    ListView lvSubTypes;

    DrawerLayout drawer;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String userid;
    TextView toolbar_title;
    String[] strSubtypes = {"GOLD", "DIAMOND", "PLATINUM", "SILVER", "GEMSTONE", "LOOSE DIAMONDS", "LAB GROWN DIAMONDS"};
    String[] strSubtypesNotForRetailors = {"GOLD", "DIAMOND", "PLATINUM", "SILVER", "GEMSTONE"};

    String selectedType = "";
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_dir_subtypes);
        final android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar_title = (TextView) findViewById(R.id.toolbar_title1);

        sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "");

        //Retrieve selectedType from Intent
        selectedType = getIntent().getStringExtra("type");
        toolbar_title.setText(selectedType);

        lvSubTypes = (ListView) findViewById(R.id.sub_types_list);

        if(selectedType.equalsIgnoreCase("RETAILER")){
            adapter = new ArrayAdapter(this, R.layout.center_list_item, R.id.list_text, strSubtypesNotForRetailors);
        }
        else{
            adapter = new ArrayAdapter(this, R.layout.center_list_item, R.id.list_text, strSubtypes);
        }

        lvSubTypes.setAdapter(adapter);
        lvSubTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedSubType = adapter.getItem(position).toString();
                Intent i = new Intent(getApplicationContext(), Directory.class);
                i.putExtra("type", selectedType);
                i.putExtra("subtype", selectedSubType);
                startActivity(i);
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;*/
            case R.id.back:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
