package com.lzyc.ybtappcal.bean;

import java.util.ArrayList;

public class ShopingCart extends BaseBean2 implements Cloneable{
	private boolean isGroupSelected;//结算状态组是否被选中
	private boolean isGroupDeleteSelected;//删除状态组是否被选中
	private boolean isInvalidList=false;//是否有效
	private ArrayList<GoodsBean> GoodsList;
	private String Name;
	private String ID;
	private String Freight;
	private String Logo;
	private String FreeShipp;

	public boolean isInvalidList() {
		return isInvalidList;
	}
	public void setInvalidList(boolean invalidList) {
		isInvalidList = invalidList;
	}

	public boolean isGroupSelected()
	{
		return isGroupSelected;
	}

	public void setIsGroupSelected(boolean isGroupSelected)
	{
		this.isGroupSelected = isGroupSelected;
	}

	public ArrayList<GoodsBean> getGoodsList() {
		return GoodsList;
	}

	public void setGoodsList(ArrayList<GoodsBean> goodsList)
	{
		this.GoodsList = goodsList;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getFreight() {
		return Freight;
	}

	public void setFreight(String freight) {
		Freight = freight;
	}

	public String getLogo() {
		return Logo;
	}

	public void setLogo(String logo) {
		Logo = logo;
	}

	public String getFreeShipp() {
		return FreeShipp;
	}

	public void setFreeShipp(String freeShipp) {
		FreeShipp = freeShipp;
	}

	@Override
	public ShopingCart clone()  {
		try {
			return (ShopingCart) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setGroupSelected(boolean groupSelected) {
		isGroupSelected = groupSelected;
	}

	public boolean isGroupDeleteSelected() {
		return isGroupDeleteSelected;
	}

	public void setGroupDeleteSelected(boolean groupDeleteSelected) {
		isGroupDeleteSelected = groupDeleteSelected;
	}
}
