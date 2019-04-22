package com.lzyc.ybtappcal.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.Intents;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.lzyc.ybtappcal.view.pagestate.PageManager;
import com.lzyc.ybtappcal.volley.BaseService;
import com.lzyc.ybtappcal.volley.ServiceResponseCallback;
import com.lzyc.ybtappcal.widget.popupwindow.PopupWindowTwoButton;
import com.umeng.message.PushAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by yanlc on 2017/4/24.
 */
public abstract class BaseFragment extends Fragment implements ServiceResponseCallback,EasyPermissions.PermissionCallbacks {

    @BindView(R.id.ib_left)
    ImageButton mTitleLeftBtn;
    @BindView(R.id.ib_left_1)
    ImageButton mTitleRegisterLeftBtn;
    @BindView(R.id.tv_title)
    TextView mTitleTv;
    @BindView(R.id.tv_right)
    TextView mTitleRightTvBtn;
    @BindView(R.id.ib_right)
    ImageButton mTitleRightBtn;
    @BindView(R.id.rl_top)
    RelativeLayout mTitleLayout;
    @BindString(R.string.empty_error_net_server)
    public String errorServer;

    private PageManager mPageManager;
    private View mContentView;

    private BaseService httpRequest;
    private Unbinder mUnBinder;

    private ViewGroup mRootView;
    protected Context mContext;

    protected PopupWindowTwoButton popLoginButton;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_base_layout, container, false);
        mRootView = rootView;
        mContentView = LayoutInflater.from(getActivity()).inflate(getContentViewId(), (ViewGroup) rootView.findViewById(R.id.root_layout), false);
        rootView.addView(mContentView);
        //页面状态管理库 文档查看 https://github.com/hss01248/PageStateManager
        //这里不再初始化pageManager ，在需要添加pagerManager的页面手动调用 setPageStateView或者setCustomPageStateView
        //这样可以减少不需要loading或者error的页面添加，减少页面嵌套层级
