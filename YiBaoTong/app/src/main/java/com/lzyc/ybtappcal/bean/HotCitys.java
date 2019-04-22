package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by lovelin on 2017/1/21.
 */

public class HotCitys extends BaseBean {


    /**
     * status : 0
     * msg :
     * data : {"cityList":[{"province":"北京","city":"北京市"},{"province":"上海","city":"上海市"},{"province":"广东省","city":"广州市"},{"province":"广东省","city":"深圳市"},{"province":"河北省","city":"石家庄市"},{"province":"河南省","city":"郑州市"},{"province":"内蒙古自治区","city":"呼和浩特市"},{"province":"江苏省","city":"南京市"},{"province":"四川省","city":"成都市"}]}
     */

    private int status;
    private String msg;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<CityListBean> cityList;

        public List<CityListBean> getCityList() {
            return cityList;
        }

        public void setCityList(List<CityListBean> cityList) {
            this.cityList = cityList;
        }

        public static class CityListBean {
            /**
             * province : 北京
             * city : 北京市
             */

            private String province;
            private String city;

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "cityList=" + cityList +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "HotCitys{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
