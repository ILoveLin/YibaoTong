package com.lzyc.ybtappcal.volley;

import org.json.JSONObject;

/**
 * 作者：xujm
 * 时间：2015/12/25
 * 备注：接口回调
 */
public interface ServiceResponseCallback {

     void done(String msg, int what, JSONObject response);

     void error(String errorMsg);

}
