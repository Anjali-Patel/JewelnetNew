package com.gss.jbc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

public class news_more extends AppCompatActivity {

    ImageView pic, profile;
    VideoView play;
    ImageView play_bt, share;
    TextView title, description, display, post;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String userid;
    MediaController ctrl;
    ImageView GotoTop;
    String videoimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_more);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "");


        pic = findViewById(R.id.imageView);
        play = findViewById(R.id.play);
        play_bt = findViewById(R.id.play_bt);
        description = findViewById(R.id.description);
        title = findViewById(R.id.textView);
        display = findViewById(R.id.readmore);

        share = findViewById(R.id.share);
        share.setVisibility(View.GONE);
        post = findViewById(R.id.post);
        GotoTop = findViewById(R.id.GotoTop);
        post.setVisibility(View.GONE);
        GotoTop.setVisibility(View.GONE);
/*
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, title.getText());
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, title.getText());
                sendIntent.putExtra(Intent.EXTRA_TEXT, description.getText());
                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(getIntent().getExtras().getString("img")));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
*/


        if  (getIntent().getExtras().getString("type").equalsIgnoreCase("img")) {
           if (getIntent().getExtras() != null) {
                Picasso.with(getApplicationContext()).load(getIntent().getExtras().getString("img")).into(pic);
//            Picasso.with(getApplicationContext()).load(getIntent().getExtras().getString("profile")).into(profile);
            }
        }
        else   {
            pic.setVisibility(View.VISIBLE);

            String videoimage = getIntent().getExtras().getString("videoimage")+ ".png";
            Log.e("videoimage",videoimage);
            Picasso.with(getApplicationContext()).load(videoimage).into(pic);
//            play_bt.setVisibility(View.GONE);
//            play.setVisibility(View.VISIBLE);
//             ctrl = new MediaController(news_more.this);
//             if (play.isPlaying()) {
//                 if (play != null) {
//                     play.stopPlayback();
//                     play.suspend();
//                     ctrl.hide();
//                 }
//             } else {
//                 play.setVideoURI(Uri.parse(url));
//                 ctrl.setAnchorView(play);
//                 ctrl.setMediaPlayer(play);
//                 play.setMediaController(ctrl);
//                 play.requestFocus();
//                 play.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                     @Override
//                     public void onPrepared(MediaPlayer mp) {
//                         ctrl.show(0);
//                         play.start();
//                     }
//                 });
//             }
        }
//        Log.d("data", getIntent().getExtras().getString("img") + " " + getIntent().getExtras().getString("profile"));
        description.setText(getIntent().getExtras().getString("desc"));
        title.setText(getIntent().getExtras().getString("title"));
        /*   Toast.makeText(getApplicationContext(),getIntent().getExtras().get("id").toString(),Toast.LENGTH_SHORT).show();*/


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
