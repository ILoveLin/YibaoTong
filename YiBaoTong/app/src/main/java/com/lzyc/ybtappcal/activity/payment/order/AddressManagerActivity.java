package com.lzyc.ybtappcal.activity.payment.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.AddressManagerAdapter;
import com.lzyc.ybtappcal.bean.AddressInfo;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.view.pagestate.PageManager;
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
import butterknife.OnClick;

/**
 * 地址管理
 * Created by yang on 2017/03/15.
 */
public class AddressManagerActivity extends BaseActivity implements AddressManagerAdapter.OnChildClickListener,PageManager.OnClickListener {
    private static final String TAG=AddressManagerActivity.class.getSimpleName();
    public static final int ACTION_NONE =0;//没有动作
    public static final int ACTION_CHOOSE=1;//选择动作
//    public static final int ACTION_SET_DEFAULT=2;//设置默认地址动作
    public static final int ACTION_DELETE =2;//删除上个界面地址动作
    private int flagAction;
    @BindView(R.id.address_manager_lv)
    XYbtRefreshListView addressManagerLv;
    @BindString(R.string.address_manager_title)
    public String titleName;
    @BindString(R.string.address_manager_title1)
    public String titleName1;

    private AddressManagerAdapter mAdapter;
    private List<AddressInfo> listAddressInfo;
    private int typePage;
    private boolean isXX = false;//是否设置默认
    private boolean isEmpty=false;
    private Intent intent;
    private String deleteAddressId;
    private String fromAddressID;

    @Override
    public int getContentViewId() {
        return R.layout.activity_address_manager;
    }

    @Override
    protected void init() {
        flagAction= ACTION_NONE;//默认，认为没有动作
        intent=getIntent();
        typePage = intent.getIntExtra(Contants.KEY_PAGE_ADDRESS_MANAGER, Contants.VAL_PAGE);
        if(typePage==Contants.VAL_PAGE_PAYMENT_CHECK){
            setTitleName(titleName1);
            fromAddressID=intent.getStringExtra(Contants.KEY_ADDRESS_ID);
        }else{
            setTitleName(titleName);
        }
        setPageStateView();
        mPageManager.setOnClickListener(this);
        showLoading();
        listAddressInfo=new ArrayList<>();
        mAdapter = new AddressManagerAdapter(this, R.layout.item_address_manager, listAddressInfo);
        mAdapter.setOnChildClickListener(this);
        addressManagerLv.setPullLoadEnable(false);
        addressManagerLv.setPullRefreshEnable(false);
        addressManagerLv.setAdapter(mAdapter);
    }

    @Override
    protected void onClickRetry() {
        requestAddressInfo();
    }

    @OnClick({R.id.ib_left, R.id.address_manager_add})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.ib_left:
                closeActivity();
                break;
            case R.id.address_manager_add:
                switchAddressBuildActivity();
                break;
        }
    }

    private void switchAddressBuildActivity() {
        Bundle bundle=new Bundle();
        bundle.putInt("size",listAddressInfo.size());
        bundle.putInt(Contants.KEY_PAGE,Contants.VAL_PAGE_ADDRESSMANAGER);
        openActivity(AddressBuildActivity.class,bundle);
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        listAddressInfo = new ArrayList<>();
        requestAddressInfo();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

    /**
     * 选择地址的处理
     * @param addressInfo
     */
    private void closeActivity(AddressInfo addressInfo) {
        intent.putExtra(Contants.KEY_ADDRESS_INFO, addressInfo);
        closeActivity();
    }

    /**
     * 关闭activity
     */
    private void closeActivity() {
        if(typePage==Contants.VAL_PAGE_PAYMENT_CHECK){//如果是检查订单过来的
            /*
            * 1.先判断是否有数据，把这个事件传过去
            * 2.是否有动作，把动作传过去处理，如果是手动选择把数据传过去
            * */
            if(isEmpty){
               intent.putExtra(Contants.KEY_ADDRESS_EMPTY,Contants.INT_0);
            }else{
                intent.putExtra(Contants.KEY_ADDRESS_EMPTY,Contants.INT_1);
                intent.putExtra(Contants.KEY_ADDRESS_ACTION, flagAction);
            }
            setResult(RESULT_OK,intent);
        }
        this.finish();
    }

    @Override
    public void onEditClickListener(AddressInfo addressInfo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Contants.KEY_OBJ_ADDRESS_INFO, addressInfo);
        openActivity(AddressEditActivity.class, bundle);
    }

    @Override
    public void onClearClickListener(String id) {
        popupClearAddressInfo(id);
    }


    @Override
    public void onSetDefaultListener(int position, AddressInfo addressInfo) {
//        flagAction=ACTION_SET_DEFAULT;
        mAdapter.updateItem(position);//刷新视图
        requestAddressInfoSetDefault(addressInfo.getID(), addressInfo.getDefault());
    }

    @Override
    public void onSetResultListener(AddressInfo addressInfo) {
        if(typePage==Contants.VAL_PAGE_PAYMENT_CHECK){
            flagAction=ACTION_CHOOSE;
            closeActivity(addressInfo);
        }
    }

    //是否清除历史记录
    private void popupClearAddressInfo(final String id) {
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(this);
        twoButton.getTvOk().setVisibility(View.VISIBLE);
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTv_content().setText("确认删除该地址?");
        twoButton.getTvCancel().setText("取消");
        twoButton.getTvOk().setText("确认");
        twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAddressInfoClear(id);
                twoButton.dismiss();
            }
        });
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(addressManagerLv, Gravity.CENTER);
    }

