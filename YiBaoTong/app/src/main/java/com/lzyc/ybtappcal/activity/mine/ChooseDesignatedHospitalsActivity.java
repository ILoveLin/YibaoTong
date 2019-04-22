package com.lzyc.ybtappcal.activity.mine;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.PointHospitalAdapter;
import com.lzyc.ybtappcal.adapter.PointHospitalSearchAdapter;
import com.lzyc.ybtappcal.bean.PointHospitalBean;
import com.lzyc.ybtappcal.bean.PointHospitalBean.DataBean.ListBean;
import com.lzyc.ybtappcal.bean.PointHospitalSearchBean;
import com.lzyc.ybtappcal.bean.PointHospitalSearchBean.DataBean.SearchListBean;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.KeyBoardUtils;
import com.lzyc.ybtappcal.util.MD5ChangeUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.view.ContainsEmojiEditText;
import com.lzyc.ybtappcal.view.YbtListView;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.ybtappcal.widget.popupwindow.PopupWindowTwoButton;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择定点医院
 * Created by yang on 2017/05/23.
 */
public class ChooseDesignatedHospitalsActivity extends BaseActivity {
    private static  final String TAG=ChooseDesignatedHospitalsActivity.class.getSimpleName();
    @BindView(R.id.input_edit)
    ContainsEmojiEditText inputEdit;
    @BindView(R.id.search_textview)
    TextView searchTextview;
    @BindView(R.id.tv_choose_distance)
    TextView tvChooseDistance;
    @BindView(R.id.designated_nearby_lv)
    YbtListView designatedNearbyListView;
    @BindView(R.id.designated_search_dim)
    YbtListView searchDimListView;
    @BindView(R.id.designated_linear)
    LinearLayout designatedLinear;
    @BindView(R.id.designated_search_overscroll)
    TwinklingRefreshLayout designatedSearchOverscroll;
    @BindString(R.string.title_designated_choose)
    String titleName;
    @BindView(R.id.nearby_linear)
    LinearLayout nearbyLinear;
    private PointHospitalBean dataBean;
    private String mLat;
    private String mLng;
    private ArrayList<ListBean> mResultDataList;
    private PointHospitalAdapter mResultAdapter;
    private PointHospitalSearchBean searchDataBean;
    private ArrayList<SearchListBean> mSearchDataList;
    private PointHospitalSearchAdapter mSearchAdapter;
    private String keyword;


    @Override
    public int getContentViewId() {
        return R.layout.activity_designated_choose;
    }

    @Override
    protected void init() {
        setTitleName(titleName);
        setPageStateView();
        showLoading();
        initView();
        requestDesignatedhospital();
        responseListener();
    }

