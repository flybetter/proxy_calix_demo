package com.cn.calix.server.dto;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/5/15
 * Time: 下午2:11
 */
public class ProxyResult<T> {
    private boolean success;

    private T data;

    private String error;

    public static final boolean SUCCESS=true;

    public static final boolean FAIL=false;

    public ProxyResult() {
    }

    public ProxyResult(T data ,boolean success) {
        this.success = success;
        this.data = data;
    }

    public ProxyResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
