package com.blanink.bean;

/**
 * Created by Administrator on 2017/9/7 0007.
 */

public class DataSynRefreshEvent {
    private Boolean isRefresh;//是否刷新
    private int code;//刷新标识
    private String reason;

    public DataSynRefreshEvent(Boolean isRefresh, int code, String reason) {
        this.isRefresh = isRefresh;
        this.code = code;
        this.reason = reason;
    }

    public Boolean getRefresh() {
        return isRefresh;
    }

    public void setRefresh(Boolean refresh) {
        isRefresh = refresh;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
