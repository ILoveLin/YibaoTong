package com.lzyc.ybtappcal.activity.mine;

import android.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.ChoosePointHospitalAdapter;
import com.lzyc.ybtappcal.bean.ChoosePointBean;
import com.lzyc.ybtappcal.bean.ChoosePointBean.DataBean.ListBean;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.MD5ChangeUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * 定点医院
 * Created by yang on 2017/05/24.
 */
public class DesignatedHospitalsActivity extends BaseActivity {
    private static final String TAG=DesignatedHospitalsActivity.class.getSimpleName();
    @BindView(R.id.designated_notice)
    TextView designatedNotice;
    @BindView(R.id.designated_listview)
    ListView designatedListview;
    @BindString(R.string.empty_designated_list)
    String emptyDesignatedList;
    @BindString(R.string.title_designated)
    String titleName;

    private ArrayList<ListBean> mDatas;
    private ChoosePointHospitalAdapter mAdapter;
    private ListBean bean;

    @Override
    public int getContentViewId() {
        return R.layout.activity_designated;
    }

    @Override
    protected void init() {
        setTitleName(titleName);
        setTitleRightTvbtnName("添加");
        setPageStateView(designatedListview);
        mDatas = new ArrayList<ListBean>();
        mAdapter = new ChoosePointHospitalAdapter(this, R.layout.item_designated, mDatas);
        designatedListview.setAdapter(mAdapter);
        designatedListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (ListBean) parent.getItemAtPosition(position);
                if (bean != null) {
                    showDeleteDialog(bean);
                }
                return true;
            }
        });
    }

    /**
     * title 右侧tv按钮点击事件
     *
     * @param v
     */
    @OnClick(R.id.tv_right)
    public void onClickTitleRightTvBtn(View v) {
        openActivityNoAnim(ChooseDesignatedHospitalsActivity.class);
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        showLoading();
        requestChoosePointHospitals();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }
    /**
     * 刪除异地方案
     *
     * @param
     */
    public void showDeleteDialog(final ListBean bean) {
        final AlertDialog a = new AlertDialog.Builder(this).create();
        a.setCanceledOnTouchOutside(true);
        a.show();
        a.getWindow().setContentView(R.layout.dialog_message_delete);
        TextView dialog_message_delete = (TextView) a.getWindow().findViewById(R.id.dialog_message_delete);
        dialog_message_delete.setText("删除该定点医院");
        dialog_message_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (a != null) {
                    a.dismiss();
                }
                requestDeletePointHospitals(bean);
            }
        });
    }

    /**
     * 删除定点医院
     *
     * @param bean
     */
    private void requestDeletePointHospitals(ListBean bean) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, "Dingdian");
        params.put(HttpConstant.PARAM_KEY_CLASS, "DelDingdian");
        String sign = MD5ChangeUtil.Md5_32("DingdianDelDingdian" + HttpConstant.APP_SECRET);
        params.put(HttpConstant.PARAM_KEY_SIGN, sign);
        params.put("hostops_id", bean.getHostops_id());
        request(params, HttpConstant.DESIGNATED_DELETE);
    }

    /**
     * 已经选择的定点医院
     */
    private void requestChoosePointHospitals() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, "Dingdian");
        params.put(HttpConstant.PARAM_KEY_CLASS, "GetDingdian");
        String sign = MD5ChangeUtil.Md5_32("DingdianGetDingdian" + HttpConstant.APP_SECRET);
        params.put(HttpConstant.PARAM_KEY_SIGN, sign);
        params.put("phone", (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PHONE, ""));
        request(params, HttpConstant.DESIGNATED_LIST);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.DESIGNATED_LIST:
                try {
                    ChoosePointBean chooseBean = JsonUtil.getModle(response.toString(), ChoosePointBean.class);
                    fullInfo(chooseBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case HttpConstant.DESIGNATED_DELETE:
                try {
                    mDatas.remove(bean);
                    if (mDatas.isEmpty()) {
                        designatedNotice.setVisibility(View.VISIBLE);
                        showEmpty(emptyDesignatedList,R.mipmap.image_empty_designated);
                    } else {
                        designatedNotice.setVisibility(View.GONE);
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();


                }
                break;
        }
    }


    private void fullInfo(ChoosePointBean chooseBean) {
        ArrayList<ListBean> list = (ArrayList<ListBean>) chooseBean.getData().getList();
        if (list == null) {
            list = new ArrayList<ListBean>();
        }
        mDatas.clear();
        if (list.size()==0) {
            showEmpty(emptyDesignatedList,R.mipmap.image_empty_designated);
            designatedNotice.setVisibility(View.VISIBLE);
        } else {
            mDatas.addAll(list);
            showContent();
            designatedNotice.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 点击了错误重试
     */
    protected void  onClickRetry(){
        showLoading();
        requestChoosePointHospitals();
    }

    @Override
    public void error(String errorMsg) {
       try{
           ErrorResponse errorResponse=JsonUtil.getModle(errorMsg,ErrorResponse.class);
           switch (errorResponse.getWhat()){
               case HttpConstant.DESIGNATED_LIST:
                   if(errorResponse.isNetError()){
                       showError();
                   }else{
                       designatedNotice.setVisibility(View.VISIBLE);
                       showEmpty(emptyDesignatedList,R.mipmap.image_empty_designated);
                   }
                   break;
               case HttpConstant.DESIGNATED_DELETE:
                   if(errorResponse.isNetError()){
                       showToast(errorNetToast);
                   }else{
                       showToast(errorResponse.getMsg());
                   }
                break;
           }

       }catch (Exception e){

       }
    }
}
