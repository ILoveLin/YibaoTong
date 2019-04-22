package com.lzyc.ybtappcal.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.InsuranceActivity;
import com.lzyc.ybtappcal.activity.MainActivity;
import com.lzyc.ybtappcal.activity.ShoppingCartActivity;
import com.lzyc.ybtappcal.activity.account.LoginActivity;
import com.lzyc.ybtappcal.activity.base.BaseFragment;
import com.lzyc.ybtappcal.activity.mine.BrowseHistoryActivity;
import com.lzyc.ybtappcal.activity.mine.FeedbackHelpActivity;
import com.lzyc.ybtappcal.activity.mine.HealthActivity;
import com.lzyc.ybtappcal.activity.mine.MessageListActivity;
import com.lzyc.ybtappcal.activity.mine.MinePlanActivity;
import com.lzyc.ybtappcal.activity.mine.PersonalInfoActivity;
import com.lzyc.ybtappcal.activity.mine.SettingActivity;
import com.lzyc.ybtappcal.activity.mine.medicine.FamilyMedicineActivity;
import com.lzyc.ybtappcal.activity.mine.withdraw.MineBalanceListActivity;
import com.lzyc.ybtappcal.activity.payment.order.AddressManagerActivity;
import com.lzyc.ybtappcal.activity.payment.order.OrderMineActivity;
import com.lzyc.ybtappcal.adapter.BiyaoStatePagerAdapter;
import com.lzyc.ybtappcal.app.ActivityCollector;
import com.lzyc.ybtappcal.bean.MyInfoBean;
import com.lzyc.ybtappcal.bean.RefundBrief;
import com.lzyc.ybtappcal.bean.TopBanner;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.Intents;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.ScreenUtils;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.lzyc.ybtappcal.util.cache.SPCache;
import com.lzyc.ybtappcal.util.cache.SPCacheList;
import com.lzyc.ybtappcal.view.BadgeView;
import com.lzyc.ybtappcal.view.roundimageview.RoundedImageView;
import com.lzyc.ybtappcal.view.viewpager.DecoratorViewPager;
import com.lzyc.ybtappcal.widget.popupwindow.BasePopupWindow;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import ybt.library.indicator.indicator.PageIndicatorView;

/**
 * 个人中心
 *
 * @author yang
 */
public class MineFragment extends BaseFragment implements UMShareListener {
    protected static final String TAG = MineFragment.class.getSimpleName();
    public static final String MINE_POINT_HOSPITAL = "no";
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private static final int ANIMATIONS_DURATION = 400;
    private String phone;
    private MyInfoBean infoBean;
    private Boolean isLogin;
    private String messageState;
    private boolean mIsTheTitleContainerVisible = false;
    private RefundBrief refundBrief;
    private List<TopBanner.DataBean.ImagesBean> listImageUrl;
    private BiyaoStatePagerAdapter advertisementAdapter;
    @BindView(R.id.frag_mine_appbar_layout)
    AppBarLayout frag_mine_appbar_layout;
    @BindView(R.id.frag_mine_bg_image)
    ImageView frag_mine_bg_image;
    @BindView(R.id.fragment_mine_toolbar)
    Toolbar fragment_mine_toolbar;
    @BindView(R.id.frag_mine_titlelay)
    LinearLayout frag_mine_titlelay;
    @BindView(R.id.iv_fg_my_avator)
    RoundedImageView iv_fg_my_avator;
    @BindView(R.id.frag_mine_withdrawals)
    FrameLayout frag_mine_withdrawals;
    @BindView(R.id.tv_fg_my_plan)
    TextView tv_fg_my_plan;
    @BindView(R.id.tv_fg_my_medicine)
    TextView tv_fg_my_medicine;
    @BindView(R.id.tv_fg_my_nickname)
    TextView tv_fg_my_nickname;
    @BindView(R.id.tv_fg_my_share)
    TextView tv_fg_my_share;
    @BindView(R.id.tv_fg_my_health)
    TextView tv_fg_my_health;
    @BindView(R.id.tv_fg_my_setting)
    TextView tv_fg_my_setting;
    @BindView(R.id.tv_fg_my_order)
    TextView tv_fg_my_order;
    @BindView(R.id.frag_mine_avator_linear)
    LinearLayout frag_mine_avator_linear;
    @BindView(R.id.frag_mine_message_notify)
    ImageView frag_mine_message_notify;//通知
    @BindView(R.id.frag_mine_badge_message)
    ImageView frag_mine_badge_message;
    @BindView(R.id.frag_mine_topview)
    RelativeLayout frag_mine_topview;
    @BindView(R.id.frag_mine_balance)
    TextView frag_mine_balance;
    @BindView(R.id.frag_mine_refund_total)
    TextView frag_mine_refund_total;
    @BindView(R.id.frag_mine_refund_pending)
    TextView frag_mine_refund_pending;

