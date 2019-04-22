package com.lzyc.ybtappcal.activity.mine;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;

import com.bm.library.PhotoView;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.FeedBackAdapter;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.BitmapUtil;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.KeyBoardUtils;
import com.lzyc.ybtappcal.util.MD5ChangeUtil;
import com.lzyc.ybtappcal.util.ScreenUtils;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.lzyc.ybtappcal.widget.popupwindow.BasePopupWindow;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

import static android.provider.DocumentsContract.getDocumentId;
import static android.provider.DocumentsContract.isDocumentUri;

/**
 * 意见反馈
 * @author yang
 */
public class FeedBackActivity extends BaseActivity {
    private static  final String TAG=FeedBackActivity.class.getSimpleName();

    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.et_contact_info)
    EditText etContactInfo;
    @BindView(R.id.gvImages)
    GridView gvImages;
    @BindString(R.string.title_feedback)
    String titleName;
    private List<String> listPath;
    private FeedBackAdapter mAdapter;
    private static final int IMAGE_REQUEST_CODE = 0;

    @Override
    public int getContentViewId() {
        return R.layout.activity_feed_back;
    }

    @Override
    public void init() {
        setTitleName(titleName);
        listPath = new ArrayList<>();
        KeyBoardUtils.openKeybord(etContent, this);
        gvImages.setSelector(new ColorDrawable(Color.TRANSPARENT));
        listPath = new ArrayList<>();
        mAdapter = new FeedBackAdapter(FeedBackActivity.this, listPath);
        mAdapter.setOnFbItemChildListener(new FeedBackAdapter.OnFbItemChildListener() {
            @Override
            public void onImageContentClick(int position, String path) {
                if (position < mAdapter.getCount() - 1) {
                        Bitmap bitmap = BitmapUtil.getimage(path);
                        popImagePreView(bitmap);
                }
            }

            @Override
            public void onImageDeleteClick(int position, String path) {
                if (position < mAdapter.getCount() - 1) {
                    mAdapter.deleteItem(path);
                    mAdapter.deleteUrl(position);
                }
            }

            @Override
            public void onImageContentAddClick(int position) {
                if (position == mAdapter.getCount() - 1 && position < 3) {
                    try {
                        toAlbum();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    showToast("限制3张");
                }
            }
        });
        gvImages.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

    /**
     * 图片预览
     *
     * @param bitmap
     */
    private void popImagePreView(Bitmap bitmap) {
        KeyBoardUtils.closeKeybord(etContent, this);
        int w = ScreenUtils.getScreenWidth() - DensityUtils.dp2px(30);
        final BasePopupWindow popImagePreview = new BasePopupWindow(FeedBackActivity.this, R.layout.popup_image_preview, w, ViewGroup.LayoutParams.MATCH_PARENT);
        popImagePreview.setBackgroundDrawable(new BitmapDrawable());
        View conentView = popImagePreview.getConentView();
        PhotoView ivPreview = (PhotoView) conentView.findViewById(R.id.pop_preview);
        ivPreview.enable();
        ivPreview.setImageBitmap(bitmap);
        popImagePreview.showPopupWindow(gvImages, Gravity.CENTER);
        conentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popImagePreview.dismiss();
            }
        });
    }

    /**
     * 从相册获取
     */
    protected void toAlbum() throws Exception{
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    @OnClick(R.id.bt_feedback)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_feedback:
                requestEventCode("r006");
                String content = etContent.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    showEditTextPrompt(etContent, "反馈内容不能为空！");
                    etContent.setSelection(content.length());
                    return;
                }
                String newContent = content.replace("\n", "");
                requestFankui(newContent);
                break;
        }
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.MY_FEEDBACK:
                ToastUtil.showToastCenter(FeedBackActivity.this, "反馈成功！");
                finish();
                break;
            case 0:
                try {
                    JSONObject jo = new JSONObject(response.toString());
                    String data = jo.get("data").toString();
                    JSONObject dataJs = new JSONObject(data.toString());
                    String url = dataJs.get("url").toString();
                    mAdapter.addUrl(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (requestCode == IMAGE_REQUEST_CODE) {
                String filePath = "$$";
                Uri contentUri = data.getData();
                int sdkInt = Build.VERSION.SDK_INT; //兼容4.4以下的
                if (sdkInt >= 19 && isDocumentUri(FeedBackActivity.this, contentUri)) {
                    String wholeID = getDocumentId(contentUri);
                    String id = wholeID.split(":")[1];
                    String[] column = {MediaStore.Images.Media.DATA};
                    String sel = MediaStore.Images.Media._ID + "=?";
                    Cursor cursor = FeedBackActivity.this.getContentResolver().query(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);
                    int columnIndex = cursor.getColumnIndex(column[0]);
                    if (cursor.moveToFirst()) {
                        filePath = cursor.getString(columnIndex);
                    }
                    cursor.close();
                } else {
                    if (!TextUtils.isEmpty(contentUri.getAuthority())) {
                        Cursor cursor = getContentResolver().query(contentUri,
                                new String[]{MediaStore.Images.Media.DATA},
                                null, null, null);
                        if (null == cursor) {
                            showToast("图片没找到");
                        }
                        cursor.moveToFirst();
                        filePath = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Images.Media.DATA));
                        cursor.close();
                    } else {
                        filePath = data.getData().getPath();
                    }
                }
                mAdapter.addItem(filePath);
                requestImage(filePath);
            }
        }
    }
    //网络请求

    /**
     * 图片上传
     *
     * @param filePath
     */
    private void requestImage(String filePath) {
        File file = new File(filePath);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("app", "Ucenter");
        params.put("class", "Feedback2");
        String sign = MD5ChangeUtil.Md5_32("UcenterFeedback2" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        params.put("img", file);
        requestFile(file.getName(), file, params, 0);
    }

    /**
     * 问题反馈
     */
    public void requestFankui(String content) {
        String textContact = etContactInfo.getText().toString().trim();
        if (TextUtils.isEmpty(textContact)) {
            textContact = "";
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("app", "Ucenter");
        params.put("class", "Feedback2");
        String sign = MD5ChangeUtil.Md5_32("UcenterFeedback2" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        params.put("phone", textContact);
        params.put("content", content);
        params.put(HttpConstant.APP_UID,(String) SharePreferenceUtil.get(this, SharePreferenceUtil.UID, ""));
        List<String> listU = mAdapter.getListUrl();
        StringBuffer stringBuffer = new StringBuffer();
        String url = "";
        if (listU.size() == 1) {
            url = listU.get(0);
            stringBuffer.append("" + url);
        } else {
            for (int i = 0; i < listU.size(); i++) {
                url = listU.get(i);
                if (i == listU.size() - 1) {
                    stringBuffer.append("" + url);
                } else {
                    stringBuffer.append(url + "|");
                }
            }
        }
        if (!stringBuffer.toString().isEmpty()) {
            params.put("imagesUrl", stringBuffer.toString());
        }
        request(params, HttpConstant.MY_FEEDBACK);
    }
}
