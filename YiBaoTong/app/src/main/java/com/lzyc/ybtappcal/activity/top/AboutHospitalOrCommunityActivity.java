package com.lzyc.ybtappcal.activity.top;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.CommunityAdapter;
import com.lzyc.ybtappcal.adapter.CycleViewStatePagerAdapter;
import com.lzyc.ybtappcal.bean.Event;
import com.lzyc.ybtappcal.bean.HCCommentBean;
import com.lzyc.ybtappcal.bean.HospitalBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.listener.OnYbtRefreshListener;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.MD5ChangeUtil;
import com.lzyc.ybtappcal.view.StarBar;
import com.lzyc.ybtappcal.view.viewpager.DecoratorViewPager;
import com.lzyc.ybtappcal.view.xlist.XYbtRefreshListView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.OnClick;
import ybt.library.indicator.indicator.PageIndicatorView;

/**
 * Created by lovelin on 16/8/23.
 * 医院/社区,药品评价界面
 */
public class AboutHospitalOrCommunityActivity extends BaseActivity implements CommunityAdapter.OnHcChildClickListemer, OnYbtRefreshListener {
   private static final String TAG=AboutHospitalOrCommunityActivity.class.getSimpleName();
    private TextView id_about_community_count;                          //OnHcChildClickListemer
    private StarBar test_star;
    private TextView id_about_hospital_service;
    private TextView id_about_hospital_drug;
    private TextView id_about_hospital_getdrug;
    private View headerViewPager;
    private DecoratorViewPager mViewPage;
    private TextView header_hc_tv_name_vp;
    private CommunityAdapter mAdapter;
    private HospitalBean mHospitalBean;
    private int indexPage;
    private int typeAddress;
    private TextView header_hc_detail;
    private TextView hc_about_titlebar_content;
    private TextView hc_about_bottom_distance;
    private ArrayList<HCCommentBean.hcData.HcDataListBean> mData;
    private TextView id_about_community_point;
    private LinearLayout linear_loading;
    private LinearLayout id_about_community_linear;
    private ImageView loading_image_iv;
    private TextView header_hc_tell;
    private LinearLayout linear_about_community_goto_hispitaldetail;
    private LinearLayout detail_vp_bottom_linear;
    private PageIndicatorView detail_vp_indicator;
    private List<String> hospitalImages;
    private XYbtRefreshListView hc_listview;
    private TextView header_detail_hospital_level;


    @Override
    public int getContentViewId() {
        return R.layout.activity_about_hospital_community;
    }

    @Override
    protected void init() {
        setTitleBarVisibility(View.GONE);
        initView();
        initListener();
    }

    private void initView() {
        initHeaderView();
        hc_listview = (XYbtRefreshListView) findViewById(R.id.hc_listview);
        linear_loading = (LinearLayout) findViewById(R.id.include_loading_linear);
        id_about_community_linear = (LinearLayout) findViewById(R.id.id_about_community_linear);
        loading_image_iv = (ImageView) findViewById(R.id.include_loading_iv);
        //医院评价 (本activity的 默认隐藏)
        hc_about_titlebar_content = (TextView) findViewById(R.id.hc_about_titlebar_content);
        hc_about_bottom_distance = (TextView) findViewById(R.id.hc_about_bottom_distance);
        mData = new ArrayList<HCCommentBean.hcData.HcDataListBean>();
        iniData();
        mAdapter = new CommunityAdapter(this, R.layout.item_hospital_community, mData);
        hc_listview.setOnRefreshListener(this);
        hc_listview.addHeaderView(headerViewPager);
        hc_listview.setAdapter(mAdapter);
        mAdapter.setOnHcChildClickListemer(this);
    }

