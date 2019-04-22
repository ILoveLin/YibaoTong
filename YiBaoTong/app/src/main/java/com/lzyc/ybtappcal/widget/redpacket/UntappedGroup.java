package com.lzyc.ybtappcal.widget.redpacket;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.widget.redpacket.anima.Rotate3dAnimation;


/**
 * Created by lxx on 2017/4/12.
 */
public class UntappedGroup extends FrameLayout {

    public UntappedGroup(Context context) {
        this(context,null);
    }

    public UntappedGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    private void initViews(){

        LayoutInflater.from(getContext()).inflate(R.layout.layout_widget_redpacket_untapped,this,true);

        final FrameLayout layout = (FrameLayout) findViewById(R.id.layout);

        final ImageView iconToastCoin = (ImageView) findViewById(R.id.icon_toast_coin);

        final ImageView btnReceive = (ImageView) findViewById(R.id.btn_receive);

        final AnimationSet mainAnimationSet = createAnimationSet();

        mainAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(null != onUntappedkListener){
                    onUntappedkListener.onFinish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        final AnimationSet iconToastAnim = createAnimationSet();

        iconToastAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startAnimation(mainAnimationSet);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        final Rotate3dAnimation rotateAnimation = new Rotate3dAnimation(50,50, Rotate3dAnimation.ROTATE_DECREASE);

        rotateAnimation.setDuration(800);

//        rotateAnimation.setRepeatCount(1);

        rotateAnimation.setFillAfter(true);

        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                iconToastCoin.clearAnimation();
                iconToastCoin.startAnimation(iconToastAnim);
            }

            @Override
            public void onAnimationRepeat(Animation rotateAnimation) {

            }
        });

        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(null != onUntappedkListener){
//                    onUntappedkListener.onFinish();
//                }
            }
        });

        btnReceive.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnReceive.setClickable(false);
                        btnReceive.setEnabled(false);
                        iconToastCoin.clearAnimation();
                        iconToastCoin.startAnimation(rotateAnimation);
                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                show(layout);
            }
        },1000);

//        show(layout);
    }

    public void show(View v){
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(v, "alpha", 0f, 1f);

        ObjectAnimator anim2 = ObjectAnimator.ofFloat(v, "scaleX", 1, 1.1f);

        ObjectAnimator anim3 = ObjectAnimator.ofFloat(v, "scaleY", 1, 1.1f);

        ObjectAnimator anim4 = ObjectAnimator.ofFloat(v, "scaleX", 1.1f, 1);

        ObjectAnimator anim5 = ObjectAnimator.ofFloat(v, "scaleY", 1.1f, 1);


        AnimatorSet animSet = new AnimatorSet();

        animSet.play(anim1).with(anim2);

        animSet.play(anim2).with(anim3);

        animSet.play(anim4).after(anim3);

        animSet.play(anim5).after(anim3);

        animSet.setDuration(200);

        animSet.start();

    }

    private AnimationSet createAnimationSet(){

        ScaleAnimation scaleAnimation = new ScaleAnimation(1f,2f,1f,2f,
                ScaleAnimation.RELATIVE_TO_SELF,0.5f, ScaleAnimation.RELATIVE_TO_SELF,0.5f);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1f,0f);

        final AnimationSet animationSet = new AnimationSet(true);

        animationSet.setDuration(800);

        animationSet.setFillAfter(true);

        animationSet.addAnimation(scaleAnimation);

        animationSet.addAnimation(alphaAnimation);

        return animationSet;
    }

    private UntappedkListener onUntappedkListener;

    public void setOnUntappedkListener(UntappedkListener onUntappedkListener){
        this.onUntappedkListener = onUntappedkListener;
    }

    public interface UntappedkListener{
        void onFinish();
    }
}
