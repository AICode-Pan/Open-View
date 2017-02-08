package com.demo.videodemo.videodemo;

import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.demo.videodemo.videodemo.heart.PeriscopeLayout;
import com.demo.videodemo.videodemo.media.AndroidMediaController;
import com.demo.videodemo.videodemo.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getName();
    private PeriscopeLayout ll_heart;
    private Button ll_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ll_heart = (PeriscopeLayout) findViewById(R.id.ll_heart);
        ll_btn = (Button) findViewById(R.id.ll_btn);

        ll_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_heart.addHeart();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
