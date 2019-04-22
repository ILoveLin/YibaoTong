package com.lzyc.ybtappcal.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lzyc.ybtappcal.R;

/**
 * 自定义listview解决顶部圆角和底部圆角，滑动不越界的问题
 * Created by yang on 2016/11/21.
 */
public class ConnerListView extends ListView {
    public ConnerListView(Context context) {
        super(context);
    }

    public ConnerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /****
     * 拦截触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                int itemnum = pointToPosition(x, y);
                if (itemnum == AdapterView.INVALID_POSITION)
                    break;
                else {
                    if (itemnum == 0) {
                        if (itemnum == (getAdapter().getCount() - 1)) {
                            setSelector(R.drawable.shape_conner_lv_normal);//只有一项数据，设置背景设置为圆角的
                        } else {
                            setSelector(R.drawable.shape_conner_lv_top); //第一项，设置为上面为圆角的
                        }
                    } else if (itemnum == (getAdapter().getCount() - 1))
                        setSelector(R.drawable.shape_conner_lv_bottom); //最后一项，设置为下面为圆角的
                    else {
                        setSelector(R.drawable.shape_conner_lv_center);//中间项，不用设置为圆角
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(ev);
    }
}