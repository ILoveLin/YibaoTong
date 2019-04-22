package com.lzyc.ybtappcal.activity.top;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.HospitalPositionPagerAdapter;
import com.lzyc.ybtappcal.bean.HospitalBean;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.view.StarBar;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 医院位置
 * 附近的医院，社区
 * Created by yang on 2016/11/07.
 */
public class HospitalNearbyActivity extends BaseActivity {
    private static final String TAG = HospitalNearbyActivity.class.getSimpleName();
    @BindView(R.id.map_view)
    MapView mMapView;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.rel_bottom)
    RelativeLayout hnearRelayBottom;
    @BindView(R.id.viewpager_out)
    View viewpagerOut;
    @BindString(R.string.title_hispital_location)
    String titleName;
    private String lat;
    private String lng;
    private HospitalPositionPagerAdapter mAdapter;
    private List<View> mDataViews;
    private BaiduMap mBaiduMap;
    private BitmapDescriptor pointShequNor, pointShequPre, pointYiyuanNor, pointYiyuanPre, pointDingwei;
    private ArrayList<HospitalBean> listData;
    private LatLng latLng = null;
    private LatLng latLngMine;
    private LatLng latLngTarget;
    private HospitalBean hBean;
    private String address;
    private String addressTarget;
    private int page = -1;//分页
    private List<HospitalBean> listItem;
    private LatLng minLatLng, maxLatlng;
    private MarkerOptions dingweiOptions;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           fillData();
            showContent();
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.activity_hospital_nearby;
    }

    @Override
    public void init() {
        setTitleName(titleName);
        setPageStateView();
        showLoading();
        Bundle bundle = getIntent().getExtras();
        listData = (ArrayList<HospitalBean>) bundle.get("list");
        lat = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.LATITUDE, "");
        lng = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.LONGITUDE, "");
        address = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.ADDRESS, "");
        latLngMine = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        pointShequNor = BitmapDescriptorFactory.fromResource(R.mipmap.icon_hp_shequ_nor);
        pointShequPre = BitmapDescriptorFactory.fromResource(R.mipmap.icon_hp_shequ_pre);
        pointYiyuanNor = BitmapDescriptorFactory.fromResource(R.mipmap.icon_hp_yiyuan_nor);
        pointYiyuanPre = BitmapDescriptorFactory.fromResource(R.mipmap.icon_hp_yiyuan_pre);
        pointDingwei = BitmapDescriptorFactory.fromResource(R.mipmap.icon_dingwei);
        mMapView.showZoomControls(false);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mDataViews = new ArrayList<>();
        page = 0;
        listItem = new ArrayList<>();
        dingweiOptions = new MarkerOptions().position(latLngMine).icon(pointDingwei).perspective(true).draggable(false).zIndex(2).anchor(0.5f, 1f);
        if (listData.size() == 0) {//定位
            mBaiduMap.addOverlay(dingweiOptions);
            hnearRelayBottom.setVisibility(View.GONE);
            viewLine.setVisibility(View.GONE);
        } else {//显示列表，地图描点
            hnearRelayBottom.setVisibility(View.VISIBLE);
            viewLine.setVisibility(View.VISIBLE);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(0);
                }
            },500);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {
                viewpagerOut.setVisibility(View.VISIBLE);

            }

            @Override
            public void onPageSelected(int position) {
                if (page != position / 10) {
                    page = position / 10;
                    listItem = getItemList(page);
                    boundMap();
                }
                setMarks(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void fillData() {
        for (int i = 0; i < listData.size(); i++) {
            HospitalBean hBean = listData.get(i);
            View vpItemView = View.inflate(HospitalNearbyActivity.this, R.layout.item_pager_hp, null);
            final LinearLayout item_hp_pager_ = (LinearLayout) vpItemView.findViewById(R.id.item_hp_pager_);
            TextView tvName = (TextView) vpItemView.findViewById(R.id.item_hp_pager_hpname);
            if (listData.size() > 1) {
                tvName.setText((i + 1) + "." + hBean.getName());
            } else {
                tvName.setText("" + hBean.getName());
            }
            TextView tvName2 = (TextView) vpItemView.findViewById(R.id.item_hp_pager_hpname2);
            StarBar item_hp_pager_star2 = (StarBar) vpItemView.findViewById(R.id.item_hp_pager_star2);
            if (listData.size() > 1 && i <= listData.size() - 2) {
                HospitalBean hBean2 = listData.get(i + 1);
                tvName2.setText((i + 2) + "." + hBean2.getName());
                item_hp_pager_star2.setStarMark(Float.parseFloat(hBean2.getAverage()));
            }
            StarBar item_hp_pager_star = (StarBar) vpItemView.findViewById(R.id.item_hp_pager_star);
            item_hp_pager_star.setStarMark(Float.parseFloat(hBean.getAverage()));
            TextView item_hp_pager_level = (TextView) vpItemView.findViewById(R.id.item_hp_pager_level);
            TextView item_hp_pager_level2 = (TextView) vpItemView.findViewById(R.id.item_hp_pager_level2);

            if ("一级".equals(hBean.getLevel())) {
                item_hp_pager_level.setBackgroundResource(R.mipmap.icon_nav_yiji);
            } else if ("二级".equals(hBean.getLevel())) {
                item_hp_pager_level.setBackgroundResource(R.mipmap.icon_nav_erji);
            } else if ("三级".equals(hBean.getLevel())) {
                item_hp_pager_level.setBackgroundResource(R.mipmap.icon_nav_sanji);
            } else {
                item_hp_pager_level.setBackgroundResource(R.mipmap.icon_nav_wu);
            }

            try {
                HospitalBean bean = listData.get(i + 1);

                if ("一级".equals(bean.getLevel())) {
                    item_hp_pager_level2.setBackgroundResource(R.mipmap.icon_nav_yiji);
                } else if ("二级".equals(bean.getLevel())) {
                    item_hp_pager_level2.setBackgroundResource(R.mipmap.icon_nav_erji);
                } else if ("三级".equals(bean.getLevel())) {
                    item_hp_pager_level2.setBackgroundResource(R.mipmap.icon_nav_sanji);
                } else {
                    item_hp_pager_level2.setBackgroundResource(R.mipmap.icon_nav_wu);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            TextView item_hp_pager_yytype = (TextView) vpItemView.findViewById(R.id.item_hp_pager_yytype);
            item_hp_pager_yytype.setText(hBean.getLevel() + hBean.getJibie());
            final TextView item_hp_page_juli = (TextView) vpItemView.findViewById(R.id.item_hp_page_juli);
            String d = hBean.getDistance();
            if (!TextUtils.isEmpty(d)) {
                double distance = Double.parseDouble("" + d);
                double dis = (distance / 1000);
                item_hp_page_juli.setText(String.format("%.1f", dis).toString() + "km");
            }
            final LinearLayout item_pager_linear = (LinearLayout) vpItemView.findViewById(R.id.item_pager_linear);
            final int position = i;
            item_pager_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listData.size() > 1) {
                        TranslateAnimation anim = new TranslateAnimation(-100, 0, 0, 0);
                        anim.setInterpolator(new BounceInterpolator());
                        anim.setDuration(800);
                        item_pager_linear.startAnimation(anim);
                        anim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                if (position <= listData.size() - 2) {
                                    item_hp_pager_.setVisibility(View.VISIBLE);
                                }
                                viewpagerOut.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                item_hp_pager_.setVisibility(View.GONE);
                                viewpagerOut.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                    }
                }
            });
            mDataViews.add(vpItemView);
            mBaiduMap.addOverlay(dingweiOptions);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        setMarks(0);//添加地图覆盖物
                        boundMap();//设置地图覆盖物范围
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        listItem = getItemList(page);
        mAdapter = new HospitalPositionPagerAdapter(mDataViews);
        mViewPager.setAdapter(mAdapter);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng mLatLngMarker = marker.getPosition();
                if (mLatLngMarker.latitude == latLngMine.latitude && mLatLngMarker.longitude == mLatLngMarker.longitude) {
                    showToast("当前的定位地址是：" + address);
                    return true;
                }
                requestEventCode("i001");
                int position = marker.getExtraInfo().getInt("position");
                setMarks(position);
                mViewPager.setCurrentItem(position);
                return false;
            }
        });
    }

    private void boundMap() {
        if (listItem.size() == 0) {
            return;
        }
        Double[] latArray = new Double[listItem.size()];
        Double[] lngArray = new Double[listItem.size()];
        for (int i = 0; i < listItem.size(); i++) {
            HospitalBean hBean = listItem.get(i);
            latArray[i] = Double.parseDouble(hBean.getLat());
            lngArray[i] = Double.parseDouble(hBean.getLng());
        }
        Arrays.sort(latArray);
        Arrays.sort(lngArray);
        minLatLng = new LatLng(latArray[0], lngArray[0]);
        maxLatlng = new LatLng(latArray[listItem.size() - 1], lngArray[listItem.size() - 1]);
        LatLngBounds bounds = new LatLngBounds.Builder().include(minLatLng).include(maxLatlng).build();
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(bounds);
        mBaiduMap.setMapStatus(u);
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    showContent();
//                    Thread.sleep(1500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//        if (listItem.size() > 1 && listItem.size() < 10) {
//            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(bounds);
//            mBaiduMap.setMapStatus(u);
////            MapStatus build = new MapStatus.Builder().zoom(9).build();
////            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(build));
//        } else {
//            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(bounds);
//            mBaiduMap.setMapStatus(u);
//        }
    }

    private List<HospitalBean> getItemList(int page) {
        listItem.clear();
        for (int i = page * 10; i < (page + 1) * 10; i++) {
            HospitalBean hBean = listData.get(i);
            listItem.add(hBean);
            if (i == listData.size() - 1) {
                break;
            }
        }
        return listItem;
    }

    /**
     * 设置覆盖物
     *
     * @param position
     */
    private void setMarks(int position) {
        mBaiduMap.clear();
        for (int i = 0; i < listItem.size(); i++) {
            HospitalBean hBean = listItem.get(i);
            final View ponitView = View.inflate(this, R.layout.item_mapview_ponit, null);
            final TextView textView = (TextView) ponitView.findViewById(R.id.item_mapview_ponit);
            if (listItem.size() > 1) {
                textView.setText("" + ((i + 1) + 10 * page));
                if (position == (i + page * 10)) {
                    textView.setBackgroundResource(R.mipmap.icon_hp_point_pre);
                } else {
                    textView.setBackgroundResource(R.mipmap.icon_hp_point_nor);
                }
            } else {
                textView.setText("");
                textView.setBackgroundResource(R.mipmap.icon_hp_point_nor);
            }
            MarkerOptions pointViewMarkerOptions2;
            BitmapDescriptor pointDescriptor = BitmapDescriptorFactory.fromView(ponitView);
            latLng = new LatLng(Double.parseDouble(hBean.getLat()), Double.parseDouble(hBean.getLng()));
            MarkerOptions pointMarkerOptions = new MarkerOptions().position(latLng).icon(pointDescriptor).perspective(true).draggable(false).zIndex(2).anchor(0.5f, 1f);
            Marker marker = (Marker) mBaiduMap.addOverlay(pointMarkerOptions);
            Bundle bundle = new Bundle();
            bundle.putInt("position", (i + page * 10));
            marker.setExtraInfo(bundle);
            if (hBean.getTypeAddress() == 0) {
                if (position == (i + page * 10)) {
                    pointViewMarkerOptions2 = new MarkerOptions().position(latLng).icon(pointYiyuanPre).perspective(false).draggable(false).zIndex(0).anchor(0.5f, 0.5f);
                } else {
                    pointViewMarkerOptions2 = new MarkerOptions().position(latLng).icon(pointYiyuanNor).perspective(false).draggable(false).zIndex(0).anchor(0.5f, 0.5f);
                }
            } else {
                if (position == (i + page * 10)) {
                    pointViewMarkerOptions2 = new MarkerOptions().position(latLng).icon(pointShequPre).perspective(false).draggable(false).zIndex(0).anchor(0.5f, 0.5f);
                } else {
                    pointViewMarkerOptions2 = new MarkerOptions().position(latLng).icon(pointShequNor).perspective(false).draggable(false).zIndex(0).anchor(0.5f, 0.5f);
                }
            }
            mBaiduMap.addOverlay(pointViewMarkerOptions2);
        }
        mBaiduMap.addOverlay(dingweiOptions);
    }


    /**
     * 启动百度地图导航(Native)
     */
    public void startNavi() {
        requestEventCode("i002");
        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(latLngMine).endPoint(latLngTarget)
                .startName(address).endName(addressTarget);
        try {
            BaiduMapNavigation.openBaiduMapNavi(para, this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showDialog();
        }

    }

    /**
     * 提示未安装百度地图app或app版本过低
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(HospitalNearbyActivity.this);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @OnClick({R.id.ib_left, R.id.tv_daohang})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.ib_left:
                finish();
                break;
            case R.id.tv_daohang:
                if (hBean == null) {
                    hBean = listData.get(0);
                }
                latLngTarget = new LatLng(Double.parseDouble(hBean.getLat()), Double.parseDouble(hBean.getLng()));
                addressTarget = hBean.getName();
                startNavi();
                break;

        }

    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
}
