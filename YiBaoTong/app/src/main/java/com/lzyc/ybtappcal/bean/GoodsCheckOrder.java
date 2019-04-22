package com.lzyc.ybtappcal.bean;

/**
 * Created by Lxx on 2017/3/20.
 */
public class GoodsCheckOrder {

    /**
     * ID : 139
     * UID : 1535
     * Name : 贺伟
     * Phone : 13718691968
     * AddressRegion : 北京 北京市 大兴区
     * AddressDetail : 荣华南路荣华国际5号楼1607
     * Default : 1
     * CreateTime : 2017-03-23 17:55:19
     */

    private DefaultAddressBean defaultAddress;
    /**
     * Balance : 10.00
     */

    private OtherBean Other;

    private Other oth;

    public DefaultAddressBean getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(DefaultAddressBean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public OtherBean getOther() {
        return Other;
    }

    public void setOther(OtherBean Other) {
        this.Other = Other;
    }

    public Other getOth(){
        return oth;
    }

    public void setOth(Other oth){
        this.oth = oth;
    }

    public static class DefaultAddressBean {
        private String ID;
        private String UID;
        private String Name;
        private String Phone;
        private String AddressRegion;
        private String AddressDetail;
        private String Default;
        private String CreateTime;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getUID() {
            return UID;
        }

        public void setUID(String UID) {
            this.UID = UID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String Phone) {
            this.Phone = Phone;
        }

        public String getAddressRegion() {
            return AddressRegion;
        }

        public void setAddressRegion(String AddressRegion) {
            this.AddressRegion = AddressRegion;
        }

        public String getAddressDetail() {
            return AddressDetail;
        }

        public void setAddressDetail(String AddressDetail) {
            this.AddressDetail = AddressDetail;
        }

        public String getDefault() {
            return Default;
        }

        public void setDefault(String Default) {
            this.Default = Default;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }
    }

    public static class OtherBean {
        private String Balance;

        public String getBalance() {
            return Balance;
        }

        public void setBalance(String Balance) {
            this.Balance = Balance;
        }
    }

    public static class Other{
        private String Balance;

        public String getBalance() {
            return Balance;
        }

        public void setBalance(String Balance) {
            this.Balance = Balance;
        }
    }
}
