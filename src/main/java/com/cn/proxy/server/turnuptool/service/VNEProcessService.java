package com.cn.proxy.server.turnuptool.service;

import com.cn.proxy.server.CacheConsts.IpRangeCache;
import com.cn.proxy.server.ana.ANAConstants;
import com.cn.proxy.server.ana.common.result.ProxyResult;
import com.cn.proxy.server.ana.net.socket.ANATcpSocketClient;
import com.cn.proxy.server.ana.process.proxy.ANAProxyService;
import com.cn.proxy.server.ana.process.proxy.ANASocketClientInfo;
import com.cn.proxy.server.turnuptool.TurnUptoolService;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.StringReader;



public class VNEProcessService implements ErrorMsgAware {
	private static final Logger log = Logger.getLogger(TurnUptoolService.class);
	private static final VNEProcessService instance = new VNEProcessService();

	public static VNEProcessService getInstance() {
		return instance;
	}

	/**
	 * add ip range function, create b6 device  you need check which cms though ip range.
     * the result of add b6 device is success, you need add this b6 device to the device
	 * @param request
	 * @param userName
	 * @return
	 */
	public String createVNE(String request,String userName) {

		/**
		 * check whether the ip is added,if added return the ip has been add response
		 */
//		ProxyResult<String> ipCheckProxyResult=IpRangeCache.checkWhetherIpIsAdd(request,userName);
//		if(ipCheckProxyResult.isSuccess()){
//			return ipCheckProxyResult.getData();
//		}

		ProxyResult<String> proxyResult= IpRangeCache.getCMSServerIpByRequest(request);
		String CMSIP=null;
		if(proxyResult.isSuccess()){
			CMSIP=proxyResult.getData();
		}else{
			log.info("Ip range not work,reason:"+proxyResult.getError());
			CMSIP = ANAProxyService.getFirstFromIpRangeCache();
			if(CMSIP==null){
				log.info(" have no ip range in ProxySettings.xml");
			}
//			CMSIP=ANAProxyService.getCMS4CreateVNE();
		}
		ANATcpSocketClient client = new ANATcpSocketClient(CMSIP, ANAConstants.TURNUPTOOL_DEFAULT_SOCKET_PORT,
				ANAConstants.TURNUPTOOL_DEFAULT_ISSSL);
		client.connect();
		log.info("b6 device ip:"+IpRangeCache.getB6DeviceIp(request)+" to CMS ip:"+CMSIP);
		String result = client.send("from_proxy_to_CMS:["+userName+"]"+request);
        if (result.contains("success")) {
            proxyResult=IpRangeCache.addClientMap(request,CMSIP);
            if (!proxyResult.isSuccess()){
                log.error("add ClientMap error:"+proxyResult.getError());
            }
        }
		client.disconnect();
		return result;
	}
	

	public String deleteVNE(String request,String userName) {
		SAXReader reader = new SAXReader();
		String networkName = "";
		try {
			Document document = reader.read(new StringReader(request));
			Element root = document.getRootElement();
			String req = root.element("param").element("value").getText();
			networkName = "NTWK-"
					+ req.substring(req.indexOf("Key=") + 4,
							req.lastIndexOf(")"));
			ANASocketClientInfo info = ANAProxyService
					.getClientByDeviceName(networkName);
			if(info == null){
				log.info("[TurnUpTool]Device[" + networkName + "]Not Cached CMS");
				String result = "";
				for (ANASocketClientInfo tmpc : ANAProxyService.getAllClients()) {
					ANATcpSocketClient client = new ANATcpSocketClient(
							tmpc.getIpaddress(),
							ANAConstants.TURNUPTOOL_DEFAULT_SOCKET_PORT,
							ANAConstants.TURNUPTOOL_DEFAULT_ISSSL);
					client.connect();
					result = client.send("from_proxy_to_CMS:["+userName+"]"+request);
					client.disconnect();
					if("VNE not exists in CMS".equals(result)){
						continue;
					}else{
						return result;
					}
				}
				return "VNE not exists in CMS";
			}
			ANATcpSocketClient client = new ANATcpSocketClient(info.getIpaddress(),
					ANAConstants.TURNUPTOOL_DEFAULT_SOCKET_PORT,
					ANAConstants.TURNUPTOOL_DEFAULT_ISSSL);
			client.connect();
			String result = client.send("from_proxy_to_CMS:["+userName+"]"+request);
			client.disconnect();
			return result;
		} catch (Exception e) {
			log.error("Failed to delete VNE: " + networkName, e);
			return SERVICE_INTERNAL_ERROR + e.getMessage();
		}
	}
}