//package com.lzyc.ybtappcal.activity.mine;
//
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//
//import com.google.gson.reflect.TypeToken;
//import com.lzyc.ybtappcal.R;
//import com.lzyc.ybtappcal.activity.account.LoginActivity;
//import com.lzyc.ybtappcal.activity.base.BaseActivity;
//import com.lzyc.ybtappcal.adapter.UsedAdapter;
//import com.lzyc.ybtappcal.bean.UsedPositionBean;
//import com.lzyc.ybtappcal.constant.HttpConstant;
//import com.lzyc.ybtappcal.listener.OnAddressClickListener;
//import com.lzyc.ybtappcal.util.JsonUtil;
//import com.lzyc.ybtappcal.util.MD5ChangeUtil;
//import com.lzyc.ybtappcal.util.SharePreferenceUtil;
//import com.lzyc.ybtappcal.util.ToastUtil;
//
//import org.json.JSONObject;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import butterknife.BindView;
//
//
///**
// * 作者：xujm
// * 时间：2016/2/22
// * 备注：常用地址
// */
//public class UsedPositionActivity extends BaseActivity implements OnAddressClickListener {
//
//    @BindView(R.id.lv_used)
//    ListView lv_used;
//    @BindView(R.id.ll_used)
//    ListView ll_used;
//
//    @BindView(R.id.rl_add_emptyview)
//    RelativeLayout rl_add_emptyview;
//
//    private String phone;
//    private ArrayList<UsedPositionBean> mList;
//    private UsedAdapter adapter;
//
//    @Override
//    public int getContentViewId() {
//        return R.layout.activity_used;
//    }
//
//    @Override
//    public void init() {
//        setTitleName("预设地址");
//        setTitleRightBtnResources(R.mipmap.icon_titlebar_right_add);
//        Boolean isLogin = (Boolean) SharePreferenceUtil.get(this, SharePreferenceUtil.IS_LOGIN, false);
//        if (isLogin) {
//            phone = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PHONE, "");
//        } else {
//            openActivity(LoginActivity.class);
//        }
//        initLv();
//
//    }
//
//    @Override
//    public void onClickTitleRightBtn(View v) {
//        super.onClickTitleRightBtn(v);
////        openActivity(SelectPositionActivity.class);
//    }
//
//    private void initLv() {
//        mList = new ArrayList<UsedPositionBean>();
//        adapter = new UsedAdapter(this, mList, R.layout.item_used);
//        adapter.setListener(this);
//        lv_used.setAdapter(adapter);
//        lv_used.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                finish();
//            }
//        });
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        requestAddress();
//    }
//
//    /**
//     * 点击删除回调
//     *
//     * @param position
//     */
//    @Override
//    public void onAddressClick(int position) {
//        requestDeleteAddress(mList.get(position).getId());
//    }
//
//    /**
//     * 常用地址列表
//     */
//    public void requestAddress() {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("app", "GroupAddressBean");
//        params.put("class", "Index");
//        String sign = MD5ChangeUtil.Md5_32("AddressIndex" + HttpConstant.APP_SECRET);
//        params.put("sign", sign);
//        params.put("phone", phone);
//        request(params, HttpConstant.MY_ADDRESS_LIST);
//    }
//
//
//    /**
//     * 删除地址
//     */
//    public void requestDeleteAddress(String id) {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("app", "GroupAddressBean");
//        params.put("class", "Del");
//        String sign = MD5ChangeUtil.Md5_32("AddressDel" + HttpConstant.APP_SECRET);
//        params.put("sign", sign);
//        params.put("id", id);
//        request(params, HttpConstant.MY_ADDRESS_DELETE);
//    }
//
//
//    @Override
//    public void done(String msg, int what, JSONObject response) {
//        super.done(msg, what, response);
//        switch (what) {
//            case HttpConstant.MY_ADDRESS_LIST:
//                Type type = new TypeToken<ArrayList<UsedPositionBean>>() {
//                }.getType();
//                List<UsedPositionBean> list = JsonUtil.getListModle(response.toString(), "data", type);
//                reload(list);
//                break;
//            case HttpConstant.MY_ADDRESS_DELETE:
////                showToast("删除成功");
//                ToastUtil.showShort(UsedPositionActivity.this, "删除成功");
//
//                requestAddress();
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void reload(List<UsedPositionBean> list) {
//        if (!mList.isEmpty()) {
//            mList.clear();
//        }
//        mList.addAll(list);
//        if (list.size() == 0) {
//            rl_add_emptyview.setVisibility(View.VISIBLE);
//        } else {
//            rl_add_emptyview.setVisibility(View.INVISIBLE);
//        }
//
//        adapter.notifyDataSetChanged();
//    }
//
//}