    private void initHeaderView() {
        headerViewPager = View.inflate(AboutHospitalOrCommunityActivity.this, R.layout.header_about_hospital_community_viewpage, null);
        mViewPage = (DecoratorViewPager) headerViewPager.findViewById(R.id.viewpage);
        detail_vp_indicator = (PageIndicatorView) headerViewPager.findViewById(R.id.detail_vp_indicator);
        header_hc_detail = (TextView) headerViewPager.findViewById(R.id.header_hc_detail);
        header_detail_hospital_level = (TextView) headerViewPager.findViewById(R.id.header_detail_hospital_level);
        header_hc_tv_name_vp = (TextView) headerViewPager.findViewById(R.id.header_hc_tv_name);
        id_about_community_count = (TextView) headerViewPager.findViewById(R.id.id_about_community_count);
        id_about_community_point = (TextView) headerViewPager.findViewById(R.id.id_about_community_header_point);
        test_star = (StarBar) headerViewPager.findViewById(R.id.test_star);
        id_about_hospital_service = (TextView) headerViewPager.findViewById(R.id.id_about_hospital_service);
        id_about_hospital_drug = (TextView) headerViewPager.findViewById(R.id.id_about_hospital_drug);
        id_about_hospital_getdrug = (TextView) headerViewPager.findViewById(R.id.id_about_hospital_getdrug);
        linear_about_community_goto_hispitaldetail = (LinearLayout) headerViewPager.findViewById(R.id.linear_about_community_goto_hispitaldetail);
        header_hc_tell = (TextView) headerViewPager.findViewById(R.id.header_hc_tell);
        detail_vp_bottom_linear = (LinearLayout) headerViewPager.findViewById(R.id.detail_vp_bottom_linear);
        mHospitalBean = (HospitalBean) getIntent().getSerializableExtra(Contants.KEY_OBJ_HOSPITALBEAN);
        hospitalImages = mHospitalBean.getHospitalImages();
        if (hospitalImages.get(0).equals("#")) {
            detail_vp_bottom_linear.setVisibility(View.GONE);
        } else {
            detail_vp_bottom_linear.setVisibility(View.VISIBLE);
        }
        CycleViewStatePagerAdapter mCycleViewStatePagerAdapter = new CycleViewStatePagerAdapter(this, getSupportFragmentManager(), hospitalImages);
        mViewPage.setAdapter(mCycleViewStatePagerAdapter);
        detail_vp_indicator.setCount(hospitalImages.size());
        detail_vp_indicator.setViewPager(mViewPage);
        detail_vp_indicator.setNotAutoScroll();
    }

    private void iniData() {
        mHospitalBean = (HospitalBean) getIntent().getSerializableExtra(Contants.KEY_OBJ_HOSPITALBEAN);
        typeAddress = mHospitalBean.getTypeAddress();
        String titleContent;
        if (typeAddress == 0) {
            titleContent = "医院";
            header_hc_detail.setVisibility(View.VISIBLE);

        } else {
            titleContent = "社区";
            header_hc_detail.setVisibility(View.VISIBLE);
        }
        hc_about_titlebar_content.setText(titleContent);
        String d = mHospitalBean.getDistance();
        if (!TextUtils.isEmpty(d)) {
            double distance = Double.parseDouble("" + d);
            double dis = (distance / 1000);
            hc_about_bottom_distance.setText(String.format("%.1f", dis).toString() + "km");
        }
        header_detail_hospital_level.setText(""+mHospitalBean.getLevel()+mHospitalBean.getJibie());
        header_hc_tv_name_vp.setText("" + mHospitalBean.getName());
        hospitalImages = mHospitalBean.getHospitalImages();
        if (hospitalImages.get(0).equals("#")) {
            detail_vp_bottom_linear.setVisibility(View.GONE);
        } else {
            detail_vp_bottom_linear.setVisibility(View.VISIBLE);
        }
    }

