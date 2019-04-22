package com.lzyc.ybtappcal.widget.redpacket;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by lxx on 2017/4/12.
 */
public class RedPacketGroup extends FrameLayout {
    TakeRedPacketGroup takeRedPacketGroup;
    LittleRedPacketGroup littleRedPacket;

    public RedPacketGroup(Context context) {
        this(context,null);
    }

    public RedPacketGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public void initViews(){

        final UntappedGroup untappedGroup = new UntappedGroup(getContext());

        takeRedPacketGroup = new TakeRedPacketGroup(getContext());

        littleRedPacket = new LittleRedPacketGroup(getContext());

        addView(littleRedPacket);
        addView(untappedGroup);

        untappedGroup.setOnUntappedkListener(new UntappedGroup.UntappedkListener() {
            @Override
            public void onFinish() {
                addView(takeRedPacketGroup);
                takeRedPacketGroup.show();
                post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            removeView(untappedGroup);
                        }catch (Exception e){}
                    }
                });
            }
        });

        takeRedPacketGroup.setOnTakeRedPacketListener(new TakeRedPacketGroup.TakeRedPacketListener() {
            @Override
            public void onFinish() {
                littleRedPacket.playLayout();
                post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            removeView(takeRedPacketGroup);
                        }catch (Exception e){}
                    }
                });
            }
        });

    }

    public void setReturnMoney(String returnMoney){
        takeRedPacketGroup.setReturnMoney(returnMoney);
        littleRedPacket.setReturnMoney(returnMoney);
    }

}
