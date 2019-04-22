package com.lzyc.ybtappcal.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.activity.mine.medicine.MedicinePersonActivity;
import com.lzyc.ybtappcal.activity.reimbursement.ReimbursementDetailsActivity;
import com.lzyc.ybtappcal.activity.top.DrugNoDetailActivity;
import com.lzyc.ybtappcal.activity.top.ResultsActivity;
import com.lzyc.ybtappcal.bean.DrugBean;
import com.lzyc.ybtappcal.bean.HistoryCache;
import com.lzyc.ybtappcal.bean.MedicineFamilyBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.fragment.TopFragment;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.PermissionUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.cache.SPCacheList;
import com.lzyc.ybtappcal.view.dialog.LoadingProgressBar;
import com.lzyc.ybtappcal.view.scan.ScanBoxView;
import com.lzyc.ybtappcal.view.scan.ScanView;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.ybtappcal.widget.popupwindow.PopupWindowTwoButton;
import com.lzyc.zxing.decoding.RGBLuminanceSourcee;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 扫码
 * Created by yang on 2017/06/12.
 */
public class ScanActivity extends BaseActivity implements QRCodeView.Delegate, ScanView.OnScanCodeListener {
    private static final String TAG = ScanActivity.class.getSimpleName();
    private static final int REQUESTCODE_IMAGE = 100;
    private static final int TIME_ANIM = 400;

    @BindView(R.id.scan_view)
    ScanView scanView;
    @BindView(R.id.scan_view_box)
    ScanBoxView scanViewBox;
    @BindView(R.id.titlebar_back)
    TextView titlebarBack;
    @BindView(R.id.titlebar_content)
    TextView titlebarContent;
    @BindView(R.id.titlebar_scan_help)
    ImageButton titlebarScanHelp;
    @BindView(R.id.tv_top)
    TextView tvTop;
    @BindView(R.id.tv_bottom)
    TextView tvBottom;
    @BindView(R.id.tv_flash)
    TextView tvFlash;
    @BindView(R.id.tv_image_selected)
    TextView tvImageSelected;
    @BindView(R.id.tv_scan_history)
    TextView tvScanHistory;
    @BindView(R.id.tv_drugbag)
    TextView tvDrugbag;
    @BindView(R.id.iv_shot_image_top)
    ImageView ivShotImageTop;
    @BindView(R.id.iv_shot_image_bottom)
    ImageView ivShotImageBottom;
    @BindView(R.id.iv_scan_success)
    ImageView ivScanSuccess;
    @BindView(R.id.relative_scan)
    RelativeLayout relativeScan;
    @BindString(R.string.torch_on)
    String torchOn;
    @BindString(R.string.torch_off)
    String torchOff;

    private LoadingProgressBar mLoadingProgressBar;
    private Bitmap bitmapTop, bitmapBottom;
    private MedicineFamilyBean.ListBean bean;
    private String memberId;
    private String resultCode;
    private Drawable drawableToptNor, drawableTopPre;
    private boolean isFlashlight = false;
    private int typePage;
    private int cutHeight;//切割线所在高度,也就是顶部距离切割线的高度
    private int underSplitHeight;//底部距离切割线的高度
    private boolean isClose = false;
    private Handler mHandler = new Handler();

    @Override
    public int getContentViewId() {
        return R.layout.activity_scan;
    }

