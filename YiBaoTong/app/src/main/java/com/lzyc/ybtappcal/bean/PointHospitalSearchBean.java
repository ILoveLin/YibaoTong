package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by lovelin on 2016/11/8.
 * 定点医院搜索结果bean
 */
public class PointHospitalSearchBean extends BaseBean {

    /**
     * status : 0
     * msg :
     * data : {"list":[{"Yyid":"00100211000102","Name":"北京积水潭医院（回龙观院区）","ShortName":"北京积水潭医院（回龙观院区）"},{"Yyid":"00100211000101","Name":"北京积水潭医院（北京大学第四临床医学院）","ShortName":"北京积水潭医院（北京大学第四临床医学院）"}],"count":2}
     */

    private int status;
    private String msg;
    /**
     * list : [{"Yyid":"00100211000102","Name":"北京积水潭医院（回龙观院区）","ShortName":"北京积水潭医院（回龙观院区）"},{"Yyid":"00100211000101","Name":"北京积水潭医院（北京大学第四临床医学院）","ShortName":"北京积水潭医院（北京大学第四临床医学院）"}]
     * count : 2
     */

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
        private int count;
        /**
         * Yyid : 00100211000102
         * Name : 北京积水潭医院（回龙观院区）
         * ShortName : 北京积水潭医院（回龙观院区）
         */

        private List<SearchListBean> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<SearchListBean> getList() {
            return list;
        }

        public void setList(List<SearchListBean> list) {
            this.list = list;
        }

        public static class SearchListBean {
            private String Yyid;
            private String Name;
            private String ShortName;

            public String getYyid() {
                return Yyid;
            }

            public void setYyid(String Yyid) {
                this.Yyid = Yyid;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public String getShortName() {
                return ShortName;
            }

            public void setShortName(String ShortName) {
                this.ShortName = ShortName;
            }
        }
    }
}
