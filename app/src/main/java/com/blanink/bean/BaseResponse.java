package com.blanink.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/12.
 * 上传结果
 */

public class BaseResponse<T> implements Serializable {
    public String errorCode;
    public String reason;
    public T result;

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

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
