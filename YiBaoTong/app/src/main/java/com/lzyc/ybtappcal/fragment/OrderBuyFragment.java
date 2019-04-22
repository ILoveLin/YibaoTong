package com.lzyc.ybtappcal.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.ShoppingCartActivity;
import com.lzyc.ybtappcal.activity.base.BaseFragment;
import com.lzyc.ybtappcal.activity.payment.order.DekaiDetailsActivity;
import com.lzyc.ybtappcal.activity.payment.order.OrderCheckActivity;
import com.lzyc.ybtappcal.activity.payment.order.PopShuomingshu;
import com.lzyc.ybtappcal.bean.GoodsBean;
import com.lzyc.ybtappcal.bean.GoodsDrugDetail2;
import com.lzyc.ybtappcal.bean.MerchantInfo;
import com.lzyc.ybtappcal.bean.ShopingCart;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.ScreenUtils;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.StringUtils;
import com.lzyc.ybtappcal.util.Util;
import com.lzyc.ybtappcal.view.BadgeView;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.ybtappcal.widget.popupwindow.BasePopupWindow;
import com.lzyc.ybtappcal.widget.popupwindow.PopupWindowTwoButton;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

import static java.lang.Integer.parseInt;

/**
 * 购买药品
 * Created by lxx on 2017/6/27.
 */

public class OrderBuyFragment extends BaseFragment implements  UMShareListener {

