package com.cn.proxy.server.ana.common.result;

public class ProxyResult <T> {
	
	private boolean success;
	
	private T data;
	
	private String error;
	
	public static final boolean SUCCESS=true;
	
	public static final boolean FAILURE=false;


	public ProxyResult() {

	}

	public ProxyResult(boolean success, String error) {
		super();
		this.success = success;
		this.error = error;
	}
	
	
	public ProxyResult(boolean success, T data) {
		super();
		this.success = success;
		this.data = data;
	}


	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	
}
