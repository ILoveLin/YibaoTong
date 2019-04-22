package com.lzyc.zxing.decoding;

/*
 * Copyright 2009 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.zxing.LuminanceSource;
import com.lzyc.ybtappcal.util.BitmapUtil;

import java.io.FileNotFoundException;

/**
 * This class is used to help decode images from files which arrive as RGB data
 * from Android bitmaps. It does not support cropping or rotation.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class RGBLuminanceSourcee extends LuminanceSource {
    private final byte[] luminances;
    private boolean isOkSize;

    public RGBLuminanceSourcee(String path) throws FileNotFoundException {
        this(loadBitmap(path));
    }

    public RGBLuminanceSourcee(String path, boolean isOkSize) throws FileNotFoundException {
        this(loadBitmap(path, isOkSize));
        this.isOkSize = isOkSize;
    }

    public RGBLuminanceSourcee(Bitmap bitmap) {
        super(bitmap.getWidth(), bitmap.getHeight());
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        // In order to measure pure decoding speed, we convert the entire image
        // to a greyscale array
        // up front, which is the same as the Y channel of the
        // YUVLuminanceSource in the real app.
        luminances = new byte[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                int pixel = pixels[offset + x];
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
                if (r == g && g == b) {
                    // Image is already greyscale, so pick any channel.
                    luminances[offset + x] = (byte) r;
                } else {
                    // Calculate luminance cheaply, favoring green.
                    luminances[offset + x] = (byte) ((r + g + g + b) >> 2);
                }
            }
        }
    }

    @Override
    public byte[] getRow(int y, byte[] row) {
        if (y < 0 || y >= getHeight()) {
            throw new IllegalArgumentException(
                    "Requested row is outside the image: " + y);
        }
        int width = getWidth();
        if (row == null || row.length < width) {
            row = new byte[width];
        }
        System.arraycopy(luminances, y * width, row, 0, width);
        return row;
    }

    // Since this class does not support cropping, the underlying byte array
    // already contains
    // exactly what the caller is asking for, so give it to them without a copy.
    @Override
    public byte[] getMatrix() {
        return luminances;
    }

    private static Bitmap loadBitmap(String path) throws FileNotFoundException {

        Bitmap bitmap = BitmapUtil.compress(path);
        //ZXing源码
        //Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap == null) {
            throw new FileNotFoundException("Couldn't open " + path);
        }
        return bitmap;
    }

    private static Bitmap loadBitmap(String path, boolean isOkSize) throws FileNotFoundException {
        Bitmap bitmap = null;
        if (isOkSize) {  //小图
            bitmap = BitmapFactory.decodeFile(path);
        } else { //大图压缩
            bitmap = getDecodeAbleBitmap(path);
        }
        //ZXing源码
        if (bitmap == null) {
            throw new FileNotFoundException("Couldn't open " + path);
        }
        return bitmap;
    }


    /**
     * 将本地图片文件转换成可解码二维码的 Bitmap。为了避免图片太大，这里对图片进行了压缩。感谢 https://github.com/devilsen 提的 PR
     *
     * @param picturePath 本地图片文件路径
     * @return
     */
    private static Bitmap getDecodeAbleBitmap(String picturePath) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picturePath, options);
            int sampleSize = options.outHeight / 400;
            if (sampleSize <= 0) {
                sampleSize = 1;
            }
            options.inSampleSize = sampleSize;
            options.inJustDecodeBounds = false;

            return BitmapFactory.decodeFile(picturePath, options);
        } catch (Exception e) {
            return null;
        }
    }
}