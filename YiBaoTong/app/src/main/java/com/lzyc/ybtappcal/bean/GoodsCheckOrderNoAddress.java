package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by Lxx on 2017/3/26.
 */
public class GoodsCheckOrderNoAddress {
    /**
     * Balance : 1000.00
     */

    private OtherBean Other;
    /**
     * defaultAddress : []
     * Other : {"Balance":"1000.00"}
     */

    private List<?> defaultAddress;

    public OtherBean getOther() {
        return Other;
    }

    public void setOther(OtherBean Other) {
        this.Other = Other;
    }

    public List<?> getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(List<?> defaultAddress) {
        this.defaultAddress = defaultAddress;
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
}
