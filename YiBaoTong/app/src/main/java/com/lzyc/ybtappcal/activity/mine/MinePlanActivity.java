package com.lzyc.ybtappcal.activity.mine;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.google.gson.reflect.TypeToken;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.account.LoginActivity;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.activity.top.PlanDetailActivity;
import com.lzyc.ybtappcal.adapter.LocalAdapter;
import com.lzyc.ybtappcal.adapter.OtherPlaceAdapter;
import com.lzyc.ybtappcal.bean.AreaBean;
import com.lzyc.ybtappcal.bean.DrugBean;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.listener.OnAddressClickListener;
import com.lzyc.ybtappcal.listener.OnDeletePlanClickListener;
import com.lzyc.ybtappcal.listener.OnYbtRefreshListener;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.MD5ChangeUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.lzyc.ybtappcal.view.xlist.XYbtRefreshListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的方案
 *@author yang
 */
public class MinePlanActivity extends BaseActivity implements OnAddressClickListener, OnDeletePlanClickListener {
    private static final String TAG=MinePlanActivity.class.getSimpleName();
    @BindView(R.id.smlv_my_plan)
    XYbtRefreshListView smlv_my_plan;
    @BindView(R.id.empty_mine_plan)
    RelativeLayout empty_mine_plan;
    @BindString(R.string.title_plan)
    String titleName;

    private String phone;
    private int localPage;
    private int cLocalPage = -1;
    private int otherPage;
    private int cOtherPage = -1;
    private LocalAdapter localAdapter;
    private ArrayList<DrugBean> localList;
    private OtherPlaceAdapter otherPlaceAdapter;
    private ArrayList<AreaBean> otherList;
    private SwipeMenuCreator creator;
    private int cPosition;
    private DrugBean drugBean;
    private int tabType = -1;

    @Override
    public int getContentViewId() {
        return R.layout.activity_mine_plan;
    }

    @Override
    public void init() {
        setTitleName(titleName);
        setPageStateView(smlv_my_plan);
        showLoading();
        Boolean isLogin = (Boolean) SharePreferenceUtil.get(this, SharePreferenceUtil.IS_LOGIN, false);
        if (isLogin) {
            phone = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PHONE, "");
        } else {
            openActivity(LoginActivity.class);
        }
        empty_mine_plan.setVisibility(View.GONE);
        initLv();
        tabType = 0;
        setTabType(tabType);
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

