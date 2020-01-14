package com.demo.custom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.demo.custom.R;

public class FuzzyActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuzzy);
    }
}
