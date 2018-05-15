package com.blanink.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/26.
 */

public class Attribute implements Serializable {
    public String id;

    @Override
    public String toString() {
        return "Attribute{" +
                "id='" + id + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
