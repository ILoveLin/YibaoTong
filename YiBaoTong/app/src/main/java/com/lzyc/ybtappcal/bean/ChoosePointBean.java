package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by lovelin on 2016/11/7.
 */
public class ChoosePointBean extends  BaseBean {


    /**
     * status : 0
     * msg :
     * data : {"list":[{"hostops_id":"42","name":"北京市海淀区清河街道怡美社区卫生服务站","phone":"18270720637","Yyid":"00100816206401","Jibie":"级","Level":"未评","average":"0.0"},{"hostops_id":"44","name":"北京市海淀区上地街道上地社区卫生服务站","phone":"18270720637","Yyid":"00100816200101","Jibie":"级","Level":"未评","average":"4.8"},{"hostops_id":"45","name":"北京市海淀区清河街道美和园社区卫生服务站","phone":"18270720637","Yyid":"001008162154","Jibie":"级","Level":"未评","average":"0.0"},{"hostops_id":"46","name":"北京市海淀区清河街道西二旗社区卫生服务站","phone":"18270720637","Yyid":"00100816208701","Jibie":"级","Level":"未评","average":"5.0"},{"hostops_id":"47","name":"北京体育大学医院","phone":"18270720637","Yyid":"00100811101602","Jibie":"合格","Level":"一级","average":"0.0"}]}
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
        /**
         * hostops_id : 42
         * name : 北京市海淀区清河街道怡美社区卫生服务站
         * phone : 18270720637
         * Yyid : 00100816206401
         * Jibie : 级
         * Level : 未评
         * average : 0.0
         */

        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String hostops_id;
            private String name;
            private String phone;
            private String Yyid;
            private String Jibie;
            private String Level;
            private String average;

            public String getHostops_id() {
                return hostops_id;
            }

            public void setHostops_id(String hostops_id) {
                this.hostops_id = hostops_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getYyid() {
                return Yyid;
            }

            public void setYyid(String Yyid) {
                this.Yyid = Yyid;
            }

            public String getJibie() {
                return Jibie;
            }

            public void setJibie(String Jibie) {
                this.Jibie = Jibie;
            }

            public String getLevel() {
                return Level;
            }

            public void setLevel(String Level) {
                this.Level = Level;
            }

            public String getAverage() {
                return average;
            }

            public void setAverage(String average) {
                this.average = average;
            }
        }
    }
}