    @BindView(R.id.frag_mine_refund_total_linear)
    LinearLayout frag_mine_refund_total_linear;
    @BindView(R.id.frag_mine_refund_pending_linear)
    LinearLayout frag_mine_refund_pending_linear;
    @BindView(R.id.frag_mine_address_manager)
    TextView frag_mine_address_manager;
    @BindView(R.id.frag_mine_shopping_cart)
    TextView frag_mine_shopping_cart;
    @BindView(R.id.mine_browse_history)
    TextView mine_browse_history;
    @BindView(R.id.frag_mine_insurance)
    TextView frag_mine_insurance;
    @BindView(R.id.tv_fg_my_help)
    TextView tv_fg_my_help;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.line_plan)
    View line_plan;
    @BindView(R.id.avator_forword_right)
    ImageView avator_forword_right;
    @BindView(R.id.nested_scrollview)
    NestedScrollView nested_scrollview;
    @BindView(R.id.badgeview_shop_num)
    BadgeView badgeview_shop_num;
    @BindView(R.id.frag_mine_linear_name)
    LinearLayout frag_mine_linear_name;
    @BindView(R.id.frag_mine_insured)
    ImageView frag_mine_insured;
    @BindView(R.id.viewpager)
    DecoratorViewPager viewpager;
    @BindView(R.id.vp_bottom_linear)
    LinearLayout vp_bottom_linear;
    @BindView(R.id.vp_indicator)
    PageIndicatorView vp_indicator;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void init() {
        tv_fg_my_nickname.setVisibility(View.VISIBLE);
        fragment_mine_toolbar.setTitle("");
        frag_mine_appbar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                frag_mine_bg_image.setAlpha(1.0f - percentage);
                frag_mine_titlelay.setAlpha(percentage);
                handleAlphaOnTitle(percentage);
            }
        });
        if (listImageUrl == null) {
            listImageUrl = SPCacheList.getInstance().readLisImageBean();
        }
        if (listImageUrl.size() > 0) {
            advertisementAdapter = new BiyaoStatePagerAdapter(mContext, getActivity().getSupportFragmentManager(), listImageUrl);
            viewpager.setAdapter(advertisementAdapter);
            vp_indicator.setCount(listImageUrl.size());
            vp_indicator.setViewPager(viewpager);
            vp_indicator.setAutoScroll();
            if (listImageUrl.size() == 1) {
                vp_indicator.setVisibility(View.GONE);
            } else {
                vp_indicator.setVisibility(View.VISIBLE);
            }
        }
    }

    // 控制Title的显示
    public void handleAlphaOnTitle(float percentage) {
        if (percentage > 0) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(frag_mine_linear_name, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
//                startAlphaAnimation(v.frag_mine_insured, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(avator_forword_right, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
//                startScaleAnimation(tv_fg_my_nickname, ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(frag_mine_linear_name, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
//                startAlphaAnimation(v.frag_mine_insured, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(avator_forword_right, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
//                startScaleAnimation(tv_fg_my_nickname, ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    // 设置渐变的动画
    private void startAlphaAnimation(View v, long duration, int visibility) {

        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    // 设置缩放的动画
    private void startScaleAnimation(View v, long duration, int visibility) {
        ScaleAnimation scaleAnimation = (visibility == View.VISIBLE) ? new ScaleAnimation(0f, 1f, 0.0f, 1.0f, 0.5f, 0.5f) : new ScaleAnimation(1.0f, 0f, 1.0f, 0.0f, -1.0f, 0.0f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setRepeatMode(Animation.RELATIVE_TO_SELF);
        scaleAnimation.setDuration(duration);
        v.startAnimation(scaleAnimation);
    }


    @butterknife.OnClick({R.id.frag_mine_titlelay, R.id.frag_mine_refund_total_linear, R.id.frag_mine_refund_pending_linear,
            R.id.frag_mine_withdrawals, R.id.tv_fg_my_order, R.id.frag_mine_shopping_cart, R.id.frag_mine_address_manager,
            R.id.tv_fg_my_plan, R.id.tv_fg_my_medicine, R.id.mine_browse_history, R.id.tv_fg_my_nickname, R.id.iv_fg_my_avator,
            R.id.frag_mine_avator_linear, R.id.tv_fg_my_share, R.id.frag_mine_message_notify, R.id.tv_fg_my_health, R.id.tv_fg_my_setting,
            R.id.frag_mine_insurance, R.id.tv_fg_my_help
    })
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.frag_mine_titlelay:
                break;
            case R.id.frag_mine_refund_total_linear://累计返现
                requestEventCode("c011");
                if (isLogin && infoBean != null) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("type", 1);
                    bundle1.putInt("red_packet", 0);
                    Intents.openMineWithdrawReturnActivity(mContext, bundle1);
                } else {
                    openActivity(LoginActivity.class);
                }
                break;
            case R.id.frag_mine_refund_pending_linear://待返现
                requestEventCode("c012");
                if (isLogin && infoBean != null) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("type", 2);
                    Intents.openMineWithdrawReturnActivity(mContext, bundle1);
                } else {
                    openActivity(LoginActivity.class);
                }
                break;
            case R.id.frag_mine_withdrawals://可用余额
                requestEventCode("c018");
                if (isLogin && infoBean != null) {
                    bundle.putString(HttpConstant.PERSON_WITHDRAW_BALANCE, infoBean.getBalance());
                    openActivity(MineBalanceListActivity.class, bundle);
                } else {
                    openActivity(LoginActivity.class);
                }
                break;
            //我的订单
            case R.id.tv_fg_my_order:
                requestEventCode("c017");
                if (isLogin) {
                    openActivity(OrderMineActivity.class);
                } else {
                    openActivity(LoginActivity.class);
                }
                break;
            case R.id.frag_mine_shopping_cart://购物车
                requestEventCode("c016");
                if (isLogin) {
                    openActivity(ShoppingCartActivity.class);
                } else {
                    openActivity(LoginActivity.class);
                }
                break;
            case R.id.frag_mine_address_manager://地址管理
                requestEventCode("c009");
                if (isLogin) {
                    openActivity(AddressManagerActivity.class);
                } else {
                    openActivity(LoginActivity.class);
                }
                break;
            case R.id.tv_fg_my_plan://我的方案
                requestEventCode("c004");
//                EventBus.getDefault().post(new Event("c004"));
                if (isLogin) {
                    openActivity(MinePlanActivity.class);
                } else {
                    openActivity(LoginActivity.class);
                }
                break;
            case R.id.tv_fg_my_medicine:  //家庭药箱
                requestEventCode("a-4010");
//                requestEventCode("c019");
//                EventBus.getDefault().post(new Event("a-4010"));
                if (isLogin) {
                    if (!NetworkUtil.CheckConnection(mContext)) {
                        showNetDialog();
                        return;
                    }
                    openActivity(FamilyMedicineActivity.class);
                } else {
                    openActivity(LoginActivity.class);
                }
                break;
            case R.id.mine_browse_history://浏览记录
                requestEventCode("c005");
                if (isLogin) {
                    openActivity(BrowseHistoryActivity.class);
                } else {
                    openActivity(LoginActivity.class);
                }
                break;
            //头像
            case R.id.tv_fg_my_nickname:
            case R.id.iv_fg_my_avator:
            case R.id.frag_mine_avator_linear:
                requestEventCode("c001");
                if (isLogin && infoBean != null) {
                    Intent avatorIntent = new Intent(getActivity(), PersonalInfoActivity.class);
                    avatorIntent.putExtra(Contants.KEY_OBJ_MINE_INFO, infoBean);
                    startActivity(avatorIntent);
                } else {
                    openActivity(LoginActivity.class);
                }
                break;
            //我的分享
            case R.id.tv_fg_my_share:
                requestEventCode("c003");
                if (isLogin) {
                    if (infoBean != null) {
                        initPopup();
                    }
                } else {
                    openActivity(LoginActivity.class);
                }
                break;
            //消息通知
//            case R.id.frag_mine_topview_message_notify:
            case R.id.frag_mine_message_notify:
                requestEventCode("c006");
//                EventBus.getDefault().post(new Event("c006"));
                if (isLogin) {
                    openActivity(MessageListActivity.class);
                } else {
                    openActivity(LoginActivity.class);
                }
                break;
            //医保计算器
            case R.id.tv_fg_my_health:
                requestEventCode("c007");
                String ybType = "ZZ00";
                Intent healthIntent = new Intent(mContext, HealthActivity.class);
                if (infoBean != null) {
                    ybType = infoBean.getYbtype();
                }
                healthIntent.putExtra("ybtype", ybType);
                startActivity(healthIntent);
                break;
            //设置
            case R.id.tv_fg_my_setting:
                requestEventCode("c008");
                Intent intent = new Intent(mContext, SettingActivity.class);
                intent.putExtra(Contants.KEY_STR_MESSAGE_STATE, messageState);
                startActivity(intent);
                break;
            case R.id.frag_mine_insurance://保险
                ToastUtil.showToastCenter(mContext, "我的保险服务");
                openActivity(InsuranceActivity.class);
                break;
            case R.id.tv_fg_my_help://帮助与反馈
                openActivity(FeedbackHelpActivity.class);
                break;
            default:
                break;
        }
    }

