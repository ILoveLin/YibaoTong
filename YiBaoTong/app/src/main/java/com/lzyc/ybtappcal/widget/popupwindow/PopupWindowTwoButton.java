package com.lzyc.ybtappcal.widget.popupwindow;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.util.ScreenUtils;
import com.lzyc.ybtappcal.util.Util;

/**
 * 作者：xujm on 2015/12/29
 * 备注：com.lzyc.framwork.widget.popupwindow
 */
public class PopupWindowTwoButton extends PopupWindow {

    private Activity mContext;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvCancel;
    private TextView tvOk;
    private View lineTwo;
    private View conentView;

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

    public TextView getTv_content() {
        return tvContent;
    }

    public void setTv_content(TextView tv_content) {
        this.tvContent = tv_content;
    }

    public TextView getTvCancel() {
        return tvCancel;
    }

    public void setTvCancel(TextView tvCancel) {
        this.tvCancel = tvCancel;
    }

    public TextView getTvOk() {
        return tvOk;
    }

    public View getLineTwo() {
        return lineTwo;
    }

    public void setLineTwo(View lineTwo) {
        this.lineTwo = lineTwo;
    }

    public void setTvOk(TextView tvOk) {
        this.tvOk = tvOk;
    }

    public PopupWindowTwoButton(Activity context) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.popup_two_button, null);

        tvTitle = (TextView) conentView.findViewById(R.id.tv_two_bt_title);
        tvContent = (TextView) conentView.findViewById(R.id.tv_two_bt_content);
        tvCancel = (TextView) conentView.findViewById(R.id.tv_two_bt_cancel);
        tvCancel.getPaint().setFakeBoldText(true);
        tvOk = (TextView) conentView.findViewById(R.id.tv_two_bt_ok);
        lineTwo =conentView.findViewById(R.id.line_two);

        int w = ScreenUtils.getScreenWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(new BitmapDrawable());
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
    }


    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent, int gravity) {
        if (!this.isShowing()) {

            if(View.GONE == tvOk.getVisibility()){
                tvCancel.setBackgroundResource(R.drawable.selector_pop_corner_bottom);
            }

            show(conentView);
            // 以下拉方式显示popupwindow
            Util.setBackgroundAlpha(mContext, 0.5f);
            this.showAtLocation(parent, gravity, 0, 0);
        } else {
            this.dismiss();
        }

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

    @Override
    public void dismiss() {
        super.dismiss();
        Util.setBackgroundAlpha(mContext, 1f);
    }

}
