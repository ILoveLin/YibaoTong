package com.lzyc.ybtappcal.widget.redpacket;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.Intents;


/**
 * Created by Lxx on 2017/4/13.
 */
public class LittleRedPacketGroup extends FrameLayout {

    ImageView iconGold;
    ImageView circleShadow;
    View redPacked;
    String returnMoney;

    public LittleRedPacketGroup(Context context) {
        super(context);
        initViews();
    }

    private void initViews(){
        LayoutInflater.from(getContext()).inflate(R.layout.layout_widget_redpacket_littleredpacket,this,true);

        iconGold = (ImageView) findViewById(R.id.gold_icon);
        circleShadow = (ImageView) findViewById(R.id.circle_shadow);
        redPacked = findViewById(R.id.little_red_packed);

        redPacked.setClickable(false);
        redPacked.setEnabled(false);

        circleShadow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle mBundle = new Bundle();
                mBundle.putInt("type", 2);
                mBundle.putInt("red_packet", 123);
                mBundle.putString("returnMoney", returnMoney);
                mBundle.putInt(Contants.KEY_PAGE,Contants.VAL_PAGE_HOONBAO);
                Intents.openMineWithdrawReturnActivity(getContext(), mBundle);
            }
        });

    }

    public void playLayout(){
        float cy = redPacked.getY();

        ObjectAnimator anim1 = ObjectAnimator.ofFloat(redPacked, "y",  cy , cy+30);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(redPacked, "y",  cy);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(redPacked, "y", cy, cy-10);
        ObjectAnimator anim4 = ObjectAnimator.ofFloat(redPacked, "y", cy);
        ObjectAnimator anim5 = ObjectAnimator.ofFloat(redPacked, "y", cy, cy+10);
        ObjectAnimator anim6 = ObjectAnimator.ofFloat(redPacked, "y", cy);
        ObjectAnimator anim7 = ObjectAnimator.ofFloat(redPacked, "y", cy, cy-5);
        ObjectAnimator anim8 = ObjectAnimator.ofFloat(redPacked, "y", cy);


        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim1);
        animSet.play(anim2).after(anim1);
        animSet.play(anim3).after(anim2);
        animSet.play(anim4).after(anim3);
        animSet.play(anim5).after(anim4);
        animSet.play(anim6).after(anim5);
        animSet.play(anim7).after(anim6);
        animSet.play(anim8).after(anim7);

        animSet.setDuration(100);
        animSet.start();
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                playGold();
            }
        });

    }

    public void playGold() {
        float cy = iconGold.getY();

        ObjectAnimator anim1 = ObjectAnimator.ofFloat(iconGold, "scaleX", 0, 4);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(iconGold, "scaleY", 0, 4);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(iconGold, "y",  cy , cy-300);
        ObjectAnimator anim4 = ObjectAnimator.ofFloat(iconGold, "y", cy);
        ObjectAnimator anim5 = ObjectAnimator.ofFloat(iconGold, "scaleX", 4, 0);
        ObjectAnimator anim6 = ObjectAnimator.ofFloat(iconGold, "scaleY", 4, 0);

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim1).with(anim2);
        animSet.play(anim2).with(anim3);
        animSet.play(anim4).after(anim3);
        animSet.play(anim5).with(anim4).after(anim3);
        animSet.play(anim6).with(anim4).after(anim3);
        animSet.setDuration(1000);
        animSet.start();

        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                redPacked.setClickable(true);
                redPacked.setEnabled(true);

                startUPAndUnderAnimation();

                RedPacketGroup rg = new RedPacketGroup(getContext());
            }
        });
    }


    private void startUPAndUnderAnimation() {
        float redPackedY = redPacked.getTranslationY();
        ObjectAnimator animator = ObjectAnimator.ofFloat(redPacked, "translationY", redPackedY, redPackedY - DensityUtils.dp2px(5), redPackedY);
        AnimatorSet set = new AnimatorSet();
        set.play(animator);
        set.setDuration(3000);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startUPAndUnderAnimation();
            }
        });

    }
    public void setReturnMoney(String returnMoney) {
        this.returnMoney = returnMoney;
    }
}
