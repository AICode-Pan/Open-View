package com.ai.code.customview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;

import com.ai.code.customview.R;

/**
 * Created by AICode-Pan on 20/05/10.
 */
public class SlideSelectView extends View {
    //线距离两头的边距
    private static float MARGEN_LINE = 60;
    //小圆半径
    private int smallCircleRadius;
    //大圆半径
    private float bigCircleRadius;
    //大圆圆心半径
    private float bigCircleCenterRadius;
    //小圆的数量
    private int countOfSmallCircle;
    //小圆的横坐标
    private float circlesX[];
    private Context mContext;
    //线的画笔
    private Paint mLinePaint;
    //小圆画笔
    private Paint mSmallCirclePaint;
    //大圆画笔
    private Paint mBigCirclePaint;
    //圆心画笔
    private Paint mBigCircleCenterPaint;
    //文字画笔
    private TextPaint mTextPaint;
    //控件高度
    private float mHeight;
    //控件宽度
    private float mWidth;
    //大圆的横坐标
    private float bigCircleX;
    //是否是手指跟随模式
    private boolean isFollowMode;
    //手指按下的x坐标
    private float startX;
    //线的颜色
    private int lineColor;
    //线的宽度
    private int lineStrokeWidth;
    //小圆颜色
    private int smallCircleColor;
    //大圆颜色
    private int bigCircleColor;
    //圆心颜色
    private int bigCircleCenterColor;
    //文字大小
    private float textSize;
    //文字颜色
    private int textColor;
    //文字选中颜色
    private int textSelectColor;
    //文字宽度
    private float textWidth;
    //当前大球距离最近的位置
    private int currentPosition;
    //小圆之间的间距
    private float distanceX;
    //文字顶部间距
    private int textTopMargin;
    //利率文字
    private String[] text4Rates = {};
    //依附效果实现
    private ValueAnimator valueAnimator;
    //用于纪录松手后的x坐标
    private float currentPositionX;

    private onSelectListener selectListener;


    public SlideSelectView(Context context) {
        this(context, null);
    }

