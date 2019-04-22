package com.lzyc.ybtappcal.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.widget.popupwindow.lib.DensityUtil;

/**
 * 滑动跟随的linearlayout，实现无结果界面的滑动悬浮效果
 * 包含动画和手势
 * Created by yang on 2017/02/20.
 */
public class HdGensuiLinearlayout extends LinearLayout {
    private static final long ANIM_DURATION = 600;
    private int lastX;
    private int lastY;
    private int mScreenWidth;
    private int mScreenHeight;
    private int statusHeight;
    /*
     * 初始化的时候是否执行动画，默认不执行。
     * 认为isInitAnim在onlayout的第一次的值为false的时候，
     * 手势isTouch也是false。
     * 即：初始动画与手势事件的行为是正相关，
     * 界面第一次打开执行初始动画的话，那么手势事件是有效的。
     */
    private boolean isInitAnim = false;
    private boolean isTouch = false;

    /*
     *自上而下动画类型
     * TOP_MIDDLE：顶端到中部
     * TOP_MIDDLE_BOTTOM：中部带底部
     * BOTTOM_MIDDLE:底部到中部
     * BOTTOM_MIDDLE_TOP：中部到顶部
     */
    public enum AnimType {
        TOP_MIDDLE, TOP_MIDDLE_BOTTOM, TOP_BOTTOM,
        BOTTOM_MIDDLE, BOTTOM_MIDDLE_TOP, BOTTOM_TOP,
    }

    /*
     * 接收的一般是下次点击操作的动画类型
     */
    private AnimType animType = AnimType.BOTTOM_MIDDLE_TOP;

    /*
     *接收用户点击行为
     * 0被指定为点击了第一个图标
     * 1被指定为点击了第二个图标
     */
    private int action;
    private OnHdGenSuiLinearListener listener;

    private int height;
    private int middleHeight;

    private float startY = 0;//按下时y值
    private int mTouchSlop;//系统值

    public HdGensuiLinearlayout(Context context) {
        super(context);
        init(context);
    }

    public HdGensuiLinearlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HdGensuiLinearlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LogUtil.e("tag", "#### view onAttachedToWindow ###");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LogUtil.e("tag", "### view onMeasure ###");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LogUtil.e("tag", "#### view onSizeChanged ###");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        LogUtil.e("tag", "#### view onLayout ###");
        if (isInitAnim) {
            LogUtil.e("tag", "###是否执行初始动画####" + isInitAnim);
            isTouch = true;
            isInitAnim = false;//当前动画只是在界面初次进来的时候执行，所以只调用一次。
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startAnimationTopMiddle();
                }
            },500);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LogUtil.e("tag", "#### view onDraw ###");
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        LogUtil.e("tag", "#### view onWindowFocusChanged ###");
    }

    /**
     * 界面打开是否执行初始动画
     *
     * @param isAnim
     */
    public void setInitAnimation(boolean isAnim) {
        this.isInitAnim = isAnim;
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        animType = AnimType.BOTTOM_MIDDLE_TOP;
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        height=dm.heightPixels- DensityUtil.dip2px(context,200);
        middleHeight=height/2;
    }


    //手势
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isTouch) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = x;
                    lastY = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int offY = y - lastY;
//                    getTranslationY();
                    layout(getLeft(), getTop() + offY,
                            getRight(), getBottom() + offY);
                    break;
            }
        }
        return true;
    }

