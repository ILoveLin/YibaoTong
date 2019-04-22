package com.lzyc.ybtappcal.bean;

import java.io.Serializable;

/**
 * Created by yang on 2016/4/29.
 */
public class Version implements Serializable{

    public static final int OS_ANDROID = 1000;

    private static final Long serialVersionUID = 1L;
    private long versionId;
    private String packageName;
    private String versionCode;
    private String versionName;
    private String appUrl;
    private Long createTime;
    private Long lastModifyTime;
    private String description;
    private Integer os = OS_ANDROID;
    private String checkSum;
    private Long appId;
    private int forced;
    private int minVersionCode;
    private boolean isLoadding;//是否在下载

    public boolean isLoadding() {
        return isLoadding;
    }

    public void setLoadding(boolean loadding) {
        isLoadding = loadding;
    }

    private static final long CHANNEL_APP_DEFAULT=10000L;
    public long getAppId() {
        return appId == null ? CHANNEL_APP_DEFAULT: appId.longValue();
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }
    public long getVersionId() {
        return versionId;
    }

    public void setVersionId(long versionId) {
        this.versionId = versionId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public long getCreateTime() {
        return createTime == null ? 0 : createTime.longValue();
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLastModifyTime() {
        return lastModifyTime == null ? 0 : lastModifyTime.longValue();
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOs() {
        return os == null ? OS_ANDROID : os.intValue();
    }

    public void setOs(int os) {
        this.os = os;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public static int getOsAndroid() {
        return OS_ANDROID;
    }

    public static Long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setLastModifyTime(Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public void setOs(Integer os) {
        this.os = os;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public static long getChannelAppDefault() {
        return CHANNEL_APP_DEFAULT;
    }

    public int getForced() {
        return forced;
    }

    public void setForced(int forced) {
        this.forced = forced;
    }

    public int getMinVersionCode() {
        return minVersionCode;
    }

    public void setMinVersionCode(int minVersionCode) {
        this.minVersionCode = minVersionCode;
    }

    @Override
    public String toString() {
        return "Version{" +
                "versionId=" + versionId +
                ", packageName='" + packageName + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", versionName='" + versionName + '\'' +
                ", appUrl='" + appUrl + '\'' +
                ", createTime=" + createTime +
                ", lastModifyTime=" + lastModifyTime +
                ", description='" + description + '\'' +
                ", os=" + os +
                ", checkSum='" + checkSum + '\'' +
                ", appId=" + appId +
                '}';
    }
}
