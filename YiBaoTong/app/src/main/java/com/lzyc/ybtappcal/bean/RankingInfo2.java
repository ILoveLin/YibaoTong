package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by yang on 2017/03/24.
 */
public class RankingInfo2 extends BaseBean{


    /**
     * data : {"drugList":[{"Name":"藿香正气水","drugImage":"http://app.yibaotongapp.com//pic/1606.png","DrugNameID":"ZA02AA000H0441","venderCountry":"Test vender country","Sell":"0","SpecificationsID":"002","Price":"14.37","Vender":"北京同仁堂科技发展股份有限公司制药厂","drug_id":"144","VenderID":"0010096","renkedu":"1","Specifications":"10ml*10/盒","GoodsName":""}]}
     * msg :
     * status : 0
     */

    private DataBean data;
    private String msg;
    private int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {
        private List<DrugBean> drugList;

        public List<DrugBean> getDrugList() {
            return drugList;
        }

        public void setDrugList(List<DrugBean> drugList) {
            this.drugList = drugList;
        }

    }
}
