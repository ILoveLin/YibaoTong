package com.lzyc.ybtappcal.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dalong.marqueeview.MarqueeView;
import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView;
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter;
import com.google.gson.reflect.TypeToken;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.activity.payment.order.OrderCheckActivity;
import com.lzyc.ybtappcal.activity.payment.order.OrderbyActivity;
import com.lzyc.ybtappcal.adapter.CartExpandableAdapter;
import com.lzyc.ybtappcal.app.ActivityCollector;
import com.lzyc.ybtappcal.bean.GoodsBean;
import com.lzyc.ybtappcal.bean.ShopingCart;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.view.pagestate.PageManager;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.ybtappcal.widget.popupwindow.BasePopupWindow;
import com.lzyc.ybtappcal.widget.popupwindow.PopupWindowTwoButton;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 购物车
 * Created by yang on 2017/04/25.
 */
public class ShoppingCartActivity extends BaseActivity implements CartExpandableAdapter.OnCartItemClickListener, PageManager.OnClickListener {
   private static final String TAG=ShoppingCartActivity.class.getSimpleName();
    private static final int TYPE_BTN_DELETE = 0;
    private static final int TYPE_BTN_SETTLEMENT = 1;
    @BindView(R.id.linear_cart_remind)
    LinearLayout linearCartRemind;
    @BindView(R.id.marquee_view)
    MarqueeView marqueeView;
    @BindView(R.id.iv_selected)
    ImageView ivSelected;//是否需全选图标
    @BindView(R.id.tv_selected)
    TextView tvSelected;//是否需全选文本
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;//结算总价 总额
    @BindView(R.id.tv_fanxian)
    TextView tvFanxian;//结算总价 返现
    @BindView(R.id.tv_postage)
    TextView tvPostage;//结算邮费
    @BindView(R.id.tv_submit)
    TextView tvSubmit;//提交按钮
    @BindView(R.id.rel_bottom)
    RelativeLayout relBottom;//底部
    @BindView(R.id.expandable_lv)
    FloatingGroupExpandableListView mExpandableLv;//ExpandableListView列表控件
    @BindView(R.id.linear_place_order)
    LinearLayout linearPlaceOrder;//底部计算价格相关布局的显示隐藏
    @BindView(R.id.rel_all)
    RelativeLayout relAll;//整体的布局
    @BindString(R.string.empty_error_net_server)
    public String errorServer;
    @BindString(R.string.empty_shoping_cart)
    public String emptyMsg;
    @BindString(R.string.cart_title)
    public String titleName;
    @BindString(R.string.edit)
    public String mEdit;
    @BindString(R.string.cart_setttlement)
    public String mSetttlement;
    @BindString(R.string.delete)
    public String mDelete;
    @BindString(R.string.complete)
    public String mComplete;
    private boolean isSelectedAll = false;//是否是结算全选判断
    private boolean isSelectedAllDelete = false;//是否删除全选判断
    private CartExpandableAdapter mAdapter;
    private int btnType = TYPE_BTN_SETTLEMENT;//默认界面是结算
    private boolean isShowSoftKey = false;//监听软键盘是否弹起
    private String uid;
    private boolean isSelectedDelete = true;//是否选中删除

    @Override
    public int getContentViewId() {
        return R.layout.activity_shopping_cart;
    }

