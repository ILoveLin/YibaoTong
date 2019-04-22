package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by lovelin on 2016/11/7.
 */
public class PointHospitalBean extends BaseBean {

    /**
     * status : 0
     * msg :
     * data : {"list":[{"Yyid":"00100816208701","Name":"北京市海淀区清河街道西二旗社区卫生服务站","ShortName":"清河街道西二旗社区卫生服务站","juli":"984"},{"Yyid":"00100816206401","Name":"北京市海淀区清河街道怡美社区卫生服务站","ShortName":"北京市清河街道怡美社区卫生服务站","juli":"1174"},{"Yyid":"001008162127","Name":"北京市海淀区上地街道东馨园社区卫生服务站","ShortName":"上地街道东馨园社区卫生服务站","juli":"1201"},{"Yyid":"001008162154","Name":"北京市海淀区清河街道美和园社区卫生服务站","ShortName":"清河街道美和园社区卫生服务站","juli":"1962"},{"Yyid":"00100816200101","Name":"北京市海淀区上地街道上地社区卫生服务站","ShortName":"北京市上地街道上地社区卫生服务站","juli":"2102"},{"Yyid":"00100811101602","Name":"北京体育大学医院","ShortName":"北京体育大学医院","juli":"2117"},{"Yyid":"00100811101601","Name":"北京体育大学社区卫生服务中心","ShortName":"北京体育大学社区卫生服务中心","juli":"2117"},{"Yyid":"00100811002701","Name":"北京市海淀区清河社区卫生服务中心(北京市海淀区清河医院)","ShortName":"清河社区卫生服务中心(清河医院)","juli":"2213"}]}
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
         * Yyid : 00100816208701
         * Name : 北京市海淀区清河街道西二旗社区卫生服务站
         * ShortName : 清河街道西二旗社区卫生服务站
         * juli : 984
         */

        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String Yyid;
            private String Name;
            private String ShortName;
            private String juli;

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

            public String getJuli() {
                return juli;
            }

            public void setJuli(String juli) {
                this.juli = juli;
            }
        }
    }
}
