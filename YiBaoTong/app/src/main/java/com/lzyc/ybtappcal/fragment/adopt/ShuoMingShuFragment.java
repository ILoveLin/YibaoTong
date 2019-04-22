package com.lzyc.ybtappcal.fragment.adopt;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
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
 * 说明书
 */
public class ShuoMingShuFragment extends BaseFragment {

    WebSettings settings;

    @BindView(R.id.frag_detail_webview)
    CustomWebView mWebView;
    @BindView(R.id.empty_linear_info)
    LinearLayout mLayout;

//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

//        mWebView = (CustomWebView) rootView.findViewById(R.id.frag_detail_webview);
//
//        mLayout = (LinearLayout) rootView.findViewById(R.id.empty_linear_info);

        Bundle bundle = getArguments();

        String url = bundle.getString(Contants.KEY_STR_URL);

        if (TextUtils.isEmpty(url)) {
            try {
                mWebView.setVisibility(View.GONE);
                mLayout.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return;
        }

        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });

        settings = mWebView.getSettings();
        mWebView.setHorizontalScrollBarEnabled(false);
        settings.setDefaultTextEncodingName("utf-8");
//        mWebView.setBackgroundColor(Color.argb(0, 0, 0, 0));

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
                    mWebView.loadDataWithBaseURL(null, s, "text/html", "utf-8", null);
                } catch (Exception e) {
                    e.printStackTrace();
                    mLayout.setVisibility(View.VISIBLE);
                    mWebView.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });

        requestQueue.add(stringRequest);
    }

    private static final String TAG = ShuoMingShuFragment.class.getSimpleName();

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
