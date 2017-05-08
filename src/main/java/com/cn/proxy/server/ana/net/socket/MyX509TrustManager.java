/** 
 * Project Name:ana_client 
 * File Name:MyX509TrustManager.java 
 * Package Name:com.fairpoint.tools.asap.client 
 * Date:1 Nov, 2016
 * Copyright (c) 2016, Calix All Rights Reserved. 
 * 
 */
package com.cn.proxy.server.ana.net.socket;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * ClassName:MyX509TrustManager <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 1 Nov, 2016 <br/>
 * 
 * @author Tony Ben
 * @version
 * @since JDK 1.6
 * @see
 */
public class MyX509TrustManager implements X509TrustManager {

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}

}
