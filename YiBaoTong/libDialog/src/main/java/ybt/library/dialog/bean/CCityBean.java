package ybt.library.dialog.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 二级城市
 * Created by yang on 2017/01/05.
 */
public class CCityBean implements Serializable{
    private String city;
    private String city_id;
    private List<CCountysBean> countys;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public List<CCountysBean> getCountys() {
        return countys;
    }

    public void setCountys(List<CCountysBean> countys) {
        this.countys = countys;
    }
}
