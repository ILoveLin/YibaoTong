package com.lzyc.ybtappcal.view;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.ScreenUtils;

/**
 * 解决手势冲突
 * Created by yang on 2017/3/25.
 */
public class CustomScrollView extends ScrollView implements ValueAnimator.AnimatorUpdateListener{

    /**
     * 手指按下时在屏幕的位置
     **/
    private float startPositionY;
    /**
     * 手指按下时view已经偏移的距离
     **/
    private float viewStartPositionY;
    private boolean isMoveUp;
    private boolean isOnTouch=true;
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
    private TimeInterpolator interpolator = new LinearInterpolator();

    private int downY;
    private boolean mScrolling;

    private Context mContext;
    private LinearLayout parentLay;
    private int screenHeight;
    private View mShadowView;
    private int mShadowH;

    public CustomScrollView(Context context) {
        super(context);
        this.mContext=context;
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        createAnimation();
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext=context;
        createAnimation();
    }

    public void setIsOnTouch(boolean isOnTouch){
        this.isOnTouch=isOnTouch;
    }

    public void setParentLayout(LinearLayout linearLayout){
        this.parentLay=linearLayout;
    }

    public void setTopShadowView(View v){
        this.mShadowView = v;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View parentView = (View) getParent();
        int pH = parentView.getHeight();
        View child = getChildAt(0);
        child.setClickable(false);
        if(child.getMeasuredHeight() > pH) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
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


    /**
     * 创建动画
     */
    private void createAnimation() {
        screenHeight= ScreenUtils.getScreenHeight()- ScreenUtils.getStatusHeight();
        valueAnimator = ValueAnimator.ofFloat();
        valueAnimator.setDuration(autoSlideDuration);
//        valueAnimator.setInterpolator(interpolator);
        valueAnimator.addUpdateListener(this);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float value = (float) valueAnimator.getAnimatedValue();
        this.setTranslationY(value);
    }

    //----------------------------------------onInterceptTouchEvent onTouchEvent---------------------------------

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(!isOnTouch){
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mShadowH = mShadowView.getHeight();
                downY = (int)event.getY();
                viewHeight = this.getHeight();
                startPositionY = event.getRawY();
                //记录按下时view距离初始点距离 正数在初始点下面 负数在初始点上面
                viewStartPositionY = this.getTranslationY();
                mScrolling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(downY - event.getY()) >= ViewConfiguration.get(
                        getContext()).getScaledTouchSlop()) {
                    mScrolling = true;
                } else {
                    mScrolling = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                mScrolling = false;
                break;
        }
        return mScrolling;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float distance= viewHeight-screenHeight;
        if(!isOnTouch){
            return false;
        }
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                //测量手指移动距离 负数向上移动  正数向下移动
                float difference = motionEvent.getRawY() - startPositionY;
                //计算此时mPanleView的位置距离初始状态位置的距离
                float moveTo = viewStartPositionY + difference;
//                if (moveTo > -maxOffset && moveTo < viewHeight - bottomHeight + maxOffset) {
                this.setTranslationY(moveTo); //moveTo为正的时候下移 负的时候上移
//                }
                //判断是移动方向
                if (difference > 0) {
                    isMoveUp = false;
                } else {
                    isMoveUp = true;
                    parentLay.setBackgroundColor(Color.parseColor("#f1f1f3"));
                }
                int translationY= (int) this.getTranslationY();
                if(translationY<0){
                    parentLay.setBackgroundColor(Color.parseColor("#f1f1f3"));
                }else{
                    parentLay.setBackgroundColor(Color.argb(0,0,0,0));
                }
                break;
            case MotionEvent.ACTION_UP:
                float slideAnimationFrom = this.getTranslationY();
				if (slideAnimationFrom == viewStartPositionY){
                    return false;
                }
                if(!isMoveUp){
                    if(slideAnimationFrom>screenHeight-500-mShadowH){
//                        if(distance>0){
                            valueAnimator.setDuration(autoSlideDuration);
                            valueAnimator.setFloatValues(slideAnimationFrom, screenHeight-500-mShadowH);
                            valueAnimator.start();
//                        }else{
//                            valueAnimator.setDuration(autoSlideDuration);
//                            valueAnimator.setFloatValues(slideAnimationFrom, screenHeight-300);
//                            valueAnimator.start();
//                        }
                    }
                }else{
                    if(slideAnimationFrom<=-distance- mShadowH-DensityUtils.dp2px(50)){
                        valueAnimator.setDuration(autoSlideDuration);
                        valueAnimator.setFloatValues(slideAnimationFrom,-distance- mShadowH-DensityUtils.dp2px(50));
                        valueAnimator.start();
//                        valueAnimator.addListener(new AnimatorListenerAdapter() {
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                super.onAnimationEnd(animation);
//                                int translationY= (int) getTranslationY();
//                                if(translationY<-mShadowH){
//                                    parentLay.setBackgroundColor(Color.parseColor("#f1f1f3"));
//                                }else{
//                                    parentLay.setBackgroundColor(Color.argb(0,0,0,0));
//                                }
//                            }
//                        });
                    }
                }
//				float slideAnimationFrom = this.getTranslationY();
//				if (slideAnimationFrom == viewStartPositionY) return false;
//				if (!isMoveUp) {//向下滑动
//					boolean scrollableAreaConsumed = this.getTranslationY() > this.getHeight() / 5;
//					if (scrollableAreaConsumed) {
//						if(viewHeight>contentHeight) {
//							if (slideAnimationFrom < 300) {
//								slideAnimationTo = contentHeight - viewHeight;
//							} else if (slideAnimationFrom > 300) {
//								slideAnimationTo = 300;
//							}
//						} else {
//							slideAnimationTo = 0;
//						}
//					} else {
//						slideAnimationTo = this.getHeight() - bottomHeight;
//					}
//
//				} else {//向上滑动
//					boolean scrollableAreaConsumed = this.getTranslationY() < (this.getHeight() / 5) * 4;
//					if (scrollableAreaConsumed) {
//						slideAnimationTo = viewHeight > contentHeight ? (contentHeight - bottomHeight) : viewHeight - bottomHeight;
//					} else {
//						slideAnimationTo = 0;
//					}
//
//				}
//				valueAnimator.setDuration(autoSlideDuration);
//				valueAnimator.setFloatValues(slideAnimationFrom, slideAnimationTo);
//				valueAnimator.start();
                break;
        }
        return true;
    }



}
