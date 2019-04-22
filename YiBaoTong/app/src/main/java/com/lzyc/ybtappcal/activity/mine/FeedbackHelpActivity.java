package com.lzyc.ybtappcal.activity.mine;

import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.HelpAdapter;
import com.lzyc.ybtappcal.bean.HelpBean;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.StringUtils;
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
 * 帮助中心，操作帮助
 * Created by yang on 17/07/07.
 */
public class FeedbackHelpActivity extends BaseActivity{
    private static final String TAG=FeedbackHelpActivity.class.getSimpleName();
    @BindView(R.id.huitan)
    TwinklingRefreshLayout huiTan;
    @BindView(R.id.listview)
    ListView mListView;
    @BindView(R.id.tv_worktime)
    TextView tvWorktime;
    @BindView(R.id.linear_kefu)
    LinearLayout linearKefu;
    @BindView(R.id.linear_feedback)
    LinearLayout linearFeedback;
    @BindView(R.id.linear_action)
    LinearLayout linearAction;
    @BindString(R.string.help)
    String titleName;
    @BindString(R.string.kefu_help)
    String kefuHelp;
    private List<HelpBean> mDatas;
    private HelpAdapter mAdapter;
    @Override
    public int getContentViewId() {
        return R.layout.activity_feedback_help;
    }

    @Override
    protected void init() {
        setTitleName(titleName);
        setPageStateView();
        showLoading();
        huiTan.setPureScrollModeOn();
        requestFeedHelp();
        mDatas=new ArrayList<>();
        mAdapter=new HelpAdapter(mContext,R.layout.item_help,mDatas);
        View footerView=getLayoutInflater().inflate(R.layout.footer_help,null);
        mListView.addFooterView(footerView);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 获取帮助列表
     */
    public void requestFeedHelp() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_UCENTER);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.UCENTER_FEEDBACK_HELP_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.UCENTER_FEEDBACK_HELP_SIGN);
        if(!NetworkUtil.CheckConnection(mContext)){
            showError();
           return;
        }
        request(params, HttpConstant.FEEDBACK_HELP);
    }

    @OnClick({R.id.linear_kefu, R.id.linear_feedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linear_kefu:
                popupCall();
                break;
            case R.id.linear_feedback:
                openActivity(FeedBackActivity.class);
                break;
        }
    }

    /**
     * 联系客服
     */
    private void popupCall() {
        String phone = StringUtils.getSubsectionPhone(kefuHelp);
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(this);
        twoButton.getTv_content().setText(phone);
        twoButton.getTvOk().setText("呼叫");
        twoButton.getTvCancel().setText("取消");
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:"+kefuHelp );
                intent.setData(data);
                startActivity(intent);
                twoButton.dismiss();
            }
        });
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(mListView, Gravity.CENTER);
    }

    /**
     * 点击了错误重试
     */
    protected void  onClickRetry(){
        showLoading();
        requestFeedHelp();
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
        super.done(msg, what, response);
        switch (what){
            case HttpConstant.FEEDBACK_HELP:
                try {
                    JSONObject joData = response.getJSONObject(HttpConstant.DATA);
                    Type type = new TypeToken<ArrayList<HelpBean>>() {
                    }.getType();
                    mDatas = JsonUtil.getListModle(joData.toString(), HttpConstant.LIST, type);
                    mAdapter.setList(mDatas);
                    showContent();
                } catch (JSONException e) {
                    e.printStackTrace();
                    showErrorServer();
                }
                break;
        }
    }

    @Override
    public void error(String errorMsg) {
        try{
            ErrorResponse mErrorResponse=JsonUtil.getModle(errorMsg,ErrorResponse.class);
            int what=mErrorResponse.getWhat();
            if(what==HttpConstant.FEEDBACK_HELP){
                String msg= mErrorResponse.getMsg();
                if(mErrorResponse.isNetError()){
                   showError();
                    return;
                }
                if(!msg.isEmpty()){
                    showToast(msg);
                }
                showErrorServer();
            }
        }catch (Exception e){
            showErrorServer();
        }

    }
}
