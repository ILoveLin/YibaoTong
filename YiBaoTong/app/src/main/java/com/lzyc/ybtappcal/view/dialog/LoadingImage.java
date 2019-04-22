package com.lzyc.ybtappcal.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.lzyc.ybtappcal.R;

/**
 * package_name com.lzyc.ybtappcal.view.dialog
 * Created by yang on 2016/6/25.
 */
public class LoadingImage extends Dialog {
    private int height=0;
    public LoadingImage(Context context) {
        super(context,R.style.LoadingImageDialog);
    }
    public LoadingImage(Context context,int height) {
        super(context,R.style.LoadingImageDialog);
        this.height=height;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_loading_image);
        ImageView imageView = (ImageView) findViewById(R.id.include_loading_iv);
        WindowManager.LayoutParams layoutParams= getWindow().getAttributes();
        layoutParams.width=getWindow().getWindowManager().getDefaultDisplay().getWidth();
        if(height>0){
            layoutParams.height=height;
            getWindow().setGravity(Gravity.CENTER);
        }else{
            layoutParams.height=getWindow().getWindowManager().getDefaultDisplay().getHeight();
        }
        this.getWindow().setAttributes(layoutParams);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }
}
