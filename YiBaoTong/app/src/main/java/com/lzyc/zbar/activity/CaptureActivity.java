package com.lzyc.zbar.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.MainActivity;
import com.lzyc.ybtappcal.activity.SearchActivity;
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
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.ybtappcal.widget.popupwindow.PopupWindowTwoButton;
import com.lzyc.zbar.camera.CameraManager;
import com.lzyc.zbar.decoding.CaptureActivityHandler;
import com.lzyc.zbar.decoding.InactivityTimer;
import com.lzyc.ybtappcal.activity.CaptureHistoryActivity;
import com.lzyc.ybtappcal.activity.ScanHelpActivity;
import com.lzyc.ybtappcal.activity.ScancodeErrorActivity;
import com.lzyc.zxing.decoding.RGBLuminanceSourcee;
import com.lzyc.zxing.view.ViewfinderView;
import com.umeng.analytics.MobclickAgent;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.ImageScanner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * zbar
 * 扫码
 */
public class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnClickListener {
    private static  final String TAG=CaptureActivity.class.getSimpleName();
    private boolean flag = true;
    private static final long VIBRATE_DURATION = 200L;
    private ImageButton titleRight;
    private static final int MSG_PERMISSION_CAMERA = 100;
    private static final int MSG_CLEAN_PICTURE = 200;
    private ImageScanner mScanner;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private com.lzyc.zbar.decoding.InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private TextView titleBack;
    private String resultCode;
    private int typePage;
    private String cityYbChoose;
    private LoadingProgressBar mLoadingProgressBar;
    private TextView mOpenLight;//开闪光灯
    private TextView capture_tv_selected_image;
    private Camera camera;
    private Camera.Parameters parameter;
    private boolean isOpen = true;
    private static final int REQUESTCODE_IMAGE = 100;
    private TextView capture_tv_history;
    //动画唯一的长度
    private int midData;
    private int underSplitHeight;
    private Bitmap bitmapTop, bitmapBottom;
    private int bottonHeight;
    private int topHeight;
    private LinearLayout id_linear_capture_top;
    private LinearLayout id_linear_capture_under;
    private TextView id_tv_top_captrue_top_1;
    private TextView id_tv_top_captrue_under_1;
    private boolean isDone = false;
    private ImageView imageScan;
    private Drawable drawableToptNor, drawableTopPre;
    private boolean isNoPERMISSION_CAMERA = false;
    private String lat, lng;
    private ImageView shortImageViewTop;//截屏上
    private ImageView shortImageViewBottom;//截屏下
    private RelativeLayout relativeCapture;//整个布局
    private boolean isClose = false;
    private TextView captureTvDrugbag;
    private SurfaceView surfaceView;
    private MedicineFamilyBean.ListBean bean;
    private String memberId;
    private int what;
    private Bitmap bitmapScan;
   private static final int  REQUEST_CODE_CAMERA=1;
   private static final int  REQUEST_CODE_CAMERA_LIGHT=2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_PERMISSION_CAMERA:
                    isNoPERMISSION_CAMERA = true;
                    showPermissionCAMERA();
                    break;
            }
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.activity_capture;
    }

    @Override
    protected void init() {
        setTitleBarVisibility(View.GONE);
        CameraManager.init(CaptureActivity.this);
        this.hasSurface = false;
        this.inactivityTimer = new InactivityTimer(this);
        initView();
    }

    private void initView() {
        typePage = getIntent().getExtras().getInt(Contants.KEY_PAGE_SEARCH);
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        relativeCapture = (RelativeLayout) findViewById(R.id.relative_capture);
        id_tv_top_captrue_top_1 = (TextView) findViewById(R.id.tv_top);
        id_tv_top_captrue_under_1 = (TextView) findViewById(R.id.id_tv_top_captrue_under_1);
        drawableToptNor = getResources().getDrawable(R.mipmap.icon_scan_flashlight_nor);
        drawableToptNor.setBounds(0, 0, drawableToptNor.getMinimumWidth(), drawableToptNor.getMinimumHeight());
        drawableTopPre = getResources().getDrawable(R.mipmap.icon_scan_flashlight_pre);
        drawableTopPre.setBounds(0, 0, drawableTopPre.getMinimumWidth(), drawableTopPre.getMinimumHeight());
        titleBack = (TextView) findViewById(R.id.title_back);
        titleRight = (ImageButton) findViewById(R.id.title_right);
        imageScan = (ImageView) findViewById(R.id.image_scan);
        titleRight.setOnClickListener(this);
        mOpenLight = (TextView) findViewById(R.id.tv_open_light);
        capture_tv_selected_image = (TextView) findViewById(R.id.capture_tv_selected_image);
        capture_tv_history = (TextView) findViewById(R.id.capture_tv_history);
        captureTvDrugbag = (TextView) findViewById(R.id.capture_tv_drugbag);
        shortImageViewTop = (ImageView) findViewById(R.id.loading_iv_top);
        shortImageViewBottom = (ImageView) findViewById(R.id.loading_iv_bottm);
        id_linear_capture_top = (LinearLayout) findViewById(R.id.id_linear_capture_top);
        id_linear_capture_under = (LinearLayout) findViewById(R.id.id_linear_capture_under);

        String locationCity = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.CITY, "");
        cityYbChoose = (String) SharePreferenceUtil.get(CaptureActivity.this, SharePreferenceUtil.CITY_CANBAO, locationCity);
        mLoadingProgressBar = new LoadingProgressBar(this);
        lat = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.LATITUDE, "");
        lng = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.LONGITUDE, "");
        midData = getIntent().getExtras().getInt("midData");
        underSplitHeight = TopFragment.bitmap.getHeight() - midData;
        mScanner = new ImageScanner();
        mScanner.setConfig(0, Config.X_DENSITY, 3);
        mScanner.setConfig(0, Config.Y_DENSITY, 3);
        id_tv_top_captrue_top_1.setBackgroundResource(R.color.color_333333_50);
        id_tv_top_captrue_under_1.setBackgroundResource(R.color.color_333333_50);
        this.viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        switch (typePage) {
            case Contants.VAL_PAGE_SEARCH_DURUG:
                titleBack.setText("药品查询");
                captureTvDrugbag.setVisibility(View.GONE);
                capture_tv_history.setVisibility(View.VISIBLE);
                break;
            case Contants.VAL_PAGE_SEARCH_REIMBURSEMENT:
                titleBack.setText("报销查询");
                captureTvDrugbag.setVisibility(View.GONE);
                capture_tv_history.setVisibility(View.VISIBLE);
                break;
            case Contants.VAL_PAGE_SEARCH_DRUG_ADD:
                titleBack.setText("添加用药");
                bean = (MedicineFamilyBean.ListBean) getIntent().getExtras().getSerializable(Contants.KEY_MEDICINE_BEAN);
                memberId = bean.getID();
                captureTvDrugbag.setText(bean.getNickname());
                captureTvDrugbag.setVisibility(View.VISIBLE);
                capture_tv_history.setVisibility(View.GONE);
                break;
        }
        titleBack.setOnClickListener(this);
        mOpenLight.setOnClickListener(this);
        capture_tv_selected_image.setOnClickListener(this);
        capture_tv_history.setOnClickListener(this);
        captureTvDrugbag.setOnClickListener(this);
        startAnima();

    }

    private void closeActivity() {
        shortImageViewTop.setImageBitmap(bitmapTop);
        shortImageViewBottom.setImageBitmap(bitmapBottom);
        shortImageViewTop.setVisibility(View.VISIBLE);
        shortImageViewBottom.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(id_linear_capture_top, "translationY", -topHeight, 0);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(id_linear_capture_under, "translationY", bottonHeight, 0);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(id_tv_top_captrue_top_1, "translationY", -topHeight, 0);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(id_tv_top_captrue_under_1, "translationY", bottonHeight, 0);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator).with(animator1).with(animator2).with(animator3);
        animSet.setDuration(400);
        animSet.start();
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(viewfinderView!=null){
                    viewfinderView.closeDrawViewfinder();
                }
                if(handler!=null){
                    handler.quitSynchronously();
                }
                if (mHandler != null) {
                    mHandler.removeCallbacksAndMessages(null);
                }
                if (Contants.VAL_PAGE_SEARCH_DURUG == typePage) {
                    TopFragment.isCaptureBack = true;
                    finish();
                    overridePendingTransition(0, 0);
                    TopFragment.isCreate = true;
                } else if (typePage == Contants.VAL_PAGE_SEARCH_REIMBURSEMENT) {//报销
                    SharePreferenceUtil.put(com.lzyc.zbar.activity.CaptureActivity.this, SharePreferenceUtil.DYNAMIC_SWITCH_TAB, Contants.TAB_DRUG_SEACH);
                    openActivityNoAnim(MainActivity.class);
                } else if (typePage == Contants.VAL_PAGE_SEARCH_DRUG_ADD) {
                    if (bitmapScan != null) {
                        bitmapScan.recycle();
                        bitmapScan = null;
                    }
                    finish();
                    overridePendingTransition(0, 0);
                }


            }
        });
    }

    private void showPermissionCAMERA() {
        if (Build.VERSION.SDK_INT >=23) {
            requestCODE_CAMERA();
        } else {
            PermissionUtil.getInstance().showSaoma(CaptureActivity.this);
        }
    }

    /**
     * 图片切割
     */
    private void cutting() {
        // 切割第一个图
        bitmapTop = Bitmap.createBitmap(TopFragment.bitmap, 0, 0, TopFragment.bitmap.getWidth(), midData);
//        切割第二个图
        bitmapBottom = Bitmap.createBitmap(TopFragment.bitmap, 0, this.midData, TopFragment.bitmap.getWidth(), this.underSplitHeight);
        if (bitmapTop != null) {
            shortImageViewTop.setImageBitmap(bitmapTop);
        }
        if (bitmapBottom != null) {
            shortImageViewBottom.setImageBitmap(bitmapBottom);
        }
        //回收截屏资源
        if (TopFragment.bitmap != null) {
            TopFragment.bitmap.recycle();
            TopFragment.bitmap = null;
        }
    }

    /**
     * 播放动画
     */
    private void startAnima() {
        cutting();
        relativeCapture.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                topHeight = shortImageViewTop.getHeight();   //id_linear_capture_top   id_linear_capture_under
                bottonHeight = shortImageViewBottom.getHeight();
                onLayout1(id_tv_top_captrue_under_1, bottonHeight);
                onLayout2(id_tv_top_captrue_top_1, bottonHeight);
                ObjectAnimator animator = ObjectAnimator.ofFloat(id_linear_capture_top, "translationY", 0, -topHeight);
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(id_linear_capture_under, "translationY", 0, bottonHeight);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(id_tv_top_captrue_top_1, "translationY", 0, -topHeight);
                ObjectAnimator animator3 = ObjectAnimator.ofFloat(id_tv_top_captrue_under_1, "translationY", 0, bottonHeight);
                ObjectAnimator animator4 = ObjectAnimator.ofFloat(viewfinderView, "scaleX", 0, 1);
                ObjectAnimator animator5 = ObjectAnimator.ofFloat(viewfinderView, "scaleY", 0, 1);
                AnimatorSet animSet = new AnimatorSet();
                animSet.play(animator).with(animator1).with(animator2).with(animator3).with(animator4).with(animator5);
                animSet.setDuration(400);
                animSet.start();
                animSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        shortImageViewTop.setVisibility(View.GONE);
                        shortImageViewBottom.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                relativeCapture.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void onLayout1(TextView id_tv_top_captrue_under_1, int bottonHeight) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(120));
        lp.topMargin = topHeight - DensityUtils.dp2px(120);
        id_tv_top_captrue_under_1.setLayoutParams(lp);
    }

    private void onLayout2(TextView id_tv_top_captrue_top_1, int bottonHeight) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(70));
        layoutParams.topMargin = topHeight;
        id_tv_top_captrue_top_1.setLayoutParams(layoutParams);
    }

    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);
            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.weixin);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            mHandler.sendEmptyMessage(MSG_PERMISSION_CAMERA);
            return;
        } catch (RuntimeException e) {
            mHandler.sendEmptyMessage(MSG_PERMISSION_CAMERA);
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this);
        }
    }

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }


    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    /**
     * 识别结果
     *
     * @param result
     */
    public void handleDecode(String result, Bitmap bitmap) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        this.resultCode = result;
        if (result.equals("")) {
            showToast("未识别数据");
            imageScan.setVisibility(View.GONE);
            viewfinderView.drawViewfinder();
            handler = new CaptureActivityHandler(CaptureActivity.this);
            handler.restart();
        } else {
            if (typePage == Contants.VAL_PAGE_SEARCH_DRUG_ADD) {
                imageScan.setImageBitmap(bitmap);
                viewfinderView.closeDrawViewfinder();
                handler.quitSynchronously();
                Rect rect = viewfinderView.getFrame();
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageScan.getLayoutParams();
                layoutParams.width = rect.width();
                layoutParams.height = rect.height();
                layoutParams.leftMargin = rect.left + 10;
                layoutParams.rightMargin = rect.right + 10;
                layoutParams.topMargin = rect.top - 15;
                layoutParams.bottomMargin = rect.bottom;
                imageScan.setLayoutParams(layoutParams);
                try {
                    bitmapScan = Bitmap.createBitmap(bitmap, layoutParams.leftMargin, layoutParams.topMargin, layoutParams.width, layoutParams.height);
                } catch (IllegalArgumentException e) {
                    bitmapScan = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), layoutParams.height);
                }catch (Exception e){

                }
                if (bitmapScan != null) {
                    imageScan.setImageBitmap(bitmapScan);
                }
            }
            bitmap.recycle();
            requestDrug(result);
        }
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
                imageScan.setVisibility(View.GONE);
                viewfinderView.drawViewfinder();
                handler = new CaptureActivityHandler(CaptureActivity.this);
                handler.restart();
            }
        });
        twoButton.showPopupWindow(relativeCapture, Gravity.CENTER);
    }

    public void light() {
        if (this.flag) {
            CameraManager.get().openLight();
            this.flag = false;
            return;
        }
//        CameraManager.get().offLight();
        this.flag = true;
    }

    //关键字：网络请求

    /**
     * 搜索药品
     */
    public void requestDrug(String code) {
        mLoadingProgressBar.show();
        switch (typePage) {
            case Contants.VAL_PAGE_SEARCH_DURUG:
            case Contants.VAL_PAGE_SEARCH_REIMBURSEMENT:
            case Contants.VAL_PAGE_SEARCH_DRUG_ADD:
                requestLocalDrug(code);
                break;
            default:
                mLoadingProgressBar.dismiss();
                break;
        }
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().offLight();
        inactivityTimer.onPause();
        CameraManager.get().closeDriver();
        if (!hasSurface) {
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        handler = null;
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        inactivityTimer.onResume();
        vibrate = true;
        mOpenLight.setCompoundDrawables(null, drawableToptNor, null, null);
        mOpenLight.setText("打开闪光灯");
        isOpen = true;
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                if (!isClose) {
                    isClose = true;
                    closeActivity();
                }
                break;
            case R.id.title_right:
              requestEventCode("j001");
                Bundle bundle = new Bundle();
                bundle.putInt(Contants.KEY_PAGE_SEARCH, typePage);
                if (typePage == Contants.VAL_PAGE_SEARCH_DRUG_ADD) {
                    bundle.putSerializable(Contants.KEY_MEDICINE_BEAN, bean);
                }
                openActivity(ScanHelpActivity.class, bundle);
                break;
            case R.id.capture_tv_selected_image://选择相册
                try {
                    requestEventCode("j003");
                    switchSelectedImage();
                } catch (Exception e) {
                    showToast("图片识别失败");
                    LogUtil.e(TAG, "error:switchSelectedImage.\n" + e.getMessage());
                }
                break;
            case R.id.capture_tv_history:
                requestEventCode("j004");
                bundle = new Bundle();
                bundle.putInt(Contants.KEY_PAGE_SEARCH, typePage);
                openActivity(CaptureHistoryActivity.class, bundle);
                break;
            case R.id.tv_open_light:
                requestEventCode("j002");
//                if (isNoPERMISSION_CAMERA) {
//                    mHandler.sendEmptyMessage(MSG_PERMISSION_CAMERA);
//                } else {
                    try {
                        if (isOpen) {
                            mOpenLight.setCompoundDrawables(null, drawableTopPre, null, null);
                            mOpenLight.setText("关闭闪光灯");
                            CameraManager.get().openLight();
                            isOpen = false;
                        } else {  // 关灯
                            mOpenLight.setCompoundDrawables(null, drawableToptNor, null, null);
                            mOpenLight.setText("打开闪光灯");
                            CameraManager.get().offLight();
                            isOpen = true;
                        }
                    } catch (Exception e) {
                        mHandler.sendEmptyMessage(MSG_PERMISSION_CAMERA);
                        e.printStackTrace();
                    }
//                }
                break;
            case R.id.capture_tv_drugbag://小药箱
                bundle = new Bundle();
                bundle.putSerializable(Contants.KEY_MEDICINE_BEAN, bean);
                openActivity(MedicinePersonActivity.class, bundle);
                CaptureActivity.this.finish();
                sendBroadcast(new Intent().setAction(Contants.ACTION_NAME_DRUG_ADD_FINISH));
                sendBroadcast(new Intent().setAction(Contants.ACTION_NAME_MEDICINEPERSION_FINISH));
                break;
        }

    }

    /**
     * 扫码
     *
     * @param code
     */
    private void requestLocalDrug(String code) {
        String currentProvince = "";
        switch (typePage) {
            case Contants.VAL_PAGE_SEARCH_DRUG_ADD:
            case Contants.VAL_PAGE_SEARCH_DURUG:
                currentProvince = (String) SharePreferenceUtil.get(CaptureActivity.this, SharePreferenceUtil.PROVICE_TOP_CHOOSE, "北京");
                break;
            case Contants.VAL_PAGE_SEARCH_REIMBURSEMENT:
                currentProvince = (String) SharePreferenceUtil.get(CaptureActivity.this, SharePreferenceUtil.PROVICE_JIUZHEN, "北京");
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
        params.put(HttpConstant.PARAM_KEY_PAGEINDEX2,   "0");
        LogUtil.y("#############################params#######"+params.toString());
        request(params, HttpConstant.SEARCH_DRUG_LIST);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        mLoadingProgressBar.dismiss();
        HistoryCache historyCache = new HistoryCache();
        switch (what) {
            case HttpConstant.SEARCH_DRUG_LIST:
                LogUtil.y("#####response######"+response.toString());
                try {
                    JSONObject data = response.getJSONObject(HttpConstant.DATA);
                    int count = data.getInt(HttpConstant.COUNT);
                    Type typeD = new TypeToken<ArrayList<DrugBean>>() {
                    }.getType();
                    List<DrugBean> sList = JsonUtil.getListModle(data.toString(),HttpConstant.INFO, typeD);
                    DrugBean drugBean = sList.get(0);
                    if (typePage == Contants.VAL_PAGE_SEARCH_DRUG_ADD) {
                        if (null != drugBean) {
                            if (drugBean.getMedicineChest().equals("1")) {
                                showPrompt(viewfinderView, "该药品已添加到药箱!");
                            } else {
                                if(drugBean.getType()==1){
                                    showPrompt(viewfinderView, "很抱歉，找不到本条形码的药品");
                                }else{
                                    imageScan.setVisibility(View.VISIBLE);
                                    popWindowDrugBagAdd(drugBean.getDrugID());
                                }
                            }
                        } else {
                            showPrompt(viewfinderView, "很抱歉，找不到本条形码的药品");
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
                addDugBag(imageScan);
                break;
            default:
                break;
        }
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
                    break;
            }
        } else if (type == 1) {
            switchDrugNoDetailActivity(drugBean);
        } else {
            LogUtil.e(TAG, "error,default switchResultActivity");
        }
    }

    /**
     * 首页切换到搜索结果页
     *
     * @param drugBean
     */
    private void switchResultsActivity(DrugBean drugBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Contants.KEY_OBJ_DRUGBEAN, drugBean);
        bundle.putInt(Contants.KEY_PAGE_RESULTS, Contants.VAL_PAGE_SAOMA);
        openActivity(ResultsActivity.class, bundle);


    }

    /**
     * 报销查询结果界面
     *
     * @param drugBean
     */
    private void switchReimbursementDetailsActivity(DrugBean drugBean) {
        Intent intent = new Intent(CaptureActivity.this, ReimbursementDetailsActivity.class);
        intent.putExtra(Contants.KEY_OBJ_DRUGBEAN, drugBean);
        intent.putExtra(Contants.KEY_PAGE_SEARCH, typePage);
        intent.putExtra(Contants.KEY_PAGE, Contants.VAL_PAGE_SAOMA);
        startActivity(intent);
    }

    private void switchDrugNoDetailActivity(DrugBean drugBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Contants.KEY_OBJ_DRUGBEAN, drugBean);
        openActivity(DrugNoDetailActivity.class, bundle);
    }

    private void switchSearchActivity() {
        requestEventCode("a003");
        Bundle mBundle = new Bundle();
        mBundle.putString(Contants.KEY_STR_KEYWORD_CODE, resultCode);
        mBundle.putInt(Contants.KEY_PAGE_SEARCH, Contants.VAL_PAGE_SEARCH_SCAN);
        openActivity(SearchActivity.class, mBundle);
    }


    @Override
    public void error(String errorMsg) {
        mLoadingProgressBar.dismiss();
        if (typePage == Contants.VAL_PAGE_SEARCH_DRUG_ADD) {
            if (what == HttpConstant.MINE_DURG_BAG_ADD) {
                ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);
                if (!TextUtils.isEmpty(errorResponse.getMsg())) {
                    showPrompt(viewfinderView, errorResponse.getMsg());
                } else {
                    showPrompt(viewfinderView, "很抱歉，找不到本条形码的药品");
                }
            } else {
                showToast("未识别数据");
                imageScan.setVisibility(View.GONE);
                viewfinderView.drawViewfinder();
                handler = new CaptureActivityHandler(CaptureActivity.this);
                handler.restart();
            }
        } else {
            switchScancodeErrorActivity();
        }
    }

    private void switchScancodeErrorActivity() {
        if (typePage == Contants.VAL_PAGE_SEARCH_DURUG) {
            requestEventCode("a010");//本地扫码失败
        } else {
            requestEventCode("b006");
        }
        Bundle bundle = new Bundle();
        bundle.putString("resultcode", resultCode);
        bundle.putInt(Contants.KEY_PAGE_SEARCH, typePage);
        openActivity(ScancodeErrorActivity.class, bundle);
    }

    /**
     * 跳转到系统相册选图片
     */
    private void switchSelectedImage() throws Exception {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        CaptureActivity.this.startActivityForResult(intent, REQUESTCODE_IMAGE);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUESTCODE_IMAGE:
                    String filePath = "$";
                    int sdkInt = Build.VERSION.SDK_INT; //兼容4.4系统的问题处理
                    Uri contentUri = data.getData();
                    if (sdkInt >= 19 && DocumentsContract.isDocumentUri(CaptureActivity.this, contentUri)) {
                        String wholeID = DocumentsContract
                                .getDocumentId(contentUri);
                        String id = wholeID.split(":")[1];
                        String[] column = {MediaStore.Images.Media.DATA};
                        String sel = MediaStore.Images.Media._ID + "=?";
                        Cursor cursor = CaptureActivity.this.getContentResolver().query(
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
//                    Bitmap fileBitmap = BitmapUtil.compress(filePath);
                    try {
//                        int width = fileBitmap.getWidth();
//                        int height = fileBitmap.getHeight();
//                        int[] pixels = new int[width * height];
//                        fileBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
//                        Image image = new Image(width, height, "RGB4");
//                        image.setData(pixels);
//                        SymbolSet Syms = mScanner.getResults();
//                        for (Symbol mSym : Syms) {
//                            // mSym.getType()方法可以获取扫描的类型，ZBar支持多种扫描类型,这里实现了条形码、二维码、ISBN码的识别
//                            int type = mSym.getType();
//                            if (type == Symbol.CODE128
//                                    || type == Symbol.QRCODE
//                                    || type == Symbol.CODABAR) {
//                                handleDecode(mSym.getData().toString(), fileBitmap);//根据code,执行网络请求
//                            }
//                        }
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
    @Override
    public void onBackPressed() {
        if (!isClose) {
            isClose = true;
            closeActivity();
        }
    }

    PathMeasure mPathMeasure;
    private float[] mCurrentPosition = new float[2];


    /**
     * 把药品添加到小药箱的抛物线动画
     *
     * @param itemDrugImageView
     */
    private void addDugBag(final ImageView itemDrugImageView) {
        final ImageView imageDrug = new ImageView(this);
        Drawable drawable = itemDrugImageView.getDrawable();
        imageDrug.setImageDrawable(drawable);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        relativeCapture.addView(imageDrug, params);
        int[] parentLocation = new int[2];
        relativeCapture.getLocationInWindow(parentLocation);
        int startLoc[] = new int[2];
        itemDrugImageView.getLocationInWindow(startLoc);
        int endLoc[] = new int[2];
        captureTvDrugbag.getLocationInWindow(endLoc);
        float startX = startLoc[0] - parentLocation[0];
        float startY = startLoc[1] - parentLocation[1] + itemDrugImageView.getHeight() / 2;
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
                imageScan.setVisibility(View.GONE);
                viewfinderView.drawViewfinder();
                handler = new CaptureActivityHandler(CaptureActivity.this);
                handler.restart();
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
        what = HttpConstant.MINE_DURG_BAG_ADD;
        request(params, HttpConstant.MINE_DURG_BAG_ADD);
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
        twoButton.getTv_content().setText(msg);
        twoButton.getTvOk().setVisibility(View.GONE);
        twoButton.getLineTwo().setVisibility(View.GONE);
        twoButton.getTvCancel().setText(Contants.STR_GOT_IT);
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageScan.setVisibility(View.GONE);
                viewfinderView.drawViewfinder();
                handler = new CaptureActivityHandler(CaptureActivity.this);
                handler.restart();
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(v, Gravity.CENTER);
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
        handleDecode(result.getText(),null);//根据code,执行网络请求
    }

    @AfterPermissionGranted(REQUEST_CODE_CAMERA)
    public void  requestCODE_CAMERA(){//相机
        String perms[] ={Manifest.permission.CAMERA,Manifest.permission.VIBRATE,Manifest.permission.FLASHLIGHT} ;
        if (!EasyPermissions.hasPermissions(mContext, perms)) {
            EasyPermissions.requestPermissions(this,"医保通申请相机权限",REQUEST_CODE_CAMERA, perms);
        }
    }
}