    @Override
    protected void init() {
        setTitleBarVisibility(View.GONE);
        Bundle mBundle = getIntent().getExtras();
        cutHeight = mBundle.getInt(Contants.KEY_POSITION_CUT);
        mLoadingProgressBar = new LoadingProgressBar(this);
        underSplitHeight = TopFragment.bitmap.getHeight() - cutHeight;
        bitmapTop = Bitmap.createBitmap(TopFragment.bitmap, 0, 0, TopFragment.bitmap.getWidth(), cutHeight);
        bitmapBottom = Bitmap.createBitmap(TopFragment.bitmap, 0, cutHeight, TopFragment.bitmap.getWidth(), this.underSplitHeight);
        if (bitmapTop != null) {
            ivShotImageTop.setImageBitmap(bitmapTop);
        }
        if (bitmapBottom != null) {
            ivShotImageBottom.setImageBitmap(bitmapBottom);
        }
        if (TopFragment.bitmap != null) {
            TopFragment.bitmap.recycle();
            TopFragment.bitmap = null;
        }
        typePage = mBundle.getInt(Contants.KEY_PAGE_SCAN);
        switch (typePage) {
            case Contants.VAL_PAGE_SEARCH_DURUG://药品查询
                titlebarBack.setText("药品查询");
                tvDrugbag.setVisibility(View.GONE);
                tvScanHistory.setVisibility(View.VISIBLE);
                break;
            case Contants.VAL_PAGE_SEARCH_DRUG_ADD://添加用药
                titlebarBack.setText("添加用药");
                bean = (MedicineFamilyBean.ListBean) getIntent().getExtras().getSerializable(Contants.KEY_MEDICINE_BEAN);
                memberId = bean.getID();
                tvDrugbag.setText(bean.getNickname());
                tvDrugbag.setVisibility(View.VISIBLE);
                tvScanHistory.setVisibility(View.GONE);
                break;
        }
        tvTop.setBackgroundResource(R.color.color_333333_50);
        tvBottom.setBackgroundResource(R.color.color_333333_50);
        drawableToptNor = getResources().getDrawable(R.mipmap.icon_scan_flashlight_nor);
        drawableToptNor.setBounds(0, 0, drawableToptNor.getMinimumWidth(), drawableToptNor.getMinimumHeight());
        drawableTopPre = getResources().getDrawable(R.mipmap.icon_scan_flashlight_pre);
        drawableTopPre.setBounds(0, 0, drawableTopPre.getMinimumWidth(), drawableTopPre.getMinimumHeight());
        scanView.startSpot();
        scanView.setDelegate(ScanActivity.this);
        scanView.setOnScanCodeListener(ScanActivity.this);
        requestCODE_CAMERA();
        playInitAnimaiton();
        IntentFilter filter = new IntentFilter();
        filter.addAction("error");
        registerReceiver(receiver, filter);
    }

    /**
     * 播放展开动画
     */
    private void playInitAnimaiton() {
        relativeScan.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onLayoutTvBottom();
                onLayoutTvTop();
                ObjectAnimator animator = ObjectAnimator.ofFloat(ivShotImageTop, "translationY", 0, -cutHeight);
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(ivShotImageBottom, "translationY", 0, underSplitHeight);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(tvTop, "translationY", 0, -cutHeight);
                ObjectAnimator animator3 = ObjectAnimator.ofFloat(tvBottom, "translationY", 0, underSplitHeight);
                ObjectAnimator animator4 = ObjectAnimator.ofFloat(scanViewBox, "scaleX", 0, 1);
                ObjectAnimator animator5 = ObjectAnimator.ofFloat(scanViewBox, "scaleY", 0, 1);
                AnimatorSet animSet = new AnimatorSet();
                animSet.play(animator).with(animator1).with(animator2).with(animator3).with(animator4).with(animator5);
                animSet.setDuration(TIME_ANIM);
                animSet.start();
                animSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        ivShotImageTop.setVisibility(View.GONE);
                        ivShotImageBottom.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                relativeScan.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }


