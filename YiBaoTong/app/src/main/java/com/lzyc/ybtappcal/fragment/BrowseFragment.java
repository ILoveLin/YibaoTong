package com.lzyc.ybtappcal.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseFragment;
import com.lzyc.ybtappcal.activity.mine.BrowseHistoryActivity;
import com.lzyc.ybtappcal.activity.payment.order.OrderbyActivity;
import com.lzyc.ybtappcal.activity.top.ResultsActivity;
import com.lzyc.ybtappcal.adapter.BrowseAdapter;
import com.lzyc.ybtappcal.bean.BrowseBean;
import com.lzyc.ybtappcal.bean.DrugBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.listener.OnYbtRefreshListener;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.TimeUtil;
import com.lzyc.ybtappcal.view.xlist.XYbtRefreshListView;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.ybtappcal.widget.popupwindow.PopupWindowTwoButton;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;

/**
 * 买药浏览
 * Created by yang on 2017/06/02.
 */
public class BrowseFragment extends BaseFragment implements BrowseAdapter.OnItemBrowseClickListener {
    private static final String TAG=BrowseFragment.class.getSimpleName();

    @BindView(R.id.root_layout)
    LinearLayout rootLayout;
    @BindView(R.id.listview)
    XYbtRefreshListView mListView;
    @BindString(R.string.empty_browse_history_list_goods)
    String emptyBrowseHistoryGoods;
    @BindString(R.string.empty_browse_history_list_drug)
    String emptyBrowseHistoryDurg;
    @BindString(R.string.edit)
    String edit;
    @BindString(R.string.complete)
    String complete;

    public static final int ACTION_EDIT_GOODS = 100;
    public static final int ACTION_COMPLETE_GOODS = 101;
    public static final int ACTION_EDIT_DRUG = 102;
    public static final int ACTION_COMPLETE_DRUG = 103;
    public int actionTypeGoods = ACTION_EDIT_GOODS;//买药编辑动作缓存
    public int actionTypeDrug = ACTION_EDIT_DRUG;//查药编辑动作缓存
    private BrowseAdapter mAdapter;
    private int tabValue = 0;//tab值
    private int pageIndex = 0;
    private boolean isInit = false;
    private List<BrowseBean> browseBeanList;

    public void setTabvalue(int tabValue){
        this.tabValue=tabValue;
    }
    @Override
    public int getContentViewId() {
        return R.layout.frag_browser_history;
    }

    @Override
    protected void init() {
        setPageStateView();
        isInit = true;
        rootLayout.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
        actionTypeGoods = ACTION_EDIT_GOODS;
        showLoading();
        browseBeanList = new ArrayList<>();
        mListView.setPullRefreshEnable(false);
        mListView.setPullLoadEnable(true);
        mAdapter = new BrowseAdapter(getActivity(), R.layout.item_frag_browse, browseBeanList);
        mAdapter.setOnItemBrowseClickListener(this);
        mListView.setAdapter(mAdapter);
        requestBrowseHistoryList();
        mListView.setOnRefreshListener(new OnYbtRefreshListener() {
            @Override
            public void onDownPullRefresh() {
                pageIndex = 0;
                requestBrowseHistoryList();
            }

            @Override
            public void onLoadingMore() {
                pageIndex++;
                requestBrowseHistoryList();
            }
        });
    }

    @Override
    public void onResume() {
        MobclickAgent.onPageStart(TAG+tabValue);
        super.onResume();
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd(TAG+tabValue);
        super.onPause();
    }

