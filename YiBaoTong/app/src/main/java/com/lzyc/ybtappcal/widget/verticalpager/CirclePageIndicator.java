/*
 * Copyright (C) 2011 Patrik Akerfeldt
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lzyc.ybtappcal.widget.verticalpager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;
import static com.lzyc.ybtappcal.widget.verticalpager.PagerHandler.MSG_DELAY;
import static com.lzyc.ybtappcal.widget.verticalpager.PagerHandler.MSG_UPDATE_IMAGE;

@SuppressWarnings("deprecation")
public class CirclePageIndicator extends View implements ViewPager.OnPageChangeListener {

    protected static final int INVALID_POINTER = -1;

    protected float mRadius;
    protected final Paint mPaintPageFill = new Paint(ANTI_ALIAS_FLAG);
    protected final Paint mPaintStroke = new Paint(ANTI_ALIAS_FLAG);
    protected final Paint mPaintFill = new Paint(ANTI_ALIAS_FLAG);
    protected ViewPager mViewPager;
    protected double mDisBetweenCirclesMultiplier = 3;
    protected double mDisMultiplierCompensation = 1.07;
    protected int mCurrentPage;
    protected int mSnapPage;
    protected float mPageOffset;
    protected int mScrollState;
    protected int mOrientation;
    protected boolean mCentered;
    protected boolean mSnap;

    protected int mTouchSlop;
    protected float mLastMotionX = -1;
    protected int mActivePointerId = INVALID_POINTER;
    protected boolean mIsDragging;

    protected PagerHandler handler;

    public CirclePageIndicator(Context context) {
        this(context, null);
    }

    public CirclePageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) return;

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
    }

    /**
     * 设置圆点之间的距离相对于圆点半径的倍数
     * @param timesToRadius_multiplier 圆点间距相对于圆点半径的倍数
     */
    public void setDistanceBetweenCircles(double timesToRadius_multiplier) {
        mDisBetweenCirclesMultiplier = timesToRadius_multiplier;
        invalidate();
    }

    /**
     * 得到圆点之间的距离相对于圆点半径的倍数
     * @return 圆点间距相对于圆点半径的倍数
     */
    public double getDistanceBetweenCirclesMultiplier() {
        return mDisBetweenCirclesMultiplier;
    }

    public void setCentered(boolean centered) {
        mCentered = centered;
        invalidate();
    }

    public boolean isCentered() {
        return mCentered;
    }

    public void setPageColor(@ColorInt int pageColor) {
        mPaintPageFill.setColor(pageColor);
        invalidate();
    }

    public int getPageColor() {
        return mPaintPageFill.getColor();
    }

    /**
     * 设置当前被选中的圆点的填充颜色
     * @param fillColor 当前被选中的圆点的填充颜色
     */
    public void setFillColor(@ColorInt int fillColor) {
        mPaintFill.setColor(fillColor);
        invalidate();
    }

    /**
     * 得到当前被选中的圆点的填充颜色
     * @return 当前被选中的圆点的填充颜色
     */
    public int getFillColor() {
        return mPaintFill.getColor();
    }

    public void setOrientation(int orientation) {
        switch (orientation) {
            case HORIZONTAL:
            case VERTICAL:
                mOrientation = orientation;
                requestLayout();
                break;
            default:
                throw new IllegalArgumentException("Orientation must be either HORIZONTAL or VERTICAL.");
        }
    }

    public int getOrientation() {
        return mOrientation;
    }

    public void setStrokeColor(int strokeColor) {
        mPaintStroke.setColor(strokeColor);
        invalidate();
    }

    public int getStrokeColor() {
        return mPaintStroke.getColor();
    }

    public void setStrokeWidth(float strokeWidth) {
        mPaintStroke.setStrokeWidth(strokeWidth);
        invalidate();
    }

    public float getStrokeWidth() {
        return mPaintStroke.getStrokeWidth();
    }

    /**
     * 设置圆点半径(px)
     * @param radiusInPx 圆点半径(px)
     */
    public void setRadius(float radiusInPx) {
        mRadius = radiusInPx;
        invalidate();
    }

    /**
     * 得到圆点半径(px)
     * @return  圆点半径(px)
     */
    public float getRadius() {
        return mRadius;
    }

    public void setSnap(boolean snap) {
        mSnap = snap;
        invalidate();
    }

    public boolean isSnap() {
        return mSnap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mViewPager == null) return;
        final int count = mViewPager.getAdapter().getCount() / 1000;
        if (count == 0) return;

        if (mCurrentPage >= count) {
            setCurrentItem(count - 1);
            return;
        }

        int longSize;
        int longPaddingBefore;
        int longPaddingAfter;
        int shortPaddingBefore;
        if (mOrientation == HORIZONTAL) {
            longSize = getWidth();
            longPaddingBefore = getPaddingLeft();
            longPaddingAfter = getPaddingRight();
            shortPaddingBefore = getPaddingTop();
        } else {
            longSize = getHeight();
            longPaddingBefore = getPaddingTop();
            longPaddingAfter = getPaddingBottom();
            shortPaddingBefore = getPaddingLeft();
        }

        final float threeRadius = (float) (mRadius * mDisBetweenCirclesMultiplier * mDisMultiplierCompensation);
        final float shortOffset = shortPaddingBefore + mRadius;
        float longOffset = longPaddingBefore + mRadius;
        if (mCentered) longOffset += ((longSize - longPaddingBefore - longPaddingAfter) / 2.0f) - ((count * threeRadius) / 2.0f);

        float dX;
        float dY;

        float pageFillRadius = mRadius;
        if (mPaintStroke.getStrokeWidth() > 0) pageFillRadius -= mPaintStroke.getStrokeWidth() / 2.0f;

        //Draw stroked circles
        for (int iLoop = 0; iLoop < count; iLoop++) {
            float drawLong = longOffset + (iLoop * threeRadius);
            if (mOrientation == HORIZONTAL) {
                dX = drawLong;
                dY = shortOffset;
            } else {
                dX = shortOffset;
                dY = drawLong;
            }
            // Only paint fill if not completely transparent
            if (mPaintPageFill.getAlpha() > 0) canvas.drawCircle(dX, dY, pageFillRadius, mPaintPageFill);

            // Only paint stroke if a stroke width was non-zero
            if (pageFillRadius != mRadius) canvas.drawCircle(dX, dY, mRadius, mPaintStroke);
        }

        //Draw the filled circle according to the current scroll
        float cx = (mSnap ? mSnapPage : mCurrentPage) * threeRadius;
        if (mCurrentPage != count - 1) {
            cx += mPageOffset * threeRadius;
            canvas.drawCircle(longOffset + cx, shortOffset, mRadius, mPaintFill);
        } else if (mPageOffset < 0.5) {
            canvas.drawCircle(longOffset + cx, shortOffset, mRadius, mPaintFill);
        } else {
            canvas.drawCircle(longOffset, shortOffset, mRadius, mPaintFill);
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (super.onTouchEvent(ev)) return true;
        if ((mViewPager == null) || (mViewPager.getAdapter().getCount() == 0)) return false;

        final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mLastMotionX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE: {
                final int activePointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                final float x = MotionEventCompat.getX(ev, activePointerIndex);
                final float deltaX = x - mLastMotionX;

                if (!mIsDragging) if (Math.abs(deltaX) > mTouchSlop) mIsDragging = true;

                if (mIsDragging) {
                    mLastMotionX = x;
                    if (mViewPager.isFakeDragging() || mViewPager.beginFakeDrag()) mViewPager.fakeDragBy(deltaX);
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (!mIsDragging) {
                    final int count = mViewPager.getAdapter().getCount() / 1000;
                    final int width = getWidth();
                    final float halfWidth = width / 2f;
                    final float sixthWidth = width / 6f;

                    if ((mCurrentPage > 0) && (ev.getX() < halfWidth - sixthWidth)) {
                        return true;
                    } else if ((mCurrentPage < count - 1) && (ev.getX() > halfWidth + sixthWidth)) {
                        return true;
                    }
                }

                mIsDragging = false;
                mActivePointerId = INVALID_POINTER;
                if (mViewPager.isFakeDragging()) mViewPager.endFakeDrag();
                break;
            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int index = MotionEventCompat.getActionIndex(ev);
                mLastMotionX = MotionEventCompat.getX(ev, index);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            }
            case MotionEventCompat.ACTION_POINTER_UP:
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                mLastMotionX = MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, mActivePointerId));
                break;
        }
        return true;
    }

    public void setViewPager(ViewPager view) {
        if (mViewPager == view) return;
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        if (view.getAdapter() == null) throw new IllegalStateException("ViewPager does not have mAdapter instance.");
        mViewPager = view;
        mViewPager.setOnPageChangeListener(this);
        invalidate();
    }

    public void setViewPager(ViewPager view, PagerHandler handler) {
        setViewPager(view);
        this.handler = handler;
    }

    public void setCurrentItem(int item) {
        if (mViewPager == null) throw new IllegalStateException("ViewPager has not been bound.");
        mCurrentPage = item % (mViewPager.getAdapter().getCount() / 1000);
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mScrollState = state;

        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
//                handler.sendEmptyMessage(PagerHandler.MSG_KEEP_SILENT);
                break;
            case ViewPager.SCROLL_STATE_IDLE:
//                handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(1 >= mViewPager.getAdapter().getCount()) return;
        mCurrentPage = position % (mViewPager.getAdapter().getCount() / 1000);
        mPageOffset = positionOffset;
        invalidate();

        View view = mViewPager.getChildAt(position);
//        if(position == 0){
//            scrollTo(view.getWidth(),0);
//        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mSnap || mScrollState == ViewPager.SCROLL_STATE_IDLE) {
            mCurrentPage = position % (mViewPager.getAdapter().getCount() / 1000);
            mSnapPage = position % (mViewPager.getAdapter().getCount() / 1000);
            invalidate();
        }

        int count = mViewPager.getAdapter().getCount();
        if(position == count - 1 && count != 0){
            position = 0;
            Message message = Message.obtain();
            message.what = PagerHandler.MSG_PAGE_CHANGED;
            message.arg1 = position;
            handler.sendMessage(message);
        }
