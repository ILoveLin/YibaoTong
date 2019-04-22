package com.lzyc.ybtappcal.activity.top;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.account.LoginActivity;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.CommunityReplyAdapter;
import com.lzyc.ybtappcal.bean.CommunityReplyBean;
import com.lzyc.ybtappcal.bean.Event;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.listener.OnYbtRefreshListener;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.MD5ChangeUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.StringUtils;
import com.lzyc.ybtappcal.util.Util;
import com.lzyc.ybtappcal.view.StarBar;
import com.lzyc.ybtappcal.view.xlist.XYbtRefreshListView;
import com.lzyc.ybtappcal.widget.MyRoundImageView;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;


/**
 * Created by lovelin on 16/8/29.
 * 评价详情
 */
public class HospitalCommunityRaplyActivity extends BaseActivity implements CommunityReplyAdapter.OnMyItemClickListener, OnYbtRefreshListener {
    private static final String TAG=HospitalCommunityRaplyActivity.class.getSimpleName();
    protected static final int MESSAGER_REFRESH_UI = 1;
    protected static final int MESSAGER_REFRESH_UI_SUCCESS = 3;
    protected static final int TAG_IS_LICK_CLICKED = 1;
    @BindView(R.id.id_hospital_community_lv)
    XYbtRefreshListView id_hospital_community_lv;
    @BindView(R.id.edit_comment)
    EditText edit_comment;
    @BindView(R.id.tv_raply_send_message)
    TextView tv_raply_send_message;
    @BindView(R.id.include_loading_iv)
    ImageView loading_image_iv;
    @BindView(R.id.include_loading_linear)
    LinearLayout linear_loading;
    @BindView(R.id.id_relative_show_content)
    RelativeLayout id_relative_show_content;
    private String commentId;
    private int pager = 0;
    private CommunityReplyAdapter mAdapter;
    private ArrayList<CommunityReplyBean.DataBean.ListBean> mData;
    private TextView item_hc_tv_date;
    private TextView item_hc_tv_phone_num;
    private TextView item_hc_tv_content;
    private StarBar starBar;
    private String type = 2 + "";
    private String content;
    private CommunityReplyBean.DataBean.ListBean mListBean;
    private int currentType;
    private LinearLayout header_item_hc_linea_comment;
    private TextView item_hc_tv_desc;
    private TextView header_tv_lick_count;
    private TextView header_tv_commnity_count;
    private ImageView item_hc_iv_like;
    private LinearLayout header_item_hc_linea_like;
    private boolean isFirstIn;
    private String nickname;
    private Boolean isLogin;
    private String loginPhone;
    private TextView item_community_start;
    private String pPhone;
    private boolean isPush = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGER_REFRESH_UI:
                    onFocusChange(false, false);
                    break;
                case MESSAGER_REFRESH_UI_SUCCESS:
                    pager = 0;
                    currentType = 2;
                    reuqestCommunity();
                    break;
                default:
                    break;
            }
        }
    };
    private String token;
    private CommunityReplyBean dataBean;
    private MyRoundImageView item_recomment_iv_covert;

    @Override
    public int getContentViewId() {
        return R.layout.activity_hospital_community;
    }

    @Override
    public void init() {
        setTitleName("评价详情");
//        registerBroadCast();
        token = PushAgent.getInstance(this).getRegistrationId();
        Bundle bundle = getIntent().getExtras();
        commentId = bundle.getString("commentId");
        isLogin = (Boolean) SharePreferenceUtil.get(HospitalCommunityRaplyActivity.this, SharePreferenceUtil.IS_LOGIN, false);
        initView();
        responseListener();
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        pager = 0;
        currentType = 2;
        isFirstIn = true;
        AnimationDrawable startAniuma = (AnimationDrawable) loading_image_iv.getBackground();
        startAniuma.start();
        linear_loading.setVisibility(View.VISIBLE);
        id_relative_show_content.setVisibility(View.GONE);
        if (!isPush) {//如果广播没有执行
            reuqestCommunity();
            isPush = false;
        }
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

    /**
     * 显示或隐藏输入法
     * 第一个true 表示显示输入法
     * 第二个true 表示lv的item中的回复交流
     */
    private void onFocusChange(final boolean hasFocus, final boolean commentChart) {
        mHandler.postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        edit_comment.getContext().getSystemService(INPUT_METHOD_SERVICE);
                if (hasFocus) {
                    //显示输入法
                    edit_comment.requestFocus();//获取焦点
                    if (commentChart) {
                        edit_comment.setText("");
                        edit_comment.setHint("回复" + nickname);
                    } else {
                        edit_comment.setText("");
                        edit_comment.setHint("我也有话说");
                    }
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    //隐藏输入法
                    edit_comment.setText("");
                    edit_comment.setHint("我也有话说");
                    imm.hideSoftInputFromWindow(edit_comment.getWindowToken(), 0);
                }
            }
        }, 200);
    }

    /**
     * 点击屏幕其他地方收起输入法
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                onFocusChange(false, false);
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 隐藏或者显示输入框
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            /**
             *这堆数值是算我的下边输入区域的布局的，
             * 规避点击输入区域也会隐藏输入区域
             */
            v.getLocationInWindow(leftTop);
            int left = leftTop[0] - 50;
            int top = leftTop[1] - 50;
            int bottom = top + v.getHeight() + 300;
            int right = left + v.getWidth() + 120;
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private void initView() {
        View headerView = View.inflate(this, R.layout.header_item_hospital_community, null);
        item_hc_iv_like = (ImageView) headerView.findViewById(R.id.item_hc_iv_like);
        item_hc_tv_phone_num = (TextView) headerView.findViewById(R.id.item_hc_tv_phone_num);
        item_hc_tv_content = (TextView) headerView.findViewById(R.id.item_hc_tv_content);
        header_item_hc_linea_comment = (LinearLayout) headerView.findViewById(R.id.header_item_hc_linea_comment);
        header_item_hc_linea_like = (LinearLayout) headerView.findViewById(R.id.header_item_hc_linea_like);
        item_hc_tv_date = (TextView) headerView.findViewById(R.id.item_hc_tv_date);
        item_hc_tv_desc = (TextView) headerView.findViewById(R.id.item_hc_tv_desc);
        item_community_start = (TextView) headerView.findViewById(R.id.item_community_start);
        item_hc_iv_like = (ImageView) headerView.findViewById(R.id.item_hc_iv_like);
        item_recomment_iv_covert = (MyRoundImageView) headerView.findViewById(R.id.item_recomment_iv_covert);
        header_tv_lick_count = (TextView) headerView.findViewById(R.id.header_tv_lick_count);
        header_tv_commnity_count = (TextView) headerView.findViewById(R.id.header_tv_commnity_count);
        starBar = (StarBar) headerView.findViewById(R.id.test_star);
        mData = new ArrayList<CommunityReplyBean.DataBean.ListBean>();
        mAdapter = new CommunityReplyAdapter(this, R.layout.item_communtiy_reply, mData);
        id_hospital_community_lv.setOnRefreshListener(this);
        id_hospital_community_lv.addHeaderView(headerView);
        id_hospital_community_lv.setAdapter(mAdapter);
        mAdapter.setOnMyItemClickListener(this);
    }

    private void responseListener() {
        edit_comment.addTextChangedListener(Util.getUnSupportEmojiListen(this, edit_comment));
        tv_raply_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLogin = (Boolean) SharePreferenceUtil.get(HospitalCommunityRaplyActivity.this, SharePreferenceUtil.IS_LOGIN, false);
                content = edit_comment.getText().toString().trim();
                if (StringUtils.isEmpty(content)) {
                    showToast("消息不能为空");
                    return;
                } else if (content.length() > 140) {
                    showToast("字数不能超过140个字");
                }
                if (isLogin) {
                    loginPhone = (String) SharePreferenceUtil.get(HospitalCommunityRaplyActivity.this, SharePreferenceUtil.PHONE, "");
                    if (currentType == 1) {  //回复交流
                        requestEventCode("y006");
                        reuqestSubmitCommentChart(mListBean);
                    } else if (currentType == 2) {
                        requestEventCode("y006");
                        reuqestSubmitCommentt();   //头布局评论
                    } else {
                        LogUtil.e(HospitalCommunityRaplyActivity.class.getSimpleName(), "error");
                    }
                } else {
                    openActivity(LoginActivity.class);
                }
            }
        });

        //头布局  评论
        header_item_hc_linea_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentType = 2;
                onFocusChange(true, false);
            }
        });

        //头布局点赞
        header_item_hc_linea_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lickClicked = Integer.parseInt(dataBean.getData().getHeader().getPraiseStatus());
                if (TAG_IS_LICK_CLICKED != lickClicked) {
                    reuqestSubmitCommentLike(); //头布局点赞
                } else {
                    showToast("您已经点过赞了");
                }
            }
        });
    }

    /**
     * 评价详情界面 交流
     *
     * @param raplyBean
     */
    private void reuqestSubmitCommentChart(CommunityReplyBean.DataBean.ListBean raplyBean) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("app", "Comment");
        params.put("class", "SubmitComment");
        String sign = MD5ChangeUtil.Md5_32("CommentSubmitComment" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        params.put("phone", loginPhone);
        params.put("token", token);
        params.put("content", content);
        params.put("type", type);
        params.put("p_phone", mListBean.getPhone());
        params.put("parent_id", mListBean.getId());
        params.put("comment_id", commentId);
        request(params, HttpConstant.TOP_COMMENT_HOSPITAL_LIST_TWO);
    }

    /**
     * 评价详情界面    头布局点赞
     *
     * @param
     */
    private void reuqestSubmitCommentLike() {
        HashMap<String, String> params = new HashMap<String, String>();
        loginPhone = (String) SharePreferenceUtil.get(HospitalCommunityRaplyActivity.this, SharePreferenceUtil.PHONE, "");
        params.put("app", "Comment");
        params.put("class", "Praise");
        String sign = MD5ChangeUtil.Md5_32("CommentPraise" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        params.put("phone", loginPhone);
        params.put("status", 1 + "");
        params.put("token", token);
        params.put("comment_id", commentId);
        request(params, HttpConstant.TOP_COMMENT_HOSPITAL_LIST_HEADER_LIKE);
    }

    /**
     * 评价详情界面    头布局评论
     * 我有话说
     */
    private void reuqestSubmitCommentt() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("app", "Comment");
        params.put("class", "SubmitComment");
        String sign = MD5ChangeUtil.Md5_32("CommentSubmitComment" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        params.put("phone", loginPhone);
        params.put("token", token);
        params.put("content", content);
        params.put("type", type);
        params.put("p_phone", pPhone);
        params.put("parent_id", "");
        params.put("comment_id", commentId);
        request(params, HttpConstant.TOP_COMMENT_HOSPITAL_LIST_HEADER_THREE);
    }


    protected void reuqestCommunity() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("app", "Comment");
        params.put("class", "ShowCommentReply");
        String sign = MD5ChangeUtil.Md5_32("CommentShowCommentReply" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        params.put("comment_id", commentId);
        String phone = (String) SharePreferenceUtil.get(HospitalCommunityRaplyActivity.this, SharePreferenceUtil.PHONE, "");
        if (!phone.isEmpty()) {
            params.put("phone", phone);
        }
        params.put("pageIndex", pager + "");
        params.put("token", token);
        request(params, HttpConstant.TOP_COMMENT_HOSPITAL_REPLY_LIST);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        try {
            switch (what) {
                case HttpConstant.TOP_COMMENT_HOSPITAL_REPLY_LIST:
                    dataBean = JsonUtil.getModle(response.toString(), CommunityReplyBean.class);
                    raplyCommnityReply(dataBean.getData().getList());
                    raplyCommunityReply(dataBean.getData().getHeader());
                    break;
                case HttpConstant.TOP_COMMENT_HOSPITAL_LIST_TWO:
                    edit_comment.setText("");
                    mHandler.sendEmptyMessage(MESSAGER_REFRESH_UI_SUCCESS);
                    break;
                case HttpConstant.TOP_COMMENT_HOSPITAL_LIST_HEADER_THREE:
                    edit_comment.setText("");
                    mHandler.sendEmptyMessage(MESSAGER_REFRESH_UI_SUCCESS);
                    break;
                case HttpConstant.TOP_COMMENT_HOSPITAL_LIST_HEADER_LIKE:
                    item_hc_iv_like.setBackgroundResource(R.mipmap.icon_hospital_like_pre);
                    int i = Integer.parseInt(dataBean.getData().getHeader().getPraiseCount()) + 1;
                    header_tv_lick_count.setText("(" + i + ")");
                    break;
            }

        } catch (Exception e) {
            edit_comment.setText("");
        }
    }

    private void raplyCommunityReply(CommunityReplyBean.DataBean.HeaderData header) {
        String mark = header.getAverage();
        if (!TextUtils.isEmpty(mark)) {
            starBar.setStarMark(Float.parseFloat(mark));
        }
        header_tv_commnity_count.setText("(" + header.getReplyCount() + ")");
        if (!(TextUtils.isEmpty(header.getHeadImg()))) {
            Picasso.with(this).load(header.getHeadImg()).error(R.mipmap.icon_system_logo).into(item_recomment_iv_covert);
        } else {
            item_recomment_iv_covert.setImageResource(R.mipmap.icon_system_logo);
        }
        if ("1".equals(header.getPraiseStatus())) {
            item_hc_iv_like.setBackgroundResource(R.mipmap.icon_hospital_like_pre);
        }
        header_tv_lick_count.setText("(" + header.getPraiseCount() + ")");
        item_community_start.setText("" + Float.parseFloat(mark) + "");
        item_hc_tv_phone_num.setText("" + header.getP_nickname());
        item_hc_tv_content.setText("" + header.getContent());
        item_hc_tv_date.setText("" + header.getDate());
        pPhone = header.getPhone();
    }

    private void raplyCommnityReply(List<CommunityReplyBean.DataBean.ListBean> list) {

        if (pager == 0) {
            mAdapter.setList(list);
            id_hospital_community_lv.stopRefresh();
        } else {
            if (list.isEmpty()) {
                showToast("没有更多数据");
            }
            pager++;
            mAdapter.addList(list);
            id_hospital_community_lv.stopLoadMore();
        }
        if (list.size() == 0 && mAdapter.getCount() == 0) {
            item_hc_tv_desc.setText("暂无回复");
        } else {
            item_hc_tv_desc.setText("回复");
        }
        if (isFirstIn) {
            isFirstIn = false;
            mHandler.sendEmptyMessage(MESSAGER_REFRESH_UI);
        }
        linear_loading.setVisibility(View.GONE);
        id_relative_show_content.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMyItemClick(CommunityReplyBean.DataBean.ListBean bean, int position) {
        isLogin = (Boolean) SharePreferenceUtil.get(this, SharePreferenceUtil.IS_LOGIN, false);
        if (isLogin) {
            loginPhone = (String) SharePreferenceUtil.get(HospitalCommunityRaplyActivity.this, SharePreferenceUtil.PHONE, "");
            mListBean = bean;
            currentType = 1;
            nickname = bean.getNickname();
            onFocusChange(true, true);
        } else {
            openActivity(LoginActivity.class);
        }
    }

    @Override
    public void onDownPullRefresh() {
        pager = 0;
        reuqestCommunity();
    }

    @Override
    public void onLoadingMore() {
        pager++;
        reuqestCommunity();

    }
}
