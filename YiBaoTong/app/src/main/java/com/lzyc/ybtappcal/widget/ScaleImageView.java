package com.lzyc.ybtappcal.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * 圆型Image
 *
 * @author luxf 2015-6-10 下午5:45:21
 */
public class ScaleImageView extends ImageView implements OnTouchListener {
    private Context mContext;
    private float MAX_SCALE = 2f;

    private Matrix mMatrix;
    private final float[] mMatrixValues = new float[9];

    // display width height.
    private int mWidth;
    private int mHeight;

    private int mIntrinsicWidth;
    private int mIntrinsicHeight;

    private float mScale;
    private float mMinScale;

    private float mPrevDistance;
    private boolean isScaling;

    private int mPrevMoveX;
    private int mPrevMoveY;
    private GestureDetector mDetector;

    String TAG = "ScaleImageView";

    public ScaleImageView(Context context, AttributeSet attr) {
        super(context, attr);
        this.mContext = context;
        initialize();
    }

    public ScaleImageView(Context context) {
        super(context);
        this.mContext = context;
        initialize();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        this.initialize();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        this.initialize();
    }

    private void initialize() {
        this.setScaleType(ScaleType.MATRIX);
        this.mMatrix = new Matrix();
        Drawable d = getDrawable();
        if (d != null) {
            mIntrinsicWidth = d.getIntrinsicWidth();
            mIntrinsicHeight = d.getIntrinsicHeight();
            setOnTouchListener(this);
        }
        mDetector = new GestureDetector(mContext,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        maxZoomTo((int) e.getX(), (int) e.getY());
                        cutting();
                        return super.onDoubleTap(e);
                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        Log.i(TAG, "onSingleTapUp");
                        if (mOnSingleTabUpListener != null) {
                            mOnSingleTabUpListener.onSingleTabUp(e);
                        }
                        return super.onSingleTapUp(e);
                    }
                });
    }

    private OnSingleTabUpListener mOnSingleTabUpListener;

    public interface OnSingleTabUpListener {
        public void onSingleTabUp(MotionEvent e);
    }

    public void setOnSingleTabUpListener(
            OnSingleTabUpListener onSingleTabUpListener) {
        mOnSingleTabUpListener = onSingleTabUpListener;
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        mWidth = r - l;
        mHeight = b - t;

        mMatrix.reset();
        int r_norm = r - l;
        mScale = (float) r_norm / (float) mIntrinsicWidth;

        int paddingHeight = 0;
        int paddingWidth = 0;
        // scaling vertical
        if (mScale * mIntrinsicHeight > mHeight) {
            mScale = (float) mHeight / (float) mIntrinsicHeight;
            mMatrix.postScale(mScale, mScale);
            paddingWidth = (r - mWidth) / 2;
            paddingHeight = 0;
            // scaling horizontal
        } else {
            mMatrix.postScale(mScale, mScale);
            paddingHeight = (b - mHeight) / 2;
            paddingWidth = 0;
        }
        mMatrix.postTranslate(paddingWidth, paddingHeight);

        setImageMatrix(mMatrix);
        mMinScale = mScale;
        zoomTo(mScale, mWidth / 2, mHeight / 2);
        cutting();
        return super.setFrame(l, t, r, b);
    }

    protected float getValue(Matrix matrix, int whichValue) {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[whichValue];
    }

    protected float getScale() {
        return getValue(mMatrix, Matrix.MSCALE_X);
    }

    public float getTranslateX() {
        return getValue(mMatrix, Matrix.MTRANS_X);
    }

    protected float getTranslateY() {
        return getValue(mMatrix, Matrix.MTRANS_Y);
    }

    protected void maxZoomTo(int x, int y) {
        if (mMinScale != getScale() && (getScale() - mMinScale) > 0.1f) {
            // threshold 0.1f
            float scale = mMinScale / getScale();
            zoomTo(scale, x, y);
        } else {
            float scale = MAX_SCALE / getScale();
            zoomTo(scale, x, y);
        }
    }

    public void zoomTo(float scale, int x, int y) {
        if (getScale() * scale < mMinScale) {
            return;
        }
        if (scale >= 1 && getScale() * scale > MAX_SCALE) {
            return;
        }
        mMatrix.postScale(scale, scale);
        // move to center
        mMatrix.postTranslate(-(mWidth * scale - mWidth) / 2,
                -(mHeight * scale - mHeight) / 2);

        // move x and y distance
        mMatrix.postTranslate(-(x - (mWidth / 2)) * scale, 0);
        mMatrix.postTranslate(0, -(y - (mHeight / 2)) * scale);
        setImageMatrix(mMatrix);
    }

    public void cutting() {
        int width = (int) (mIntrinsicWidth * getScale());
        int height = (int) (mIntrinsicHeight * getScale());
        if (getTranslateX() < -(width - mWidth)) {
            mMatrix.postTranslate(-(getTranslateX() + width - mWidth), 0);
        }
        if (getTranslateX() > 0) {
            mMatrix.postTranslate(-getTranslateX(), 0);
        }
        if (getTranslateY() < -(height - mHeight)) {
            mMatrix.postTranslate(0, -(getTranslateY() + height - mHeight));
        }
        if (getTranslateY() > 0) {
            mMatrix.postTranslate(0, -getTranslateY());
        }
        if (width < mWidth) {
            mMatrix.postTranslate((mWidth - width) / 2, 0);
        }
        if (height < mHeight) {
            mMatrix.postTranslate(0, (mHeight - height) / 2);
        }
        setImageMatrix(mMatrix);
    }

    private float distance(float x0, float x1, float y0, float y1) {
        float x = x0 - x1;
        float y = y0 - y1;
        return Math.signum(x * x + y * y);
    }

    private float dispDistance() {
        return Math.signum(mWidth * mWidth + mHeight * mHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDetector.onTouchEvent(event)) {
            return true;
        }
        int touchCount = event.getPointerCount();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_1_DOWN:
            case MotionEvent.ACTION_POINTER_2_DOWN:
                if (touchCount >= 2) {
                    float distance = distance(event.getX(0), event.getX(1),
                            event.getY(0), event.getY(1));
                    mPrevDistance = distance;
                    isScaling = true;
                } else {
                    mPrevMoveX = (int) event.getX();
                    mPrevMoveY = (int) event.getY();
                }
            case MotionEvent.ACTION_MOVE:
                if (touchCount >= 2 && isScaling) {
                    float dist = distance(event.getX(0), event.getX(1),
                            event.getY(0), event.getY(1));
                    float scale = (dist - mPrevDistance) / dispDistance();
                    mPrevDistance = dist;
                    scale += 1;
                    scale = scale * scale;
                    zoomTo(scale, mWidth / 2, mHeight / 2);
                    cutting();
                } else if (!isScaling) {
                    int distanceX = mPrevMoveX - (int) event.getX();
                    int distanceY = mPrevMoveY - (int) event.getY();
                    mPrevMoveX = (int) event.getX();
                    mPrevMoveY = (int) event.getY();
                    mMatrix.postTranslate(-distanceX, -distanceY);
                    cutting();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_POINTER_2_UP:
                if (event.getPointerCount() <= 1) {
                    isScaling = false;
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public boolean canScrollHor(int direction) {// 返回true：viewPager不会翻页，false：viewPager会翻页
        // -----------------------
        // 判断触点控件在页面内是否可滚动，若可滚动返回true，否则返回false
        // 测试时发现一个现象：
        // 如果当页面内容尺寸大于WebView的尺寸，并将页面内容移至webview的中央（左右两边均留一点空间）
        // 那么在组件中翻动时的效果一切正常。
        // -----------------------
        /*
         * final int offset = computeHorizontalScrollOffset(); final int range =
		 * computeHorizontalScrollRange() - computeHorizontalScrollExtent(); if
		 * (range == 0) return false; if (direction < 0) { return offset > 0; }
		 * else { return offset < range - 1; }
		 */
        float s = getScale();
        /*
         * Log.i(TAG,
		 * "判断是否需要翻页，图片实际宽高："+mIntrinsicWidth+","+mIntrinsicHeight+" 方向："
		 * +direction); Log.i(TAG, "判断是否需要翻页，当前比例："+s); Log.i(TAG,
		 * "当前图片变比宽高："+mIntrinsicWidth*s+","+mIntrinsicHeight*s); Log.i(TAG,
		 * "判断是否需要翻页，当前x,y："+getTranslateX()+","+getTranslateY()); Log.i(TAG,
		 * "判断是否需要翻页，imageView宽高："+getMeasuredWidth()+","+getMeasuredHeight());
		 */

        float tx = getTranslateX();
        int w = getMeasuredWidth();
        // 如果getTranslateX小于0，说明图片还在屏幕左方隐藏，不允许ViewPager向右拖动
        if (direction < 0 && tx < 0) {
            return true;
        }
        // 如果getTranslateX小于0，并且translateX+ImageView宽度小于图片宽度，说明图片隐藏在屏幕右边，不允许ViewPager向左拖动
        if (direction > 0 && tx < 0 && -tx + w <= mIntrinsicWidth * s - 10) {
            return true;
        }
        return false;
    }
}
