package com.lzyc.ybtappcal.activity.payment.order;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;

/**
 * 处方药预定成功弹窗
 * Created by Lxx on 2017/3/16.
 */
public class PopChufangyao {
    // 声明PopupWindow对象的引用
    private PopupWindow popupWindow;
    private TextView tvClose;
    private TextView tvNotice;
    private TextView tvConnect;
    private TextView tvSuccess;
    private TextView tvReturn;

    private Activity mContext;
    private final String NOTICE = "<font color='#000'><normal>温馨提示:</normal></font><font color='#555555'>晚上10点以后提交的需求将于次日早上10点前回拨给您。如紧急，请拨打&nbsp;<img src='"+R.mipmap.icon_buy_toast_dail+"'/>&nbsp;<font color='#409bf0'>400-600-8725</font>(非客服时间可留言)，请注意我们的客服回拨区号，<font color='#409bf0'>020-固话</font>请注意接听。客服电话:<font color='#409bf0'>020-8306 4110，020-8306 4116，020-8306 4303，020-8306 4305，202-8306 4107</font></font>";

//    private final String NOTICE = "<font color='#000'><normal>温馨提示:</normal></font>\n" +
//            " <font color='#555555'>晚上10点以后提交的需求将于次日早上10点前回拨给您。如紧急，请拨打&nbsp;</font>\n" +
//            " <img src='\"+R.mipmap.icon_buy_toast_dail+\"'/>&nbsp;\n" +
//            " <font color='#409bf0'>400-600-8725</font>\n" +
//            " <font color='#555555'>(非客服时间可留言)，请注意我们的客服回拨区号，</font>\n" +
//            " <font color='#409bf0'>020-固话</font>\n" +
//            " <font color='#555555'>请注意接听。客服电话:</font>\n" +
//            " <font color='#409bf0'>020-8306 4110，020-8306 4116，020-8306 4303，020-8306 4305，202-8306 4107</font>";


    private final String phoneNum = "4006008725";
    private int isOTC = 0;

    public PopChufangyao(View v, Activity mContext, int isOTC){
        this.mContext = mContext;
        this.isOTC = isOTC;
        try{
            init(v);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void init(View v){
        getPopupWindow();
        popupWindow.showAtLocation(v, Gravity.LEFT, 0, 0);
    }

    /**
     * 创建PopupWindow
     */
    protected void initPopuptWindow() {
        View popupWindow_view = mContext.getLayoutInflater().inflate(R.layout.pop_chufangyao, null,
                false);
        popupWindow = new PopupWindow(popupWindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        final LinearLayout linToast = (LinearLayout) popupWindow_view.findViewById(R.id.pop_chufangyao_toast);
        tvClose = (TextView) popupWindow_view.findViewById(R.id.chufangyao_close);
        tvNotice = (TextView) popupWindow_view.findViewById(R.id.chufangyao_notice);
        tvConnect = (TextView) popupWindow_view.findViewById(R.id.chufangyao_connect);
        tvSuccess = (TextView) popupWindow_view.findViewById(R.id.chufangyao_success);
        tvReturn = (TextView) popupWindow_view.findViewById(R.id.chufangyao_return);
        if(2 == isOTC){
            getNoticeInfo(tvNotice);
        } else {
            tvNotice.setVisibility(View.GONE);
            tvConnect.setVisibility(View.GONE);
            tvSuccess.setGravity(Gravity.CENTER);
            tvSuccess.setText("恭喜您，支付成功");
            tvReturn.setGravity(Gravity.CENTER);
        }

        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setFillAfter(true);
        animationSet.setDuration(800);
        linToast.startAnimation(animationSet);

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
                popupWindow = null;
                mContext.finish();
            }
            }
        });
    }

    /***
     * 获取PopupWindow实例
     */
    private void getPopupWindow() {
        if (null != popupWindow) {
            popupWindow.dismiss();
            return;
        } else {
            initPopuptWindow();
        }
    }

    /**
     * 设置详情文字
     */
    private void getNoticeInfo(TextView tv){
        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String s) {
                Drawable drawable = mContext.getResources().getDrawable(Integer.valueOf(s));
                drawable.setBounds(0, -15, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()-15);
                return drawable;
            }
        };

        SpannableString spanableInfo = new SpannableString(Html.fromHtml(NOTICE,imageGetter,null));
        spanableInfo.setSpan(new Clickable(callListener),42,54, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spanableInfo);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private View.OnClickListener callListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + phoneNum);
            intent.setData(data);
            mContext.startActivity(intent);
        }
    };

    class Clickable extends ClickableSpan {
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener l) {
            mListener = l;
        }

        /**
         * 重写父类点击事件
         */
        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }

}
