package ybt.library.dialog.bean;

import java.io.Serializable;

/**
 * 县级城市
 * Created by yang on 2017/01/05.
 */
public class CCountysBean implements Serializable {
    private String county;
    private String county_id;

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCounty_id() {
        return county_id;
    }

    public void setCounty_id(String county_id) {
        this.county_id = county_id;
    }
}
