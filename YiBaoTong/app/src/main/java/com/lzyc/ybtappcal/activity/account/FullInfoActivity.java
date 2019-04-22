package com.lzyc.ybtappcal.activity.account;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.MainActivity;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.app.ActivityCollector;
import com.lzyc.ybtappcal.bean.RegionBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.FileUtil;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.MD5ChangeUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ybt.library.dialog.wheel.DataPickerDialog;
import ybt.library.dialog.wheel.RegionPickerDialog;

/**
 * 完善信息
 */
public class FullInfoActivity extends BaseActivity {
    protected static final String TAG=FullInfoActivity.class.getSimpleName();
    @BindView(R.id.linear_info)
    LinearLayout linearInfo;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.rb_info1)
    RadioButton rbInfo1;
    @BindView(R.id.rb_info2)
    RadioButton rbInfo2;
    @BindView(R.id.rb_full_info3)
    RadioButton rb_full_info3;
    @BindView(R.id.rb_full_info4)
    RadioButton rb_full_info4;
    @BindView(R.id.rb_full_info5)
    RadioButton rb_full_info5;
    @BindView(R.id.rb_full_info6)
    RadioButton rb_full_info6;
//    @BindView(R.id.ib_left_scan)
//    ImageButton ib_left;
    @BindView(R.id.tv_full_info_man)
    TextView tvsexMan;
    @BindView(R.id.tv_full_info_woman)
    TextView tvsexWoman;
    @BindView(R.id.rg_full_info)
    RadioGroup rg_full_info;

    private String phone;
    private String sex = "";
    private String ybtype = "";
    private String cityId = "";
    private String regionId = "";
    private ArrayList<String> listAge;
    private List<RegionBean> regionList;
    private String currentProvice;
    private String currentCity;


    @Override
    public int getContentViewId() {
        return R.layout.activity_full_info;
    }

    @Override
    public void init() {

        initData();
    }

    private void initData() {
//        ib_left.setVisibility(View.INVISIBLE);
        ActivityCollector.getInstance().addActivityRegiser(this);
        setTitleName("完善信息");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String json = FileUtil.readCityListJson(FullInfoActivity.this);
                    Type type = new TypeToken<ArrayList<RegionBean>>() {
                    }.getType();
                    regionList = JsonUtil.getListModle(json, "data", type);
                }catch (Exception e) {
                    LogUtil.e(TAG,"error:解析出错"+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        ).start();
        phone = getIntent().getStringExtra("phone");
        sex="1";
        rg_full_info.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_full_info_man) {
                    sex = "1";
                } else {
                    sex = "0";
                }
                requestEventCode("w002");
            }
        });
        tvAge.setText("35");
        cityId="2";
        regionId="1";
        currentProvice="北京";
        currentCity="北京市";
        tvCity.setText("北京市");
        ybtype="ZZ00";
        setType(rbInfo1);
    }



    @OnClick({R.id.tv_full_info_ok, R.id.tv_full_info_next, R.id.tv_age, R.id.tv_city,R.id.rb_info1, R.id.rb_info2, R.id.rb_full_info3, R.id.rb_full_info4, R.id.rb_full_info5, R.id.rb_full_info6, R.id.tv_full_info_man, R.id.tv_full_info_woman})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_full_info_ok:
                String age = tvAge.getText().toString();
                if (TextUtils.isEmpty(age)) {
                    showPrompt(linearInfo,"年龄不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(sex)) {
                    showPrompt(linearInfo,"性别不能为空！");
                    return;
                }

                if (TextUtils.isEmpty(ybtype)) {
                    showPrompt(linearInfo,"医保类型不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(regionId)) {
                    showPrompt(linearInfo,"省ID不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(cityId)) {
                    showPrompt(linearInfo,"省ID不能为空！");
                    return;
                }
                requestEventCode("w005");
                requestFull(age);
                break;

            case R.id.tv_full_info_next:
                requestDefaultInfo();//设置默认信息
                saveIsFirstOK();
                openActivity(MainActivity.class);
                ActivityCollector.removeRegisterAll();
                break;
            case R.id.tv_age:
                requestEventCode("w001");
                showDialogAge();
                break;
            case R.id.tv_city:
                requestEventCode("w003");
                showDialogRegion();
                break;
            case R.id.rb_info1:
                ybtype="ZZ00";
                setType(rbInfo1);
                break;
            case R.id.rb_info2:
                ybtype="ZZ03";
                setType(rbInfo2);
                break;
            case R.id.rb_full_info3:
//                setImgType(rb_full_info3);
                break;
            case R.id.rb_full_info4:
                ybtype="ZZ04";
                setType(rb_full_info4);
                break;
            case R.id.rb_full_info5:
                ybtype="ZZ02";
                setType(rb_full_info5);
                break;
            case R.id.rb_full_info6:
                ybtype="ZZ01";
                setType(rb_full_info6);
                break;
            case R.id.tv_full_info_man:
                setsex("1", tvsexMan);
                break;
            case R.id.tv_full_info_woman:
                setsex("0", tvsexWoman);
                break;
        }
    }

    public void setsex(String sex, TextView tv){

        requestEventCode("w002");

        this.sex = sex;
        tvsexMan.setSelected(false);
        tvsexWoman.setSelected(false);

        tv.setSelected(true);
    }

    public void setType(RadioButton rb) {
        requestEventCode("w004");
        rbInfo1.setChecked(false);
        rbInfo2.setChecked(false);
        rb_full_info3.setChecked(false);
        rb_full_info4.setChecked(false);
        rb_full_info5.setChecked(false);
        rb_full_info6.setChecked(false);
        rb.setChecked(true);
    }

//    /**
//     * 选择城市
//     */
//    private void initPopupCity() {
//        OptionsPopupWindow popupAge = new OptionsPopupWindow(this);
//        popupAge.setPicker(options1Items, options2Items, true);
//        popupAge.setOnoptionsSelectListener(new OptionsPopupWindow.OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int option2, int options3) {
//                regionId = regionList.get(options1).getRegion_id();
//                cityId = regionList.get(options1).getRegion().get(option2).getRegion_id();
//                tvCity.setText(""+options2Items.get(options1).get(option2));
//            }
//        });
//        popupAge.showAtLocation(tvCity, Gravity.BOTTOM, 0, 0);
//    }


//    /**
//     * 选择年龄提示
//     */
//    private void initPopupAge() {
//        OptionsPopupWindow popupAge = new OptionsPopupWindow(this);
//        listAge = new ArrayList<String>();
//        for (int i = 0; i < 100; i++) {
//            listAge.add(i +1 + "");
//        }
//
//        popupAge.setPicker(listAge);
//        popupAge.setOnoptionsSelectListener(new OptionsPopupWindow.OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int option2, int options3) {
//                tvAge.setText(listAge.get(options1));
//            }
//        });
//        String sAge=tvAge.getText().toString();
//        if(sAge.isEmpty()){
//            sAge="1";
//        }
//        int position=Integer.parseInt(sAge)-1;
//        popupAge.showAtLocation(tvAge, Gravity.BOTTOM, 0, 0);
//        popupAge.setSelectOptions(position);
//    }

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
                    public void onDataSelected(String itemValue,int position) {
                        tvAge.setText(itemValue);
                    }
                }).create();

        dialog.show();
    }
    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
