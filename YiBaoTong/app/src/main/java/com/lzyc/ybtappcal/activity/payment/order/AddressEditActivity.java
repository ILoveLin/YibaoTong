package com.lzyc.ybtappcal.activity.payment.order;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.bean.AddressInfo;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.KeyBoardUtils;
import com.lzyc.ybtappcal.util.SoftKeyBoardListener;
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
 * 编辑地址
 * Created by yang on 2017/03/15.
 */
public class AddressEditActivity extends BaseActivity {
    private static final String TAG=AddressEditActivity.class.getSimpleName();
    @BindView(R.id.et_account)
    FloatingEditText etAccount;
    @BindView(R.id.et_phone)
    FloatingEditText etPhone;
    @BindView(R.id.et_region)
    FloatingEditText etRegion;
    @BindView(R.id.et_detail)
    TextInputEditText etDetail;
    @BindView(R.id.linear_address)
    LinearLayout linearAddress;
    private AddressInfo addressInfo;
    @BindString(R.string.address_edit_title)
    public String titleName;
    @BindString(R.string.back_prompt_edit_address)
    public String backPrompt;
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
    private RegionPickerDialog2 dialog;
    private RegionPickerDialog2.Builder builder;
    private Handler mHandler;
    @Override
    public int getContentViewId() {
        return R.layout.activity_address_edit;
    }

    @Override
    protected void init() {
        setTitleName(titleName);
        addressInfo = (AddressInfo) getIntent().getExtras().getSerializable(Contants.KEY_OBJ_ADDRESS_INFO);
        etAccount.setText(addressInfo.getName());
//        Selection.setSelection(etAccount.getText(), addressInfo.getName().length());
        etPhone.setText(addressInfo.getPhone());
        etRegion.setText(addressInfo.getAddressRegion());
        etDetail.setText(addressInfo.getAddressDetail());
        etAccount.getEditText().setSelection(etAccount.getText().length());
//        KeyBoardUtils.openKeybord(etAccount.getEditText(), this);

        etRegion.getEditText().setInputType(InputType.TYPE_NULL);
        etRegion.getEditText().setKeyListener(null);

        SoftKeyBoardListener.setListener(AddressEditActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {

            }

            @Override
            public void keyBoardHide(int height) {

                setViewFocus(linearAddress);
            }
        });

        //yanlc 提前初始化,提升点击灵敏度
        mHandler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //主要在这句，builder（）里面进行了数据读取 耗时
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        builder = new RegionPickerDialog2.Builder(AddressEditActivity.this);
                    }
                });
            }
        }).start();

    }

    @Override
    protected void onClickRetry() {

    }

    @OnClick({R.id.ib_left, R.id.et_region, R.id.address_build_edit,R.id.address_edit_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_left://返回
                saveInfo();
                break;
            case R.id.address_edit_save://保存
                requestEditAddressInfo();
                break;
            case R.id.address_build_edit:

                setViewFocus(etRegion);

                showDialog();
                break;
        }
    }

    private void showDialog(){
        if(dialog == null){
            if(builder== null) {
                dialog =new RegionPickerDialog2.Builder(this).create();
            }
            builder.setOnRegionSelectedListener(new RegionPickerDialog2.OnTitlebarClickListener() {
                @Override
                public void onTitlebarOkClickListener(int pIndex, int cIndex, int qIndex, String provice, String city, String qu) {
                    String diqu = provice + " " + city + " " + qu;
                    etRegion.setText(diqu);
                    setViewFocus(linearAddress);
                }
            });
            dialog=builder.create();
            dialog.show();
        } else if(!dialog.isShowing()){
            dialog.show();
        }

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                setViewFocus(linearAddress);
            }
        });

    }

    private void setViewFocus(View v){
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.requestFocusFromTouch();
    }

    //关键字 弹窗

    /**
     * 是否保存的弹窗
     */
    private void popWindowSaveInfo() {
        KeyBoardUtils.closeKeybord(etAccount, this);
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(this);
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTv_content().setText(backPrompt);
        twoButton.getTvOk().setText(determine2);
        twoButton.getTvCancel().setText(cancel);
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
                AddressEditActivity.this.finish();
            }
        });
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(linearAddress, Gravity.CENTER);
    }

    /**
     * 地区
     */
    private final void showDialogRegion() {
        dialog.show();
    }

    /**
     * yanlc
     * 创建dialog
     */
    private final void createDialogRegion(){
        if(builder== null) {
            dialog =new RegionPickerDialog2.Builder(this).create();
        }
        builder.setOnRegionSelectedListener(new RegionPickerDialog2.OnTitlebarClickListener() {
            @Override
            public void onTitlebarOkClickListener(int pIndex, int cIndex, int qIndex, String provice, String city, String qu) {
                String diqu = provice + " " + city + " " + qu;
                etRegion.setText(diqu);
            }
        });
    }

    //网络请求
    /**
     * 编辑地址
     */
    private void requestEditAddressInfo() {
        String account = etAccount.getText().toString();
        if (account.isEmpty()) {
            showToast(emptyAccountName);
            return;
        }
        String phone = etPhone.getText().toString();
        if (phone.isEmpty()) {
            showToast(emptyAccount);
            return;
        }
        String diqu = etRegion.getText().toString();
        if (diqu.isEmpty()) {
            showToast(emptyRegion);
            return;
        }
        String addressDetail = etDetail.getText().toString();
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
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_ADDRESS_UPDATE_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_ADDRESS_UPDATE_SIGN);
        params.put(HttpConstant.ID, addressInfo.getID());
        params.put(HttpConstant.DEFAULT, addressInfo.getDefault());
        params.put(HttpConstant.SHOP_ADDRESS_UPDATE_NAME, account);
        params.put(HttpConstant.PARAM_KEY_PHONE2, phone);
        params.put(HttpConstant.SHOP_ADDRESS_UPDATE_REGION, diqu);
        params.put(HttpConstant.SHOP_ADDRESS_UPDATE_DETAIL, addressDetail);
        request(params, HttpConstant.ORDER_ADDRESS_EDIT);
    }


    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.ORDER_ADDRESS_EDIT:
                AddressEditActivity.this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isClickEt(view, event)) {
                KeyBoardUtils.closeKeybord(view, AddressEditActivity.this);
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


    @Override
    public void onBackPressed() {//返回
        saveInfo();
    }

    private void saveInfo() {
        if (!addressInfo.getName().equals(etAccount.getText().toString())) {
            popWindowSaveInfo();
            return;
        }
        if (!addressInfo.getPhone().equals(etPhone.getText().toString())) {
            popWindowSaveInfo();
            return;
        }
        if (!addressInfo.getAddressRegion().equals(etRegion.getText().toString())) {
            popWindowSaveInfo();
            return;
        }
        if (!addressInfo.getAddressDetail().equals(etDetail.getText().toString())) {
            popWindowSaveInfo();
            return;
        }
        AddressEditActivity.this.finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }


    @Override
    public void error(String errorMsg) {
        try{
        ErrorResponse errorResponse= JsonUtil.getModle(errorMsg,ErrorResponse.class);
        String msg=errorResponse.getMsg();
        if(!TextUtils.isEmpty(msg)){
            showToast(msg);
        }
      if(errorResponse.isNetError()){
                showToast(errorNetToast);
            }
        }catch (Exception e){

        }
    }
}