    private void initListener() {
        //进入医院详情页
        linear_about_community_goto_hispitaldetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutHospitalOrCommunityActivity.this, HispitalDetailActivity.class);
                intent.putExtra("url", HttpConstant.BASE_URL + "/");
                intent.putExtra("yyId", mHospitalBean.getYyid());
                startActivity(intent);
            }
        });
        String phone = mHospitalBean.getPhone();
        if (!TextUtils.isEmpty(phone)) {
            header_hc_tell.setText("" + phone);
        } else {
            header_hc_tell.setVisibility(View.INVISIBLE);
        }
        header_hc_tell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestEventCode("y002");
                String num = header_hc_tell.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("tel:" + num));
                startActivity(intent);
            }
        });
        if ("".equals(mHospitalBean.getPhone())) {
            header_hc_tell.setVisibility(View.GONE);
        }
    }

    private void switchHcCommentEditActivity() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Contants.KEY_OBJ_HOSPITALBEAN, mHospitalBean);
        openActivity(HcCommentEditActivity.class, bundle);
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        indexPage = 0;
        AnimationDrawable animationDrawable = (AnimationDrawable) loading_image_iv.getBackground();
        animationDrawable.start();
        id_about_community_linear.setVisibility(View.GONE);
        linear_loading.setVisibility(View.VISIBLE);
        reuqestAboutHospitalOrCommunity();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

    private void follInfoHeader(HCCommentBean data) {
        HCCommentBean.hcData hcData = data.getData();
        HCCommentBean.hcData.HeaderBean headerData = hcData.getHeader();
        String mark = headerData.getAverage();
        if (!TextUtils.isEmpty(mark)) {
            test_star.setStarMark(Float.parseFloat(mark));
        }
        id_about_hospital_service.setText("  服务态度 " + headerData.getServiceAttitude());
        id_about_hospital_drug.setText("  药品齐全 " + headerData.getDrugMany());
        id_about_hospital_getdrug.setText("  拿药方便 " + headerData.getGetDrugConvenience());
        id_about_community_count.setText(headerData.getCount() + "人评价");
        id_about_community_point.setText(Float.parseFloat(mark)+"");
        List<HCCommentBean.hcData.HcDataListBean> list = hcData.getList();

        rolatAboutListview(list);
    }

    private void rolatAboutListview(List<HCCommentBean.hcData.HcDataListBean> list) {
        if (indexPage == 0) {

            mAdapter.setList(list);
            hc_listview.stopRefresh();
        } else {
            if (list.isEmpty()) {
                showToast("没有更多数据");
            }
            indexPage++;
            mAdapter.addList(list);
            hc_listview.stopLoadMore();
        }
        if (mAdapter.getCount() == 0) {//在 item 里面是设置 emptyiew
            mAdapter.setEmpty();
        }
        id_about_community_linear.setVisibility(View.VISIBLE);
        linear_loading.setVisibility(View.GONE);
    }

    private void reuqestSubmitCommentLike(HCCommentBean.hcData.HcDataListBean bean) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("app", "Comment");
        params.put("class", "Praise");
        String sign = MD5ChangeUtil.Md5_32("CommentPraise" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        params.put("token", PushAgent.getInstance(this).getRegistrationId());
        params.put("phone", bean.getPhone());
        params.put("status", 1 + "");
        params.put("token", PushAgent.getInstance(this).getRegistrationId());
        params.put("comment_id", bean.getId());
        request(params, HttpConstant.TOP_COMMENT_HOSPITAL_LIST_LIKE);
    }

    protected void reuqestAboutHospitalOrCommunity() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("app", "Comment");
        params.put("class", "ShowCommentHos");
        params.put("token", PushAgent.getInstance(this).getRegistrationId());
        String sign = MD5ChangeUtil.Md5_32("CommentShowCommentHos" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        params.put("yyid", mHospitalBean.getYyid());
        params.put("pageIndex", indexPage + "");
        request(params, HttpConstant.TOP_COMMENT_HOSPITAL_LIST);

    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.TOP_COMMENT_HOSPITAL_LIST:
                try {
                    HCCommentBean data = JsonUtil.getModle(response.toString(), HCCommentBean.class);
                    follInfoHeader(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case HttpConstant.TOP_COMMENT_HOSPITAL_LIST_ONE:
                reuqestAboutHospitalOrCommunity();
                break;
            case HttpConstant.TOP_COMMENT_HOSPITAL_LIST_LIKE:
                break;
        }
    }
    @OnClick({R.id.hc_about_bottom_position, R.id.hc_about_bottom_comment_edit, R.id.hc_about_titlebar_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.hc_about_bottom_position:
                Bundle bundle=new Bundle();
                ArrayList<HospitalBean> list=new ArrayList<HospitalBean>();
                list.add(mHospitalBean);
                bundle.putSerializable("list", list);
                openActivity(HospitalNearbyActivity.class, bundle);
                break;
            case R.id.hc_about_bottom_comment_edit:
                requestEventCode("y003");
                switchHcCommentEditActivity();
                break;
            case R.id.hc_about_titlebar_left:
                AboutHospitalOrCommunityActivity.this.finish();
                break;
            default:
                super.onClickTitleLeftBtn(view);
                break;
        }
    }

    /**
     * 点赞和评论的回调
     *
     * @param position
     * @param bean
     */
    @Override
    public void onChildLike(int position, HCCommentBean.hcData.HcDataListBean bean) {
        if (bean.getParaiseStatus().equals("0")) {
            bean.setParaiseStatus("1");
            bean.setClickLike(true);
            mAdapter.setLikeStatus(position, bean);
            reuqestSubmitCommentLike(bean);

        } else {
            showToast("您已经点过赞了");
        }
    }
    @Override
    public void onChildComment(int position, HCCommentBean.hcData.HcDataListBean bean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("commentId", bean.getId());
        openActivity(HospitalCommunityRaplyActivity.class, bundle);
    }

    @Override
    public void onItemClickListener(int position, HCCommentBean.hcData.HcDataListBean bean) {
        Bundle bundle = new Bundle();
        bundle.putString("commentId", bean.getId());
        openActivity(HospitalCommunityRaplyActivity.class, bundle);
    }

    @Override
    public void onDownPullRefresh() {
        indexPage = 0;
        reuqestAboutHospitalOrCommunity();
    }

    @Override
    public void onLoadingMore() {
        indexPage++;
        reuqestAboutHospitalOrCommunity();
    }
}
