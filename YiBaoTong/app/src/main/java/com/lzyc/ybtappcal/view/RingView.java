package com.lzyc.ybtappcal.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lxx on 2017/4/19.
 */

public class RingView extends View {

    private final Paint paint;
    private final Context context;

    public RingView(Context context) {
        this(context, null);
    }

    public RingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.paint = new Paint();
        this.paint.setAntiAlias(true); //消除锯齿
        this.paint.setStyle(Paint.Style.STROKE); //绘制空心圆
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int center = getWidth()/2;
        int innerCircle = dip2px(context, 35); //设置内圆半径
        int ringWidth = dip2px(context, 5); //设置圆环宽度

        //绘制圆环
        this.paint.setARGB(255, 28 ,208, 171);
        this.paint.setStrokeWidth(2);
        canvas.drawCircle(center,center, innerCircle+1+ringWidth/2, this.paint);

        super.onDraw(canvas);
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
