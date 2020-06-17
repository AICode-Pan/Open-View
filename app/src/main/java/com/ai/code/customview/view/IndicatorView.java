package com.ai.code.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ai.code.customview.R;

/**
 * 指示器
 */
public class IndicatorView extends View {

    private Paint mCirclePaint;
    private int mCount = 3; // indicator 的数量
    private int mRadius = 10;//半径
    private int mSelectWidth = 0;
    private int mSelectColor = Color.RED;
    private int mDotNormalColor = Color.WHITE;// 小圆点默认颜色
    private int mSpace = 20;// 圆点之间的间距
    private int mSelectPosition = 0; // 选中的位置

    public IndicatorView(Context context) {
        super(context);
        init();
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        getAttr(context, attrs);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        getAttr(context, attrs);
    }

    /**
     * 获取自定义属性
     *
     * @param context
     * @param attrs
     */
    private void getAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        mRadius = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_indicatorRadius, 10);
        mSpace = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_indicatorSpace, 20);
        // color
        mSelectColor = typedArray.getColor(R.styleable.IndicatorView_indicatorSelectColor, Color.WHITE);
        mDotNormalColor = typedArray.getColor(R.styleable.IndicatorView_indicatorColor, Color.GRAY);
        mSelectWidth = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_indicatorSelectWidth, 0);

        typedArray.recycle();
    }

    private void init() {
        mCirclePaint = new Paint();
        mCirclePaint.setDither(true);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mCirclePaint.setColor(mDotNormalColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = mRadius * 2 * mCount + mSpace * (mCount - 1) + mSelectWidth;
        int height = mRadius * 2 + mSpace * 2;

        Log.d("IndicatorView", "onMeasure width=" + width + " ,height=" + height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int cx = 0;
        int cy = getMeasuredHeight() / 2;
        for (int i = 0; i < mCount; i++) {
            if (i == 0) {
                cx = mRadius;
            } else {
                cx += mRadius * 2 + mSpace;
            }

            float x = cx;
            float y = cy;

            Log.d("IndicatorView", "onDraw isSelected=" + (mSelectPosition == i) + " view.x=" + x + " ,view.y=" + y);

            if (mSelectPosition == i) {
                mCirclePaint.setColor(mSelectColor);
                if (mSelectWidth == 0) {
                    canvas.drawCircle(x, y, mRadius, mCirclePaint);
                } else {
                    mCirclePaint.setStrokeWidth(mRadius * 2);
                    canvas.drawLine(x, y, x + mSelectWidth, y, mCirclePaint);
                    cx += mSelectWidth;
                }
            } else {
                mCirclePaint.setColor(mDotNormalColor);
                canvas.drawCircle(x, y, mRadius, mCirclePaint);
            }
        }
    }

    public void setCount(int count) {
        this.mCount = count;
        invalidate();
    }

    /**
     * 设置选中的item
     * @param position
     */
    public void setSelectPosition(int position) {
        if (mCount > 0 && mSelectPosition < mCount) {
            this.mSelectPosition = position;
            invalidate();
        }
    }

    /**
     * 设置半径
     * @param radius
     */
    public void setRadius(int radius) {
        this.mRadius = radius;
    }

    /**
     * 设置选中的颜色
     * @param color
     */
    public void setSelectColor(int color) {
        this.mSelectColor = color;
    }

    /**
     * 设置间距
     * @param space
     */
    public void setSpace(int space) {
        this.mSpace = space;
    }

    public void setDotNormalColor(int color) {
        this.mDotNormalColor = color;
    }
}
