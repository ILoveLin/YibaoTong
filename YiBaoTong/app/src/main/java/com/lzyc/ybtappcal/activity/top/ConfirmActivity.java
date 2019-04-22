package com.lzyc.ybtappcal.activity.top;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.MainActivity;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.ConfirmAdapter;
import com.lzyc.ybtappcal.bean.DrugBean;
import com.lzyc.ybtappcal.bean.DrugConfirmBean;
import com.lzyc.ybtappcal.bean.Event;
import com.lzyc.ybtappcal.constant.Contants;
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
import butterknife.OnClick;


/**
 * 作者：xujm
 * 时间：2016/2/22
 * 备注：认可度
 */
public class ConfirmActivity extends BaseActivity implements OnYbtRefreshListener {
    private static final String TAG=ConfirmActivity.class.getSimpleName();
    @BindView(R.id.tv_confirm_name)
    TextView tv_confirm_name;
    @BindView(R.id.tv_confirm_namefu)
    TextView tv_confirm_namefu;
    @BindView(R.id.tv_title_first)   //皇冠
            ImageView tv_title_first;
    @BindView(R.id.tv_confirm_spec)
    TextView tv_confirm_spec;
    @BindView(R.id.tv_confirm_foctory)
    TextView tv_confirm_foctory;
    @BindView(R.id.tv_confirm_price)              //第一个价格
            TextView tv_confirm_price;
    @BindView(R.id.tv_confirm_oprice)             //第二个价格
            TextView tv_confirm_oprice;
    @BindView(R.id.tv_confirm_num)
    TextView tv_confirm_num;
    @BindView(R.id.lv_confirm)
    XYbtRefreshListView lv_confirm;
    @BindView(R.id.linear_drug)
    LinearLayout linear_drug;
    @BindView(R.id.include_loading_linear)
    LinearLayout linear_loading;
    @BindView(R.id.linear_confirm)
    LinearLayout linear_confirm;
    @BindView(R.id.include_loading_iv)
    ImageView loading_image_iv;
    @BindView(R.id.include_net_error_linear)
    LinearLayout net_error_linear;
    @BindView(R.id.include_net_setting_tv)
    TextView net_setting;
    private String drugNameID;
    private String specificationsID;
    private String venderID;
    private int page;
    private ArrayList<DrugConfirmBean> mList;
    private ConfirmAdapter adapter;
    private String num;
    private String num1;
    private String currentProvince;
    private String drugId;
    private int typePage = 0;

    @Override
    public int getContentViewId() {
        return R.layout.activity_confirm;
    }

