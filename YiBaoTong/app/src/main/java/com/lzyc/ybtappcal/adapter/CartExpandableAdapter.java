package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.GoodsBean;
import com.lzyc.ybtappcal.bean.ShopingCart;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.LogUtil;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * 购物车ExpandableListView适配器
 * Created by yang on 2017/04/25.
 */
public class CartExpandableAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<ShopingCart> mDatas = new ArrayList<>();
    private OnCartItemClickListener listener;
    private boolean isDelete=false;//是否删除状态

    public CartExpandableAdapter(Context context) {
        mContext = context;
    }
    /**
     * 设置是否是删除状态
     * @param isDelete
     */
    public void setDeleteStatus(boolean isDelete){
        this.isDelete=isDelete;
        notifyDataSetChanged();
    }

    public void setList(List<ShopingCart> mListShopingCart) {
        for (int i = 0; i <mListShopingCart.size() ; i++) {
            ShopingCart sc=mListShopingCart.get(i);
            ArrayList<GoodsBean> listGoodsBean=sc.getGoodsList();
            for (int j = 0; j <listGoodsBean.size() ; j++) {
                GoodsBean g=listGoodsBean.get(j);
                g.setGroupPosition(i);
                listGoodsBean.set(j,g);
            }
            sc.setGoodsList(listGoodsBean);
            mListShopingCart.set(i,sc);
        }
        this.mDatas = mListShopingCart;
        notifyDataSetChanged();
    }

    public void setOnCartItemClickListener(OnCartItemClickListener onCartItemClickListener) {
        this.listener = onCartItemClickListener;
    }

    /**
     * 改变数量
     *
     * @param groupPosition
     * @param childPosition
     * @param drugCount
     */
    public void updateItemDrugCount(int groupPosition, int childPosition, int drugCount) {
        ShopingCart shoppingCartBean = mDatas.get(groupPosition);
        ArrayList<GoodsBean> goods = shoppingCartBean.getGoodsList();
        for (int i = 0; i < goods.size(); i++) {
            GoodsBean good = goods.get(i);
            if (i == childPosition) {
                good.setGoodsCount(drugCount);
                if(isDelete){
                    good.setChildDeleteSelected(true);
                }else{
                    good.setChildSelected(true);
                }
            }
            goods.set(i, good);
        }
        shoppingCartBean.setGoodsList(goods);
        mDatas.set(groupPosition, shoppingCartBean);
        notifyDataSetChanged();
    }

    /**
     * 结算
     * 计算总价
     * 计算总返现
     */
    public String[] getSelectedAllListPrice() {
        List<GoodsBean> selectedAllList = getSelectedAll();
        double totalPrice = 0;
        double totalPriceFanxian = 0;
        int size = selectedAllList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                GoodsBean goods = selectedAllList.get(i);
                int drugCount = goods.getGoodsCount();
                double price = Double.parseDouble(goods.getDeKaiPrice());
                double priceFanxain = Double.parseDouble(goods.getReturnMoney());
                totalPrice = totalPrice + price * drugCount;
                totalPriceFanxian = totalPriceFanxian + priceFanxain * drugCount;
            }
        }
        String sTotalPrice = new DecimalFormat("0.00").format(totalPrice);
        String sTotalPriceFanxian = new DecimalFormat("0.00").format(totalPriceFanxian);
