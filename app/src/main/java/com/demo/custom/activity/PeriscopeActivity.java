package com.demo.custom.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.demo.custom.view.PeriscopeLayout;
import com.demo.custom.R;

public class PeriscopeActivity extends AppCompatActivity {
    private PeriscopeLayout ll_heart;
    private Button ll_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("PeriscopeActivity");
        setContentView(R.layout.activity_periscope);
        ll_heart = (PeriscopeLayout) findViewById(R.id.ll_heart);
        ll_btn = (Button) findViewById(R.id.ll_btn);

        ll_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_heart.addHeart();
            }
        });
    }
}
