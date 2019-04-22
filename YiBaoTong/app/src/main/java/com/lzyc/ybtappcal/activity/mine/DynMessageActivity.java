package com.lzyc.ybtappcal.activity.mine;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.activity.top.HospitalCommunityRaplyActivity;
import com.lzyc.ybtappcal.adapter.DynMessageAdapter;
import com.lzyc.ybtappcal.bean.MessageComment;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.listener.OnYbtRefreshListener;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.MD5ChangeUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.lzyc.ybtappcal.view.xlist.XYbtRefreshListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 评论动态
 * Created by yang on 2016/9/21.
 */
public class DynMessageActivity extends BaseActivity implements OnYbtRefreshListener {
    private static final String TAG=DynMessageActivity.class.getSimpleName();
    @BindView(R.id.message_dyn_lv)
    XYbtRefreshListView message_dyn_lv;
//    @BindView(R.id.inculde_empty_message_no_desc)
//    TextView inculde_empty_message_no_desc;
    
    private DynMessageAdapter mAdapter;
    private List<MessageComment> listMessageComment;
    
    private int page = 0;

    private String phone;

    String emptyMessageList = "还没有人回复你哦!";

    @Override
    public int getContentViewId() {
        return R.layout.activity_message_dyn;
    }

    @Override
    public void init() {
        setTitleName("评论动态");

        setPageStateView();

        showLoading();

        phone = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PHONE, "");
//        inculde_empty_message_no_desc.setText("还没有人回复你哦!");
        listMessageComment = new ArrayList<MessageComment>();
        mAdapter = new DynMessageAdapter(this, R.layout.item_message_comment_dynamic, listMessageComment);
        message_dyn_lv.setAdapter(mAdapter);
        requestDynMessageList();
        message_dyn_lv.setOnRefreshListener(this);
        message_dyn_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MessageComment messageComment = (MessageComment) adapterView.getItemAtPosition(position);
                if (messageComment == null) {
                    showToast("无效点击");
                    return;
                }
                requestEventCode("n001");
                Bundle bundle = new Bundle();
                bundle.putString("commentId", messageComment.getComment_id());
                openActivity(HospitalCommunityRaplyActivity.class, bundle);
            }
        });
        message_dyn_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                MessageComment comment = (MessageComment) adapterView.getItemAtPosition(position);
                if (comment == null) {
                    return false;
                }
                if (comment.getMsg_id() == null) {
                    return false;
                }
                showDeleteDialog(comment.getMsg_id());
                return true;
            }
        });

    }

    public void showDeleteDialog(final String id) {
        final AlertDialog a = new AlertDialog.Builder(this).create();
        a.setCanceledOnTouchOutside(true);
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
        params.put("app", "Ucenter");
        params.put("class", "DelMessage2");
        String sign = MD5ChangeUtil.Md5_32("UcenterDelMessage2" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        if (phone.isEmpty()) {
            return;
        }
        params.put("phone", phone);
        params.put("type", "comment");
        params.put("msg_id", id);
        request(params, HttpConstant.MESSAGE_DELETE);
    }

    private void requestDynMessageList() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("app", "Ucenter");
        params.put("class", "Message2");
        String sign = MD5ChangeUtil.Md5_32("UcenterMessage2" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        if (phone.isEmpty()) {
            return;
        }
        params.put("phone", phone);
        params.put("type", "comment");
        params.put("pageIndex", "" + page);
        if (!NetworkUtil.CheckConnection(this)) {
            if (page > 0) {
                page--;
                stopLoaderMore();
            } else {
                message_dyn_lv.stopRefresh();
            }
            return;
        }
        request(params, HttpConstant.MESSAGE_LIST_DYN);
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

    @Override
    public void done(String msg, int what, JSONObject response) {
        switch (what) {
            case HttpConstant.MESSAGE_LIST_DYN:
                try {
                    JSONObject dataJsonObject = response.getJSONObject("data");
                    Type type = new TypeToken<ArrayList<MessageComment>>() {
                    }.getType();
                    List<MessageComment> listMessageComment = JsonUtil.getListModle(dataJsonObject.toString(), "list", type);
                    if (listMessageComment == null) {
                        if (page > 0) {
                            page--;
                            stopLoaderMore();
                        } else {
                            showEmpty(emptyMessageList,R.mipmap.empty_message);
                            message_dyn_lv.stopRefresh();
                        }
                        showContent();
                        return;
                    }
                    if (listMessageComment.size() == 0) {
                        if (page > 0) {
                            page--;
                            ToastUtil.showToastCenter(DynMessageActivity.this, "没有更多数据了");
                            stopLoaderMore();
                            showContent();
                        } else {
                            showEmpty(emptyMessageList,R.mipmap.empty_message);
                            
                            message_dyn_lv.stopRefresh();
                        }
                        return;
                    }
                    if (page == 0) {
                        mAdapter.setList(listMessageComment);
                        message_dyn_lv.stopRefresh();
                    } else {
                        stopLoaderMore();
                        mAdapter.addList(listMessageComment);
                    }

                    showContent();
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (page > 0) {
                        page--;
                        stopLoaderMore();
                    } else {
                        showEmpty(emptyMessageList,R.mipmap.empty_message);
                        message_dyn_lv.stopRefresh();
                    }
                }
                break;
            case HttpConstant.MESSAGE_DELETE:
                onDownPullRefresh();
                break;
            default:
                break;
        }
    }

    private void stopLoaderMore() {
        message_dyn_lv.stopLoadMore();
    }

    @Override
    public void error(String errorMsg) {
        if (page > 0) {
            page--;
            stopLoaderMore();
        } else {
            showEmpty(emptyMessageList,R.mipmap.empty_message);
            message_dyn_lv.stopRefresh();
        }
    }

    @Override
    public void onDownPullRefresh() {
        page = 0;
        requestDynMessageList();
    }

    @Override
    public void onLoadingMore() {
        page++;
        requestDynMessageList();
    }
}
