package com.lzyc.ybtappcal.volley;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lzyc.ybtappcal.app.App;
import com.lzyc.ybtappcal.bean.Version;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.rsa.RSAUtil;
import com.lzyc.ybtappcal.volley.entity.BaseResult;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 作者：xujm
 * 时间：2015/12/25
 * 备注：volley请求基类
 */
public class BaseService {
    private static final String TAG=BaseService.class.getName();
    private static final RequestQueue requestQueue = Volley.newRequestQueue(App
            .getInstance().getApplicationContext());

    /**
     * post请求（不带what参数）
     *
     * @param api
     * @param serviceResponse
     * @param map
     * @return void
     */
    public void postMap(String api,
                        final ServiceResponseCallback serviceResponse,
                        final Map<String, String> map) {

        List<ServiceResponseCallback> callbacks = new ArrayList<ServiceResponseCallback>();
        if (serviceResponse != null) {
            callbacks.add(serviceResponse);
        }
        String phone=(String) SharePreferenceUtil.get(App.getInstance(),SharePreferenceUtil.PHONE,"");
        List<String> list=new ArrayList<String>();
        Iterator<String> keys=map.keySet().iterator();
        while (keys.hasNext()){
            String key=keys.next();
            list.add(key);
        }
        if(!list.contains("phone")){
            map.put("phone",""+phone);
        }
        if (!list.contains("uuid")) {
            map.put("uuid",  App.getInstance().mPushAgent.getRegistrationId());
        }
        try {
            postJson(api, callbacks, map, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 返回当前程序版本名
     */
    private Version getAppVersionName(Context context) {
        Version version = new Version();
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            version.setPackageName(pi.packageName);
            version.setVersionCode(String.valueOf(pi.versionCode));
            version.setVersionName(pi.versionName);
        } catch (Exception e) {
            LogUtil.e(TAG,e.getMessage());
        }
        return version;
    }

    /**
     * post请求（带what参数，适用于一个界面多个接口使用）
     *
     * @param api
     * @param serviceResponse
     * @param map
     * @param what
     * @return void
     */
    public void postMap(String api,
                        final ServiceResponseCallback serviceResponse,
                        Map<String, String> map, int what) {
        List<ServiceResponseCallback> callbacks = new ArrayList<ServiceResponseCallback>();
        if (serviceResponse != null) {
            callbacks.add(serviceResponse);
        }
        String phone=(String) SharePreferenceUtil.get(App.getInstance(),SharePreferenceUtil.PHONE,"");
        List<String> list=new ArrayList<String>();
        Iterator<String> keys=map.keySet().iterator();
        while (keys.hasNext()){
            String key=keys.next();
            list.add(key);
        }
        if(!list.contains("phone")){
            map.put("phone",""+phone);
        }
        if (!list.contains("uuid")) {
            map.put("uuid", PushAgent.getInstance(App.getInstance()).getRegistrationId());
        }
        Version version=getAppVersionName(App.getInstance().getApplicationContext());
        if(!list.contains("versioncode")){
            map.put("versioncode", version.getVersionCode());
        }
        if(!list.contains("deviceName")){
            map.put("deviceName",Build.BRAND+Build.MODEL);
        }
        if(!list.contains("os")){
            map.put("os", "android");
        }
        Iterator iterator=map.entrySet().iterator();
        StringBuffer stringBuffer=new StringBuffer();
        while (iterator.hasNext()){
            Entry entry= (Entry) iterator.next();
            String key= (String) entry.getKey();
            String value= (String) entry.getValue();
            stringBuffer.append(key+"="+value+"&");
        }
        String param=stringBuffer.toString();
        param=param.substring(0,param.length()-1);
        try {
            String afterencrypt = RSAUtil.encryptData(param);
            map=new HashMap<>();
            map.put("key",afterencrypt);
            map.put("ver","2");
            postJson(api, callbacks, map, what);
        } catch (Exception e) {
            LogUtil.y("###########postMap###catch");
            cancel();
            e.printStackTrace();
        }
    }

    /**
     * get请求（不带what参数）
     *
     * @param api
     * @param serviceResponse
     * @param map
     * @return void
     */
    public void getMap(String api,
                       final ServiceResponseCallback serviceResponse,
                       final Map<String, String> map) {
        try {
            getJson(api, serviceResponse, map, 0);
        } catch (Exception e) {
            e.printStackTrace();
            cancel();
        }
    }


    /**
     * get请求（带what参数，适用于一个界面多个接口使用）
     *
     * @param api
     * @param serviceResponse
     * @param map
     * @param what
     * @return void
     */
    public void getMap(String api,
                       final ServiceResponseCallback serviceResponse,
                       final Map<String, String> map, int what) {
        try {
            getJson(api, serviceResponse, map, what);
        } catch (Exception e) {
            e.printStackTrace();
            cancel();
        }
    }

    /**
     * post发送文件
     *
     * @param api
     * @param serviceResponse
     * @param fileName
     * @param file
     * @param map
     * @param what
     * @return void
     */
    public void postFile(String api,
                         final ServiceResponseCallback serviceResponse, String fileName,
                         File file, final Map<String, Object> map, int what) {
        List<File> listFile = new ArrayList<File>();
        if (file != null) {
            listFile.add(file);
        }
        postFileJson(api, serviceResponse, fileName, listFile, map, what);
    }

    /**
     * post上传多个文件
     *
     * @param api
     * @param serviceResponse
     * @param fileName
     * @param file
     * @param map
     * @param what
     * @return void
     */
    public void postFiles(String api,
                          final ServiceResponseCallback serviceResponse, String fileName,
                          List<File> file, final Map<String, Object> map, int what) {
        postFileJson(api, serviceResponse, fileName, file, map, what);
    }

    /**
     * json post
     *
     * @param api
     * @param serviceResponses
     * @param map
     * @param what
     * @return void
     */
    public void postJson(String api,
                         final List<ServiceResponseCallback> serviceResponses,
                         final Map<String, String> map, final int what) throws Exception{
        PostRequest postRequest = new PostRequest(api,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        LogUtil.y("#onResponse#");
                        // 加载成功处理
                        BaseResult baseResult = new Gson().fromJson(
                                response.toString(), BaseResult.class);
                        if (baseResult.status == BaseResult.STATUS_SUCCESS) {
                            for (ServiceResponseCallback serviceResponseCallback : serviceResponses) {
                                if (serviceResponseCallback != null) {
                                    serviceResponseCallback.done(baseResult.msg, what,
                                            response);
                                }
                            }
                        } else {

                            for (ServiceResponseCallback serviceResponseCallback : serviceResponses) {
                                if (serviceResponseCallback != null&&what>1000&&what<10000) { //做一层保护，后期网络接口优化
                                    ErrorResponse errorResponse=new ErrorResponse();
                                    LogUtil.y(""+baseResult.msg);
                                    String msg=baseResult.msg;
                                    if(msg.length()>50){
                                        errorResponse.setNetError(true);
                                    }
                                    if(msg.contains("org.")){
                                        msg=" ";
                                    }
                                    errorResponse.setMsg(msg);
                                    errorResponse.setWhat(what);
                                    String errorJson=new Gson().toJson(errorResponse);
                                    serviceResponseCallback.error(errorJson);
                                }else{
                                    serviceResponseCallback.error(baseResult.msg);
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.y("#onErrorResponse#");
                String errorMsg;
                for (ServiceResponseCallback serviceResponseCallback : serviceResponses) {
                    if (serviceResponseCallback != null&&what>1000&&what<10000) { //做一层保护，后期网络接口优化
                        ErrorResponse errorResponse=new ErrorResponse();
                        String msg="";
                        errorResponse.setNetError(true);
                        errorResponse.setMsg(msg);
                        errorResponse.setWhat(what);
                        errorMsg=new Gson().toJson(errorResponse);
                    }else{
                        errorMsg=error.getMessage();
                    }
                    serviceResponseCallback.error(errorMsg);
                }
            }
        }, map);

        //设置请求超时为10000ms，重连次数，
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    /**
     * get请求
     *
     * @param api
     * @param serviceResponse
     * @param map
     * @param what
     * @return void
     */
    private void getJson(String api,
                         final ServiceResponseCallback serviceResponse,
                         final Map<String, String> map, final int what) throws Exception{
        String url = getString(api, map);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                BaseResult baseResult = new Gson().fromJson(
                        response.toString(), BaseResult.class);
                if (baseResult.status == BaseResult.STATUS_SUCCESS) {
                    serviceResponse.done(baseResult.msg, what, response);
                } else {
                    serviceResponse.error(baseResult.msg);
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse=new ErrorResponse();
                String msg=error.getMessage();
                if(msg.length()>50){
                  errorResponse.setNetError(true);
                }
                errorResponse.setMsg(msg);
                errorResponse.setWhat(what);
                String errorJson=new Gson().toJson(errorResponse);
                serviceResponse.error(errorJson);
            }

        });
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * 发送文件（图片）
     *
     * @param api
     * @param serviceResponse
     * @param fileName
     * @param file
     * @param map
     * @param what
     * @return void
     */
    private void postFileJson(String api,
                              final ServiceResponseCallback serviceResponse, String fileName,
                              List<File> file, final Map<String, Object> map, final int what) {
        MultipartRequest request = new MultipartRequest(api,
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LogUtil.y(error.toString());
                        ErrorResponse errorResponse=new ErrorResponse();
                        String msg=error.getMessage();
                        if(msg.length()>50){
                          errorResponse.setNetError(true);
                        }
                        errorResponse.setMsg(msg);
                        errorResponse.setWhat(what);
                        String errorJson=new Gson().toJson(errorResponse);
                        serviceResponse.error(errorJson);
                    }
                }, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    BaseResult baseResult = new Gson().fromJson(
                            response.toString(), BaseResult.class);
                    if (baseResult.status == BaseResult.STATUS_SUCCESS) {
                        serviceResponse.done(baseResult.msg, what,
                                jsonObject);
                    } else {
                        serviceResponse.error(baseResult.msg);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }, fileName, file, map);
        requestQueue.add(request);
    }

    /**
     * map和url拼接成Get网络请求地址
     *
     * @param sourceUrl
     * @param params
     * @return
     */
    public static String getString(String sourceUrl, Map<String, String> params) {
        String urlStr = new String(sourceUrl);
        if (params != null) {
            Iterator<Entry<String, String>> it = params.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, String> entry = it.next();
                try {
                    urlStr += entry.getKey() + "="
                            + URLEncoder.encode(entry.getValue(), "utf-8")
                            + "&";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            params = null;
        }
        return urlStr;
    }

    public void cancel(){
        requestQueue.cancelAll(this);
    }

}
