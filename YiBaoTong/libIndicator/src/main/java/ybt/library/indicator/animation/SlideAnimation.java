package ybt.library.indicator.animation;


import android.animation.IntEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.view.animation.DecelerateInterpolator;

@SuppressLint("NewApi") public class SlideAnimation extends AbsAnimation<ValueAnimator> {

    private static final String ANIMATION_X_COORDINATE = "ANIMATION_X_COORDINATE";
    private static final int ANIMATION_DURATION = 350;

    private int xStartCoordinate;
    private int xEndCoordinate;

    public SlideAnimation( ValueAnimation.UpdateListener listener) {
        super(listener);
    }

    
    @SuppressLint("NewApi") @Override
    public ValueAnimator createAnimator() {
        ValueAnimator animator = new ValueAnimator();
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                onAnimateUpdated(animation);
            }
        });

        return animator;
    }

    @SuppressLint("NewApi") @Override
    public SlideAnimation progress(float progress) {
        if (animator != null) {
            long playTime = (long) (progress * animationDuration);
            animator.setCurrentPlayTime(playTime);
        }

        return this;
    }

    
    public SlideAnimation with(int startValue, int endValue) {
        if (animator != null && hasChanges(startValue, endValue)) {

            xStartCoordinate = startValue;
            xEndCoordinate = endValue;

            PropertyValuesHolder holder = createColorPropertyHolder();
            animator.setValues(holder);
        }

        return this;
    }

    private PropertyValuesHolder createColorPropertyHolder() {
        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(ANIMATION_X_COORDINATE, xStartCoordinate, xEndCoordinate);
        holder.setEvaluator(new IntEvaluator());

        return holder;
    }

    private void onAnimateUpdated( ValueAnimator animation) {
        int xCoordinate = (Integer) animation.getAnimatedValue(ANIMATION_X_COORDINATE);

        if (listener != null) {
            listener.onSlideAnimationUpdated(xCoordinate);
        }
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean hasChanges(int startValue, int endValue) {
        if (xStartCoordinate != startValue) {
            return true;
        }

        if (xEndCoordinate != endValue) {
            return true;
        }

        return false;
    }
}
