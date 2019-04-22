package com.lzyc.ybtappcal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.activity.reimbursement.ReimbursementDetailsActivity;
import com.lzyc.ybtappcal.activity.reimbursement.ReimbursementNoYbDetailsActivity;
import com.lzyc.ybtappcal.activity.reimbursement.ReimbursementYbDetailsActivity;
import com.lzyc.ybtappcal.activity.top.DrugNoDetailActivity;
import com.lzyc.ybtappcal.activity.top.ResultsActivity;
import com.lzyc.ybtappcal.bean.DrugBean;
import com.lzyc.ybtappcal.bean.HistoryCache;
import com.lzyc.ybtappcal.bean.MedicineFamilyBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.MD5ChangeUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.cache.SPCacheList;
import com.lzyc.ybtappcal.view.dialog.LoadingProgressBar;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.ybtappcal.widget.ClearEditTextView;
import com.lzyc.ybtappcal.widget.popupwindow.PopupWindowTwoButton;
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
 * 扫描帮助
 * @author yang
 */
public class ScanHelpActivity extends BaseActivity {
    private static final String TAG = ScanHelpActivity.class.getSimpleName();
    @BindView(R.id.scancode_help_etinput)
    ClearEditTextView scancode_help_etinput;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    private int typePage;
    private String resultCode;
    private String cityYbChoose;
    private LoadingProgressBar mLoadingProgressBar;
    private MedicineFamilyBean.ListBean bean;
    private String memberId;
    private int what;

    @Override
    public int getContentViewId() {
        return R.layout.activity_scan_help;
    }

    @Override
    public void init() {
        mLoadingProgressBar = new LoadingProgressBar(this);
        String title = getResources().getString(R.string.title_activity_scancode_help);
        setTitleName(title);
        typePage = getIntent().getExtras().getInt(Contants.KEY_PAGE_SEARCH);
        if (typePage == Contants.VAL_PAGE_SEARCH_DRUG_ADD) {
            bean = (MedicineFamilyBean.ListBean) getIntent().getExtras().getSerializable(Contants.KEY_MEDICINE_BEAN);
            memberId = bean.getID();
        }
        String locationCity = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.CITY, "");
        cityYbChoose = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.CITY_CANBAO, locationCity);
    }

