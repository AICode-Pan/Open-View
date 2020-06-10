package com.ai.code.customview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ai.code.customview.activity.FuzzyActivity;
import com.ai.code.customview.activity.PeriscopeActivity;

public class MainActivity extends Activity {
    private final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onPeriscopeClick(View view) {
        Intent intent = new Intent();
        intent.setClass(this, PeriscopeActivity.class);
        startActivity(intent);
    }

    public void onFuzzyClick(View view) {
        Intent intent = new Intent();
        intent.setClass(this, FuzzyActivity.class);
        startActivity(intent);
    }

    public void onIndicator(View view) {

    }
}
