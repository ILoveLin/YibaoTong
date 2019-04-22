package com.lzyc.ybtappcal.activity.payment.order;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.KeyBoardUtils;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.SoftKeyBoardListener;
import com.lzyc.ybtappcal.view.SwitchButtom;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.ybtappcal.widget.FloatingEditText;
import com.lzyc.ybtappcal.widget.popupwindow.PopupWindowTwoButton;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import ybt.library.dialog.wheel.RegionPickerDialog2;

/**
 * 新建地址
 * Created by yang on 2017/03/15.
 */
public class AddressBuildActivity extends BaseActivity {
    private static final String TAG = AddressBuildActivity.class.getSimpleName();
    public static final int ADDRESS_BUILD_SUCCESS = 1;
    public static final int ADDRESS_BUILD_FAILED = 0;
    @BindView(R.id.et_account)
    FloatingEditText tvAccount;
    @BindView(R.id.et_phone)
    FloatingEditText etPhone;
    @BindView(R.id.et_diqu)
    FloatingEditText etDiQu;
    @BindView(R.id.et_address_detail)
    TextInputEditText etAddRessDetail;
    @BindView(R.id.line_setdefault)
    View lineSetDefault;
    @BindView(R.id.sb_setdefault)
    SwitchButtom sbSetdefault;
    @BindView(R.id.linear_setdefault)
    LinearLayout linearSetDefault;
    @BindView(R.id.linear_address_build)
    LinearLayout linearAddressBuild;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindString(R.string.empty_account_name)
    public String emptyAccountName;
    @BindString(R.string.empty_account)
    public String emptyAccount;
    @BindString(R.string.empty_phone)
    public String emptyPhone;
    @BindString(R.string.error_phone)
    public String errorPhone;
    @BindString(R.string.empty_region)
    public String emptyRegion;
    @BindString(R.string.error_address_detail)
    public String errorAddressDetail;
    private String diqu;
    private String defaultSet = "0";
    private int size;
    private int typePage;
    private Intent intent;
    private RegionPickerDialog2 dialog;
    private RegionPickerDialog2.Builder builder;
    private Handler mHandler;
    private int buildStatus;//创建状态

    private boolean isFromOrder;

    @Override
    public int getContentViewId() {
        return R.layout.activity_address_build;
    }

    @Override
    protected void init() {
        setTitleName("新建地址");
        buildStatus = ADDRESS_BUILD_FAILED;//默认地址创建失败
        intent = getIntent();
        size = intent.getExtras().getInt("size");
        isFromOrder = intent.getExtras().getBoolean("order");
        typePage = intent.getExtras().getInt(Contants.KEY_PAGE_ADDRESS_BUILD);
        KeyBoardUtils.openKeybord(tvAccount.getEditText(), this);

        etDiQu.getEditText().setInputType(InputType.TYPE_NULL);

        if (size == 0) {

            if (isFromOrder) {
                tvSave.setText("保存并使用");
            }

            linearSetDefault.setVisibility(View.GONE);
            lineSetDefault.setVisibility(View.GONE);
            defaultSet = "1";
        } else {
            linearSetDefault.setVisibility(View.VISIBLE);
            lineSetDefault.setVisibility(View.VISIBLE);
            defaultSet = "0";
        }

        sbSetdefault.setColor(getResources().getColor(R.color.toggle_color_slelected), getResources().getColor(R.color.color_ffffff));
        if (defaultSet.equals("1")) {
            sbSetdefault.setOpened(true);
        } else {
            sbSetdefault.setOpened(false);
        }
        sbSetdefault.setOnStateChangedListener(new SwitchButtom.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchButtom view) {
                defaultSet = "1";
                sbSetdefault.toggleSwitch(true);
            }

            @Override
            public void toggleToOff(SwitchButtom view) {
                defaultSet = "0";
                sbSetdefault.toggleSwitch(false);
            }
        });

        SoftKeyBoardListener.setListener(AddressBuildActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {

            }

            @Override
            public void keyBoardHide(int height) {
                setViewFocus(linearAddressBuild);
            }
        });

        mHandler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        builder = new RegionPickerDialog2.Builder(AddressBuildActivity.this);
                    }
                });
            }
        }).start();

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

    /**
     * 弹出软键盘
     */
    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @OnClick({R.id.ib_left, R.id.tv_save, R.id.address_build_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_left:
                saveInfo();
                break;
            case R.id.tv_save:
                requestBuildAddress();
                break;
            case R.id.address_build_add:

                setViewFocus(etDiQu);

                showDialog();
                break;

        }
    }

    private void showDialog() {
        if (dialog == null) {
            if (builder == null) {
                dialog = new RegionPickerDialog2.Builder(this).create();
            }
            builder.setOnRegionSelectedListener(new RegionPickerDialog2.OnTitlebarClickListener() {
                @Override
                public void onTitlebarOkClickListener(int pIndex, int cIndex, int qIndex, String provice, String city, String qu) {
                    diqu = provice + " " + city + " " + qu;
                    etDiQu.setText(diqu);
                    setViewFocus(linearAddressBuild);
                }
            });
            dialog = builder.create();
            dialog.show();
        } else if (!dialog.isShowing()) {
            dialog.show();
        }

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                setViewFocus(linearAddressBuild);
            }
        });
    }

    private void setViewFocus(View v) {
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.requestFocusFromTouch();
    }

