package com.lzyc.ybtappcal.view.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

import com.lzyc.ybtappcal.R;

/**
 * Created by yang on 2016/5/4.
 */
public class LoadingProgressBar extends ProgressDialog {


    public LoadingProgressBar(Context context) {
        super(context, R.style.LoadingDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_dialog_loading_progress);
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(LoadingProgressBar.this.isShowing()){
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
