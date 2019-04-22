package com.lzyc.ybtappcal.listener;

import com.lzyc.ybtappcal.bean.DrugBean;

/**
 * 异地方案删除
 * 注意：这里的position是指group的下标
 * package_name com.lzyc.ybtappcal.listener
 * Created by yang on 2016/10/25.
 */
public interface OnDeletePlanClickListener {
    void onDeletePlanClick(int position, DrugBean drugBean);
}
