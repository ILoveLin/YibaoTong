package com.lzyc.ybtappcal.bean;

/**
 * 扫描，搜索记录
 * package_name com.lzyc.ybtappcal.bean
 * Created by yang on 2016/5/20.
 */
public class HistoryCache extends BaseBean {
    public static final int TYPE_CACHE_SCAN=0;
    public static final int TYPE_CACHE_SEARCH=1;
    public static final int TYPE_CACHE_CITY=1;
    private String id;
    private String name;
    private String goodsName;
    private String code;
    private String imageUrl;    //扫描记录图片的url
    private int cacheType;
    private long createTime;//历史记录创建时间
    private boolean isEmptyView=false;//用来设置空view的
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCacheType() {
        return cacheType;
    }

    public void setCacheType(int cacheType) {
        this.cacheType = cacheType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isEmptyView() {
        return isEmptyView;
    }

    public void setEmptyView(boolean emptyView) {
        isEmptyView = emptyView;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