//  网络请求

    /**
     * 获取地址列表
     */
    private void requestAddressInfo() {
        String uid = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.UID, "");
        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_ADDRESS_LIST_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_ADDRESS_LIST_SIGN);
        params.put(HttpConstant.PARAM_KEY_UID, uid);
        request(params, HttpConstant.ORDER_ADDRESS_MANAGER);
    }

    /**
     * 删除指定id的地址信息
     *
     * @param id
     */
    private void requestAddressInfoClear(String id) {
        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP,HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_ADDRESS_DEL_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN,  HttpConstant.SHOP_ADDRESS_DEL_SIGN);
        params.put(HttpConstant.PARAM_KEY_ID, id);
        deleteAddressId=id;
        request(params, HttpConstant.ORDER_ADDRESS_CLEAR);
    }

    /**
     * 设置指定id的默认地址
     *
     * @param id
     */
    private void requestAddressInfoSetDefault(String id, String setDefault) {
        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP,HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_ADDRESS_DEFAULT_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_ADDRESS_DEFAULT_SIGN);
        params.put(HttpConstant.PARAM_KEY_ID, id);
        params.put(HttpConstant.PARAM_KEY_DEFAULT, setDefault);
        request(params, HttpConstant.ORDER_ADDRESS_SETDEFAULT);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.ORDER_ADDRESS_MANAGER:
                try {
                    showContent();
                    Type type = new TypeToken<ArrayList<AddressInfo>>() {
                    }.getType();
                    JSONObject joAddressList = response.getJSONObject("data");
                    listAddressInfo = JsonUtil.getListModle(joAddressList.toString(),"addressList", type);
                    if (listAddressInfo.isEmpty()) {
                        isEmpty=true;
                        showEmptyBottom("请添加详细地址哦!",R.mipmap.empty_address_manager,View.VISIBLE);
                        return;
                    }
                    mAdapter.setListData(listAddressInfo);
                    List<String> listDefault=new ArrayList<>();
                    List<AddressInfo> list=mAdapter.getAddressInfo();
                    if(list.size()>0){
                        for (int i = 0; i <list.size() ; i++) {
                            AddressInfo adddressInfo=mAdapter.getAddressInfo().get(i);
                            listDefault.add(adddressInfo.getDefault());
                        }
                    }
                    if(!listDefault.contains("1")){
                        isXX=true;
                        requestAddressInfoSetDefault(list.get(0).getID(),"1");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showEmpty(errorServer,R.mipmap.empty_error_net_server);
                }
                break;
            case HttpConstant.ORDER_ADDRESS_CLEAR:
               if(typePage==Contants.VAL_PAGE_PAYMENT_CHECK){
                   if(fromAddressID.equals(deleteAddressId)){
                       flagAction= ACTION_DELETE;
                       intent.putExtra(Contants.KEY_ADDRESS_DELETE_ID, deleteAddressId);
                   }
               }
                requestAddressInfo();
                break;
            case HttpConstant.ORDER_ADDRESS_SETDEFAULT:
                if(isXX){
                    requestAddressInfo();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        closeActivity();
        super.onBackPressed();
    }

    @Override
    public void error(String errorMsg) {
        ErrorResponse errorResponse=JsonUtil.getModle(errorMsg,ErrorResponse.class);
        switch (errorResponse.getWhat()){
            case HttpConstant.ORDER_ADDRESS_MANAGER:
                if(errorResponse.isNetError()){
                    showError();
                }else{
                    showEmpty(errorServer,R.mipmap.empty_error_net_server);
                }
                break;
            case HttpConstant.ORDER_ADDRESS_CLEAR:
                showToast("删除失败 ");
                break;
            default:
                super.error(errorMsg);
                break;
        }
    }

    @Override
    public void onBtnClickListener() {
        switchAddressBuildActivity();
    }



}