    public SlideSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlideSelectView);
        lineColor = a.getColor(R.styleable.SlideSelectView_lineColor, 0xff717171);
        lineStrokeWidth = a.getDimensionPixelSize(R.styleable.SlideSelectView_lineStrokeWidth, 10);
        smallCircleColor = a.getColor(R.styleable.SlideSelectView_smallCircleColor, 0xff717171);
        smallCircleRadius = a.getDimensionPixelSize(R.styleable.SlideSelectView_smallCircleRadius, 0);
        bigCircleColor = a.getColor(R.styleable.SlideSelectView_bigCircleColor, 0xffEB535E);
        bigCircleRadius = a.getDimensionPixelSize(R.styleable.SlideSelectView_bigCircleRadius, 30);
        bigCircleCenterColor = a.getColor(R.styleable.SlideSelectView_bigCenterColor, 0xffffffff);
        bigCircleCenterRadius = a.getDimensionPixelSize(R.styleable.SlideSelectView_bigCenterRadius, 0);
        countOfSmallCircle = a.getInt(R.styleable.SlideSelectView_circleCount, 5);
        textSize = a.getDimensionPixelSize(R.styleable.SlideSelectView_textSize, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        textTopMargin = a.getDimensionPixelSize(R.styleable.SlideSelectView_textTopMargin, 4);
        textColor = a.getColor(R.styleable.SlideSelectView_textColor, 0xff717171);
        textSelectColor = a.getColor(R.styleable.SlideSelectView_textSelectColor, 0xffEB535E);
        a.recycle();
        MARGEN_LINE = bigCircleRadius + 20;
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(lineColor);
        mLinePaint.setStrokeWidth(lineStrokeWidth);
        mLinePaint.setAntiAlias(true);

        if (smallCircleRadius != 0) {
            mSmallCirclePaint = new Paint();
            mSmallCirclePaint.setStyle(Paint.Style.FILL);
            mSmallCirclePaint.setColor(smallCircleColor);
            mSmallCirclePaint.setAntiAlias(true);
        }

        mBigCirclePaint = new Paint();
        mBigCirclePaint.setStyle(Paint.Style.FILL);
        mBigCirclePaint.setColor(bigCircleColor);
        mBigCirclePaint.setAntiAlias(true);

        if (bigCircleCenterRadius != 0) {
            mBigCircleCenterPaint = new Paint();
            mBigCircleCenterPaint.setStyle(Paint.Style.FILL);
            mBigCircleCenterPaint.setColor(bigCircleCenterColor);
            mBigCircleCenterPaint.setAntiAlias(true);
        }

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);

        currentPosition = countOfSmallCircle / 2;

    }

    /**
     * 设置显示文本
     *
     * @param strings
     */
    public void setString(String[] strings) {
        text4Rates = strings;
        textWidth = mTextPaint.measureText(text4Rates[0]);

        if (countOfSmallCircle != text4Rates.length) {
            throw new IllegalArgumentException("the count of small circle must be equal to the " +
                    "text array length !");
        }

    }

    /**
     * 设置监听器
     *
     * @param listener
     */
    public void setOnSelectListener(onSelectListener listener) {
        selectListener = listener;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        //画中间的线
        drawLine(canvas);

        //画小圆
        if (smallCircleRadius != 0) {
            for (int i = 0; i < countOfSmallCircle; i++) {
                canvas.drawCircle(circlesX[i], mHeight / 2, smallCircleRadius, mSmallCirclePaint);
            }
        }

        //画大圆的默认位置
        canvas.drawCircle(bigCircleX, mHeight / 2, bigCircleRadius, mBigCirclePaint);

        //如果圆心半径不为0，绘制圆心
        if (bigCircleCenterRadius != 0) {
            canvas.drawCircle(bigCircleX, mHeight / 2, bigCircleCenterRadius, mBigCircleCenterPaint);
        }

        //画文字
        for (int i = 0, size = text4Rates.length; i < size; i++) {
            textWidth = mTextPaint.measureText(text4Rates[i]);
            if (i == currentPosition) {
                mTextPaint.setColor(textSelectColor);
            } else {
                mTextPaint.setColor(textColor);
            }
            canvas.drawText(text4Rates[i],
                    circlesX[i] - textWidth / 2,
                    (mHeight / 2) + bigCircleRadius * 2 + textTopMargin,
                    mTextPaint);
        }
    }

    //修改线的类型，如果为true，线一直未一种颜色，如果为false，头到大圆的位置为大圆的颜色，大圆到尾，是线原本的颜色。
    private boolean lineMode = false;

    private void drawLine(Canvas canvas) {
        if (lineMode) {
            canvas.drawLine(MARGEN_LINE, mHeight / 2, mWidth - MARGEN_LINE, mHeight / 2,
                    mLinePaint);
        } else {
            mLinePaint.setColor(bigCircleColor);
            canvas.drawLine(MARGEN_LINE, mHeight / 2, bigCircleX, mHeight / 2,
                    mLinePaint);
            mLinePaint.setColor(lineColor);
            canvas.drawLine(bigCircleX, mHeight / 2, mWidth - MARGEN_LINE, mHeight / 2, mLinePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:

                startX = event.getX();
                //如果手指按下的x坐标与大圆的x坐标的距离小于半径，则是follow模式
                if (Math.abs(startX - bigCircleX) <= bigCircleRadius) {
                    isFollowMode = true;
                } else {
                    isFollowMode = false;
                }

                break;
            case MotionEvent.ACTION_MOVE:

                //如果是follow模式，则大圆跟随手指移动
                if (isFollowMode) {
                    //防止滑出边界
                    if (event.getX() >= MARGEN_LINE && event.getX() <= (mWidth - MARGEN_LINE)) {
                        //LogHelper.d("TAG", "event.getX()=" + event.getX() + "__mWidth=" + mWidth);
                        bigCircleX = event.getX();
                        int position = (int) ((event.getX() - MARGEN_LINE) / (distanceX / 2));
                        //更新当前位置
                        currentPosition = (position + 1) / 2;
                        invalidate();
                    }

                }

                break;
            case MotionEvent.ACTION_UP:

                if (isFollowMode) {
                    float endX = event.getX();
                    //当前位置距离最近的小白点的距离
                    float currentDistance = endX - MARGEN_LINE - currentPosition * distanceX;
                    //拉到最后或者最头
                    if ((currentPosition == 0 && currentDistance < 0) || (currentPosition == (text4Rates.length - 1) && currentDistance > 0)) {
                        bigCircleX = currentPosition * distanceX + MARGEN_LINE;
                        invalidate();
                        if (null != selectListener) {
                            selectListener.onSelect(currentPosition);
                        }
                        return true;
                    }

                    currentPositionX = bigCircleX;

                    valueAnimator = ValueAnimator.ofFloat(currentDistance);
                    valueAnimator.setInterpolator(new AccelerateInterpolator());
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float slideDistance = (float) animation.getAnimatedValue();
                            bigCircleX = currentPositionX - slideDistance;
                            invalidate();
                        }
                    });

                    valueAnimator.setDuration(100);
                    valueAnimator.start();
                    if (null != selectListener) {
                        selectListener.onSelect(currentPosition);
                    }
                } else {
                    float endX = event.getX();
                    //当前位置距离最近的小白点的距离
                    float currentDistance = endX - MARGEN_LINE - currentPosition * distanceX;

                    // 是否点击到小圆，点击误差小于allowDistanErr
                    float distanceErr = currentDistance % distanceX;
                    int allowDistanErr = 20; // 允许出错范围
                    boolean isClickCircle = Math.abs(distanceErr) < allowDistanErr || distanceX - Math.abs(distanceErr) < allowDistanErr;
                    if (isClickCircle) {
                        // 计算当前点击的pos位置
                        int currentDistancePositon = (int) (currentDistance / distanceX);
                        if (distanceErr > 0 && distanceErr > allowDistanErr) {
                            currentDistancePositon++;
                        } else if (distanceErr < 0 && Math.abs(distanceErr) > allowDistanErr) {
                            currentDistancePositon--;
                        }

                        currentDistance = currentDistancePositon * distanceX;
                        currentPosition = currentPosition + currentDistancePositon;
                        currentPositionX = bigCircleX;
                        bigCircleX = currentPositionX + currentDistance;
                        invalidate();
                        if (null != selectListener) {
                            selectListener.onSelect(currentPosition);
                        }
                    }
                }

                break;
        }


        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
        //计算每个小圆点的x坐标
        circlesX = new float[countOfSmallCircle];
        distanceX = (mWidth - MARGEN_LINE * 2) / (countOfSmallCircle - 1);
        for (int i = 0; i < countOfSmallCircle; i++) {
            circlesX[i] = i * distanceX + MARGEN_LINE;
        }

        bigCircleX = circlesX[currentPosition];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int screenSize[] = getScreenSize();

        int resultWidth;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            resultWidth = widthSize;
        } else {
            resultWidth = screenSize[0];

            if (widthMode == MeasureSpec.AT_MOST) {
                resultWidth = Math.min(widthSize, screenSize[0]);
            }
        }

        int resultHeight;
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            resultHeight = heightSize;
        } else {
            resultHeight = (int) (bigCircleRadius * 6);

            if (heightMode == MeasureSpec.AT_MOST) {
                resultHeight = Math.min(heightSize, resultHeight);
            }
        }

        setMeasuredDimension(resultWidth, resultHeight);

    }

    private int[] getScreenSize() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return new int[]{displayMetrics.widthPixels, displayMetrics.heightPixels};
    }

    public interface onSelectListener {
        public void onSelect(int index);
    }

    /**
     * 上一个
     */
    public void pre() {
        if (currentPosition > 0) {
            currentPosition--;
            bigCircleX = currentPositionX = bigCircleX - distanceX;
            invalidate();
            if (null != selectListener) {
                selectListener.onSelect(currentPosition);
            }
        }

    }

    /**
     * 下一个
     */
    public void next() {
        if (currentPosition < (text4Rates.length - 1)) {
            currentPosition++;
            bigCircleX = currentPositionX = bigCircleX + distanceX;
            invalidate();
            if (null != selectListener) {
                selectListener.onSelect(currentPosition);
            }
        }
    }

    //设置当前颜色
    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        bigCircleX = currentPositionX = currentPosition * distanceX;
        invalidate();
    }


}