package com.cn.proxy.server.CacheConsts;


import com.cn.proxy.server.ana.common.CommonStringUtils;
import com.cn.proxy.server.ana.common.result.ProxyResult;
import com.cn.proxy.server.ana.process.proxy.ANAProxyService;
import com.cn.proxy.server.ana.process.proxy.ANASocketClientInfo;
import com.cn.proxy.server.domain.IpRange;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

@Component
public class IpRangeCache {
	
	public static Vector<IpRange> ipRangeList=new Vector<IpRange>();
	
	private final static String IPRANGELIST_IS_NULL="ipRangeList is null"; 
	
	private final static String IPRANGLIST_NOT_FIND="ipRangeList not find";
	
	private final static String IPXML_NOT_FIND="request not find b6 device ip";

	private final static String IP_ERROR="request ip not right";

	private final static String ANACLIENT_ERROR="ipRange CMS Server not in ClientMap ";

	@Value("${pathSettings}")
	public String path ;

	@PostConstruct
	public void run(){
		System.out.println(path);
	}
	
	
	public static ProxyResult<String> getCMSServerByIp(String ip){


		if(ipRangeList.size()==0){
			return new ProxyResult(ProxyResult.FAILURE,IPRANGELIST_IS_NULL);
		}
		
		for(IpRange iprange:ipRangeList){
			if(iprange.getIps().contains(ip)){
                ProxyResult proxyResult=new ProxyResult();
                proxyResult.setSuccess(ProxyResult.SUCCESS);
                proxyResult.setData(iprange.getCmsServerIp());
				return proxyResult;
			}
		}
		
		return new ProxyResult(ProxyResult.FAILURE,IPRANGLIST_NOT_FIND);
	}


	public static void addIpRange(IpRange ipRange) {
		IpRangeCache.ipRangeList.add(ipRange);
	}
	
	public static ProxyResult<String> getCMSServerIpByRequest(String request){

		String b6DeviceIp="";
        b6DeviceIp=IpRangeCache.getB6DeviceIp(request);

        if(!CommonStringUtils.checkIP(b6DeviceIp)){
            return new ProxyResult(ProxyResult.FAILURE,IP_ERROR);
        }
		
		if(b6DeviceIp!=null){
			ProxyResult proxyResult=IpRangeCache.getCMSServerByIp(b6DeviceIp);
			return proxyResult;
		}
		
		return new ProxyResult(ProxyResult.FAILURE,IPXML_NOT_FIND);
	}



	public static String getB6DeviceName(String request) {
		Document document = null;
		try {
			document = DocumentHelper.parseText(request);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Element root = document.getRootElement();
		Element info = root.element("param").element("value")
				.element("management.IElementManagement");

		String NetworkName = ((Element) info.elements("ElementName").get(0))
				.getText();
		if (NetworkName != null && !NetworkName.startsWith("NTWK-")) {
			NetworkName = "NTWK-" + NetworkName;
		}

		return NetworkName;
	}


	public static ProxyResult addClientMap(String request,String cmsServerIp){
	    String deviceName=IpRangeCache.getB6DeviceName(request);
        Map<String, ANASocketClientInfo> hashmap= ANAProxyService.clientMap;
        Iterator iterator=hashmap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry entry=( Map.Entry)iterator.next();
            if (entry.getKey().toString().contains(cmsServerIp)) {
                ANAProxyService.deviceNameMap.put(deviceName,entry.getKey().toString());
                ProxyResult proxyResult=new ProxyResult();
                proxyResult.setSuccess(ProxyResult.SUCCESS);
                proxyResult.setData(entry.getKey().toString());
                return proxyResult;
            }
        }
        return  new ProxyResult(ProxyResult.FAILURE,ANACLIENT_ERROR);

    }

	public static String getB6DeviceIp(String request){
        Document document = null;
        try {
            document = DocumentHelper.parseText(request);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        Element info = root.element("param").element("value")
                .element("management.IElementManagement");
        String req = ((Element) info.elements("ID").get(0)).getText();
        String IP = req.substring(req.indexOf("IP=") + 3, req.indexOf(")"));
        return IP;
	}

	public static ProxyResult checkWhetherIpIsAdd(String request,String username){
		String deviceName=IpRangeCache.getB6DeviceName(request);
		String deviceIp=IpRangeCache.getB6DeviceIp(request);
		Map<String,String> deviceNameMap=ANAProxyService.deviceNameMap;
		Iterator iterator=deviceNameMap.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry entry=( Map.Entry)iterator.next();
			if(entry.getKey().equals(deviceName)){
                String result="<?xml version=\"1.0\" ?><CONNECT FAILED><ResultCode>ip repeat</ResultCode><Message>Command completed successfully</Message><Data></Data><Input><Username>"+username+"</Username><Hostname>"+deviceName.replaceAll("NTWK-", "")+"</Hostname><HostIP>"+deviceIp+"</HostIP></Input></CONNECT FAILED>";
                ProxyResult proxyResult=new ProxyResult();
				proxyResult.setSuccess(ProxyResult.SUCCESS);
                proxyResult.setData(result);
				return proxyResult;
			}
		}
		return new ProxyResult(ProxyResult.FAILURE,"");
	}




}
