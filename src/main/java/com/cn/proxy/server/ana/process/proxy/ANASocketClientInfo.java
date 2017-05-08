/** 
 * Project Name:sw_comps_Nms 
 * File Name:ANASocketClientInfo.java 
 * Package Name:com.calix.bseries.server.ana.process.proxy 
 * Date:7 Dec, 2016
 * Copyright (c) 2016, Calix All Rights Reserved. 
 * 
 */
package com.cn.proxy.server.ana.process.proxy;

import com.cn.proxy.server.ana.net.socket.ANATcpSocketClient;
import org.apache.log4j.Logger;

import java.util.Timer;
import java.util.TimerTask;


/**
 * ClassName:ANASocketClientInfo <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 7 Dec, 2016 <br/>
 * 
 * @author Tony Ben
 * @version
 * @since JDK 1.6
 * @see
 */
public class ANASocketClientInfo extends TimerTask {
	private static final Logger log = Logger
			.getLogger(ANASocketClientInfo.class);
	// IP address for client
	private String ipaddress;
	// port for socket
	private int port;
	private boolean isSSL = false;
	// socket connection status, true means connected, false means not connected
	private boolean socketState = false;
	// socket client
	private ANATcpSocketClient client;
	// sync device start time
	private long startSyncTimeLong = 0L;
	// sync device end time
	private long endSyncTimeLong = 0L;
	// sync result
	private boolean isSyncRuning = false;
	private boolean needSyncDevice = false;

	public ANASocketClientInfo(String name, String ipaddress, int port,
			boolean isSSL, boolean needSyncDevice) {
		this.ipaddress = ipaddress;
		this.port = port;
		this.isSSL = isSSL;
		this.needSyncDevice = needSyncDevice;
		client = new ANATcpSocketClient(ipaddress, port, isSSL);
		Timer timer = new Timer();
		timer.schedule(this, 1000, 1000*60);
	}

	public ANATcpSocketClient getNewClient() {
		ANATcpSocketClient client = null;
		client = new ANATcpSocketClient(ipaddress, port, isSSL);
		client.connect();
		return client;
	}

	public void setNeedSyncDevice(boolean needSyncDevice) {
		this.needSyncDevice = needSyncDevice;
	}

	public boolean getNeedSyncDeviceFlg() {
		return needSyncDevice;
	}

	public boolean isConnect() {
		return socketState;
	}

	public ANATcpSocketClient getClient() {
		return client;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
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

	@Override
	public void run() {
		if (client.isOnline()) {
			socketState = true;
			this.cancel();
			return;
		}
		if (client.connect()) {
			socketState = true;
			return;
		}
		socketState = false;
	}

	@Override
	public String toString() {
		return "ANASocketClientInfo [ipaddress=" + ipaddress + ", port=" + port
				+ ", isSSL=" + isSSL + ", socketState=" + socketState + "]";
	}

	public long getStartSyncTimeLong() {
		return startSyncTimeLong;
	}

	public void setStartSyncTimeLong(long startSyncTimeLong) {
		this.startSyncTimeLong = startSyncTimeLong;
	}

	public long getEndSyncTimeLong() {
		return endSyncTimeLong;
	}

	public void setEndSyncTimeLong(long endSyncTimeLong) {
		this.endSyncTimeLong = endSyncTimeLong;
	}

	public boolean isSyncRuning() {
		return isSyncRuning;
	}

	public void setSyncRuning(boolean isSyncRuning) {
		this.isSyncRuning = isSyncRuning;
	}

}
