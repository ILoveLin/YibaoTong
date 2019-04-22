package com.lzyc.ybtappcal.bean;

import android.view.View;

import java.io.Serializable;

/**
 * Created by yang on 2017/06/14.
 */

public class FoldingCellBean implements Serializable{
    private DrugBean drugBean;
    private View.OnClickListener requestBtnClickListener;

    public DrugBean getDrugBean() {
        return drugBean;
    }

    public void setDrugBean(DrugBean drugBean) {
        this.drugBean = drugBean;
    }

    public View.OnClickListener getRequestBtnClickListener() {
        return requestBtnClickListener;
    }

    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return false;

    }


}