    @Override
    protected void init() {
        setPageStateView();
        mPageManager.setOnClickListener(this);
        setIntTitle();
        showLoading();
        SharePreferenceUtil.put(this, Contants.KEY_BOL_CART_REFRSH, false);
        uid = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.UID, "");
        requestCartList();
        mAdapter = new CartExpandableAdapter(this);
        WrapperExpandableListAdapter wrapperAdapter = new WrapperExpandableListAdapter(
                mAdapter);
        mExpandableLv.setAdapter(wrapperAdapter);
        tvSubmit.setBackgroundColor(ContextCompat.getColor(this, R.color.base_btn_deep_color));
        mAdapter.setOnCartItemClickListener(this);
        mExpandableLv.setAdapter(wrapperAdapter);
        mExpandableLv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
        for (int i = 0; i < wrapperAdapter.getGroupCount(); i++) {
            mExpandableLv.expandGroup(i);
        }
        marqueeView.setText("购药满50元即可获得价值49元的保险一份");
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        boolean isRefresh = (boolean) SharePreferenceUtil.get(this, Contants.KEY_BOL_CART_REFRSH, false);
        if (isRefresh) {
            SharePreferenceUtil.put(this, Contants.KEY_BOL_CART_REFRSH, false);
            requestCartList();
            ivSelected.setSelected(false);
        }
        marqueeView.startScroll();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
        marqueeView.stopScroll();
    }

    private void setIntTitle() {
        setTitleName(titleName);
        mTitleRightTvBtn.setText(mEdit);
        tvSubmit.setText(mSetttlement + "(0)");
        mTitleRightTvBtn.setVisibility(View.GONE);
    }

    /**
     * title 右侧tv按钮点击事件
     *
     * @param v
     */
    @OnClick(R.id.tv_right)
    public void onClickTitleRightTvBtn(View v) {
        if (mTitleRightTvBtn.getText().equals(mEdit)) {
            requestEventCode("l006");
            mTitleRightTvBtn.setText(mComplete);
            btnType = TYPE_BTN_DELETE;
            animationBottomRelative();
        } else {
            requestEventCode("l002");
            mTitleRightTvBtn.setText(mEdit);
            btnType = TYPE_BTN_SETTLEMENT;
            animationBottomRelative();
        }

    }

    @Override
    protected void onClickRetry() {
        requestCartList();
    }

    @OnClick({R.id.iv_selected, R.id.tv_selected, R.id.tv_submit,R.id.cart_remind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_selected:
            case R.id.tv_selected://全选
                requestEventCode("l005");
                selectedTogetherAction();
                break;
            case R.id.tv_submit://提交购物车
                cartSubmitAction();
                break;
            case R.id.cart_remind:
                showToast("去凑单");
                break;
        }
    }

    /**
     * 按钮提交，删除或结算
     */
    private void cartSubmitAction() {
        if (btnType == TYPE_BTN_DELETE) {//删除
            List<GoodsBean> selectedAll = mAdapter.getSelectedAllDrugListDelete();
            if (selectedAll.size() > 0) {
                popWindowDelete();
            } else {
                showToast("请选择要删除的商品");
            }
        } else {//结算
            requestEventCode("l004");
            switchActivity(0);
        }
    }

    /**
     * 是否删除部分浏览记录的弹窗
     */
    private void popWindowDelete() {
        final PopupWindowTwoButton popupWindow = new PopupWindowTwoButton(this);
        popupWindow.getTvTitle().setVisibility(View.GONE);
        popupWindow.getTv_content().setText("确认删除选中商品？");
        popupWindow.getTvOk().setText("确认");
        popupWindow.getTvCancel().setText("取消");
        popupWindow.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCartGoodsDeleteEffective();
                popupWindow.dismiss();
            }
        });
        popupWindow.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showPopupWindow(mExpandableLv, Gravity.CENTER);
    }


    /**
     * 全选的点击操作
     */
    private void selectedTogetherAction() {
        if (ivSelected.isSelected()) {
            ivSelected.setSelected(false);
            if (btnType == TYPE_BTN_DELETE) {
                isSelectedAllDelete = false;
            } else {
                isSelectedAll = false;
            }
        } else {
            ivSelected.setSelected(true);
            if (btnType == TYPE_BTN_DELETE) {
                isSelectedAllDelete = true;
            } else {
                isSelectedAll = true;
            }
            changeListSelected(isSelectedAll);
        }
        if (btnType == TYPE_BTN_DELETE) {
            changeListSelectedDelete(isSelectedAllDelete);
        } else {
            changeListSelected(isSelectedAll);
        }
    }

    /**
     * 结算状态，全选改变
     *
     * @param isSelectedAll
     */
    private void changeListSelected(boolean isSelectedAll) {
        List<ShopingCart> list = mAdapter.getDatas();
        if (!list.isEmpty()) {
            mAdapter.setSelectedAll(list, isSelectedAll);
        }
        setSettlementCount();//数量的改变
    }

    /**
     * 删除状态，全选改变
     *
     * @param isSelectedAll
     */
    private void changeListSelectedDelete(boolean isSelectedAll) {
        List<ShopingCart> list = mAdapter.getDatas();
        if (!list.isEmpty()) {
            mAdapter.setSelectedAllDelete(list, isSelectedAll);
        }
    }

    //组
    @Override
    public void onGroupSelectedClickListener(int groupPosition, ShopingCart cartBean, boolean isSelected, int mGroupCount) {
        mAdapter.setSelectedGroup(groupPosition, cartBean, isSelected);
        if (mGroupCount == mAdapter.getSelectedGroupCount()) {
            ivSelected.setSelected(true);
            if (btnType == TYPE_BTN_DELETE) {
                isSelectedAllDelete = true;
            } else {
                isSelectedAll = true;
            }
        } else {
            ivSelected.setSelected(false);
            if (btnType == TYPE_BTN_DELETE) {
                isSelectedAllDelete = false;
            } else {
                isSelectedAll = false;
            }
        }
        setSettlementCount();
    }

    //清除失效数据
    @Override
    public void onGroupClearClickListener(int groupPosition, ShopingCart cartBean) {
        popWindowDeleteInvalid(groupPosition, cartBean);
    }


    /**
     * 清除失效数据
     */
    private void popWindowDeleteInvalid(final int groupPosition,final ShopingCart cartBean) {
        final PopupWindowTwoButton popupWindow = new PopupWindowTwoButton(ShoppingCartActivity.this);
        popupWindow.getTvTitle().setVisibility(View.GONE);
        popupWindow.getTv_content().setText("确认清除失效商品？");
        popupWindow.getTvOk().setText("确认");
        popupWindow.getTvCancel().setText("取消");
        popupWindow.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCartGoodsDeleteInvalid();
                mAdapter.removeInvalidList(groupPosition, cartBean);
                popupWindow.dismiss();
            }
        });
        popupWindow.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showPopupWindow(mExpandableLv, Gravity.CENTER);
    }

    //单选
    @Override
    public void onChildSelectedClickListener(int groupPosition, int childPosition, boolean isSelected, int mGroupCount, int mGoodsCount) {
        mAdapter.setSelectedChild(groupPosition, childPosition, isSelected);
        updateChangeSelected(groupPosition, mGoodsCount, mGroupCount);
    }

    @Override
    public void onChildSubtractionClickListener(int groupPosition, int childPosition, int mGoodsCount, int dragCount, int mGroupCount, String dkId) {
        //减
        if (dragCount > 1) {
            dragCount = dragCount - 1;
            updateCount(groupPosition, childPosition, mGoodsCount, dragCount, mGroupCount, dkId, -1);
        } else {
            if (btnType == TYPE_BTN_DELETE) {
                showDeleteDialog(dkId, groupPosition, childPosition);
            }
        }
    }

    public void showDeleteDialog(final String orderId, final int groupPosition, final int childPosition) {
        final AlertDialog a = new AlertDialog.Builder(mContext).create();
        a.setCanceledOnTouchOutside(true);
        a.show();
        a.getWindow().setContentView(R.layout.dialog_message_delete);
        TextView dialog_message_delete = (TextView) a.getWindow().findViewById(R.id.dialog_message_delete);
        dialog_message_delete.setText("删除该药品");
        dialog_message_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCartGoodsDelete(orderId);
                if (a != null) {
                    a.dismiss();
                }
            }
        });
    }



    @Override
    public void onChildAdditionClickListener(int groupPosition, int childPosition, int mGoodsCount, int dragCount, int mGroupCount, String dkId) {
        //加
//        if (dragCount < 1000) {
        dragCount = dragCount + 1;
        updateCount(groupPosition, childPosition, mGoodsCount, dragCount, mGroupCount, dkId, 1);
//        } else {
//            showToast("最多可购买999个药品哦!");
//        }

    }

    //手动编辑数量
    @Override
    public void onChildDrugCountClickListener(int groupPosition, int childPosition, int mGoodsCount, int drugCount, int mGroupCount, String dkId) {
        popupEditDrugCount(groupPosition, childPosition, mGoodsCount, drugCount, mGroupCount, dkId);
    }

    //有效药品
    @Override
    public void onChildItemClickListener(String dkId) {
        if (!NetworkUtil.CheckConnection(this)) {
            showToast("网络不给力");
            return;
        }
        switchActivity(OrderbyActivity.class, dkId);
    }

    @Override
    public void onChildItemLongClickListener(String dkId, int groupPosition, int childPosition) {
        showDeleteDialog(dkId, groupPosition, childPosition);
        this.groupPosition=groupPosition;
        this.childPosition=childPosition;
        isSelectedDelete = false;
    }
    //失效药品
    @Override
    public void onChildItemClickInvalidListener(String message) {
        showToast("本商品已失效");
    }

    /**
     * 弹窗 手动编辑指定下标的药品数量
     *
     * @param groupPosition
     * @param childPosition
     * @param drugCount
     */
    private void popupEditDrugCount(final int groupPosition, final int childPosition, final int mGoodsCount, final int drugCount, final int mGroupCount, final String dkId) {
        final int oldCount = drugCount;
        final BasePopupWindow popupWindow = new BasePopupWindow(this, R.layout.popup_edit_drug_count, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setSoftInputMode(popupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        final View contentView = popupWindow.getConentView();
        final EditText editCount = (EditText) contentView.findViewById(R.id.pop_edit_count);
        editCount.setText("" + drugCount);
        TextView tvCancel = (TextView) contentView.findViewById(R.id.pop_edit_cancel);
        TextView tvOk = (TextView) contentView.findViewById(R.id.pop_edit_ok);
        final ImageButton mSubtraction = (ImageButton) contentView.findViewById(R.id.pop_edit_subtraction);
        ImageButton mAddition = (ImageButton) contentView.findViewById(R.id.pop_edit_addition);
        final InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(1000, InputMethodManager.HIDE_NOT_ALWAYS);
        popupWindow.showPopupWindow(mExpandableLv, Gravity.CENTER);
        if (drugCount == 1) {
            mSubtraction.setImageResource(R.mipmap.icon_operator_subtraction_black_nor1);
        } else {
            mSubtraction.setImageResource(R.mipmap.icon_operator_subtraction_black_pre1);
        }
        editCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                if (editable.length() > 3) {
//                    editCount.setText("999");
//                }
//                String text = editCount.getText().toString();
//                if (!TextUtils.isEmpty(text)) {
//                    Integer count = Integer.parseInt(text);
//                    if (count < 1) {
//                        count = 1;
//                        editCount.setText("" + count);
//                    }
//                }
//                editCount.setSelection(editCount.getText().length());
            }
        });

        contentView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right,
                                       int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > 100)) {
                    isShowSoftKey = true;
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > 100)) {
                    isShowSoftKey = false;
                }
            }
        });

        editCount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return false;
            }
        });

        editCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowSoftKey) {
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(editCount.getWindowToken(), 0);
                String text = editCount.getText().toString().trim();
                if (text.isEmpty()) {
                    return;
                }
                int count = (int) Double.parseDouble(text);
                if (count == 0) {
                    showToast("商品数不能为0");
                    return;
                }
                int incrementCount = count - oldCount;
                if (incrementCount != 0) {
                    updateCount(groupPosition, childPosition, mGoodsCount, count, mGroupCount, dkId, incrementCount);
                }
                popupWindow.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(editCount.getWindowToken(), 0);
                popupWindow.dismiss();
            }
        });
        mSubtraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = (int) Double.parseDouble(editCount.getText().toString());
                if (count > 1) {
                    count--;
                    editCount.setText("" + count);
                }
                if (count == 1) {
                    mSubtraction.setImageResource(R.mipmap.icon_operator_subtraction_black_nor1);
                } else {
                    mSubtraction.setImageResource(R.mipmap.icon_operator_subtraction_black_pre1);
                }
            }
        });
        mAddition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = (int) Double.parseDouble(editCount.getText().toString());
