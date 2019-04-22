package com.lzyc.ybtappcal.widget.redpacket;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import java.util.HashMap;

public class Flake {
    float x, y;
    float rotation;
    float speed;
    float rotationSpeed;
    int width,height;
    Bitmap bitmap;

    static HashMap<Integer, Bitmap> bitmapMap = new HashMap<Integer, Bitmap>();

    static Flake createFlake(float xRange, Bitmap originalBitmap, Context Context) {
        Flake flake = new Flake();
        DisplayMetrics metrics = getDisplayMetrics(Context);
        if (metrics.widthPixels >= 1080) {
            flake.width = (int) (5 + (float) Math.random() * 80);
            float hwRatio = originalBitmap.getHeight() / originalBitmap.getWidth();
            flake.height = (int) (flake.width * hwRatio + 60);
        } else {
            flake.width = (int) (5 + (float) Math.random() * 50);
            float hwRatio = originalBitmap.getHeight() / originalBitmap.getWidth();
            flake.height = (int) (flake.width * hwRatio + 40);

        }
        flake.x = (float) Math.random() * (xRange - flake.width);
        flake.y = 0 - (flake.height + (float) Math.random() * flake.height);
        flake.speed = 50 + (float) Math.random() * 150;
        flake.rotation = (float) Math.random() * 180 - 90;
        flake.rotationSpeed = (float) Math.random() * 90 - 45;
        flake.bitmap = bitmapMap.get(flake.width);
        if (flake.bitmap == null) {
            flake.bitmap = Bitmap.createScaledBitmap(originalBitmap,(int) flake.width, (int) flake.height, true);
            bitmapMap.put(flake.width,flake.bitmap);
        }
        return flake;
    }
    
    /**
     * 获取屏幕尺寸与密度.
     * @param context the context
     * @return mDisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        Resources mResources;
        if (context == null) {
            mResources = Resources.getSystem();

        } else {
            mResources = context.getResources();
        }
        //DisplayMetrics{density=1.5, width=480, height=854, scaledDensity=1.5, xdpi=160.421, ydpi=159.497}
        //DisplayMetrics{density=2.0, width=720, height=1280, scaledDensity=2.0, xdpi=160.42105, ydpi=160.15764}
        DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
        return mDisplayMetrics;
    }
}
