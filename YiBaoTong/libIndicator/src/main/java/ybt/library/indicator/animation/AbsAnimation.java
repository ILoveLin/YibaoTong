package ybt.library.indicator.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;

public abstract class AbsAnimation<T extends Animator> {

    public static final int DEFAULT_ANIMATION_TIME = 350;

    protected long animationDuration = DEFAULT_ANIMATION_TIME;
    protected ValueAnimation.UpdateListener listener;
    protected T animator;

    public AbsAnimation(ValueAnimation.UpdateListener listener) {
        this.listener = listener;
        animator = createAnimator();
    }

    
    public abstract T createAnimator();

    public abstract AbsAnimation progress(float progress);

    public AbsAnimation duration(long duration) {
        animationDuration = duration;

        if (animator instanceof AnimatorSet) {
            int size = ((AnimatorSet) animator).getChildAnimations().size();
            long singleDuration = animationDuration / size;
            animator.setDuration(singleDuration);

        } else {
            animator.setDuration(animationDuration);
        }

        return this;
    }

    public void start() {
        if (animator != null) {
            animator.start();
        }
    }

    public void end() {
        if (animator != null) {
            animator.end();
        }
    }
}