    @Override
    public void init() {
        setTitleName("认可度");
        setTitleRightBtnResources(R.drawable.selector_titlebar_right_backhome);
        drugNameID = getIntent().getStringExtra("drugNameID");
        specificationsID = getIntent().getStringExtra("specificationsID");
        venderID = getIntent().getStringExtra("venderID");
        currentProvince = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.PROVICE_TOP_CHOOSE, "北京市");
        typePage = getIntent().getIntExtra(Contants.KEY_PAGE, typePage);
        drugId = getIntent().getStringExtra("drugId");
        tv_title_first.setVisibility(View.VISIBLE);
        linear_loading.setVisibility(View.VISIBLE);
        linear_confirm.setVisibility(View.GONE);
        AnimationDrawable animationDrawable = (AnimationDrawable) loading_image_iv.getBackground();
        animationDrawable.start();
        initLv();
    }

    private void initLv() {
        mList = new ArrayList<>();
        adapter = new ConfirmAdapter(this, R.layout.item_confirm2, mList);
        adapter.setCurrentProvince(currentProvince);
        //上拉加载下拉刷新
        lv_confirm.setOnRefreshListener(this);
        lv_confirm.setAdapter(adapter);
        lv_confirm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               requestEventCode("g002");
                DrugConfirmBean bean = (DrugConfirmBean) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                DrugBean drugBean = new DrugBean();
                drugBean.setDrugNameID(bean.getDrugNameID());
                drugBean.setVenderID(bean.getVenderID());
                drugBean.setSpecificationsID(bean.getSpecificationsID());
                drugBean.setDrugID(bean.getDrug_id());
                bundle.putSerializable(Contants.KEY_OBJ_DRUGBEAN, drugBean);
                bundle.putInt(Contants.KEY_PAGE_RESULTS, typePage);
                openActivity(ResultsActivity.class, bundle);
            }
        });
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

    @OnClick({R.id.tv_confirm_num, R.id.ib_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm_num:  //认可度bug修复,第一条采纳医院才可以点击
                Intent intent = new Intent(this, HispitalListActivity.class);
                intent.putExtra("drugId", drugId);
                intent.putExtra("venderID", venderID);
                intent.putExtra("drugNameID", drugNameID);
                intent.putExtra("specificationsID", specificationsID);
                startActivity(intent);
                break;
            case R.id.ib_right:
                Intent toHome = new Intent(this, MainActivity.class);
                toHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toHome);
                break;
            default:
                super.onClickTitleLeftBtn(view);
                break;
        }
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        requestConfirm(page);
    }

    /**
     * 认可度列表
     */
    public void requestConfirm(int page) {
        String ybType = (String) SharePreferenceUtil.get(ConfirmActivity.this, SharePreferenceUtil.KEY_OBJ_TYPE_JOB, Contants.STR_TYPE_JOB);
        Map<String, String> params = new HashMap<String, String>();
        params.put("app", "Home");
        params.put("class", "Collect2");
        String sign = MD5ChangeUtil.Md5_32("HomeCollect2" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        params.put("drug_id", drugId);
        params.put("DrugNameID", drugNameID);
        params.put("SpecificationsID", specificationsID);
        params.put("VenderID", venderID);
        params.put("pageIndex", page + "");
        params.put("ybtype", ybType);
        params.put("province", currentProvince);
        if (!NetworkUtil.CheckConnection(this)) {
            linear_loading.setVisibility(View.GONE);
            net_error_linear.setVisibility(View.VISIBLE);
            net_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetworkUtil.CheckConnection(ConfirmActivity.this)) {
                        showNetDialog();
                        return;
                    }
                    requestConfirm(0);
                }
            });
            return;
        } else {
            net_error_linear.setVisibility(View.GONE);
        }
        request(params, HttpConstant.TOP_CONFIRM);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.TOP_CONFIRM:
                try {
                    JSONObject data = response.getJSONObject("data");
                    DrugConfirmBean info = JsonUtil.getModle(data.getJSONObject("info").toString(), DrugConfirmBean.class);
                    fullInfo(info);
                    num = info.getDrugHostopID();
                    num1 = info.getHosNum();
                    adapter.setDrugConfirmBean(info);
                    Type type = new TypeToken<ArrayList<DrugConfirmBean>>() {
                    }.getType();
                    List<DrugConfirmBean> list = JsonUtil.getListModle(data.toString(), "info2", type);
                    reload(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void fullInfo(DrugConfirmBean info) {
        String hosNum = info.getHosNum();
        String goodsName = info.getGoodsName();
        String name = info.getName();
        //设置名字
        if (goodsName.equals("")) {    //白加黑id   goodname白加黑   美芬片name
            tv_confirm_name.setText(name);
        } else if (name.equals("")) {
            tv_confirm_name.setText(goodsName);
        } else {
            tv_confirm_name.setText(goodsName);
            tv_confirm_namefu.setText(name);
        }
        //公司和药量
        tv_confirm_spec.setText("规格:" + info.getSpecifications() + "");
        tv_confirm_foctory.setText(info.getVender());
        if (currentProvince.equals("北京") || typePage == Contants.VAL_PAGE_MINEPLAN) {
            //设置价格
            if (info.getPrice().equals(info.getNewPrice())) {

            } else {
                tv_confirm_oprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            }
            String newPrice = info.getNewPrice();
            if (TextUtils.isEmpty(newPrice)) {
                tv_confirm_price.setVisibility(View.VISIBLE);
                tv_confirm_oprice.setVisibility(View.GONE);
                tv_confirm_price.setText("暂无报价");
            } else {
                if (Double.parseDouble(newPrice) == 0) {
                    tv_confirm_price.setVisibility(View.VISIBLE);
                    tv_confirm_oprice.setVisibility(View.GONE);
                    tv_confirm_price.setText("暂无报价");
                } else {
                    tv_confirm_price.setText("¥ " + info.getNewPrice());
                    tv_confirm_oprice.setText("¥ " + info.getPrice());
                    tv_confirm_oprice.setAlpha(0.8f);
                }
            }

        } else {
            //不是北京省
            if (info.getNewPrice().equals("")) {
                tv_confirm_price.setText("¥ " + info.getPrice());
                tv_confirm_oprice.setText("¥" + info.getPrice());
                tv_confirm_oprice.setAlpha(0.8f);
            } else {
                tv_confirm_price.setText("¥ " + info.getPrice());
                tv_confirm_oprice.setText("");
                tv_confirm_oprice.setAlpha(0.8f);
            }
        }
        //设置采纳医院
        tv_confirm_num.setText("采纳医院 " + hosNum + " 家");
        tv_confirm_num.setTextColor(Color.parseColor("#12714d"));
        tv_confirm_num.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
        linear_drug.setBackgroundResource(R.mipmap.icon_bg_confirm_message);
        linear_loading.setVisibility(View.GONE);
        linear_confirm.setVisibility(View.VISIBLE);
    }


    private void reload(List<DrugConfirmBean> list) {
        if (list.isEmpty()) {
            ToastUtil.showShort(this, "没有更多数据了");
        }
        if (page == 0) {
            if (list == null) {
                list = new ArrayList<>();
            }
            if (mList != null) {
                mList.clear();
            }
            DrugConfirmBean drugConfirmBean = list.get(0);
            drugConfirmBean.setFirst(true);
            list.set(0, drugConfirmBean);
            mList.addAll(list);
            adapter.notifyDataSetChanged();
            lv_confirm.stopRefresh();
        } else {
            page++;
            if (list == null) {
                list = new ArrayList<>();
            }
            mList.addAll(list);
            adapter.notifyDataSetChanged();
            lv_confirm.stopLoadMore();
        }
        fullinfoTitileMessage(mList);
        lv_confirm.stopRefresh();
        lv_confirm.stopLoadMore();
    }

    private void fullinfoTitileMessage(List<DrugConfirmBean> list) {
        if (Integer.parseInt(num1) == Integer.parseInt(mList.get(0).getHosNum())) {
            tv_title_first.setVisibility(View.VISIBLE);
        } else {
            tv_title_first.setVisibility(View.INVISIBLE);
        }
        if (list.size() == 1) {
            tv_title_first.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDownPullRefresh() {
        page = 0;
        requestConfirm(page);
    }

    @Override
    public void onLoadingMore() {
        page++;
        requestConfirm(page);
    }
}
