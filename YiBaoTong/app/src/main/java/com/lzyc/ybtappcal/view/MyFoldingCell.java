package com.lzyc.ybtappcal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lzyc.ybtappcal.util.LogUtil;
import com.ramotion.foldingcell.FoldingCell;
import com.ramotion.foldingcell.animations.AnimationEndListener;
import com.ramotion.foldingcell.animations.FoldAnimation;
import com.ramotion.foldingcell.animations.HeightAnimation;
import com.ramotion.foldingcell.views.FoldingCellView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yang on 17/06/17.
 * yanlc modify
 * 由于FoldingCell的属性大多为私有,不适合继承,这里直接重写FoldingCell
 */
public class MyFoldingCell extends RelativeLayout {

    private final String TAG = "folding-cell";

    // state variables
    private boolean mUnfolded;
    private boolean mAnimationInProgress;

    // default values
    private final int DEF_ANIMATION_DURATION = 1000;
    private final int DEF_BACK_SIDE_COLOR = Color.GRAY;
    private final int DEF_ADDITIONAL_FLIPS = 0;
    private final int DEF_CAMERA_HEIGHT = 30;

    // current settings
    private int mAnimationDuration = DEF_ANIMATION_DURATION;
    private int mBackSideColor = DEF_BACK_SIDE_COLOR;
    private int mAdditionalFlipsCount = DEF_ADDITIONAL_FLIPS;
    private int mCameraHeight = DEF_CAMERA_HEIGHT;

