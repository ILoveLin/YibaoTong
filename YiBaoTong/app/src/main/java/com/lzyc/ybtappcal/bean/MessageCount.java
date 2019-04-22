package com.lzyc.ybtappcal.bean;

/**
 * 未读消息数量
 * Created by yang on 2016/9/21.
 */
public class MessageCount extends BaseBean{

    private int systemMessageCount;
    private int commentMessageCount;

    public int getSystemMessageCount() {
        return systemMessageCount;
    }

    public void setSystemMessageCount(int systemMessageCount) {
        this.systemMessageCount = systemMessageCount;
    }

    public int getCommentMessageCount() {
        return commentMessageCount;
    }

    public void setCommentMessageCount(int commentMessageCount) {
        this.commentMessageCount = commentMessageCount;
    }
}