//        int totalPriceFanxianInt= (int) totalPriceFanxian;
//        String sTotalPriceFanxian = new DecimalFormat("0.00").format(totalPriceFanxian);
        String[] strPriceArray = new String[]{sTotalPrice, ""+sTotalPriceFanxian};
        return strPriceArray;
    }

    public void removeList(List<GoodsBean> list){
        for (int i = 0; i <list.size() ; i++) {
            GoodsBean g=list.get(i);
            ShopingCart cart=mDatas.get(g.getGroupPosition());
            ArrayList<GoodsBean> listG=cart.getGoodsList();
            for (int j = 0; j <listG.size() ; j++) {
                GoodsBean goodsBean=listG.get(j);
                if(goodsBean.getDKID().equals(g.getDKID())){
                    listG.remove(j);
                }
            }
            cart.setGoodsList(listG);
            if(listG.size()==0){
                mDatas.remove(g.getGroupPosition());
            }else{
                mDatas.set(g.getGroupPosition(),cart);
            }
        }
        notifyDataSetChanged();
    }


    /**
     * 删除失效药品
     *
     * @param groupPosition
     * @param cartBean
     */
    public void removeInvalidList(int groupPosition, ShopingCart cartBean) {
        if (mDatas.contains(cartBean)) {
            mDatas.remove(cartBean);
        }
        notifyDataSetChanged();
    }

    /**
     * 移除某一行childItem
     * @param groupPosition
     * @param childPosition
     */
    public void removeItemChild(int groupPosition,int childPosition) {
        ShopingCart shopingCart = mDatas.get(groupPosition);
        ArrayList<GoodsBean> goodsList = shopingCart.getGoodsList();
        goodsList.remove(childPosition);
        shopingCart.setGoodsList(goodsList);
        if(goodsList.size()==0){
            mDatas.remove(groupPosition);
        }else{
            mDatas.set(groupPosition,shopingCart);
        }
        notifyDataSetChanged();
    }
    /**
     * 设置是否全选
     *
     * @param listShopBean
     * @param isSelected
     */
    public void setSelectedAll(List<ShopingCart> listShopBean, boolean isSelected) {
        LogUtil.y("###########isDelete#################"+isDelete);
        for (int i = 0; i < listShopBean.size(); i++) {
            ShopingCart shoppingCartBean = listShopBean.get(i);
            if(isDelete){
                shoppingCartBean.setGroupDeleteSelected(isSelected);
            }else{
                shoppingCartBean.setIsGroupSelected(isSelected);
            }
            ArrayList<GoodsBean> mGoodsList = shoppingCartBean.getGoodsList();
            for (int j = 0; j < mGoodsList.size(); j++) {
                GoodsBean goods = mGoodsList.get(j);
                if(isDelete){
                    goods.setChildDeleteSelected(isSelected);
                }else{
                    goods.setChildSelected(isSelected);
                }
                mGoodsList.set(j, goods);
            }
            shoppingCartBean.setGoodsList(mGoodsList);
            mDatas.set(i, shoppingCartBean);
        }
        notifyDataSetChanged();
    }

    /**
     * 设置是否全选
     *
     * @param listShopBean
     * @param isSelected
     */
    public void setSelectedAllDelete(List<ShopingCart> listShopBean, boolean isSelected) {
        for (int i = 0; i < listShopBean.size(); i++) {
            ShopingCart shoppingCartBean = listShopBean.get(i);
            shoppingCartBean.setGroupDeleteSelected(isSelected);
            ArrayList<GoodsBean> mGoodsList = shoppingCartBean.getGoodsList();
            for (int j = 0; j < mGoodsList.size(); j++) {
                GoodsBean goods = mGoodsList.get(j);
                goods.setChildDeleteSelected(isSelected);
                mGoodsList.set(j, goods);
            }
            shoppingCartBean.setGoodsList(mGoodsList);
            mDatas.set(i, shoppingCartBean);
        }
        notifyDataSetChanged();
    }

    /**
     * 指定下标设置group是否选中
     *
     * @param position
     * @param isSelected
     */
    public void setSelectedGroup(int position, boolean isSelected) {
        ShopingCart shoppingCartBean = mDatas.get(position);
        LogUtil.y("##########setSelectedGroup###########isDelete#####"+isDelete);
        if(isDelete){
            shoppingCartBean.setGroupDeleteSelected(isSelected);
        }else{
            shoppingCartBean.setIsGroupSelected(isSelected);
        }
        mDatas.set(position, shoppingCartBean);
    }

    /**
     * 获取指定group被选中的数量
     * @param position
     * @return
     */
    public int getGroupSelectedCount(int position){
        int count=0;
        ShopingCart shopingCart=mDatas.get(position);
        List<GoodsBean> list=shopingCart.getGoodsList();
        for (int i = 0; i <list.size() ; i++) {
            GoodsBean g=list.get(i);
            if(isDelete){
                if(g.isChildDeleteSelected()){
                    count++;
                }
            }else{
                if(g.isChildSelected()){
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 设置某个group是否全选
     *
     * @param groupPosition
     * @param gropBean
     * @param isSelected
     */
    public void setSelectedGroup(int groupPosition, ShopingCart gropBean, boolean isSelected) {
        ArrayList<GoodsBean> mGoodsList = gropBean.getGoodsList();
        for (int i = 0; i < mGoodsList.size(); i++) {
            GoodsBean goods = mGoodsList.get(i);
            if(isDelete){
                goods.setChildDeleteSelected(isSelected);
            }else{
                goods.setChildSelected(isSelected);
            }
            mGoodsList.set(i, goods);
        }
        ShopingCart shoppingCartBean = mDatas.get(groupPosition);
        shoppingCartBean.setGoodsList(mGoodsList);
        if(isDelete){
            shoppingCartBean.setGroupDeleteSelected(isSelected);
        }else{
            shoppingCartBean.setIsGroupSelected(isSelected);
        }
        mDatas.set(groupPosition, shoppingCartBean);
        notifyDataSetChanged();
    }

    /**
     * 设置某个child是否选择
     *
     * @param groupPosition
     * @param childPosition
     * @param isSelected
     */
    public void setSelectedChild(int groupPosition, int childPosition, boolean isSelected) {
        ShopingCart cartbean = mDatas.get(groupPosition);
        LogUtil.y("##########setSelectedChild###########isDelete#####"+isDelete);
        ArrayList<GoodsBean> mGoodsList = cartbean.getGoodsList();
        for (int i = 0; i < mGoodsList.size(); i++) {
            GoodsBean good = mGoodsList.get(i);
            if (i == childPosition) {
                if(isDelete){
                    good.setChildDeleteSelected(isSelected);
                }else{
                    good.setChildSelected(isSelected);
                }
                mGoodsList.set(i, good);
            }
            cartbean.setGoodsList(mGoodsList);
        }
        mDatas.set(groupPosition, cartbean);
        notifyDataSetChanged();
    }

    /**
     * 获取全部被选中的good数据,
     * 获取全部药品列表
     * @return List ShoppingCartBean
     */
    public ArrayList<GoodsBean> getSelectedAll() {
        ArrayList<GoodsBean> listSelectedAll = new ArrayList<>();
        LogUtil.y("######mDatas######"+mDatas.size());
        for (int i = 0; i < mDatas.size(); i++) {
            ShopingCart cartBean = mDatas.get(i);
            LogUtil.y("######ShopingCart#####json##"+new Gson().toJson(cartBean));
            ArrayList<GoodsBean> goodsList = cartBean.getGoodsList();
            for (int j = 0; j < goodsList.size(); j++) {
                GoodsBean good = goodsList.get(j);
                good.setGroupPosition(i);
                good.setChildPosition(j);
                if(isDelete){
                    if (good.isChildDeleteSelected()&& !cartBean.isInvalidList()) {
                        listSelectedAll.add(good);
                    }
                }else{
                    if (good.isChildSelected() && !cartBean.isInvalidList()) {
                        listSelectedAll.add(good);
                    }
                }
            }
        }
        return listSelectedAll;
    }

    /**
     * 结算时选中的药品
     * @return ArrayList<ShopingCart>
     */
    public ArrayList<ShopingCart> getJiesuanList() {
        ArrayList<ShopingCart> list = new ArrayList<ShopingCart>();
        for (int i = 0; i < mDatas.size(); i++) {
            ShopingCart cartBean = mDatas.get(i);
            ShopingCart cartBeanNew = new ShopingCart();
            ArrayList<GoodsBean> goodsList = cartBean.getGoodsList();
            ArrayList<GoodsBean> arrayListGoodsNew=new ArrayList<>();
            for (int j = 0; j < goodsList.size(); j++) {
                GoodsBean good = goodsList.get(j);
                if (good.isChildSelected() && !cartBean.isInvalidList()) {
                    arrayListGoodsNew.add(good);
                }
            }
            for (int j = 0; j < arrayListGoodsNew.size(); j++) {
                GoodsBean good = arrayListGoodsNew.get(j);
                if (good.isChildSelected() && !cartBean.isInvalidList()) {
                    cartBeanNew.setName(cartBean.getName());
                    cartBeanNew.setFreight(cartBean.getFreight());
                    cartBeanNew.setMsg(cartBean.getMsg());
                    cartBeanNew.setInvalidList(cartBean.isInvalidList());
                    cartBeanNew.setID(cartBean.getID());
                    cartBeanNew.setLogo(cartBean.getLogo());
                    cartBeanNew.setStatus(cartBean.getStatus());
                    cartBeanNew.setIsGroupSelected(cartBean.isGroupSelected());
                    cartBeanNew.setGoodsList(arrayListGoodsNew);
                    cartBeanNew.setFreeShipp(cartBean.getFreeShipp());
                    list.add(cartBeanNew);
                    break;
                }
            }
        }
        LogUtil.y("#######################进来了嘛#################"+list.size());
        return list;
    }


    /**
     * 获取所有被选中的group的数据
     *
     * @return
     */
    public List<ShopingCart> getSelectedGroup() {
        List<ShopingCart> shoppingCartBeanList = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            ShopingCart shoppingCartBean = mDatas.get(i);
            if(isDelete){
                if (shoppingCartBean.isGroupDeleteSelected() && !shoppingCartBean.isInvalidList()) {
                    shoppingCartBeanList.add(shoppingCartBean);
                }
            }else{
                if (shoppingCartBean.isGroupSelected() && !shoppingCartBean.isInvalidList()) {
                    shoppingCartBeanList.add(shoppingCartBean);
                }
            }
        }
        return shoppingCartBeanList;
    }

    /**
     * 获取所有被选中的数据
     *
     * @return
     */
    public int getSelectedAllDrugCount() {
        int count = 0;
        List<GoodsBean> listG = getSelectedAllDrugList();
        int size = listG.size();
        if (size > 0) {
            for (int i = 0; i < listG.size(); i++) {
                GoodsBean g = listG.get(i);
                count = count + g.getGoodsCount();
            }
        }
        return count;
    }

    /**
     * 获取所有被选中的数据
     *
     * @return
     */
    public List<GoodsBean> getSelectedAllDrugList() {
        List<GoodsBean> listGoods = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            ShopingCart shoppingCartBean = mDatas.get(i);
            if (!shoppingCartBean.isInvalidList()) {//非失效数据
                ArrayList<GoodsBean> goods = shoppingCartBean.getGoodsList();
                for (int j = 0; j < goods.size(); j++) {
                    GoodsBean g = goods.get(j);
                    if(isDelete){
                        if (g.isChildDeleteSelected()) {
                            listGoods.add(g);
                        }
                    }else{
                        if (g.isChildSelected()) {
                            listGoods.add(g);
                        }
                    }
                }
            }

        }
        return listGoods;
    }
    /**
     * 删除状态获取所有被选中的数据
     *
     * @return
     */
    public List<GoodsBean> getSelectedAllDrugListDelete() {
        List<GoodsBean> listGoods = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            ShopingCart shoppingCartBean = mDatas.get(i);
            if (!shoppingCartBean.isInvalidList()) {//非失效数据
                ArrayList<GoodsBean> goods = shoppingCartBean.getGoodsList();
                for (int j = 0; j < goods.size(); j++) {
                    GoodsBean g = goods.get(j);
                    g.setGroupPosition(i);
                    g.setChildPosition(j);
                    if (g.isChildDeleteSelected()) {
                        listGoods.add(g);
                    }
                }
            }

        }
        return listGoods;
    }

    /**
     * 获取所有有效的group的数据
     *
     * @return
     */
    public List<ShopingCart> getEffectiveGroup() {
        List<ShopingCart> shoppingCartBeanList = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            ShopingCart shoppingCartBean = mDatas.get(i);
            if (!shoppingCartBean.isInvalidList()) {
                shoppingCartBeanList.add(shoppingCartBean);
            }
        }
        return shoppingCartBeanList;
    }

    /**
     * 获取所有有效的group的数据
     *
     * @return
     */
    public int getEffectiveGroupCount() {
        int count = getEffectiveGroup().size();
        return count;
    }

    /**
     * 获取所有被选中的group的数据
     *
     * @return
     */
    public int getSelectedGroupCount() {
        int count = getSelectedGroup().size();
        return count;
    }

    /**
     * 获取所有child的数据
     *
     * @return
     */
    public int getAllChildCount() {
        int count=0;
        for (int i = 0; i <mDatas.size() ; i++) {
            ShopingCart s=mDatas.get(i);
            if(!s.isInvalidList()){
                count=count+s.getGoodsList().size();
            }
        }
        return count;
    }

    /**
     * 获取所有group无效药品的数据
     *
     * @return
     */
    public List<GoodsBean> getSelectedInvalidGoodsAll() {
        List<GoodsBean> goodsList = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            ShopingCart shoppingCartBean = mDatas.get(i);
            if (shoppingCartBean.isInvalidList()) {
                for (int j = 0; j < shoppingCartBean.getGoodsList().size(); j++) {
                    goodsList.add(shoppingCartBean.getGoodsList().get(j));
                }
            }
        }
        return goodsList;
    }

    /**
     * 获取所有group无效药品的数据
     *
     * @return
     */
    public int getSelectedGroupInvalidCount() {
        List<ShopingCart> shoppingCartBeanList = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            ShopingCart shoppingCartBean = mDatas.get(i);
            if (shoppingCartBean.isInvalidList()) {
                shoppingCartBeanList.add(shoppingCartBean);
            }
        }
        return shoppingCartBeanList.size();
    }


    /**
     * 获取某个group被选中的数据
     *
     * @param groupPosition
     * @return
     */
    public List<GoodsBean> getSelectedAll(int groupPosition) {
        List<GoodsBean> goodsListGroup = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            if (groupPosition == i) {
                ShopingCart cartBean = mDatas.get(i);
                ArrayList<GoodsBean> goodsList = cartBean.getGoodsList();
                for (int j = 0; j < goodsList.size(); j++) {
                    GoodsBean good = goodsList.get(j);
                    if(isDelete){
                        if (good.isChildDeleteSelected()) {
                            goodsListGroup.add(good);
                        }
                    }else{
                        if (good.isChildSelected()) {
                            goodsListGroup.add(good);
                        }
                    }
                }
            }
        }
        return goodsListGroup;
    }

    /**
     * 获取某个group被选中的数据
     *
     * @param positionGroup
     * @return
     */
    public int getSelectedGroupCount(int positionGroup) {
        int groupChildCount = getSelectedAll(positionGroup).size();
        return groupChildCount;
    }

    public int getGroupCount() {
        return mDatas.size();
    }

    /**
     * 获取所有药品的数量
     *
     * @return
     */
    public int getDrugCount() {
        int count = 0;
        for (int i = 0; i < mDatas.size(); i++) {
            ShopingCart shoppingCartBean = mDatas.get(i);
            ArrayList<GoodsBean> goods = shoppingCartBean.getGoodsList();
            if(!shoppingCartBean.isInvalidList()){
                for (int j = 0; j < goods.size(); j++) {
                    GoodsBean goods1 = goods.get(j);
                    count = count + goods1.getGoodsCount();
                }
            }

        }
        return count;
    }

    public List<ShopingCart> getDatas() {
        return mDatas;
    }

    public int getChildrenCount(int groupPosition) {
        return mDatas.get(groupPosition).getGoodsList().size();
    }

    public Object getGroup(int groupPosition) {
        return mDatas.get(groupPosition);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return mDatas.get(groupPosition).getGoodsList().get(childPosition);
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public boolean hasStableIds() {
        return false;
    }

    //父视图 parent  GroupView 组视图
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_cart_expandable_gourp, parent, false);
            holder.groupResLogo= (ImageView) convertView.findViewById(R.id.res_merchant_logo);
            holder.groupResName= (TextView) convertView.findViewById(R.id.res_merchant_name);
            holder.groupEffective = (LinearLayout) convertView.findViewById(R.id.cart_group_linear_effective);//有效的
            holder.groupInvalid = (LinearLayout) convertView.findViewById(R.id.cart_group_linear_invalid);//无效的
            holder.vInvalidLine = convertView.findViewById(R.id.cart_group_invalid_line);//无效的
            holder.groupEffectiveSelected = (ImageView) convertView.findViewById(R.id.cart_group_selected_iv);
            holder.groupInvalidClear = (TextView) convertView.findViewById(R.id.cart_group_invalid_clear);
            holder.lineViewTop = convertView.findViewById(R.id.line_view_top);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        final ShopingCart shoppingCartBean = mDatas.get(groupPosition);
        boolean isInvalid = shoppingCartBean.isInvalidList();
        if (isInvalid) {
            holder.groupEffective.setVisibility(View.GONE);
            holder.groupInvalid.setVisibility(View.VISIBLE);
            holder.vInvalidLine.setVisibility(View.VISIBLE);
        } else {
            Picasso.with(mContext).load(shoppingCartBean.getLogo()).error(R.mipmap.empty_logo).placeholder(R.mipmap.empty_logo).into(holder.groupResLogo);
            holder.groupResName.setText(shoppingCartBean.getName());
            holder.groupEffective.setVisibility(View.VISIBLE);
            holder.groupInvalid.setVisibility(View.GONE);
            holder.vInvalidLine.setVisibility(View.GONE);
            if(isDelete){
                if (shoppingCartBean.isGroupDeleteSelected()) {
                    holder.groupEffectiveSelected.setSelected(true);
                } else {
                    holder.groupEffectiveSelected.setSelected(false);
                }
            }else{
                if (shoppingCartBean.isGroupSelected()) {
                    holder.groupEffectiveSelected.setSelected(true);
                } else {
                    holder.groupEffectiveSelected.setSelected(false);
                }
            }
        }
        holder.groupEffectiveSelected.setOnClickListener(new View.OnClickListener() {//grou effectivep selected
            @Override
            public void onClick(View view) {
                boolean isSelected;
                if (holder.groupEffectiveSelected.isSelected()) {
                    isSelected = false;
                } else {
                    isSelected = true;
                }
                if (listener != null) {
                    listener.onGroupSelectedClickListener(groupPosition, shoppingCartBean, isSelected, getEffectiveGroupCount());
                }
            }
        });
        holder.groupInvalidClear.setOnClickListener(new View.OnClickListener() {//group invalid clear

            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onGroupClearClickListener(groupPosition, shoppingCartBean);
                }
            }
        });
        return convertView;
    }

    //子视图 child  ChildView
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_cart_expandable_child, parent, false);
            holder.lineViewBottom2 = convertView.findViewById(R.id.line_view_bottom2);
            holder.lineViewBottom3 = convertView.findViewById(R.id.line_view_bottom3);
            holder.childEffectiveIvSelected = (ImageView) convertView.findViewById(R.id.child_effective_iv);
            holder.childInvalidDesc = (TextView) convertView.findViewById(R.id.child_invalid_desc);
            holder.childInvalidNone = (TextView) convertView.findViewById(R.id.child_invalid_data_none);
            holder.childEffectivePrice = (LinearLayout) convertView.findViewById(R.id.child_effective_linear_price);
            holder.childEffectiveReturn = (LinearLayout) convertView.findViewById(R.id.child_effective_return_moeny);
            holder.childEffectiveSubtraction = (ImageButton) convertView.findViewById(R.id.child_operator_subtraction);
            holder.childEffectiveAddition = (ImageButton) convertView.findViewById(R.id.child_operator_addition);
            holder.childDrugImage = (ImageView) convertView.findViewById(R.id.item_drug_image);
            holder.childDrugName = (TextView) convertView.findViewById(R.id.item_drug_name);
            holder.childDrugType = (TextView) convertView.findViewById(R.id.item_drug_type);
            holder.childDrugPriceUnit = (TextView) convertView.findViewById(R.id.item_drug_price_unit);
            holder.childDrugPriceRetail = (TextView) convertView.findViewById(R.id.item_drug_price_retail);
            holder.childDrugCount = (TextView) convertView.findViewById(R.id.child_drug_count);
            holder.childDrugCountLinear = (LinearLayout) convertView.findViewById(R.id.child_drug_count_linear);
            holder.childEffectiveFanxian = (TextView) convertView.findViewById(R.id.child_effective_fanxian);
            holder.childExpandableDrug = (LinearLayout) convertView.findViewById(R.id.child_expandable_drug);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }

        if(groupPosition==mDatas.size()-1&&childPosition == mDatas.get(groupPosition).getGoodsList().size() - 1&&mDatas.get(groupPosition).getGoodsList().size()>3){
            holder.lineViewBottom2.setVisibility(View.VISIBLE);
        }else{
            holder.lineViewBottom2.setVisibility(View.GONE);
        }
        if(childPosition==mDatas.get(groupPosition).getGoodsList().size()-1){
            holder.lineViewBottom3.setVisibility(View.VISIBLE);
        }else{
            holder.lineViewBottom3.setVisibility(View.GONE);
        }
        holder.childDrugCountLinear.setVisibility(View.GONE);
        final ShopingCart shoppingCartBean = mDatas.get(groupPosition);
        boolean isInvalid = shoppingCartBean.isInvalidList();
        final GoodsBean good = shoppingCartBean.getGoodsList().get(childPosition);
        int type = Integer.parseInt(good.getType());
        final int drugCount = good.getGoodsCount();
        if (drugCount == 1) {
            holder.childEffectiveSubtraction.setImageResource(R.mipmap.icon_operator_subtraction_black_nor);
        } else {
            holder.childEffectiveSubtraction.setImageResource(R.mipmap.icon_operator_subtraction_black_pre);
        }
        String typeDrug = "";
        String textName="";
        if (type == 1) {
            typeDrug = "【非处方】";
            textName="                 "+ good.getGoodsName() + " " + good.getName();
        }else if(type == 2){
            typeDrug = "【处方】";
            textName="             "+ good.getGoodsName() + " " + good.getName();
        }
        holder.childDrugType.setText(typeDrug);
        holder.childDrugName.setText(getSpannableText(textName, good.getSpecifications()));
        holder.childDrugPriceRetail.setText(good.getRetailPrice());
        holder.childDrugPriceRetail.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.childDrugCount.setText("" + drugCount);
        holder.childDrugPriceUnit.setText(""+good.getDeKaiPrice());
        holder.childDrugPriceRetail.setText("" + good.getRetailPrice());
        holder.childEffectiveFanxian.setText("￥" + good.getReturnMoney());
        Picasso.with(mContext).load(good.getImage()).error(R.mipmap.image_empty_order).placeholder(R.mipmap.image_empty_order).into(holder.childDrugImage);
        final int mGroupCount = getEffectiveGroupCount();
        if (!isInvalid) {
            holder.childDrugName.setTextColor(Color.parseColor("#000000"));
            holder.childInvalidDesc.setVisibility(View.GONE);
            holder.childInvalidNone.setVisibility(View.GONE);
            holder.childEffectiveIvSelected.setVisibility(View.VISIBLE);
            holder.childEffectivePrice.setVisibility(View.VISIBLE);
            holder.childEffectiveReturn.setVisibility(View.VISIBLE);
            holder.childDrugCountLinear.setVisibility(View.VISIBLE);
            holder.childEffectiveIvSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isSelected;
                    if (holder.childEffectiveIvSelected.isSelected()) {
                        isSelected = false;
                    } else {
                        isSelected = true;
                    }
                    if (listener != null) {
                        listener.onChildSelectedClickListener(groupPosition, childPosition, isSelected, mGroupCount, shoppingCartBean.getGoodsList().size());
                    }
                }
            });
            holder.childEffectiveSubtraction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(drugCount<1){
                        return;
                    }
                    if (listener != null) {
                        listener.onChildSubtractionClickListener(groupPosition, childPosition, shoppingCartBean.getGoodsList().size(), drugCount, mGroupCount, good.getDKID());
                    }
                }
            });
            holder.childEffectiveAddition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onChildAdditionClickListener(groupPosition, childPosition, shoppingCartBean.getGoodsList().size(), drugCount, mGroupCount, good.getDKID());
                    }
                }
            });
            holder.childDrugCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onChildDrugCountClickListener(groupPosition, childPosition, shoppingCartBean.getGoodsList().size(), drugCount, mGroupCount, good.getDKID());
                    }
                }
            });
            holder.childExpandableDrug.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null&&!isDelete) {
                        listener.onChildItemClickListener(good.getDKID());
                    }
                }
            });

            if(isDelete){
                holder.childDrugCountLinear.setVisibility(View.GONE);
                if (good.isChildDeleteSelected()) {
                    holder.childEffectiveIvSelected.setSelected(true);
                } else {
                    holder.childEffectiveIvSelected.setSelected(false);
                }
            }else{
                if (good.isChildSelected()) {
                    holder.childEffectiveIvSelected.setSelected(true);
                } else {
                    holder.childEffectiveIvSelected.setSelected(false);
                }
                holder.childDrugCountLinear.setVisibility(View.VISIBLE);
            }
            holder.childDrugType.setTextColor(Color.parseColor("#000000"));
        } else {
            final String message = good.getMessage();
            holder.childInvalidNone.setText("" + message);
            holder.childInvalidDesc.setVisibility(View.VISIBLE);
            holder.childInvalidNone.setVisibility(View.VISIBLE);
            holder.childEffectiveIvSelected.setVisibility(View.GONE);
            holder.childEffectivePrice.setVisibility(View.GONE);
            holder.childEffectiveReturn.setVisibility(View.GONE);
            holder.childDrugName.setTextColor(Color.parseColor("#60000000"));
            holder.childInvalidDesc.setTextColor(Color.parseColor("#80000000"));
            holder.childInvalidNone.setTextColor(Color.parseColor("#60000000"));
            holder.childDrugType.setTextColor(Color.parseColor("#60000000"));
            holder.childDrugCountLinear.setVisibility(View.GONE);
            holder.childExpandableDrug.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null&&!isDelete) {
                        listener.onChildItemClickInvalidListener(message);
                    }
                }
            });
        }
        if(groupPosition==mDatas.size()-1&&childPosition == mDatas.get(groupPosition).getGoodsList().size() - 1){
            holder.lineViewBottom2.setVisibility(View.VISIBLE);
        }else{
            holder.lineViewBottom2.setVisibility(View.GONE);
        }
        holder.childExpandableDrug.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(listener!=null){
                    listener.onChildItemLongClickListener(good.getDKID(),groupPosition,childPosition);
                }
                return true;
            }
        });
        return convertView;
    }


    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    /**
     * gorup 的 viewHolder
     */
    private static class GroupViewHolder {
        LinearLayout groupEffective, groupInvalid;
        ImageView groupEffectiveSelected,groupResLogo;
        TextView groupInvalidClear,groupResName;
        View lineViewTop;
        View vInvalidLine;
    }

    /**
     * child 的 viewHolder
     */
    private static class ChildViewHolder {
        View lineViewBottom2,lineViewBottom3;
        ImageView childEffectiveIvSelected, childDrugImage;
        TextView childInvalidDesc, childInvalidNone, childDrugName,childDrugType,
                childDrugPriceUnit, childDrugPriceRetail, childEffectiveFanxian;
        TextView childDrugCount;
        LinearLayout childEffectivePrice, childEffectiveReturn;
        ImageButton childEffectiveSubtraction, childEffectiveAddition;
        LinearLayout childDrugCountLinear;
        LinearLayout childExpandableDrug;
    }

    /**
     * item子控件的点击事件接口
     */
    public interface OnCartItemClickListener {
        void onGroupSelectedClickListener(int groupPosition, ShopingCart gropBean, boolean isSelected, int groupCount);//group selected

        void onGroupClearClickListener(int groupPosition, ShopingCart cartBean);//group clear

        void onChildSelectedClickListener(int groupPosition, int childPosition, boolean isSelected, int groupCount, int childCount);//child selected

        void onChildSubtractionClickListener(int groupPosition, int childPosition, int mGoodsCount, int drugCount, int mGroupCount, String dkId);//减

        void onChildAdditionClickListener(int groupPosition, int childPosition, int mGoodsCount, int drugCount, int mGroupCount, String dkId);//加

        void onChildDrugCountClickListener(int groupPosition, int childPosition, int mGoodsCount, int drugCount, int mGroupCount, String dkId);//数量手动

        void onChildItemClickListener(String dkId);
        void onChildItemLongClickListener(String dkId,int gp,int cp);

        void onChildItemClickInvalidListener(String message);
    }

    /**
     * 设置bu内部字体颜色
     *
     * @param text
     * @param speci
     * @return
     */
    private SpannableStringBuilder getSpannableText(String text, String speci) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(speci);
        AbsoluteSizeSpan sizeSpan1 = new AbsoluteSizeSpan(DensityUtils.dp2px(12));
        spannableStringBuilder2.setSpan(new ForegroundColorSpan(Color.parseColor("#70000000")), 0, speci.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringBuilder2.setSpan(sizeSpan1, 0, speci.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringBuilder.append(spannableStringBuilder2);
        return spannableStringBuilder;
    }
}