//        else{
//            position += 1;
//        }
        handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mOrientation == HORIZONTAL) {
            setMeasuredDimension(measureLong(widthMeasureSpec), measureShort(heightMeasureSpec));
        } else {
            setMeasuredDimension(measureShort(widthMeasureSpec), measureLong(heightMeasureSpec));
        }
    }

    /**
     * Determines the width of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    protected int measureLong(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if ((specMode == MeasureSpec.EXACTLY) || (mViewPager == null)) {
            //We were told how big to be
            result = specSize;
        } else {
            //Calculate the width according the mInflatedViews count
            final int count = mViewPager.getAdapter().getCount() / 1000;
            final double threeRadius = mRadius * mDisBetweenCirclesMultiplier * mDisMultiplierCompensation;
            result = (int) (count * threeRadius);
        }
        return result;
    }

    /**
     * Determines the height of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    protected int measureShort(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            //We were told how big to be
            result = specSize;
        } else {
            //Measure the height
            result = (int) (2 * mRadius + getPaddingTop() + getPaddingBottom() + 1);
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) result = Math.min(result, specSize);
        }
        return result;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mCurrentPage = savedState.currentPage;
        mSnapPage = savedState.currentPage;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPage = mCurrentPage;
        return savedState;
    }

    protected static class SavedState extends BaseSavedState {

        protected int currentPage;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        protected SavedState(Parcel in) {
            super(in);
            currentPage = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPage);
        }

        @SuppressWarnings("UnusedDeclaration")
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
