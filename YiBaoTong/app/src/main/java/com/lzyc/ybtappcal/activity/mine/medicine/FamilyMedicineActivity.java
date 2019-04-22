package com.lzyc.ybtappcal.activity.mine.medicine;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.FamilyMedicineAdapter;
import com.lzyc.ybtappcal.bean.MedicineFamilyBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.view.xlist.XYbtRefreshListView;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 家庭药箱
 * Created by lxx on 2017/5/24.
 */

public class FamilyMedicineActivity extends BaseActivity {

    @BindView(R.id.x_listview)
    XYbtRefreshListView xListView;
    @BindView(R.id.rel_loading)
    RelativeLayout relLoading;
    @BindView(R.id.lin_add)
    LinearLayout linAdd;

    FamilyMedicineAdapter mAdapter;

    List<MedicineFamilyBean.ListBean> mData = new ArrayList<>();

    int positioin;//列表删除下标

    Bundle bundle = new Bundle();

    @Override
    public int getContentViewId() {
        return R.layout.activity_family_medicine;
    }

    @Override
    protected void init() {

        setTitleName(Contants.STR_TITLE_FAMILY_MEDICINE);
        setPageStateView(relLoading);
        showLoading();

        initListView();

        showAdd(linAdd);
    }

    private void initListView(){
        mAdapter = new FamilyMedicineAdapter(mContext, R.layout.item_family_medicine, mData);

        xListView.setAdapter(mAdapter);

        xListView.setPullLoadEnable(false);

        xListView.setPullRefreshEnable(false);

        mAdapter.setOnMedicineListener(new FamilyMedicineAdapter.MedicineListener() {
            @Override
            public void onItem(MedicineFamilyBean.ListBean bean) {

                requestEventCode("a-4002");

                bundle.putSerializable(Contants.KEY_MEDICINE_BEAN, bean);
                openActivity(MedicinePersonActivity.class, bundle);
            }
        });

        mAdapter.setOnMedicineLongListener(new FamilyMedicineAdapter.MedicineLongListener() {
            @Override
            public void onItemLong(int position, String id, MedicineFamilyBean.ListBean bean) {
                bundle.putInt(Contants.KEY_MEMBER_EDIT, 1);
                bundle.putSerializable(Contants.KEY_MEDICINE_BEAN, bean);
                showDeleteDialog(position, id);

            }
        });

    }

    @OnClick(R.id.lin_add)
    public void onViewClicked() {
        requestEventCode("a-4001");

        openActivity(AddMemberActivity.class);
    }

    public void showDeleteDialog(final int pos, final String id) {
        final AlertDialog a = new AlertDialog.Builder(mContext).create();
        a.setCanceledOnTouchOutside(true);
        a.show();
        a.setContentView(R.layout.dialog_message_delete);
        TextView dialog_message_delete = (TextView) a.getWindow().findViewById(R.id.dialog_message_delete);
        TextView dialogPopTop = (TextView) a.getWindow().findViewById(R.id.dialog_pop_top);
        TextView dialogPopBottom = (TextView) a.getWindow().findViewById(R.id.dialog_pop_bottom);
        dialogPopTop.setText("删除该药箱");
        dialogPopBottom.setText("编辑该药箱");
        dialogPopTop.setVisibility(View.VISIBLE);
        dialogPopBottom.setVisibility(View.VISIBLE);
        dialog_message_delete.setVisibility(View.GONE);

        dialogPopTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requestMemberDel(id);

                positioin = pos;

                a.dismiss();
            }
        });

        dialogPopBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openActivity(AddMemberActivity.class, bundle);

                a.dismiss();
            }
        });
    }

    private void showAdd(View v) {
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(v, "translationY", 100, 0);

        ObjectAnimator anim4 = ObjectAnimator.ofFloat(v, "alpha", 0, 1f);

        AnimatorSet animSet = new AnimatorSet();

        animSet.play(anim3).with(anim4);

        animSet.setDuration(300);

        animSet.start();
    }

    /**
     * 家庭成员列表
     */
    private void requestMemberList(){
        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.HOME_MEDICINE_CHEST_MEMBER_LIST_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.HOME_MEDICINE_CHEST_MEMBER_LIST_SIGN);
        params.put(HttpConstant.APP_UID, UID);

        request(params, HttpConstant.HOME_MEDICINE_CHEST_MEMBER_LIST);
    }

    /**
     * 删除成员
     */
    private void requestMemberDel(String id){
        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.HOME_MEDICINE_CHEST_MEMBER_DEL_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.HOME_MEDICINE_CHEST_MEMBER_DEL_SIGN);
        params.put(HttpConstant.ID, id);

        request(params, HttpConstant.HOME_MEDICINE_CHEST_MEMBER_DEL);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);

        switch (what){
            case HttpConstant.HOME_MEDICINE_CHEST_MEMBER_LIST:
                try{

                    JSONObject data = response.getJSONObject(HttpConstant.DATA);

                    MedicineFamilyBean bean = new Gson().fromJson(data.toString(), MedicineFamilyBean.class);

                    if(0 == bean.getCount()){
                        showEmpty("您还没有添加成员哦!", R.mipmap.icon_family_medicine_empty);
                        return;
                    }

                    mData.clear();

                    mData.addAll(bean.getList());

                    mAdapter.updata(mData);

                    showContent();

                } catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case HttpConstant.HOME_MEDICINE_CHEST_MEMBER_DEL:

                try{
                    mAdapter.updataRemove(positioin);

                    if(0 == mAdapter.getCount())
                        showEmpty("您还没有添加成员哦!", R.mipmap.icon_family_medicine_empty);

                } catch (Exception e){
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public void error(String errorMsg) {
        super.error(errorMsg);
        try{
            ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);

            switch (errorResponse.getWhat()){
                case HttpConstant.HOME_MEDICINE_CHEST_MEMBER_LIST:
                    if(errorResponse.isNetError()){
                        showError();
                    } else {
                        showToast(errorResponse.getMsg());
                    }
                    break;

                default:
                    if(errorResponse.isNetError()){
                        showToast(getResources().getString(R.string.error_net_toast));
                    } else {
                        showToast(errorResponse.getMsg());
                    }
                    break;
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onClickRetry() {
        super.onClickRetry();
        showLoading();
        requestMemberList();
    }

    private static final String TAG = FamilyMedicineActivity.class.getSimpleName();

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();

        requestMemberList();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }
}
