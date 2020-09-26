package com.gss.jbc;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

public class WatchVideoPost extends AppCompatActivity {
    VideoView play;
    String url;
    MediaController ctrl;
    ImageView video_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_video_post);
        play = findViewById(R.id.play);
        video_back = findViewById(R.id.video_back);
        url = getIntent().getStringExtra("url");
        video_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ctrl = new MediaController(WatchVideoPost.this);
        if (play.isPlaying()) {
            if (play != null) {
                play.stopPlayback();
                play.suspend();
                ctrl.hide();
            }
        } else {

            play.setVideoPath(url);

//            play.setVideoURI(Uri.parse(url));
            ctrl.setAnchorView(play);
            ctrl.setMediaPlayer(play);
            play.setMediaController(ctrl);
            play.requestFocus();
            play.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    ctrl.show(0);
                    play.start();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        play.stopPlayback();
        play.suspend();
        ctrl.hide();
    }
}