    public void initLv() {
        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu swipeMenu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xE4, 0x21, 0x21)));
                deleteItem.setWidth(DensityUtils.dp2px(90));
                swipeMenu.addMenuItem(deleteItem);
            }
        };
        localList = new ArrayList<DrugBean>();
        localAdapter = new LocalAdapter(this,  R.layout.item_drugs_list,localList);
        otherList = new ArrayList<AreaBean>();
        otherPlaceAdapter = new OtherPlaceAdapter(this, R.layout.item_other_place,otherList);
        otherPlaceAdapter.setListener(this);
        otherPlaceAdapter.setDeletePlanClickListener(this);
        smlv_my_plan.setOnRefreshListener(new OnYbtRefreshListener() {
            @Override
            public void onDownPullRefresh() {
                switch (tabType) {
                    case 0:
                        requestPlan();
                        break;
                    case 1:
                        requestArea();
                        break;
                }
            }

            @Override
            public void onLoadingMore() {
                switch (tabType) {
                    case 0:
                        localPage++;
                        requestPlan();
                        break;
                    case 1:
                        otherPage++;
                        requestArea();
                        break;
                }
            }
        });
    }

    @OnClick({R.id.rb_scan_left, R.id.rb_scan_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_scan_left:  //本地
                tabType = 0;
                setTabType(tabType);
                break;
            case R.id.rb_scan_right: //异地
                tabType = 1;
                setTabType(tabType);
                break;
        }
    }

    private void setTabType(int tabType) {
        switch (tabType) {
            case 0:
                smlv_my_plan.setDividerHeight(0);
                smlv_my_plan.setAdapter(localAdapter);
                smlv_my_plan.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                        drugBean = (DrugBean) adapterView.getItemAtPosition(i);
                        if (drugBean != null) {
                            showDeleteDialog(drugBean.getId());
                        }
                        return true;
                    }
                });
                smlv_my_plan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        DrugBean drugBean = (DrugBean) parent.getItemAtPosition(position);
                        if (drugBean != null) {
                            Intent intent = new Intent(MinePlanActivity.this, PlanDetailActivity.class);
                            intent.putExtra("plan", drugBean);
                            startActivity(intent);
                        }
                    }
                });
                showEmptyView();
                break;
            //异地
            case 1:
                smlv_my_plan.setAdapter(otherPlaceAdapter);
                smlv_my_plan.setDividerHeight(10);
                showEmptyView();
                break;
            default:
                break;
        }
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.MY_PLAN_LIST:
                Type planType = new TypeToken<ArrayList<DrugBean>>() {
                }.getType();
                List<DrugBean> planList = JsonUtil.getListModle(response.toString(), "data", planType);
                reloadLocal(planList);

                break;
            case HttpConstant.MY_AREA_LIST:    //请求地区列表
                Type areaType = new TypeToken<ArrayList<AreaBean>>() {
                }.getType();
                List<AreaBean> areaList = JsonUtil.getListModle(response.toString(), "data", areaType);
                reloadOther(areaList);
                showContent();
                break;
            case HttpConstant.MY_AREA_PLAN_LIST:   //异地城市被点击了
                Type aPlanType = new TypeToken<ArrayList<DrugBean>>() {
                }.getType();
                List<DrugBean> pList = JsonUtil.getListModle(response.toString(), "data", aPlanType);
                otherPlaceAdapter.setpList(pList);
                otherPlaceAdapter.notifyDataSetChanged();
                break;
            case HttpConstant.MY_DELETE_PLAN:
                ToastUtil.showToastCenter(MinePlanActivity.this, "删除成功！");
                if (tabType == 0) {
                    // requestPlan();
                    //移出被删除的数据,在刷新
                    localList.remove(drugBean);
                    localAdapter.notifyDataSetChanged();
                } else {
                    otherPlaceAdapter.notifyDataSetChanged();
                }
                showEmptyView();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        if (tabType == -1) {
            tabType = 0;
        }

        requestPlan();
        requestArea();
    }

    /**
     * 异地城市的回调接口
     *
     * @param position
     */
    @Override
    public void onAddressClick(int position) {
        cPosition = position;
        smlv_my_plan.setSelection(cPosition);
        //请求当前被点击城市的数据 otherPlaceAdapter
        requestAreaList(otherList.get(cPosition).getCity());
    }

    /**
     * 异地方案删除
     *
     * @param position
     * @param drugBean
     */
    @Override
    public void onDeletePlanClick(int position, DrugBean drugBean) {
        String id = drugBean.getId();
        showDeleteDialog(id, position, drugBean);

    }

    /**
     * 请求本地方案
     */
    public void requestPlan() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("app", "Home");
        params.put("class", "MyPlan2");
        String sign = MD5ChangeUtil.Md5_32("HomeMyPlan2" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        params.put("phone", phone);
        String lat = (String) SharePreferenceUtil.get(this, "positionlat", "");
        String lng = (String) SharePreferenceUtil.get(this, "positionlng", "");
        params.put("lng", lng);
        params.put("lat", lat);
        params.put("pageIndex", localPage + "");
        empty_mine_plan.setVisibility(View.GONE);
        smlv_my_plan.setVisibility(View.GONE);
        request(params, HttpConstant.MY_PLAN_LIST);

    }


    /**
     * 请求地区列表
     */
    public void requestArea() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("app", "Home");
        params.put("class", "MyPlanYi");
        String sign = MD5ChangeUtil.Md5_32("HomeMyPlanYi" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        params.put("phone", phone);
        params.put("pageIndex", otherPage + "");
        request(params, HttpConstant.MY_AREA_LIST);

    }

    /**
     * 异地方案列表
     * yidi : 0 本地 ，1 异地
     */
    public void requestAreaList(String city) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("app", "Home");
        params.put("class", "MyPlanYiList");
        String sign = MD5ChangeUtil.Md5_32("HomeMyPlanYiList" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        params.put("phone", phone);
        params.put("Yidi", "1");
        params.put("city", city);
        params.put("pageIndex", "0");
        request(params, HttpConstant.MY_AREA_PLAN_LIST);

    }

    /**
     * 删除方案
     */
    public void requestDeletePlan(String id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("app", "Home");
        params.put("class", "DelPlan");
        String sign = MD5ChangeUtil.Md5_32("HomeDelPlan" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        params.put("phone", phone);
        params.put("id", id);
        request(params, HttpConstant.MY_DELETE_PLAN);

    }


    private void reloadLocal(List<DrugBean> list) {
        if (localPage > cLocalPage) {
            cLocalPage = localPage;
            if (list.isEmpty()) {
                if (otherPage != 0) {
                    ToastUtil.showToastCenter(MinePlanActivity.this, "没有更多数据了");
                }
            } else {
                localList.addAll(list);
                localAdapter.notifyDataSetChanged();
            }
        }
        smlv_my_plan.onRefreshComplete();
        showEmptyView();
    }


    private void reloadOther(List<AreaBean> list) {
        if (otherPage > cOtherPage) {
            cOtherPage = otherPage;
            otherList.addAll(list);
            otherPlaceAdapter.notifyDataSetChanged();

        }
        smlv_my_plan.onRefreshComplete();
        showEmptyView();
    }


    public void showEmptyView() {
        int count = -1;
        if (tabType == 0) {
            count = localAdapter.getCount();
        } else {
            count = otherPlaceAdapter.getCount();
        }
        if (count == 0) {
            empty_mine_plan.setVisibility(View.VISIBLE);
            smlv_my_plan.setVisibility(View.GONE);
        } else {
            empty_mine_plan.setVisibility(View.GONE);
            smlv_my_plan.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 删除本地
     *
     * @param id
     */
    public void showDeleteDialog(final String id) {
        showDeleteDialog(id, 0, null);
    }

    public void showDeleteDialog(final String id, final int position, final DrugBean drugBean)  {
        final AlertDialog a = new AlertDialog.Builder(this).create();
        a.setCanceledOnTouchOutside(true);
        a.show();
        a.getWindow().setContentView(R.layout.dialog_message_delete);
        TextView dialog_message_delete = (TextView) a.getWindow().findViewById(R.id.dialog_message_delete);
        dialog_message_delete.setText("删除该方案");
        dialog_message_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabType == 1) {
                    otherPlaceAdapter.removeItem(position, drugBean);
                }
                requestDeletePlan(id);
                if (a != null) {
                    a.dismiss();
                }
            }
        });
    }


}