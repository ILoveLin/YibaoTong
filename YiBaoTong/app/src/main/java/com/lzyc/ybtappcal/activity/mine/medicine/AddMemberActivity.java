package com.lzyc.ybtappcal.activity.mine.medicine;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.bean.MedicineFamilyBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.ybtappcal.widget.popupwindow.PopupWindowTwoButton;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import ybt.library.dialog.ios.ActionSheetDialog;
import ybt.library.dialog.wheel.DataPickerDialog;

/**
 * 添加成员
 * Created by lxx on 2017/5/24.
 */

public class AddMemberActivity extends BaseActivity {

    @BindView(R.id.v_name_click)
    View vNameClick;
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.img_gender)
    ImageView imgGender;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_have)
    TextView tvHave;
    @BindView(R.id.tv_not_have)
    TextView tvNotHave;
    @BindView(R.id.edit_remarks)
    EditText editRemarks;
    @BindView(R.id.lin_hide)
    LinearLayout linlHide;
    @BindString(R.string.edit_limit)
    String mInputLimit;

    PopupWindowTwoButton popLoginButton;

    List<String> listAge;
    MedicineFamilyBean.ListBean bean;

    int age = 20;
    String gender;//1是“男”0是“女”
    String allergy = "0";//有无过敏史 1有0没有（默认为0

    String clz;
    String sign;
    String memberID;

    Intent intent;

    String beforeNickName;

    boolean changed;

    final String NAME = "请输入称呼";
    final String GENDER = "请选择性别";
    final String AGE = "请选择年龄";
    final String GAVE_UP = "确认放弃本次操作吗？";
    final String REMARK = "请输入备注";

    @Override
    public int getContentViewId() {
        return R.layout.activity_member;
    }

    @Override
    protected void init() {

        setTitleRightTvbtnName(getResources().getString(R.string.save));

        tvNotHave.setSelected(true);

        UID = SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "").toString();

        initBundle();

        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                beforeNickName = editName.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                changed = true;

                if (editable.toString().length() > 8) {
                    showToast(mInputLimit);
                    editName.setText(beforeNickName);
                    editName.setSelection(editName.getText().length());

                }
            }
        });

        editRemarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                changed = true;
            }
        });

    }

    private void initBundle() {
        intent=getIntent();
        if (0 != getIntent().getIntExtra(Contants.KEY_MEMBER_EDIT, 0)) {

            clz = HttpConstant.HOME_MEDICINE_CHEST_MEMBER_EDIT_CLZ;
            sign = HttpConstant.HOME_MEDICINE_CHEST_MEMBER_EDIT_SIGN;
            setTitleName(Contants.STR_TITLE_MEMBER_EDIT);

        } else {

            clz = HttpConstant.HOME_MEDICINE_CHEST_MEMBER_ADD_CLZ;
            sign = HttpConstant.HOME_MEDICINE_CHEST_MEMBER_ADD_SIGN;
            setTitleName(Contants.STR_TITLE_MEMBER_ADD);
        }

        bean = (MedicineFamilyBean.ListBean) getIntent().getSerializableExtra(Contants.KEY_MEDICINE_BEAN);

        if (null == bean) return;

        try {
            age = Integer.parseInt(bean.getAge());
        } catch (Exception e) {
            e.printStackTrace();
        }

        editName.setText(bean.getNickname());
        tvAge.setText(String.valueOf(age));
        tvGender.setText(bean.getGender());
        editRemarks.setText(bean.getNote());
        tvAge.setTextColor(getResources().getColor(R.color.color_333333));
        tvGender.setTextColor(getResources().getColor(R.color.color_333333));
        editName.setSelection(editName.getText().toString().length());
        editRemarks.setSelection(editRemarks.getText().toString().length());

        memberID = bean.getID();
        gender = bean.getSex();
        allergy = bean.getAllergy();

        if("0".equals(allergy)){
            setAllergySeleced(false, true);
        } else {
            setAllergySeleced(true, false);
        }

    }

    @Override
    public void onClickTitleRightTvBtn(View v) {
        super.onClickTitleRightTvBtn(v);

        if (TextUtils.isEmpty(editName.getText().toString())) {
            popupWindowTwoButtonNotice(editName.getHint().toString(), false);
            return;
        }

        if (NAME.equals(editName.getText().toString())) {
            popupWindowTwoButtonNotice(editName.getText().toString(), false);
            return;
        }

        if (GENDER.equals(tvGender.getText().toString())) {
            popupWindowTwoButtonNotice(tvGender.getText().toString(), false);
            return;
        }

        if (AGE.equals(tvAge.getText().toString())) {
            popupWindowTwoButtonNotice(tvAge.getText().toString(), false);
            return;
        }

        if (!changed) {
            this.finish();
            return;
        }

        requestMemberAdd();

    }

    @OnClick({R.id.tv_have, R.id.tv_not_have, R.id.tv_age, R.id.tv_gender, R.id.lin_remarks, R.id.v_name_click})
    public void onViewClicked(View view) {

        switch (view.getId()) {

            case R.id.tv_have:
                changed = true;

                setAllergySeleced(true, false);

                break;

            case R.id.tv_not_have:
                changed = true;

                setAllergySeleced(false, true);

                break;

            case R.id.tv_age:
                showDialogAge();
                break;

            case R.id.tv_gender:
                showDialogGender();
                break;

            case R.id.lin_remarks:
                showSoftKeyboard(editRemarks);
                editRemarks.setSelection(editRemarks.getText().length());
                break;

            case R.id.v_name_click:
                vNameClick.setVisibility(View.GONE);
                showSoftKeyboard(editName);
                editName.setSelection(editName.getText().length());
                break;
        }
    }

    @Override
    public void onClickTitleLeftBtn(View v) {

        if (changed) {
            popupWindowTwoButtonNotice(GAVE_UP, true);
            return;
        }

        super.onClickTitleLeftBtn(v);
    }

    @Override
    public void onBackPressed() {

        if (changed) {
            popupWindowTwoButtonNotice(GAVE_UP, true);
            return;
        }

        super.onBackPressed();
    }

    private void setAllergySeleced(boolean flag1, boolean flag2) {

        tvHave.setSelected(flag1);
        tvNotHave.setSelected(flag2);

        if(flag1){
            allergy = Contants.STR_1;
        } else {
            allergy = Contants.STR_0;
        }
    }

    /**
     * 提示框
     */
    protected void popupWindowTwoButtonNotice(String title, final boolean flag) {

        if (null == popLoginButton) {
            popLoginButton = new PopupWindowTwoButton(this);
        }

        popLoginButton.getTv_content().setText(title);
        popLoginButton.getTvOk().setText("确认");
        popLoginButton.getTvCancel().setText("取消");
        popLoginButton.getTvTitle().setVisibility(View.GONE);
        popLoginButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popLoginButton.dismiss();

                if (flag) finish();
            }
        });
        popLoginButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popLoginButton.dismiss();
            }
        });
        popLoginButton.showPopupWindow(new View(mContext), Gravity.CENTER);

    }

    /**
     * 年龄
     */
    private void showDialogAge() {

        if (0 != age) {
            age -= 1;
        }

        int position = age;
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        listAge = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            listAge.add(i + 1 + "");
        }

        builder.setTextSize(20F);
        DataPickerDialog dialog = builder.setData(listAge).setSelection(position)
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue, int position) {
                        changed = true;
                        tvAge.setTextColor(getResources().getColor(R.color.color_333333));
                        tvAge.setText(itemValue);
                        age = Integer.parseInt(itemValue);
                    }
                }).create();

        dialog.show();
    }

    /**
     * 性别
     */
    private void showDialogGender() {
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("男", ActionSheetDialog.SheetItemColor.TEXTCOLOR,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                changed = true;
                                tvGender.setTextColor(getResources().getColor(R.color.color_333333));
                                tvGender.setText("男");
                                gender = "1";
                                Picasso.with(mContext)
                                        .load(R.mipmap.icon_medicine_boy)
                                        .placeholder(R.mipmap.icon_medicine_none)
                                        .error(R.mipmap.icon_medicine_none)
                                        .into(imgGender);
                            }
                        })
                .addSheetItem("女", ActionSheetDialog.SheetItemColor.TEXTCOLOR,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                changed = true;
                                tvGender.setTextColor(getResources().getColor(R.color.color_333333));
                                tvGender.setText("女");
                                gender = "0";
                                Picasso.with(mContext)
                                        .load(R.mipmap.icon_medicine_girl)
                                        .placeholder(R.mipmap.icon_medicine_none)
                                        .error(R.mipmap.icon_medicine_none)
                                        .into(imgGender);
                            }
                        }).show();
    }

    private void deassignBean(String id) {
        if(bean==null){
            bean=new MedicineFamilyBean.ListBean();
        }
        bean.setNickname(editName.getText().toString());
        bean.setSex(gender);
        bean.setAge(tvAge.getText().toString());
        bean.setAllergy(allergy);
        bean.setID(id);
        bean.setNote(editRemarks.getText().toString());
        intent.putExtra(Contants.KEY_MEDICINE_BEAN, bean);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    if(editName.hasFocus()){
                        editName.clearFocus();
                    }

                    if(editRemarks.hasFocus()){
                        editRemarks.clearFocus();
                    }

                    vNameClick.setVisibility(View.VISIBLE);
                }
            }
            return super.dispatchTouchEvent(ev);
        }

        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
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

    /**
     * 接口
     */
    private void requestMemberAdd() {

        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, clz);
        params.put(HttpConstant.PARAM_KEY_SIGN, sign);
        params.put(HttpConstant.APP_UID, UID);
        params.put(HttpConstant.HOME_MEMBER_ADD_PARAM_NICKNAME, editName.getText().toString());
        params.put(HttpConstant.HOME_MEMBER_ADD_PARAM_AGE, tvAge.getText().toString());
        params.put(HttpConstant.HOME_MEMBER_ADD_PARAM_SEX, gender);
        params.put(HttpConstant.HOME_MEMBER_ADD_PARAM_ALLERGY, allergy);
        params.put(HttpConstant.HOME_MEMBER_ADD_PARAM_NOTE, editRemarks.getText().toString());

        if (!TextUtils.isEmpty(memberID)) {
            params.put(HttpConstant.HOME_MEDICINE_CHEST_DETAIL_LIST_MEMBERID, memberID);
        }

        request(params, HttpConstant.HOME_MEDICINE_CHEST_MEMBER_ADD);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        try {
            JSONObject joData= response.getJSONObject(HttpConstant.DATA);
            String msgStr =joData.optString("Message");
            showToast(msgStr);
            String memID=joData.optString("MemberID");

            if(!TextUtils.isEmpty(memID)){
                memberID = memID;
            }

            deassignBean(memberID);
            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.y("setResult出错"+e.getMessage());
        }
    }

    @Override
    public void error(String errorMsg) {
        super.error(errorMsg);
        try {
            ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);

            if (errorResponse.isNetError()) {
                showToast(getResources().getString(R.string.error_net_toast));
            } else {
                showToast(errorResponse.getMsg());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String TAG = AddMemberActivity.class.getSimpleName();

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
