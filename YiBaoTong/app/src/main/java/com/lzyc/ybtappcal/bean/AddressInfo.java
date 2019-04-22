package com.lzyc.ybtappcal.bean;

/**
 * Created by yang on 2017/03/20.
 */

public class AddressInfo extends BaseBean {

    private String Default;
    private String Name;
    private String AddressDetail;
    private String Phone;
    private String UID;
    private String CreateTime;
    private String ID;
    private String AddressRegion;

    public String getDefault() {
        return Default;
    }

    public void setDefault(String aDefault) {
        Default = aDefault;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddressDetail() {
        return AddressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        AddressDetail = addressDetail;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAddressRegion() {
        return AddressRegion;
    }

    public void setAddressRegion(String addressRegion) {
        AddressRegion = addressRegion;
    }

    @Override
    public String toString() {
        return "AddressInfo{" +
                "Default='" + Default + '\'' +
                ", Name='" + Name + '\'' +
                ", AddressDetail='" + AddressDetail + '\'' +
                ", Phone='" + Phone + '\'' +
                ", UID='" + UID + '\'' +
                ", CreateTime='" + CreateTime + '\'' +
                ", ID='" + ID + '\'' +
                ", AddressRegion='" + AddressRegion + '\'' +
                '}';
    }
}