//                if (count <= 999) {
                count++;
                editCount.setText("" + count);
//                }
                if (count == 1) {
                    mSubtraction.setImageResource(R.mipmap.icon_operator_subtraction_black_nor1);
                } else {
                    mSubtraction.setImageResource(R.mipmap.icon_operator_subtraction_black_pre1);
                }
            }
        });
    }

    /**
     * 设置结算按钮的所选药品的数量
     * 总价，总返现
     */
    private void setSettlementCount() {
        int dragSelectedCount = mAdapter.getSelectedAllDrugCount();
        switch (btnType) {
            case TYPE_BTN_SETTLEMENT:
                tvSubmit.setText(mSetttlement + "(" + dragSelectedCount + ")");
                calculatePrice();
                break;
            case TYPE_BTN_DELETE:
                tvSubmit.setText(mDelete);
                break;
        }
    }
    int groupPosition,childPosition;
    int cgroupPosition,cchildPosition,cmGoodsCount,cmGroupCount,cdrugCount;
    /**
     * 改变数量
     *
     * @param childPosition
     * @param groupPosition
     * @param mGoodsCount
     */
    private void updateCount(int groupPosition, int childPosition, int mGoodsCount, int drugCount, int mGroupCount, String dkId, int incrementCount) {
        if (!NetworkUtil.CheckConnection(this)) {
            showToast("网络不给力");
            return;
        }
        this.cgroupPosition=groupPosition;
        this.cchildPosition=childPosition;
        this.cmGoodsCount=mGoodsCount;
        this.cmGroupCount=mGroupCount;
        this.cdrugCount=drugCount;
        requestAddShopCard(dkId, incrementCount);
    }

    /**
     * 改变选中状态
     *
     * @param groupPosition group下标
     * @param mGoodsCount   指定group下标，child的数量
     * @param mGroupCount   所有group的数量
     */
    private void updateChangeSelected(int groupPosition, int mGoodsCount, int mGroupCount) {
        setGroupSelected(groupPosition, mGoodsCount);
        if (mGroupCount == mAdapter.getSelectedGroupCount()) {
            ivSelected.setSelected(true);
            if (btnType == TYPE_BTN_DELETE) {
                isSelectedAllDelete = true;
            } else {
                isSelectedAll = true;
            }
        } else {
            ivSelected.setSelected(false);
            if (btnType == TYPE_BTN_DELETE) {
                isSelectedAllDelete = false;
            } else {
                isSelectedAll = false;
            }
        }
        setTitleCount();
    }

    /**
     * 设置group是否可选
     *
     * @param groupPosition 组下标
     * @param mGoodsCount   指定组下标下的goods大小
     */
    private void setGroupSelected(int groupPosition, int mGoodsCount) {
        int groupCount = mAdapter.getSelectedGroupCount(groupPosition);
        if (groupCount == mGoodsCount) {
            mAdapter.setSelectedGroup(groupPosition, true);
        } else {
            mAdapter.setSelectedGroup(groupPosition, false);
        }
    }

    /**
     * 设置购物车标题、结算按钮的数量
     * 显示结算、删除状态
     */
    private void setTitleCount() {
        int drugCount = mAdapter.getDrugCount();
        setTitleName(titleName + "(" + drugCount + ")");
        if (drugCount == 0) {
            mTitleRightTvBtn.setVisibility(View.GONE);
        } else {
            mTitleRightTvBtn.setVisibility(View.VISIBLE);
        }
        setSettlementCount();
        //
    }

    /**
     * 计算价钱
     */
    private void calculatePrice() {
        String[] strArray = mAdapter.getSelectedAllListPrice();
        tvTotalMoney.setText(strArray[0]);
        tvFanxian.setText(strArray[1]);
    }


    /**
     * 组装ids字符串
     *
     * @param listGoods
     * @return
     */
    private String getIds(List<GoodsBean> listGoods) {
        String ids;
        StringBuffer sb = new StringBuffer();
        int size = listGoods.size();
        if (size == 1) {
            ids = sb.append(listGoods.get(0).getDKID()).toString();
        } else {
            for (int i = 0; i < size; i++) {
                GoodsBean good = listGoods.get(i);
                sb.append(good.getDKID() + ",");
            }
            String strIds = sb.toString();
            ids = strIds.substring(0, strIds.length() - 1);
        }
        return ids;
    }

    /**
     * 结算提交
     * 跳转界面
     */
    private void switchActivity(int type) {
        ArrayList<ShopingCart> mlist = mAdapter.getJiesuanList();
        String[] strPrice = mAdapter.getSelectedAllListPrice();
        if (!mlist.isEmpty()) {
            Bundle mBundle = new Bundle();
            mBundle.putSerializable(Contants.KEY_GOODS_LIST, mlist);
            mBundle.putString(Contants.KEY_GOODS_PRICE_ALL, strPrice[0]);
            mBundle.putString(Contants.KEY_GOODS_PRICE_RETURN, strPrice[1]);
            openActivity(OrderCheckActivity.class, mBundle);
        } else {
            if (type == 0) {//点击结算
                showToast("请选择要购买的商品");
            }
        }
    }

    /**
     * @param clz
     * @param dkId
     */
    private void switchActivity(Class<? extends Activity> clz, String dkId) {
        Bundle mBundle = new Bundle();
        mBundle.putInt(Contants.KEY_PAGE_PAYMENT, Contants.VAL_PAGE_PAYMENT_CART);
        mBundle.putString(HttpConstant.SHOP_GOODS_PARAM_DKID, dkId);
        openActivity(clz, mBundle);
    }

