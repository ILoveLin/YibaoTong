package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * 省级城市，一级城市
 * Created by yang on 2017/01/05.
 */
public class CProviceBean extends BaseBean {
    private String province;
    private String province_id;
    private List<CCityBean> citys;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public List<CCityBean> getCitys() {
        return citys;
    }

    public void setCitys(List<CCityBean> citys) {
        this.citys = citys;
    }
}
