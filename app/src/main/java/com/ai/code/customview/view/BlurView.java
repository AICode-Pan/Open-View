package com.ai.code.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.ai.code.customview.R;

/**
 * Created by AICode-Pan on 20/06/20.
 */
public class BlurView extends View {
    private Bitmap bitmap = null;
    private Paint paint = null;
    private BitmapFactory.Options options = null;
    private long lastActionTime = 0;
    //缩减比例,太小会失真
    private float downScaleFactor = 0.8f;
    //视图圆角处理
    private float mRadius = 0;
    //为了使效果更接近IOS的效果，最后绘制一个蒙板盖上面。
    private int maskColor = 0;
    //模糊程度, 25f是最大模糊度, 越大绘制也越慢。
    private float mBlurRadius = 0;
    // 创建RenderScript内核对象
    private RenderScript rs = null;
    // 创建一个模糊效果的RenderScript的工具对象
    private ScriptIntrinsicBlur blurScript = null;

    private int alpha = 255;

    public BlurView(Context context) {
        super(context);
        init();
    }

    public BlurView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        getAttr(context, attrs);
    }

    public BlurView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        getAttr(context, attrs);
    }

    private void init() {
        //关闭硬件加速
        setLayerType(View.LAYER_TYPE_HARDWARE, null);

        paint = new Paint();
        options = new BitmapFactory.Options();
        //NORMAL: 内外都模糊绘制
        //SOLID: 内部正常绘制，外部模糊
        //INNER: 内部模糊，外部不绘制
//        paint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.NORMAL));

        rs = RenderScript.create(getContext());
        blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
    }

    /**
     * 设置遮罩颜色
     * @param color
     */
    public void setMaskColor(int color) {
        this.maskColor = color;
    }

    /**
     * 设置模糊程度
     * @param radius
     */
    public void setBlurRadius(int radius) {
        this.mBlurRadius = radius;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    private void getAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BlurView);
        //压缩比例，
        downScaleFactor = typedArray.getFloat(R.styleable.BlurView_downScale, 0.8f);
        //是否裁剪圆角
        mRadius = typedArray.getDimensionPixelSize(R.styleable.BlurView_circleRadius, 0);
        //背景色
        maskColor = typedArray.getColor(R.styleable.BlurView_maskColor, 0);
        //模糊度
        mBlurRadius = typedArray.getFloat(R.styleable.BlurView_blurRadius, 0);
        typedArray.recycle();
    }

    /**
     * 获取需要Bitmap
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    private void setBlurBitmapFromView(int left, int top, int right, int bottom) {
        //获取父View
        ViewGroup parent = (ViewGroup) getParent();
        //将此布局从父View移除,在截图不截进此视图。
        parent.removeView(this);
        //获取对应位置的Bitmap对象
        bitmap = getDownscaledBitmapForView(parent, new Rect(left, top, right, bottom), downScaleFactor);
        //截完图获取到bitmap之后，再把此View加载到父布局上
        parent.addView(this);
        printfActionTime("图片处理");

        //模糊处理
        bitmap = blurBitmap(bitmap, mBlurRadius);
        printfActionTime("模糊处理");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        Log.i("BlurView", "onLayoutBlurView changed=" + changed + " left=" + left + " ,top=" + top + " ,right=" + right + " ,bottom=" + bottom);

        lastActionTime = System.currentTimeMillis();
        //获取Bitmap从View

        if (mBlurRadius > 0 && mBlurRadius < 25) {
            setBlurBitmapFromView(getLeft(), getTop(), getRight(), getBottom());
        } else {
            bitmap = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path mPath = new Path();

        paint.setAlpha(alpha);

        if (mRadius > 0) {
            mPath.addRoundRect(new RectF(0, 0, getWidth(), getHeight()) , mRadius, mRadius, Path.Direction.CW);
            canvas.clipPath(mPath);
        }

        //绘制
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, null, new Rect(0, 0, getWidth(), getHeight()), paint);
        }
        printfActionTime("绘制完成");

        if (maskColor != 0) {
            canvas.drawColor(maskColor);
        }
    }

    /**
     * 模糊处理
     * @param image
     * @param blurRadius
     * @return
     */
    public Bitmap blurBitmap(Bitmap image, float blurRadius) {
        // 创建一张渲染后的输出图片
        Bitmap outputBitmap = Bitmap.createBitmap(image);
//        Log.i("BlurView", "onLayoutBlurView bitmap.size=" + inputBitmap.getByteCount());

        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
        Allocation tmpIn = Allocation.createFromBitmap(rs, image);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(blurRadius);
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut);

        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);
        image.recycle();
        rs.destroy();

        return outputBitmap;
    }

    /**
     * 打印方法执行时间
     * @param function
     */
    private void printfActionTime(String function) {
        long time = System.currentTimeMillis();
        Log.i("ActionTime", function + " time=" + (time - lastActionTime));
        lastActionTime = time;
    }

    /**
     * 裁剪View，View转Bitmap，同时进行Bitmap压缩
     * @param view
     * @param crop
     * @param downscaleFactor 压缩比例
     * @return
     */
    private Bitmap getDownscaledBitmapForView(View view, Rect crop, float downscaleFactor) {
        int width = Math.round(crop.width() * downscaleFactor);
        int height = Math.round(crop.height() * downscaleFactor);

        if (view.getWidth() <= 0 || view.getHeight() <= 0 || width <= 0 || height <= 0) {
            throw new IllegalArgumentException("No screen available (width or height = 0)");
        }

        float dx = -crop.left * downscaleFactor;
        float dy = -crop.top * downscaleFactor;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Matrix matrix = new Matrix();
        matrix.preScale(downscaleFactor, downscaleFactor);
        matrix.postTranslate(dx, dy);
        canvas.setMatrix(matrix);
        view.draw(canvas);

        return bitmap;
    }
}
