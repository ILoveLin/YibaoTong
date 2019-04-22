package com.lzyc.ybtappcal.view.scan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.ScreenUtils;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.io.ByteArrayOutputStream;

import cn.bingoogolapple.qrcode.core.ProcessDataTask;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.BarcodeFormat;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;

/**
 * zxing和zbar扫码集成
 * Created by yang on 2017/06/12.
 */
public class ScanView extends QRCodeView {
    public static final int SCAN_TYPE_ZBAR = 0;
    public static final int SCAN_TYPE_ZXING = 1;
    private int scanType = SCAN_TYPE_ZBAR;
    private ImageScanner mScanner;
    private MultiFormatReader mMultiFormatReader;

    public ScanView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupScanner();//zbar初始化
        initMultiFormatReader();//zxing初始化
    }

    private void initMultiFormatReader() {
        mMultiFormatReader = new MultiFormatReader();
        mMultiFormatReader.setHints(QRCodeDecoder.HINTS);
    }

    private void setupScanner() {
        mScanner = new ImageScanner();
        mScanner.setConfig(0, Config.X_DENSITY, 3);
        mScanner.setConfig(0, Config.Y_DENSITY, 3);

        mScanner.setConfig(Symbol.NONE, Config.ENABLE, 0);
        for (BarcodeFormat format : BarcodeFormat.ALL_FORMATS) {
            mScanner.setConfig(format.getId(), Config.ENABLE, 1);
        }
    }

    /**
     * 0：SCAN_TYPE_ZBAR
     * 1：SCAN_TYPE_ZXING
     *
     * @param scanType
     */
    public void setScanType(int scanType) {
        this.scanType = scanType;
    }

    @Override
    public String processData(byte[] data, int width, int height, boolean isRetry) {
        String result = null;
        if (scanType == SCAN_TYPE_ZBAR) {
            Image barcode = new Image(width, height, "Y800");
            Rect rect = mScanBoxView.getScanBoxAreaRect(height);
            if (rect != null && !isRetry && rect.left + rect.width() <= width && rect.top + rect.height() <= height) {
                barcode.setCrop(rect.left, rect.top, rect.width(), rect.height());
            }
            barcode.setData(data);
            result = processData(barcode);
        } else if (scanType == SCAN_TYPE_ZXING) {
            try {
                PlanarYUVLuminanceSource source = null;
                Rect rect = mScanBoxView.getScanBoxAreaRect(height);
                if (rect != null) {
                    source = new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height(), false);
                } else {
                    source = new PlanarYUVLuminanceSource(data, width, height, 0, 0, width, height, false);
                }
                Result rawResult = mMultiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(source)));
                if (rawResult != null) {
                    result = rawResult.getText();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mMultiFormatReader.reset();
            }
        }
//        if (!TextUtils.isEmpty(result)){
//            if(result.length()<10){
//                startSpot();
//                return result;
//            }
//            if (mListener != null) {
//                LogUtil.y(result + "###end#######processData#################" + scanType);
//                mListener.onScanSuccess(result, null);
//                stopSpot();
//            }
//        }
        return result;
    }

    @Override
    public void onPreviewFrame(final byte[] data, final Camera camera) {
        if (mSpotAble) {
            cancelProcessDataTask();
            mProcessDataTask = new ProcessDataTask(camera, data, this) {
                @Override
                protected void onPostExecute(String result) {
                    if (mSpotAble) {
                        if (mDelegate != null && !TextUtils.isEmpty(result)) {
                            try {
                                mDelegate.onScanQRCodeSuccess(result);
                                if (!TextUtils.isEmpty(result)) {
                                    if (result.length() < 10) {
                                        startSpot();
                                        return;
                                    }
                                    if (mListener != null) {
                                        Camera.Parameters parameters = camera.getParameters();
                                        Camera.Size size = parameters.getPreviewSize();
                                        YuvImage yuvimage = new YuvImage(
                                                data,
                                                ImageFormat.NV21,
                                                size.width,
                                                size.height,
                                                null);
                                        LogUtil.y(size.width+"size.width,size.height"+size.height+"  w"+ScreenUtils.getScreenWidth()+"  h"+ScreenUtils.getScreenHeight());
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        yuvimage.compressToJpeg(new Rect(0, 0, yuvimage.getWidth(), yuvimage.getHeight()), 100, baos);// 80--JPG图片的质量[0-100],100最高
                                        byte[] rawImage = baos.toByteArray();
                                        BitmapFactory.Options options = new BitmapFactory.Options();
                                        options.outWidth= ScreenUtils.getScreenWidth();
                                        options.outHeight=ScreenUtils.getScreenHeight();
                                        Bitmap bitmap1 = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length, options);
                                        Matrix matrix = new Matrix();
                                        matrix.preRotate(90);
                                        Bitmap bitmap2 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), matrix, true);
                                        mListener.onScanSuccess(result, bitmap2);
                                        stopSpot();
                                    }
                                }
                            } catch (Exception e) {
                            }
                        } else {
                            try {
                                camera.setOneShotPreviewCallback(ScanView.this);
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }.perform();
        }
    }


    public String processData(Image barcode) {
        String result = null;
        if (mScanner.scanImage(barcode) != 0) {
            SymbolSet syms = mScanner.getResults();
            for (Symbol sym : syms) {
                String symData = sym.getData();
                if (!TextUtils.isEmpty(symData)) {
                    result = symData;
                    break;
                }
            }
        }
        return result;
    }

    public ImageScanner getScanner() {
        return mScanner;
    }

    /**
     * 延迟1.5秒后开始识别
     */
    public void startSpot() {
        hiddenScanRect();
        startSpotDelay(1500);
    }
    private OnScanCodeListener mListener;

    public void setOnScanCodeListener(OnScanCodeListener listener) {
        this.mListener = listener;
    }

    public interface OnScanCodeListener {
        void onScanSuccess(String result, Bitmap bitmap);
    }
}
