package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by lxx on 2017/5/12.
 */

public class BannerBean {

    private List<ImagesBean> images;

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    /**
     * web_url : http://www.baidu.com
     * img_url : http://new.yibaotongapp.com//uploads/page_style/image_banner@2x.png
     */

    public static class ImagesBean {
        /**
         * web_url : http://www.baidu.com
         * img_url : http://new.yibaotongapp.com//uploads/page_style/image_banner@2x.png
         */

        private String web_url;
        private String img_url;

        public String getWeb_url() {
            return web_url;
        }

        public void setWeb_url(String web_url) {
            this.web_url = web_url;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }
    }

    @Override
    public String toString() {
        return "BannerBean{" +
                "images=" + images +
                '}';
    }
}
