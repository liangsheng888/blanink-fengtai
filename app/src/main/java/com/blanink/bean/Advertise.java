package com.blanink.bean;

/**
 * Created by Administrator on 2017/9/18 0018.
 */

public class Advertise {
    private String photo;
    private String url;

    public Advertise(String photo, String url) {
        this.photo = photo;
        this.url = url;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
