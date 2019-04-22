package com.lzyc.ybtappcal.volley.entity;

import java.io.Serializable;

/**
 * Created by yang on 2017/04/30.
 */

public class ErrorResponse implements Serializable{
    private int what;
    private String msg;
    private boolean isNetError;

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isNetError() {
        return isNetError;
    }

    public void setNetError(boolean netError) {
        isNetError = netError;
    }
}
