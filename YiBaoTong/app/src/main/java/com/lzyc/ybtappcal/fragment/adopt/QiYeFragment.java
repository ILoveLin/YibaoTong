package com.lzyc.ybtappcal.fragment.adopt;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseFragment;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.view.CustomWebView;
import com.lzyc.ybtappcal.volley.MineStringRequest;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;


/**
 * 企业
 */
public class QiYeFragment extends BaseFragment {
    private static final String TAG = QiYeFragment.class.getSimpleName();
    @BindView(R.id.frag_detail_webview)
    CustomWebView mWebView;
    @BindView(R.id.empty_linear_qiye)
    LinearLayout mLayout;
    WebSettings settings;
    String phone;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        if (null != rootView) {
//            ViewGroup parent = (ViewGroup) rootView.getParent();
//            if (null != parent) {
//                parent.removeView(rootView);
//            }
//        } else {
//            rootView = inflater.inflate(R.layout.fragment_detail_webview, null);
//            initView(rootView);
//        }
//
//        return rootView;
//    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_detail_webview;
    }

    @Override
    protected void init() {
        initView();
    }

    private void initView() {

        Bundle bundle = getArguments();
        String url = bundle.getString(Contants.KEY_STR_URL);

        if (TextUtils.isEmpty(url)) {
            try {
                mLayout.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                WebView.HitTestResult result = ((WebView) view).getHitTestResult();
                if (null == result) return true;

                int type = result.getType();

                if (WebView.HitTestResult.PHONE_TYPE == type) {

                    Uri uri = Uri.parse("tel:" + phone);
                    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(intent);

                    return false;
                }

                return true;
            }
        });


        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                String tag = "tada:tel";

                if (url.contains(tag)) {
                    String mobile = url.substring(url.lastIndexOf("/") + 1);
                    Uri uri = Uri.parse("tel:" + mobile);
                    Intent intent = new Intent(Intent.ACTION_CALL, uri);
                    startActivity(intent);
                    return true;
                }


                return super.shouldOverrideUrlLoading(view, url);
            }

        });

//        mWebView.setScrollContainer(false);
//        mWebView.setVerticalScrollBarEnabled(false);
//        mWebView.setHorizontalScrollBarEnabled(false);
        settings = mWebView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        MineStringRequest stringRequest = new MineStringRequest(url, new Response.Listener<String>() {


            @Override
            public void onResponse(String s) {
                try {

                    if (s.indexOf("<body") != -1) {
                        s = s.replace("<body", "<body style=\"color:#a1a1a8\";font-size:14px\"");
                    }
                    if (s.indexOf("color") != -1) {
                        s = s.replace("color=\"#727171\"", "color=\"#a1a1a8\"");
                    }

                    getPhoneNum(s);

                    mWebView.loadDataWithBaseURL(null, s, "text/html", "utf-8", null);
                } catch (Exception e) {
                    e.printStackTrace();
                    mWebView.setVisibility(View.GONE);
                    mLayout.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        requestQueue.add(stringRequest);
    }

    private void getPhoneNum(String str) {
        try {
            int start = str.indexOf("<a href=\"tel:") + 13;

            String strs = str.substring(start, start + 20);

            phone = strs.split("\">")[0];

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int tabValue;

    @Override
    public void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

}
