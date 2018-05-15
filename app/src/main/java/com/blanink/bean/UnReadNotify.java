package com.blanink.bean;

import java.io.Serializable;

/**
 * 作者：Created by liangshen on 2017/10/19 0019 14:59
 * 邮箱：liangshen6868@163.com
 */
public class UnReadNotify implements Serializable {

    /**
     * errorCode : 00000
     * reason : 获取数据成功！
     * result : 9
     */

    private String errorCode;
    private String reason;
    private long result;

    @Override
    public String toString() {
        return "UnReadNotify{" +
                "errorCode='" + errorCode + '\'' +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
