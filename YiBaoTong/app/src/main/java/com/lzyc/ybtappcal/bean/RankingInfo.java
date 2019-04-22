package com.lzyc.ybtappcal.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yang on 2017/03/24.
 */
public class RankingInfo implements Serializable{
    private String Vender;
    private String Name;
    private String Ranking;
    private String DrugNameID;
    private String GoodsName;
    private int cellPosition=-1;
    private int cellActionType=-1;
    private int drugPosition=0;
    private List<RankingDrugDetail> Detail;
    private boolean isCurrentItem;

    public String getVender() {
        return Vender;
    }

    public void setVender(String vender) {
        Vender = vender;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRanking() {
        return Ranking;
    }

    public void setRanking(String ranking) {
        Ranking = ranking;
    }

    public String getDrugNameID() {
        return DrugNameID;
    }

    public void setDrugNameID(String drugNameID) {
        DrugNameID = drugNameID;
    }

    public String getGoodsName() {
        return GoodsName;
    }

    public void setGoodsName(String goodsName) {
        GoodsName = goodsName;
    }

    public int getCellPosition() {
        return cellPosition;
    }

    public void setCellPosition(int cellPosition) {
        this.cellPosition = cellPosition;
    }

    public int getCellActionType() {
        return cellActionType;
    }

    public void setCellActionType(int cellActionType) {
        this.cellActionType = cellActionType;
    }

    public List<RankingDrugDetail> getRankingDrugDetailList() {
        return Detail;
    }

    public void setRankingDrugDetailList(List<RankingDrugDetail> rankingDrugDetailList) {
        this.Detail = rankingDrugDetailList;
    }

    public List<RankingDrugDetail> getDetail() {
        return Detail;
    }

    public void setDetail(List<RankingDrugDetail> detail) {
        Detail = detail;
    }

    public boolean isCurrentItem() {
        return isCurrentItem;
    }

    public void setCurrentItem(boolean currentItem) {
        isCurrentItem = currentItem;
    }

    public int getDrugPosition() {
        return drugPosition;
    }

    public void setDrugPosition(int drugPosition) {
        this.drugPosition = drugPosition;
    }
}
