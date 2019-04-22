package com.lzyc.ybtappcal.activity.mine.medicine;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.activity.top.ResultsDetailActivity;
import com.lzyc.ybtappcal.adapter.MedicinePersonAdapter;
import com.lzyc.ybtappcal.bean.MedicineChestBean;
import com.lzyc.ybtappcal.bean.MedicineFamilyBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.ybtappcal.widget.xrecycler.XRecyclerView;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人的药箱
 * Created by lxx on 2017/5/24.
 */

public class MedicinePersonActivity extends BaseActivity {

    @BindView(R.id.ib_left_01)
    ImageButton ibLeft;
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_drug_num)
    TextView tvDrugNum;
    @BindView(R.id.recyclerview)
    XRecyclerView recyclerview;
    @BindView(R.id.rel_loading)
    RelativeLayout relLoading;
    @BindView(R.id.lin_add)
    LinearLayout linAdd;

    MedicinePersonAdapter mAdapter;

    ArrayList<MedicineChestBean.ListBean> mData = new ArrayList<>();

    Bundle bundle;

    MedicineFamilyBean.ListBean bean;

    int position;

    String idStr;

    int drugNum;

    @Override
    public int getContentViewId() {
        return R.layout.activity_medicine_person;
    }

    @Override
    protected void init() {

        setTitleBarVisibility(View.GONE);

        setPageStateView(relLoading);

        showLoading();

        initRecycler();

        initBundle(null);

        mData.clear();

        requestMedicineList();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Contants.ACTION_NAME_MEDICINEPERSION_FINISH);
        intentFilter.addAction(Contants.ACTION_NAME_MEDICINE_PERSON_REFRESH);
        registerReceiver(receiver, intentFilter);

        showAdd(linAdd);
    }

    private void initBundle(Intent intent) {

        if (null == intent) {
            bundle = getIntent().getExtras();
        } else {
            bundle = intent.getExtras();
        }

        bean = (MedicineFamilyBean.ListBean) bundle.getSerializable(Contants.KEY_MEDICINE_BEAN);

        idStr = bean.getID();

        String msg = bean.getAge()+"岁";

        if(!TextUtils.isEmpty(bean.getNote())){
            msg += " " + bean.getNote();
        }

        tvAge.setText(msg);
        tvName.setText(bean.getNickname());
        if(0 < bean.getCount()){
            tvDrugNum.setText(bean.getCount() + "种药品");
        }

        if("1".equals(bean.getSex())){
            Picasso.with(mContext)
                    .load(R.mipmap.icon_medicine_bag_user_man)
                    .into(imgIcon);
            return;
        }

        Picasso.with(mContext)
                .load(R.mipmap.icon_medicine_bag_user_woman)
                .into(imgIcon);

    }

    private void initRecycler() {

        mAdapter = new MedicinePersonAdapter(mContext);

        recyclerview.setAdapter(mAdapter);

        initFooter();

        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));

        recyclerview.setPullRefreshEnabled(false);

        recyclerview.setLoadingMoreEnabled(false);

        recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                requestMedicineList();
            }
        });

        mAdapter.setOnMedicinePersonListener(new MedicinePersonAdapter.MedicinePersonListener() {
            @Override
            public void onItemLong(String id, int pos) {
                showDeleteDialog(id, pos);
            }

            @Override
            public void onItem(String id) {
                requestEventCode("e004");

                bundle.putString("drug_id", id);
                openActivity(ResultsDetailActivity.class, bundle);
            }

        });
    }

    @OnClick({R.id.ib_left_01, R.id.lin_top, R.id.lin_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_left_01:
                finish();
                break;

            case R.id.lin_top:
                requestEventCode("a-4004");

                Intent intent = new Intent(mContext, AddMemberActivity.class);
                bundle.putSerializable(Contants.KEY_MEDICINE_BEAN, bean);
                bundle.putInt(Contants.KEY_MEMBER_EDIT, 1);
                intent.putExtras(bundle);
                startActivityForResult(intent, Contants.INT_1);
                break;

            case R.id.lin_add:

                requestEventCode("a-4003");

                if(!NetworkUtil.CheckConnection(this)){
                    showToast(getResources().getString(R.string.error_net_toast));
                    return;
                }

                bundle.putSerializable(Contants.KEY_MEDICINE_BEAN, bean);
                openActivity(DrugAddActivity.class, bundle);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Contants.INT_1:
                    initBundle(data);
                    break;

                case Contants.INT_2:
                    break;
            }
        }
    }

    private void showAdd(View v) {
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(v, "translationY", 100, 0);

        ObjectAnimator anim4 = ObjectAnimator.ofFloat(v, "alpha", 0, 1f);

        AnimatorSet animSet = new AnimatorSet();

        animSet.play(anim3).with(anim4);

        animSet.setDuration(300);

        animSet.start();
    }

    public void showDeleteDialog(final String id, final int pos) {
        final AlertDialog a = new AlertDialog.Builder(mContext).create();
        a.setCanceledOnTouchOutside(true);
        a.show();
        a.setContentView(R.layout.dialog_message_delete);
        TextView dialog_message_delete = (TextView) a.getWindow().findViewById(R.id.dialog_message_delete);
        dialog_message_delete.setText("删除该药品");
        dialog_message_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                position = pos;

                requestMedicineDel(id);

                if (a != null) a.dismiss();
            }
        });
    }

    /**
     * 接口-->药品列表
     */
    private void requestMedicineList() {
        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.HOME_MEDICINE_CHEST_DETAIL_LIST_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.HOME_MEDICINE_CHEST_DETAIL_LIST_SIGN);
        params.put(HttpConstant.HOME_MEDICINE_CHEST_DETAIL_LIST_MEMBERID, idStr);
        params.put(HttpConstant.HOME_MEDICINE_CHEST_DETAIL_LIST_OFFSET, String.valueOf(mData.size()));

        request(params, HttpConstant.HOME_MEDICINE_LIST);

        LogUtil.d("lxx", "317--->"+params.toString());
    }

    /**
     * 接口-->删除
     */
    private void requestMedicineDel(String id) {
        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.HOME_MEDICINE_CHEST_DEL_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.HOME_MEDICINE_CHEST_DEL_SIGN);
        params.put(HttpConstant.IDS, id);

        request(params, HttpConstant.HOME_MEDICINE_DEL);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);

        switch (what) {
            case HttpConstant.HOME_MEDICINE_LIST:

                try {
                    recyclerview.loadMoreComplete();

                    JSONObject data = response.getJSONObject(HttpConstant.DATA);

                    LogUtil.d("lxx", "176-->" + data.toString());

                    MedicineChestBean bean = new Gson().fromJson(data.toString(), MedicineChestBean.class);

                    drugNum = bean.getCount();

                    if(0 < bean.getCount()){
                        tvDrugNum.setText(bean.getCount() + "种药品");
                        recyclerview.setVisibility(View.VISIBLE);
                    } else {
                        tvDrugNum.setText("暂时还没有药品");
                        recyclerview.setVisibility(View.GONE);
                    }

                    if (0 == bean.getCount()) {
                        showEmpty("请添加一些药品吧!", R.mipmap.icon_medicine_empty_gray);
                        return;
                    }

                    if (0 == bean.getList().size()) {
                        showToast(getResources().getString(R.string.no_more_data));
                    }

                    mData.addAll(bean.getList());

                    mAdapter.updata(mData);

                    showContent();

                    recyclerview.setLoadingMoreEnabled(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case HttpConstant.HOME_MEDICINE_DEL:

                try {
                    mAdapter.updataRemove(position);

                    drugNum--;

                    if(0 < drugNum){
                        tvDrugNum.setText(drugNum + "种药品");
                    }

                    if (0 != mAdapter.getDataSize()) return;

                    mData.clear();
                    requestMedicineList();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

        }
    }

    @Override
    public void error(String errorMsg) {
        super.error(errorMsg);
        try {
            ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);

            switch (errorResponse.getWhat()) {
                case HttpConstant.HOME_MEDICINE_LIST:

                    if (errorResponse.isNetError()) {
                        showError();
                    } else {
                        showToast(errorResponse.getMsg());
                    }

                    recyclerview.loadMoreComplete();

                    break;

                case HttpConstant.HOME_MEDICINE_DEL:

                    if (errorResponse.isNetError()) {
                        showToast(getResources().getString(R.string.error_net_toast));
                    } else {
                        showToast(errorResponse.getMsg());
                    }

                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * loading
     */
    private void initFooter() {

        LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext)
                .inflate(R.layout.home_footer, null);

        moreView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ImageView imageView = (ImageView) moreView
                .findViewById(R.id.loadding_listview_image);
        imageView.setBackgroundResource(R.drawable.ybtlistview_load);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView
                .getBackground();
        View v = new View(mContext);
        v.setLayoutParams(new LinearLayout.LayoutParams(10, 50));
        moreView.addView(v);
        animationDrawable.start();

        recyclerview.setFootView(moreView);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(Contants.ACTION_NAME_MEDICINE_PERSON_REFRESH == intent.getAction()){
                init();
                return;
            }

            MedicinePersonActivity.this.finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onClickRetry() {
        super.onClickRetry();

        showLoading();
        requestMedicineList();
    }

    private static final String TAG = MedicinePersonActivity.class.getSimpleName();

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

}