//关键字： 网络请求

    /**
     * 获取购物车
     */
    private void requestCartList() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_CART_LIST_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_CART_LIST_SIGN);
        params.put(HttpConstant.APP_UID, uid);
        if (!NetworkUtil.CheckConnection(this)) {
            showError();
            mTitleRightTvBtn.setVisibility(View.GONE);
            return;
        }
        request(params, HttpConstant.SHOP_CART_LIST);
    }

    /**
     * 加入购物车
     * 改变购物车商品数量
     */
    public void requestAddShopCard(String dkId, int incrementCount) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_CARD_ADD_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_CARD_ADD_SIGN);
        params.put(HttpConstant.APP_UID, uid);
        params.put(HttpConstant.SHOP_CARD_ADD_GOODS_ID, dkId);
        params.put(HttpConstant.SHOP_CARD_ADD_COUNT, "" + incrementCount);
        if (!NetworkUtil.CheckConnection(this)) {
            showToast("网络不给力");
            return;
        }
        request(params, HttpConstant.BUY_ORDERBY_ADD_SHOP_CARD);
    }


    /**
     * 删除购物车有效商品
     */
    private void requestCartGoodsDeleteEffective() {
        List<GoodsBean> listGoodsEffective = mAdapter.getSelectedAll();
        String ids = getIds(listGoodsEffective);
        requestCartGoodsDelete(ids);
    }

    /**
     * 删除购物车无效商品
     */
    private void requestCartGoodsDeleteInvalid() {
        requestEventCode("l003");
        List<GoodsBean> listGoodsInvalid = mAdapter.getSelectedInvalidGoodsAll();
        String ids = getIds(listGoodsInvalid);
        requestCartGoodsDelete(ids);
    }

    /**
     * 删除购物车商品
     */
    private void requestCartGoodsDelete(String ids) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_CART_DEL_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_CART_DEL_SIGN);
        params.put(HttpConstant.APP_UID, uid);
        params.put(HttpConstant.IDS, ids);
        if (!NetworkUtil.CheckConnection(this)) {
            showToast(errorNetToast);
            return;
        }
        request(params, HttpConstant.SHOP_CART_DEL);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        LogUtil.y("##done"+response.toString());
        switch (what) {
            case HttpConstant.SHOP_CART_LIST:
                try {
                    JSONObject data = response.getJSONObject("data");
                    Type shoppingCartType = new TypeToken<ArrayList<ShopingCart>>() {
                    }.getType();
                    Type goodsBeanType = new TypeToken<ArrayList<GoodsBean>>() {
                    }.getType();
                    List<ShopingCart> listShoppingCart = JsonUtil.getListModle(data.toString(), "ShopingCartList", shoppingCartType);
                    ArrayList<GoodsBean> listDownShelves = JsonUtil.getListModle2(data.toString(), "DownShelves", goodsBeanType);
                    if (!listDownShelves.isEmpty()) {
                        ShopingCart shopingCart = new ShopingCart();
                        shopingCart.setInvalidList(true);
                        shopingCart.setGoodsList(listDownShelves);
                        listShoppingCart.add(shopingCart);
                    }
                    LogUtil.y("###SHOP_CART_LIST###" + listShoppingCart.size());
                    if (listShoppingCart.size() == 0) {
                        showEmpty(emptyMsg, R.mipmap.empty_shopping_cart, View.VISIBLE);
                        setTitleName(titleName+"(0)");
                        mTitleRightTvBtn.setVisibility(View.GONE);
                    } else {
                        mAdapter.setList(listShoppingCart);
                        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
                            mExpandableLv.expandGroup(i);
                        }
                        showContent();
                        setTitleCount();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showEmpty(errorServer, R.mipmap.empty_error_net_server);
                }catch(Exception e){
                    e.printStackTrace();
                    showEmpty(errorServer, R.mipmap.empty_error_net_server);
                }
                break;
            case HttpConstant.SHOP_CART_DEL:
                try{
                    if (isSelectedDelete) {//全选中删除
                        List<GoodsBean> list = mAdapter.getSelectedAll();
                        mAdapter.removeList(list);
                    } else {//非全选中删除
                        mAdapter.removeItemChild(groupPosition,childPosition);
                    }
                    int count = mAdapter.getSelectedAll().size();
                    int allCount = mAdapter.getAllChildCount();
                    if (count == allCount) {
                        isSelectedAll = true;
                        ivSelected.setSelected(true);
                    } else {
                        isSelectedAll = false;
                        ivSelected.setSelected(false);
                    }
                    List<ShopingCart> datas = mAdapter.getDatas();
                    for (int i = 0; i < datas.size(); i++) {
                        ShopingCart s = (ShopingCart) mAdapter.getGroup(i);
                        int sCount = s.getGoodsList().size();
                        int groupSelectedCount = mAdapter.getGroupSelectedCount(i);
                        if (sCount == groupSelectedCount) {
                            mAdapter.setSelectedGroup(i, true);
                        }
                    }
                    if (mAdapter.getDatas().size() == 0) {
                        showEmpty(emptyMsg, R.mipmap.empty_shopping_cart, View.VISIBLE);
                        setTitleName(titleName+"(0)");
                        mTitleRightTvBtn.setVisibility(View.GONE);
                    }else{
                        long drugCount = mAdapter.getDrugCount();
                        setTitleName(titleName+"("+drugCount+")");
                        mAdapter.notifyDataSetChanged();
                    }
                }catch (IndexOutOfBoundsException e){
                    LogUtil.y("报错");
                    requestCartList();
                }

                break;
            case HttpConstant.BUY_ORDERBY_ADD_SHOP_CARD:
                mAdapter.updateItemDrugCount(cgroupPosition, cchildPosition, cdrugCount);
                updateChangeSelected(cgroupPosition, cmGoodsCount, cmGroupCount);
                LogUtil.y("####改变购物车数量返回的JSON####" + response.toString());
                break;
        }
    }

    @Override
    public void error(String errorMsg) {
        LogUtil.y("#error#"+errorMsg);
        try{
            ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);
            String msg=errorResponse.getMsg();
            switch (errorResponse.getWhat()) {
                case HttpConstant.SHOP_CART_LIST:
                    if(errorResponse.isNetError()){
                        showError();
                    }
                    break;
                case HttpConstant.SHOP_CART_DEL:
                    if(errorResponse.isNetError()) {
                        showToast("网络不给力");
                        return;
                    }
                    if(!TextUtils.isEmpty(msg)){
                        showToast(msg);
                    }
                    break;
                case HttpConstant.BUY_ORDERBY_ADD_SHOP_CARD:
                    if(!TextUtils.isEmpty(msg)){
                        showToast(msg);
                    }
                    break;
            }
        }catch (Exception e){

        }


    }