//    //动态代理 漏斗二
//    private void initClick() {
//        tvSearch.setOnClickListener(new OnClickListenerHandler(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        }, this, FunnelContants.FUNNEL2_SCAN_START).create());}


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

    @OnClick({R.id.ib_left,R.id.tv_search})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.tv_search){
            requestEventCode("k001");
            String code = scancode_help_etinput.getText().toString().trim();
            requestDrug(code);
        }else{
            finish();
            overridePendingTransition(0,0);
        }
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        mLoadingProgressBar.dismiss();
        HistoryCache historyScan = new HistoryCache();
        switch (what) {
            case HttpConstant.SEARCH_DRUG_LIST:
                try {
                    JSONObject data = response.getJSONObject(HttpConstant.DATA);
                    int count = data.getInt(HttpConstant.COUNT);
                    Type typeD = new TypeToken<ArrayList<DrugBean>>() {
                    }.getType();
                    List<DrugBean> sList = JsonUtil.getListModle(data.toString(), HttpConstant.INFO, typeD);
                    DrugBean drugBean = sList.get(0);
                    switch (count) {
                        case 0://这里不存入历史
                            switchScancodeErrorActivity();
                            break;
                        case 1:
                            if(typePage==Contants.VAL_PAGE_SEARCH_DRUG_ADD){
                                if (null != drugBean) {
                                    if (drugBean.getMedicineChest().equals(Contants.STR_1)) {
                                        showPrompt(tvSearch, "该药品已添加到药箱!");
                                    } else {
                                        popWindowDrugBagAdd(drugBean.getDrugID());
                                    }
                                } else {
                                    showPrompt(tvSearch, "很抱歉，找不到本条形码的药品");
                                }
                            }else{
                                historyScan.setCode(resultCode);
                                historyScan.setImageUrl(drugBean.getImage());
                                historyScan.setName(drugBean.getName());
                                historyScan.setGoodsName(drugBean.getGoodsName());
                                switchActivity(drugBean);
                            }
                            break;
                        default:
                            if (drugBean != null) {
                                historyScan.setCode(resultCode);
                                historyScan.setImageUrl(drugBean.getImage());
                                historyScan.setName(drugBean.getName());
                                historyScan.setGoodsName(drugBean.getGoodsName());
                            }
                            switchSearchActivity();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                historyScan.setCacheType(HistoryCache.TYPE_CACHE_SCAN);
                SPCacheList.getInstance().writeDrugScanHistory(historyScan);
                break;
            case HttpConstant.MINE_DURG_BAG_ADD:
                showToast("添加成功");
                ScanHelpActivity.this.finish();
                break;
            default:
                break;
        }
    }

    private void switchActivity(DrugBean drugBean) {
        if (drugBean == null) {
            drugBean = new DrugBean();
        }
        int type = drugBean.getType();
        if (type == 0) {
            requestEventCode("a011");//扫码后出推荐
            switch (typePage) {
                case Contants.VAL_PAGE_SEARCH_DURUG:
                    switchResultsActivity(drugBean);
                    break;
                case Contants.VAL_PAGE_SEARCH_REIMBURSEMENT: //从异地报销跳转到搜索页面
                    switchReimbursementDetailsActivity(drugBean);
                    break;
                default:
                    break;
            }
        } else if (type == 1) {
            switchDrugNoDetailActivity(drugBean);
        } else {
            LogUtil.e(TAG, "error,default switchResultActivity");
        }
    }

    /**
     * 报销查询结果界面
     *
     * @param drugBean
     */
    private void switchReimbursementDetailsActivity(DrugBean drugBean) {
        Intent intent = new Intent(this, ReimbursementDetailsActivity.class);
        intent.putExtra(Contants.KEY_OBJ_DRUGBEAN, drugBean);
        intent.putExtra(Contants.KEY_PAGE_SEARCH, typePage);
        intent.putExtra(Contants.KEY_PAGE, Contants.VAL_PAGE_SAOMA);
        startActivity(intent);
    }


    /**
     * 首页切换到搜索结果页
     *
     * @param drugBean
     */
    private void switchResultsActivity(DrugBean drugBean) {
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(Contants.KEY_OBJ_DRUGBEAN, drugBean);
//        bundle.putInt(Contants.KEY_PAGE_SEARCH, typePage);
//        bundle.putInt(Contants.KEY_PAGE, Contants.VAL_PAGE_SAOMA);
//        openActivity(ResultsActivity.class, bundle);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Contants.KEY_OBJ_DRUGBEAN, drugBean);
        bundle.putInt(Contants.KEY_PAGE_RESULTS, typePage);
        openActivity(ResultsActivity.class, bundle);
    }


    /**
     * 搜索药品
     */
    public void requestDrug(String code) {
        if (code.isEmpty()) {
            showToast("请输入有效的条形码");
            return;
        }
        mLoadingProgressBar.show();
        resultCode = code;
        switch (typePage) {
            case Contants.VAL_PAGE_SEARCH_DURUG:
            case Contants.VAL_PAGE_SEARCH_REIMBURSEMENT:
            case Contants.VAL_PAGE_SEARCH_DRUG_ADD:
                requestLocalDrug(code);
                break;
            default:
                mLoadingProgressBar.dismiss();
                break;
        }
    }



    /**
     * 扫码
     *
     * @param code
     */
    private void requestLocalDrug(String code) {
        String currentProvince = "";
        switch (typePage) {
            case Contants.VAL_PAGE_SEARCH_DRUG_ADD:
            case Contants.VAL_PAGE_SEARCH_DURUG:
                currentProvince = (String) SharePreferenceUtil.get(ScanHelpActivity.this, SharePreferenceUtil.PROVICE_TOP_CHOOSE, "北京");
                break;
            case Contants.VAL_PAGE_SEARCH_REIMBURSEMENT:
                currentProvince = (String) SharePreferenceUtil.get(ScanHelpActivity.this, SharePreferenceUtil.PROVICE_JIUZHEN, "北京");
                break;
            default:
                mLoadingProgressBar.dismiss();
                break;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.HOME_SEARCH_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.HOME_SEARCH_SIGN);
        params.put(HttpConstant.PARAM_KEY_PROVINCE2, currentProvince);
        if (typePage == Contants.VAL_PAGE_SEARCH_DRUG_ADD) {
            params.put(HttpConstant.MEMBERID, memberId);
        }
        params.put(HttpConstant.PARAM_KEY_CODE2, code);
        params.put(HttpConstant.PARAM_KEY_KEYWORD2, "");
        params.put(HttpConstant.PARAM_KEY_PAGEINDEX2,   "0");
        request(params, HttpConstant.SEARCH_DRUG_LIST);

    }
    /**
     * 异地扫码
     */
    public void requestYidiDrug(String code) {
        String cityValue = (String) SharePreferenceUtil.get(ScanHelpActivity.this, SharePreferenceUtil.PROVINCE_CANBAO, "");
        if (cityValue.equals("北京市")) {
            cityValue = "北京";
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("app", "Yidi");
        params.put("class", "ScanDrugsSearch");
        String sign = MD5ChangeUtil.Md5_32("YidiScanDrugsSearch" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        params.put("Yyid", "");
        params.put("Ybcity", cityValue);
        params.put("code", code);
        params.put("pageIndex", "0");
        params.put("phone", (String) SharePreferenceUtil.get(ScanHelpActivity.this, SharePreferenceUtil.PHONE, ""));
        request(params, HttpConstant.REMOTE_DRUGS_SEARCH);
    }


    private void switchDrugNoDetailActivity(DrugBean drugBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Contants.KEY_OBJ_DRUGBEAN, drugBean);
        openActivity(DrugNoDetailActivity.class, bundle);
    }


    private void switchSearchActivity() {
        requestEventCode("a003");
        Bundle mBundle = new Bundle();
        mBundle.putString(Contants.KEY_STR_KEYWORD_CODE, resultCode);
        mBundle.putInt(Contants.KEY_PAGE_SEARCH, Contants.VAL_PAGE_SEARCH_SCAN);
        openActivity(SearchActivity.class, mBundle);
    }

    @Override
    public void error(String errorMsg) {
        mLoadingProgressBar.dismiss();
        if (typePage == Contants.VAL_PAGE_SEARCH_DRUG_ADD) {
            if (what == HttpConstant.MINE_DURG_BAG_ADD) {
                ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);
                if (!TextUtils.isEmpty(errorResponse.getMsg())) {
                    showPrompt(tvSearch, errorResponse.getMsg());
                } else {
                    showPrompt(tvSearch, "很抱歉，找不到本条形码的药品");
                }
            } else {
                showPrompt(tvSearch, errorMsg);
            }
        } else {
            switchScancodeErrorActivity();
        }
    }

    private void switchScancodeErrorActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("resultcode", resultCode);
        bundle.putInt(Contants.KEY_PAGE_SEARCH, typePage);
        openActivity(ScancodeErrorActivity.class, bundle);
    }

    private void switchScanResultActivity(DrugBean drugBean) {
        if (drugBean != null) {
            int type = drugBean.getType();
            if (type == 0) {
//                Intent intent = new Intent(ScanHelpActivity.this, ScanResultActivity.class);
//                intent.putExtra("drug_id", drugBean.getDrug_id());
//                intent.putExtra("drugNameID", "" + drugBean.getDrugNameID());
//                intent.putExtra("venderID", "" + drugBean.getVenderID());
//                intent.putExtra("specificationsID", "" + drugBean.getSpecificationsID());
//                intent.putExtra(Contants.KEY_PAGE,Contants.VAL_PAGE_SAOMA);
//                startActivity(intent);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Contants.KEY_OBJ_DRUGBEAN, drugBean);
                bundle.putInt(Contants.KEY_PAGE_SEARCH, typePage);
                bundle.putInt(Contants.KEY_PAGE, Contants.VAL_PAGE_SAOMA);
                openActivity(ResultsActivity.class, bundle);
            } else if (type == 1) {
                switchDrugNoDetailActivity(drugBean);
            }
        }

    }

    /**
     * 异地报销切换到搜索结果页
     *
     * @param drugBean
     */
    private void switchReimbursementActivity(DrugBean drugBean) {
        if (drugBean == null) {
            LogUtil.e(TAG, "error:switchReimbursementActivity，drugBean=null.");
            switchScancodeErrorActivity();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(Contants.KEY_OBJ_DRUGBEAN, drugBean);
        bundle.putInt(Contants.KEY_PAGE_DRUG, Contants.VAL_PAGE_SEARCH_REIMBURSEMENT);
        if (cityYbChoose.equals("北京市")) {
            openActivity(ReimbursementYbDetailsActivity.class, bundle);
        } else {
            openActivity(ReimbursementNoYbDetailsActivity.class, bundle);
        }

    }

    /**
     * 添加用药
     */
    private void requestDrugAdd(String drugId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.HOME_DRUG_ADD_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.HOME_DRUG_ADD_SIGN);
        params.put(HttpConstant.MEMBERID, memberId);
        params.put(HttpConstant.DRUGID, drugId);
        this.what = HttpConstant.MINE_DURG_BAG_ADD;
        request(params, HttpConstant.MINE_DURG_BAG_ADD);
    }


    /**
     * 是否加入药箱
     */
    private void popWindowDrugBagAdd(final String drugId) {
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(this);
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTv_content().setText("确认加入药箱？");
        twoButton.getTvOk().setText("确认");
        twoButton.getTvCancel().setText("取消");
        twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDrugAdd(drugId);
                twoButton.dismiss();
            }
        });
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(tvSearch, Gravity.CENTER);
    }

    /**
     * 弹窗，知道了
     *
     * @param v
     * @param msg
     */
    public void showPrompt(View v, String msg) {
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(this);
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTv_content().setText(msg);
        twoButton.getTvOk().setVisibility(View.GONE);
        twoButton.getLineTwo().setVisibility(View.GONE);
        twoButton.getTvCancel().setText(Contants.STR_GOT_IT);
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanHelpActivity.this.finish();
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(v, Gravity.CENTER);
    }


}