package com.ai.code.customview.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ai.code.customview.R;
import com.ai.code.customview.view.IndicatorView;

public class IndicatorActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private IndicatorView indicatorView1;
    private IndicatorView indicatorView2;

    private int count = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("指示器");
        setContentView(R.layout.activity_indicator);
        viewPager = findViewById(R.id.viewpager);

        indicatorView1 = findViewById(R.id.indicator1);
        indicatorView2 = findViewById(R.id.indicator2);
        indicatorView1.setCount(count);
        indicatorView2.setCount(count);

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return count;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                TextView textView = new TextView(IndicatorActivity.this);
                textView.setText("页面" + (position + 1));
                textView.setGravity(Gravity.CENTER);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                textView.setLayoutParams(lp);

                container.addView(textView);
                return textView;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                indicatorView1.setSelectPosition(i);
                indicatorView2.setSelectPosition(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
}
