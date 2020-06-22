package com.ai.code.customview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.ai.code.customview.R;
import com.ai.code.customview.view.SlideSelectView;

public class SlideSelectActivity extends AppCompatActivity {
    String[] s1 = {"1", "2", "3", "4"};
    String[] s2 = {"极小", "小", "中", "大", "很大"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("SlideSelectActivity");
        setContentView(R.layout.activity_slide_select);

        SlideSelectView slideview1 = findViewById(R.id.slideview1);
        SlideSelectView slideview2 = findViewById(R.id.slideview2);
        SlideSelectView slideview3 = findViewById(R.id.slideview3);
        final TextView tvTips = findViewById(R.id.tips);

        slideview1.setOnSelectListener(new SlideSelectView.onSelectListener() {
            @Override
            public void onSelect(int index) {
                Toast.makeText(SlideSelectActivity.this, "选择了第" + index + "个Index", Toast.LENGTH_SHORT).show();
            }
        });


        slideview2.setString(s1);


        slideview3.setString(s2);
        slideview3.setCurrentPosition(2);
        slideview3.setOnSelectListener(new SlideSelectView.onSelectListener() {
            @Override
            public void onSelect(int index) {
                tvTips.setText("您选择了 " + s2[index]);
            }
        });
    }
}
