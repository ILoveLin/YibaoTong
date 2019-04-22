package com.lzyc.ybtappcal.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * 解决乱码问题
 * Created by yang on 2017/03/26.
 */
public class MineStringRequest extends StringRequest {
    private static final  String TAG=MineStringRequest.class.getSimpleName();
    private String url;

//    public MineStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
//        super(method, url, listener, errorListener);
//        this.url=url;
//    }

    public MineStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        this.url=url;
    }


    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String str=new String(response.data,"utf-8");
            if(StringUtils.isGarbledCode(str)){
                LogUtil.e(TAG,"出现乱码"+url);
                str = new String(response.data,"gbk");
            }else{
                LogUtil.e(TAG,"没有乱码"+url);
            }
            return Response.success(str,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }


}
