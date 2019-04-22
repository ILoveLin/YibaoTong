package com.lzyc.ybtappcal.activity.mine.withdraw;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.StringUtils;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.ybtappcal.widget.ClearEditText;
import com.lzyc.ybtappcal.widget.popupwindow.PopupWindowWithdraw;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 余额提现
 * Created by Lxx on 2017/3/20.
 */

public class MineWithdrawActivity extends BaseActivity {
    @BindView(R.id.act_mine_wd_balance)
    TextView tvBalance;
    @BindView(R.id.activity_mine_withdraw_draw_num)
    TextView tvCount;
    @BindView(R.id.act_mine_wd_draw_balance)
    ClearEditText editBalance;
    @BindView(R.id.act_mine_wd_input_alipay)
    ClearEditText editAlipay;
    @BindView(R.id.act_mine_wd_input_aliname)
    ClearEditText editAliname;
    @BindView(R.id.act_mine_wd_submit)
    TextView tvSubmit;
    @BindView(R.id.activity_mine_withdraw_layout)
    LinearLayout linHide;
    @BindView(R.id.img_show_cache)
    ImageView imgShowCache;

    static final String NOTICE = "输入金额超过可用余额";
    static final String BALANCE_ERR = "最少可提现一元";

    int iEditBalance;

    Context mContext;

    boolean neesRefresh;//余额明细刷新

    boolean subType;

    List<AliBean> dataCache = new ArrayList<>();

    List<AliBean> dataCacheShow = new ArrayList<>();

    int position = -1;

    @Override
    public int getContentViewId() {
        return R.layout.activity_mine_withdraw;
    }

    @Override
    public void init() {
        mContext = this;
        setPageStateView();
        showLoading();

        setTitleName("余额提现");
        setTitleRightTvbtnName("提现记录");

        UID = SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "").toString();

        editAlipay.addTextChangedListener(watcher);
        editAliname.addTextChangedListener(watcher);

        editBalance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editBalance.setText(s);
                        editBalance.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editBalance.setText(s);
                    editBalance.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editBalance.setText(s.subSequence(0, 1));
                        editBalance.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    double bDalance = Double.parseDouble(tvBalance.getText().toString());
                    iEditBalance = Integer.parseInt(s.toString());

