package com.blanink.bean;

/**
 * 作者：Created by liangshen on 2018/1/11 0011 10:53
 * 邮箱：liangshen6868@163.com
 */
public class FileLoadEvent {
    long total;
    long bytesLoaded;

    public long getBytesLoaded() {
        return bytesLoaded;
    }

    public long getTotal() {
        return total;
    }

    public FileLoadEvent(long total, long bytesLoaded) {
        this.total = total;
        this.bytesLoaded = bytesLoaded;
    }

}
