/*
 * Copyright (C) 2008 ZXing authors
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

package com.lzyc.zxing.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.util.ScreenUtils;
import com.lzyc.zxing.camera.CameraManager;

import java.util.Collection;
import java.util.HashSet;


/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 */
public final class ViewfinderView extends View {

    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    /**
     * 刷新界面的时间
     */
    private static final long ANIMATION_DELAY = 1L;
    private static final int OPAQUE = 0xFF;

    private final Paint paint;
    private Bitmap resultBitmap;
    private Bitmap bgBitmap;
    private final int maskColor;
    private final int resultColor;
    private final int frameColor;
    private final int laserColor;
    private final int resultPointColor;
    private final int angelColor;
    private final String textTop = "请在光线充足的地方将条形码/监管码";
    private final String textTop1 = "全部放入扫码框内";
    private int scannerAlpha;
    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;
    private int rectY;
    private Context context;
    private Rect frame=new Rect();
    private int measureedWidth;
    private int measureedHeight;
    private float density;

    private boolean isRefesh = false;

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        paint = new Paint();
        Resources resources = getResources();
        density = context.getResources().getDisplayMetrics().density;
        resultBitmap = BitmapFactory.decodeResource(resources, R.mipmap.image_scan_rect);
        bgBitmap = BitmapFactory.decodeResource(resources, R.mipmap.image_scan_bg_rect);
        maskColor = resources.getColor(R.color.color_000000_65);
        resultColor = resources.getColor(R.color.color_000000_65);
        frameColor = resources.getColor(R.color.color_ffffff_ff);
        laserColor = resources.getColor(R.color.color_ffff00_ff);
        resultPointColor = resources.getColor(R.color.color_ffff00_c0);
        angelColor = resources.getColor(R.color.color_02a3f0);
        scannerAlpha = 0;
        rectY = -500;
        possibleResultPoints = new HashSet<ResultPoint>(5);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (frame == null) {
            frame = CameraManager.init(getContext()).get().getFramingRect();
        }
        //获取屏幕的宽和高
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(OPAQUE);
            if (rectY == -500) {
                rectY = frame.top + 1;
            } else {
                rectY = rectY + 5;
            }
            if (rectY >= frame.bottom) {
                rectY = frame.top;
            }

            Rect rect = new Rect(frame.left+10, frame.top+15, frame.right-10, rectY);

            //modify yanlc
            int srcTop = rectY - frame.top;
            int srcBottom = resultBitmap.getHeight();
            srcTop = resultBitmap.getHeight() - srcTop;
            if(srcTop <= 0)
                srcTop =0;

            Rect srcRect = new Rect(0,srcTop,resultBitmap.getWidth(),srcBottom);

