package com.lzyc.ybtappcal.listener;

import android.widget.AbsListView;

/**
 * YbtRefreshListView OnScrollListener
 * Created by yang on 2016/10/24.
 */
public interface OnYbtScrollListener {
  void onYbtScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
}