    public MyFoldingCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeFromAttributes(context, attrs);
        this.setClipChildren(false);
        this.setClipToPadding(false);
    }

    public MyFoldingCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeFromAttributes(context, attrs);
        this.setClipChildren(false);
        this.setClipToPadding(false);
    }

    public MyFoldingCell(Context context) {
        super(context);
        this.setClipChildren(false);
        this.setClipToPadding(false);
    }

    /**
     * Initializes folding cell programmatically with custom settings
     *
     * @param animationDuration    animation duration, default is 1000
     * @param backSideColor        color of back side, default is android.graphics.Color.GREY (0xFF888888)
     * @param additionalFlipsCount count of additional flips (after first one), set 0 for auto
     */
    public void initialize(int animationDuration, int backSideColor, int additionalFlipsCount) {
        this.mAnimationDuration = animationDuration;
        this.mBackSideColor = backSideColor;
        this.mAdditionalFlipsCount = additionalFlipsCount;
    }

    /**
     * Initializes folding cell programmatically with custom settings
     *
     * @param animationDuration    animation duration, default is 1000
     * @param backSideColor        color of back side, default is android.graphics.Color.GREY (0xFF888888)
     * @param additionalFlipsCount count of additional flips (after first one), set 0 for auto
     */
    public void initialize(int cameraHeight, int animationDuration, int backSideColor, int additionalFlipsCount) {
        this.mAnimationDuration = animationDuration;
        this.mBackSideColor = backSideColor;
        this.mAdditionalFlipsCount = additionalFlipsCount;
        this.mCameraHeight = cameraHeight;
    }

    public boolean isUnfolded() {
        return mUnfolded;
    }

    /**
     * Unfold cell with (or without) animation
     *skipAnimation true 无动画展开 ，false 执行动画展开
     * @param skipAnimation if true - change state of cell instantly without animation
     */
    public void unfold(boolean skipAnimation, final UnfoldAnimListener animListener) {
        if (mUnfolded || mAnimationInProgress) return;
        // get main content parts
        final View contentView = getChildAt(0);
        if (contentView == null) return;
        final View titleView = getChildAt(1);
        if (titleView == null) return;
//        LogUtil.y("展开转换之前titleView"+titleView.getVisibility()+"contentView"+contentView.getVisibility());
//        if(titleView.getVisibility()==INVISIBLE||titleView.getVisibility()==GONE){
//            titleView.setVisibility(VISIBLE);
//        }
//        if(contentView.getVisibility()==INVISIBLE||contentView.getVisibility()==GONE){
//            contentView.setVisibility(VISIBLE);
//        }
//        LogUtil.y("展开转换之后titleView"+titleView.getVisibility()+"contentView"+contentView.getVisibility());
        // Measure views and take a bitmaps to replace real views with images
//        contentView.setVisibility(INVISIBLE);
//        Log.e("yanlc","contentView invisible");
//        titleView.setVisibility(GONE);
//        contentView.setVisibility(GONE);
        Bitmap bitmapFromTitleView = measureViewAndGetBitmap(titleView, this.getMeasuredWidth());
        Bitmap bitmapFromContentView = measureViewAndGetBitmap(contentView, this.getMeasuredWidth());

        // hide title and content views
        titleView.setVisibility(INVISIBLE);
        contentView.setVisibility(GONE);

        if (skipAnimation) {
            contentView.setVisibility(VISIBLE);
            MyFoldingCell.this.mUnfolded = true;
            MyFoldingCell.this.mAnimationInProgress = false;
            this.getLayoutParams().height = contentView.getHeight();
        } else {
            // create layout container for animation elements
            final LinearLayout foldingLayout = createAndPrepareFoldingContainer();
            this.addView(foldingLayout);
            // calculate heights of animation parts
            ArrayList<Integer> heights = calculateHeightsForAnimationParts(titleView.getHeight(), contentView.getHeight(), mAdditionalFlipsCount);
            // create list with animation parts for animation
            ArrayList<FoldingCellView> foldingCellElements = prepareViewsForAnimation(heights, bitmapFromTitleView, bitmapFromContentView);
            // start unfold animation with end listener
            int childCount = foldingCellElements.size();
            int part90degreeAnimationDuration = mAnimationDuration / (childCount * 2);
            startUnfoldAnimation(foldingCellElements, foldingLayout, part90degreeAnimationDuration, new AnimationEndListener() {
                public void onAnimationEnd(Animation animation) {
                    contentView.setVisibility(VISIBLE);
                    titleView.setVisibility(View.GONE);
                    foldingLayout.setVisibility(GONE);
                    MyFoldingCell.this.removeView(foldingLayout);
                    MyFoldingCell.this.mUnfolded = true;
                    MyFoldingCell.this.mAnimationInProgress = false;
                    if(animListener != null){
                        animListener.unFoldAnimEnd();
                    }
                }
            });

            startExpandHeightAnimation(heights, part90degreeAnimationDuration * 2);
            this.mAnimationInProgress = true;
        }

    }

    /**
     * Fold cell with (or without) animation
     *
     * @param skipAnimation if true - change state of cell instantly without animation
     */
    public void fold(boolean skipAnimation, final FoldAnimListener animListener) {
        if (!mUnfolded || mAnimationInProgress) return;
        Log.e("yanlc","fold");
        // get basic views
        final View contentView = getChildAt(0);
        if (contentView == null) return;
        final View titleView = getChildAt(1);
        if (titleView == null) return;

//        LogUtil.y("收起转换之前titleView"+titleView.getVisibility()+"contentView"+contentView.getVisibility());
//        if(titleView.getVisibility()==INVISIBLE||titleView.getVisibility()==GONE){
//            titleView.setVisibility(VISIBLE);
//        }
//        if(contentView.getVisibility()==INVISIBLE||contentView.getVisibility()==GONE){
//            contentView.setVisibility(VISIBLE);
//        }
//        LogUtil.y("收起转换之后titleView"+titleView.getVisibility()+"contentView"+contentView.getVisibility());

        // make bitmaps from title and content views
        Bitmap bitmapFromTitleView = measureViewAndGetBitmap(titleView, this.getMeasuredWidth());
        Bitmap bitmapFromContentView = measureViewAndGetBitmap(contentView, this.getMeasuredWidth());

        // hide title and content views
        titleView.setVisibility(INVISIBLE);
        contentView.setVisibility(GONE);

        if (skipAnimation) {
            contentView.setVisibility(INVISIBLE);
            titleView.setVisibility(VISIBLE);
            MyFoldingCell.this.mAnimationInProgress = false;
            MyFoldingCell.this.mUnfolded = false;
            this.getLayoutParams().height = titleView.getHeight();
        } else {

            // create empty layout for folding animation
            final LinearLayout foldingLayout = createAndPrepareFoldingContainer();
            // add that layout to structure
            this.addView(foldingLayout);

            // calculate heights of animation parts
            ArrayList<Integer> heights = calculateHeightsForAnimationParts(titleView.getHeight(), contentView.getHeight(), mAdditionalFlipsCount);
            // create list with animation parts for animation
            ArrayList<FoldingCellView> foldingCellElements = prepareViewsForAnimation(heights, bitmapFromTitleView, bitmapFromContentView);
            int childCount = foldingCellElements.size();
            int part90degreeAnimationDuration = mAnimationDuration / (childCount * 2);
            // start fold animation with end listener
            startFoldAnimation(foldingCellElements, foldingLayout, part90degreeAnimationDuration, new AnimationEndListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    contentView.setVisibility(INVISIBLE);
                    titleView.setVisibility(VISIBLE);
                    foldingLayout.setVisibility(GONE);
                    MyFoldingCell.this.removeView(foldingLayout);
                    MyFoldingCell.this.mAnimationInProgress = false;
                    MyFoldingCell.this.mUnfolded = false;
                    if(animListener != null){
                        animListener.foldAnimEnd();
                    }
                }
            });
            startCollapseHeightAnimation(heights, part90degreeAnimationDuration * 2);
            this.mAnimationInProgress = true;
        }
    }


    public void samSungReFold(){
        final View contentView = getChildAt(0);
        if (contentView == null) return;
        final View titleView = getChildAt(1);
        if (titleView == null) return;

//        Bitmap bitmapFromTitleView = measureViewAndGetBitmap(titleView, this.getMeasuredWidth());
//        Bitmap bitmapFromContentView = measureViewAndGetBitmap(contentView, this.getMeasuredWidth());
        int specW = View.MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), View.MeasureSpec.EXACTLY);
        int specH = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        contentView.measure(specW, specH);
        contentView.layout(0, 0, contentView.getMeasuredWidth(), contentView.getMeasuredHeight());

        contentView.setVisibility(INVISIBLE);
        titleView.setVisibility(VISIBLE);
        MyFoldingCell.this.mAnimationInProgress = false;
        MyFoldingCell.this.mUnfolded = false;
        this.getLayoutParams().height = titleView.getHeight();
    }

    public void unFold(){
        unfold(true,null);//默认不执行动画铺开
        fold(true,null);//默认不执行动画收起
    }

    /**
     * Toggle current state of FoldingCellLayout
     */
    public void toggle(boolean skipAnimation) {
        if (this.mUnfolded) {
            this.fold(skipAnimation,null);
        } else {
            this.unfold(skipAnimation,null);
            this.requestLayout();
        }
    }
    /**
     * Create and prepare list of FoldingCellViews with different bitmap parts for fold animation
     *
     * @param titleViewBitmap   bitmap from title view
     * @param contentViewBitmap bitmap from content view
     * @return list of FoldingCellViews with bitmap parts
     */
    protected ArrayList<FoldingCellView> prepareViewsForAnimation(ArrayList<Integer> viewHeights, Bitmap titleViewBitmap, Bitmap contentViewBitmap) {
        if (viewHeights == null || viewHeights.isEmpty())
            throw new IllegalStateException("ViewHeights array must be not null and not empty");

        ArrayList<FoldingCellView> partsList = new ArrayList<>();

        int partWidth = titleViewBitmap.getWidth();
        int yOffset = 0;
        for (int i = 0; i < viewHeights.size(); i++) {
            int partHeight = viewHeights.get(i);
            Bitmap partBitmap = Bitmap.createBitmap(partWidth, partHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(partBitmap);
            Rect srcRect = new Rect(0, yOffset, partWidth, yOffset + partHeight);
            Rect destRect = new Rect(0, 0, partWidth, partHeight);
            canvas.drawBitmap(contentViewBitmap, srcRect, destRect, null);
            ImageView backView = createImageViewFromBitmap(partBitmap);
            ImageView frontView = null;
            if (i < viewHeights.size() - 1) {
                frontView = (i == 0) ? createImageViewFromBitmap(titleViewBitmap) : createBackSideView(viewHeights.get(i + 1));
            }
            partsList.add(new FoldingCellView(frontView, backView, getContext()));
            yOffset = yOffset + partHeight;
        }

        return partsList;
    }

    /**
     * Calculate heights for animation parts with some logic
     * TODO: Add detailed descriptions for logic
     *
     * @param titleViewHeight      height of title view
     * @param contentViewHeight    height of content view
     * @param additionalFlipsCount count of additional flips (after first one), set 0 for auto
     * @return list of calculated heights
     */
    protected ArrayList<Integer> calculateHeightsForAnimationParts(int titleViewHeight, int contentViewHeight, int additionalFlipsCount) {
        ArrayList<Integer> partHeights = new ArrayList<>();
        int additionalPartsTotalHeight = contentViewHeight - titleViewHeight * 2;
        if (additionalPartsTotalHeight < 0)
            throw new IllegalStateException("Content View height is too small");
        // add two main parts - guarantee first flip
        partHeights.add(titleViewHeight);
        partHeights.add(titleViewHeight);

        // if no space left - return
        if (additionalPartsTotalHeight == 0)
            return partHeights;

        // if some space remained - use two different logic
        if (additionalFlipsCount != 0) {
            // 1 - additional parts count is specified and it is not 0 - divide remained space
            int additionalPartHeight = additionalPartsTotalHeight / additionalFlipsCount;
            int remainingHeight = additionalPartsTotalHeight % additionalFlipsCount;

            if (additionalPartHeight + remainingHeight > titleViewHeight)
                throw new IllegalStateException("Additional flips count is too small");
            for (int i = 0; i < additionalFlipsCount; i++)
                partHeights.add(additionalPartHeight + (i == 0 ? remainingHeight : 0));
        } else {
            // 2 - additional parts count isn't specified or 0 - divide remained space to parts with title view size
            int partsCount = additionalPartsTotalHeight / titleViewHeight;
            int restPartHeight = additionalPartsTotalHeight % titleViewHeight;
            for (int i = 0; i < partsCount; i++)
                partHeights.add(titleViewHeight);
            if (restPartHeight > 0)
                partHeights.add(restPartHeight);
        }

        return partHeights;
    }

    /**
     * Create image view for display back side of flip view
     *
     * @param height height for view
     * @return ImageView with selected height and default background color
     */
    protected ImageView createBackSideView(int height) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundColor(mBackSideColor);
        imageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        return imageView;
    }

    /**
     * Create image view for display selected bitmap
     *
     * @param bitmap bitmap to display in image view
     * @return ImageView with selected bitmap
     */
    protected ImageView createImageViewFromBitmap(Bitmap bitmap) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(bitmap);
        imageView.setLayoutParams(new LayoutParams(bitmap.getWidth(), bitmap.getHeight()));
        return imageView;
    }

    /**
     * Create bitmap from specified View with specified with
     *
     * @param view        source for bitmap
     * @param parentWidth result bitmap width
     * @return bitmap from specified view
     */
    protected Bitmap measureViewAndGetBitmap(View view, int parentWidth) {
        int specW = View.MeasureSpec.makeMeasureSpec(parentWidth, View.MeasureSpec.EXACTLY);
        int specH = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(specW, specH);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        return b;
    }

    /**
     * Create layout that will be a container for animation elements
     *
     * @return Configured container for animation elements (LinearLayout)
     */
    protected LinearLayout createAndPrepareFoldingContainer() {
        LinearLayout foldingContainer = new LinearLayout(getContext());
        foldingContainer.setClipToPadding(false);
        foldingContainer.setClipChildren(false);
        foldingContainer.setOrientation(LinearLayout.VERTICAL);
        foldingContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        return foldingContainer;
    }

    /**
     * Prepare and start height expand animation for FoldingCellLayout
     *
     * @param partAnimationDuration one part animate duration
     * @param viewHeights           heights of animation parts
     */
    protected void startExpandHeightAnimation(ArrayList<Integer> viewHeights, int partAnimationDuration) {
        if (viewHeights == null || viewHeights.isEmpty())
            throw new IllegalArgumentException("ViewHeights array must have at least 2 elements");

        ArrayList<Animation> heightAnimations = new ArrayList<>();
        int fromHeight = viewHeights.get(0);
        int delay = 0;
        int animationDuration = partAnimationDuration - delay;
        for (int i = 1; i < viewHeights.size(); i++) {
            int toHeight = fromHeight + viewHeights.get(i);
            HeightAnimation heightAnimation = new HeightAnimation(this, fromHeight, toHeight, animationDuration)
                    .withInterpolator(new DecelerateInterpolator());
            heightAnimation.setStartOffset(delay);
            heightAnimations.add(heightAnimation);
            fromHeight = toHeight;
        }
        createAnimationChain(heightAnimations, this);
        this.startAnimation(heightAnimations.get(0));
    }

    /**
     * Prepare and start height collapse animation for FoldingCellLayout
     *
     * @param partAnimationDuration one part animate duration
     * @param viewHeights           heights of animation parts
     */
    protected void startCollapseHeightAnimation(ArrayList<Integer> viewHeights, int partAnimationDuration) {
        if (viewHeights == null || viewHeights.isEmpty())
            throw new IllegalArgumentException("ViewHeights array must have at least 2 elements");

        ArrayList<Animation> heightAnimations = new ArrayList<>();
        int fromHeight = viewHeights.get(0);
        for (int i = 1; i < viewHeights.size(); i++) {
            int toHeight = fromHeight + viewHeights.get(i);
            heightAnimations.add(new HeightAnimation(this, toHeight, fromHeight, partAnimationDuration)
                    .withInterpolator(new DecelerateInterpolator()));
            fromHeight = toHeight;
        }
        Collections.reverse(heightAnimations);
        createAnimationChain(heightAnimations, this);
        this.startAnimation(heightAnimations.get(0));
    }

    /**
     * Create "animation chain" for selected view from list of animations objects
     *
     * @param animationList   collection with animations
     * @param animationObject view for animations
     */
    protected void createAnimationChain(final List<Animation> animationList, final View animationObject) {
        for (int i = 0; i < animationList.size(); i++) {
            Animation animation = animationList.get(i);
            if (i + 1 < animationList.size()) {
                final int finalI = i;
                animation.setAnimationListener(new AnimationEndListener() {
                    public void onAnimationEnd(Animation animation) {
                        animationObject.startAnimation(animationList.get(finalI + 1));
                    }
                });
            }
        }
    }

    /**
     * Start fold animation
     *
     * @param foldingCellElements           ordered list with animation parts from top to bottom
     * @param foldingLayout                 prepared layout for animation parts
     * @param part90degreeAnimationDuration animation duration for 90 degree rotation
     * @param animationEndListener          animation end callback
     */
    protected void startFoldAnimation(ArrayList<FoldingCellView> foldingCellElements, ViewGroup foldingLayout,
                                      int part90degreeAnimationDuration, AnimationEndListener animationEndListener) {
        for (FoldingCellView foldingCellElement : foldingCellElements)
            foldingLayout.addView(foldingCellElement);

        Collections.reverse(foldingCellElements);

        int nextDelay = 0;
        for (int i = 0; i < foldingCellElements.size(); i++) {
            final FoldingCellView cell = foldingCellElements.get(i);
            cell.setVisibility(VISIBLE);
            // not FIRST(BOTTOM) element - animate front view

            if(i == foldingCellElements.size() - 1){
                cell.getBackView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cell.getBackView().setVisibility(View.INVISIBLE);
                    }
                },nextDelay);
            }

            if (i != 0) {
                FoldAnimation foldAnimation = new FoldAnimation(FoldAnimation.FoldAnimationMode.UNFOLD_UP, mCameraHeight, part90degreeAnimationDuration)
                        .withStartOffset(nextDelay)
                        .withInterpolator(new DecelerateInterpolator());
                // if last(top) element - add end listener
                if (i == foldingCellElements.size() - 1) {
                    foldAnimation.setAnimationListener(animationEndListener);
                }
                cell.animateFrontView(foldAnimation);
                nextDelay = nextDelay + part90degreeAnimationDuration;
            }

            // if not last(top) element - animate whole view
            if (i != foldingCellElements.size() - 1) {
                cell.startAnimation(new FoldAnimation(FoldAnimation.FoldAnimationMode.FOLD_UP, mCameraHeight, part90degreeAnimationDuration)
                        .withStartOffset(nextDelay)
                        .withInterpolator(new DecelerateInterpolator()));
                nextDelay = nextDelay + part90degreeAnimationDuration;
            }

        }
    }

    /**
     * Start unfold animation
     *
     * @param foldingCellElements           ordered list with animation parts from top to bottom
     * @param foldingLayout                 prepared layout for animation parts
     * @param part90degreeAnimationDuration animation duration for 90 degree rotation
     * @param animationEndListener          animation end callback
     */
    protected void startUnfoldAnimation(ArrayList<FoldingCellView> foldingCellElements, ViewGroup foldingLayout,
                                        int part90degreeAnimationDuration, AnimationEndListener animationEndListener) {
        int nextDelay = 0;
        for (int i = 0; i < foldingCellElements.size(); i++) {
            final FoldingCellView cell = foldingCellElements.get(i);
            cell.setVisibility(VISIBLE);
            foldingLayout.addView(cell);
            // if not first(top) element - animate whole view

            if (i != 0) {
                FoldAnimation foldAnimation = new FoldAnimation(FoldAnimation.FoldAnimationMode.UNFOLD_DOWN, mCameraHeight, part90degreeAnimationDuration)
                        .withStartOffset(nextDelay)
                        .withInterpolator(new DecelerateInterpolator());

                // if last(bottom) element - add end listener
                if (i == foldingCellElements.size() - 1) {
                    foldAnimation.setAnimationListener(animationEndListener);
                }

                nextDelay = nextDelay + part90degreeAnimationDuration;
                cell.startAnimation(foldAnimation);

            }
            // not last(bottom) element - animate front view
            if (i != foldingCellElements.size() - 1) {
                cell.animateFrontView(new FoldAnimation(FoldAnimation.FoldAnimationMode.FOLD_DOWN, mCameraHeight, part90degreeAnimationDuration)
                        .withStartOffset(nextDelay)
                        .withInterpolator(new DecelerateInterpolator()));
                nextDelay = nextDelay + part90degreeAnimationDuration;
            }

            if(i == 0){
                cell.getBackView().setVisibility(View.INVISIBLE);
                cell.getBackView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cell.getBackView().setVisibility(View.VISIBLE);
                    }
                },100);
            }
        }
    }

    /**
     * Initialize folding cell with parameters from attribute
     *
     * @param context context
     * @param attrs   attributes
     */
    protected void initializeFromAttributes(Context context, AttributeSet attrs) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, com.ramotion.foldingcell.R.styleable.FoldingCell, 0, 0);
        try {
            this.mAnimationDuration = array.getInt(com.ramotion.foldingcell.R.styleable.FoldingCell_animationDuration, DEF_ANIMATION_DURATION);
            this.mBackSideColor = array.getColor(com.ramotion.foldingcell.R.styleable.FoldingCell_backSideColor, DEF_BACK_SIDE_COLOR);
            this.mAdditionalFlipsCount = array.getInt(com.ramotion.foldingcell.R.styleable.FoldingCell_additionalFlipsCount, DEF_ADDITIONAL_FLIPS);
            this.mCameraHeight = array.getInt(com.ramotion.foldingcell.R.styleable.FoldingCell_cameraHeight, DEF_CAMERA_HEIGHT);
        } finally {
            array.recycle();
        }
    }

    /**
     * Instantly change current state of cell to Folded without any animations
     */
    public void setStateToFolded() {
        if (this.mAnimationInProgress || !this.mUnfolded) return;
        // get basic views
        final View contentView = getChildAt(0);
        if (contentView == null) return;
        final View titleView = getChildAt(1);
        if (titleView == null) return;
        contentView.setVisibility(GONE);
        titleView.setVisibility(VISIBLE);
        MyFoldingCell.this.mUnfolded = false;
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        layoutParams.height = titleView.getHeight();
        this.setLayoutParams(layoutParams);
        this.requestLayout();
    }

    public void autoOpenAndClose(final FoldAnimListener foldAnimListener){
        unfold(false, new UnfoldAnimListener() {
            @Override
            public void unFoldAnimEnd() {
                mHanler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fold(false,foldAnimListener);
                    }
                },1000);
            }
        });
    }

    public void setBackSideColor(int backSideColor){
        this.mBackSideColor=backSideColor;
        this.requestLayout();

      }

    public static Handler mHanler = new Handler();

    public interface UnfoldAnimListener{
        public void unFoldAnimEnd();
    }

    public interface FoldAnimListener{
        public void foldAnimEnd();
    }

