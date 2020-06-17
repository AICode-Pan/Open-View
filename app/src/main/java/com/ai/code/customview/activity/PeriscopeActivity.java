package com.ai.code.customview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ai.code.customview.view.PeriscopeLayout;
import com.ai.code.customview.R;

public class PeriscopeActivity extends AppCompatActivity {
    private PeriscopeLayout ll_heart;
    private Button ll_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("点赞动画");
        setContentView(R.layout.activity_periscope);
        ll_heart = findViewById(R.id.ll_heart);
        ll_btn = findViewById(R.id.ll_btn);

        ll_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_heart.addHeart();
            }
        });
    }
}
