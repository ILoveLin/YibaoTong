package com.lzyc.ybtappcal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.lzyc.ybtappcal.R;

/**
 * 创建者： 丁立鹏
 * 创建日期： 2016/2/25
 * 创建时间： 10:19
 * 包名： com.lzyc.ybtappcal.view
 */
public class RoundProgressBar extends View {
    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private float progress;
    private float cProgress;
    /**
     * 是否显示中间的进度
     */
    private boolean textIsDisplayable;

    /**
     * 进度的风格，实心或者空心
     */
    private int style;

    public static final int STROKE = 0;
    public static final int FILL = 1;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint = new Paint();


        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBar);

        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.parseColor("#e7e7e7"));
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor,  Color.parseColor("#90bcfa"));
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor,  Color.parseColor("#3e6398"));
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize0, 30);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_width, 5);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_styles, 0);

        mTypedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画最外层的大圆环
         */
        int centre = getWidth()/2; //获取圆心的x坐标
        int radius = (int) (centre - roundWidth/2); //圆环的半径
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centre, centre, radius, paint); //画出圆环

//        paint = new Paint();
//        paint.setColor(roundColor); //设置圆环的颜色
//        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
//        paint.setAntiAlias(true);  //消除锯齿
//        canvas.drawCircle(10, 10, 10, paint);
        /**
         * 画进度百分比
         */
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
        float percent = (((float)progress / (float)max) * (float)100);  //中间的进度百分比，先转换成float在进行除法运算，不然都为0
        float textWidth = paint.measureText(percent + "%");   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间

//        if(textIsDisplayable && percent != 0 && style == STROKE){
        if(textIsDisplayable && style == STROKE){
            canvas.drawText(percent + "%", centre - textWidth / 2, centre + textSize/2, paint); //画出进度百分比
        }


        /**
         * 画圆弧 ，画圆环的进度
         */

        //设置进度是实心还是空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setColor(roundProgressColor);  //设置进度的颜色
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius);  //用于定义的圆弧的形状和大小的界限

        switch (style) {
            case STROKE:{
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, 90, 360 * progress / max, false, paint);  //根据进度画圆弧
                break;
            }
//            case FILL:{
//                paint.setStyle(Paint.Style.FILL_AND_STROKE);
//                if(progress !=0)
//                    canvas.drawArc(oval, 0, 360 * progress / max, true, paint);  //根据进度画圆弧
//                break;
//            }
        }

        //画圆圈
//        double x=0;
//        double y=0;
//        int act = 360 * progress / max;
//            float rRadius=radius-roundWidth/2;
//        if(act<=90){
//            x=rRadius-rRadius*Math.sin(act);
//            y=rRadius+rRadius*Math.cos(act);
//        }else if(act>90&&act<=180){
//            x=rRadius-rRadius*Math.cos(act);
//            y=rRadius-rRadius*Math.sin(act);
//        }else if(act>180&&act<=270){
//            x=rRadius+rRadius*Math.sin(act);
//            y=rRadius-rRadius*Math.cos(act);
//        }else if(act>270&&act<=360){
//            x=rRadius+rRadius*Math.cos(act);
//            y=rRadius+rRadius*Math.sin(act);
//        }
//
//        Log.i("------------------",x+"-------------"+y);
//
//        int mRadius = (int)roundWidth*2; //圆环的半径
//        paint.setColor(roundColor); //设置圆环的颜色
//        paint.setStyle(Paint.Style.STROKE); //设置空心
//        paint.setAntiAlias(true);  //消除锯齿
//        canvas.drawCircle((float)x, (float)y, mRadius, paint); //画出圆环

    }


    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     * @param max
     */
    public synchronized void setMax(int max) {
        if(max < 0){
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     * @return
     */
    public synchronized float getProgress() {
        return progress;
    }





    private Handler handler = new Handler();
    private MyRunnable runnable;
    class MyRunnable implements Runnable {
        @Override
        public void run() {

            if (progress<cProgress) {
                ++progress;
                if (progress>cProgress){
                    progress=cProgress;
                }
                postInvalidate();
                handler.postDelayed(runnable, 10);
            }
        }
    }
    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     * @param progress
     */
    public synchronized void setProgress(float progress) {
        if(progress < 0){
            throw new IllegalArgumentException("progress not less than 0");
        }
        if(progress > max){
            progress = max;
        }
        if(progress <= max){
            this.cProgress = progress;

            runnable = new MyRunnable();

            handler.postDelayed(runnable, 10);
        }

    }



    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }



}