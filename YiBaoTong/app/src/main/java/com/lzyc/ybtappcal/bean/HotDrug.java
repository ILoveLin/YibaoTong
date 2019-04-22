package com.lzyc.ybtappcal.bean;

/**
 * 热门药品
 * Created by lovelin on 2016/5/19.
 */
public class HotDrug extends BaseBean {
    private String drug_name;
    private String id;
    private String is_rec;
    private String num;
    private int colorIndex;

    public String getDrug_name() {
        return drug_name;
    }

    public void setDrug_name(String drug_name) {
        this.drug_name = drug_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_rec() {
        return is_rec;
    }

    public void setIs_rec(String is_rec) {
        this.is_rec = is_rec;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getColorIndex() {
        return colorIndex;
    }

    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    @Override
    public String toString() {
        return "HotHistoryBean{" +
                "drug_name='" + drug_name + '\'' +
                ", id='" + id + '\'' +
                ", is_rec='" + is_rec + '\'' +
                ", num='" + num + '\'' +
                ", colorIndex=" + colorIndex +
                '}';
    }
}
