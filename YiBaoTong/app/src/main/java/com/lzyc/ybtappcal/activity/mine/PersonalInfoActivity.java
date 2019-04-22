package com.lzyc.ybtappcal.activity.mine;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.EditSetActivity;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.bean.MyInfoBean;
import com.lzyc.ybtappcal.bean.RegionBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.constant.YBConstant;
import com.lzyc.ybtappcal.util.FileUtil;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.StringUtils;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.lzyc.ybtappcal.view.roundimageview.RoundedImageView;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.ybtappcal.widget.popupwindow.BasePopupWindow;
import com.lzyc.ybtappcal.widget.popupwindow.PopupWindowTwoButton;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import ybt.library.dialog.ios.ActionSheetDialog;
import ybt.library.dialog.wheel.DataPickerDialog;
import ybt.library.dialog.wheel.RegionPickerDialog;

/**
 * 个人资料
 * @author yang
 */
public class PersonalInfoActivity extends BaseActivity {
    protected static String TAG = PersonalInfoActivity.class.getSimpleName();
    private static final String IMAGE_FILE_NAME = "face.jpg";
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESIZE_REQUEST_CODE = 2;
    private static final int REQ_RESULT_EDITE_SET = 3;
    @BindView(R.id.iv_avatar)
    RoundedImageView ivAvatar;
    @BindView(R.id.et_nickname)
    TextView etNickname;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_person_sex)
    TextView tv_person_sex;
    @BindView(R.id.linear_sex)
    LinearLayout linearSex;
    @BindView(R.id.tv_yibao_type)
    TextView tvYibaoType;
    @BindView(R.id.tv_canbao_city)
    TextView tvCanBaoCity;
    @BindView(R.id.linear_age)
    LinearLayout linearAge;
    @BindView(R.id.person_linear)
    LinearLayout personLinear;
    @BindView(R.id.tv_bind_change)
    TextView tvBindChange;
    @BindView(R.id.linear_dingdian)
    LinearLayout linearDingdian;
    @BindString(R.string.title_persion_info)
    String titleName;
    private ArrayList<String> listAge;
    private ArrayList<String> listHealth;
    private boolean isFirstIn = true;
    private List<RegionBean> regionList;
    private String phone;
    private String nickname = "";
    private String age = "";
    private String sex = "";
    private String ybtype = "";
    private String cityId = "";
    private String regionId = "";
    private File file;
    private MyInfoBean data;
    private ArrayList<String> listSex;
    private String currentCity;
    private String currentProvice;
    private boolean isRefreshPhone = false;

    @Override
    public int getContentViewId() {
        return R.layout.activity_personal_info;
    }

    @Override
    public void init() {
        setTitleName(titleName);
        initData();
    }

    private void initData() {
        data = (MyInfoBean) getIntent().getSerializableExtra(Contants.KEY_OBJ_MINE_INFO);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String json = FileUtil.readCityListJson(PersonalInfoActivity.this);
                    Type type = new TypeToken<ArrayList<RegionBean>>() {
                    }.getType();
                    regionList = JsonUtil.getListModle(json, HttpConstant.DATA, type);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();
        currentProvice = data.getRegionName();
        currentCity = data.getCityName();
        regionId = data.getRegionId();
        cityId = data.getCityId();
        listHealth = new ArrayList<String>();
        listSex = new ArrayList<String>();
        listHealth.addAll(Arrays.asList(YBConstant.YBNAME));
        listSex.addAll(Arrays.asList((YBConstant.SEXLOVE)));
        phone = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PHONE, "");
        fillInfo(data);
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

    private void fillInfo(MyInfoBean data) {
        if (data == null) {
            return;
        }
        nickname = data.getNickname();
        sex = data.getSex();
        age = data.getAge();
        ybtype = data.getYbtype();
        regionId = data.getRegionId();
        cityId = data.getCityId();
        String img = data.getImg();
        if (!TextUtils.isEmpty(img)) {
            Picasso.with(this)
                    .load(img)
                    .placeholder(R.mipmap.image_cache_drug_list)
                    .error(R.mipmap.image_cache_drug_list)
                    .into(ivAvatar);
        } else {
            ivAvatar.setImageResource(R.mipmap.icon_system_logo);
        }
        etNickname.setText(nickname);
        tvAge.setText(age);
        String substring = phone.substring(0, 3);
        String substring1 = phone.substring(7, phone.length());
        tvBindChange.setText("" + substring + "****" + substring1);
        if (sex.equals("0")) {
            tv_person_sex.setText("女");
        } else {
            tv_person_sex.setText("男");
        }
        for (int i = 0; i < YBConstant.YBTYPE.length; i++) {
            if (YBConstant.YBTYPE[i].equals(ybtype)) {
                tvYibaoType.setText(listHealth.get(i));
                break;
            }
        }
        String canbaoCity = data.getCityName();
        tvCanBaoCity.setText("" + canbaoCity);
        if (canbaoCity.equals("北京市")) {
            linearDingdian.setVisibility(View.VISIBLE);
        } else {
            linearDingdian.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.tv_dingdian, R.id.tv_update_pwd, R.id.tv_bind_change,
            R.id.ib_left, R.id.et_nickname, R.id.iv_avatar, R.id.linear_sex,
            R.id.linear_age, R.id.tv_yibao_type, R.id.tv_canbao_city})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_dingdian:
                if (isFirstIn) {
                    isFirstIn = false;
                    SharePreferenceUtil.put(PersonalInfoActivity.this, SharePreferenceUtil.CLICK_AGAIN, "yes");
                    SharePreferenceUtil.put(PersonalInfoActivity.this, SharePreferenceUtil.CLICK, "yes");
                }
                openActivity(DesignatedHospitalsActivity.class);
                break;
            case R.id.tv_update_pwd:
                requestEventCode("s007");
                openActivity(ChangePasswordActivity.class);
                break;
            case R.id.tv_bind_change://绑定新手机号
                requestEventCode("s009");
                isRefreshPhone = true;
                openActivity(BindPhoneActivity.class);
                break;
            case R.id.ib_left:
                nickname = etNickname.getText().toString();
                age = tvAge.getText().toString();
                String position = tvCanBaoCity.getText().toString();

                if (TextUtils.isEmpty(nickname)) {
//                    showToast("昵称不能为空！");
                    showPrompt(etNickname, "昵称不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(sex)) {
                    showPrompt(etNickname, "性别不能为空！");
//                    showToast("性别不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(age)) {
//                    showToast("年龄不能为空！");
                    showPrompt(etNickname, "年龄不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(ybtype)) {
//                    showToast("医保类型不能为空！");
                    showPrompt(etNickname, "医保类型不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(position)) {
//                    showToast("医保城市不能为空！");
                    showPrompt(etNickname, "医保城市不能为空！");
                    return;
                }
                if (file != null) {
                    initSavePopup();
                    return;
                }

                if (!data.getNickname().equals(nickname)) {
                    initSavePopup();
                    return;
                }
                if (!data.getAge().equals(age)) {
                    initSavePopup();
                    return;
                }
                if (!data.getSex().equals(sex)) {   //性别
                    initSavePopup();
                    return;
                }

                if (!data.getYbtype().equals(ybtype)) {
                    initSavePopup();
                    return;
                }
                if (!data.getCityId().equals(cityId)) {
                    initSavePopup();
                    return;
                }
                if (!data.getRegionId().equals(regionId)) {
                    initSavePopup();
                    return;
                }
                finish();
                break;
            case R.id.et_nickname:
                Intent intent = new Intent(PersonalInfoActivity.this, EditSetActivity.class);
                intent.putExtra(EditSetActivity.KEY_OBJ_TITLE, "昵称");
                intent.putExtra(EditSetActivity.KEY_OBJ_CONTENT, "" + etNickname.getText().toString());
                startActivityForResult(intent, REQ_RESULT_EDITE_SET);
                requestEventCode("s002");

                break;
            case R.id.iv_avatar:
                requestEventCode("s001");
                showDialogAvatar();
                break;
            case R.id.linear_age:
                requestEventCode("s003");
                showDialogAge();
                break;
            case R.id.linear_sex:
                requestEventCode("s004");
                showDialogSexLove();
                break;
            case R.id.tv_yibao_type:
                requestEventCode("s005");
                showDialogYbType();
                break;
            case R.id.tv_canbao_city:
                requestEventCode("s006");
                showDialogRegion();
                break;
            default:
                super.onClickTitleLeftBtn(view);
                break;
        }
    }


    /**
     * 医保城市
     */
    private final void showDialogRegion() {
        RegionPickerDialog.Builder builder = new RegionPickerDialog.Builder(this);
        RegionPickerDialog dialog = builder.setOnRegionSelectedListener(new RegionPickerDialog.OnTitlebarClickListener() {
            @Override
            public void onTitlebarOkClickListener(int pIndex, int cIndex, String provice, String city) {
                currentProvice = provice;
                currentCity = city;
                regionId = regionList.get(pIndex).getRegion_id();
                cityId = regionList.get(pIndex).getRegion().get(cIndex).getRegion_id();
                if (currentCity.equals("北京市")) {
                    linearDingdian.setVisibility(View.VISIBLE);
                } else {
                    linearDingdian.setVisibility(View.GONE);
                }
                tvCanBaoCity.setText("" + currentCity);
            }
        }).create();
        builder.setDefaultValue(currentProvice, currentCity);
        dialog.show();
    }

    /**
     * 性别
     */
    private void showDialogSexLove() {
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("男", ActionSheetDialog.SheetItemColor.TEXTCOLOR,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                sex = "1";
                                tv_person_sex.setText("男");
                            }
                        })
                .addSheetItem("女", ActionSheetDialog.SheetItemColor.TEXTCOLOR,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                sex = "0";
                                tv_person_sex.setText("女");
                            }
                        }).show();
    }

    /**
     * 头像
     */
    private void showDialogAvatar() {
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.TEXTCOLOR,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                boolean isTemp = requestPermissionCamera();
                                if (isTemp) {
                                    try {
                                        toCamera();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    requestPermissionCamera();
                                }
                            }
                        })
                .addSheetItem("从相册选择", ActionSheetDialog.SheetItemColor.TEXTCOLOR,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                boolean isTemp = requestPermissionRead();
                                if (isTemp) {
                                    try {
                                        toAlbum();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    requestPermissionRead();
                                }

                            }
                        }).show();
    }

    /**
     * 选择头像提示
     */
    private void initPopupAvator() {
        final BasePopupWindow popup = new BasePopupWindow(this, R.layout.popup_pic, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View conentView = popup.getConentView();
        //相册
        conentView.findViewById(R.id.tv_popup_pic_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    toAlbum();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                popup.dismiss();
            }
        });
        //拍照
        conentView.findViewById(R.id.tv_popup_pic_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    toCamera();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                popup.dismiss();
            }
        });
        //取消
        conentView.findViewById(R.id.tv_popup_pic_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
        popup.showPopupWindow(ivAvatar, Gravity.BOTTOM);
    }


    /**
     * 拍照
     */
    protected void toCamera() throws Exception {
        try {
            Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 判断存储卡是否可以用，可用进行存储
            if (FileUtil.isSDCardEnable()) {
                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,

                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
            }
            startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 从相册获取
     */
    protected void toAlbum() throws Exception {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }


    /**
     * 年龄
     */
    private void showDialogAge() {
        String sAge = tvAge.getText().toString();
        int position = Integer.parseInt(sAge) - 1;
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        listAge = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            listAge.add(i + 1 + "");
        }
        builder.setTextSize(20F);
        DataPickerDialog dialog = builder.setData(listAge).setSelection(position)
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue, int position) {
                        tvAge.setText(itemValue);
                    }
                }).create();

        dialog.show();
    }

    /**
     * 医保类型
     */
    private void showDialogYbType() {
        int position = 0;
        String currentYbType = tvYibaoType.getText().toString();
        for (int i = 0; i < listHealth.size(); i++) {
            String ybType = listHealth.get(i);
            if (ybType.equals(currentYbType)) {
                position = i;
                break;
            }
        }
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        DataPickerDialog dialog = builder.setData(listHealth).setSelection(position)
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue, int position) {
                        ybtype = YBConstant.YBTYPE[position];
                        tvYibaoType.setText(itemValue);
                    }
                }).create();

        dialog.show();
    }

    private void initSavePopup() {
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(this);
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTv_content().setText("确认更改信息？");
        twoButton.getTvOk().setText("确认");
        twoButton.getTvCancel().setText("取消");
        twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestEventCode("s012");
                requestFull();
                twoButton.dismiss();
            }
        });
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestEventCode("s013");
                twoButton.dismiss();
                finish();
            }
        });

        twoButton.showPopupWindow(ivAvatar, Gravity.CENTER);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case REQ_RESULT_EDITE_SET:
                    String text = data.getStringExtra(EditSetActivity.KEY_OBJ_CONTENT);
                    etNickname.setText("" + text);
                    break;
                case IMAGE_REQUEST_CODE:
                    String filePath = "$$";
                    Uri contentUri = data.getData();
                    int sdkInt = Build.VERSION.SDK_INT; //兼容4.4
                    if (sdkInt >= 19 && DocumentsContract.isDocumentUri(PersonalInfoActivity.this, contentUri)) {
                        String wholeID = DocumentsContract
                                .getDocumentId(contentUri);
                        String id = wholeID.split(":")[1];
                        String[] column = {MediaStore.Images.Media.DATA};
                        String sel = MediaStore.Images.Media._ID + "=?";
                        Cursor cursor = PersonalInfoActivity.this.getContentResolver().query(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                column, sel, new String[]{id}, null);
                        int columnIndex = cursor.getColumnIndex(column[0]);
                        if (cursor.moveToFirst()) {
                            filePath = cursor.getString(columnIndex);
                        }
                        cursor.close();
                    } else {
                        if (!TextUtils.isEmpty(contentUri.getAuthority())) {
                            Cursor cursor = getContentResolver().query(contentUri,
                                    new String[]{MediaStore.Images.Media.DATA},
                                    null, null, null);
                            if (null == cursor) {
                                showToast("图片没找到");
                            }
                            cursor.moveToFirst();
                            filePath = cursor.getString(cursor
                                    .getColumnIndex(MediaStore.Images.Media.DATA));
                            cursor.close();
                        } else {
                            filePath = data.getData().getPath();
                        }
                    }
                    Uri uri = Uri.fromFile(new File(filePath));
                    try {
                        resizeImage(uri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case CAMERA_REQUEST_CODE:
                    if (FileUtil.isSDCardEnable()) {
                        File file = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
                        try {
                            resizeImage(Uri.fromFile(file));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ToastUtil.showToastCenter(PersonalInfoActivity.this, "未找到存储卡，无法存储照片！");
                    }
                    break;
                case RESIZE_REQUEST_CODE:
                    try {
                        if (data != null) {
                            showResizeImage(data);
                        }
                    } catch (Exception e) {

                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void resizeImage(Uri uri) throws Exception {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESIZE_REQUEST_CODE);
    }

    private void showResizeImage(Intent data) throws Exception {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            file = FileUtil.saveBitmapToFile(photo, Environment.getExternalStorageDirectory().toString(), IMAGE_FILE_NAME);
            LogUtil.y(file.getPath());
            Drawable drawable = new BitmapDrawable(photo);
            ivAvatar.setImageDrawable(drawable);
        }
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        phone = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PHONE, "");
        if (isRefreshPhone) {
            isRefreshPhone = false;
            fillInfo(data);
        }

    }

    /**
     * 完善信息
     */
    public void requestFull() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_USER);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.USER_PERFECTINFO_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.USER_PERFECTINFO_SIGN);
        params.put(HttpConstant.PARAM_KEY_PHONE, phone);
        params.put(HttpConstant.PARAM_KEY_NICKNAME, nickname);
        params.put(HttpConstant.PARAM_KEY_SEX, sex);
        params.put(HttpConstant.PARAM_KEY_AGE, age);
        params.put(HttpConstant.PARAM_KEY_CITYID, cityId);
        params.put(HttpConstant.PARAM_KEY_REGIONID, regionId);
        params.put(HttpConstant.PARAM_KEY_YBTYPE, ybtype);
        if (file != null) {
            params.put(HttpConstant.PARAM_KEY_IMG, file);
        }
        requestFile(IMAGE_FILE_NAME, file, params, HttpConstant.FULL_INFO);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.FULL_INFO:
                showToast("设置成功");
                SharePreferenceUtil.put(PersonalInfoActivity.this, SharePreferenceUtil.KEY_OBJ_TYPE_JOB, ybtype);
                if (!TextUtils.isEmpty(currentCity)) {
                    SharePreferenceUtil.put(PersonalInfoActivity.this, SharePreferenceUtil.CITY_CANBAO, currentCity);
                    SharePreferenceUtil.put(PersonalInfoActivity.this, SharePreferenceUtil.PROVINCE_CANBAO, StringUtils.getProvice(currentProvice));
                } else {
                    SharePreferenceUtil.put(PersonalInfoActivity.this, SharePreferenceUtil.CITY_CANBAO, "");
                    SharePreferenceUtil.put(PersonalInfoActivity.this, SharePreferenceUtil.PROVINCE_CANBAO, "");
                }
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        nickname = etNickname.getText().toString();
        age = tvAge.getText().toString();
        String position = tvCanBaoCity.getText().toString();
        if (TextUtils.isEmpty(nickname)) {
            showPrompt(etNickname, "昵称不能为空！");
            return;
        }
        if (TextUtils.isEmpty(sex)) {
            showPrompt(etNickname, "性别不能为空！");
            return;
        }
        if (TextUtils.isEmpty(age)) {
            showPrompt(etNickname, "年龄不能为空！");
            return;
        }
        if (TextUtils.isEmpty(ybtype)) {
            showPrompt(etNickname, "医保类型不能为空！");
            return;
        }
        if (TextUtils.isEmpty(position)) {
            showPrompt(etNickname, "医保城市不能为空！");
            return;
        }
        if (file != null) {
            initSavePopup();
            return;
        }
        if (!data.getNickname().equals(nickname)) {
            initSavePopup();
            return;
        }
        if (!data.getAge().equals(age)) {
            initSavePopup();
            return;
        }
        if (!data.getSex().equals(sex)) {
            initSavePopup();
            return;
        }
        if (!data.getYbtype().equals(ybtype)) {
            initSavePopup();
            return;
        }
        if (!data.getCityId().equals(cityId)) {
            initSavePopup();
            return;
        }
        if (!data.getRegionId().equals(regionId)) {
            initSavePopup();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void error(String errorMsg) {
        LogUtil.y("###报错信息###" + errorMsg);
        try {
            ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);
            switch (errorResponse.getWhat()) {
                case HttpConstant.FULL_INFO:
                    popSaveError(personLinear, "信息更改失败");
                    break;
            }
        } catch (Exception e) {

        }

    }

    /**
     * 弹窗，确定
     *
     * @param v
     * @param msg
     */
    public void popSaveError(View v, String msg) {
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(this);
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTv_content().setText(msg);
        twoButton.getTvOk().setVisibility(View.GONE);
        twoButton.getLineTwo().setVisibility(View.GONE);
        twoButton.getTvCancel().setText("确认");
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonalInfoActivity.this.finish();
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(v, Gravity.CENTER);
    }

    @AfterPermissionGranted(Contants.REQUEST_PERMISSION_CAMERA)
    public boolean requestPermissionCamera() {//相机
        boolean isTemp = false;
        String perms[] = {Manifest.permission.CAMERA, Manifest.permission.VIBRATE, Manifest.permission.FLASHLIGHT};
        if (!EasyPermissions.hasPermissions(mContext, perms)) {
            EasyPermissions.requestPermissions(this, "医保通申请相机权限", Contants.REQUEST_PERMISSION_CAMERA, perms);
        } else {
            isTemp = true;
        }
        return isTemp;
    }

    @AfterPermissionGranted(Contants.REQUEST_PERMISSION_READ)
    public boolean requestPermissionRead() {//访问SD卡
        boolean isTemp = false;
        String perms[] = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(mContext, perms)) {
            EasyPermissions.requestPermissions(this, "医保通申请访问SD卡权限", Contants.REQUEST_PERMISSION_READ, perms);
        } else {
            isTemp = true;
        }
        return isTemp;
    }

}
