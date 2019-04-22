package com.lzyc.ybtappcal.activity.payment.order;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.fragment.OrderBuyFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 说明书弹窗
 * Created by Lxx on 2017/3/16.
 */
public class PopShuomingshu {

    private PopupWindow popupWindow;
    private TextView tvClose;
    private Activity mContext;

    private Bundle mBundle;

    public PopShuomingshu(View v, Activity mContext, Bundle mBundle){
        this.mContext = mContext;
        this.mBundle = mBundle;
        init(v);
    }

    private void init(View v){
        getPopupWindow();
        popupWindow.showAtLocation(v, Gravity.LEFT, 0, 0);
    }

    /**
     * 创建PopupWindow
     */
    protected void initPopuptWindow() {
        View popupWindow_view = mContext.getLayoutInflater().inflate(R.layout.pop_shuomingshu, null, false);

        popupWindow = new PopupWindow(popupWindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        LinearLayout popLayout = (LinearLayout) popupWindow_view.findViewById(R.id.pop_layout);

        final LinearLayout contentLayout = (LinearLayout) popupWindow_view.findViewById(R.id.content_layout);

        popupWindow.setFocusable(true);

        popupWindow.setTouchable(true);

        tvClose = (TextView) popupWindow_view.findViewById(R.id.tv_close);

        TextView tvInstruction = (TextView) popupWindow_view.findViewById(R.id.tv_instruction);

        TextView tvInfo = (TextView) popupWindow_view.findViewById(R.id.tv_info);

        tvInstruction.setText(mBundle.getString(OrderBuyFragment.INSTRUCTION_TYPE));

        String text = mBundle.getString(OrderBuyFragment.INSTRUCTION_INFO);

        tvInfo.setText(matcherText(text));

        tvInfo.setMovementMethod(ScrollingMovementMethod.getInstance());

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closePopupWindow();
            }
        });

        show(contentLayout);
    }

    private String matcherText(String text){
        String newText;
        Pattern CRLF = Pattern.compile("(<br>)");
        Matcher m = CRLF.matcher(text);
        if (m.find()) {
            newText = m.replaceAll("\n");
            return newText;
        }
        return text;
    }

    public void show(View v){

        ObjectAnimator anim2 = ObjectAnimator.ofFloat(v, "scaleX", 1, 1.1f);

        ObjectAnimator anim3 = ObjectAnimator.ofFloat(v, "scaleY", 1, 1.1f);

        ObjectAnimator anim4 = ObjectAnimator.ofFloat(v, "scaleX", 1.1f, 1);

        ObjectAnimator anim5 = ObjectAnimator.ofFloat(v, "scaleY", 1.1f, 1);


        AnimatorSet animSet = new AnimatorSet();

        animSet.play(anim2).with(anim3);

        animSet.play(anim4).after(anim3);

        animSet.play(anim5).after(anim3);

        animSet.setDuration(200);

        animSet.start();

    }

    private void closePopupWindow(){
        if (popupWindow != null && popupWindow.isShowing()) {

            popupWindow.dismiss();

            popupWindow = null;
        }
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

}
