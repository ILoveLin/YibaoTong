package com.lzyc.zbar.decoding;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.util.BitmapUtil;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.ScreenUtils;
import com.lzyc.zbar.activity.CaptureActivity;
import com.lzyc.zbar.camera.CameraManager;

import java.io.ByteArrayOutputStream;


/**
 * This class handles all the messaging which comprises the state machine for capture.
 */
public final class CaptureActivityHandler extends Handler {

  private static final String TAG = CaptureActivityHandler.class.getSimpleName();

  private final CaptureActivity activity;
  private final DecodeThread decodeThread;
  private State state;

  private enum State 
  {
    PREVIEW,
    SUCCESS,
    DONE
  }

  public CaptureActivityHandler(CaptureActivity activity) 
  {
    this.activity = activity;
    decodeThread = new DecodeThread(activity);
    decodeThread.start();
    state = State.SUCCESS;
    CameraManager.get().startPreview();
    restartPreviewAndDecode();
    CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
  }

  public void restart(){
    state = State.SUCCESS;
    CameraManager.get().startPreview();
    restartPreviewAndDecode();
  }

  @Override
  public void handleMessage(Message message) 
  {
    switch (message.what) {
      case R.id.auto_focus:
        if (state == State.PREVIEW) 
        {
          CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
        }
        break;
      case R.id.restart_preview:
        Log.d(TAG, "Got restart preview message");
        restartPreviewAndDecode();
        break;
      case R.id.decode_succeeded:
    	String strResult=(String) message.obj;
        Log.d(TAG, "Got decode succeeded message:"+strResult);
        state = State.SUCCESS;
        Bundle bundle= message.getData();
        byte[] data= bundle.getByteArray("byte");
        int w=bundle.getInt("w");
        int h=bundle.getInt("h");
        YuvImage yuvimage = new YuvImage(
                data,
                ImageFormat.NV21,
                w,
                h,
                null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, yuvimage.getWidth(), yuvimage.getHeight()), 100, baos);// 80--JPG图片的质量[0-100],100最高
        byte[] rawImage = baos.toByteArray();
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap1 = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length, options);
        Matrix matrix = new Matrix();
        matrix.preRotate(90);
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), matrix, true);
        activity.handleDecode(strResult,bitmap2);
        bitmap1.recycle();
        /***********************************************************************/
        break;
      case R.id.decode_failed:
        state = State.PREVIEW;
        CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
        break;
      case R.id.return_scan_result:
        Log.d(TAG, "Got return scan result message");
        activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
        activity.finish();
        break;
    
    }
  }

  public void quitSynchronously() 
  {
    state = State.DONE;
    CameraManager.get().stopPreview();
    Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
    quit.sendToTarget();
    try 
    {
      decodeThread.join();
    } catch (InterruptedException e) {
      // continue
    }
    removeMessages(R.id.decode_succeeded);
    removeMessages(R.id.decode_failed);
  }

  public void restartPreviewAndDecode()
  {
    if (state == State.SUCCESS) 
    {
      state = State.PREVIEW;
      CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
      CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
      activity.drawViewfinder();
    }
  }



}
