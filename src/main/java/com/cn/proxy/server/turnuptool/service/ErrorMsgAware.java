package com.cn.proxy.server.turnuptool.service;

public interface ErrorMsgAware {

	public final static String SUCCESS = "success";
	public final static String IP_INVALID_ = "IP is invalid";
	public final static String IP_EXISTED = "IP Already exists in CMS";
	public final static String NETWORKNAME_EXISTED = "NetWorkName Already exists in CMS";
	public final static String VNE_NOT_EXISTED = "VNE not exists in CMS";
	public final static String SERVICE_INTERNAL_ERROR = "service internal error,";
}