    /**
     * 点击了错误重试
     */
    protected void onClickRetry() {
        showLoading();
        isInit=false;
        pageIndex=0;
        requestBrowseHistoryList();
    }
    /**
     * 删除浏览记录
     */
    public void requestDeleteBrowse(int tabValue,String deleteType,String ids) {
        if (!NetworkUtil.CheckConnection(mContext)) {
            showToast("网络不给力");
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.HOME_BROWSE_DELETE_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.HOME_BROWSE_DELETE_SIGN);
        String uid = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "");
        params.put(HttpConstant.APP_UID, uid);
        params.put(HttpConstant.PARAM_KEY_TYPE2, "" + (tabValue + 1));
        params.put(HttpConstant.PARAM_KEY_DELTYPE,deleteType);
        if(deleteType.equals(Contants.STR_1)){
            params.put(HttpConstant.IDS,ids);
        }
        request(params, HttpConstant.MINE_BROWSE_DELETE);
    }

    /**
     * 获取记录浏览列表
     */
    public void requestBrowseHistoryList() {
        if (!NetworkUtil.CheckConnection(mContext)) {
            showError();
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.HOME_BROWSE_HISTORYLIST_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.HOME_BROWSE_HISTORYLIST_SIGN);
        String uid = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "");
        params.put(HttpConstant.APP_UID, uid);
        params.put(HttpConstant.PARAM_KEY_TYPE2, "" + (tabValue + 1));
        params.put(HttpConstant.PARAM_KEY_PAGEINDEX2, "" + pageIndex);
        request(params, HttpConstant.MINE_BROWSE_HISTORYLIST);
    }


    String oldTime;

    @Override
    public void done(String msg, int what, final JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.MINE_BROWSE_HISTORYLIST:
                mAdapter.setTabValue(tabValue);
                try {
                    showContent();
                    JSONObject data = response.getJSONObject(HttpConstant.DATA);
                    Type type = new TypeToken<ArrayList<BrowseBean>>() {
                    }.getType();
                    List<BrowseBean> listBrowse = JsonUtil.getListModle(data.toString(),HttpConstant.LIST2, type);
                    if (listBrowse.isEmpty()) {
                        if (isInit) {
                            isInit = false;
                            showEmpty(emptyBrowseHistoryGoods, R.mipmap.empty_browser_hisotry);
                            if(tabValue==0){
                                Intent intent=new Intent().setAction(Contants.ACTION_NAME_BROWSE);
                                intent.putExtra(Contants.KEY_COUNT,0);
                                intent.putExtra(Contants.KEY_TABVALUE,tabValue);
                                mContext.sendBroadcast(intent);
                            }
                        } else {
                            if(pageIndex==0){
                                if(tabValue==0){
                                    showEmpty(emptyBrowseHistoryGoods, R.mipmap.empty_browser_hisotry);
                                }else{
                                    showEmpty(emptyBrowseHistoryDurg, R.mipmap.empty_browser_hisotry);
                                }
                                sendBroadCast(0);
                            }else{
                                showToast("没有更多数据了");
                                pageIndex--;
                            }
                            mListView.onRefreshComplete();
                        }
                        return;
                    }
                    if(isInit){
                        isInit=false;
                        if(tabValue==0){
                            Intent intent=new Intent().setAction(Contants.ACTION_NAME_BROWSE);
                            intent.putExtra(Contants.KEY_COUNT,1);
                            intent.putExtra(Contants.KEY_TABVALUE,tabValue);
                            mContext.sendBroadcast(intent);
                        }
                    }
                    if (pageIndex == 0) {
                        oldTime = TimeUtil.getCurrentTime();
                    } else {
                        oldTime = mAdapter.getLastTime();
                    }
                    for (int i = 0; i < listBrowse.size(); i++) {
                        BrowseBean browse = listBrowse.get(i);
                        String lastTime = browse.getLastTime();
                        String newLastTime = lastTime.substring(0, 10);
                        if (oldTime.equals(newLastTime)) {
                            browse.setEquals(true);
                            if (pageIndex == 0 && i == 0) {
                                browse.setEquals(false);
                            }
                        } else {
                            browse.setEquals(false);
                        }
                        browse.setLastTime(newLastTime);
                        listBrowse.set(i, browse);
                        oldTime = newLastTime;
                    }
                    if (pageIndex == 0) {
                        mAdapter.setList(listBrowse);
                    } else {
                        mAdapter.addList(listBrowse);
                    }
                    mListView.onRefreshComplete();
                } catch (JSONException e) {
                    showEmpty(errorServer, R.mipmap.empty_error_net_server);
                } catch (Exception e) {
                    showEmpty(errorServer, R.mipmap.empty_error_net_server);
                }
                break;
            case HttpConstant.MINE_BROWSE_DELETE:
                isInit=false;
                pageIndex=0;
                requestBrowseHistoryList();
                break;
        }

    }

    @Override
    public void error(String errorMsg) {
        try {
            ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);
            String msg = errorResponse.getMsg();
            switch (errorResponse.getWhat()) {
                case HttpConstant.MINE_BROWSE_HISTORYLIST:
                    if (errorResponse.isNetError()) {
                        if (isInit) {
                            isInit = false;
                            showError();
                        } else {
                            showToast("网络不给力");
                        }
                    } else {
                        if (!TextUtils.isEmpty(msg)) {
                            showToast(msg);
                        }
                    }
                    break;
                default:
                    if (!TextUtils.isEmpty(msg)) {
                        showToast(msg);
                    }
                    break;
            }
        } catch (Exception e) {

        }
    }

    public void onRefresh(int tabValue) {

//        isInit = true;
        this.tabValue = tabValue;
        mAdapter.setTabValue(tabValue);
        if (!NetworkUtil.CheckConnection(mContext)) {
            showError();
            return;
        }
//        requestBrowseHistoryList();
        if (tabValue == 0) {
            if(mAdapter.getCount()==0){
                showEmpty(emptyBrowseHistoryGoods, R.mipmap.empty_browser_hisotry);
                sendBroadCast(0);
            }
        } else {
            if(mAdapter.getCount()==0) {
                showEmpty(emptyBrowseHistoryDurg, R.mipmap.empty_browser_hisotry);
                sendBroadCast(0);
            }
        }
    }

    private void sendBroadCast(int count) {
        Intent intent=new Intent().setAction(Contants.ACTION_NAME_BROWSE);
        intent.putExtra(Contants.KEY_COUNT,count);
        intent.putExtra(Contants.KEY_TABVALUE,tabValue);
        mContext.sendBroadcast(intent);
    }


    @Override
    public void onItemSelectedGoods(int position, BrowseBean browseBean) {
        mAdapter.setItemSelected(position, browseBean);
        int count = mAdapter.getCount();
        int countSelected = mAdapter.getSelectedGoodsCount();
        Intent intent=new Intent();
        intent.setAction(Contants.ACTION_NAME_BROWSE_SELECTED);
        if (count == countSelected) {
            intent.putExtra("isSelected",true);
        } else {
            intent.putExtra("isSelected",false);
        }
        mContext.sendBroadcast(intent);
    }

    @Override
    public void onItemSelectedDrug(int position, BrowseBean browseBean) {
        mAdapter.setItemSelected(position, browseBean);
        int count = mAdapter.getCount();
        int countSelected = mAdapter.getSelectedDrugCount();
        Intent intent=new Intent();
        intent.setAction(Contants.ACTION_NAME_BROWSE_SELECTED);
        if (count == countSelected) {
            intent.putExtra("isSelected",true);
        } else {
            intent.putExtra("isSelected",false);
        }
        mContext.sendBroadcast(intent);
    }

    @Override
    public void onItemBrowseGoodsClick(int position, BrowseBean browseBean) {
        Bundle mBundle = new Bundle();
        mBundle.putInt(Contants.KEY_PAGE_PAYMENT, Contants.VAL_PAGE_PAYMENT_CART);
        mBundle.putString(HttpConstant.SHOP_GOODS_PARAM_DKID, browseBean.getDKID());
        openActivity(OrderbyActivity.class, mBundle);
    }

    @Override
    public void onItemBrowseDrugClick(int position, BrowseBean browseBean) {
        DrugBean drugBean = new DrugBean();
        drugBean.setDrugID(browseBean.getDrugID());
        drugBean.setDrugNameID("");
        drugBean.setSpecificationsID("");
        drugBean.setVenderID("");
        Bundle bundle = new Bundle();
        bundle.putSerializable(Contants.KEY_OBJ_DRUGBEAN, drugBean);
        bundle.putInt(Contants.KEY_PAGE_RESULTS, Contants.VAL_PAGE_SEARCH_DRUGLIST);
        openActivity(ResultsActivity.class, bundle);
    }

    @Override
    public void onItemLongBrowseClick(int tabvalue,int position, BrowseBean browseBean) {
        showDeleteDialog(tabvalue,position, browseBean);
    }

    public void showDeleteDialog(final int tabValue, final int position, final BrowseBean browseBean) {
        final AlertDialog a = new AlertDialog.Builder(mContext).create();
        a.setCanceledOnTouchOutside(true);
        a.show();
        a.getWindow().setContentView(R.layout.dialog_message_delete);
        TextView dialog_message_delete = (TextView) a.getWindow().findViewById(R.id.dialog_message_delete);
        dialog_message_delete.setText("删除该药品");
        dialog_message_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestDeleteBrowse(tabValue,"1",browseBean.getID());
                if (a != null) {
                    a.dismiss();
                }
            }
        });
    }


    public void onEditAction(int tabValue,int actionType) {
        mAdapter.setTabValue(tabValue);
        if (actionType == BrowseHistoryActivity.ACTION_DRUG_COMPLETE) {
            mAdapter.setStatusDrug(true);
        } else if (actionType == BrowseHistoryActivity.ACTION_DRUG_EDIT) {
            mAdapter.setStatusDrug(false);
        } else if (actionType == BrowseHistoryActivity.ACTION_GOODS_EDIT) {
            mAdapter.setStatusGoods(false);
        } else if (actionType == BrowseHistoryActivity.ACTION_GOODS_COMPLETE) {
            mAdapter.setStatusGoods(true);
        }
    }

    /**
     * 全选
     * @param tabValue
     * @param actionType
     * @param isSelected
     */
    public void onSelectedAction(int tabValue,int actionType,boolean isSelected){
        this.tabValue=tabValue;
        mAdapter.setTabValue(tabValue);
        if (actionType == BrowseHistoryActivity.ACTION_DRUG_COMPLETE) {
            mAdapter.setSelecteAllDrug(isSelected);
        }else if (actionType == BrowseHistoryActivity.ACTION_GOODS_COMPLETE) {
            mAdapter.setSelecteAllGoods(isSelected);
        }
    }

    /**
     * 清空
     * @param tabValue
     */
    public void onDeleteAction(int tabValue){
        popWindowDelete(tabValue);
    }

    /**
     *删除部分选中
     * @param tabValue
     */
    public void onDeleteActionSome(int tabValue){
        int count=0;
        if(tabValue==0){
            count= mAdapter.getSelectedGoodsCount();
        }else{
            count= mAdapter.getSelectedDrugCount();
        }
        if(count==0){
            showToast("请选择要删除的浏览记录");
        }else{
            List<BrowseBean> list= mAdapter.getListSelected();
            StringBuffer stringBuffer=new StringBuffer();
            for (int i = 0; i <list.size() ; i++) {
              BrowseBean browseBean=list.get(i);
                if(list.size()==0){
                    stringBuffer.append(browseBean.getID());
                }else{
                    if(i==list.size()-1){
                        stringBuffer.append(browseBean.getID());
                    }else{
                        stringBuffer.append(browseBean.getID()+",");
                    }
                }
            }
            popWindowDeleteGoodsSome(tabValue,stringBuffer.toString());
        }
    }


    /**
     * 是否删除部分浏览记录的弹窗
     */
    private void popWindowDeleteGoodsSome(final int tabvalue, final String str) {
        final PopupWindowTwoButton popupWindow = new PopupWindowTwoButton(getActivity());
        popupWindow.getTvTitle().setVisibility(View.GONE);
        popupWindow.getTv_content().setText("确认删除选中浏览记录？");
        popupWindow.getTvOk().setText("确认");
        popupWindow.getTvCancel().setText("取消");
        popupWindow.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDeleteBrowse(tabvalue,"1",str);
                popupWindow.dismiss();
            }
        });
        popupWindow.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showPopupWindow(mListView, Gravity.CENTER);
    }

    /**
     *清空
     */
    private void popWindowDelete(final int tabValue) {
        final PopupWindowTwoButton popupWindow = new PopupWindowTwoButton(getActivity());
        popupWindow.getTvTitle().setVisibility(View.GONE);
        final String desc;
        if(tabValue==0){
            desc="买药";
        }else{
            desc="查药";
        }
        popupWindow.getTv_content().setText("确认清空"+desc+"浏览记录？");
        popupWindow.getTvOk().setText("确认");
        popupWindow.getTvCancel().setText("取消");
        popupWindow.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDeleteBrowse(tabValue,"2","");
                popupWindow.dismiss();
            }
        });
        popupWindow.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showPopupWindow(mListView, Gravity.CENTER);
    }

}
