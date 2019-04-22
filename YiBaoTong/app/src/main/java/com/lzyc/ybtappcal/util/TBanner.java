package com.lzyc.ybtappcal.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;
import com.youth.banner.loader.ImageLoaderInterface;

import java.io.InputStream;

public class TBanner extends Banner {

    Context context;

    public TBanner(Context context) {
        this(context,null);
    }

    public TBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init(){
        setImageLoader(new GlideImageLoader());
        setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        setIndicatorGravity(BannerConfig.CENTER);

        setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                if(null != onImgRollChanged){
                    onImgRollChanged.onChanged(position - 1);
                }
            }
        });
    }

    class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, SimpleDraweeView imageView) {

//            String p = (String) path;
//
//            if(!NetworkUtil.CheckConnection(context)){
//                if(p.contains("?")){
//                    p = p.split("\\?")[0];
//                    path = p;
//                }
//            }

            new GetBannerHeight().execute((String)path);

            imageView.setImageURI((String)path);
//            Picasso.with(context.getApplicationContext())
//                .load((String)path)
//                .into(imageView);
        }
    }

    protected class GetBannerHeight extends AsyncTask<String, Intent, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap tmpBitmap = null;
            try {
                InputStream is = new java.net.URL(url).openStream();
                tmpBitmap = BitmapFactory.decodeStream(is);
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return tmpBitmap;

        }

        protected void onPostExecute(Bitmap result) {

            int height = Integer.parseInt(SharePreferenceUtil.getString(context, SharePreferenceUtil.KEY_CACHE_BANNER_HEIGHT, "602"));
            int width = ScreenUtils.getScreenWidth();

            try{
                height = width * result.getHeight() / result.getWidth();

                SharePreferenceUtil.putString(context, SharePreferenceUtil.KEY_CACHE_BANNER_HEIGHT, String.valueOf(height));

            } catch (Exception e){
                e.printStackTrace();
            }
            setLayoutParams(new LinearLayout.LayoutParams(width, height));
        }
    }

    public abstract class ImageLoader implements ImageLoaderInterface<SimpleDraweeView> {

        @Override
        public SimpleDraweeView createImageView(Context context) {
            SimpleDraweeView imageView = new SimpleDraweeView(context);
            return imageView;
        }

    }


    private ImgRollChanged onImgRollChanged;

    public void setOnImgRollChanged(ImgRollChanged onImgRollChanged){
        this.onImgRollChanged = onImgRollChanged;
    }
    public interface ImgRollChanged{
        public void onChanged(int p);
    }
}
