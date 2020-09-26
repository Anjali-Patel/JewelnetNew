package com.gss.jbc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    TextView mname, mmobno, memail, maddress, mcity;
    RoundedImageView imgView;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile3);
        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        title = findViewById(R.id.toolbar_title1);
        title.setText(getIntent().getStringExtra("type"));
        mname = findViewById(R.id.tvgetname);
        mmobno = findViewById(R.id.tvgetmobno);
        memail = findViewById(R.id.tvgetemail);
        maddress = findViewById(R.id.tvgetadd);
        mcity = findViewById(R.id.tvgetcity);
        imgView = findViewById(R.id.imgProfile);

        mname.setText(getIntent().getExtras().getString("name"));
        mmobno.setText(getIntent().getExtras().getString("phone"));
        memail.setText(getIntent().getExtras().getString("email"));
        maddress.setText(getIntent().getExtras().getString("address"));
        mcity.setText(getIntent().getExtras().getString("city"));
        Log.d("img", getIntent().getExtras().getString("image"));
        Picasso.with(getApplicationContext()).load(getIntent().getExtras().getString("image")).into(imgView);
        //imgView.setImageResource(Integer.parseInt(getIntent().getExtras().getString("image")));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this, Directory.class);
        intent.putExtras(getIntent());
        startActivity(intent);
    }
}