//        PageManager.initInApp(getActivity().getApplicationContext());
//        mPageManager = PageManager.init(mContentView, false, new Runnable() {
//            @Override
//            public void run() {
//                onClickRetry();
//            }
//        });
        //初始化黄油刀 文档查看 https://github.com/JakeWharton/butterknife
        mUnBinder = ButterKnife.bind(this,rootView);
        mContext=getActivity();
        if (httpRequest == null) {
            httpRequest = new BaseService();
        }
        setTitleBarVisibility(View.GONE);
        init();
        return rootView;
    }

    /**
     * 返回布局layout的资源
     *
     * @return 布局layout
     */
    public abstract int getContentViewId();

    /**
     * 相当于onCreate 做一些初始化操作
     */
    protected abstract void init();

    @Override
    public void onPause() {
        if(httpRequest!=null){
            httpRequest.cancel();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if(httpRequest!=null){
            httpRequest.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        httpRequest.cancel();//解决volley 内存溢出的问题
        mUnBinder.unbind();
    }

    //------------------------title_bar 操作-----------------

    /**
     * 设置标题栏名称
     *
     * @param title
     */
    public void setTitleName(String title) {
        mTitleTv.setText(title);
    }

    public void setTitleName(String title, int resCoclo) {
        mTitleTv.setText(title);
        mTitleTv.setTextColor(resCoclo);
    }

    /**
     * 隐藏或者显示标题栏
     * @param visibility
     */
    public void setTitleBarVisibility(int visibility) {
        mTitleLayout.setVisibility(visibility);
    }

    /**
     * 设置标题原色
     * @param resColor
     */
    public void setTitleBackrounColor(int resColor) {
        mTitleLayout.setBackgroundColor(getResources().getColor(resColor));
    }

    /**
     * 隐藏或者显示左边按钮
     * @param visibility
     */
    public void setTitleLeftBtnVisibility(int visibility) {
        mTitleLeftBtn.setVisibility(visibility);
    }

    /**
     * 隐藏或者显示左边按钮(注册登入)
     * @param visibility
     */
    public void setTitleRegisterLeftBtnVisibility(int visibility) {
        mTitleRegisterLeftBtn.setVisibility(visibility);
    }

    /**
     * 隐藏或者显示右边imageBtn按钮
     * @param visibility
     */
    public void setTitleRightBtnVisibility(int visibility) {
        mTitleRightBtn.setVisibility(visibility);

    }

    /**
     * 设置右边imageBtn按钮图片
     * @param imgId
     */
    public void setTitleRightBtnResources(int imgId) {
        mTitleRightBtn.setImageResource(imgId);
        mTitleRightBtn.setVisibility(View.VISIBLE);
    }

    /**
     * 设置右边文字按钮背景图片
     * @param imgId
     */
    public void setTitleRightTvbtnBackgroundResources(int imgId) {
        mTitleRightTvBtn.setBackgroundResource(imgId);
        mTitleRightTvBtn.setVisibility(View.VISIBLE);
    }

    /**
     * 设置右边文字按钮名字
     * @param name
     */
    public void setTitleRightTvbtnName(String name) {
        mTitleRightTvBtn.setText(name);
        mTitleRightTvBtn.setVisibility(View.VISIBLE);
    }

    /**
     * 设置右边名字颜色
     * @param name
     * @param color
     */
    public void setTitleRightTvbtnName(String name, String color) {
        mTitleRightTvBtn.setText(name);
        mTitleRightTvBtn.setTextColor(Color.parseColor(color));
        mTitleRightTvBtn.setVisibility(View.VISIBLE);
    }


    /**
     * title 左侧按钮点击事件
     * @param v
     */
    @OnClick(R.id.ib_left)
    public void onClickTitleLeftBtn(View v) {

    }
    /**
     * title 左侧注册按钮点击事件
     * @param v
     */
    @OnClick(R.id.ib_left_1)
    public void onClickTitleRegisterLeftBtn(View v) {

    }
    /**
     * title 右侧tv按钮点击事件
     * @param v
     */
    @OnClick(R.id.tv_right)
    public void onClickTitleRightTvBtn(View v) {

    }
    /**
     * title 右侧按钮点击事件
     * @param v
     */
    @OnClick(R.id.ib_right)
    public void onClickTitleRightBtn(View v) {

    }

    //------------------------ 页面状态修改 -----------------

    public void showLoading() {
        if (mPageManager != null)
            mPageManager.showLoading();
    }

    public void showContent() {
        if (mPageManager != null)
            mPageManager.showContent();
    }

    public void showEmpty() {
        if (mPageManager != null)
            mPageManager.showEmpty();
    }

    public void showEmpty(String msg) {
        if (mPageManager != null)
            mPageManager.showEmpty(msg);
    }

    public void showEmpty(String msg, int imgId) {
        if (mPageManager != null)
            mPageManager.showEmpty(msg, imgId);
    }

    public void setEmptyBgResourse(int color) {
        if (mPageManager != null)
            mPageManager.setEmptyBgResourse(color);
    }

    public void showError() {
        if (mPageManager != null)
            mPageManager.showError();
    }

    public void showError(CharSequence errorMsg) {
        if (mPageManager != null)
            mPageManager.showError(errorMsg);
    }

    public void showError(CharSequence errorMsg, int imgid) {
        if (mPageManager != null)
            mPageManager.showError(errorMsg, imgid);
    }

    /**
     * 登录弹窗
     *
     * true  登录  false 未登录
     */
    protected boolean popupLogin(){
        if(TextUtils.isEmpty(SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "").toString())){

            if(null == popLoginButton){
                popLoginButton = new PopupWindowTwoButton(getActivity());
            }

            popLoginButton.getTv_content().setText("未登录，请先登录");
            popLoginButton.getTvOk().setText("确认");
            popLoginButton.getTvCancel().setText("取消");
            popLoginButton.getTvOk().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(Intents.openLoginActivity(mContext));
                    popLoginButton.dismiss();
                }
            });
            popLoginButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popLoginButton.dismiss();
                }
            });
            popLoginButton.showPopupWindow(new View(mContext), Gravity.CENTER);

            return false;
        }
        return true;
    }

    /**
     * 设置error 页面trybtn的背景
     *
     * @param resid
     */
    public void setErrorTryBtnBg(int resid) {
        if (mPageManager != null)
            mPageManager.setErrorTryBtnBg(resid);
    }

    /**
     * 服务器返回错误页面
     */
    public void showErrorServer() {
        mPageManager.showEmpty(getResources().getString(R.string.empty_error_net_server), R.mipmap.empty_error_net_server);
    }


    /**
     * 使用默认页面管理
     */
    public void setPageStateView() {
        PageManager.initInApp(getActivity().getApplicationContext());
        mPageManager = PageManager.init(mContentView, false, new Runnable() {
            @Override
            public void run() {
                onClickRetry();
            }
        });
        if (mPageManager.BASE_LOADING_LAYOUT_ID == R.layout.page_loading) {
            ImageView loadingView = (ImageView) mRootView.findViewById(R.id.page_loading_iv);
            AnimationDrawable anim = (AnimationDrawable) loadingView.getBackground();
            anim.start();
        }
    }


    /**
     * 在指定view上加载loadding
     */
    public void setPageStateView(View view) {
        PageManager.initInApp(getActivity().getApplicationContext());
        mPageManager = PageManager.init(view, false, new Runnable() {
            @Override
            public void run() {
                onClickRetry();
            }
        });
        if (mPageManager.BASE_LOADING_LAYOUT_ID == R.layout.page_loading) {
            ImageView loadingView = (ImageView) mRootView.findViewById(R.id.page_loading_iv);
            AnimationDrawable anim = (AnimationDrawable) loadingView.getBackground();
            anim.start();
        }
    }


    /**
     * 自定义各种状态页面
     *
     * @param layoutIdOfEmpty
     * @param layoutIdOfLoading
     * @param layoutIdOfError
     */
    public void setCustomPageStateView(int layoutIdOfEmpty, int layoutIdOfLoading, int layoutIdOfError) {
        PageManager.initInApp(getActivity().getApplicationContext(), layoutIdOfEmpty, layoutIdOfLoading, layoutIdOfError);
        mPageManager = PageManager.init(mContentView, false, new Runnable() {
            @Override
            public void run() {
                onClickRetry();
            }
        });
        if (layoutIdOfLoading == R.layout.page_loading) {
            ImageView loadingView = (ImageView) mRootView.findViewById(R.id.page_loading_iv);
            AnimationDrawable anim = (AnimationDrawable) loadingView.getBackground();
            anim.start();
        }
    }

    /**
     * 点击了错误重试
     */
    protected void onClickRetry() {

    }

    //-----------------------------基础方法---------------------------------------------

    /**
     * 打开activity
     *
     * @param ActivityClass
     */
    public void openActivity(Class<? extends Activity> ActivityClass) {
        Intent intent = new Intent(getActivity(), ActivityClass);
        startActivity(intent);
    }


    /**
     * 打开activity
     *
     * @param ActivityClass
     * @param b
     */
    public void openActivityNoAnim(Class<? extends Activity> ActivityClass, Bundle b) {
        Intent intent = new Intent(mContext, ActivityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtras(b);
        startActivity(intent);
    }

    /**
     * 打开activity
     *
     * @param ActivityClass
     * @param b
     */
    public void openActivity(Class<? extends Activity> ActivityClass, Bundle b) {
        Intent intent = new Intent(getActivity(), ActivityClass);
        intent.putExtras(b);
        startActivity(intent);
    }

    /**
     * 显示toast弹框
     * @param text
     */
    public void showToast(String text) {
        ToastUtil.showToastCenter(getActivity(), text);
    }

    //------------------------------- 网络方法---------------------------------------

    /**
     * 提交统计事件
     * @param eventCode
     */
    public void requestEventCode(String eventCode) {
        String phone= (String) SharePreferenceUtil.get(mContext,SharePreferenceUtil.PHONE,"");
        String uuid = PushAgent.getInstance(mContext).getRegistrationId();
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP,HttpConstant.APP_STATS);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.STATS_EVENT_CLZ);
        params.put(HttpConstant.PARAM_KEY_EVENT_CODE, eventCode);
        params.put(HttpConstant.PARAM_KEY_PHONE, phone);
        if (TextUtils.isEmpty(uuid)) {
            uuid = PushAgent.getInstance(mContext).getRegistrationId();
        }
        params.put(HttpConstant.PARAM_KEY_UUID, uuid);
        params.put(HttpConstant.PARAM_KEY_OS, HttpConstant.OS_ANDROID);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.STATS_EVENT_SIGN);
        request(params);
    }


    public void request(Map<String, String> params) {
        try {
            httpRequest.postMap(HttpConstant.APP_URL, this, params);
        } catch (Exception e) {
            LogUtil.e("error", "error:在网络请求时与服务器连接出错" + e.getStackTrace());
        }
    }

    /**
     * 网络请求
     */
    public void request(Map<String, String> params, int what) {
        try {
            httpRequest.postMap(HttpConstant.APP_URL, this, params, what);
            LogUtil.d("lxx", "requestData-->"+params.toString());
        } catch (Exception e) {
            LogUtil.e("error", "error:在网络请求时与服务器连接出错" + e.getStackTrace());
        }
    }
    public void request(Map<String, String> params, int what, boolean isLoading,boolean isShowNet) {
        if(isShowNet&&!NetworkUtil.CheckConnection(mContext)){
            showError();
            return;
        }
        request(params,what,isLoading);
    }

    public void request(Map<String, String> params, int what, boolean isLoading) {
        if (isLoading) {
            showLoading();
        }
        httpRequest.postMap(HttpConstant.APP_URL, this, params, what);
    }

    /**
     * 网络不可用弹窗
     */
    public void showNetDialog() {
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(getActivity());
        twoButton.getTvTitle().setVisibility(View.INVISIBLE);
        twoButton.getTv_content().setText("当前网络连接不可用，是否设置网络？");
        twoButton.getTvOk().setText("去设置");
        twoButton.getTvCancel().setText("取消");
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
            }
        });
        twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    NetworkUtil.openNetWorkSetting(mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(new View(mContext), Gravity.CENTER);
    }

    /**
     * 网络请求成功
     *
     * @param msg      成功信息
     * @param what     what值
     * @param response 请求返回数据
     */
    @Override
    public void done(String msg, int what, JSONObject response) {
    }

    /**
     * 网络请求出错
     *
     * @param errorMsg 错误信息
     */
    @Override
    public void error(String errorMsg) {
        LogUtil.e("fragment","error:"+errorMsg);
    }

    /**
     * 弹窗，知道了
     *
     * @param v
     * @param msg
     */
    public void showPrompt(View v, String msg) {
         final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(getActivity());
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
            }
        });
        twoButton.showPopupWindow(v, Gravity.CENTER);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        LogUtil.y("####权限授予成功##onPermissionsGranted############"+requestCode);
        for (String perm:perms) {
            LogUtil.y("############perm  "+perm);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        LogUtil.y("####权限授予失败####onPermissionsDenied##########"+requestCode);
        for (String perm:perms) {
            LogUtil.y("############perm  "+perm);
        }
    }
}