                    if (iEditBalance > bDalance) {
                        showToast(NOTICE);
                        editBalance.setText("");
                    }
                    if (StringUtils.isEmpty(editBalance.getText().toString())
                            || StringUtils.isEmpty(editAlipay.getText().toString())
                            || StringUtils.isEmpty(editAliname.getText().toString())) {
                        subType = false;
                        tvSubmit.setBackgroundResource(R.drawable.shape_bg_btn_long_withdraw_gray);
                    } else {
                        tvSubmit.setBackgroundResource(R.drawable.shape_bg_btn_long_withdraw_green);
                        subType = true;
                    }
                } catch (NumberFormatException e) {
                }
            }
        });

        showFirstCache();

        requestWithdrawCount();
    }

    private void showFirstCache(){

        if(isHaveCache(getCache())){
            if(!dataCacheShow.isEmpty()){

                imgShowCache.setVisibility(View.VISIBLE);

                AliBean bean = dataCacheShow.get(dataCacheShow.size()-1);

                editAlipay.setText(bean.getPhone());

                editAliname.setText(bean.getName());
            }
        } else {
            imgShowCache.setVisibility(View.INVISIBLE);
        }
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (StringUtils.isEmpty(editBalance.getText().toString())
                    || StringUtils.isEmpty(editAlipay.getText().toString())
                    || StringUtils.isEmpty(editAliname.getText().toString())) {
                subType = false;
                tvSubmit.setBackgroundResource(R.drawable.shape_bg_btn_long_withdraw_gray);
            } else {
                subType = true;
                tvSubmit.setBackgroundResource(R.drawable.shape_bg_btn_long_withdraw_green);
            }
        }
    };

    @OnClick({R.id.activity_mine_withdraw_layout, R.id.img_show_cache, R.id.act_mine_wd_submit})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.activity_mine_withdraw_layout:
                hideKeyBoard();
                break;

            case R.id.img_show_cache:

                hideKeyBoard();

                List<AliBean> data = getCache();

                if(isHaveCache(data))
                    showPopupWindow(dataCacheShow);

                break;

            case R.id.act_mine_wd_submit:

                requestEventCode("c013");

                if(!subType) return;

                if (1 > iEditBalance) {
                    showToast(BALANCE_ERR);
                    return;
                }

                if (!StringUtils.isMobileNum(editAlipay.getText().toString()) && !StringUtils.isEmail(editAlipay.getText().toString())) {
                    showToast("支付宝账户格式不正确！");
                    return;
                }
                requestWithdraw();

                break;
        }

    }

    @Override
    public void onClickTitleRightTvBtn(View v) {
        super.onClickTitleRightTvBtn(v);
        requestEventCode("c014");
        openActivity(MineWithdrawListActivity.class);
    }

    private boolean isHaveCache(List<AliBean> data){

        dataCacheShow.clear();

        if(null != data && !data.isEmpty()){

            for(AliBean bean : data){

                if(bean.getUserID().equals(UID)){

                    dataCacheShow.add(bean);
                }
            }
            return true;
        }

        return false;
    }

    private void showPopupWindow(List<AliBean> data){

        final PopupWindowWithdraw pop = new PopupWindowWithdraw(mContext);

        pop.updata(data);

        pop.showView(imgShowCache);

        pop.setOnWithdrawListener(new PopupWindowWithdraw.OnWithdrawListener() {
            @Override
            public void onDel(int pos) {

                pop.updataRemove(pos);

                dataCacheShow.remove(pos);

                position = pos;

                saveCache(false);

                position = -1;

                if(null == dataCacheShow || dataCacheShow.isEmpty()){
                    pop.dismissView();
                    imgShowCache.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onSelected(int pos) {

                AliBean bean = dataCache.get(pos);

                editAlipay.setText(bean.getPhone());

                editAliname.setText(bean.getName());

                pop.dismissView();
            }
        });
    }

    private void save(){
        for(AliBean bean : dataCache){
            if(bean.getUserID().equals(UID) && bean.getPhone().equals(editAlipay.getText().toString())){
                dataCache.remove(bean);
                return;
            }
        }
    }

    private void saveCache(boolean isSave){

        StringBuilder sbr = new StringBuilder();

        if(isSave){

            save();

            AliBean bean = new AliBean();
            bean.setUserID(UID);
            bean.setPhone(editAlipay.getText().toString());
            bean.setName(editAliname.getText().toString());

            dataCache.add(bean);

        } else {

            if(-1 == position || dataCache.isEmpty())  return;

            dataCache.remove(position);
        }

        for(AliBean bean : dataCache){
            sbr.append(bean.getUserID() + "," + bean.getPhone() + "," +bean.getName() + ";");
        }

        SharePreferenceUtil.put(mContext, SharePreferenceUtil.KEY_ALI_INFO, sbr.toString());
    }

    private List<AliBean> getCache(){

        String cache = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.KEY_ALI_INFO, "");

        if(!dataCache.isEmpty()){
            dataCache.clear();
        }

        if(TextUtils.isEmpty(cache)) return null;

        String[] strCache = cache.split(";");

        for(String str : strCache){

            String[] strs = str.split(",");

            AliBean bean = new AliBean();

            bean.setUserID(strs[0]);
            bean.setPhone(strs[1]);
            bean.setName(strs[2]);

            dataCache.add(bean);
        }

        return dataCache;
    }

    public void requestWithdrawCount() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_UCENTER);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.MINE_WITHDRAW_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.MINE_WITHDRAW_SIGN);
        params.put(HttpConstant.APP_UID, UID);

        if (!NetworkUtil.CheckConnection(mContext)) {
            showError();
            mTitleRightTvBtn.setVisibility(View.GONE);
            return;
        }

        request(params, HttpConstant.WHTHDRAW_RETURN);
    }

    public void requestWithdraw() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.PERSON_WITHDRAW_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.PERSON_WITHDRAW_SIGN);
        params.put(HttpConstant.APP_UID, UID);
        params.put(HttpConstant.PERSON_WITHDRAW_BALANCE, editBalance.getText().toString());
        params.put(HttpConstant.PERSON_WITHDRAW_ALIPAY, editAlipay.getText().toString());
        params.put(HttpConstant.PERSON_WITHDRAW_ALINAME, editAliname.getText().toString());

        if (!NetworkUtil.CheckConnection(mContext)) {
            showToast("网络不给力");
            return;
        }

        request(params, HttpConstant.WHTHDRAW);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.WHTHDRAW_RETURN:
                try {
                    int count = response.getJSONObject(HttpConstant.DATA).optInt(HttpConstant.MINE_WITHDRAW_PARAM_WITHDRAWCOUNT);
                    String balance = response.getJSONObject(HttpConstant.DATA).optString(HttpConstant.MINE_WITHDRAW_CLZ_SMALL);

                    tvBalance.setText(balance);

                    if (0 >= count) {
                        tvCount.setVisibility(View.GONE);
                    } else {
                        tvCount.setText("您有" + count + "笔提现处理中");
                        tvCount.setVisibility(View.VISIBLE);
                    }

                    mTitleRightTvBtn.setVisibility(View.VISIBLE);

                    showContent();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case HttpConstant.WHTHDRAW:
                try {
                    JSONObject data = response.getJSONObject(HttpConstant.DATA);
                    String msgToast = data.getString(HttpConstant.MSG);
                    showToast(msgToast);
                    double drawBalance = Double.parseDouble(editBalance.getText().toString());
                    double balance = Double.parseDouble(tvBalance.getText().toString());
                    tvBalance.setText(new DecimalFormat("0.00").format(balance - drawBalance));

                    saveCache(true);

                    imgShowCache.setVisibility(View.VISIBLE);

                    editBalance.setText("");
                    editAlipay.setText("");
                    editAliname.setText("");

                    subType = false;

                    requestWithdrawCount();

                    neesRefresh = true;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onClickRetry() {
        showLoading();
        requestWithdrawCount();
    }

    @Override
    public void error(String errorMsg) {
        super.error(errorMsg);
        try{
            ErrorResponse errorResponse = new ErrorResponse();
            if(errorResponse.isNetError()){
                showError();
            } else {
                showToast(errorResponse.getMsg());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClickTitleLeftBtn(View v) {

        if(neesRefresh){
            setResult(RESULT_OK);
        }
        super.onClickTitleLeftBtn(v);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(!isSoftShowing() && neesRefresh){
            setResult(RESULT_OK);
        }

        return super.onKeyDown(keyCode, event);
    }

    private void hideKeyBoard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInput(linHide, InputMethodManager.SHOW_FORCED);

        imm.hideSoftInputFromWindow(linHide.getWindowToken(), 0);
    }

    /**
     * 键盘是否显示
     */
    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return screenHeight - rect.bottom - getSoftButtonsBarHeight() != 0;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    public class AliBean implements Serializable {

        String userID;
        String phone;
        String name;

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "AliBean{" +
                    "userID='" + userID + '\'' +
                    ", phone='" + phone + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    private static final String TAG = MineWithdrawActivity.class.getSimpleName();

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
