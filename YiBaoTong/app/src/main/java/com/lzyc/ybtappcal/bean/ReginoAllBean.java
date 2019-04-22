package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by lovelin on 2017/1/19.
 */

public class ReginoAllBean extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * province : 北京
         * province_id : 1
         * citys : [{"city":"北京市","city_id":"2","countys":[]}]
         */

        private String province;
        private String province_id;
        private List<CitysBean> citys;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getProvince_id() {
            return province_id;
        }

        public void setProvince_id(String province_id) {
            this.province_id = province_id;
        }

        public List<CitysBean> getCitys() {
            return citys;
        }

        public void setCitys(List<CitysBean> citys) {
            this.citys = citys;
        }

        public static class CitysBean {
            /**
             * city : 北京市
             * city_id : 2
             * countys : []
             */

            private String city;
            private String city_id;
            private List<?> countys;

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getCity_id() {
                return city_id;
            }

            public void setCity_id(String city_id) {
                this.city_id = city_id;
            }

            public List<?> getCountys() {
                return countys;
            }

            public void setCountys(List<?> countys) {
                this.countys = countys;
            }
        }


//        public static class xianBean{
//            private String county;
//            private String county_id;
//
//            public String getCounty() {
//                return county;
//            }
//
//            public void setCounty(String county) {
//                this.county = county;
//            }
//
//            public String getCounty_id() {
//                return county_id;
//            }
//
//            public void setCounty_id(String county_id) {
//                this.county_id = county_id;
//            }
//        }
    }
}
