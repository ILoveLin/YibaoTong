package com.lzyc.ybtappcal.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;

import com.lzyc.ybtappcal.util.AndtoidRomUtil;
import com.lzyc.ybtappcal.util.DensityUtils;

/**
 * Created by lovelin on 2016/12/2.
 * 自定义 mineFragment 水波纹动画
 */
public class DynamicWave extends View {

    // 波纹颜色
    private static final int WAVE_PAINT_COLOR = 0x880000aa;
    // y = Asin(wx+b)+h
    private static Double STRETCH_FACTOR_A = 45.0;       //A---振幅
    private static final int OFFSET_Y = 0;                  //h---初像
    // 第一条水波移动速度
    private static final Double TRANSLATE_X_SPEED_ONE = 1.2;
    // 第二条水波移动速度
    private static final Double TRANSLATE_X_SPEED_TWO = 0.8;
    private final Paint mWavePaint_02;                    //不同颜色的波浪
    private float mCycleFactorW;

    private int mTotalWidth, mTotalHeight;
    private float[] mYPositions;
    private float[] mResetOneYPositions;
    private float[] mResetTwoYPositions;
    private Double mXOffsetSpeedOne;
    private Double mXOffsetSpeedTwo;
    private int mXOneOffset;
    private int mXTwoOffset;

    private Paint mWavePaint;
    private DrawFilter mDrawFilter;
    private double S1;
    private double S2;

    public DynamicWave(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 将dp转化为px，用于控制不同分辨率上移动速度基本一致
        STRETCH_FACTOR_A = DensityUtils.dipToPx(context, 15.0);
        if (AndtoidRomUtil.isMIUI()) {
            S1 = 55;
            S2 = 50;
            mXOffsetSpeedOne = 1.1;
            mXOffsetSpeedTwo = 0.60;
        } else {
            S1 = 85;
            S2 = 80;
            mXOffsetSpeedOne = DensityUtils.dipToPx(context, TRANSLATE_X_SPEED_ONE);
            mXOffsetSpeedTwo = DensityUtils.dipToPx(context, TRANSLATE_X_SPEED_TWO);
        }


        // 初始绘制波纹的画笔
        mWavePaint = new Paint();
        mWavePaint_02 = new Paint();
        // 去除画笔锯齿
        mWavePaint.setAntiAlias(true);
        mWavePaint_02.setAntiAlias(true);
        // 设置风格为实线
        mWavePaint.setStyle(Paint.Style.FILL);
        mWavePaint_02.setStyle(Paint.Style.FILL);
        // 设置画笔颜色
//        mWavePaint.setColor(WAVE_PAINT_COLOR);
        mWavePaint.setColor(Color.parseColor("#10FFFFFF"));
        mWavePaint_02.setColor(Color.parseColor("#10FFFFFF"));
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 从canvas层面去除绘制时锯齿
        canvas.setDrawFilter(mDrawFilter);
        resetPositonY();
        for (int i = 0; i < mTotalWidth; i++) {

            // 减400只是为了控制波纹绘制的y的在屏幕的位置，大家可以改成一个变量，然后动态改变这个变量，从而形成波纹上升下降效果
            // 绘制第一条水波纹
            /**
             * mResetOneYPositions[i] - 400   -400值得是距离底部的位置
             */
            canvas.drawLine(i, (float) (mTotalHeight - mResetOneYPositions[i] - S1), i,
                    mTotalHeight,
                    mWavePaint);

            // 绘制第二条水波纹
            canvas.drawLine(i, (float) (mTotalHeight - mResetTwoYPositions[i] - S2), i,
                    mTotalHeight,
                    mWavePaint_02);
        }

        // 改变两条波纹的移动点
        mXOneOffset += mXOffsetSpeedOne;
        mXTwoOffset += mXOffsetSpeedTwo;

        // 如果已经移动到结尾处，则重头记录
        if (mXOneOffset >= mTotalWidth) {
            mXOneOffset = 0;
        }
        if (mXTwoOffset > mTotalWidth) {
            mXTwoOffset = 0;
        }

        // 引发view重绘，一般可以考虑延迟20-30ms重绘，空出时间片
        postInvalidate();
    }

    private void resetPositonY() {
        // mXOneOffset代表当前第一条水波纹要移动的距离
        int yOneInterval = mYPositions.length - mXOneOffset;
        // 使用System.arraycopy方式重新填充第一条波纹的数据
        System.arraycopy(mYPositions, mXOneOffset, mResetOneYPositions, 0, yOneInterval);
        System.arraycopy(mYPositions, 0, mResetOneYPositions, yOneInterval, mXOneOffset);

        int yTwoInterval = mYPositions.length - mXTwoOffset;
        System.arraycopy(mYPositions, mXTwoOffset, mResetTwoYPositions, 0,
                yTwoInterval);
        System.arraycopy(mYPositions, 0, mResetTwoYPositions, yTwoInterval, mXTwoOffset);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 记录下view的宽高
        mTotalWidth = w;
        mTotalHeight = h;
        // 用于保存原始波纹的y值
        mYPositions = new float[mTotalWidth];
        // 用于保存波纹一的y值
        mResetOneYPositions = new float[mTotalWidth];
        // 用于保存波纹二的y值
        mResetTwoYPositions = new float[mTotalWidth];

        // 将周期定为view总宽度
        mCycleFactorW = (float) (2 * Math.PI / mTotalWidth);
//        mCycleFactorW=mCycleFactorW*1.5f;
        // 根据view总宽度得出所有对应的y值
        for (int i = 0; i < mTotalWidth; i++) {
            mYPositions[i] = (float) (STRETCH_FACTOR_A * Math.sin(mCycleFactorW * i) + OFFSET_Y);
        }
    }
}

