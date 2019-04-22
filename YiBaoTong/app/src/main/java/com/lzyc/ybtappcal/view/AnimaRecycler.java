package com.lzyc.ybtappcal.view;


import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * Created by Lxx on 2017/3/14.
 */
public class AnimaRecycler extends DefaultItemAnimator {

    private List<ViewHolder> runHolders = new ArrayList<ViewHolder>();

    @Override
    public boolean animateAdd(ViewHolder holder) {
        runHolders.add(holder);
        return super.animateAdd(holder);
    }

    @Override
    public void runPendingAnimations() {
        super.runPendingAnimations();
        for(final ViewHolder holder : runHolders){
            final View view = holder.itemView;
            TranslateAnimation animation = new TranslateAnimation(-view.getWidth(),0, 0, 0);
            animation.setDuration(800);
            animation.setFillAfter(true);
            view.startAnimation(animation);
            animation.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    dispatchAddStarting(holder);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {

                }
                @Override
                public void onAnimationEnd(Animation animation) {

                    TranslateAnimation lefAnimation = new TranslateAnimation(0,-30, 0, 0);
                    lefAnimation.setDuration(100);
                    lefAnimation.setFillAfter(true);
                    view.startAnimation(lefAnimation);
                    lefAnimation.setAnimationListener(new AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            TranslateAnimation rightAnimation = new TranslateAnimation(-30,0, 0, 0);
                            rightAnimation.setInterpolator(new BounceInterpolator());
                            rightAnimation.setDuration(500);
                            rightAnimation.setFillAfter(true);
                            view.startAnimation(rightAnimation);
                        }
                    });

                    dispatchAddFinished(holder);
                    runHolders.remove(holder);
                }
            });
        }
    }
}
