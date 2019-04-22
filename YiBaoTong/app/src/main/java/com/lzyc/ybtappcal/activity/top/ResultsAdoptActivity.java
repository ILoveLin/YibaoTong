//package com.lzyc.ybtappcal.activity.top;
//
//import android.animation.Animator;
//import android.animation.ObjectAnimator;
//import android.animation.ValueAnimator;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.AnimationDrawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.LinearInterpolator;
//import android.view.animation.TranslateAnimation;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.android.pc.ioc.event.EventBus;
//import com.android.pc.ioc.inject.InjectLayer;
//import com.android.pc.ioc.inject.InjectView;
//import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
//import com.baidu.mapapi.navi.BaiduMapNavigation;
//import com.baidu.mapapi.navi.NaviParaOption;
//import com.baidu.mapapi.utils.OpenClientUtil;
//import com.google.gson.reflect.TypeToken;
//import com.lzyc.ybtappcal.R;
//import com.lzyc.ybtappcal.adapter.PopNayaoLeftAdapter;
//import com.lzyc.ybtappcal.adapter.PopNayaoRightAdapter;
//import com.lzyc.ybtappcal.adapter.ResultsNayaoAdapter;
//import com.lzyc.ybtappcal.adapter.TypeJobStatusAdapter;
//import com.lzyc.ybtappcal.bean.DrugBean;
//import com.lzyc.ybtappcal.bean.Event;
//import com.lzyc.ybtappcal.bean.HospitalBean;
//import com.lzyc.ybtappcal.bean.PlacedDataBean;
//import com.lzyc.ybtappcal.bean.SresultAreaBean;
//import com.lzyc.ybtappcal.bean.SresultStreetBean;
//import com.lzyc.ybtappcal.bean.TypeJobStatus;
//import com.lzyc.ybtappcal.constant.Contants;
//import com.lzyc.ybtappcal.constant.HttpConstant;
//import com.lzyc.ybtappcal.util.DensityUtils;
//import com.lzyc.ybtappcal.util.JsonUtil;
//import com.lzyc.ybtappcal.util.MD5ChangeUtil;
//import com.lzyc.ybtappcal.util.NetworkUtil;
//import com.lzyc.ybtappcal.util.PermissionUtil;
//import com.lzyc.ybtappcal.util.ScreenUtils;
//import com.lzyc.ybtappcal.util.SharePreferenceUtil;
//import com.lzyc.ybtappcal.util.ToastUtil;
//import com.lzyc.ybtappcal.view.RoundedRectListView;
//import com.lzyc.ybtappcal.widget.popupwindow.BasePopupWindow;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 医院社区采纳列表
// * @author  yang
// */
//@InjectLayer(value = R.layout.activity_results_adopt, parent = R.id.ll_content, isFull = false, isTitle = false)
//public class ResultsAdoptActivity extends BaseActivity implements ResultsNayaoAdapter.OnItemClickListener, Animator.AnimatorListener {
//    protected static final String TAG = ResultsAdoptActivity.class.getName();
//    public static final String VAL_DEFAULT_TYPE_JOB = "ZZ00";
//    private static final int TIME_LODDING_COUNT_REMOVE = 1000;//加载图标移除等待时长，默认为1秒
//    private static final int TIME_ANIM = 300;//动画执行时长
//    private static final int MSG_LISTVIEW_LOADDING_ADD_TYPE_PRICE = 1010;
//    private static final int MSG_LISTVIEW_LOADDING_REMOVE_TYPE_PRICE = 1020;
//    private static final int MSG_LISTVIEW_TYPE_DATA_ADD_PRICE = 1030;
//    private String drugNameID;
//    private String venderID;
//    private String specificationsID;
//    private String drugId;
//    private PlacedDataBean placedData;
//    @InjectView(R.id.sresult_relative_content)
//    private RelativeLayout sresult_relative_content;
//    @InjectView(R.id.include_loading_linear)
//    private LinearLayout include_loading_linear;
//    @InjectView(R.id.include_loading_iv)
//    private ImageView include_loading_iv;
//    @InjectView(R.id.include_net_error_linear)
//    private LinearLayout include_net_error_linear;
//    @InjectView(R.id.include_net_setting_tv)
//    private TextView include_net_setting;
//    @InjectView(R.id.tv_scan_result_goodsname)
//    private TextView tv_scan_result_goodsname;
//    @InjectView(R.id.tv_scan_result_price)
//    private TextView tv_scan_result_price;
//    @InjectView(R.id.tv_scan_result_price_rmb)
//    private TextView tv_scan_result_price_rmb;
//    @InjectView(R.id.tv_scan_result_name)
//    private TextView tv_scan_result_name;
//    @InjectView(R.id.tv_scan_result_spec)
//    private TextView tv_scan_result_spec;
//    @InjectView(R.id.sresult_linear_content_top)
//    private LinearLayout linear_content_top;
//    @InjectView(R.id.scan_reslut_iv_a)
//    private ImageView scan_reslut_iv_a;
//    @InjectView(R.id.scan_reslut_iv_b)
//    private ImageView scan_reslut_iv_b;
//    @InjectView(R.id.scan_reslut_iv_ab)
//    private ImageView scan_reslut_iv_ab;
//    @InjectView(R.id.scan_reslut_iv_m)
//    private ImageView scan_reslut_iv_m;
//    @InjectView(R.id.scan_result_rekedu)
//    private TextView scan_result_rekedu;
//    @InjectView(R.id.sresult_tab_address)
//    private TextView sresult_tab_address;
//    @InjectView(R.id.sresult_tab_nayao)
//    private TextView sresult_tab_nayao;
//    @InjectView(R.id.sresult_tab_juli)
//    private TextView sresult_tab_juli;
//    @InjectView(R.id.sresult_tab_price)
//    private TextView sresult_tab_price;
//    @InjectView(R.id.sresult_relative_dingwei)
//    private RelativeLayout sresult_relative_dingwei;
//    @InjectView(R.id.sresult_lv)
//    private ListView sresult_lv;
//    @InjectView(R.id.sresult_relative_tab)
//    private RelativeLayout sresult_relative_tab;
//    @InjectView(R.id.pop_linear)
//    private LinearLayout pop_linear;
//    @InjectView(R.id.sresult_titlebar_content)
//    private TextView sresult_titlebar_content;
//    @InjectView(R.id.sresult_titlebar_content_iv)
//    private ImageView sresult_titlebar_content_iv;
//    @InjectView(R.id.pop_linear_top)
//    private LinearLayout pop_linear_top;
//    @InjectView(R.id.sresult_titlebar_relative)
//    private RelativeLayout sresult_titlebar_relative;
//    @InjectView(R.id.pop_lv_left)
//    private RoundedRectListView pop_lv_left;
//    @InjectView(R.id.pop_lv_right)
//    private RoundedRectListView pop_lv_right;
//    @InjectView(R.id.sresult_view_out)
//    private View sresult_view_out;
//    @InjectView(R.id.pop_sh_tv_one)
//    private TextView pop_sh_tv_one;
//    @InjectView(R.id.pop_sh_tv_one2)
//    private TextView pop_sh_tv_one2;
//    @InjectView(R.id.pop_sh_tv_two)
//    private TextView pop_sh_tv_two;
//    @InjectView(R.id.pop_sh_tv_two2)
//    private TextView pop_sh_tv_two2;
//    @InjectView(R.id.pop_sh_linear_one_view)
//    private View pop_sh_linear_one_view;
//    @InjectView(R.id.pop_sh_linear_two_view)
//    private View pop_sh_linear_two_view;
//    @InjectView(R.id.pop_sh_linear_one)
//    private LinearLayout pop_sh_linear_one;
//    @InjectView(R.id.pop_sh_linear_two)
//    private LinearLayout pop_sh_linear_two;
//    @InjectView(R.id.scan_result_caina)
//    private LinearLayout scan_result_caina;
//    @InjectView(R.id.scan_result_rekedu_right)
//    private ImageView scan_result_rekedu_right;
//    @InjectView(R.id.scan_result_tvdesc)
//    private TextView scan_result_tvdesc;
//    @InjectView(R.id.inculde_net_error_desc)
//    private TextView inculde_net_error_desc;
//    private View view_footer;
//    private String typeJob;
//    private ResultsNayaoAdapter mAdapter;
//    private int leftPage = 0;
//    private String lat, lng;
//    private boolean isReshLeft = false;
//    private int leftIndex;
//    private int cLeftPage = -1;
//    private View footerView;
//    private ArrayList<HospitalBean> leftList;
//    private boolean isDonePlan = false;
//    private BasePopupWindow popTransparency;
//    private LinearLayout footer_ybtlv_linear;
//    private PopupWindow popJob = null;
//    @InjectView(R.id.pop_linear_city)
//    private LinearLayout pop_linear_city;
//    @InjectView(R.id.pop_linear_hospital)
//    private LinearLayout pop_linear_hospital;
//    private String[] typeJobs, typeJobsType;
//    private PopNayaoRightAdapter childAdapter;
//    private ArrayList<HospitalBean> lList;
//    private String area = "";//街道上级地址
//    private String street = "";//街道地址
//    @InjectView(R.id.sresult_yaopin)
//    private TextView sresult_yaopin;
//    private DrugBean recommedDrugBean;
//    @InjectView(R.id.loadding_listview_linear)
//    private LinearLayout loadding_listview_linear;
//    @InjectView(R.id.loadding_listview_image)
//    private ImageView loadding_listview_image;
//    private boolean isEnding = false;
//    private boolean isTouch = true;
//    private boolean isShowing = true;//走了显示动画就不在走隐藏动画的字段判断,防止动画多次执行
//    private boolean isHiddening = false;//走了隐藏动画就不在走显示动画的字段判断,防止动画多次执行
//    @InjectView(R.id.sresult_linear_drug)
//    private LinearLayout sresult_linear_drug;
//    @InjectView(R.id.sresult_titlebar_right)
//    private ImageButton sresult_titlebar_right;
//    @InjectView(R.id.sresult_tab_nayao_cancel)
//    private TextView sresult_tab_nayao_cancel;
//    @InjectView(R.id.pop_linear_content)
//    private LinearLayout pop_linear_content;
//    private TypeJobStatusAdapter typeJobStatusAdapter;
//    private int typePage = 0;
//
//    private Handler mHandler = new Handler() {
//        public void handleMessage(android.os.Message msg) {
//            switch (msg.what) {
//                case MSG_LISTVIEW_LOADDING_ADD_TYPE_PRICE:
//                    sresult_lv.setEnabled(false);
//                    sresult_titlebar_right.setEnabled(false);
//                    try {
//                        mAdapter.addLoadding();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    isReshLeft = true;
//                    leftIndex = mAdapter.getCount() - 1;
//                    mHandler.sendEmptyMessage(MSG_LISTVIEW_TYPE_DATA_ADD_PRICE);
//                    break;
//                case MSG_LISTVIEW_LOADDING_REMOVE_TYPE_PRICE:
//                    mAdapter.removeLoadding(leftIndex);
//                    sresult_lv.setEnabled(true);
//                    sresult_titlebar_right.setEnabled(true);
//                    break;
//                case MSG_LISTVIEW_TYPE_DATA_ADD_PRICE:
//                    leftPage++;
//                    requestDrug();
//                    break;
//                case 1:
//                    dismissPopJob();
//                    mHandler.sendEmptyMessageDelayed(2, 100);
//                    break;
//                case 2:
//                    refreshUI();
//                    break;
//                default:
//                    break;
//            }
//        }
//
//    };
//    private int topHeight;
//    private boolean hidden = true;
//    private boolean isDown = false;
//    private boolean isUp = false;
//    private boolean isHidden = false;
//    private boolean isAnim = false;
//    private float lastX = 0;
//    private float lastY = 0;
//    private boolean isOneHide = false;
//    private int hiddenCount = 0;
//    private String currentProvince;
//    private int renkedu;
//
//    @Override
//    public void init() {
//        super.init();
//        hideTop();
//        lList = new ArrayList<>();
//        initView();
//        setListener();
//    }
//
//    private void setListener() {
//        sresult_lv.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (isAnim) {
//                    return false;
//                }
//                final int action = event.getAction();
//                float x = event.getX();
//                float y = event.getY();
//                switch (action) {
//                    case MotionEvent.ACTION_DOWN:
//                        lastY = y;
//                        lastX = x;
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        float dY = Math.abs(y - lastY);
//                        float dX = Math.abs(x - lastX);
//                        boolean down = y > lastY ? true : false;
//                        if (isOneHide) {
//                            down = y - linear_content_top.getHeight() - 20 > lastY ? true
//                                    : false;
//                            // 坐标在控件隐藏时预留纠正时间
//                            hiddenCount++;
//                            if (hiddenCount > 10) {
//                                hiddenCount = 0;
//                                isOneHide = false;
//                            }
//                        }
//                        lastY = y;
//                        lastX = x;
//                        isUp = dX < 8 && dY > 8 && !isHidden && !down;
//                        isDown = dX < 8 && dY > 8 && isHidden && down;
//                        if (isUp) {
//                            if (isShowing && !isHiddening) {
//                                if (isTouch) {
//                                    hiddenTouchView();
//                                }
//                            }
//                        } else if (isDown) {
//                            if (!isShowing && isHiddening) {
//                                if (isTouch) {
//                                    showTouchView();
//                                }
//                            }
//                        } else {
//                            return false;
//                        }
//                        isAnim = true;
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        isOneHide = false;
//                        hiddenCount = 0;
//                        break;
//                    default:
//                        return false;
//                }
//                return false;
//            }
//        });
//    }
//
//    private void initView() {
//        Bundle bundle=getIntent().getExtras();
//        DrugBean drugBean= (DrugBean) bundle.getSerializable(Contants.KEY_OBJ_DRUGBEAN);
//        drugId = drugBean.getDrug_id();
//        drugNameID = drugBean.getDrugNameID();
//        specificationsID = drugBean.getSpecificationsID();
//        venderID = drugBean.getVenderID();
//        typePage =bundle.getInt(Contants.KEY_PAGE, typePage);
//        if (drugId.isEmpty()) {
//            drugId = "";
//        }
//        if (drugNameID.isEmpty()) {
//            drugNameID = "";
//        }
//        if (specificationsID.isEmpty()) {
//            specificationsID = "";
//        }
//        if (venderID.isEmpty()) {
//            venderID = "";
//        }
//        sresult_tab_price.setSelected(true);
//        sresult_tab_price.setText("从低到高");
//        sresult_tab_juli.setSelected(false);
//        showLoaddding();
//        leftPage = 0;
//        cLeftPage = -1;
//        requestResult();
//        leftList = new ArrayList<>();
//        mAdapter = new ResultsNayaoAdapter(this, leftList, R.layout.item_results_nayao, this);
//        sresult_lv.setAdapter(mAdapter);
//        footerView = View.inflate(this, R.layout.footer_listview_scanresult, null);
//        view_footer = footerView.findViewById(R.id.view_footer);
//        footer_ybtlv_linear = (LinearLayout) footerView.findViewById(R.id.footer_ybtlv_linear);
//        sresult_lv.addFooterView(footerView, null, false);
//        sresult_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
//                    if (mAdapter.getCount() == sresult_lv.getLastVisiblePosition() && isEnding == false) {
//                        mHandler.sendEmptyMessage(MSG_LISTVIEW_LOADDING_ADD_TYPE_PRICE);
//                    }
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//            }
//        });
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        boolean isDingwei = (Boolean) SharePreferenceUtil.get(this, SharePreferenceUtil.KEY_DW_STATUS, false);
//        if (!isDingwei) {
//            if (currentProvince.equals("北京") || typePage == Contants.VAL_PAGE_MINEPLAN) {
//                sresult_relative_dingwei.setVisibility(View.VISIBLE);
//            } else {
//                sresult_relative_dingwei.setVisibility(View.GONE);
//            }
//        } else {
//            sresult_relative_dingwei.setVisibility(View.GONE);
//        }
//        if (popTransparency != null) {
//            popTransparency.dismiss();
//            popTransparency = null;
//            isDonePlan = false;
//        }
//        typeJobs = getResources().getStringArray(R.array.type_job);
//        typeJobsType = getResources().getStringArray(R.array.type_job_status);
//        typeJob = (String) SharePreferenceUtil.get(ResultsAdoptActivity.this, SharePreferenceUtil.KEY_OBJ_TYPE_JOB, VAL_DEFAULT_TYPE_JOB);
//        for (int i = 0; i < typeJobs.length; i++) {
//            if (typeJob.equals(typeJobsType[i])) {
//                sresult_titlebar_content.setText(typeJobs[i]);
//                break;
//            }
//        }
//    }
//
//    @Override
//    public void onBtnClick(View view) {
//        switch (view.getId()) {
//            case R.id.sresult_titlebar_left:
//                ResultsAdoptActivity.this.finish();
//                break;
//            case R.id.sresult_view_out:
//                dismissPopBottom();
//                break;
//            case R.id.sresult_titlebar_content:
//            case R.id.sresult_titlebar_content_iv:
//                if (popJob == null) {
//                    popupJobType();
//                } else {
//                    dismissPopJob();
//                }
//                break;
//            case R.id.sresult_titlebar_right:
//                EventBus.getDefault().post(new Event("i001"));
//                Bundle bundle = new Bundle();
//                ArrayList<HospitalBean> list = mAdapter.getList();
//                bundle.putSerializable("list", list);
//                bundle.putInt(Contants.KEY_PAGE_YIYUANWEIZHI, Contants.VAL_PAGE_YIYUANWEIZHI_SRESULT);
//                openActivity(HospitalNearbyActivity.class, bundle);
//                break;
//            case R.id.sresult_tab_address:
//                sresult_view_out.setVisibility(View.VISIBLE);
//                if (pop_linear_city.getVisibility() == View.GONE) {
//                    popBottomAddress();
//                } else {
//                    dismissPopBottom();
//                }
//                break;
//            case R.id.sresult_tab_nayao://tab 拿药
//                sresult_view_out.setVisibility(View.VISIBLE);
//                if (pop_linear_hospital.getVisibility() == View.GONE) {
//                    popBottomNayao();
//                } else {
//                    dismissPopBottom();
//                }
//                break;
//            case R.id.sresult_tab_juli://tab 距离
//                dismissPopBottom();
//                EventBus.getDefault().post(new Event("e007"));
//                sresult_tab_juli.setText("从近到远");
//                sresult_tab_juli.setSelected(true);
//                sresult_tab_price.setSelected(false);
//                sresult_tab_price.setText("价格");
//                refreshUI();
//                break;
//            case R.id.sresult_tab_price://价格
//                dismissPopBottom();
//                EventBus.getDefault().post(new Event("e006"));
//                sresult_tab_price.setText("从低到高");
//                sresult_tab_juli.setSelected(false);
//                sresult_tab_price.setSelected(true);
//                sresult_tab_juli.setText("距离");
//                refreshUI();
//                break;
//            case R.id.sresult_tv_detail://药品详情
//                if (placedData != null) {
//                    EventBus.getDefault().post(new Event("y001"));
////                    Intent intent = new Intent(this, DrugDetailActivity.class);
////                    intent.putExtra("drugNameID", placedData.getDrugNameID());
////                    intent.putExtra("specificationsID", placedData.getSpecificationsID());
////                    intent.putExtra("venderID", placedData.getVenderID());
////                    intent.putExtra("province", currentProvince);
////                    intent.putExtra(Contants.KEY_PAGE, typePage);
////                    if (typePage == Contants.VAL_PAGE_MINEPLAN) {
////                        intent.putExtra("plan", "comeFromPlanDetail");
////                    }
////                    startActivity(intent);
//
//                } else {
//                    ToastUtil.showShort(ResultsAdoptActivity.this, "药品信息为空！");
//                }
//                break;
////            case R.id.scan_result_relative_recomment:
////                EventBus.getDefault().post(new Event("e009"));
////                Intent intent1 = new Intent(ResultsAdoptActivity.this, ResultsAdoptActivity.class);
////                intent1.putExtra("drugNameID", recommedDrugBean.getDrugNameID());
////                intent1.putExtra("venderID", recommedDrugBean.getVenderID());
////                intent1.putExtra("specificationsID", recommedDrugBean.getSpecificationsID());
////                intent1.putExtra("drug_id", recommedDrugBean.getDrug_id());
////                intent1.putExtra(Contants.KEY_PAGE, typePage);
////                if (typePage == Contants.VAL_PAGE_MINEPLAN) {
////                    intent1.putExtra("plan", "comeFromPlanDetail");
////                }
////                startActivity(intent1);
////                break;
//            case R.id.scan_result_rekedu://认可度
//            case R.id.tv_scan_result_end://跳转到认可度界面
//                Intent intent = new Intent(this, ConfirmActivity.class);
//                intent.putExtra("drugNameID", placedData.getDrugNameID());
//                intent.putExtra("drugHostopID", placedData.getDrugHostopID());
//                intent.putExtra("specificationsID", placedData.getSpecificationsID());
//                intent.putExtra("venderID", placedData.getVenderID());
//                intent.putExtra(Contants.KEY_PAGE, typePage);
//                if (TextUtils.isEmpty(drugId)) {
//                    drugId = "";
//                }
//                if (typePage == Contants.VAL_PAGE_MINEPLAN) {
//                    intent.putExtra("plan", "comeFromPlanDetail");
//                }
//                intent.putExtra("drugId", drugId);
//                intent.putExtra("province", currentProvince);
//                startActivity(intent);
//                break;
//            case R.id.pop_sh_linear_one:
//                popNayaoOneSelected();
//                break;
//            case R.id.pop_sh_linear_two:
//                popNayoTwoSelected();
//                break;
//            case R.id.sresult_tv_dingwei_open:
//                PermissionUtil.getInstance().showDingwei(ResultsAdoptActivity.this);
//                break;
//            case R.id.sresult_tab_nayao_cancel:
//                dismissPopBottom();
//                break;
//            default:
//                super.onBtnClick(view);
//                break;
//        }
//    }
//
//    /**
//     * 医保拿药
//     */
//    private void popNayoTwoSelected() {
//        pop_sh_tv_one.setTextColor(getResources().getColor(R.color.color_444444));
//        pop_sh_tv_one2.setTextColor(getResources().getColor(R.color.color_666666));
//        pop_sh_tv_two.setTextColor(getResources().getColor(R.color.color_6074b4));
//        pop_sh_tv_two2.setTextColor(getResources().getColor(R.color.color_6074b4));
//        pop_sh_linear_one_view.setVisibility(View.INVISIBLE);
//        pop_sh_linear_two_view.setVisibility(View.VISIBLE);
//        pop_sh_linear_one.setSelected(false);
//        pop_sh_linear_two.setSelected(true);
//        sresult_tab_nayao.setText("医保拿药");
//        dismissPopBottomYanchi();
//    }
//
//    /**
//     * 弹窗延迟隐藏
//     */
//    private void dismissPopBottomYanchi() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dismissPopBottom();
//                refreshUI();
//            }
//        }, 100);
//    }
//
//    /**
//     * 全部医院点击
//     */
//    private void popNayaoOneSelected() {
//        EventBus.getDefault().post(new Event("e010"));
//        pop_sh_tv_one.setTextColor(getResources().getColor(R.color.color_6074b4));
//        pop_sh_tv_one2.setTextColor(getResources().getColor(R.color.color_6074b4));
//        pop_sh_tv_two.setTextColor(getResources().getColor(R.color.color_444444));
//        pop_sh_tv_two2.setTextColor(getResources().getColor(R.color.color_666666));
//        pop_sh_linear_one_view.setVisibility(View.VISIBLE);
//        pop_sh_linear_two_view.setVisibility(View.INVISIBLE);
//        pop_sh_linear_one.setSelected(true);
//        pop_sh_linear_two.setSelected(false);
//        sresult_tab_nayao.setText("全部医院");
//        dismissPopBottomYanchi();
//    }
//
//    /**
//     * 地址弹窗
//     */
//    private void popBottomAddress() {
//        sresult_tab_address.setSelected(true);
//        sresult_tab_nayao.setSelected(false);
//        int bottomHieght = sresult_relative_tab.getHeight();
//        TranslateAnimation animatorBottom = new TranslateAnimation(0, 0, 0, bottomHieght);
//        animatorBottom.setInterpolator(new LinearInterpolator());
//        animatorBottom.setDuration(TIME_ANIM);
//        sresult_relative_tab.startAnimation(animatorBottom);
//        animatorBottom.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                sresult_relative_tab.setVisibility(View.GONE);
//                pop_linear_hospital.setVisibility(View.GONE);
//                TranslateAnimation animation1 = new TranslateAnimation(0, 0, pop_linear_content.getHeight(), 0);
//                animation1.setInterpolator(new LinearInterpolator());
//                animation1.setDuration(500);
//                pop_linear_content.startAnimation(animation1);
//                animation1.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//                        pop_linear_city.setVisibility(View.VISIBLE);
//                        sresult_tab_nayao_cancel.setVisibility(View.VISIBLE);
//                        pop_linear.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//    }
//
//    /**
//     * 拿药弹窗
//     */
//    private void popBottomNayao() {
//        sresult_tab_nayao.setSelected(true);
//        sresult_tab_address.setSelected(false);
//        int bottomHieght = sresult_relative_tab.getHeight();
//        TranslateAnimation animatorBottom = new TranslateAnimation(0, 0, 0, bottomHieght);
//        animatorBottom.setInterpolator(new LinearInterpolator());
//        animatorBottom.setDuration(TIME_ANIM);
//        sresult_relative_tab.startAnimation(animatorBottom);
//        animatorBottom.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                sresult_relative_tab.setVisibility(View.GONE);
//                pop_linear_city.setVisibility(View.GONE);
//                TranslateAnimation animation1 = new TranslateAnimation(0, 0, pop_linear_content.getHeight(), 0);
//                animation1.setInterpolator(new LinearInterpolator());
//                animation1.setDuration(500);
//                pop_linear_content.startAnimation(animation1);
//                animation1.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//                        pop_linear_hospital.setVisibility(View.VISIBLE);
//                        sresult_tab_nayao_cancel.setVisibility(View.VISIBLE);
//                        pop_linear.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//    }
//
//    /**
//     * 扫描结果
//     */
//    private void requestResult() {
//        currentProvince = (String) SharePreferenceUtil.get(ResultsAdoptActivity.this, SharePreferenceUtil.PROVICE_TOP_CHOOSE, "北京");
//        String phone = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PHONE, "");
//        String ybType = (String) SharePreferenceUtil.get(ResultsAdoptActivity.this, SharePreferenceUtil.KEY_OBJ_TYPE_JOB, VAL_DEFAULT_TYPE_JOB);
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("app", "Home");
//        params.put("class", "Results3");
//        String sign = MD5ChangeUtil.Md5_32("HomeResults3" + HttpConstant.APP_SECRET);
//        params.put("sign", sign);
//        if (drugId != null) {
//            params.put("drug_id", drugId);
//        }
//        params.put("DrugNameID", drugNameID);
//        params.put("VenderID", venderID);
//        params.put("SpecificationsID", specificationsID);
//        params.put("pageIndex", "0");
//        params.put("ybtype", ybType);
//        params.put("province", "北京");
//        if (!TextUtils.isEmpty(phone)) {
//            params.put("phone", phone);
//        } else {
//            params.put("phone", "");
//        }
//        if (!NetworkUtil.CheckConnection(this)) {
//            showNetError();
//
//            include_net_setting.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (!NetworkUtil.CheckConnection(ResultsAdoptActivity.this)) {
//                        showNetDialog();
//                        return;
//                    }
//                    requestResult();
//                }
//            });
//            return;
//        }
//        request(params, HttpConstant.TOP_SCAN_RESULT);
//    }
//
//    @Override
//    public void done(String msg, int what, JSONObject response) {
//        super.done(msg, what, response);
//        switch (what) {
//            case HttpConstant.TOP_SCAN_RESULT:
//                try {
//                    JSONObject data = response.getJSONObject("data");
//                    placedData = JsonUtil.getModle(data.getJSONObject("drugDetail").toString(), PlacedDataBean.class);
//                    fullInfo();
//                    List<SresultAreaBean> areaDara = JsonUtil.getListModle(data.toString(), "areaList", new TypeToken<ArrayList<SresultAreaBean>>() {
//                    }.getType());
//                    loadPopBottom(areaDara);
//                } catch (JSONException e) {
//                    showNetError();
//                    e.printStackTrace();
//                }
//                break;
//            case HttpConstant.RESULTS_HOSPITAL_LIST:
//                showListvieLoadddingContent();
//                try {
//                    JSONObject data = response.getJSONObject("data");
//                    Contants.oldType = (String) SharePreferenceUtil.get(ResultsAdoptActivity.this, SharePreferenceUtil.KEY_OBJ_TYPE_JOB, VAL_DEFAULT_TYPE_JOB);
//                    lList = JsonUtil.getListModle2(data.toString(), "hosList", new TypeToken<ArrayList<HospitalBean>>() {
//                    }.getType());
//                    if (lList == null) {
//                        lList = new ArrayList<>();
//                    }
//                    reloadLeft(lList);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            default:
//                break;
//        }
//    }
//
//    /**
//     * 加载地址弹窗数据
//     *
//     * @param areaDara
//     */
//    private void loadPopBottom(List<SresultAreaBean> areaDara) {
//        if (areaDara.isEmpty()) {
//            return;
//        }
//        final PopNayaoLeftAdapter groupAdapter = new PopNayaoLeftAdapter(ResultsAdoptActivity.this, areaDara, R.layout.item_pop_lv_nayao_left);
//        pop_lv_left.setAdapter(groupAdapter);
//        groupAdapter.setItemSelected(0);
//        childAdapter = new PopNayaoRightAdapter(ResultsAdoptActivity.this, areaDara.get(0).getStreetList(), R.layout.item_pop_lv_nayao_right);
//        pop_lv_right.setAdapter(childAdapter);
//        childAdapter.setItemSelected(0);
//        pop_lv_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                SresultAreaBean group = (SresultAreaBean) adapterView.getItemAtPosition(position);
//                String childItemSelected = sresult_tab_address.getText().toString();//获取childItem选中项
//                groupAdapter.updateItem(group);
//                List<SresultStreetBean> listChild = group.getStreetList();
//                childAdapter = new PopNayaoRightAdapter(ResultsAdoptActivity.this, listChild, R.layout.item_pop_lv_nayao_right);
//                pop_lv_right.setAdapter(childAdapter);
//                childAdapter.setItemSelected(childItemSelected);//清空选择历史，只记住唯一被选项
//                area = group.getArea();//网络请求时，给area赋值
//            }
//        });
//        pop_lv_right.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                SresultStreetBean childAddressBean = (SresultStreetBean) adapterView.getItemAtPosition(position);
//                childAdapter.updateItem(childAddressBean);
//                sresult_tab_address.setText(childAddressBean.getStreet());
//                dismissPopBottomYanchi();
//                EventBus.getDefault().post(new Event("e001"));
//                street = childAddressBean.getStreet();//网络请求时，给street赋值
//            }
//        });
//
//    }
//
//    private void reloadLeft(List<HospitalBean> list) {
//        if (isReshLeft) {
//            mHandler.sendEmptyMessageDelayed(MSG_LISTVIEW_LOADDING_REMOVE_TYPE_PRICE, TIME_LODDING_COUNT_REMOVE);
//        }
//        if (leftPage > cLeftPage) {
//            if (list.isEmpty()) {
//                isEnding = true;
//                view_footer.setVisibility(View.GONE);
//                footer_ybtlv_linear.setVisibility(View.VISIBLE);
//            } else {
//                view_footer.setVisibility(View.VISIBLE);
//                footer_ybtlv_linear.setVisibility(View.GONE);
//            }
//            cLeftPage = leftPage;
//            if (leftPage == 0) {
//                if (!leftList.isEmpty()) {
//                    leftList.clear();
//                }
//            }
//            leftList.addAll(list);
//            mAdapter.notifyDataSetChanged();
//        }
//        if (mAdapter.getCount() == 0) {
//            sresult_lv.setVisibility(View.GONE);
//            scan_result_caina.setVisibility(View.VISIBLE);
//            sresult_relative_tab.setVisibility(View.GONE);
//            if (placedData.getRenkedu() == -1) {//无医院/社区采纳
//                scan_result_tvdesc.setText("暂无医院/社区采纳");
//            } else {
//                if (currentProvince.equals("北京") || typePage == Contants.VAL_PAGE_MINEPLAN) {
//                    scan_result_tvdesc.setText("本规格药品暂无医院/社区采纳");
//                } else {
//                    scan_result_tvdesc.setText("没有找到符合条件的医院/社区");
//                }
//            }
//        } else {
//            sresult_lv.setVisibility(View.VISIBLE);
//            scan_result_caina.setVisibility(View.GONE);
//        }
//        if (mAdapter.getCount() < 3) {
//            isEnding = true;
//            isTouch = false;
//            view_footer.setVisibility(View.GONE);
//            footer_ybtlv_linear.setVisibility(View.GONE);
////            if (sresult_relative_tab.getVisibility() == View.GONE && mAdapter.getCount() != 0) {
////                sresult_relative_tab.setVisibility(View.GONE);
////            }
//        } else if (mAdapter.getCount() >= 3 && mAdapter.getCount() < 10) {
//            isEnding = true;
//            view_footer.setVisibility(View.GONE);
//            footer_ybtlv_linear.setVisibility(View.VISIBLE);
////            if (sresult_relative_tab.getVisibility() == View.GONE && mAdapter.getCount() != 0) {
////                sresult_relative_tab.setVisibility(View.GONE);
////            }
//        }
//    }
//
//    @Override
//    public void error(String errorMsg) {
//        super.error(errorMsg);
//        showNetError();
//    }
//
//    private void showNetError() {
//        inculde_net_error_desc.setTextColor(Color.parseColor("#70ffffff"));
//        include_net_setting.setTextColor(Color.parseColor("#70ffffff"));
//        sresult_relative_content.setVisibility(View.GONE);
//        include_loading_linear.setVisibility(View.GONE);
//        include_net_error_linear.setVisibility(View.VISIBLE);
//    }
//
//    private void fullInfo() {
//        showContent();
//        String baoxiao = placedData.getBaoxiao();
//        String goodsName = placedData.getGoodsName();
//        String name = placedData.getName();
//        if (!TextUtils.isEmpty(goodsName)) {
//            tv_scan_result_goodsname.setText(goodsName);
//            tv_scan_result_name.setText(name);
//        } else {
//            tv_scan_result_goodsname.setText(name);
//            tv_scan_result_name.setText("");
//        }
//        String drugType = placedData.getDrugType();
//        if (drugType.equals(Contants.STR_DRUG_TYPE_A)) {
//            sresult_linear_drug.setBackgroundResource(R.mipmap.bg_drug_a);
//            scan_reslut_iv_a.setVisibility(View.VISIBLE);
//            scan_reslut_iv_b.setVisibility(View.GONE);
//            scan_reslut_iv_ab.setVisibility(View.GONE);
//            scan_reslut_iv_m.setVisibility(View.GONE);
//        } else if (drugType.equals(Contants.STR_DRUG_TYPE_B)) {
//            sresult_linear_drug.setBackgroundResource(R.mipmap.bg_drug_b);
//            scan_reslut_iv_a.setVisibility(View.GONE);
//            scan_reslut_iv_b.setVisibility(View.VISIBLE);
//            scan_reslut_iv_ab.setVisibility(View.GONE);
//            scan_reslut_iv_m.setVisibility(View.GONE);
//        } else if (drugType.equals(Contants.STR_DRUG_TYPE_AB)) {
//            sresult_linear_drug.setBackgroundResource(R.mipmap.bg_drug_ab);
//            scan_reslut_iv_a.setVisibility(View.GONE);
//            scan_reslut_iv_b.setVisibility(View.GONE);
//            scan_reslut_iv_ab.setVisibility(View.VISIBLE);
//            scan_reslut_iv_m.setVisibility(View.GONE);
//        } else {
//            sresult_linear_drug.setBackgroundResource(R.mipmap.bg_drug_m);
//            scan_reslut_iv_a.setVisibility(View.GONE);
//            scan_reslut_iv_b.setVisibility(View.GONE);
//            scan_reslut_iv_ab.setVisibility(View.GONE);
//            scan_reslut_iv_m.setVisibility(View.VISIBLE);
//        }
//
//        if (currentProvince.equals("北京") || typePage == Contants.VAL_PAGE_MINEPLAN) {
//            if ("0.00".equals(placedData.getPrice())) {
//                tv_scan_result_price.setText("暂无报价");
//            } else {
//                tv_scan_result_price.setText(placedData.getPrice());
//            }
//            tv_scan_result_spec.setText("" + placedData.getVender());
//            renkedu = placedData.getRenkedu();
//            tv_scan_result_price_rmb.setVisibility(View.VISIBLE);
//        } else {
//            String price = placedData.getPrice();
//            if (TextUtils.isEmpty(price)) {
//                tv_scan_result_price.setText("暂无报价");
//            } else {
//                if (Double.parseDouble(price) == 0) {
//                    tv_scan_result_price.setText("暂无报价");
//                } else {
//                    tv_scan_result_price_rmb.setVisibility(View.VISIBLE);
//                    tv_scan_result_price.setText(price);
//                }
//            }
//            tv_scan_result_price_rmb.setVisibility(View.VISIBLE);
//            tv_scan_result_spec.setText("" + placedData.getVender());
//            renkedu = placedData.getRenkedu();
//            if (renkedu == -1) {   //无医院/社区采纳
//                scan_result_tvdesc.setVisibility(View.GONE);
//            } else if (renkedu == 1) {   //认可度第一名
//                scan_result_tvdesc.setVisibility(View.GONE);
//            }
//        }
//        if (renkedu == -1) {//无医院/社区采纳
//            scan_result_caina.setVisibility(View.VISIBLE);
//            sresult_relative_tab.setVisibility(View.GONE);
//            scan_result_rekedu_right.setVisibility(View.GONE);
//            scan_result_rekedu.setText("无医院/社区采纳");
//            scan_result_tvdesc.setText("暂无医院/社区采纳");
//            scan_result_rekedu.setEnabled(false);
//            sresult_lv.setVisibility(View.GONE);
//            loadding_listview_linear.setVisibility(View.GONE);
//        } else {
//            sresult_lv.setVisibility(View.VISIBLE);
//            scan_result_rekedu.setEnabled(true);
//            scan_result_rekedu.setText("认可度(第" + renkedu + "名)");
//            scan_result_rekedu_right.setVisibility(View.VISIBLE);
//            scan_result_caina.setVisibility(View.GONE);
//            loadding_listview_linear.setVisibility(View.VISIBLE);
//        }
//        sresult_yaopin.setText(placedData.getSpecifications());
//        showListvieLoaddding();
//        popNayaoOneSelected();
//    }
//
//    private void showContent() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                sresult_relative_content.setVisibility(View.VISIBLE);
//                include_loading_linear.setVisibility(View.GONE);
//                include_net_error_linear.setVisibility(View.GONE);
//                if (mAdapter.getCount() != 0) {
//                    sresult_relative_tab.setVisibility(View.VISIBLE);
//                }
//            }
//        }, 800);
//    }
//
//    private void showLoaddding() {
//        AnimationDrawable animationDrawable = (AnimationDrawable) include_loading_iv.getBackground();
//        animationDrawable.start();
//        sresult_relative_content.setVisibility(View.GONE);
//        include_loading_linear.setVisibility(View.VISIBLE);
//        include_net_error_linear.setVisibility(View.GONE);
//        sresult_relative_tab.setVisibility(View.GONE);
//    }
//
//    private void showListvieLoaddding() {
//        loadding_listview_image.setBackgroundResource(R.drawable.loadding_image);
//        AnimationDrawable animationDrawable = (AnimationDrawable) loadding_listview_image.getBackground();
//        animationDrawable.start();
//        sresult_lv.setVisibility(View.GONE);
//        loadding_listview_linear.setVisibility(View.VISIBLE);
//        include_net_error_linear.setVisibility(View.GONE);
//        sresult_relative_tab.setVisibility(View.GONE);
//    }
//
//    private void showListvieLoadddingContent() {
//        sresult_lv.setVisibility(View.VISIBLE);
//        loadding_listview_linear.setVisibility(View.GONE);
//        include_net_error_linear.setVisibility(View.GONE);
//    }
//
//    //网络请求
//
//    /**
//     * 按筛选条件请求数据
//     */
//    private void requestDrug() {
//        if (placedData == null) {//加层保护
//            return;
//        }
//        String yibao = sresult_tab_nayao.getText().toString();
//        if (area.equals("北京市")) {//如果是全北京传空串
//            area = "";
//        }
//        if (!TextUtils.isEmpty(street)) {//如果street不是空字符串
//            if (street.indexOf("全") != -1) {//如果street字符串中包含‘全’字，street置空
//                street = "";
//            }
//        }
//        if (yibao.equals("全部医院")) {
//            yibao = "0";
//        } else {
//            yibao = "1";
//        }
//        String sort = "";//排序字符串
//        if (sresult_tab_price.isSelected()) {
//            sort = "price";
//        } else {
//            sort = "nearest";
//        }
//        lat = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.LATITUDE, "");
//        lng = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.LONGITUDE, "");
//        String ybType = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.KEY_OBJ_TYPE_JOB, VAL_DEFAULT_TYPE_JOB);
//        String phone = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PHONE, "");
//        Map<String, String> params = new HashMap<>();
//        params.put("app", "Home");
//        params.put("class", "ResultsHospital");
//        String sign = MD5ChangeUtil.Md5_32("HomeResultsHospital" + HttpConstant.APP_SECRET);
//        params.put("sign", sign);
//        String drugNameID = placedData.getDrugNameID();
//        if (TextUtils.isEmpty(drugNameID)) {
//            drugNameID = "";
//        }
//        params.put("DrugNameID", drugNameID);
//        String venderId = placedData.getVenderID();
//        if (TextUtils.isEmpty(venderId)) {
//            venderId = "";
//        }
//        params.put("VenderID", venderId);
//        String specificationsID = placedData.getSpecificationsID();
//        if (TextUtils.isEmpty(specificationsID)) {
//            specificationsID = "";
//        }
//        params.put("SpecificationsID", specificationsID);
//        params.put("pageIndex", leftPage + "");
//        params.put("phone", phone);
//        params.put("lat", lat);
//        params.put("lng", lng);
//        params.put("ybtype", ybType);
//        params.put("area", area);
//        params.put("street", street);
//        params.put("yibao", yibao);//
//        params.put("sort", sort);
//        params.put("province", "北京");
//        if (drugId != null) {
//            params.put("drug_id", drugId);
//        }
//        if (!NetworkUtil.CheckConnection(this)) {
//            include_net_error_linear.setVisibility(View.VISIBLE);
//            sresult_lv.setVisibility(View.GONE);
//            include_net_setting.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (!NetworkUtil.CheckConnection(ResultsAdoptActivity.this)) {
//                        showNetDialog();
//                        return;
//                    }
//                    requestDrug();
//                }
//            });
//            return;
//        } else {
//            include_net_error_linear.setVisibility(View.GONE);
//            sresult_lv.setVisibility(View.VISIBLE);
//        }
//        request(params, HttpConstant.RESULTS_HOSPITAL_LIST);
//    }
//
//    private void switchToHospitalDrugDetailActivity(HospitalBean hospitalBean) {
//        popTransparency();                                      //AboutHospitalOrCommunityActivity   评论
//        if (hospitalBean != null && placedData != null) {   //HospitalDrugDetailActivity         之前的动画
//            Intent intent = new Intent(this, HospitalDrugDetailActivity.class);
//            intent.putExtra(Contants.KEY_OBJ_HOSPITALBEAN, hospitalBean);
//            intent.putExtra(Contants.KEY_OBJ_PLACEDATA, placedData);
//            startActivity(intent);
//        } else {
//            ToastUtil.showShort(this, "药品信息为空！");
//            if (popTransparency != null) {
//                popTransparency.dismiss();
//                popTransparency = null;
//                isDonePlan = false;
//            }
//        }
//    }
//
//    @Override
//    public void OnChildLeftClickListener(int position, HospitalBean item) {
//        if (isDonePlan == false) {
//            isDonePlan = true;
//            EventBus.getDefault().post(new Event("e008"));
//            switchToHospitalDrugDetailActivity(item);
//        }
//    }
//
//    @Override
//    public void OnChildRightClickListener(int position, HospitalBean item) {
//        EventBus.getDefault().post(new Event("e005"));
//        startNavi(item.getLat(), item.getLng(), item.getName());
//    }
//
//
//    /**
//     * 启动百度地图导航(Native)
//     */
//    public void startNavi(String latTarget, String lngTarget, String addressTarget) {
//        String address = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.ADDRESS, "");
//        LatLng pt1;
//        LatLng pt2;
//        if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lng)) {
//            pt1 = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
//        } else {
//            ToastUtil.showToastCenter(ResultsAdoptActivity.this, "医院经纬度为空，无法导航");
//            return;
//        }
//        if (!TextUtils.isEmpty(latTarget) && !TextUtils.isEmpty(lngTarget)) {
//            pt2 = new LatLng(Double.parseDouble(latTarget), Double.parseDouble(lngTarget));
//        } else {
//            ToastUtil.showShort(ResultsAdoptActivity.this, "我的定位经纬度为空");
//            return;
//        }
//        NaviParaOption para = new NaviParaOption()
//                .startPoint(pt1).endPoint(pt2)
//                .startName(address).endName(addressTarget);
//        try {
//            BaiduMapNavigation.openBaiduMapNavi(para, this);
//        } catch (BaiduMapAppNotSupportNaviException e) {
//            e.printStackTrace();
//            showDialog();
//        }
//    }
//
//    /**
//     * 提示未安装百度地图app或app版本过低
//     */
//    public void showDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
//        builder.setTitle("提示");
//        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                OpenClientUtil.getLatestBaiduMapApp(ResultsAdoptActivity.this);
//            }
//        });
//
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.create().show();
//    }
//
//
//    private void popTransparency() {
//        isDonePlan = true;
//        int w = ScreenUtils.getScreenWidth() - DensityUtils.dp2px(30);
//        popTransparency = new BasePopupWindow(this, R.layout.pop_transparency, w, ViewGroup.LayoutParams.MATCH_PARENT);
//        isDonePlan = true;
//        popTransparency.showPopupWindow(sresult_lv, Gravity.CENTER);
//    }
//
//    /**
//     * 滑动显示动画
//     */
//    private void showTouchView() {
//        if (isShowing) {
//            topHeight = linear_content_top.getHeight();
//            return;
//        }
//        isShowing = true;
//        isHiddening = false;
//        isHidden = false;
//        ValueAnimator animator = createDropAnimator(linear_content_top, 0, topHeight);
//        animator.setDuration(TIME_ANIM);
//        animator.addListener(ResultsAdoptActivity.this);
//        animator.start();
//        int bottomHeight = sresult_relative_tab.getHeight();
//        TranslateAnimation animatorBottom = new TranslateAnimation(0, 0, bottomHeight, 0);
//        animatorBottom.setInterpolator(new LinearInterpolator());
//        animatorBottom.setDuration(TIME_ANIM);
//        sresult_relative_tab.startAnimation(animatorBottom);
//        sresult_relative_tab.setVisibility(View.VISIBLE);
//        linear_content_top.setVisibility(View.VISIBLE);
//    }
//
//    private ValueAnimator createDropAnimator(final View v, int start, int end) {
//        ValueAnimator animator = ValueAnimator.ofInt(start, end);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator arg0) {
//                Integer animatedValue = (Integer) arg0.getAnimatedValue();
//                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
//                layoutParams.height = animatedValue;
//                v.setLayoutParams(layoutParams);
//            }
//        });
//        return animator;
//    }
//
//    /**
//     * 滑动隐藏动画
//     */
//    private void hiddenTouchView() {
//        if (isHiddening) {
//            return;
//        }
//        isShowing = false;
//        isHiddening = true;
//        if (hidden) {
//            topHeight = linear_content_top.getHeight();
//            hidden = false;
//        }
//        isOneHide = true;
//        ValueAnimator animator = createDropAnimator(linear_content_top, topHeight, 0);
//        animator.setDuration(TIME_ANIM);
//        animator.addListener(ResultsAdoptActivity.this);
//        animator.start();
//        int bottomHieght = sresult_relative_tab.getHeight();
//        TranslateAnimation animatorBottom = new TranslateAnimation(0, 0, 0, bottomHieght);
//        animatorBottom.setInterpolator(new LinearInterpolator());
//        animatorBottom.setDuration(TIME_ANIM);
//        sresult_relative_tab.startAnimation(animatorBottom);
//        sresult_relative_tab.setVisibility(View.GONE);
//    }
//
//    /**
//     * 弹出职位状态
//     */
//    private void popupJobType() {
//        ObjectAnimator animator = ObjectAnimator.ofFloat(sresult_titlebar_content_iv, "rotation", 0F, 180F);
//        animator.setDuration(300);
//        animator.start();
//        pop_linear_top.setVisibility(View.VISIBLE);
//        View conentView = View.inflate(this, R.layout.popup_type_job, null);
//        popJob = new PopupWindow(conentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        String[] typeJobs = getResources().getStringArray(R.array.type_job);
//        String[] typeJobsType = getResources().getStringArray(R.array.type_job_status);
//        List<TypeJobStatus> list = new ArrayList<TypeJobStatus>();
//        for (int i = 0; i < typeJobs.length; i++) {
//            TypeJobStatus s = new TypeJobStatus();
//            typeJob = (String) SharePreferenceUtil.get(ResultsAdoptActivity.this, SharePreferenceUtil.KEY_OBJ_TYPE_JOB, VAL_DEFAULT_TYPE_JOB);
//            s.setTypeJob(typeJobsType[i]);
//            if (typeJob.equals(typeJobsType[i])) {
//                s.setSelected(true);
//                SharePreferenceUtil.put(ResultsAdoptActivity.this, SharePreferenceUtil.KEY_OBJ_TYPE_JOB, s.getTypeJob());
//            } else {
//                s.setSelected(false);
//            }
//            s.setDesc(typeJobs[i]);
//            list.add(s);
//        }
//        ListView jobTypeListView = (ListView) conentView.findViewById(R.id.pop_typejob_lv);
//        typeJobStatusAdapter = new TypeJobStatusAdapter(this, list, R.layout.item_pop_type_job);
//        jobTypeListView.setAdapter(typeJobStatusAdapter);
//        jobTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(final AdapterView<?> adapterView, View view, final int i, long l) {
//                TypeJobStatus ts = (TypeJobStatus) adapterView.getItemAtPosition(i);
//                ts.setSelected(true);
//                typeJob = ts.getTypeJob();
//                SharePreferenceUtil.put(ResultsAdoptActivity.this, SharePreferenceUtil.KEY_OBJ_TYPE_JOB, typeJob);
//                typeJobStatusAdapter.updateItem(i, ts);
//                sresult_titlebar_content.setText(ts.getDesc());
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(300);
//                            mHandler.sendEmptyMessage(1);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//                showTouchView();
//            }
//        });
//        popJob.setAnimationStyle(R.style.PopStyleTranslate);
//        popJob.showAsDropDown(sresult_titlebar_relative, 0, 0);
//        conentView.findViewById(R.id.pop_out).setOnClickListener(typeJobOnClickListener);
//    }
//
//    private void refreshUI() {
//        isEnding = false;
//        isTouch = true;
//        leftPage = 0;
//        cLeftPage = -1;
//        if (footer_ybtlv_linear != null) {
//            footer_ybtlv_linear.setVisibility(View.GONE);
//        }
//        leftList = new ArrayList<>();
//        mAdapter = new ResultsNayaoAdapter(ResultsAdoptActivity.this, leftList, R.layout.item_results_nayao, ResultsAdoptActivity.this);
//        sresult_lv.setAdapter(mAdapter);
//        requestDrug();
//    }
//
//    private void dismissPopJob() {
//        pop_linear_top.setVisibility(View.GONE);
//        popJob.dismiss();
//        popJob = null;
//        ObjectAnimator animator = ObjectAnimator.ofFloat(sresult_titlebar_content_iv, "rotation", 180F, 0F);
//        animator.setDuration(300);
//        animator.start();
//    }
//
//    private View.OnClickListener typeJobOnClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.pop_out:
//                    dismissPopJob();
//                    break;
//            }
//        }
//    };
//
//    @Override
//    public void onBackPressed() {
//        if (pop_linear_city.getVisibility() == View.VISIBLE || pop_linear_hospital.getVisibility() == View.VISIBLE) {
//            dismissPopBottom();
//            return;
//        }
//        if (popJob != null) {
//            dismissPopJob();
//            return;
//        }
//        super.onBackPressed();
//    }
//
//    /**
//     * 隐藏弹窗，底部弹出，tab弹出
//     */
//    private void dismissPopBottom() {
//        sresult_tab_address.setSelected(false);
//        sresult_tab_nayao.setSelected(false);
//        TranslateAnimation animation1 = new TranslateAnimation(0, 0, 0,pop_linear_content.getHeight());
//        animation1.setInterpolator(new LinearInterpolator());
//        animation1.setDuration(500);
//        pop_linear_content.startAnimation(animation1);
//        animation1.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                pop_linear_city.setVisibility(View.GONE);
//                pop_linear_hospital.setVisibility(View.GONE);
//                pop_linear.setVisibility(View.GONE);
//                int bottomHieght=sresult_relative_tab.getHeight();
//                TranslateAnimation animatorBottom = new TranslateAnimation(0, 0, bottomHieght, 0);
//                animatorBottom.setInterpolator(new LinearInterpolator());
//                animatorBottom.setDuration(TIME_ANIM);
//                sresult_relative_tab.startAnimation(animatorBottom);
//                animatorBottom.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//                        sresult_view_out.setVisibility(View.INVISIBLE);
//                        sresult_tab_nayao_cancel.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        sresult_relative_tab.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//    }
//
//    @Override
//    public void onAnimationStart(Animator animator) {
//
//    }
//
//    @Override
//    public void onAnimationEnd(Animator animator) {
//        if (isUp) {
//            linear_content_top.clearAnimation();
//            isHidden = true;
//        }
//        isAnim = false;
//    }
//
//    @Override
//    public void onAnimationCancel(Animator animator) {
//
//    }
//
//    @Override
//    public void onAnimationRepeat(Animator animator) {
//
//    }
//}