//网络请求

    /**
     * 添加地址
     */
    private void requestBuildAddress() {
        String account = tvAccount.getText().toString().trim();
        if (account.isEmpty()) {
            showToast(emptyAccountName);
            return;
        }

        String phone = etPhone.getText().toString().trim();
        if (phone.isEmpty()) {
            showToast(emptyAccount);
            return;
        }
        if (TextUtils.isEmpty(diqu)) {
            showToast(emptyRegion);
            return;
        }
        String addressDetail = etAddRessDetail.getText().toString().trim();
        if (addressDetail.length() < 5) {
            showToast(errorAddressDetail);
            return;
        }
        if (phone.isEmpty()) {
            showToast(emptyPhone);
            return;
        }
        if (phone.length() <= 6 || phone.length() > 11) {
            showToast(errorPhone);
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_ADDRESS_ADD_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_ADDRESS_ADD_SIGN);
        params.put(HttpConstant.PARAM_KEY_UID, SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "").toString());
        params.put(HttpConstant.PARAM_KEY_NAME, account);
        params.put(HttpConstant.PARAM_KEY_PHONE2, phone);
        params.put(HttpConstant.PARAM_KEY_ADDRESS_REGION, diqu);
        params.put(HttpConstant.PARAM_KEY_ADDRESS_DETAIL, addressDetail);
        params.put(HttpConstant.PARAM_KEY_ADDRESS_DEFAULT, defaultSet);
        request(params, HttpConstant.ORDER_ADDRESS_ADD);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.ORDER_ADDRESS_ADD:
                if (isFromOrder && size == 0) {
                    showToast("地址添加成功并使用");
                } else {
                    showToast("地址添加成功");
                }
                buildStatus = ADDRESS_BUILD_SUCCESS;
                closeActivity();
                break;
            default:
                break;
        }
    }

    private void closeActivity() {
        if (typePage != Contants.VAL_PAGE_ADDRESSMANAGER) {
            intent.putExtra(Contants.KEY_ADDRESS_ACTION, buildStatus);
            setResult(RESULT_OK, intent);
        }
        AddressBuildActivity.this.finish();
    }

    /**
     * 获取当前点击位置是否为et
     *
     * @param v     焦点所在View
     * @param event 触摸事件
     * @return
     */
    public boolean isClickEt(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isClickEt(v, ev)) {
                KeyBoardUtils.closeKeybord(v, AddressBuildActivity.this);
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        KeyBoardUtils.closeKeybord(tvAccount, this);
        saveInfo();
    }

    private void saveInfo() {
        if (!tvAccount.getText().toString().isEmpty()) {
            popWindowSaveInfo();
            return;
        }
        if (!etPhone.getText().toString().isEmpty()) {
            popWindowSaveInfo();
            return;
        }
        if (!etDiQu.getText().toString().isEmpty()) {
            popWindowSaveInfo();
            return;
        }
        if (!etAddRessDetail.getText().toString().isEmpty()) {
            popWindowSaveInfo();
            return;
        }
        if (typePage != Contants.VAL_PAGE_ADDRESSMANAGER) {
            closeActivity();
        }
        AddressBuildActivity.this.finish();
    }

    /**
     * 是否保存的弹窗
     */
    private void popWindowSaveInfo() {
        KeyBoardUtils.closeKeybord(tvAccount, this);
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(this);
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTv_content().setText("信息还未保存，确认现在返回？");
        twoButton.getTvOk().setText("确认");
        twoButton.getTvCancel().setText("取消");
        twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
                AddressBuildActivity.this.finish();
            }
        });
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressBuildActivity.this.finish();
                twoButton.dismiss();
            }
        });

        twoButton.showPopupWindow(linearAddressBuild, Gravity.CENTER);
    }

    @Override
    public void error(String errorMsg) {
        try {
            ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);
            String msg = errorResponse.getMsg();
            if (errorResponse.isNetError()) {
                showToast("网络不给力");
            }
            if (!TextUtils.isEmpty(msg)) {
                showToast(msg);
            }
        } catch (Exception e) {

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

}