    private void initView() {
        designatedSearchOverscroll.setPureScrollModeOn();
        mLat = (String) SharePreferenceUtil.get(this, "positionlat", "");
        mLng = (String) SharePreferenceUtil.get(this, "positionlng", "");
        mResultDataList = new ArrayList<ListBean>();
        mResultAdapter = new PointHospitalAdapter(this, R.layout.item_point_hospital, mResultDataList);
        designatedNearbyListView.setAdapter(mResultAdapter);
        mSearchDataList = new ArrayList<SearchListBean>();
        mSearchAdapter = new PointHospitalSearchAdapter(this, R.layout.item_search_point_hospital, mSearchDataList);
        searchDimListView.setAdapter(mSearchAdapter);
        inputEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                keyword = inputEdit.getText().toString().trim();
                mSearchAdapter.setKeyword(keyword);
                if (!(TextUtils.isEmpty(keyword))) {
                    requestDimSearch(keyword);
                } else {
                    showNearbyView();
                }
            }
        });
        inputEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = inputEdit.getText().toString();
                    if (!(TextUtils.isEmpty(keyword))) {
                        requestDimSearch(keyword);
                    } else {
                        showToast("请输入医院名称");
                    }
                    return true;
                }
                return false;
            }
        });

    }


    private void responseListener() {
        designatedNearbyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListBean bean = (ListBean) parent.getItemAtPosition(position);
                if (bean == null) {
                    return;
                }
                showSaveDialog(bean.getYyid(),bean.getName());
            }
        });

        searchDimListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchListBean searchBean = (SearchListBean) parent.getItemAtPosition(position);
                KeyBoardUtils.closeKeybord(inputEdit, ChooseDesignatedHospitalsActivity.this);
                if (searchBean == null) {
                    return;
                }
                showSaveDialog(searchBean.getYyid(),searchBean.getName());
            }
        });

        searchDimListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchListBean searchBean = (SearchListBean) parent.getItemAtPosition(position);
                KeyBoardUtils.closeKeybord(inputEdit, ChooseDesignatedHospitalsActivity.this);
                if (searchBean == null) {
                    return;
                }
                showSaveDialog(searchBean.getYyid(),searchBean.getName());
            }
        });

    }

    private void showSaveDialog(final String yyid, final String name) {
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(this);
        twoButton.getTv_content().setText("确认添加该定点医院？");
        twoButton.getTvOk().setText("确认");
        twoButton.getTvCancel().setText("取消");
        twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAddPointHospitalSearch(yyid,name);
                twoButton.dismiss();
            }
        });
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(designatedLinear, Gravity.CENTER);
    }

    @OnClick(R.id.search_textview)
    public void onViewClicked() {
        String keyword = inputEdit.getText().toString();
        if (!(TextUtils.isEmpty(keyword))) {
            requestDimSearch(keyword);
        } else {
            showToast("请输入医院名称");
        }
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

    //网络
    /**
     * 获取附近数据
     */
    private void requestDesignatedhospital() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, "Dingdian");
        params.put(HttpConstant.PARAM_KEY_CLASS, "NearbyHospital");
        String sign = MD5ChangeUtil.Md5_32("DingdianNearbyHospital" + HttpConstant.APP_SECRET);
        params.put(HttpConstant.PARAM_KEY_SIGN, sign);
        params.put(HttpConstant.PARAM_KEY_LAT, mLat);
        params.put(HttpConstant.PARAM_KEY_LNG, mLng);
        if (!NetworkUtil.CheckConnection(this)) {
            showError();
            return;
        }
        request(params, HttpConstant.DESIGNATED_NEAR_LIST);
    }

    /**
     * 联想词 请求
     *
     * @param keyword
     */
    private void requestDimSearch(String keyword) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, "Dingdian");
        params.put(HttpConstant.PARAM_KEY_CLASS, "SearchHospital");
        String sign = MD5ChangeUtil.Md5_32("DingdianSearchHospital" + HttpConstant.APP_SECRET);
        params.put(HttpConstant.PARAM_KEY_SIGN, sign);
        params.put("key", keyword);
        request(params, HttpConstant.DESIGNATED_DIM_LIST);
    }

    /**
     * 添加定点医院
     * @param yyid
     * @param name
     */
    private void requestAddPointHospitalSearch(String yyid,String name) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, "Dingdian");
        params.put(HttpConstant.PARAM_KEY_CLASS, "SetDingdian");
        String sign = MD5ChangeUtil.Md5_32("DingdianSetDingdian" + HttpConstant.APP_SECRET);
        params.put(HttpConstant.PARAM_KEY_SIGN, sign);
        params.put("yyid", yyid);
        params.put("phone", (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PHONE, ""));
        params.put("yyname",name);
        request(params, HttpConstant.DESIGNATED_ADD);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            //附近的医院
            case HttpConstant.DESIGNATED_NEAR_LIST:
                try {
                    dataBean = JsonUtil.getModle(response.toString(), PointHospitalBean.class);
                    fullInfoNearList(dataBean);
                    showContent();
                } catch (Exception e) {
                    e.printStackTrace();
                    showEmpty(errorServer, R.mipmap.empty_error_net_server);
                }
                break;
            //添加定点医院
            case HttpConstant.DESIGNATED_ADD:
                try {
                    showToast("添加成功");
                    ChooseDesignatedHospitalsActivity.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            //联想搜索列表医院
            case HttpConstant.DESIGNATED_DIM_LIST:
                try {
                    searchDataBean = JsonUtil.getModle(response.toString(), PointHospitalSearchBean.class);
                    fullDimInfo(searchDataBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 填充联想搜索列表
     *
     * @param searchDataBean
     */
    private void fullDimInfo(PointHospitalSearchBean searchDataBean) {
        ArrayList<SearchListBean> list = (ArrayList<SearchListBean>) searchDataBean.getData().getList();
        if (list == null) {
            list = new ArrayList<>();
        }
        mSearchDataList.clear();
        mSearchDataList.addAll(list);
        mSearchAdapter.notifyDataSetChanged();
        showDimView();
    }

    /**
     * 填充附近医院列表
     *
     * @param dataBean
     */
    private void fullInfoNearList(PointHospitalBean dataBean) {
        ArrayList<ListBean> list = (ArrayList<ListBean>) dataBean.getData().getList();
        if (list == null) {
            list = new ArrayList<ListBean>();
        }
        mResultDataList.clear();
        mResultDataList.addAll(list);
        mResultAdapter.notifyDataSetChanged();
        showNearbyView();
    }

    @Override
    public void error(String errorMsg) {
        try {
            ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);
            switch (errorResponse.getWhat()) {
                case HttpConstant.DESIGNATED_NEAR_LIST:
                    if (errorResponse.isNetError()) {
                        showError();
                    } else {
                        showEmpty(errorServer, R.mipmap.empty_error_net_server);
                    }
                    break;
                case HttpConstant.DESIGNATED_DIM_LIST:
                    break;
                case HttpConstant.DESIGNATED_ADD:
                    if (errorResponse.isNetError()) {
                       showToast("网络不给力");
                    } else {
                        showToast(""+errorResponse.getMsg());
                    }
                    break;
            }
        } catch (Exception e) {

        }
    }

    /**
     * 显示联想搜索
     */
    private void showDimView() {
        nearbyLinear.setVisibility(View.GONE);
        designatedNearbyListView.setVisibility(View.GONE);
        searchDimListView.setVisibility(View.VISIBLE);
    }

    /**
     * 显示附近列表
     */
    private void showNearbyView() {
        nearbyLinear.setVisibility(View.VISIBLE);
        designatedNearbyListView.setVisibility(View.VISIBLE);
        searchDimListView.setVisibility(View.GONE);
    }


    /**
     * 点击了错误重试
     */
    protected void onClickRetry() {
        showLoading();
        requestDesignatedhospital();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isClickEt(view, event)) {
                inputEdit.clearFocus();
                KeyBoardUtils.closeKeybord(view, ChooseDesignatedHospitalsActivity.this);
            }
            return super.dispatchTouchEvent(event);
        }
        if (getWindow().superDispatchTouchEvent(event)) {
            return true;
        }
        return onTouchEvent(event);
    }

    /**
     * 获取当前点击位置是否为et
     *
     * @param view  焦点所在View
     * @param event 触摸事件
     * @return
     */
    public boolean isClickEt(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] leftTop = {0, 0};
            view.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else if (event.getY() < 200 || 980 < event.getY() && event.getY() < 1060) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}
