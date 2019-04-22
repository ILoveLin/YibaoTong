package com.lzyc.ybtappcal.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2017/04/21.
 */

public class TopBanner {

    /**
     * data : {"images":[{"img_url":"http://new.yibaotongapp.com//uploads/page_style/biyao_banner_1.png","web_url":""},{"img_url":"http://new.yibaotongapp.com//uploads/page_style/biyao_banner_1.png","web_url":""}]}
     * msg :
     * status : 0
     */

    private DataBean data;
    private String msg;
    private int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean implements Serializable{
        private ArrayList<ImagesBean> images;

        public ArrayList<ImagesBean> getImages() {
            return images;
        }

        public void setImages(ArrayList<ImagesBean> images) {
            this.images = images;
        }

        public static class ImagesBean implements Serializable{
            /**
             * img_url : http://new.yibaotongapp.com//uploads/page_style/biyao_banner_1.png
             * web_url :
             */

            private String img_url;
            private String web_url;

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getWeb_url() {
                return web_url;
            }

            public void setWeb_url(String web_url) {
                this.web_url = web_url;
            }
        }
    }
}
