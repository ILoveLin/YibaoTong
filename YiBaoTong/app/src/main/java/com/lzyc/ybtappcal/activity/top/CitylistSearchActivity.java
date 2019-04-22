package com.lzyc.ybtappcal.activity.top;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.reflect.TypeToken;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.SearchCityHeaderAdapter;
import com.lzyc.ybtappcal.adapter.SearchCityLXAdapter;
import com.lzyc.ybtappcal.adapter.WenziAdapter;
import com.lzyc.ybtappcal.app.App;
import com.lzyc.ybtappcal.bean.CharacterParser;
import com.lzyc.ybtappcal.bean.City;
import com.lzyc.ybtappcal.bean.County;
import com.lzyc.ybtappcal.bean.HistoryCache;
import com.lzyc.ybtappcal.bean.HotCitys;
import com.lzyc.ybtappcal.bean.ProvinceBean;
import com.lzyc.ybtappcal.bean.WenziBean;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.service.LocationService;
import com.lzyc.ybtappcal.util.FileUtil;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.KeyBoardUtils;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.PinYinUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.StringUtils;
import com.lzyc.ybtappcal.util.cache.SPCacheList;
import com.lzyc.ybtappcal.view.NoScrollGridView;
import com.lzyc.ybtappcal.view.RightBar;
import com.lzyc.ybtappcal.widget.ClearEditTextView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 搜索城市，定位城市，选择城市
 * Created by yang on 2017/01/09.
 */
public class CitylistSearchActivity extends BaseActivity {

    private static final String TAG = CitylistSearchActivity.class.getSimpleName();

    @BindView(R.id.listview)
    ListView mListView;
    @BindView(R.id.right_bar)
    RightBar rightBar;
    @BindView(R.id.et_search)
    ClearEditTextView etSearch;
    @BindView(R.id.tv_alphabet)
    TextView tvAlphabet;
    @BindView(R.id.listview_dim)
    ListView dimListView;
    @BindView(R.id.rel_content)
    RelativeLayout relContent;
    @BindString(R.string.empty_city)
    String emptyCity;

    private List<String> listString, listStringSub;
    private ArrayList<String> erJiCitytList;
    private List<ProvinceBean> regionList;
    private NoScrollGridView headerGvHistory, headerGvHot;//历史访问城市，热门城市
    private SearchCityHeaderAdapter hotAdapter, historyAdapter;
    private List<WenziBean> listHot, listHotHistory;//热门城市,历史城市
    private LinearLayout headerLinearHistory, headerLinearDingwei;//历史访问城市父布局,定位的
    private WenziAdapter wenziAdapter;
    private ArrayList<WenziBean> cityList, dimList;
    private SearchCityLXAdapter searchCityAdapter;
    private CharacterParser characterParser;
    private LocationService locationService;
    private TextView headerTvCity;
    private Map<String, String> cityToProvince;
    private int i = 1;
    private String oldText = "";//记住上行字母，解决字母滑动重复的问题

    private MyLocationListener mListener = new MyLocationListener();

    @Override
    public int getContentViewId() {
        return R.layout.activity_search_citylist;
    }

    private Handler mHandler = new Handler();

    @Override
    public void init() {
        setTitleBarVisibility(View.GONE);
        setPageStateView(relContent);
        showLoading();
        initView();
        requestHotCitys();
        initEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationService = App.getInstance().locationService;
        locationService.registerListener(mListener);
    }

