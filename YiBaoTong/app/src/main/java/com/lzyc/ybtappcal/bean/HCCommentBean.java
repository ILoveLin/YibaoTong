package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by lovelin on 16/8/17.
 */
public class HCCommentBean extends BaseBean {
    /**
     * status : 0
     * msg :
     * data : {"header":{"count":7,"average":"3.9","drugMany":"3.8","serviceAttitude":"3.8","getDrugConvenience":"4.1"},"list":[{"id":"84","yyid":"00100811010101","content":"o阿","nickname":"18270720637","phone":"18270720637","average2":"4.3","date":"2016-08-17"},{"id":"83","yyid":"00100811010101","content":"o阿","nickname":"18270720637","phone":"18270720637","average2":"4.3","date":"2016-08-17"},{"id":"14","yyid":"00100811010101","content":"这","nickname":"137180立体","phone":"13718081106","average2":"5.0","date":"2016-08-17"},{"id":"8","yyid":"00100811010101","content":"我","nickname":"137180立体","phone":"13718081106","average2":"5.0","date":"2016-08-17"},{"id":"6","yyid":"00100811010101","content":"呃呃呃","nickname":"18270720637","phone":"18270720637","average2":"3.3","date":"2016-08-17"},{"id":"5","yyid":"00100811010101","content":"一起走过的路","nickname":"137180立体","phone":"13718081106","average2":"2.8","date":"2016-08-17"},{"id":"4","yyid":"00100811010101","content":"我们都会有些不","nickname":"137180立体","phone":"13718081106","average2":"2.7","date":"2016-08-17"}]}
     */

    private int status;
    private String msg;
    /**
     * header : {"count":7,"average":"3.9","drugMany":"3.8","serviceAttitude":"3.8","getDrugConvenience":"4.1"}
     * list : [{"id":"84","yyid":"00100811010101","content":"o阿","nickname":"18270720637","phone":"18270720637","average2":"4.3","date":"2016-08-17"},{"id":"83","yyid":"00100811010101","content":"o阿","nickname":"18270720637","phone":"18270720637","average2":"4.3","date":"2016-08-17"},{"id":"14","yyid":"00100811010101","content":"这","nickname":"137180立体","phone":"13718081106","average2":"5.0","date":"2016-08-17"},{"id":"8","yyid":"00100811010101","content":"我","nickname":"137180立体","phone":"13718081106","average2":"5.0","date":"2016-08-17"},{"id":"6","yyid":"00100811010101","content":"呃呃呃","nickname":"18270720637","phone":"18270720637","average2":"3.3","date":"2016-08-17"},{"id":"5","yyid":"00100811010101","content":"一起走过的路","nickname":"137180立体","phone":"13718081106","average2":"2.8","date":"2016-08-17"},{"id":"4","yyid":"00100811010101","content":"我们都会有些不","nickname":"137180立体","phone":"13718081106","average2":"2.7","date":"2016-08-17"}]
     */

    private hcData data;

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

    public hcData getData() {
        return data;
    }

    public void setData(hcData data) {
        this.data = data;
    }

    public static class hcData {
        /**
         * count : 7
         * average : 3.9
         * drugMany : 3.8
         * serviceAttitude : 3.8
         * getDrugConvenience : 4.1
         */

        private HeaderBean header;
        /**
         * id : 84
         * yyid : 00100811010101
         * content : o阿
         * nickname : 18270720637
         * phone : 18270720637
         * average2 : 4.3
         * date : 2016-08-17
         */

        private List<HcDataListBean> list;

        public HeaderBean getHeader() {
            return header;
        }

        public void setHeader(HeaderBean header) {
            this.header = header;
        }

        public List<HcDataListBean> getList() {
            return list;
        }

        public void setList(List<HcDataListBean> list) {
            this.list = list;
        }

        public static class HeaderBean {
            private int count;
            private String average;
            private String drugMany;
            private String serviceAttitude;
            private String getDrugConvenience;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getAverage() {
                return average;
            }

            public void setAverage(String average) {
                this.average = average;
            }

            public String getDrugMany() {
                return drugMany;
            }

            public void setDrugMany(String drugMany) {
                this.drugMany = drugMany;
            }

            public String getServiceAttitude() {
                return serviceAttitude;
            }

            public void setServiceAttitude(String serviceAttitude) {
                this.serviceAttitude = serviceAttitude;
            }

            public String getGetDrugConvenience() {
                return getDrugConvenience;
            }

            public void setGetDrugConvenience(String getDrugConvenience) {
                this.getDrugConvenience = getDrugConvenience;
            }
        }

        public static class HcDataListBean extends BaseBean {
            private String id;
            private String yyid;
            private String content;
            private String nickname;
            private String phone;
            private String average2;
            private String date;
            private boolean isLoadding;//false 默认没有加载
            private boolean isEmptyView;
            private String headImg;

            public String getHeadImg() {
                return headImg;
            }

            public void setHeadImg(String headImg) {
                this.headImg = headImg;
            }

            public boolean isEmptyView() {
                return isEmptyView;
            }

            public void setEmptyView(boolean emptyView) {
                isEmptyView = emptyView;
            }

            public String getParaiseStatus() {
                return paraiseStatus;
            }

            public void setParaiseStatus(String paraiseStatus) {
                this.paraiseStatus = paraiseStatus;
            }

            private boolean isEmpty;//false 默认不是空
            private String replycount;
            private String praisecount;
            private String paraiseStatus;

            private boolean isClickLike;
            private boolean isClickCommunity;


            public boolean isClickCommunity() {
                return isClickCommunity;
            }

            public void setClickCommunity(boolean clickCommunity) {
                isClickCommunity = clickCommunity;
            }

            public boolean isClickLike() {
                return isClickLike;
            }

            public void setClickLike(boolean clickLike) {
                isClickLike = clickLike;
            }

            public String getReplycount() {
                return replycount;
            }

            public void setReplycount(String replycount) {
                this.replycount = replycount;
            }

            public String getPraisecount() {
                return praisecount;
            }

            public void setPraisecount(String praisecount) {
                this.praisecount = praisecount;
            }

            @Override
            public String toString() {
                return "HcDataListBean{" +
                        "id='" + id + '\'' +
                        ", yyid='" + yyid + '\'' +
                        ", content='" + content + '\'' +
                        ", nickname='" + nickname + '\'' +
                        ", phone='" + phone + '\'' +
                        ", average2='" + average2 + '\'' +
                        ", date='" + date + '\'' +
                        ", isLoadding=" + isLoadding +
                        ", isEmpty=" + isEmpty +
                        '}';
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getYyid() {
                return yyid;
            }

            public void setYyid(String yyid) {
                this.yyid = yyid;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getAverage2() {
                return average2;
            }

            public void setAverage2(String average2) {
                this.average2 = average2;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public boolean isLoadding() {
                return isLoadding;
            }

            public void setLoadding(boolean loadding) {
                isLoadding = loadding;
            }

            public boolean isEmpty() {
                return isEmpty;
            }

            public void setEmpty(boolean empty) {
                isEmpty = empty;
            }
        }
    }
}
