package com.blanink.bean;

/**
 * 作者：Created by liangshen on 2017/11/7 0007 11:51
 * 邮箱：liangshen6868@163.com
 */
public class ResponseAllocation {
    private String errorCode;
    private String result;
    private String reason;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
