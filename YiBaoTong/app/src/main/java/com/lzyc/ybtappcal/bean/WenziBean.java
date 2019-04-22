package com.lzyc.ybtappcal.bean;


import com.lzyc.ybtappcal.util.PinYinUtil;

public class WenziBean implements Comparable<WenziBean> {
	private String pinyin;
	public String name;
	public String provinceName;
	public String provinceId;

	public PinYinStyle pinYinStyle=new PinYinStyle();
	//使用成员变量生成构造方法：alt+shift+s->o


	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public PinYinStyle getPinYinStyle() {
		return pinYinStyle;
	}

	public void setPinYinStyle(PinYinStyle pinYinStyle) {
		this.pinYinStyle = pinYinStyle;
	}

	public WenziBean(String name) {
		super();
		this.name = name;
		//一开始就转化好拼音
		setPinyin(PinYinUtil.getPinyin(name));
	}

	public WenziBean(String name,String pingying ) {
		super();
		this.name = name;
		this.provinceName = provinceName;
		//一开始就转化好拼音
		setPinyin(pingying.substring(0,1));
	}

	@Override
	public int compareTo(WenziBean another) {
		return getPinyin().compareTo(another.getPinyin());
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String p) {
		p=p.substring(0,1);
		this.pinyin = p;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "WenziBean{" +
				"pinyin='" + pinyin + '\'' +
				", name='" + name + '\'' +
				", pinYinStyle=" + pinYinStyle +
				'}';
	}
}
