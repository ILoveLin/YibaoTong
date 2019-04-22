package com.lzyc.ybtappcal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lzyc.ybtappcal.R;

/**
 * 自定义listview实现圆角滑动
 * Created by yang on 2016/11/22.
 */
public class RoundedRectListView extends ListView {
    private Path mClip;
    private int radiusUpTopLeft=0;
    private int radiusUpTopRight=0;
    private int radiusUpBottomLeft=0;
    private int radiusUpBottomRight=0;
    private int radiusDownTopLeft=0;
    private int radiusDownTopRight=0;
    private int radiusDownBottomLeft=0;
    private int radiusDownBottomRight=0;
    public RoundedRectListView( Context context, AttributeSet attrs) {
        super (context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedRectListView);
        this.radiusUpTopLeft= (int) mTypedArray.getDimension(R.styleable.RoundedRectListView_radiusUpTopLeft, 0);
        this.radiusUpTopRight= (int) mTypedArray.getDimension(R.styleable.RoundedRectListView_radiusUpTopRight, 0);
        this.radiusUpBottomLeft= (int) mTypedArray.getDimension(R.styleable.RoundedRectListView_radiusUpBottomLeft, 0);
        this.radiusUpBottomRight= (int) mTypedArray.getDimension(R.styleable.RoundedRectListView_radiusUpBottomRight, 0);
        this.radiusDownTopLeft= (int) mTypedArray.getDimension(R.styleable.RoundedRectListView_radiusDownTopLeft, 0);
        this.radiusDownTopRight= (int) mTypedArray.getDimension(R.styleable.RoundedRectListView_radiusDownTopRight, 0);
        this.radiusDownBottomLeft= (int) mTypedArray.getDimension(R.styleable.RoundedRectListView_radiusDownBottomLeft, 0);
        this.radiusDownBottomRight= (int) mTypedArray.getDimension(R.styleable.RoundedRectListView_radiusDownBottomRight, 0);
        mTypedArray.recycle();
    }

    @Override
    protected void onSizeChanged( int w, int h, int oldw, int oldh) {
        super .onSizeChanged (w, h, oldw, oldh) ;
        mClip = new Path( ) ;
        RectF rect = new RectF( 0, 0, w, h) ;
        float [] radii=new float[]{radiusUpTopLeft,radiusUpTopRight,radiusUpBottomLeft,radiusUpBottomRight,radiusDownTopLeft,radiusDownTopRight,radiusDownBottomLeft,radiusDownBottomRight};
        mClip.addRoundRect(rect,radii,Path.Direction.CW) ;
    }
    @Override
    protected void dispatchDraw( Canvas canvas) {
        canvas.save ( ) ;
        canvas.clipPath (mClip) ;
        super .dispatchDraw ( canvas) ;
        canvas.restore ( ) ;
    }
}