package com.lzyc.ybtappcal.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.lzyc.ybtappcal.R;

/**
 * Created by lxx on 2017/4/18.
 */

public class DiffuseView extends View {

    /** 扩散圆圈颜色 */
    private int mColor = getResources().getColor(R.color.color_ffffff);

    /** 是否正在扩散中 */
    private boolean mIsDiffuse = false;

    private Paint mPaint;

    public DiffuseView(Context context) {
        this(context, null);
    }

    public DiffuseView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DiffuseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

    }

    @Override
    public void invalidate() {
//        if(hasWindowFocus()){
            super.invalidate();
//        }
    }

    private float wP = 0;
    private float hP = 0;

    private float wP2 = 0;
    private float hP2 = 0;

    private boolean is2AnimStart = false;

    private int alpha1 = 150;
    private int alpha2 = 150;

    @Override
    public void onDraw(Canvas canvas) {
        // 绘制扩散圆
        mPaint.setColor(mColor);

        int w = getWidth();
        int h = getHeight();

        float centerX = w/2;
        float centerY = h/2;

        mPaint.setAlpha(alpha1);
        RectF oval = new RectF(centerX-wP, centerY-hP, centerX+wP, centerY+hP);
        canvas.drawOval(oval, mPaint);
        wP = wP+1;
        hP = hP+(2f/5f);
        alpha1 = alpha1 - (int)(150/centerX);
        if(wP >= centerX){
            wP = 0;
            hP = 0;
            alpha1 = 150;
        }

        if(wP>centerX/2){
            is2AnimStart = true;
        }

        if(is2AnimStart) {
            mPaint.setAlpha(alpha2);
            RectF oval2 = new RectF(centerX - wP2, centerY - hP2, centerX + wP2, centerY + hP2);
            canvas.drawOval(oval2, mPaint);
            wP2 = wP2 + 1;
            hP2 = hP2 + (2f / 5f);
            alpha2 = alpha2 - (int)(150/centerX);
            if (wP2 >= centerX) {
                wP2 = 0;
                hP2 = 0;
                alpha2 = 150;
            }
        }
        if(mIsDiffuse) {
            postInvalidateDelayed(20);
        }
    }

    /**
     * 开始扩散
     */
    public void start() {
        mIsDiffuse = true;
        invalidate();
    }

    /**
     * 停止扩散
     */
    public void stop() {
        mIsDiffuse = false;
    }

    /**
     * 是否扩散中
     */
    public boolean isDiffuse(){
        return mIsDiffuse;
    }


    /**
     * 设置扩散圆颜色
     */
    public void setColor(int colorId){
        mColor = colorId;
    }


}
