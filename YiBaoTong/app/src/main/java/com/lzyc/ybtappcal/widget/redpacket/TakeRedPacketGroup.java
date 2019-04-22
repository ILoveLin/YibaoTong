package com.lzyc.ybtappcal.widget.redpacket;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lxx on 2017/4/12.
 */

public class TakeRedPacketGroup extends FrameLayout {

    FrameLayout redLayout;
    View iconCard;

    ImageView imgShadow;
    View redPacked;

    FlakeView flakeView;

    String returnMoney;

    LinearLayout moneyLayout;

    public TakeRedPacketGroup(Context context) {
        super(context);
        initViews();
    }

    private void initViews(){

        LayoutInflater.from(getContext()).inflate(R.layout.layout_widget_redpacket_takeredracket,this,true);

        redLayout = (FrameLayout) findViewById(R.id.red_packed_layout);
        iconCard = findViewById(R.id.icon_card);

        imgShadow = (ImageView) findViewById(R.id.img_shadow);
        redPacked = findViewById(R.id.red_packed);

        moneyLayout = (LinearLayout) findViewById(R.id.money_layout);
        redLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                moveOut(0);
            }
        });
    }

    public void show(){

        String money = "";

        try{
            money = String.valueOf(Double.valueOf(returnMoney));
        }catch (Exception e){
            money = "0.00";
        }

        List<View> views = initMoneyViews(money);
        for(View v : views){
            moneyLayout.addView(v);
        }
        startAnimation(createAnimationSet());
        TranslateAnimation translateAnimation;

        translateAnimation = new TranslateAnimation(0, 0, 0, -(ScreenUtils.getScreenHeight() / 6));

        translateAnimation.setDuration(1500);
        translateAnimation.setFillAfter(true);
        iconCard.startAnimation(translateAnimation);

        flakeView = new FlakeView(getContext());
        this.addView(flakeView);
        flakeView.addFlakes(50);
        flakeView.setLayerType(View.LAYER_TYPE_NONE, null);

        moveOut(3000);
    }

    private List<View> initMoneyViews(String moneyString){
        List<View> list = new ArrayList<>();
        for(int i = 0 ; i < moneyString.length(); i ++){

            String money = String.valueOf(moneyString.charAt(i));

            ImageView imageView = new ImageView(getContext());

            switch (money){
                case "0":
                    imageView.setImageResource(R.mipmap.red_bag0);
                    break;

                case "1":
                    imageView.setImageResource(R.mipmap.red_bag1);
                    break;

                case "2":
                    imageView.setImageResource(R.mipmap.red_bag2);
                    break;

                case "3":
                    imageView.setImageResource(R.mipmap.red_bag3);
                    break;

                case "4":
                    imageView.setImageResource(R.mipmap.red_bag4);
                    break;

                case "5":
                    imageView.setImageResource(R.mipmap.red_bag5);
                    break;

                case "6":
                    imageView.setImageResource(R.mipmap.red_bag6);
                    break;

                case "7":
                    imageView.setImageResource(R.mipmap.red_bag7);
                    break;

                case "8":
                    imageView.setImageResource(R.mipmap.red_bag8);
                    break;

                case "9":
                    imageView.setImageResource(R.mipmap.red_bag9);
                    break;

                case ".":
                    imageView.setImageResource(R.mipmap.red_bag);
                    break;
            }

            list.add(imageView);
        }

        setParams(list);

        return list;
    }

    private void setParams(List<View> list){
        int wh = DensityUtils.dp2px(30);
        int he = DensityUtils.dp2px(40);

        if(6 <= list.size()){
            wh = DensityUtils.dp2px(25);
            he = DensityUtils.dp2px(34);
        }

        if(7 <= list.size()){
            wh = DensityUtils.dp2px(20);
            he = DensityUtils.dp2px(25);
        }

        if(9 <= list.size()){
            wh = DensityUtils.dp2px(15);
            he = DensityUtils.dp2px(20);
        }

        for(View v: list){
            v.setLayoutParams(new LinearLayout.LayoutParams(wh, he));
        }
    }

    private AnimationSet createAnimationSet(){
        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1,
                ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        final AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(800);
        animationSet.setFillAfter(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        return animationSet;
    }

    private void moveOut(int delayMillis){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(0 >= flakeView.getNumFlakes()) return;

                ScaleAnimation scaleAnimation = new ScaleAnimation(1,0,1,0,
                        ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);

                float deltaX = (imgShadow.getLeft()+imgShadow.getWidth()/2) - (redPacked.getLeft()+redPacked.getWidth()/2);
                float deltaY = (imgShadow.getTop()+imgShadow.getHeight()/2) - (redPacked.getTop()+redPacked.getHeight()/2);

                TranslateAnimation translateAnimation = new TranslateAnimation(0, deltaX,0, deltaY);

                AnimationSet animationSet = new AnimationSet(true);
                animationSet.setDuration(800);
                animationSet.setFillAfter(true);
                animationSet.addAnimation(scaleAnimation);
                animationSet.addAnimation(translateAnimation);
                redPacked.startAnimation(animationSet);

                flakeView.subtractFlakes(flakeView.QUANTITY);

                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if(null != onTakeRedPacketListener){
                            onTakeRedPacketListener.onFinish();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        },delayMillis);
    }

    public String getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(String returnMoney) {
        this.returnMoney = returnMoney;
    }

    private TakeRedPacketListener onTakeRedPacketListener;
    public void setOnTakeRedPacketListener(TakeRedPacketListener onTakeRedPacketListener){
        this.onTakeRedPacketListener = onTakeRedPacketListener;
    }
    public interface TakeRedPacketListener{
        void onFinish();
    }

}