//    private int mCameraHeight;
//
//    private int mDuration;
//
//    public MyFoldingCell(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public MyFoldingCell(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    public MyFoldingCell(Context context) {
//        super(context);
//    }
//
//    @Override
//    protected void startFoldAnimation(ArrayList<FoldingCellView> foldingCellElements, ViewGroup foldingLayout, int part90degreeAnimationDuration, AnimationEndListener animationEndListener) {
//        for (FoldingCellView foldingCellElement : foldingCellElements)
//            foldingLayout.addView(foldingCellElement);
//
//        Collections.reverse(foldingCellElements);
//        mDuration = part90degreeAnimationDuration*foldingCellElements.size();
//        int nextDelay = 0;
//        for (int i = 0; i < foldingCellElements.size(); i++) {
//            final FoldingCellView cell = foldingCellElements.get(i);
//            cell.setVisibility(VISIBLE);
//            // not FIRST(BOTTOM) element - animate front view
//
//            if(i == foldingCellElements.size() - 1){
//                cell.getBackView().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        cell.getBackView().setVisibility(View.INVISIBLE);
//                    }
//                },nextDelay);
//            }
//
//            if (i != 0) {
//                FoldAnimation foldAnimation = new FoldAnimation(FoldAnimation.FoldAnimationMode.UNFOLD_UP, mCameraHeight, part90degreeAnimationDuration)
//                        .withStartOffset(nextDelay)
//                        .withInterpolator(new DecelerateInterpolator());
//                // if last(top) element - add end listener
//                if (i == foldingCellElements.size() - 1) {
//                    foldAnimation.setAnimationListener(animationEndListener);
//                }
//                cell.animateFrontView(foldAnimation);
//                nextDelay = nextDelay + part90degreeAnimationDuration;
//            }
//
//            // if not last(top) element - animate whole view
//            if (i != foldingCellElements.size() - 1) {
//                cell.startAnimation(new FoldAnimation(FoldAnimation.FoldAnimationMode.FOLD_UP, mCameraHeight, part90degreeAnimationDuration)
//                        .withStartOffset(nextDelay)
//                        .withInterpolator(new DecelerateInterpolator()));
//                nextDelay = nextDelay + part90degreeAnimationDuration;
//            }
//
//        }
//    }
//
//    @Override
//    protected void startUnfoldAnimation(ArrayList<FoldingCellView> foldingCellElements, ViewGroup foldingLayout, int part90degreeAnimationDuration, AnimationEndListener animationEndListener) {
//        int nextDelay = 0;
//        mDuration = part90degreeAnimationDuration*foldingCellElements.size();
//        for (int i = 0; i < foldingCellElements.size(); i++) {
//            final FoldingCellView cell = foldingCellElements.get(i);
//            cell.setVisibility(VISIBLE);
//            foldingLayout.addView(cell);
//            // if not first(top) element - animate whole view
//
//            if (i != 0) {
//                FoldAnimation foldAnimation = new FoldAnimation(FoldAnimation.FoldAnimationMode.UNFOLD_DOWN, mCameraHeight, part90degreeAnimationDuration)
//                        .withStartOffset(nextDelay)
//                        .withInterpolator(new DecelerateInterpolator());
//
//                // if last(bottom) element - add end listener
//                if (i == foldingCellElements.size() - 1) {
//                    foldAnimation.setAnimationListener(animationEndListener);
//                }
//
//                nextDelay = nextDelay + part90degreeAnimationDuration;
//                cell.startAnimation(foldAnimation);
//
//            }
//            // not last(bottom) element - animate front view
//            if (i != foldingCellElements.size() - 1) {
//                cell.animateFrontView(new FoldAnimation(FoldAnimation.FoldAnimationMode.FOLD_DOWN, mCameraHeight, part90degreeAnimationDuration)
//                        .withStartOffset(nextDelay)
//                        .withInterpolator(new DecelerateInterpolator()));
//                nextDelay = nextDelay + part90degreeAnimationDuration;
//            }
//
//            if(i == 0){
//                cell.getBackView().setVisibility(View.INVISIBLE);
//                cell.getBackView().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        cell.getBackView().setVisibility(View.VISIBLE);
//                    }
//                },100);
//            }
//        }
//    }
//
//    /**
//     * 父类mCameraHeight方法为私有,不能继承,这里折中继承initializeFromAttributes获取
//     * 如果MyFoldingCell在代码中new使用还需要继承initialize方法
//     * @param context
//     * @param attrs
//     */
//    @Override
//    protected void initializeFromAttributes(Context context, AttributeSet attrs) {
//        super.initializeFromAttributes(context, attrs);
//        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, com.ramotion.foldingcell.R.styleable.FoldingCell, 0, 0);
//        try {
//            this.mCameraHeight = array.getInt(com.ramotion.foldingcell.R.styleable.FoldingCell_cameraHeight, 30);
//        } finally {
//            array.recycle();
//        }
//    }
//
//
//    Handler mHanler = new Handler();
//    public void autoOpenAndClose(){
//        unfold(false);
//        Log.e("yanlc","mDuration = "+mDuration);
//        mHanler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.e("yanlc","fold");
//                fold(false);
//            }
//        },mDuration);
//    }

}
