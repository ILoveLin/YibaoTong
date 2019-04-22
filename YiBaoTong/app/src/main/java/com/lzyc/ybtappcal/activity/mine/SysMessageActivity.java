package com.lzyc.ybtappcal.activity.mine;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.activity.top.TopWebviewActivity;
import com.lzyc.ybtappcal.adapter.SysMessageAdapter;
import com.lzyc.ybtappcal.bean.MessageSystem;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.listener.OnYbtRefreshListener;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.MD5ChangeUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.lzyc.ybtappcal.view.xlist.XYbtRefreshListView;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
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
 * 系统消息
 * Created by yang on 2016/9/21.
 */
public class SysMessageActivity extends BaseActivity implements SysMessageAdapter.OnItemChildClickListener, OnYbtRefreshListener {
    private static final String TAG=SysMessageActivity.class.getSimpleName();
    @BindView(R.id.message_sys_lv)
    XYbtRefreshListView messageSysLv;
    @BindString(R.string.empty_message_list)
    String emptyMessageList;
    private SysMessageAdapter mAdapter;
    private List<MessageSystem> listMessageSystem;

    private int page = 0;

    @Override
    public int getContentViewId() {
        return R.layout.activity_message_sys;
    }

    @Override
    protected void init() {
        setTitleName("系统消息");
        setPageStateView();
        showLoading();
        listMessageSystem = new ArrayList<MessageSystem>();
        mAdapter = new SysMessageAdapter(SysMessageActivity.this, R.layout.item_message_system, listMessageSystem);
        mAdapter.setOnItemChildClickListener(this);
        messageSysLv.setAdapter(mAdapter);
        messageSysLv.setOnRefreshListener(this);
        requestSysMessageList();
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

    public void showDeleteDialog(final String id) {
        final AlertDialog a = new AlertDialog.Builder(this).create();
        a.setCanceledOnTouchOutside(false);
        a.show();
        a.getWindow().setContentView(R.layout.dialog_message_delete);
        TextView dialog_message_delete = (TextView) a.getWindow().findViewById(R.id.dialog_message_delete);
        dialog_message_delete.setText("删除该消息");
        dialog_message_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestDeleteMessage(id);
                if (a != null) {
                    a.dismiss();
                }
            }
        });
    }

    private void requestDeleteMessage(String id) {
        Map<String, String> params = new HashMap<String, String>();
        String phone = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PHONE, "");
        params.put("app", "Ucenter");
        params.put("class", "DelMessage2");
        String sign = MD5ChangeUtil.Md5_32("UcenterDelMessage2" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        if (phone.isEmpty()) {
            return;
        }
        params.put("phone", phone);
        params.put("type", "system");
        params.put("msg_id", id);
        request(params, HttpConstant.MESSAGE_DELETE);
    }

    private void requestSysMessageList() {
        String phone = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PHONE, "");
        Map<String, String> params = new HashMap<String, String>();
        params.put("app", "Ucenter");
        params.put("class", "Message2");
        String sign = MD5ChangeUtil.Md5_32("UcenterMessage2" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        if (phone.isEmpty()) {
            return;
        }
        params.put("phone", phone);
        params.put("type", "system");
        params.put("pageIndex", "" + page);
        if (!NetworkUtil.CheckConnection(this)) {
            if(page==0){
                showError();
            }else{
                showToast("网络不可用");
            }
            return;
        }
        request(params, HttpConstant.MESSAGE_LIST_SYS);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        switch (what) {
            case HttpConstant.MESSAGE_LIST_SYS:
                try {
                    JSONObject dataJsonObject = response.getJSONObject("data");
                    Type type = new TypeToken<ArrayList<MessageSystem>>() {
                    }.getType();
                    List<MessageSystem> listMessageSystem = JsonUtil.getListModle(dataJsonObject.toString(), "list", type);
                    if (listMessageSystem == null) {
                        if (page > 0) {
                            page--;
                            messageSysLv.stopLoadMore();
                        } else {
                            messageSysLv.stopRefresh();
                        }
                        return;
                    }
                    if (listMessageSystem.size() == 0) {
                        if (page > 0) {
                            page--;
                            ToastUtil.showToastCenter(SysMessageActivity.this, "没有更多数据了");
                            messageSysLv.stopLoadMore();
                        } else {
                            messageSysLv.stopRefresh();
                            showEmpty(emptyMessageList,R.mipmap.empty_message);
                        }
                        return;
                    }
                    if (page == 0) {
                        mAdapter.setList(listMessageSystem);
                        messageSysLv.stopRefresh();
                    } else {
                        mAdapter.addList(listMessageSystem);
                        messageSysLv.stopLoadMore();
                    }
                    showContent();
                } catch (JSONException e) {
                    showEmpty(errorServer,R.mipmap.empty_error_net_server);
                }catch (Exception e){
                    showEmpty(errorServer,R.mipmap.empty_error_net_server);
                }
                break;
            case HttpConstant.MESSAGE_DELETE:
                onDownPullRefresh();
                break;
            default:
                showEmpty(errorServer,R.mipmap.empty_error_net_server);
                break;

        }
    }

    @Override
    public void onItemChildListener(int position, MessageSystem messageSystem) {
        String url=messageSystem.getUrl();
        if(!TextUtils.isEmpty(url)){
            Intent intent=new Intent(this,TopWebviewActivity.class);
            intent.putExtra(Contants.KEY_STR_URL,url);
            intent.putExtra(Contants.KEY_STR_TITLE,"消息详情");
            startActivity(intent);
        }

    }

    @Override
    public void onItemChildLongLostener(int position, String msg_id) {
        showDeleteDialog(msg_id);
    }

    @Override
    public void error(String errorMsg) {
        try{
            ErrorResponse errorResponse= JsonUtil.getModle(errorMsg, ErrorResponse.class);
            switch (errorResponse.getWhat()){
                case HttpConstant.MESSAGE_LIST_SYS:
                    if(errorResponse.isNetError()){
                        if(page==0){
                            showError();
                        }else{
                            showToast("网络不可用");
                        }
                    }else{
                        showEmpty(errorServer,R.mipmap.empty_error_net_server);
                    }
                    break;
                case HttpConstant.MESSAGE_DELETE:
                    if(!TextUtils.isEmpty(errorResponse.getMsg())){
                        showToast(errorResponse.getMsg());
                    }else{
                        showToast("删除失败");
                    }
                    break;
            }
        }catch (Exception e){
            showEmpty(errorServer,R.mipmap.empty_error_net_server);
        }
    }

    @Override
    public void onDownPullRefresh() {
        page = 0;
        requestSysMessageList();
    }

    /**
     * 点击了错误重试
     */
    protected void  onClickRetry(){
        onDownPullRefresh();
    }

    @Override
    public void onLoadingMore() {
        page++;
        requestSysMessageList();
    }
}