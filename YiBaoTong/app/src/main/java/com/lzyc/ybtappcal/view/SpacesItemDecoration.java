package com.lzyc.ybtappcal.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Lxx on 2017/3/27.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    private int ignoreIndex = -1;

    public SpacesItemDecoration(int space,int position) {
        this.space=space;
        this.ignoreIndex = position;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if(ignoreIndex != -1 ){
            View ignoreView = parent.getLayoutManager().findViewByPosition(ignoreIndex);
            if(view == ignoreView){
                outRect.bottom=space;
                return;
            }
        }

        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        //注释这两行是为了上下间距相同
//        if(parent.getChildAdapterPosition(view)==0){
        outRect.top=space;
//        }

    }
}
