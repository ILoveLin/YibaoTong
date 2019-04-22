package com.lzyc.ybtappcal.widget.popupwindow;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.util.Util;

/**
 * BasePopupWindow
 * 作者：xujm on 2015/12/29
 * 备注：com.lzyc.framwork.widget.popupwindow
 */
public class BasePopupWindow extends PopupWindow {

    private Activity mContext;
    private View conentView;

    private final float START = 1f;
    private final float END = 0.95f;

    public View getConentView() {
        return conentView;
    }

    public void setConentView(View conentView) {
        this.conentView = conentView;
    }

    public BasePopupWindow(Activity context, int itemLayoutId, int w, int h) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(itemLayoutId, null);
//        int h = context.getWindowManager().getDefaultDisplay().getHeight();
//        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(h);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setOutsideTouchable(false);
        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setTouchable(true);
        this.update();
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
    }


    /**
     * 显示popupWindow
     * @param parent
     */
    public void showPopupWindow(View parent, int gravity) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            show(conentView);
            Util.setBackgroundAlpha(mContext, 0.5f);
            //setAnimationStyle(R.style.popup_anim_style);
            this.showAtLocation(parent, gravity, 0, 0);
        } else {
            this.dismiss();
        }

    }

    /**
     * 显示popupWindow
     * @param parent
     */
    public void showPopupWindowFade(View parent, int gravity) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            show(conentView);
//            Util.setBackgroundAlpha(mContext, 0.5f);
            setAnimationStyle(R.style.popAnimationFade);
            this.showAtLocation(parent, gravity, 0, 0);
        } else {
            this.dismiss();
        }

    }

    /**
     * 显示popupWindow
     * 自下而上弹出
     * @param parent
     */
    public void showPopupWindowTop(View parent, int gravity) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            Util.setBackgroundAlpha(mContext, 0.5f);
            setAnimationStyle(R.style.Animation_Bottom_Rising);
            this.showAtLocation(parent, gravity, 0, 0);
        } else {
            this.dismiss();
        }

    }


    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showDropDownPopupWindow(View parent, int x, int y) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            Util.setBackgroundAlpha(mContext, 0.5f);
            setAnimationStyle(R.style.popup_anim_style);
            this.showAsDropDown(parent, x, y);
        } else {
            this.dismiss();
        }

    }

    /**
     * 右侧下拉popupWindow
     *
     * @param parent
     */
    public void showDropPopRight(View parent, int gravity) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            Util.setBackgroundAlpha(mContext, 0.5f);
            setAnimationStyle(R.style.popup_anim_style_bootom_right);
            this.showAtLocation(parent, gravity, 0, 0);
        } else {
            this.dismiss();
        }
    }

    /**
     * 左侧下拉popupWindow
     *
     * @param parent
     */
    public void showDropPopLeft(View parent, int gravity) {
        if (!this.isShowing()) {
            Util.setBackgroundAlpha(mContext, 0.5f);
            setAnimationStyle(R.style.popup_anim_style_bootom_left);
            this.showAtLocation(parent, gravity, 0, 0);
        } else {
            this.dismiss();
        }
    }

    public void show(View v){

        ObjectAnimator anim2 = ObjectAnimator.ofFloat(v, "scaleX", START, END);

        ObjectAnimator anim3 = ObjectAnimator.ofFloat(v, "scaleY", START, END);

        ObjectAnimator anim4 = ObjectAnimator.ofFloat(v, "scaleX", END, START);

        ObjectAnimator anim5 = ObjectAnimator.ofFloat(v, "scaleY", END, START);


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
