package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by lovelin on 16/8/29.
 */
public class CommunityReplyBean extends BaseBean {


    /**
     * status : null
     * msg :
     * data : {"list":[{"id":"8","content":"你","phone":"13718081106","add_date":"2016-08-19","nickname":"13718081106","p_nickname":"15313058170"},{"id":"7","content":"我","phone":"13718081106","add_date":"2016-08-19","nickname":"13718081106","p_nickname":"15313058170"}],"count":2}
     */

    private Object status;
    private String msg;
    /**
     * list : [{"id":"8","content":"你","phone":"13718081106","add_date":"2016-08-19","nickname":"13718081106","p_nickname":"15313058170"},{"id":"7","content":"我","phone":"13718081106","add_date":"2016-08-19","nickname":"13718081106","p_nickname":"15313058170"}]
     * count : 2
     */

    private DataBean data;

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
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
         * id : 8
         * content : 你
         * phone : 13718081106
         * add_date : 2016-08-19
         * nickname : 13718081106
         * p_nickname : 15313058170
         */

        private HeaderData header;
        private List<ListBean> list;
        private String msg;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public HeaderData getHeader() {
            return header;
        }

        public void setHeader(HeaderData header) {
            this.header = header;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class HeaderData {
            private String comment_id;
            private String yyid;
            private String content;
            private String phone;
            private String date;
            private String replyCount;
            private String praiseCount;
            private String average;
            private String praiseStatus;
            private String nickname;
            private String headImg;

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getHeadImg() {
                return headImg;
            }

            public void setHeadImg(String headImg) {
                this.headImg = headImg;
            }

            @Override
            public String toString() {
                return "HeaderData{" +
                        "comment_id='" + comment_id + '\'' +
                        ", yyid='" + yyid + '\'' +
                        ", content='" + content + '\'' +
                        ", phone='" + phone + '\'' +
                        ", date='" + date + '\'' +
                        ", replyCount='" + replyCount + '\'' +
                        ", praiseCount='" + praiseCount + '\'' +
                        ", average='" + average + '\'' +
                        ", praiseStatus='" + praiseStatus + '\'' +
                        ", p_nickname='" + nickname + '\'' +
                        '}';
            }

            public String getP_nickname() {
                return nickname;
            }

            public void setP_nickname(String nickname) {
                this.nickname = nickname;
            }

            public String getPraiseStatus() {
                return praiseStatus;
            }

            public void setPraiseStatus(String praiseStatus) {
                this.praiseStatus = praiseStatus;
            }

            public String getComment_id() {
                return comment_id;
            }

            public void setComment_id(String comment_id) {
                this.comment_id = comment_id;
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

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getReplyCount() {
                return replyCount;
            }

            public void setReplyCount(String replyCount) {
                this.replyCount = replyCount;
            }

            public String getPraiseCount() {
                return praiseCount;
            }

            public void setPraiseCount(String praiseCount) {
                this.praiseCount = praiseCount;
            }

            public String getAverage() {
                return average;
            }

            public void setAverage(String average) {
                this.average = average;
            }
        }

        public static class ListBean {
            private String id;
            private String content;
            private String phone;
            private String add_date;
            private String nickname;
            private String p_nickname;

            public String getHeadImg() {
                return headImg;
            }

            public void setHeadImg(String headImg) {
                this.headImg = headImg;
            }

            private String headImg;


            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getAdd_date() {
                return add_date;
            }

            public void setAdd_date(String add_date) {
                this.add_date = add_date;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getP_nickname() {
                return p_nickname;
            }

            public void setP_nickname(String p_nickname) {
                this.p_nickname = p_nickname;
            }

            @Override
            public String toString() {
                return "ListBean{" +
                        "id='" + id + '\'' +
                        ", content='" + content + '\'' +
                        ", phone='" + phone + '\'' +
                        ", add_date='" + add_date + '\'' +
                        ", nickname='" + nickname + '\'' +
                        ", p_nickname='" + p_nickname + '\'' +
                        '}';
            }
        }
    }
}