    private void loaddingUI() {
        hotAdapter = new SearchCityHeaderAdapter(CitylistSearchActivity.this, R.layout.item_wenzi_citylist, listHot);
        headerGvHot.setAdapter(hotAdapter);
        cityList = dataList();
        wenziAdapter = new WenziAdapter(this, cityList);
        mListView.setAdapter(wenziAdapter);
        headerGvHot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                WenziBean hotBean = (WenziBean) adapterView.getItemAtPosition(position);
                HistoryCache historyCache = new HistoryCache();
                historyCache.setName(hotBean.getName());
                historyCache.setCacheType(HistoryCache.TYPE_CACHE_CITY);
                SPCacheList.getInstance().writeCityHistory(historyCache);
                SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.CITY_TOP_CHOOSE, hotBean.getName());
                KeyBoardUtils.closeKeybord(etSearch, CitylistSearchActivity.this);
                String name = hotBean.getName();
                if (name.contains("市")) {
                    name = name.substring(0, name.length() - 1);
                }
                String chosedCity = findProinceByCity(name);
                SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.PROVICE_TOP_CHOOSE, chosedCity);
                finish();
            }
        });
        headerGvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                WenziBean hotBean = (WenziBean) adapterView.getItemAtPosition(position);
                HistoryCache historyCache = new HistoryCache();
                historyCache.setName(hotBean.getName());
                historyCache.setCacheType(HistoryCache.TYPE_CACHE_CITY);
                SPCacheList.getInstance().writeCityHistory(historyCache);
                SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.CITY_TOP_CHOOSE, hotBean.getName());
                KeyBoardUtils.closeKeybord(etSearch, CitylistSearchActivity.this);
                String name = hotBean.getName();
                if (name.contains("市")) {
                    name = name.substring(0, name.length() - 1);
                }
                String chosedCity = findProinceByCity(name);
                SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.PROVICE_TOP_CHOOSE, chosedCity);
                finish();
            }
        });
        readHistory();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                WenziBean wb = (WenziBean) adapterView.getItemAtPosition(position);
                if (wb != null) {
                    String name = wb.getName();
                    String chossedProvince = wb.getProvinceName();
                    SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.CITY_TOP_CHOOSE, name);
                    if (chossedProvince.contains("市")) {
                        chossedProvince = chossedProvince.substring(0, chossedProvince.length() - 1);
                    }
                    SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.PROVICE_TOP_CHOOSE, chossedProvince);
                    HistoryCache historyCache = new HistoryCache();
                    historyCache.setName(name);
                    historyCache.setCacheType(HistoryCache.TYPE_CACHE_CITY);
                    SPCacheList.getInstance().writeCityHistory(historyCache);
                    finish();
                }

            }
        });
    }

    private void readHistory() {
        List<HistoryCache> listHistory = SPCacheList.getInstance().readCityHistory();
        if (listHistory.size() == 0) {
            headerLinearHistory.setVisibility(View.GONE);
        } else {
            for (HistoryCache hs : listHistory) {
                WenziBean hb = new WenziBean(hs.getName());
                listHotHistory.add(hb);
            }
            historyAdapter = new SearchCityHeaderAdapter(CitylistSearchActivity.this, R.layout.item_wenzi_citylist, listHotHistory);
            headerGvHistory.setAdapter(historyAdapter);
            headerLinearHistory.setVisibility(View.VISIBLE);
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

    protected void requestHotCitys() {
        HashMap<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_WAP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.WAP_HOTCITY_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.WAP_HOTCITY_SIGN);
        request(params, HttpConstant.HOT_CITY);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.HOT_CITY:
                try {
                    HotCitys hotCitys = JsonUtil.getModle(response.toString(), HotCitys.class);
                    List<HotCitys.DataBean.CityListBean> CitysList = hotCitys.getData().getCityList();
                    follInfoHotCitys(CitysList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    private void follInfoHotCitys(List<HotCitys.DataBean.CityListBean> citysList) {
        if (citysList == null) {
            citysList = new ArrayList<>();
        }
        if (listHot.isEmpty()) {
            for (int i = 0; i < citysList.size(); i++) {
                WenziBean wz = new WenziBean(citysList.get(i).getCity());
                listHot.add(wz);
            }
        }
        hotAdapter.notifyDataSetChanged();
    }

    private void initView() {
        View headView = View.inflate(CitylistSearchActivity.this, R.layout.header_search_citylist, null);
        headerGvHistory = (NoScrollGridView) headView.findViewById(R.id.gv_history);
        headerLinearHistory = (LinearLayout) headView.findViewById(R.id.linear_history);
        headerLinearDingwei = (LinearLayout) headView.findViewById(R.id.linear_dingwei);
        headerTvCity = (TextView) headView.findViewById(R.id.tv_city);
        headerGvHot = (NoScrollGridView) headView.findViewById(R.id.gv_hot);
        mListView.addHeaderView(headView, null, false);
        characterParser = CharacterParser.getInstance();
        initData();
        dimList = new ArrayList<>();  //联想搜索列表
        searchCityAdapter = new SearchCityLXAdapter(this, R.layout.item_city_searchlx, dimList);
        dimListView.setAdapter(searchCityAdapter);
        headerLinearDingwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (headerTvCity.getText().equals("定位失败，请点击重试")) {
                    showToast("定位中...");
                    dingwei();
                } else if (headerTvCity.getText().equals("定位中...")) {
                    showToast("定位中，稍后...");
                } else {
                    String provice = (String) SharePreferenceUtil.get(CitylistSearchActivity.this, SharePreferenceUtil.PROVINCE, "");
                    String city = (String) SharePreferenceUtil.get(CitylistSearchActivity.this, SharePreferenceUtil.CITY, "");
                    HistoryCache historyCache = new HistoryCache();
                    historyCache.setName(city);
                    historyCache.setCacheType(HistoryCache.TYPE_CACHE_CITY);
                    SPCacheList.getInstance().writeCityHistory(historyCache);
                    SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.CITY_TOP_CHOOSE, city);
                    if (provice.contains("市")) {
                        provice = provice.substring(0, provice.length() - 1);
                    }
                    SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.PROVICE_TOP_CHOOSE, provice);
                    finish();
                }
            }
        });

    }

    private void dingwei() {
        if (!NetworkUtil.CheckConnection(mContext)) {
            showNetDialog();
            return;
        }
        locationService.start();
    }

    private void initEvent() {
        rightBar.setOnTouchLetterListener(new RightBar.OnTouchLetterListener() {
            @Override
            public void onTouchLetter(String letter) {
                KeyBoardUtils.closeKeybord(etSearch, CitylistSearchActivity.this);
                for (int i = 0; i < cityList.size(); i++) {
                    String firstAlphabet = cityList.get(i).getPinyin().charAt(0) + "";
                    if (letter.equals(firstAlphabet)) {
                        int position = i;
                        if (position >= 1) {
                            position = position + 1;
                        }
                        mListView.setSelection(position);
                        break;
                    }
                }
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 1) {
                    tvAlphabet.setVisibility(View.VISIBLE);
                    if (cityList.size() > 0) {
                        int pos = mListView.getFirstVisiblePosition() - 1;
                        String text = cityList.get(pos + 1).getPinyin().charAt(0) + "";
                        Pattern p = Pattern.compile("[0-9]*");
                        Matcher m1 = p.matcher(text);
                        if (m1.matches()) {
                            tvAlphabet.setText("#");
                        } else {
                            String text2 = cityList.get(pos).getPinyin().charAt(0) + "";
                            tvAlphabet.setText(text);
                            if (oldText.equals(text2)) {
                                tvAlphabet.setText(text2);
                            }
                            oldText = text2;
                        }
                    } else {
                        tvAlphabet.setVisibility(View.GONE);
                    }
                } else {
                    tvAlphabet.setVisibility(View.GONE);
                }

            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    dimListView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    showContent();
                } else {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showContent();
                            fuzzySearch(s.toString());
                        }
                    }, 200);
                }
            }
        });
    }

    private void initData() {
        listHot = new ArrayList<>();
        listHotHistory = new ArrayList<>();
        cityList = new ArrayList<>();
        listString = new LinkedList<>();
        listStringSub = new LinkedList<>();
        erJiCitytList = new ArrayList<>();
        cityToProvince = new HashMap<>();
        String json = FileUtil.readCityListJson(CitylistSearchActivity.this, "region.json");
        Type type = new TypeToken<ArrayList<ProvinceBean>>() {
        }.getType();
        regionList = JsonUtil.getListModle(json, HttpConstant.DATA, type);
        for (int i = 0; i < regionList.size(); i++) {   //遍历省份
            ProvinceBean provinceBean = regionList.get(i);
            String province = provinceBean.getProvince();
            List<City> citys = provinceBean.getCitys();
            for (int j = 0; j < citys.size(); j++) {    //遍历该省下的-市
                City city = citys.get(j);
                city.setProvinceName(province);
                String currentCity = city.getCity();
//                String substringCity = currentCity.substring(currentCity.length() - 1, currentCity.length());
                //二级城市去--"市"字
                if (currentCity.contains("市")) {
                    //不带市的  城市
                    String substring = currentCity.substring(0, currentCity.length() - 1);
                    cityToProvince.put(substring, province);
                    String quString = city.getCity();
                    if (!quString.contains("区")) {
                        //解决定位 不是二级市的BUG
                        erJiCitytList.add(currentCity);
                        listString.add(currentCity);
                        listStringSub.add(substring);
                    }
                } else {
                    cityToProvince.put(city.getCity(), province);

                    String quString = city.getCity();
                    if (!quString.contains("区")) {
                        listString.add(quString);
                        listStringSub.add(quString);
                    }
                }
                List<County> countys = city.getCountys();
                for (int k = 0; k < countys.size(); k++) {//遍历该市下的-县
                    County county = countys.get(k);
                    county.setProvinceName(province);
                    cityToProvince.put(county.getCounty(), province);
                    String qupString = county.getCounty();
                    if (!qupString.contains("区")) {
                        listString.add(qupString);
                        listStringSub.add(qupString);
                    }
                }
            }
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loaddingUI();
                dingwei();
                showContent();
            }
        }, 500);

    }

    private String findProinceByCity(String name) {
        return cityToProvince.get(name);
    }

    private ArrayList<WenziBean> dataList() {
        ArrayList<WenziBean> mSortList = new ArrayList<WenziBean>();
        for (int i = 0; i < listString.size(); i++) {
            WenziBean bean = new WenziBean(listString.get(i));
            bean.setProvinceName(findProinceByCity(listStringSub.get(i)));
            mSortList.add(bean);
        }
        Collections.sort(mSortList);
        return mSortList;
    }

    private void fuzzySearch(String str) {
        ArrayList<WenziBean> filterDateList = new ArrayList<>();
        if (TextUtils.isEmpty(str)) {
            filterDateList = dataList();
        } else {
            filterDateList.clear();
            for (WenziBean wzBean : dataList()) {
                String name = wzBean.getName();
                str = characterParser.getSelling(str);
                if (PinYinUtil.getPinyin(name).contains(str.toUpperCase()) || wzBean.pinYinStyle.briefnessSpell.toUpperCase().contains(str.toUpperCase())
                        || wzBean.pinYinStyle.completeSpell.toUpperCase().contains(str.toUpperCase())) {
                    filterDateList.add(wzBean);
                }
            }
        }
        if (filterDateList.isEmpty()) {
            showEmpty(emptyCity, R.mipmap.icon_search_nothing);
        } else {
            showContent();
            searchCityAdapter = new SearchCityLXAdapter(this, R.layout.item_city_searchlx, filterDateList);
            dimListView.setAdapter(searchCityAdapter);
            dimListView.setVisibility(View.VISIBLE);
            dimListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    WenziBean wenziBean = (WenziBean) adapterView.getItemAtPosition(position);
                    if (wenziBean != null) {
                        String name = wenziBean.getName();
                        String chossedProvince = wenziBean.getProvinceName();
                        if (chossedProvince.contains("市")) {
                            chossedProvince = name.substring(0, chossedProvince.length() - 1);
                        }
                        SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.CITY_TOP_CHOOSE, "" + name);
                        SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.PROVICE_TOP_CHOOSE, "" + chossedProvince);
                        HistoryCache historyCache = new HistoryCache();
                        historyCache.setName(name);
                        historyCache.setCacheType(HistoryCache.TYPE_CACHE_CITY);
                        SPCacheList.getInstance().writeCityHistory(historyCache);
                        KeyBoardUtils.closeKeybord(etSearch, CitylistSearchActivity.this);
                        finish();
                    }
                }
            });
        }
    }

    @OnClick({R.id.iv_fangdajing, R.id.titlebar_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_fangdajing:
                mListView.setSelection(0);
                break;
            case R.id.titlebar_right:
                CitylistSearchActivity.this.finish();
                break;
            default:
                super.onClickTitleLeftBtn(view);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if (locationService != null) {
            locationService.unregisterListener(mListener);
            locationService.stop();
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        regionList = null;
        super.onBackPressed();
    }

    //百度地图监听
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            final String city = location.getCity();
            String addrStr = location.getAddrStr();
            String province = location.getProvince();
            SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.KEY_DW_STATUS, true);
            if (province != null) {
                if (province.equals("北京市")) {
                    province = "北京";
                }
                SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.PROVINCE, "" + StringUtils.getProvice(province));
            }
            if (city != null) {
                SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.CITY, "" + city);
            }
            if (addrStr != null) {
                SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.ADDRESS, "" + addrStr);
            }
            if (longitude != 4.9E-324 && latitude != 4.9E-324) {
                SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.LONGITUDE, longitude + "");
                SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.LATITUDE, latitude + "");
                SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.LONGITUDE, longitude + "");
                headerTvCity.setText(StringUtils.getCity(CitylistSearchActivity.this, city));
                if (locationService != null) {
                    locationService.unregisterListener(mListener);
                    locationService.stop();
                }
            } else {
                SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.LONGITUDE, "");
                SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.LATITUDE, "");
                SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.PROVINCE, "");
                SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.CITY, "");
                SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.ADDRESS_CURRENT_CHOOSRE, "");
                SharePreferenceUtil.put(CitylistSearchActivity.this, SharePreferenceUtil.KEY_DW_STATUS, false);
                if (i == 1) {
                    headerTvCity.setText("定位失败，请点击重试");
                    if (locationService != null) {
                        locationService.unregisterListener(mListener);
                        locationService.stop();
                    }
                }
                i++;
            }
        }
    }

    @Override
    public void onStop() {
        if (locationService != null) {
            locationService.unregisterListener(mListener);
            locationService.stop();
        }
        super.onStop();
    }

    @Override
    public void error(String errorMsg) {
        super.error(errorMsg);
        LogUtil.y("errorMsg:" + errorMsg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationService.unregisterListener(mListener);
        locationService.stop();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        regionList = null;
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
                KeyBoardUtils.closeKeybord(v, CitylistSearchActivity.this);
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

}