//动画

    /**
     * 执行动画之前，我们需要确定的两个条件
     * 一、动画操作源actionType
     * 二、动画类别 animType
     *
     * @param actionType
     */
    public void setOnHdGenSuiLinearListener(int actionType, OnHdGenSuiLinearListener listener) {
        this.action = actionType;//得到了操作源
        this.listener=listener;
        getAnimType();//根据操作源判定动画类别
    }

    /**
     * 区分动画类别
     * action为0默认aniType是TOP_MIDDLE
     * action为1默认aniType是BOTTOM_MIDDLE_TOP
     * 指定两种操作在同一条件执行的不同效果是相对真假命题现象。
     */
    public void getAnimType() {
        if (action == 0) {
            switch (animType) {
                case TOP_MIDDLE://起始点是顶端，顶端到中部
                    startAnimationTopMiddle();
                    animType = AnimType.BOTTOM_MIDDLE_TOP;
                    break;
                case TOP_MIDDLE_BOTTOM://假命题起始点是中部，中部到顶端
                    startAnimationBottomMiddleTOP();
                    animType = AnimType.TOP_MIDDLE;
                    break;
                case TOP_BOTTOM://起始点是顶部，顶端到底部
                    startAnimationTopBottom();
                    animType = AnimType.BOTTOM_TOP;
                    if(listener!=null){
                        listener.onRightChildAnimListener(true);
                        listener.onLeftChildAnimListener(true);
                    }
                    break;
                case BOTTOM_MIDDLE://假命题 起始点是底部，底部到顶端
                    startAnimationBottomTOP();
                    animType = AnimType.TOP_MIDDLE;
                    break;
                case BOTTOM_MIDDLE_TOP://起始点是中部，中部到顶端
                    startAnimationBottomMiddleTOP();
                    animType = AnimType.TOP_MIDDLE;
                    break;
                case BOTTOM_TOP://起始点是底部，底部到顶端
                    startAnimationBottomTOP();
                    animType = AnimType.TOP_MIDDLE;
                    break;
            }
        } else {
            switch (animType) {
                case TOP_MIDDLE://假命题 起始点是顶端，顶端到底部
                    startAnimationTopBottom();
                    animType = AnimType.BOTTOM_TOP;
                    if(listener!=null){
                        listener.onRightChildAnimListener(true);
                        listener.onLeftChildAnimListener(true);
                    }
                    break;
                case TOP_MIDDLE_BOTTOM://起始点是中部，中部到底部
                    startAnimationTopMiddleBottom();
                    animType = AnimType.BOTTOM_MIDDLE;
                    break;
                case TOP_BOTTOM://起始点是顶端，顶端到底部
                    startAnimationTopBottom();
                    animType = AnimType.BOTTOM_TOP;
                    if(listener!=null){
                        listener.onRightChildAnimListener(true);
                        listener.onLeftChildAnimListener(true);
                    }
                    break;
                case BOTTOM_MIDDLE://起始点是底部，底部到中部
                    startAnimationBottomMiddle();
                    animType = AnimType.BOTTOM_MIDDLE_TOP;
                    break;
                case BOTTOM_MIDDLE_TOP://相对假命题起始点是中部，中部到底部
                    startAnimationTopMiddleBottom();
                    animType = AnimType.BOTTOM_MIDDLE;
                    break;
                case BOTTOM_TOP://假命题 起始点是底部，底部到中部
                    startAnimationBottomMiddle();
                    animType = AnimType.BOTTOM_MIDDLE_TOP;
                    break;
            }
        }
    }

    //自上而下执行的动画
    /**
     * 顶端到中部
     * 认为该动画是从界面的顶端向屏幕的底部向下偏移，
     * 偏移位移是界面高度的一半，即中心位置。
     */
    private void startAnimationTopMiddle() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationY", 0, middleHeight);
        animator.setDuration(ANIM_DURATION);
        animator.start();
        if(listener!=null){
            listener.onLeftChildAnimListener(true);
        }

    }

    /**
     * 中部到底部
     * 认为该动画是从界面的顶端向屏幕的底部向下偏移，
     * 偏移位移是界面高度的一半稍微少点，即到底部留存部分布局
     */
    private void startAnimationTopMiddleBottom() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationY", middleHeight, height);
        animator.setDuration(ANIM_DURATION);
        animator.start();
        if(listener!=null){
            listener.onRightChildAnimListener(true);
        }
    }

    /**
     * 顶端到底部
     * 认为该动画是从界面的顶端向屏幕的底部向下偏移，
     * 偏移位移是界面高度的稍微少点，即到底部留存部分布局
     */
    private void startAnimationTopBottom() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationY", 0, height);
        animator.setDuration(ANIM_DURATION);
        animator.start();
    }

    //自下而上的动画
    /**
     * 底部到中部
     */
    public void startAnimationBottomMiddle() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationY", height, middleHeight);
        animator.setDuration(ANIM_DURATION);
        animator.start();
        if(listener!=null){
            listener.onRightChildAnimListener(false);
        }
    }

    /**
     * 中部到顶端
     */
    public void startAnimationBottomMiddleTOP() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationY", middleHeight, 0);
        animator.setDuration(ANIM_DURATION);
        animator.start();
        if(listener!=null){
            listener.onLeftChildAnimListener(false);
        }
    }

    /**
     * 底部到顶端
     */
    public void startAnimationBottomTOP() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationY", height, 0);
        animator.setDuration(ANIM_DURATION);
        animator.start();
        if(listener!=null){
            listener.onLeftChildAnimListener(false);
        }
    }

  //接口
    public interface OnHdGenSuiLinearListener{
      /**
       * 左边图标
       * isShowing是否显示底层，显示true，下同。
       * @param isShowing
       */
        void onLeftChildAnimListener(boolean isShowing);

      /**
       * 右边图标
       * @param isShowing
       */
        void onRightChildAnimListener(boolean isShowing);
    }

}
