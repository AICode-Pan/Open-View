package com.ai.code.customview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ai.code.customview.R;
import com.ai.code.customview.view.SlideSelectView;

public class SlideSelectActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("SlideSelectActivity");
        setContentView(R.layout.activity_slide_select);

        SlideSelectView slideview2 = findViewById(R.id.slideview2);
        SlideSelectView slideview3 = findViewById(R.id.slideview3);

        String[] s1 = {"1", "2", "3", "4"};
        slideview2.setString(s1);

        String[] s2 = {"极小", "小", "中", "大", "很大"};
        slideview3.setString(s2);
    }
}
