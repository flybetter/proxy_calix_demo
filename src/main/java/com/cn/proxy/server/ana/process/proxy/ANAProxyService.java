/** 
 * Project Name:socket_netty 
 * File Name:ANAProxyService.java 
 * Package Name:com.calix.bseries.server.ana.process 
 * Date:29 Nov, 2016
 * Copyright (c) 2016, Calix All Rights Reserved. 
 * 
 */
package com.cn.proxy.server.ana.process.proxy;


import com.cn.proxy.server.CacheConsts.IpRangeCache;
import com.cn.proxy.server.ana.ANAConstants;
import com.cn.proxy.server.ana.common.CommonDateTimeUtils;
import com.cn.proxy.server.ana.net.socket.ANATcpSocketClient;
import com.cn.proxy.server.domain.IpRange;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.Map.Entry;

/**
 * ClassName:ANAProxyService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 29 Nov, 2016 <br/>
 * 
 * @author Tony Ben
 * @version
 * @since JDK 1.6
 * @see
 */
public class ANAProxyService {
	private static final Logger log = Logger.getLogger(ANAProxyService.class);
	// cache all the device with CMS info
	public static Map<String, String> deviceNameMap = new Hashtable<String, String>();
	// cache all the client information
	public static Map<String, ANASocketClientInfo> clientMap = new Hashtable<String, ANASocketClientInfo>();
	// cache client connection information
	// private static List<ANATcpSocketClient> clientList = new
	// ArrayList<ANATcpSocketClient>();
	// LOCK
	private static String lock = "";

	/**
	 * Function:getClientByDeviceName<br/>
	 * 
	 * @author Tony Ben
	 * @param deviceName
	 * @return
	 * @since JDK 1.6
	 */
	public static ANASocketClientInfo getClientByDeviceName(String deviceName) {
		// if deviceName is empty ,return null
		if (StringUtils.isEmpty(deviceName)) {
			return null;
		}
		// initial deviceName
		deviceName = deviceName.startsWith(ANAConstants.CALIX_DEVICE_PREF) ? deviceName
				: ANAConstants.CALIX_DEVICE_PREF + deviceName;
		String key = deviceNameMap.get(deviceName);
		// device is not in cache
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		return clientMap.get(key);
	}

	/**
	 * Function:getAllClients<br/>
	 * 
	 * @author Tony Ben
	 * @return
	 * @since JDK 1.6
	 */
	public static List<ANASocketClientInfo> getAllClients() {
		List<ANASocketClientInfo> list = new ArrayList<ANASocketClientInfo>();
		Iterator<String> it = clientMap.keySet().iterator();
		while (it.hasNext()) {
			list.add(clientMap.get(it.next()));
		}
		it = null;
		return list;
	}

	/**
	 * Function:updateDeviceClient<br/>
	 * 
	 * @author Tony Ben
	 * @param deviceName
	 * @param client
	 * @since JDK 1.6
	 */
	public static void updateDeviceClient(String deviceName,
			ANATcpSocketClient client) {
		// initial deviceName
		deviceName = deviceName.startsWith(ANAConstants.CALIX_DEVICE_PREF) ? deviceName
				: ANAConstants.CALIX_DEVICE_PREF + deviceName;
		deviceNameMap.put(deviceName, client.getHost() + "-" + client.getPort()
				+ "-" + client.isSSL());
	}
	
	/**
	 * Function:turnUpTool: determine in which CMS to create VNE<br/>
	 * 
	 * @author Zheng Li
	 * @param deviceName
	 * @param client
	 * @since JDK 1.6
	 */
	public static String getCMS4CreateVNE() {
		Map<String, Integer> CMSClientsMap = new HashMap<String, Integer>();
		
		Iterator<Entry<String,String>> iterator = deviceNameMap.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String,String> entry = iterator.next();
			CMSClientsMap.put(entry.getValue(), (CMSClientsMap.get(entry.getValue())==null?0:CMSClientsMap.get(entry.getValue()))+1);
		}
		
