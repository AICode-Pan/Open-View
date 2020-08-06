package com.ai.code.customview.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ai.code.customview.R;
import com.ai.code.customview.view.BlurView;

public class BlurActivity extends Activity {
    private BlurView blurView;
    private RecyclerView recyclerview;
    private int totalDy = 0;
    private int color = Color.parseColor("#FFFFFF");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blur);
        recyclerview = findViewById(R.id.recyclerview);
        blurView = findViewById(R.id.blurview);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(new ImageAdapter());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalDy += dy;
                    if (totalDy < 3000) {
                        float i = Float.valueOf(totalDy) / 6000;
                        int alpha = (int) Math.round(i * 255f);
                        Log.e("BlurView", "onScrolled i = " + i + " alpha=" + alpha);

                        if (i == 0) {
                            blurView.setBlurRadius(0);
                        } else {
                            blurView.setBlurRadius((int) (i * 5) + 3);
                        }
                        blurView.setMaskColor(setAlpha(color, i));
                    }

                    blurView.requestLayout();
                }
            });
        }
    }

    public int setAlpha(int color, float alpha) {
        return color & 0x00FFFFFF | Math.round(0xFF * alpha) << 24;
    }

    private class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ItemViewHolder> {

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ImageView imageView = new ImageView(viewGroup.getContext());
            imageView.setLayoutParams(lp);
            imageView.setImageResource(R.drawable.img_haizeiwang);
            return new ItemViewHolder(imageView);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {
            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }

    }
}
