package com.lzyc.ybtappcal.bean;

/**
 * Create by yang
 * Ctrate at 2016/4/25
 */
public class TypeJobStatus extends BaseBean {
    private String desc;
    private String typeJob;
    private boolean isSelected;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTypeJob() {
        return typeJob;
    }

    public void setTypeJob(String typeJob) {
        this.typeJob = typeJob;
    }
}
