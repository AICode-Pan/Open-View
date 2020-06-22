package com.ai.code.customview.activity;

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

public class BlurActivity extends AppCompatActivity {
    private BlurView blurView;
    private RecyclerView recyclerview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("仿IOS Blur控件");
        setContentView(R.layout.activity_blur);
        recyclerview = findViewById(R.id.recyclerview);
        blurView = findViewById(R.id.blurview);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(new ImageAdapter());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    Log.i("BlurView", "onScrollChange");
                    blurView.requestLayout();
                }
            });
        }
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