		Iterator<Entry<String,Integer>> it = CMSClientsMap.entrySet().iterator();
		Integer currentMaxVNECount = 0;
		String currentMaxVNECMS = "";
		while(it.hasNext()){
			Entry<String,Integer> entry = it.next();
			if(entry.getValue() > currentMaxVNECount){
				currentMaxVNECount = entry.getValue();
				currentMaxVNECMS = entry.getKey();
			}
		}
		return currentMaxVNECMS.split("-")[0];
	}

	/**
	 *  get a cmsserver ip form iprangecache
	 * @return
	 */
	public static String getFirstFromIpRangeCache(){
		List<IpRange> ipRangeCacheList= IpRangeCache.ipRangeList;
		if(null!=ipRangeCacheList&&ipRangeCacheList.size()>0){
			return ipRangeCacheList.get(0).getCmsServerIp();
		}
		return null;
	}

	public static ANATcpSocketClient getOnlineClient() {
		Iterator<String> it = clientMap.keySet().iterator();
		ANATcpSocketClient onlineClient = null;
		while (it.hasNext()) {
			ANASocketClientInfo info = clientMap.get(it.next());
			onlineClient = info.getClient();
			if (onlineClient!=null&&onlineClient.connect()) {
				return onlineClient;
			}
		}
		return null;
	}

	/**
	 * Function:updateClients<br/>
	 * 
	 * @author Tony Ben
	 * @param map
	 * @since JDK 1.6
	 */
	public static void updateClients(Map<String, Map<String, String>> map) {
		if (map == null || map.isEmpty()) {
			return;
		}
		synchronized (lock) {
			List<String> removeList = new ArrayList<String>();
			// get client info ,with the relationship of client info and client
			// name
			Map<String, String> nameMap = new HashMap<String, String>();
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String name = it.next();
				Map<String, String> tmap = map.get(name);
				nameMap.put(tmap.get("ipaddress") + "-" + tmap.get("port")
						+ "-" + tmap.get("isSSL"), name);
			}
			// get remove unuse client
			it = clientMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String host = clientMap.get(key).getIpaddress() + "-"
						+ clientMap.get(key).getPort() + "-"
						+ clientMap.get(key).isSSL();
				if (nameMap.get(host) == null) {
					removeList.add(key);
				} else {
					nameMap.remove(key);
				}
			}
			// remove
			for (String key : removeList) {
				clientMap.remove(key);
			}
			// add new client
			it = nameMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String name = nameMap.get(key);
				String[] param = key.split("-");
				ANASocketClientInfo info = new ANASocketClientInfo(name,
						param[0], Integer.parseInt(param[1]),
						Boolean.parseBoolean(param[2]), true);
				clientMap.put(key, info);
			}
			it = null;
		}
	}

	/**
	 * Function:syncClientDevice<br/>
	 * Remark:TODO<br/>
	 * 
	 * @author Tony Ben
	 * @since JDK 1.6
	 */
	public static void syncClientDevice(boolean syncAll) {
		new SyncDeviceThread(syncAll).start();
	}

	/**
	 * Function:syncDeviceFromClients<br/>
	 * 
	 * @author Tony Ben
	 * @since JDK 1.6
	 */
	public static void syncDeviceFromClients(boolean syncAllFlg) {
		Map<String, String> tmpMap = new HashMap<String, String>();
		log.info("[ANA][syncDeviceFromClients]" + clientMap);
		Iterator<Entry<String, ANASocketClientInfo>> it = clientMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String,ANASocketClientInfo> entry = (Entry<String, ANASocketClientInfo>) it.next();
			// get client info
			ANASocketClientInfo info = entry.getValue();
			// if sync all device , set all client to needSyncDevice=true
			if (syncAllFlg) {
				info.setNeedSyncDevice(true);
			}
			// check whethere need sync device
			if (!info.getNeedSyncDeviceFlg()) {
				log.info(info + " not need sync device");
				continue;
			}
			// check sync time
			if (info.isSyncRuning()) {
				log.info(CommonDateTimeUtils.getDateTime()
						+ " Device Sync Failure,client is running sync devices, not need sync again");
				continue;
			}
			if (!info.isConnect()) {
				log.info(CommonDateTimeUtils.getDateTime()
						+ " Device Sync Failure,can't get connection with this server: "+info);
				continue;
			}
			info.setSyncRuning(true);
			info.setStartSyncTimeLong(System.currentTimeMillis());
			log.info("[ANA][syncDeviceFromClients]Begin sync device from "
					+ info);
			ANATcpSocketClient client = info.getClient();
			Map<String, String> tmap;

			String response = client.send("syncdevice");
			tmap = transDeviceListToMap(response, entry.getKey());

			tmpMap.putAll(tmap);
			info.setEndSyncTimeLong(System.currentTimeMillis());
			info.setSyncRuning(false);
			log.info(CommonDateTimeUtils.getDateTime()
					+ " Device Sync success, total sync " + tmap.size()
					+ " B6 devices from CMS:"+info);
			tmap = null;
		}
		it = null;
		// cache the data
		synchronized (lock) {
			deviceNameMap.clear();
			deviceNameMap.putAll(tmpMap);
			tmpMap = null;
		}
	}

	/**
	 * Function:transDeviceListToMap<br/>
	 * 
	 * @author Tony Ben
	 * @param response
	 * @param key
	 * @return
	 * @since JDK 1.6
	 */
	private static Map<String, String> transDeviceListToMap(String response,
			String key) {
		log.info("[sync device from " + key + "]\r\n" + response);
		Map<String, String> map = new HashMap<String, String>();
		if (!StringUtils.isBlank(response)) {
			for (String s : response.split(",")) {
				map.put(s, key);
			}
		}
		return map;
	}
}

/**
 * ClassName: SyncDeviceThread <br/>
 * Function: This Class is just for sync device with async <br/>
 * date: 30 Nov, 2016 <br/>
 * 
 * @author Tony Ben
 * @version
 * @since JDK 1.6
 */
class SyncDeviceThread extends Thread {
	private boolean syncAllFlg = false;

	public SyncDeviceThread(boolean syncAll) {
		this.syncAllFlg = syncAll;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ANAProxyService.syncDeviceFromClients(syncAllFlg);
	}
}