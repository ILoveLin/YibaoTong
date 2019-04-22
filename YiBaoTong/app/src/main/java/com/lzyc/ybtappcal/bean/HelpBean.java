package com.lzyc.ybtappcal.bean;


/**
 * Created by yang on 17/07/06.
 */
public class HelpBean extends BaseBean{
    private String Title;
    private String Answer;
    private boolean isOpen=false;
    private int height;//保存item高度维持二次点击

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
