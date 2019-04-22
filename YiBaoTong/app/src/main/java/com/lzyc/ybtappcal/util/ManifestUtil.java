package com.lzyc.ybtappcal.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

public class ManifestUtil {
	private static final String TAG = ManifestUtil.class.getName();
	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String metaValue = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				metaValue = metaData.getString(metaKey);
			}
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(TAG,e.getMessage(),e);
		}
		return metaValue;
	}
}
