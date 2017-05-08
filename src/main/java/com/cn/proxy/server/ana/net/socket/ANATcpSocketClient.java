/** 
 * Project Name:socket_netty 
 * File Name:ANATcpSocketClient.java 
 * Package Name:com.calix.bseries.server.ana.net.socket 
 * Date:29 Nov, 2016
 * Copyright (c) 2016, Calix All Rights Reserved. 
 * 
 */
package com.cn.proxy.server.ana.net.socket;

import com.cn.proxy.server.ana.ANAConstants;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.SecureRandom;



/**
 * ClassName:ANATcpSocketClient <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 29 Nov, 2016 <br/>
 * 
 * @author Tony Ben
 * @version
 * @since JDK 1.6
 * @see
 */
public class ANATcpSocketClient {
	// host ip or host name
	private String host;
	// socket port
	private int port;
	// whether is SSL security link
	private boolean isSSL = false;
	// IO send
	private PrintWriter out = null;
	// IO receive
	private BufferedReader in = null;
	// socket object
	private Socket cSocket = null;

	private static final Logger log = Logger
			.getLogger(ANATcpSocketClient.class);
	public ANATcpSocketClient(String host, int port, boolean ssl) {
		this.host = host;
		this.port = port;
		this.isSSL = ssl;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isSSL() {
		return isSSL;
	}

	public void setSSL(boolean isSSL) {
		this.isSSL = isSSL;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isOnline() {
		if(cSocket==null||out==null||in==null){
			return false;
		}
		return true;
	}

	public boolean connect() {
		/*if (isOnline()) {
			return true;
		}*/
		try {
			if (isSSL) {
				SSLContext context = SSLContext.getInstance("SSL");
				context.init(null,
						new TrustManager[] { new MyX509TrustManager() },
						new SecureRandom());
				SSLSocketFactory factory = context.getSocketFactory();
				cSocket = (SSLSocket) factory.createSocket(host, port);
			} else {
				cSocket = new Socket(host, port);
			}
			out = new PrintWriter(cSocket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(
					cSocket.getInputStream()));
		} catch (Exception e) {
			cSocket = null;
			return false;
		}
		return true;
	}

	public String send(String request) {
		try {
		out.println(request + "\n.\n");
		out.flush();
			return readResponse();
		} catch (IOException e) {
			log.info("socket occured exception",e);
			return ANAConstants.AnaErrorCode.CONNECTION_ERROR.getErrorMessage();
		}
		//return "";
	}

	private String readResponse() throws IOException {
		String line = null;
		StringBuffer response = new StringBuffer("");
		while ((line = in.readLine()) != null) {
			if (line != null && line.equals(".")) {
				break;
			}
			response.append(line);
		}
		return response.toString();
	}

	public boolean disconnect() {
		try {
			if (out != null) {
				out.close();
				out = null;
			}
			if (in != null) {
				in.close();
				in = null;
			}
			if (cSocket != null) {
				cSocket.close();
				cSocket = null;
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