//关键字:动画

    /**
     * 底部导航条显示的动画
     */
    private void animationBottomRelative() {
        relAll.setBackgroundColor(Color.argb(0, 0, 0, 0));
        TranslateAnimation animationBottom = new TranslateAnimation(0, 0, 0, relBottom.getHeight());
        animationBottom.setDuration(300);
        animationBottom.setFillAfter(true);
        relBottom.startAnimation(animationBottom);
        animationBottom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animationTopRelative();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void animationTopRelative() {
        TranslateAnimation animationBottom = new TranslateAnimation(0, 0, relBottom.getHeight(), 0);
        animationBottom.setDuration(300);
        animationBottom.setFillAfter(true);
        relBottom.startAnimation(animationBottom);
        animationBottom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                switch (btnType) {
                    case TYPE_BTN_DELETE:
                        linearPlaceOrder.setVisibility(View.INVISIBLE);
                        tvSubmit.setText(mDelete);
                        mAdapter.setDeleteStatus(true);
                        ivSelected.setSelected(false);
                        tvSubmit.setBackgroundColor(ContextCompat.getColor(ShoppingCartActivity.this, R.color.base_btn_light_color));
//                        changeListSelected(false);
                        break;
                    case TYPE_BTN_SETTLEMENT:
                        tvSubmit.setBackgroundColor(ContextCompat.getColor(ShoppingCartActivity.this, R.color.base_btn_deep_color));
                        linearPlaceOrder.setVisibility(View.VISIBLE);
                        mAdapter.setDeleteStatus(false);
                        setTitleCount();
                        changeListSelectedDelete(false);
                        ivSelected.setSelected(isSelectedAll);
                        int count = mAdapter.getSelectedAll().size();
                        int allCount = mAdapter.getAllChildCount();
                        if (count == allCount) {
                            isSelectedAll = true;
                            ivSelected.setSelected(true);
                        } else {
                            isSelectedAll = false;
                            ivSelected.setSelected(false);
                        }
                        List<ShopingCart> datas = mAdapter.getDatas();
                        for (int i = 0; i < datas.size(); i++) {
                            ShopingCart s = (ShopingCart) mAdapter.getGroup(i);
                            int sCount = s.getGoodsList().size();
                            int groupSelectedCount = mAdapter.getGroupSelectedCount(i);
                            if (sCount == groupSelectedCount) {
                                mAdapter.setSelectedGroup(i, true);
                            }
                        }
                        break;
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onBtnClickListener() {//去逛逛吧
        this.finish();
        ActivityCollector.removeAll();
        SharePreferenceUtil.put(this, SharePreferenceUtil.DYNAMIC_SWITCH_TAB, Contants.TAB_HOME);
        openActivityNoAnim(MainActivity.class);
    }

}
