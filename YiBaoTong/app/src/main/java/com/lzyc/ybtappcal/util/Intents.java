package com.lzyc.ybtappcal.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lzyc.ybtappcal.activity.account.LoginActivity;
import com.lzyc.ybtappcal.activity.mine.withdraw.MineWithdrawActivity;
import com.lzyc.ybtappcal.activity.mine.withdraw.MineWithdrawDetailActivity;
import com.lzyc.ybtappcal.activity.mine.withdraw.MineWithdrawListActivity;
import com.lzyc.ybtappcal.activity.mine.withdraw.MineWithdrawReturnActivity;
import com.lzyc.ybtappcal.activity.payment.order.AddressBuildActivity;
import com.lzyc.ybtappcal.activity.payment.order.DekaiDetailsActivity;
import com.lzyc.ybtappcal.activity.payment.order.LogisticsActivity;
import com.lzyc.ybtappcal.activity.payment.order.OrderCheckActivity;
import com.lzyc.ybtappcal.activity.payment.order.OrderDetailActivity;
import com.lzyc.ybtappcal.activity.payment.order.OrderSuccessActivity;
import com.lzyc.ybtappcal.activity.payment.order.OrderbyActivity;
import com.lzyc.ybtappcal.activity.reimbursement.ReimbursementDetailsActivity;
import com.lzyc.ybtappcal.activity.top.HispitalListActivity;
import com.lzyc.ybtappcal.activity.top.HomeWebActivity;


/**
 * 跳转
 * Created by Lxx on 2017/3/16.
 */
public class Intents {

    private static Intent intent = new Intent();
    private static Bundle bundle = new Bundle();

    public static final String DRAW_DETAIL = "draw_detail";

    /**
     * 药品详情【处方和非处方】
     */
    public static Intent openOrderbyActivity(Context mContext, Bundle bundle){
        intent.setClass(mContext, OrderbyActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    /**
     * 检查订单【处方和非处方】
     */
    public static Intent openOrderByCheckActivity(Context mContext, Bundle bundle){
        intent.setClass(mContext, OrderCheckActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    /**
     * 德开详情
     */
    public static Intent openDekaiDetailsActivity(Context mContext){
        intent.setClass(mContext, DekaiDetailsActivity.class);
        return intent;
    }

    /**
     * 提现
     */
    public static Intent openMineWithdrawActivity(Context mContext, Bundle bundle){
        intent.setClass(mContext, MineWithdrawActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    /**
     * 提现记录
     */
    public static void openMineWithdrawListActivity(Context mContext){
        intent.setClass(mContext, MineWithdrawListActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 提现详情
     */
    public static void openMineWithdrawDetailActivity(Context mContext, Bundle bundle){
        intent.setClass(mContext, MineWithdrawDetailActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 待返现
     */
    public static void openMineWithdrawReturnActivity(Context mContext, Bundle bundle){
        Intent intent = new Intent();
        intent.setClass(mContext, MineWithdrawReturnActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 添加地址
     */
    public static Intent openAddressBuildActivity(Context mContext, Bundle bundle){
        intent.setClass(mContext, AddressBuildActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

//    /**
//     * 扫码
//     */
//    public static Intent openCaptureActivity(Context mContext, Bundle bundle) {
//        intent.setClass(mContext, CaptureActivity.class);
//        intent.putExtras(bundle);
//        return intent;
//    }

    /**
     * 登录
     */
    public static Intent openLoginActivity(Context mContext) {
        return new Intent(mContext, LoginActivity.class);
    }

    /**
     * 订单-->待支付
     * 订单详情
     */
    public static void openOrderDetailActivity(Context mContext, Bundle bundle){
        intent.setClass(mContext, OrderDetailActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 购买成功
     */
    public static Intent openOrderSuccessActivity(Context mContext, Bundle bundle){
        intent.setClass(mContext, OrderSuccessActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    /**
     * 报销结果
     */
    public static void openReimbursementDetailsActivity(Context mContext, Bundle bundle){
        intent.setClass(mContext, ReimbursementDetailsActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 医院列表  比药
     */
//    public static void openHospitalListBYActivity(Context mContext){
//        intent.setClass(mContext, HospitalListBYActivity.class);
//        mContext.startActivity(intent);
//    }

    /**
     * 医院列表  比药
     */
    public static void openHispitalListActivity(Context mContext, Bundle bundle){
        intent.setClass(mContext, HispitalListActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 物流
     */
    public static void openLogisticsActivity(Context mContext, Bundle bundle){
        intent.setClass(mContext, LogisticsActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 首页banner  web界面
     */
    public static void openHomeWebActivity(Context mContext, Bundle bundle){
        intent.setClass(mContext,HomeWebActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

}
