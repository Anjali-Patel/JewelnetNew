package com.gss.jbc;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

public class ZoomImageActivity extends AppCompatActivity {
    private ZoomageView zoomageView;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2f1b47")));
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        zoomageView = findViewById(R.id.myZoomageView);

        Picasso.with(getApplicationContext()).load(getIntent().getExtras().getString("img")).into(zoomageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                break;
        }
        return true;
    }
}