            canvas.drawBitmap(resultBitmap, srcRect, rect, paint);
            canvas.drawBitmap(bgBitmap, null, new Rect(frame.left, frame.top, frame.right, frame.bottom ), paint);
            // Draw the exterior (i.e. outside the framing rect) darkened
            paint.setColor(resultBitmap != null ? resultColor : maskColor);
            //画出扫描框外面的阴影部分，共四个部分，扫描框的上面到屏幕上面，扫描框的下面到屏幕下面
            //扫描框的左边面到屏幕左边，扫描框的右边到屏幕右边
            canvas.drawRect(0, 0, width, frame.top, paint);
            canvas.drawRect(0, frame.top, frame.left, frame.bottom, paint);
            canvas.drawRect(frame.right, frame.top, width, frame.bottom, paint);
            canvas.drawRect(0, frame.bottom, width, height, paint);
            paint.setAntiAlias(true);
            // text color - #3D3D3D
            paint.setColor(Color.WHITE);
            paint.setTextSize(ScreenUtils.getScreenWidth() / 100 * 4);
            paint.setDither(true);
            paint.setFilterBitmap(true);
            Rect boundsTop = new Rect();
            paint.getTextBounds(textTop, 0, textTop.length(), boundsTop);
            Rect boundsTop1 = new Rect();
            paint.getTextBounds(textTop1, 0, textTop1.length(), boundsTop1);
            int x1 = (ScreenUtils.getScreenWidth() - boundsTop.width()) / 2;
            int x2 = (ScreenUtils.getScreenWidth() - boundsTop1.width()) / 2;
            int y1 = (ScreenUtils.getScreenWidth() - boundsTop.width()) / 10;
            canvas.drawText(textTop, x1,frame.top-4*y1, paint);
            canvas.drawText(textTop1, x2,frame.top-2*y1, paint);
            if(isRefesh) {
              postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
            }
//            postInvalidateDelayed(ANIMATION_DELAY, 0, 0, width, height);
        } else {
            //画出扫描框外面的阴影部分，共四个部分，扫描框的上面到屏幕上面，扫描框的下面到屏幕下面
            //扫描框的左边面到屏幕左边，扫描框的右边到屏幕右边
            // Draw the exterior (i.e. outside the framing rect) darkened
            paint.setColor(resultBitmap != null ? resultColor : maskColor);
            canvas.drawRect(0, 0, width, frame.top, paint);
            canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
            canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
            canvas.drawRect(0, frame.bottom + 1, width, height, paint);
            //鐢昏竟妗�
            canvas.drawBitmap(bgBitmap, null, new Rect(frame.left - 5, frame.top - 5, frame.right + 5, frame.bottom + 5), paint);
            //鐢诲瓧浣�
            paint.setAntiAlias(true);
            // text color - #3D3D3D
            paint.setColor(Color.WHITE);
            paint.setTextSize(16);
            paint.setDither(true); //鑾峰彇璺熸竻鏅扮殑鍥惧儚閲囨牱
            paint.setFilterBitmap(true);//杩囨护涓�浜�

            Rect boundsTop = new Rect();
            paint.getTextBounds(textTop, 0, textTop.length(), boundsTop);

            Rect boundsTop1 = new Rect();
            paint.getTextBounds(textTop1, 0, textTop1.length(), boundsTop1);
            paint.setColor(frameColor);
            canvas.drawRect(frame.left, frame.top, frame.right + 1, frame.top + 2, paint);
            canvas.drawRect(frame.left, frame.top + 2, frame.left + 2, frame.bottom - 1, paint);
            canvas.drawRect(frame.right - 1, frame.top, frame.right + 1, frame.bottom - 1, paint);
            canvas.drawRect(frame.left, frame.bottom - 1, frame.right + 1, frame.bottom + 1, paint);

            ///画扫描框边上的角，总共8个部分
            paint.setColor(angelColor);
            canvas.drawRect(frame.left - 5, frame.top - 5, frame.left + 25, frame.top, paint);
            canvas.drawRect(frame.left - 5, frame.top - 5, frame.left, frame.top + 25, paint);
            canvas.drawRect(frame.left - 5, frame.bottom - 24, frame.left, frame.bottom + 6, paint);
            canvas.drawRect(frame.left - 5, frame.bottom + 1, frame.left + 25, frame.bottom + 4, paint);
            canvas.drawRect(frame.right - 24, frame.top - 5, frame.right + 6, frame.top, paint);
            canvas.drawRect(frame.right + 1, frame.top - 5, frame.right + 6, frame.top + 25, paint);
            canvas.drawRect(frame.right + 1, frame.bottom - 24, frame.right + 6, frame.bottom + 6, paint);
            canvas.drawRect(frame.right - 24, frame.bottom + 1, frame.right + 6, frame.bottom + 6, paint);

            // Draw a red "laser scanner" line through the middle to show decoding is active
            paint.setColor(laserColor);
            paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
            scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
            int middle = frame.height() / 2 + frame.top;
            canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, paint);


            Collection<ResultPoint> currentPossible = possibleResultPoints;
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new HashSet<ResultPoint>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(OPAQUE);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 6.0f, paint);
                }
            }
            if (currentLast != null) {
                paint.setAlpha(OPAQUE / 2);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 3.0f, paint);
                }
            }
            //只刷新扫描框的内容，其他地方不刷新
            if(isRefesh) {
                postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
            }

        }
    }

    public void drawViewfinder() {
//        resultBitmap = null;
        isRefesh = true;
        invalidate();
    }

    public void closeDrawViewfinder(){
        isRefesh = false;
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureedWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureedHeight = MeasureSpec.getSize(heightMeasureSpec);
        int borderWidth = (int) (measureedWidth -30*density);
        int borderHeight=measureedHeight/3;
        int left=(measureedWidth - borderWidth) / 2;
        int top=(measureedHeight - borderHeight) / 2;
        frame.set(left, top, left+borderWidth, top + borderHeight);
    }

    public Rect getScanImageRect(int w, int h)
    {
        Rect rect = new Rect();
        rect.left = frame.left;
        rect.right = frame.right;
        float temp = h / (float) measureedHeight;
        rect.top = (int) (frame.top * temp);
        rect.bottom = (int) (frame.bottom * temp);
        return rect;
    }
    public Bitmap getBitmap(){
        Bitmap bitmap=null;
        try{
            this.layout(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
            this.setDrawingCacheEnabled(true);
            this.buildDrawingCache();
            bitmap = this.getDrawingCache();
        }catch (Exception e){
        }
        return  bitmap;
    }

    public Rect getFrame() {
        return frame;
    }

    public void setFrame(Rect frame) {
        this.frame = frame;
    }
}