    @BindView(R.id.rel_orderby)
    RelativeLayout relOrderby;
    @BindView(R.id.img_drug)
    ImageView imgDrug;
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.tv_drug_type)
    TextView tvDrugType;
    @BindView(R.id.tv_price_new)
    TextView tvPriceNew;
    @BindView(R.id.tv_price_old)
    TextView tvPriceOld;
    @BindView(R.id.tv_returnMoney)
    TextView tvReturnMoney;
    @BindView(R.id.tv_freight)
    TextView tvFreight;
    @BindView(R.id.img_discrease_num)
    ImageView imgDiscreaseNum;
    @BindView(R.id.tv_buy_num)
    TextView tvBuyNum;
    @BindView(R.id.tv_add_num)
    ImageView tvAddNum;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.tv_shiyingzheng)
    TextView tvShiyingzheng;
    @BindView(R.id.tv_yongfayongliang)
    TextView tvYongfayongliang;
    @BindView(R.id.tv_buliangfanying)
    TextView tvBuliangfanying;
    @BindView(R.id.tv_shop_num)
    TextView tvShopNum;
    @BindView(R.id.badgeview_shop_num)
    BadgeView badgeViewShopNum;
    @BindView(R.id.tv_shop_card)
    TextView tvShopCard;
    @BindView(R.id.tv_add_to_card)
    TextView tvAddToCard;
    @BindView(R.id.frame_shop_card)
    FrameLayout frameShopCard;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.lin_bottom)
    LinearLayout linBottom;
    @BindView(R.id.tv_bottom_shadow)
    TextView tvBottomShadow;
    @BindView(R.id.tv_gyzz)
    TextView tvGyzz;
    @BindView(R.id.img_pharmacy)
    ImageView imgPharmacy;
    @BindView(R.id.tv_pharmacy)
    TextView tvPharmacy;
    @BindView(R.id.img_share)
    ImageView imgShare;

    public static final int NUM1 = 1;//购买数量-
    public static final int NUM2 = 2;//购买数量+

    private final String SUB_OTC_PRE = "立即购买";
    private final String SUB_OTC_NOR = "立即预定";

    private String phoneNum;

    private Bundle bundle = new Bundle();
    public static final String OTHER_BEAN = "OtherBean";
    public static final String DRUG_NUM = "drug_num";
    public static final String INSTRUCTION_NAME = "name";
    public static final String INSTRUCTION_GOODS_NAME = "goods_name";
    public static final String INSTRUCTION_TYPE = "type";
    public static final String INSTRUCTION_INFO = "info";

    private String drugName;//弹窗药名
    private String goodsName;//弹窗药名
    String type;//是否是处方药
    String textName;

    private String DKID;

    String UID;

    private String imgaddCard;

    private String pharmacy;

    private ArrayList<ShopingCart> mDataShop = new ArrayList<>();

    boolean playLayout = false;//判断购物车动画

    GoodsDrugDetail2 goodsDrugDetail;

    String priceStr;
    String returnMoeyStr;

    /**********分享************/
    UMImage image;
    String img;
    String intro;
    String title;
    String url;

    BasePopupWindow popup;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_order_buy;
    }

    @Override
    public void init() {

        setTitleBarVisibility(View.GONE);

        setPageStateView();

        showLoading();

        if(null != getArguments()){
            bundle = getArguments();
        }

        tvPriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        DKID = bundle.getString(HttpConstant.SHOP_GOODS_PARAM_DKID, "");

        tvBuyNum.setText("1");

        requestDrugDetail();
    }

    @OnClick({R.id.lin_discrease_layout, R.id.lin_add_layout, R.id.lin_dekai, R.id.tv_shiyingzheng, R.id.tv_yongfayongliang, R.id.tv_buliangfanying, R.id.tv_add_to_card, R.id.frame_kefu, R.id.frame_shop_card, R.id.tv_submit, R.id.img_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //减少购买数量
            case R.id.lin_discrease_layout:
                changeByNum(NUM1);
                break;
            //添加购买数量
            case R.id.lin_add_layout:
                changeByNum(NUM2);
                break;
            //德开详情
            case R.id.lin_dekai:
                bundle.putString(Contants.KEY_PHARMACY, pharmacy);
                bundle.putString(Contants.KEY_PHARMACY_TITLE, tvPharmacy.getText().toString());
                openActivity(DekaiDetailsActivity.class, bundle);
                break;
            //适应症
            case R.id.tv_shiyingzheng:
                showInstructions("适应症", tvShiyingzheng);
                break;
            //用法用量
            case R.id.tv_yongfayongliang:
                showInstructions("用法用量", tvYongfayongliang);
                break;
            //不良反应
            case R.id.tv_buliangfanying:
                showInstructions("不良反应", tvBuliangfanying);
                break;
            //加入购物车
            case R.id.tv_add_to_card:
                requestEventCode("z006");
                if(!popupLogin()) return;
                requestAddShopCard();
                break;

            //客服
            case R.id.frame_kefu:
                requestEventCode("z007");
                popupCall();
                break;

            //跳转到购物车
            case R.id.frame_shop_card:
                requestEventCode("z008");
                if(!popupLogin()) return;
                openActivity(ShoppingCartActivity.class);

                break;

            //提交
            case R.id.tv_submit:
                requestEventCode("z001");
                if(!popupLogin()) return;
                mDataShop.clear();
                initShopCard(goodsDrugDetail);

                bundle.putString(DRUG_NUM, tvBuyNum.getText().toString());
                bundle.putSerializable(Contants.KEY_GOODS_LIST, mDataShop);
                bundle.putString(Contants.KEY_GOODS_PRICE_ALL, priceStr);
                bundle.putString(Contants.KEY_GOODS_PRICE_RETURN, returnMoeyStr);
                bundle.putInt(Contants.KEY_BUY_FROM_ORDERBY, 0);
                openActivity(OrderCheckActivity.class, bundle);

                break;

            case R.id.img_share:
                initPopupShare();
                break;
            default:
                break;
        }
    }

    /**
     * 用法用量弹窗
     */
    private void showInstructions(String type, TextView tv){
        bundle.putString(INSTRUCTION_GOODS_NAME, goodsName);
        bundle.putString(INSTRUCTION_NAME, drugName);
        bundle.putString(INSTRUCTION_TYPE, type);
        bundle.putString(INSTRUCTION_INFO, tv.getText().toString());
        new PopShuomingshu(tvShiyingzheng, getActivity(), bundle);
    }

    /**
     * 减购买数量
     */
    private void changeByNum(int type) {
        int num = parseInt(tvBuyNum.getText().toString());
        switch (type) {
            //减
            case NUM1:
                if (NUM1 == num) {
                    imgDiscreaseNum.setImageResource(R.mipmap.icon_buy_discrease_nor);
                } else {
                    num--;
                    tvBuyNum.setText(num + "");
                    imgDiscreaseNum.setImageResource(R.mipmap.icon_buy_discrease_pre);
                    if (NUM1 == num) {
                        imgDiscreaseNum.setImageResource(R.mipmap.icon_buy_discrease_nor);
                    }
                }
                break;
            //加
            case NUM2:
                num++;
                tvBuyNum.setText(num + "");
                imgDiscreaseNum.setImageResource(R.mipmap.icon_buy_discrease_pre);
                break;
        }

    }

    /**
     * 药品类型
     * @param goodsDrugInfoBean
     */
    private void checkDrug(GoodsDrugDetail2.GoodsDrugInfoBean goodsDrugInfoBean) {

        drugName = goodsDrugInfoBean.getName();
        goodsName = goodsDrugInfoBean.getGoodsName();

        if ("1".equals(goodsDrugInfoBean.getType())) {
            //非处方药

            type = "【非处方】";
            textName = "                 " + goodsName + " " + drugName +" "+ goodsDrugInfoBean.getSpecifications();
        } else {
            //处方药

            type = "【处方】";
            textName="             "+ goodsName + " " + drugName +" "+ goodsDrugInfoBean.getSpecifications();
        }

        if("1".equals(goodsDrugInfoBean.getOnlinePay())){
            tvSubmit.setText(SUB_OTC_PRE);//可支付
        } else {
            tvSubmit.setText(SUB_OTC_NOR);
        }
    }

    /**
     * 加入购物车圆点动画
     */
    private void pathAnimator(){
        int acitonBarHeight = DensityUtils.dp2px(50)+ ScreenUtils.getStatusHeight();
        int imgW = DensityUtils.dp2px(50);

        int[] locationStart = new int[2];
        tvAddToCard.getLocationOnScreen(locationStart);

        int[] location = new int[2];
        tvShopNum.getLocationOnScreen(location);
        int x = location[0]-tvAddNum.getWidth();
        int y = location[1]-acitonBarHeight-tvShopNum.getHeight();

        final ImageView imageView = new ImageView(mContext);

        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relOrderby.addView(imageView, param);

        Picasso.with(mContext).load(imgaddCard).resize(imgW,imgW).into(imageView);

        Path path = new Path();

        path.moveTo(locationStart[0]+tvAddToCard.getWidth()/2-imgW/2, locationStart[1]-tvAddToCard.getHeight()-acitonBarHeight);

        path.quadTo(locationStart[0]+50, locationStart[1]-DensityUtils.dp2px(300), x, y);

        final PathMeasure mPathMeasure = new PathMeasure(path, false);

        ObjectAnimator scalex = ObjectAnimator.ofFloat(imageView, View.SCALE_X, 1.0f, 0.3f);
        ObjectAnimator scaley = ObjectAnimator.ofFloat(imageView, View.SCALE_Y, 1.0f, 0.3f);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float)animation.getAnimatedValue();
                float[] mCurrentPosition = new float[2];
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                imageView.setTranslationX(mCurrentPosition[0]);
                imageView.setTranslationY(mCurrentPosition[1]);
            }
        });


        AnimatorSet animSet = new AnimatorSet();
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                try{
//                    String shopNum = tvShopNum.getText().toString();
                    String shopNum = badgeViewShopNum.getText().toString();

                    if(shopNum.contains("+")){
                        relOrderby.removeView(imageView);

                        if(!playLayout){
                            playLayout();
                        }
                        return;
                    }

                    //更新购物车数量
                    String numNewStr=tvBuyNum.getText().toString();
                    String numOldStr=badgeViewShopNum.getText().toString();
//                    String numOldStr=tvShopNum.getText().toString();

                    int buyNumNew = (int) Double.parseDouble(numNewStr);
                    int buyNumOld = (int) Double.parseDouble(numOldStr);

                    if(99 < (buyNumOld + buyNumNew)){
                        shopNum = "99+";
                    } else {
                        shopNum = String.valueOf(buyNumNew + buyNumOld);
                    }

                    tvShopNum.setText(shopNum);
                    badgeViewShopNum.setBadgeCount(shopNum);
                    badgeViewShopNum.setVisibility(View.VISIBLE);
//                    tvShopNum.setVisibility(View.VISIBLE);

                    relOrderby.removeView(imageView);

                    if(!playLayout){
                        playLayout();
                    }
                }catch (Exception e){

                }
            }
        });

        animSet.playTogether(valueAnimator,scalex, scaley);
        animSet.setDuration(800);
        animSet.start();
    }

    /**
     * 购物车动画
     */
    public void playLayout(){
        float cy = frameShopCard.getY();

        ObjectAnimator anim1 = ObjectAnimator.ofFloat(frameShopCard, "y",  cy , cy+15);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(frameShopCard, "y",  cy);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(frameShopCard, "y", cy, cy-10);
        ObjectAnimator anim4 = ObjectAnimator.ofFloat(frameShopCard, "y", cy);
        ObjectAnimator anim5 = ObjectAnimator.ofFloat(frameShopCard, "y", cy, cy+10);
        ObjectAnimator anim6 = ObjectAnimator.ofFloat(frameShopCard, "y", cy);
        ObjectAnimator anim7 = ObjectAnimator.ofFloat(frameShopCard, "y", cy, cy-5);
        ObjectAnimator anim8 = ObjectAnimator.ofFloat(frameShopCard, "y", cy);


        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim1);
        animSet.play(anim2).after(anim1);
        animSet.play(anim3).after(anim2);
        animSet.play(anim4).after(anim3);
        animSet.play(anim5).after(anim4);
        animSet.play(anim6).after(anim5);
        animSet.play(anim7).after(anim6);
        animSet.play(anim8).after(anim7);

        animSet.setDuration(100);
        animSet.start();

        playLayout = true;

        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                playLayout = false;
            }
        });

    }

    /**
     * 客服弹窗
     */

    private void popupCall() {
        String phone = StringUtils.getSubsectionPhone(phoneNum);
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(getActivity());
        twoButton.getTv_content().setText(phone);
        twoButton.getTvOk().setText("呼叫");
        twoButton.getTvCancel().setText("取消");
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phoneNum);
                intent.setData(data);
                mContext.startActivity(intent);
                twoButton.dismiss();
            }
        });
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(linBottom, Gravity.CENTER);
    }

    private void loadingData(GoodsDrugDetail2 goodsDrugDetail) {
        //商品详情
        GoodsDrugDetail2.GoodsDrugInfoBean goodsDrugInfoBean = goodsDrugDetail.getGoodsDrugInfo();
        GoodsDrugDetail2.OtherBean otherBean = goodsDrugDetail.getOther();

        MerchantInfo merchantInfo = goodsDrugDetail.getMerchantInfo();

        otherBean.setFlag(merchantInfo.getFreeShipp());

        checkDrug(goodsDrugInfoBean);

        bundle.putSerializable(Contants.GOODS_DRUG_INFO_BEAN, goodsDrugInfoBean);
        bundle.putSerializable(OTHER_BEAN, otherBean);

        tvDrugType.setText(type);
        tvGoodsName.setText(textName);
        tvPriceOld.setText(goodsDrugInfoBean.getRetailPrice());
        tvPriceNew.setText(goodsDrugInfoBean.getDeKaiPrice());
        tvReturnMoney.setText(otherBean.getReturnMoney());
        tvFreight.setText(merchantInfo.getFreightMsg());

        //药房图片  名称
        tvPharmacy.setText(merchantInfo.getName());
        Picasso.with(mContext)
                .load(merchantInfo.getLogo())
                .placeholder(R.mipmap.empty_logo)
                .error(R.mipmap.empty_logo)
                .into(imgPharmacy);

        pharmacy = merchantInfo.getDetail();

        phoneNum = merchantInfo.getPhone();


        //温馨提示
        tvNotice.setText(otherBean.getPromptMessage());

        //国药准字
        tvGyzz.setText(otherBean.getPromptMessage2());

        imgaddCard = goodsDrugInfoBean.getImage();

        Picasso.with(mContext)
                .load(imgaddCard)
                .placeholder(R.mipmap.image_empty_bg_white)
                .error(R.mipmap.image_empty_bg_white)
                .into(imgDrug);

        //用法用量
        List<String> instructions = goodsDrugDetail.getInstructions();

        try {
            String shiyingzheng = instructions.get(0);
            String yongfayongliang = instructions.get(1);
            String buliangfanying = instructions.get(2);

            tvShiyingzheng.setText(matcherText(shiyingzheng));
            tvYongfayongliang.setText(matcherText(yongfayongliang));
            tvBuliangfanying.setText(matcherText(buliangfanying));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String matcherText(String str){
        String newText;
        Pattern CRLF = Pattern.compile("(<br>)");
        Matcher m = CRLF.matcher(str);
        if (m.find()) {
            newText = m.replaceAll("\n");
            return newText;
        }
        return str;
    }

    /**
     * 检查订单值
     */
    private void initShopCard(GoodsDrugDetail2 goodsDrugDetail){

        ArrayList<GoodsBean> mDataGoodsbean = new ArrayList<>();

        MerchantInfo merchantInfo = goodsDrugDetail.getMerchantInfo();
        GoodsDrugDetail2.GoodsDrugInfoBean goodsDrugInfoBean = goodsDrugDetail.getGoodsDrugInfo();

        ShopingCart bean = new ShopingCart();
        bean.setFreeShipp(merchantInfo.getFreeShipp());
        bean.setLogo(merchantInfo.getLogo());
        bean.setFreight(merchantInfo.getFreight());
        bean.setName(merchantInfo.getName());
        bean.setID(merchantInfo.getID());

        GoodsBean goodsBean = new GoodsBean();

        double price;
        int count = 0;
        double returnMoney;

        try{
            price = Double.parseDouble(goodsDrugInfoBean.getDeKaiPrice());
            count = parseInt(tvBuyNum.getText().toString());
            returnMoney = Double.parseDouble(goodsDrugDetail.getOther().getReturnMoney());

            priceStr = Util.getFloatDotStr(String.valueOf(price * count));
            returnMoeyStr = Util.getFloatDotStr(String.valueOf(returnMoney * count));
        } catch (Exception e){

        }

        goodsBean.setGoodsName(goodsDrugInfoBean.getGoodsName());
        goodsBean.setName(goodsDrugInfoBean.getName());
        goodsBean.setGoodsCount(count);
        goodsBean.setDeKaiPrice(goodsDrugInfoBean.getDeKaiPrice());
        goodsBean.setImage(goodsDrugInfoBean.getImage());
        goodsBean.setDKID(goodsDrugInfoBean.getDKID());
        goodsBean.setVender(goodsDrugInfoBean.getVender());
        goodsBean.setOnlinePay(goodsDrugInfoBean.getOnlinePay());
        goodsBean.setRetailPrice(goodsDrugInfoBean.getRetailPrice());
        goodsBean.setSpecifications(goodsDrugInfoBean.getSpecifications());
        goodsBean.setType(goodsDrugInfoBean.getType());
        goodsBean.setReturnMoney(returnMoeyStr);

        goodsBean.setMerchantID("1");
        goodsBean.setTheShelves("1");

        mDataGoodsbean.add(goodsBean);
        bean.setGoodsList(mDataGoodsbean);

        mDataShop.add(bean);
    }

    @Override
    protected void onClickRetry() {

        showLoading();

        requestShopCardNum();
        requestDrugDetail();

        tvBuyNum.setText("1");
    }

    /**
     * 购物车数量
     */
    public void requestShopCardNum(){
        if(TextUtils.isEmpty(UID))
            return;

        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_CARD_NUM_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_CARD_NUM_SIGN);
        params.put(HttpConstant.APP_UID, UID);

        if (!NetworkUtil.CheckConnection(mContext)) {
            showToast(getResources().getString(R.string.error_net_toast));
            return;
        }

        request(params, HttpConstant.BUY_ORDERBY_SHOP_CARD_NUM);
    }

    /**
     * 加入购物车
     */
    public void requestAddShopCard(){
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_CARD_ADD_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_CARD_ADD_SIGN);
        params.put(HttpConstant.APP_UID, UID);
        params.put(HttpConstant.SHOP_CARD_ADD_GOODS_ID, DKID);
        params.put(HttpConstant.SHOP_CARD_ADD_COUNT, tvBuyNum.getText().toString());

        request(params, HttpConstant.BUY_ORDERBY_ADD_SHOP_CARD);
    }

    /**
     * 接口-->购买详情
     */
    public void requestDrugDetail() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_GOODS_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_GOODS_SIGN);
        params.put(HttpConstant.SHOP_GOODS_PARAM_DKID, DKID);

        if (!NetworkUtil.CheckConnection(mContext)) {
            showError();
            return;
        }
        params.put(HttpConstant.APP_UID, SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "").toString());
        request(params, HttpConstant.BUY_ORDERBY);

    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what){
            case HttpConstant.BUY_ORDERBY_SHOP_CARD_NUM:
                try{
                    int count = response.getJSONObject(HttpConstant.DATA).optInt("Count");

                    if(0 == count){
                        tvShopNum.setText("0");
                        badgeViewShopNum.setBadgeCount("0");
                        badgeViewShopNum.setVisibility(View.GONE);
                    }

                    String countStr = String.valueOf(count);

                    if(0 < count){
                        if(99 < count){
                            countStr = "99+";
                        }

                        badgeViewShopNum.setBadgeCount(countStr);
                        badgeViewShopNum.setVisibility(View.VISIBLE);
                    }

                }catch (Exception e){

                }
                break;
            case HttpConstant.BUY_ORDERBY_ADD_SHOP_CARD:
                try {
                    JSONObject data = response.getJSONObject(HttpConstant.DATA);
                    pathAnimator();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case HttpConstant.BUY_ORDERBY:
                try {
                    JSONObject data = response.getJSONObject(HttpConstant.DATA);
                    goodsDrugDetail = new Gson().fromJson(data.toString(), GoodsDrugDetail2.class);
                    loadingData(goodsDrugDetail);

                    showContent();

                    if(TextUtils.isEmpty(goodsDrugDetail.getGoodsDrugInfo().getInstrUrl())) return;

                    Intent intent = new Intent();
                    intent.setAction(Contants.ACTION_ORDER_INSTRU_REFRESH);
                    intent.putExtra("url", goodsDrugDetail.getGoodsDrugInfo().getInstrUrl());
//                    intent.putExtra(Contants.KEY_OBJ_DRUG_DETAIL, goodsDrugDetail);
                    mContext.sendBroadcast(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void error(String errorMsg) {
        try{
            ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);
            switch (errorResponse.getWhat()) {
                case HttpConstant.BUY_ORDERBY:
                    if(errorResponse.isNetError()) {
                        showError();
                    } else {
                        showToast(errorResponse.getMsg());
                    }
                    break;
                case HttpConstant.BUY_ORDERBY_ADD_SHOP_CARD:
                    if(errorResponse.isNetError()) {
                        showToast(getResources().getString(R.string.error_net_toast));
                    } else {
                        showToast(errorResponse.getMsg());
                    }

                    break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 分享
     */
    private void initPopupShare() {
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
//        final UMImage image = new UMImage(this, bitmap);

        initShareData();

        image = new UMImage(mContext, img);
        if(intro.isEmpty()){
            intro=" ";
        }
        if (img == null) {
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);
            image = new UMImage(mContext, bitmap);
        } else {
            image = new UMImage(mContext, img);
        }
        popup = new BasePopupWindow(getActivity(), R.layout.popup_share, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View conentView = popup.getConentView();

        conentView.findViewById(R.id.tv_popup_share_cancel).setOnClickListener(onShareClick);
        conentView.findViewById(R.id.tv_popup_share_wb).setOnClickListener(onShareClick);
        conentView.findViewById(R.id.tv_popup_share_weixin).setOnClickListener(onShareClick);
        conentView.findViewById(R.id.tv_popup_share_pyq).setOnClickListener(onShareClick);
        conentView.findViewById(R.id.tv_popup_share_qq).setOnClickListener(onShareClick);
        conentView.findViewById(R.id.tv_popup_share_qzone).setOnClickListener(onShareClick);
        conentView.findViewById(R.id.tv_popup_share_copy).setOnClickListener(onShareClick);
        popup.showPopupWindowTop(imgShare, Gravity.BOTTOM);

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(mContext, platform + "分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mContext,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            //Toast.makeText(OrderbyActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener onShareClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_popup_share_wb:
                    share(SHARE_MEDIA.SINA);
                    break;

                case R.id.tv_popup_share_weixin:
                    share(SHARE_MEDIA.WEIXIN);
                    break;

                case R.id.tv_popup_share_pyq:
                    share(SHARE_MEDIA.WEIXIN_CIRCLE);
                    break;

                case R.id.tv_popup_share_qq:
                    share(SHARE_MEDIA.QQ);
                    break;

                case R.id.tv_popup_share_qzone:
                    share(SHARE_MEDIA.QZONE);
                    break;

                case R.id.tv_popup_share_copy:
                    Util.copy(mContext, url);
                    showToast("复制成功");
                    popup.dismiss();
                    break;

                case R.id.tv_popup_share_cancel:
                    popup.dismiss();
                    break;
            }
        }
    };

    private void share(SHARE_MEDIA var){
        shareURL(var,image,url,title,intro);
        popup.dismiss();
    }

    //    /**
//     * 复制
//     */
//    private void copyUrl() {
//        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
//        ClipData mClipData = ClipData.newPlainText("Label", url);
//        cm.setPrimaryClip(mClipData);
//        showToast("复制成功");
//    }
    private void initShareData(){
        img = goodsDrugDetail.getGoodsDrugInfo().getImage();

        StringBuilder sbr = new StringBuilder();

        if(!TextUtils.isEmpty(goodsDrugDetail.getGoodsDrugInfo().getGoodsName())){
            sbr.append(goodsDrugDetail.getGoodsDrugInfo().getGoodsName() + " ");
        }

        if(!TextUtils.isEmpty(goodsDrugDetail.getGoodsDrugInfo().getName())){
            sbr.append(goodsDrugDetail.getGoodsDrugInfo().getName() + " ");
        }

        sbr.append(goodsDrugDetail.getGoodsDrugInfo().getSpecifications());

        title =  sbr.toString();

        intro = "售价¥"+goodsDrugDetail.getGoodsDrugInfo().getDeKaiPrice()+"\n"+"医保通[返现]¥"+goodsDrugDetail.getOther().getReturnMoney();

        //http://new.yibaotongapp.com/shopDrugDetail?id=2
        url = HttpConstant.BASE_URL + "/shopDrugDetail?id=" + goodsDrugDetail.getGoodsDrugInfo().getDKID();
    }

    private static final String TAG=OrderBuyFragment.class.getSimpleName();

    int tabValue;

    @Override
    public void onResume() {
        MobclickAgent.onPageStart(TAG + tabValue);
        super.onResume();
        UID = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "");
        requestShopCardNum();
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd(TAG + tabValue);
        super.onPause();
    }

    /**
     * 分享链接
     * @param platform
     * @param image
     * @param contentUrl
     * @param title
     * @param content
     */
    private  void shareURL(SHARE_MEDIA platform, UMImage image, String contentUrl, String title, String content){
        UMWeb web = new UMWeb(contentUrl);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(content);
        new ShareAction(getActivity()).setPlatform(platform).setCallback(this)
                .withMedia(web)
                .share();
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