    private void onLayoutTvBottom() {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(120));
        lp.topMargin = cutHeight - DensityUtils.dp2px(120);
        tvBottom.setLayoutParams(lp);
    }

    private void onLayoutTvTop() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(70));
        layoutParams.topMargin = cutHeight;
        tvTop.setLayoutParams(layoutParams);
    }

    /**
     * 震动
     */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    protected void onStop() {
        scanView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        scanView.startSpot();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scanView.startCamera();
                scanViewBox.drawViewfinder();
            }
        }, 500);
        tvFlash.setCompoundDrawables(null, drawableToptNor, null, null);
        tvFlash.setText(torchOn);
        isFlashlight = false;
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        //这个回调不做处理
    }

    @Override
    public void onScanSuccess(String result, Bitmap bitmap) {
        LogUtil.y("onScanSuccess  code " + result + "  Bitmap " + bitmap);
        if(TextUtils.isEmpty(result)){
            scanAgain();
            return;
        }
        vibrate();
        this.resultCode = result;
        mLoadingProgressBar.show();
        ivScanSuccess.setVisibility(View.VISIBLE);
        scanView.stopCamera();
        scanViewBox.closeDrawViewfinder();//停止网格动画
        switch (typePage) {
            case Contants.VAL_PAGE_SEARCH_DURUG://药品查询
                //有一个问题，会出现相机预览的尺寸与屏幕尺寸不匹配,暂时不给预览图
                break;
            case Contants.VAL_PAGE_SEARCH_DRUG_ADD://添加用药
                Rect rect = scanViewBox.getFrame();
                Bitmap bitmapScan = null;
                try {
                    bitmapScan = Bitmap.createBitmap(bitmap, rect.left + 10, rect.top - 15, rect.width(), rect.height());
                } catch (IllegalArgumentException e) {
                    bitmapScan = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), rect.height());
                } catch (Exception e) {

                }
                if (bitmapScan != null) {
                    ivScanSuccess.setImageBitmap(bitmapScan);
                }
                break;
        }
        requestDrug(result);
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        LogUtil.y("相机打开失败");
        sendBroadcast(new Intent("error"));
    }

    /**
     * 打开摄像头失败广播
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            PermissionUtil.getInstance().showSaoma(ScanActivity.this);
        }
    };

    @OnClick({R.id.titlebar_back, R.id.titlebar_scan_help, R.id.tv_flash, R.id.tv_image_selected, R.id.tv_scan_history, R.id.tv_drugbag, R.id.iv_scan_success})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.titlebar_back:
                if (!isClose) {
                    closeActivity();
                }
                break;
            case R.id.titlebar_scan_help:
                switchScanHelpActivity();
                break;
            case R.id.tv_flash:
                requestEventCode("j002");
                if (isFlashlight) {
                    tvFlash.setCompoundDrawables(null, drawableToptNor, null, null);
                    tvFlash.setText(torchOn);
                    isFlashlight = false;
                    scanView.closeFlashlight();
                } else {
                    tvFlash.setCompoundDrawables(null, drawableTopPre, null, null);
                    tvFlash.setText(torchOff);
                    isFlashlight = true;
                    scanView.openFlashlight();
                }
                break;
            case R.id.tv_image_selected:
                requestEventCode("j003");
                try {
                    selectedImage();
                } catch (Exception e) {
                    LogUtil.e(TAG, "error:switchSelectedImage.\n" + e.getMessage());
                }
                break;
            case R.id.tv_scan_history://扫描记录，扫描历史
                requestEventCode("j004");
                Bundle mBundle = new Bundle();
                mBundle.putInt(Contants.KEY_PAGE_SEARCH, typePage);
                openActivity(CaptureHistoryActivity.class, mBundle);
                break;
            case R.id.tv_drugbag://某人的药箱
                requestEventCode("a-4009");
                mBundle = new Bundle();
                mBundle.putSerializable(Contants.KEY_MEDICINE_BEAN, bean);
                openActivity(MedicinePersonActivity.class, mBundle);
                ScanActivity.this.finish();
                sendBroadcast(new Intent().setAction(Contants.ACTION_NAME_DRUG_ADD_FINISH));
                sendBroadcast(new Intent().setAction(Contants.ACTION_NAME_MEDICINEPERSION_FINISH));
                break;
        }
    }

    /**
     * 跳转到相册
     */
    private void selectedImage() throws Exception {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUESTCODE_IMAGE);
    }

    /**
     * 跳转到扫码帮助
     */
    private void switchScanHelpActivity() {
        requestEventCode("j001");
        Bundle bundle = new Bundle();
        bundle.putInt(Contants.KEY_PAGE_SEARCH, typePage);
        if (typePage == Contants.VAL_PAGE_SEARCH_DRUG_ADD) {
            bundle.putSerializable(Contants.KEY_MEDICINE_BEAN, bean);
        }
        openActivityNoAnim(ScanHelpActivity.class, bundle);
    }

    /**
     * 扫码
     *
     * @param code
     */
    private void requestDrug(String code) {
        String currentProvince = "";
        switch (typePage) {
            case Contants.VAL_PAGE_SEARCH_DRUG_ADD:
            case Contants.VAL_PAGE_SEARCH_DURUG:
                currentProvince = (String) SharePreferenceUtil.get(ScanActivity.this, SharePreferenceUtil.PROVICE_TOP_CHOOSE, "北京");
                break;
            case Contants.VAL_PAGE_SEARCH_REIMBURSEMENT:
                currentProvince = (String) SharePreferenceUtil.get(ScanActivity.this, SharePreferenceUtil.PROVICE_JIUZHEN, "北京");
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
        params.put(HttpConstant.PARAM_KEY_PAGEINDEX2, Contants.STR_0);
        LogUtil.y("##############params#####" + params.toString());
        request(params, HttpConstant.SEARCH_DRUG_LIST);
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
        request(params, HttpConstant.MINE_DURG_BAG_ADD);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        mLoadingProgressBar.dismiss();
        HistoryCache historyCache = new HistoryCache();
        switch (what) {
            case HttpConstant.SEARCH_DRUG_LIST:
                LogUtil.y("等到数据" + response.toString());
                try {
                    JSONObject data = response.getJSONObject(HttpConstant.DATA);
                    int count = data.getInt(HttpConstant.COUNT);
                    Type typeD = new TypeToken<ArrayList<DrugBean>>() {
                    }.getType();
                    List<DrugBean> sList = JsonUtil.getListModle(data.toString(), HttpConstant.INFO, typeD);
                    DrugBean drugBean = sList.get(0);
                    if (typePage == Contants.VAL_PAGE_SEARCH_DRUG_ADD) {
                        if (null != drugBean) {
                            if (drugBean.getMedicineChest().equals(Contants.STR_1)) {
                                showPrompt(scanViewBox, "该药品已添加到药箱!");
                            } else {
                                if (drugBean.getType() == 1) {
                                    showPrompt(scanViewBox, "很抱歉，找不到本条形码的药品");
                                } else {
                                    ivScanSuccess.setVisibility(View.VISIBLE);
                                    popWindowDrugBagAdd(drugBean.getDrugID());
                                }
                            }
                        } else {
                            showPrompt(scanViewBox, "很抱歉，找不到本条形码的药品");
                        }
                        return;
                    }
                    switch (count) {
                        case 0://这里不存入历史
                            switchScancodeErrorActivity();
                            break;
                        case 1:
                            historyCache.setCode(resultCode);
                            historyCache.setImageUrl(drugBean.getImage());
                            historyCache.setName(drugBean.getName());
                            historyCache.setGoodsName(drugBean.getGoodsName());
                            historyCache.setId(drugBean.getDrugID());
                            switchActivity(drugBean);
                            break;
                        default:
                            if (drugBean != null) {
                                historyCache.setCode(resultCode);
                                historyCache.setImageUrl(drugBean.getImage());
                                historyCache.setName(drugBean.getName());
                                historyCache.setId(drugBean.getDrugID());
                                historyCache.setGoodsName(drugBean.getGoodsName());
                            }
                            switchSearchActivity();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                historyCache.setCacheType(HistoryCache.TYPE_CACHE_SCAN);
                SPCacheList.getInstance().writeDrugScanHistory(historyCache);
                break;
            case HttpConstant.MINE_DURG_BAG_ADD:
                addDugBag();
                break;
            default:
                break;
        }
    }

    PathMeasure mPathMeasure;
    private float[] mCurrentPosition = new float[2];

    /**
     * 把药品添加到小药箱的抛物线动画
     */
    private void addDugBag() {
        final ImageView imageDrug = new ImageView(this);
        Drawable drawable = ivScanSuccess.getDrawable();
        imageDrug.setImageDrawable(drawable);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        relativeScan.addView(imageDrug, params);
        int[] parentLocation = new int[2];
        relativeScan.getLocationInWindow(parentLocation);
        int startLoc[] = new int[2];
        ivScanSuccess.getLocationInWindow(startLoc);
        int endLoc[] = new int[2];
        tvDrugbag.getLocationInWindow(endLoc);
        float startX = startLoc[0] - parentLocation[0];
        float startY = startLoc[1] - parentLocation[1] + ivScanSuccess.getHeight() / 2;
        float toX = endLoc[0] - parentLocation[0] - DensityUtils.dp2px(140);
        float toY = endLoc[1] - parentLocation[1] - DensityUtils.dp2px(90);
        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo((startX + toX) / 2, startY, toX, toY);
        mPathMeasure = new PathMeasure(path, false);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                imageDrug.setTranslationX(mCurrentPosition[0]);
                imageDrug.setTranslationY(mCurrentPosition[1]);
            }
        });
        AnimatorSet animSet = new AnimatorSet();
        valueAnimator.addListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animator) {

            }

            @Override
            public void onAnimationEnd(android.animation.Animator animator) {
                imageDrug.setVisibility(View.GONE);
                ivScanSuccess.setVisibility(View.GONE);
                scanViewBox.drawViewfinder();
                scanView.startSpot();
                scanView.startCamera();
            }

            @Override
            public void onAnimationCancel(android.animation.Animator animator) {

            }

            @Override
            public void onAnimationRepeat(android.animation.Animator animator) {

            }
        });

        ObjectAnimator scalex = ObjectAnimator.ofFloat(imageDrug, View.SCALE_X, 1.0f, 0.0f);
        ObjectAnimator scaley = ObjectAnimator.ofFloat(imageDrug, View.SCALE_Y, 1.0f, 0.0f);
        animSet.playTogether(valueAnimator, scalex, scaley);
        animSet.setDuration(900);
        animSet.start();
    }


    @Override
    public void error(String errorMsg) {
        mLoadingProgressBar.dismiss();
        try {
            ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);
            if(errorResponse.isNetError()){
                showToast("网络不给力");
                scanAgain();
                return;
            }
            int what = errorResponse.getWhat();
            switch (what) {
                case HttpConstant.MINE_DURG_BAG_ADD:
                    if (!TextUtils.isEmpty(errorResponse.getMsg())) {
                        showPrompt(relativeScan, errorResponse.getMsg());
                    } else {
                        showPrompt(relativeScan, "很抱歉，找不到本条形码的药品");
                    }
                    break;
                case HttpConstant.SEARCH_DRUG_LIST:
                    if (typePage == Contants.VAL_PAGE_SEARCH_DRUG_ADD) {
                        showToast("未识别数据");
                        scanAgain();
                    } else {
                        switchScancodeErrorActivity();
                    }
                    break;
                default:
                    switchScancodeErrorActivity();
                    break;
            }
        } catch (Exception e) {

        }

    }

    /**
     * 重新扫码
     */

    private void scanAgain() {
        ivScanSuccess.setVisibility(View.GONE);
        scanViewBox.drawViewfinder();
        scanView.startSpot();
        scanView.startCamera();
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
                ivScanSuccess.setVisibility(View.GONE);
                scanView.startSpot();
                scanViewBox.drawViewfinder();
            }
        });
        twoButton.showPopupWindow(relativeScan, Gravity.CENTER);
    }

    private void switchScancodeErrorActivity() {
        if (typePage == Contants.VAL_PAGE_SEARCH_DURUG) {
            requestEventCode("a010");//本地扫码失败
        } else {
            requestEventCode("b006");
        }
        Bundle bundle = new Bundle();
        bundle.putString(Contants.KEY_RESULT_CODE, resultCode);
        bundle.putInt(Contants.KEY_PAGE_SEARCH, typePage);
        openActivity(ScancodeErrorActivity.class, bundle);
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
                    LogUtil.e(TAG, "error:页面来源不明");
                    break;
            }
        } else if (type == 1) {
            switchDrugNoDetailActivity(drugBean);
        } else {
            LogUtil.e(TAG, "error,default switchResultActivity");
        }
    }

    /**
     * 切换到搜索结果页
     *
     * @param drugBean
     */
    private void switchResultsActivity(DrugBean drugBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Contants.KEY_OBJ_DRUGBEAN, drugBean);
        bundle.putInt(Contants.KEY_PAGE_RESULTS, typePage);
        openActivity(ResultsActivity.class, bundle);
    }

    private void switchDrugNoDetailActivity(DrugBean drugBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Contants.KEY_OBJ_DRUGBEAN, drugBean);
        openActivity(DrugNoDetailActivity.class, bundle);
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
     * 关闭界面
     */
    private void closeActivity() {
        isClose = true;
        ivShotImageTop.setImageBitmap(bitmapTop);
        ivShotImageBottom.setImageBitmap(bitmapBottom);
        ivShotImageTop.setVisibility(View.VISIBLE);
        ivShotImageBottom.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(ivShotImageTop, "translationY", -cutHeight, 0);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(ivShotImageBottom, "translationY", underSplitHeight, 0);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(tvTop, "translationY", -cutHeight, 0);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(tvBottom, "translationY", underSplitHeight, 0);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator).with(animator1).with(animator2).with(animator3);
        animSet.setDuration(TIME_ANIM);
        animSet.start();
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                scanViewBox.closeDrawViewfinder();
                scanView.onDestroy();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ScanActivity.this.finish();
                        overridePendingTransition(0, 0);
                    }
                },500);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUESTCODE_IMAGE:
                    String filePath = "$";
                    int sdkInt = Build.VERSION.SDK_INT; //兼容4.4系统的问题处理
                    Uri contentUri = data.getData();
                    if (sdkInt >= 19 && DocumentsContract.isDocumentUri(ScanActivity.this, contentUri)) {
                        String wholeID = DocumentsContract
                                .getDocumentId(contentUri);
                        String id = wholeID.split(":")[1];
                        String[] column = {MediaStore.Images.Media.DATA};
                        String sel = MediaStore.Images.Media._ID + "=?";
                        Cursor cursor = ScanActivity.this.getContentResolver().query(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                column, sel, new String[]{id}, null);
                        int columnIndex = cursor.getColumnIndex(column[0]);
                        if (cursor.moveToFirst()) {
                            filePath = cursor.getString(columnIndex);
                        }
                        cursor.close();
                    } else if (!TextUtils.isEmpty(contentUri.getAuthority())) {
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
                    try {
//                        Bitmap fileBitmap = BitmapUtil.compress(filePath);
                        scanView.setVisibility(View.VISIBLE);
                        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//                        fileBitmap = BitmapUtil.compress(filePath);
//                        id_capture_scanbackground.setVisibility(View.VISIBLE);
//                        id_capture_scanbackground.setImageBitmap(fileBitmap);
                        long bitmapsize = getBitmapsize(bitmap);
                        if (bitmapsize > Contants.SCAN_DEFAULT_SIZE) {  //大图
                            analysisRequestData(filePath, false);
                        } else {  //小图
                            analysisRequestData(filePath, true);
                        }
                        bitmap.recycle();
//                        int width = fileBitmap.getWidth();
//                        int height = fileBitmap.getHeight();
//                        int[] pixels = new int[width * height];
//                        fileBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
//                        Image image = new Image(width, height, "Y800");
//                        image.setData(pixels);
//                        int result = scanView.getScanner().ivScanSuccess(image);
//                        String code=scanView.processData(image);
//                        LogUtil.y(code+"#########result"+result);
//                       onScanSuccess(result, fileBitmap);//根据code,执行网络请求
                    } catch (Exception e) {
                        showToast("图片识别失败");
                    }
                    break;
                default:
                    LogUtil.d(TAG, "onActivityResult,error,default.");
                    break;
            }
        }
    }

    /**
     * 获取bitmap的size
     *
     * @param bitmap
     * @return
     */
    public long getBitmapsize(Bitmap bitmap) {
        if (null == bitmap) {
            return 0;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();

    }

    private void switchSearchActivity() {
        requestEventCode("a003");
        Bundle mBundle = new Bundle();
        mBundle.putString(Contants.KEY_STR_KEYWORD_CODE, resultCode);
        mBundle.putInt(Contants.KEY_PAGE_SEARCH, Contants.VAL_PAGE_SEARCH_SCAN);
        openActivity(SearchActivity.class, mBundle);
    }

    /**
     * 解析图片
     *
     * @param filePath
     * @throws FileNotFoundException
     * @throws NotFoundException
     */
    private void analysisRequestData(String filePath, boolean isAvailableSize) throws FileNotFoundException, NotFoundException {
        RGBLuminanceSourcee rgbLuminanceSource = new RGBLuminanceSourcee(filePath, isAvailableSize);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));//把可视图片转为二进制图片
        Result result = new MultiFormatReader().decode(binaryBitmap);//解析图片中的code
        onScanSuccess(result.getText(), null);//根据code,执行网络请求
    }


    @Override
    protected void onDestroy() {
        if (null != receiver) {
            unregisterReceiver(receiver);
        }
        if (null != scanView) {
            scanView.onDestroy();
        }
        if(mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (!isClose) {
            closeActivity();
        }
    }

    @AfterPermissionGranted(Contants.REQUEST_PERMISSION_CAMERA)
    public void requestCODE_CAMERA() {//相机
        String perms[] = {Manifest.permission.CAMERA, Manifest.permission.VIBRATE, Manifest.permission.FLASHLIGHT};
        if (!EasyPermissions.hasPermissions(mContext, perms)) {
            EasyPermissions.requestPermissions(this, "医保通申请相机权限", Contants.REQUEST_PERMISSION_CAMERA, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        LogUtil.y("####成功##onPermissionsGranted############" + requestCode);
        for (String perm : perms) {
            LogUtil.y("############perm  " + perm);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        LogUtil.y("####失败####onPermissionsDenied##########" + requestCode);
        for (String perm : perms) {
            LogUtil.y("############perm  " + perm);
        }
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
        twoButton.getTvOk().setText(Contants.STR_PROMPT);
        twoButton.getTv_content().setText(msg);
        twoButton.getTvOk().setVisibility(View.GONE);
        twoButton.getLineTwo().setVisibility(View.GONE);
        twoButton.getTvCancel().setText(Contants.STR_GOT_IT);
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
                scanAgain();
            }
        });
        twoButton.showPopupWindow(v, Gravity.CENTER);
    }

}