//
//    /**
//     * 邀请
//     */
//    private void initPopupShare() {
//        final String url = infoBean.getInviteUrl();
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
//        final UMImage image = new UMImage(getActivity(), bitmap);
//        final String title = "您的好友推荐您注册医保通APP";
//        final String text = "医保通";
//        final BasePopupWindow popup = new BasePopupWindow(getActivity(), R.layout.popup_share, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        View conentView = popup.getConentView();
//        conentView.findViewById(R.id.tv_popup_share_cancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popup.dismiss();
//            }
//        });
//        conentView.findViewById(R.id.tv_popup_share_wb).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UMWeb web = new UMWeb(url);
//                web.setTitle(title);//标题
//                web.setThumb(image);  //缩略图
//                new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
//                        .withText(text)
//                        .withMedia(web)
//                        .share();
//                popup.dismiss();
//            }
//        });
//        conentView.findViewById(R.id.tv_popup_share_weixin).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
//                        .withTitle(title)
//                        .withText(text)
//                        .withMedia(image)
//                        .withTargetUrl(url)
//                        .share();
//                popup.dismiss();
//            }
//        });
//        conentView.findViewById(R.id.tv_popup_share_pyq).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
//                        .withTitle(title)
//                        .withText(text)
//                        .withMedia(image)
//                        .withTargetUrl(url)
//                        .share();
//                popup.dismiss();
//            }
//        });
//        conentView.findViewById(R.id.tv_popup_share_qq).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
//                        .withTitle(title)
//                        .withText(text)
//                        .withMedia(image)
//                        .withTargetUrl(url)
//                        .share();
//                popup.dismiss();
//            }
//        });
//        popup.showPopupWindow(tv_fg_my_share, Gravity.BOTTOM);
//    }

    /**
     * 分享邀请好友
     */
    private void initPopup() {
        final String url = infoBean.getShareUrl();
        int w = ScreenUtils.getScreenWidth() - DensityUtils.dp2px(20);
        final BasePopupWindow popup = new BasePopupWindow(getActivity(), R.layout.popup_invitation, w, ViewGroup.LayoutParams.WRAP_CONTENT);
        View conentView = popup.getConentView();

        RoundedImageView riv = (RoundedImageView) conentView.findViewById(R.id.iv_popup_invitation_avator);
        ImageView qrcode = (ImageView) conentView.findViewById(R.id.iv_popup_invitation_qrcode);
        TextView tv = (TextView) conentView.findViewById(R.id.tv_popup_invitation_nickname);
        String img = infoBean.getImg();
        if (!TextUtils.isEmpty(img)) {
            Picasso.with(getActivity()).load(img).into(riv);
        } else {
            riv.setImageResource(R.mipmap.icon_system_logo);
        }
        String nickname = infoBean.getNickname();
        tv.setText(nickname);
        Picasso.with(getActivity()).load(infoBean.getAndroidImg()).into(qrcode);
        conentView.findViewById(R.id.iv_popup_invitation_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
        conentView.findViewById(R.id.tv_popup_share_wb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp(getActivity(),SHARE_MEDIA.SINA,url);
                popup.dismiss();
            }
        });
        conentView.findViewById(R.id.tv_popup_share_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp(getActivity(),SHARE_MEDIA.WEIXIN,url);
                popup.dismiss();
            }
        });
        conentView.findViewById(R.id.tv_popup_share_pyq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp(getActivity(),SHARE_MEDIA.WEIXIN_CIRCLE,url);
                popup.dismiss();
            }
        });
        conentView.findViewById(R.id.tv_popup_share_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp(getActivity(),SHARE_MEDIA.QQ,url);
                popup.dismiss();
            }
        });
        popup.showPopupWindow(coordinatorLayout, Gravity.CENTER);

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        isLogin = (Boolean) SharePreferenceUtil.get(mContext, SharePreferenceUtil.IS_LOGIN, false);
        messageState = SPCache.getInstance().getIsmessage();
        requestMessageStatus();
        if (!isLogin) {
            iv_fg_my_avator.setImageResource(R.mipmap.image_avator_default);
            tv_fg_my_nickname.setText(Contants.STR_ACCOUNT_INFO_LOGIN);
            line_plan.setVisibility(View.GONE);
            tv_fg_my_plan.setVisibility(View.GONE);
        } else {
            infoBean = SPCache.getInstance().getMyInfoBeanCache();
            phone = (String) SharePreferenceUtil.get(getActivity(), SharePreferenceUtil.PHONE, "");
            if (!TextUtils.isEmpty(phone)) {
                requestAccountInfo();
            }
            String uid = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "");
            refundBrief = SPCache.getInstance().getRefundBriefCache();
            requestRefundBriefInfo(uid);
            pullInfo();
            fullRefundBriefInfo();
        }
        int messageStatus = (Integer) SharePreferenceUtil.get(mContext, SharePreferenceUtil.KEY_MESSAGE_STATUS, 0);
        if (messageStatus == 0) {
            frag_mine_badge_message.setVisibility(View.GONE);
        } else {
            frag_mine_badge_message.setVisibility(View.VISIBLE);
        }
        requestShopCardNum();
    }

    /**
     * 账户返现简要信息
     *
     * @param uid
     */
    private void requestRefundBriefInfo(String uid) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_UCENTER);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.UCENTER_BALANCE_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.UCENTER_BALANCE_SIGN);
        params.put(HttpConstant.APP_UID, uid);
        request(params, HttpConstant.MINE_INFO_REFUND);

    }

    /**
     * 获取个人信息
     */
    public void requestAccountInfo() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_UCENTER);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.UCENTER_INDEX_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.UCENTER_INDEX_SIGN);
        params.put(HttpConstant.PARAM_KEY_PHONE, phone);
        params.put(HttpConstant.PARAM_KEY_TOKEN, PushAgent.getInstance(mContext).getRegistrationId());
        if (!NetworkUtil.CheckConnection(mContext)) {
            return;
        }
        request(params, HttpConstant.MY_INFO);
    }


    /**
     * 获取消息提醒状态
     */
    public void requestMessageStatus() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_UCENTER);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.UCENTER_GETMESSAGE_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.UCENTER_GETMESSAGE_SIGN);
        String device_token = PushAgent.getInstance(mContext).getRegistrationId();
        if (!TextUtils.isEmpty(device_token)) {
            params.put(HttpConstant.PARAM_KEY_TOKEN, device_token);
        }
        if (!NetworkUtil.CheckConnection(mContext)) {
            return;
        }
        request(params, HttpConstant.MY_GET_MESSAGE);
    }

    /**
     * 购物车数量
     */
    public void requestShopCardNum() {
        if (TextUtils.isEmpty(SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "").toString()))
            return;

        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_CARD_NUM_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_CARD_NUM_SIGN);
        params.put(HttpConstant.APP_UID, SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "").toString());
        if (!NetworkUtil.CheckConnection(mContext)) {
            return;
        }
        request(params, HttpConstant.BUY_ORDERBY_SHOP_CARD_NUM);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.MY_INFO:
                try {
                    String dataJson = response.getJSONObject(HttpConstant.DATA).toString();
                    infoBean = JsonUtil.getModle(dataJson, MyInfoBean.class);
                    SPCache.getInstance().putMineInfoCache(dataJson);
                    pullInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                    popupLogout();
                } catch (Exception e) {

                }
                break;
            case HttpConstant.MY_GET_MESSAGE:
                try {
                    JSONObject jo = (JSONObject) response.get(HttpConstant.DATA);
                    messageState = jo.get(HttpConstant.IS_MESSAGE).toString();
                    SPCache.getInstance().putIsmessage(messageState);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {

                }
                break;
            case HttpConstant.MINE_INFO_REFUND:
                try {
                    String dataJson = response.getJSONObject(HttpConstant.DATA).toString();
                    SPCache.getInstance().putRefundBriefCache(dataJson);
                    refundBrief = JsonUtil.getModle(dataJson, RefundBrief.class);
                    fullRefundBriefInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {

                }
                break;

            case HttpConstant.BUY_ORDERBY_SHOP_CARD_NUM:
                try {
                    int count = response.getJSONObject(HttpConstant.DATA).optInt("Count");
                    if (0 == count) {
                        badgeview_shop_num.setVisibility(View.GONE);
                    }
                    String countStr = String.valueOf(count);

                    if (0 < count) {
                        if (99 < count) {
                            countStr = "99+";
                        }
                        badgeview_shop_num.setVisibility(View.VISIBLE);
                        badgeview_shop_num.setText(countStr);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void fullRefundBriefInfo() {
        if (refundBrief == null) {
            return;
        }
        frag_mine_balance.setText(refundBrief.getBalance());
        frag_mine_refund_pending.setText(refundBrief.getReturnMoneyWait());
        frag_mine_refund_total.setText(refundBrief.getReturnMoneyTotal());
        SharePreferenceUtil.put(mContext, SharePreferenceUtil.KEY_FANXIAN_WIAT, refundBrief.getReturnMoneyWait());
    }

    /**
     * 获得个人信息
     */
    private void pullInfo() {
        Boolean isFirestRegister = (Boolean) SharePreferenceUtil.get(mContext, SharePreferenceUtil.IS_FIRST_REGISTER_OK, false);
        if (infoBean == null) {
            return;
        }
        if (infoBean.getPlan() == 0) {
            line_plan.setVisibility(View.GONE);
            tv_fg_my_plan.setVisibility(View.GONE);
        } else {
            line_plan.setVisibility(View.VISIBLE);
            tv_fg_my_plan.setVisibility(View.VISIBLE);
        }
        if (isFirestRegister.booleanValue()) {
            SharePreferenceUtil.put(mContext, SharePreferenceUtil.IS_FIRST_REGISTER_OK, false);
            iv_fg_my_avator.setImageResource(R.mipmap.image_avator_default);
            tv_fg_my_nickname.setText(Contants.STR_ACCOUNT_INFO_EDIT);
        } else {
            String img = infoBean.getImg();
            if (!TextUtils.isEmpty(img)) {
                Picasso.with(mContext)
                        .load(img)
                        .error(R.mipmap.icon_system_logo)
                        .placeholder(R.mipmap.icon_system_logo)
                        .fit()
                        .into(iv_fg_my_avator);
            } else {
                iv_fg_my_avator.setImageResource(R.mipmap.icon_system_logo);
            }
            tv_fg_my_nickname.setText(infoBean.getNickname());
        }

    }

    /**
     * 未查询到用户信息
     */
    public void popupLogout() {
        final AlertDialog a = new AlertDialog.Builder(mContext).create();
        a.setCanceledOnTouchOutside(false);
        a.show();
        a.getWindow().setContentView(R.layout.pop_outdis);
        TextView textView = (TextView) a.getWindow().findViewById(R.id.tv_two_bt_title);
        textView.setText(Contants.STR_PROMPT);
        TextView tv = (TextView) a.getWindow().findViewById(R.id.dialog_tv_desc);
        TextView dilog_tv_cancel = (TextView) a.getWindow().findViewById(R.id.dilog_tv_cancel);
        TextView dilog_tv_ok = (TextView) a.getWindow().findViewById(R.id.dilog_tv_ok);
        View dialog_line = a.getWindow().findViewById(R.id.dialog_line);
        dialog_line.setVisibility(View.GONE);
        tv.setText(Contants.STR_ACCOUNT_NOINFO);
        dilog_tv_ok.setText(Contants.STR_GOT_IT);
        dilog_tv_cancel.setVisibility(View.GONE);
        dilog_tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePreferenceUtil.put(mContext, SharePreferenceUtil.IS_LOGIN, false);
                //清空登录相关信息
                SharePreferenceUtil.put(mContext, SharePreferenceUtil.UID, "");
                SharePreferenceUtil.put(mContext, SharePreferenceUtil.USER_NAME, "");
                SharePreferenceUtil.put(mContext, SharePreferenceUtil.NICKNAME, "");
                SharePreferenceUtil.put(mContext, SharePreferenceUtil.PHONE, "");
                SharePreferenceUtil.put(mContext, SharePreferenceUtil.KEY_OBJ_TYPE_JOB, Contants.STR_TYPE_JOB);
                SharePreferenceUtil.put(mContext, SharePreferenceUtil.KEY_BOOL_POP_JOB_ISCHEK, true);
                SharePreferenceUtil.put(mContext, SharePreferenceUtil.PROVINCE_CANBAO, "北京");
                SharePreferenceUtil.put(mContext, SharePreferenceUtil.CITY_CANBAO, "北京市");
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                ActivityCollector.removeAll();
                a.dismiss();

            }
        });
    }

    /**
     * 邀请好友下载app
     * @param platform
     * @param contentUrl
     */
    private final void shareApp(Activity mContext, SHARE_MEDIA platform, String contentUrl) {

        try{
//            if(Build.VERSION.SDK_INT>=23){
//                String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
//                ActivityCompat.requestPermissions(mContext,mPermissionList,123);
//            }
            UMShareConfig config = new UMShareConfig();
            config.isNeedAuthOnGetUserInfo(true);
            UMShareAPI.get(mContext).setShareConfig(config);
            final String title = "医保通app所售药品最高报销80%";
            final String content = "买好药，还便宜";
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.logo);
            final UMImage image = new UMImage(mContext, bitmap);
            UMWeb web = new UMWeb(contentUrl);
            web.setTitle(title);//标题
            web.setThumb(image);  //缩略图
            web.setDescription(content);
            new ShareAction(mContext).setPlatform(platform).setCallback(this)
                    .withMedia(web)
                    .share();
        }catch (ClassCastException e){
            //测试出oppo手机会报这个错
//            java.lang.ClassCastException: java.net.Socket cannot be cast to javax.net.ssl.SSLSocket
//            at com.android.okhttp.internal.http.HttpsEngine.connected(HttpsEngine.java:45)
//            at com.android.okhttp.internal.http.HttpEngine.connect(HttpEngine.java:303)
//            at com.android.okhttp.internal.http.HttpEngine.sendSocketRequest(HttpEngine.java:255)
//            at com.android.okhttp.internal.http.HttpEngine.sendRequest(HttpEngine.java:206)
//            at com.android.okhttp.internal.http.HttpURLConnectionImpl.execute(HttpURLConnectionImpl.java:355)
//            at com.android.okhttp.internal.http.HttpURLConnectionImpl.connect(HttpURLConnectionImpl.java:89)
//            at com.android.okhttp.internal.http.HttpsURLConnectionImpl.connect(HttpsURLConnectionImpl.java:161)
//            at com.sina.weibo.sdk.net.HttpManager.requestHttpExecute(HttpManager.java:80)
//            at com.sina.weibo.sdk.net.HttpManager.openUrl(HttpManager.java:63)
//            at com.sina.weibo.sdk.utils.AidTask.loadAidFromNet(AidTask.java:400)
//            at com.sina.weibo.sdk.utils.AidTask.access$200(AidTask.java:49)
//            at com.sina.weibo.sdk.utils.AidTask$2.run(AidTask.java:232)
//            at java.lang.Thread.run(Thread.java:848)
        }catch (Exception e){

        }

    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {

    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {

    }
}
