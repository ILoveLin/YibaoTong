package com.lzyc.ybtappcal.bean;

import java.io.Serializable;

/**
 * Created by yang on 2017/03/24.
 */

public class BaseBean2 implements Serializable{
    private String msg;
    private int status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
