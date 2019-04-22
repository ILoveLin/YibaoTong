package com.lzyc.ybtappcal.util;

import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @desc 下载apk
 * Created by yang on 2016/4/29.
 */
public class DownLoadAPKUtil extends  Thread{
    private static final String TAG = DownLoadAPKUtil.class.getSimpleName();
    public static final int BUF_LEN = 1024;
    public static final int MSG_NET_RES_OK = 1000;
    public static final int MSG_NET_RES_ERR = 1001;
    public static final int MSG_NET_RES_GET = 1002;
    public static final int MSG_DOWNLOAD_OK = 1005;

    private Handler mHandler = null;
    private String mUrl = null;
    private String mFileName = "";
    private String mPath;

    public DownLoadAPKUtil(){

    }

    public DownLoadAPKUtil(Handler handler,String url,String localPath,String fileName) {
        this.mHandler=handler;
        this.mUrl=url;
        this.mFileName=fileName;
        this.mPath=localPath;
    }


    @Override
    public void run() {
        InputStream inputStream;
        try {
            URL url = new URL(mUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if(connection.getResponseCode()==200){
                int fileSize = connection.getContentLength() / 1024;
                Message msg;
                inputStream = connection.getInputStream();
                if (inputStream == null) {
                    throw new RuntimeException("stream is null");
                }

                mkDirs(mPath);
                File dFile = new File(mPath, mFileName);
                // dFile.setExecutable(true);
                FileOutputStream fos = new FileOutputStream(dFile);
                byte buf[] = new byte[BUF_LEN];
                int total = 0;
                inputStream = connection.getInputStream();
                int len = 0;
                while ((len = inputStream.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                    total += (len / 1024);
                    LogUtil.y(total * 100+"#正在下载#"+fileSize+"###进度"+total * 100 / fileSize);
                    msg = mHandler.obtainMessage(MSG_NET_RES_GET, total * 100 / fileSize);
                    mHandler.sendMessage(msg);
                }
                fos.flush();
                fos.close();
                inputStream.close();
                msg = mHandler.obtainMessage(MSG_DOWNLOAD_OK, 100);
                mHandler.sendMessage(msg);
                msg = mHandler.obtainMessage(MSG_NET_RES_OK, "complete");
                mHandler.sendMessage(msg);
            }

        } catch (Exception e) {
            Message msg = mHandler.obtainMessage(MSG_NET_RES_ERR,
                    e.toString());
            mHandler.sendMessage(msg);
        }
    }

    public static void mkDirs(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
            }
        }
    }

}
