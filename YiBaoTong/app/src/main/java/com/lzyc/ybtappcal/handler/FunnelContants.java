package com.lzyc.ybtappcal.handler;

/**
 * love lin
 * 漏斗事件的标注
 */
public class FunnelContants {

    //漏斗一 共三个事件
    public static final String FUNNEL1_SEARCH_START = "search_start";//动态代理药品listview的点击事件
    public static final String FUNNEL1_DRUESEARCHRESULTS_START = "drugsearchresults_start";//药品搜索结果条目_被点击

    //漏斗二 共两个事件
    public static final String FUNNEL2_CAMERA_CLICKED = "camera_clicked";//首页界面进入扫码界面
    public static final String FUNNEL2_SCAN_START = "scan_start";//手动输入条形码_被点击

    //漏斗三 共两个事件
    public static final String FUNNEL3_DISTANCE_START = "distance_start";//进入切换地址(药品详情界面的切换地址按钮)
    public static final String FUNNEL3_BAIDUMAP_CLICKED = "baidumap_clicked";//选择地址成功(确定按钮被点击)

    //漏斗一和漏斗二,公用的结束目标事件
    public static final String EVENT13_SCANRESULTS_START = "scanresults_start";//进入社区_医院模块
}