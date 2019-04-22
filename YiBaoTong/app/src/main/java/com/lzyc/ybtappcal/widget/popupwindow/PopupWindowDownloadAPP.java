package com.lzyc.ybtappcal.widget.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.util.Util;

/**
 * 跟检查跟新类似的弹窗
 */
public class PopupWindowDownloadAPP extends PopupWindow {

    private Activity mContext;
    private TextView tv_title;
    private ProgressBar mProgressBar;
    public TextView getTv_title() {
        return tv_title;
    }

    public void setTv_title(TextView tv_title) {
        this.tv_title = tv_title;
    }


    public ProgressBar getmProgressBar() {
        return mProgressBar;
    }

    public void setmProgressBar(ProgressBar mProgressBar) {
        this.mProgressBar = mProgressBar;
    }

    public PopupWindowDownloadAPP(Activity context) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(R.layout.pop_download_app, null);
        tv_title = (TextView) conentView.findViewById(R.id.tv_two_bt_title);
        mProgressBar= (ProgressBar) conentView.findViewById(R.id.pop_download_pb);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    }


    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent, int gravity) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            Util.setBackgroundAlpha(mContext, 0.5f);
            this.showAtLocation(parent, gravity, 0, 0);
        } else {
            this.dismiss();
        }

    }

    @Override
    public void dismiss() {
        super.dismiss();
        Util.setBackgroundAlpha(mContext, 1f);
    }
}
