package com.lzyc.ybtappcal.bean;

/**
 * Created by yang on 2016/9/18.
 */
public class UPushBean extends BaseBean{
    private String msg_id;
    private long random_min;
    private String body;
    private String extra;

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public long getRandom_min() {
        return random_min;
    }

    public void setRandom_min(long random_min) {
        this.random_min = random_min;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "UPushBean{" +
                "msg_id='" + msg_id + '\'' +
                ", random_min=" + random_min +
                ", body='" + body + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }
}
