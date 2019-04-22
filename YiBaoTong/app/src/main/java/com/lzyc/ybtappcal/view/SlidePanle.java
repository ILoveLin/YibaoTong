package com.lzyc.ybtappcal.view;


import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;

/**
 * view.getTranslationY() 当view初始化的时候，没有任何便宜时，该值为0，即移动距离为0
 * view.setTranslationY(int) 设置移动距离 当传入正数在原始位置下移 负数上移
 * Created by yanlc on 2017/3/14.
 */
public class SlidePanle implements View.OnTouchListener, ValueAnimator.AnimatorUpdateListener {

    /**
     * 控制的view
     */
    private View mPanleView;
    /**
     * 手指按下时在屏幕的位置
     **/
    private float startPositionY;
    /**
     * 手指按下时view已经偏移的距离
     **/
    private float viewStartPositionY;
    private boolean isMoveUp;
    /**
     * 滑动到底部保留高度
     **/
    private float bottomHeight = 400;
    /**
     * 当运行顶部或者底部还可以偏移的最大量，手指抬起弹回
     **/
    private float maxOffset = 200;

    private float viewHeight;
    private float viewWidth;
    private float contentHeight;

    /**
     * 动画
     **/
    private ValueAnimator valueAnimator;
    private float slideAnimationTo;
    private int autoSlideDuration = 300;
    private TimeInterpolator interpolator = new DecelerateInterpolator();
    private OnSlideMoveEventListener listener;

    public SlidePanle(View panleView,OnSlideMoveEventListener listener) {
        this.mPanleView = panleView;
        this.mPanleView.setOnTouchListener(this);
        this.listener=listener;
        mPanleView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewHeight = mPanleView.getHeight();
                viewWidth = mPanleView.getWidth();
                Log.e("yanlc", "h = " + viewHeight);
                //用完注销掉 防止多次调用
                ViewTreeObserver observer = mPanleView.getViewTreeObserver();
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                    observer.removeGlobalOnLayoutListener(this);
                } else {
                    observer.removeOnGlobalLayoutListener(this);
                }
            }
        });
        createAnimation();
    }

    /**
     * 创建动画
     */
    private void createAnimation() {
        valueAnimator = ValueAnimator.ofFloat();
        valueAnimator.setDuration(autoSlideDuration);
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.addUpdateListener(this);
    }

    /**
     * 设置自动滑动
     *
     * @param from
     * @param to
     */
    public void setTranslationY(float from, float to, int duration) {
        valueAnimator.setDuration(duration);
        valueAnimator.setFloatValues(from, to);
        valueAnimator.start();
    }

    /**
     * 设置自动滑动
     *
     * @param from
     * @param to
     */
    public void setTranslationY(float from, float to) {
        setTranslationY(from, to, autoSlideDuration);
    }

    public void setContentHeight(int h) {
        contentHeight = h;
    }

    //-------------------------------------------------anim 继承实现类--------------------------------------

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float value = (float) valueAnimator.getAnimatedValue();
        mPanleView.setTranslationY(value);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                viewHeight = mPanleView.getHeight();
                Log.e("yanlc", "h = " + viewHeight);
                startPositionY = motionEvent.getRawY();
                //记录按下时view距离初始点距离 正数在初始点下面 负数在初始点上面
                viewStartPositionY = mPanleView.getTranslationY();
                Log.e("yanlc", "down startPositionY = " + startPositionY + " viewStartPositionY = " + viewStartPositionY + " viewW = " + viewHeight);
                break;
            case MotionEvent.ACTION_MOVE:
                //测量手指移动距离 负数向上移动  正数向下移动
                float difference = motionEvent.getRawY() - startPositionY;
                //计算此时mPanleView的位置距离初始状态位置的距离
                float moveTo = viewStartPositionY + difference;
                Log.e("yanlc", "difference = " + difference + " moveTo = " + moveTo + " RawY = " + motionEvent.getRawY());
//                if (moveTo > -maxOffset && moveTo < viewHeight - bottomHeight + maxOffset) {
                mPanleView.setTranslationY(moveTo); //moveTo为正的时候下移 负的时候上移
//                }
                //判断是移动方向
                if (difference > 0) {
                    isMoveUp = false;
                } else {
                    isMoveUp = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                float slideAnimationFrom = mPanleView.getTranslationY();
                if (slideAnimationFrom == viewStartPositionY) return false;
                if (!isMoveUp) {//向下滑动
                    boolean scrollableAreaConsumed = mPanleView.getTranslationY() > mPanleView.getHeight() / 5;
                    if (scrollableAreaConsumed) {
                        if(viewHeight>contentHeight) {
                            if (slideAnimationFrom < 300) {
                                slideAnimationTo = contentHeight - viewHeight;
                            } else if (slideAnimationFrom > 300) {
                                slideAnimationTo = 300;
                            }
                        } else {
                            slideAnimationTo = 0;
                        }
                    } else {
                        slideAnimationTo = mPanleView.getHeight() - bottomHeight;
                    }
                    if(listener!=null){
                        listener.moveDown();
                    }
                } else {//向上滑动
                    boolean scrollableAreaConsumed = mPanleView.getTranslationY() < (mPanleView.getHeight() / 5) * 4;
                    if (scrollableAreaConsumed) {
                        slideAnimationTo = viewHeight > contentHeight ? (contentHeight - bottomHeight) : viewHeight - bottomHeight;
                    } else {
                        slideAnimationTo = 0;
                    }

                    if(listener!=null){
                        listener.moveUp();
                    }
                }
                valueAnimator.setDuration(autoSlideDuration);
                valueAnimator.setFloatValues(slideAnimationFrom, slideAnimationTo);
                valueAnimator.start();
                break;
        }
        return true;
    }


    public interface OnSlideMoveEventListener{
       void moveUp();//上滑
        void moveDown();//下滑
    }

}