//        requestRegion();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

    //    /**
//     * 获得省市
//     */
//    public void requestRegion() {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("app", "User");
//        params.put("class", "RegionList");
//        String sign = MD5ChangeUtil.Md5_32("UserRegionList" + HttpConstant.APP_SECRET);
//        params.put("sign", sign);
//        requestAccountInfo(params, HttpConstant.GET_REGION);
//    }

    /**
     * 完善信息
     */
    public void requestFull(String age) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("app", "User");
        params.put("class", "PerfectInfo");
        String sign = MD5ChangeUtil.Md5_32("UserPerfectInfo" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        params.put("phone", phone);
        params.put("nickname", phone);
        params.put("sex", sex);
        params.put("age", age);
        params.put("cityId", cityId);
        params.put("regionId", regionId);
        params.put("ybtype", ybtype);
        params.put("img", "");
        params.put("isRegist", "1");//是否是第一次注册，1是
        request(params, HttpConstant.FULL_INFO);
    }

    /**
     * 设置默认
     */
    public void requestDefaultInfo() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("app", "User");
        params.put("class", "PerfectInfo");
        String sign = MD5ChangeUtil.Md5_32("UserPerfectInfo" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        params.put("phone", phone);
        params.put("nickname", phone);
        params.put("sex", "0");
        params.put("age", "23");
        params.put("cityId", "2");
        params.put("regionId", "1");
        params.put("ybtype", "ZZ00");
        params.put("img", "");
        request(params, 0);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what){
            case HttpConstant.FULL_INFO:
                ToastUtil.showToastCenter(FullInfoActivity.this,"完善信息成功！");
                saveIsFirstOK();
                //sendBroadcastRefreshUserInfo();
                openActivity(MainActivity.class);
                ActivityCollector.removeRegisterAll();
                break;
            default:
                break;
        }
    }

    //    @Override
//    public void done(String msg, int what, JSONObject response) {
//        super.done(msg, what, response);
//        switch (what) {
//            case HttpConstant.FULL_INFO:
//                ToastUtil.showToastCenter(FullInfoActivity.this,"完善信息成功！");
//                saveIsFirstOK();
//                //sendBroadcastRefreshUserInfo();
//                openActivity(MainActivity.class);
//                ActivityCollector.removeRegisterAll();
//                break;
//            default:
//                break;
//        }
//    }
//    /**
//     * 发广播,通知个人中心,刷新数据
//     */
//    private void sendBroadcastRefreshUserInfo() {
//        Intent intent = new Intent();
//        intent.setAction(Contants.ACTION_NAME_REFRESH_MINE);
//        sendBroadcast(intent);
//    }
//    private void reload(List<RegionBean> list) {
//        if (!options1Items.isEmpty()) {
//            options1Items.clear();
//        }
//        for (int i = 0; i < list.size(); i++) {
//            RegionBean regionBean = list.get(i);
//            options1Items.add(regionBean.getRegion_name());
//            List<CityBean> cityBeanList = regionBean.getRegion();
//            ArrayList<String> arrayList = new ArrayList<String>();
//            for (int j = 0; j < cityBeanList.size(); j++) {
//                arrayList.add(cityBeanList.get(j).getRegion_name());
//            }
//            options2Items.add(arrayList);
//        }
//    }

    private void saveIsFirstOK() {
        SharePreferenceUtil.put(FullInfoActivity.this,SharePreferenceUtil.DYNAMIC_SWITCH_TAB, Contants.TAB_MINE);
        SharePreferenceUtil.put(FullInfoActivity.this,SharePreferenceUtil.IS_FIRST_REGISTER_OK,true);
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
                tvCity.setText(""+city);

            }
        }).create();
        builder.setDefaultValue(currentProvice, currentCity);
        dialog.show();
    }

    @Override
    public void error(String errorMsg) {
        super.error(errorMsg);
        if(!NetworkUtil.CheckConnection(mContext)){
            showError();
        }else{
            showToast(errorMsg);
        }
    }

    @Override
    protected void onDestroy() {
        ActivityCollector.getInstance().removeActivityRegister(this);
        super.onDestroy();
    }
}
