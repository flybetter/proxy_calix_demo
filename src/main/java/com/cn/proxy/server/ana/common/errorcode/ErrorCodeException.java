package com.cn.proxy.server.ana.common.errorcode;

public class ErrorCodeException extends Exception {
	private int errorCode;
	private String errorMessage;
	private Exception e;

	public ErrorCodeException() {

	}

	public ErrorCodeException(int errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public ErrorCodeException(int errorCode, String errorMessage, Exception e) {
		this.e = e;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public ErrorCodeException(Exception e) {
		this.e = e;
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the e
	 */
	public Exception getE() {
		return e;
	}

	/**
	 * @param e
	 *            the e to set
	 */
	public void setE(Exception e) {
		this.e = e;
	}
}
