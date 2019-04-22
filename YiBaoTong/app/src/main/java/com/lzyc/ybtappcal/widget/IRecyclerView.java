package com.lzyc.ybtappcal.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.lzyc.ybtappcal.widget.xrecycler.XRecyclerView;


/**
 * Created by Lxx on 2017/3/27.
 */
public class IRecyclerView extends XRecyclerView{

    int totalOffSetDy;

    public IRecyclerView(Context context) {
        this(context,null);
    }

    public IRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(null != onScrollListener){
                    onScrollListener.onScrollStateChanged(IRecyclerView.this,newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalOffSetDy -= dy;
                if(null != onScrollListener){
                    onScrollListener.onScrolled(IRecyclerView.this,dx,dy,totalOffSetDy);
                }
            }
        });
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
    }

    private ScrollListener onScrollListener;
    public void setOnScrollListener(ScrollListener onScrollListener){
        if(null == onScrollListener){
            throw new RuntimeException("onScrollListener is Null ,please check ageain !!");
        }
        this.onScrollListener = onScrollListener;
    }
    public interface ScrollListener {
        void onScrollStateChanged(IRecyclerView recyclerView, int newState);

        void onScrolled(IRecyclerView recyclerView, int dx, int dy, int totalOffsetDy);
    }
}
