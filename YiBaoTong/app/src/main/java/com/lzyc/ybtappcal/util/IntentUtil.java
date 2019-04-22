package com.lzyc.ybtappcal.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

/*
 * Author: Lucifer
 * Created Date:2015-3-9
 * Copyright @ 2015 BU
 * Description: Intent的工具类
 *
 * History:
 */
public class IntentUtil {
    /**
     * 跳转一个新界面的Intent
     *
     * @param context
     * @param cls
     * @return Intent
     * @author Lucifer 2015-3-9 下午11:11:59
     */
    public static void StartIntent(Context context, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转一个新界面的Intent
     *
     * @param context
     * @param cls
     * @return Intent
     * @author Lucifer 2015-3-9 下午11:11:59
     */
    public static void StartIntent(Context context, Class<?> cls, int flag) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.setFlags(flag);
        context.startActivity(intent);
    }

    /**
     * 跳转相册
     *
     * @return Intent
     * @author luxf 2015-4-3 上午11:04:39
     */
    public Intent getPhotoFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        return intent;
    }

    /**
     * 跳转到相机
     *
     * @return
     */
    public Intent getPhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picFile));
        return intent;
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public Intent startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        // cls.startActivityForResult(intent, 3);
        return intent;
    }

}
