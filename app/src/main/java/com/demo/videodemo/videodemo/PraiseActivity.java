package com.demo.videodemo.videodemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.demo.videodemo.videodemo.heart.PeriscopeLayout;

/**
 * Created by pan on 16/12/29.
 */

public class PraiseActivity extends Activity {
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
    protected void onDestroy() {
        super.onDestroy();
        ll_heart.clear();

        finish();
    }
}
