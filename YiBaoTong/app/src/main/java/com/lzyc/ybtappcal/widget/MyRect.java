package com.lzyc.ybtappcal.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.util.ScreenUtils;

/**
 * Created by xujm on 2016/2/26.
 * 自定义view显示统计数量的 动态加载
 */
public class MyRect extends View {


    private int mWidth;
    private int mHeight;
    private int rectHeight;
    private int textSize;

    private int nrectHeight1;
    private int nrectHeight2;
    private int nrectHeight3;
    private Handler handler = new Handler();
    private MyRunnable1 runnable1;
    private MyRunnable2 runnable2;
    private MyRunnable3 runnable3;
    private Canvas mcanvas;
    private int rectWidth;
    private String price1="¥120.00";
    private String price2="¥120.00";
    private String price3="¥120.00";

    public String getPrice1() {
        return price1;
    }

    public void setPrice1(String price1) {
        this.price1 = price1;
    }

    public String getPrice2() {
        return price2;
    }

    public void setPrice2(String price2) {
        this.price2 = price2;
    }

    public String getPrice3() {
        return price3;
    }

    public void setPrice3(String price3) {
        this.price3 = price3;
    }

    public int getmWidth() {
        return mWidth;
    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public int getmHeight() {
        return mHeight;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public MyRect(Context context) {
        this(context, null, 0);
    }

    public MyRect(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void init() {
        textSize= ScreenUtils.getScreenWidth()/100*4;
        rectHeight = mHeight-10-textSize;

        rectWidth = mWidth / 8;
        nrectHeight1 = rectHeight - 1;
        nrectHeight2 = rectHeight - 1;
        nrectHeight3 = rectHeight - 1;
        runnable1 = new MyRunnable1();
        runnable2 = new MyRunnable2();
        runnable3 = new MyRunnable3();

        handler.postDelayed(runnable1, 10);
        handler.postDelayed(runnable2, 10);
        handler.postDelayed(runnable3, 10);
    }


    class MyRunnable1 implements Runnable {
        @Override
        public void run() {
            if (nrectHeight1 > 0.8*rectHeight+0.2*textSize+2) {

                invalidate();
                nrectHeight1--;
                handler.postDelayed(runnable1, 10);
            }
        }
    }

    class MyRunnable2 implements Runnable {
        @Override
        public void run() {
            if (nrectHeight2 >  0.6*rectHeight+0.4*textSize+4) {

                invalidate();
                nrectHeight2--;
                handler.postDelayed(runnable2, 10);
            }
        }
    }

    class MyRunnable3 implements Runnable {
        @Override
        public void run() {
            if (nrectHeight3 > textSize+10) {
                invalidate();
                nrectHeight3--;
                handler.postDelayed(runnable3, 10);
                System.out.println("nrectHeight3" + nrectHeight3);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mcanvas = canvas;
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);


        mcanvas.drawText("可报销", 0, textSize + 5, textPaint);

        Rect bounds = new Rect();
        String text1 = "10次";
        textPaint.getTextBounds(text1, 0, text1.length(), bounds);

        mcanvas.drawText(text1, rectWidth*3/2-bounds.width()/2, rectHeight+textSize+5, textPaint);
        mcanvas.drawText("20次", rectWidth*7/2-bounds.width()/2, rectHeight+textSize+5, textPaint);
        mcanvas.drawText("50次", rectWidth*11/2-bounds.width()/2, rectHeight+textSize+5, textPaint);
        mcanvas.drawText("(用药次数)", rectWidth*6+15, rectHeight+textSize+5, textPaint);

        Rect bounds1 = new Rect();
        textPaint.getTextBounds(price1, 0, price1.length(), bounds1);
        textPaint.setColor(Color.parseColor("#6699CC"));
        mcanvas.drawText(price1, rectWidth*3/2-bounds1.width()/2, nrectHeight1-5, textPaint);

        Rect bounds2 = new Rect();
        textPaint.getTextBounds(price2, 0, price2.length(), bounds2);
        textPaint.setColor(Color.parseColor("#3E6398"));
        mcanvas.drawText(price2, rectWidth*7/2-bounds2.width()/2, nrectHeight2-5, textPaint);

        Rect bounds3 = new Rect();
        textPaint.getTextBounds(price3, 0, price3.length(), bounds3);
        textPaint.setColor(Color.parseColor("#0C284F"));
        mcanvas.drawText(price3, rectWidth*11/2-bounds3.width()/2, nrectHeight3-5, textPaint);



        drawRect1(mcanvas);
        drawRect2(mcanvas);
        drawRect3(mcanvas);
    }


    private void drawRect1(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.parseColor("#C3DBF3"));
        p.setStyle(Paint.Style.FILL);//设置填满
        canvas.drawRect(rectWidth, nrectHeight1, rectWidth*2, rectHeight, p);// 长方形
    }

    private void drawRect2(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.parseColor("#6699CC"));
        p.setStyle(Paint.Style.FILL);//设置填满
        canvas.drawRect(rectWidth*3, nrectHeight2, rectWidth*4, rectHeight, p);// 长方形
    }

    private void drawRect3(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.parseColor("#3E6398"));// 设置红色
        p.setStyle(Paint.Style.FILL);//设置填满
        canvas.drawRect(rectWidth*5, nrectHeight3, rectWidth*6, rectHeight, p);// 长方形
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacks(runnable1);
        handler.removeCallbacks(runnable2);
        handler.removeCallbacks(runnable3);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    //    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int resultWidth = 0;
//        // 获取宽度测量规格中的mode
//        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
//        // 获取宽度测量规格中的size
//        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
//        if (modeWidth == MeasureSpec.EXACTLY) {
//            // 那么儿子也不要让爹难做就取爹给的大小吧
//            resultWidth = sizeWidth;
//        }
//        else {
//            // 那么儿子可要自己看看自己需要多大了
//            resultWidth = this.getWidth();
//
//        /*
//         * 如果爹给儿子的是一个限制值
//         */
//            if (modeWidth == MeasureSpec.AT_MOST) {
//                // 那么儿子自己的需求就要跟爹的限制比比看谁小要谁
//                resultWidth = Math.min(resultWidth, sizeWidth);
//            }
//        }
//
//        int resultHeight = 0;
//        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
//        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
//
//        if (modeHeight == MeasureSpec.EXACTLY) {
//            resultHeight = sizeHeight;
//        } else {
//            resultHeight = this.getHeight();
//            if (modeHeight == MeasureSpec.AT_MOST) {
//                resultHeight = Math.min(resultHeight, sizeHeight);
//            }
//        }
//
//        // 设置测量尺寸
//        setMeasuredDimension(resultWidth, resultHeight);
//    }
}