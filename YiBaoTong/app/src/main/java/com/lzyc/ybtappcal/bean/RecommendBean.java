package com.lzyc.ybtappcal.bean;

/**
 * @author yang
 */
public class RecommendBean extends BaseBean{

    private String id;
    private String content;
    private String images;
    private String title;
    private String ctime;
    private String url;
    private String intro;
    private String readnum;

    public RecommendBean(String id, String content, String images, String title, String ctime, String url, String intro) {
        this.id = id;
        this.content = content;
        this.images = images;
        this.title = title;
        this.ctime = ctime;
        this.url = url;
        this.intro = intro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getReadnum() {
        return readnum;
    }

    public void setReadnum(String readnum) {
        this.readnum = readnum;
    }
}
