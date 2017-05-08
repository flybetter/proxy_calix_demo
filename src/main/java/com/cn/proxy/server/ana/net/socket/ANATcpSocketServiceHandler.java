package com.cn.proxy.server.ana.net.socket;

import com.cn.proxy.server.ana.ANAConstants;
import com.cn.proxy.server.ana.common.CommonStringUtils;
import com.cn.proxy.server.ana.process.ANAProcessResult;
import com.cn.proxy.server.ana.process.proxy.ANAProxyService;
import com.cn.proxy.server.ana.process.proxy.ANASocketClientInfo;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Handles a server-side channel.
 */
@Sharable
public class ANATcpSocketServiceHandler extends
		SimpleChannelInboundHandler<String> {
	private static final Logger log = Logger
			.getLogger(ANATcpSocketServiceHandler.class);
	private static final String LOGIN_PATTERN_STR = "user=(.*) password=(.*)";
	private static final Pattern LOGIN_PATTERN = Pattern
			.compile(LOGIN_PATTERN_STR);
	private static final String PARAM_PATTERN_STR = "Name=(.*)\\)";
	private static final Pattern PARAM_PATTERN = Pattern
			.compile(PARAM_PATTERN_STR);
	private static final String REGISTER_PATTERN_STR = "CMS:(.*);protocol:(.*);port:(.*)";
	private static final Pattern REGISTER_PATTERN = Pattern
			.compile(REGISTER_PATTERN_STR);

	// Session Info
	private static Map<String, ANAProcessResult> session = new HashMap<String, ANAProcessResult>();
	private static Map<String, ANATcpSocketClient> clientSession = new HashMap<String, ANATcpSocketClient>();
	private String response = "";

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String request)
			throws Exception {
		String key = ctx.channel().remoteAddress().toString();
		// ANA client process will send . and '' which do not know the reason
		// so we drop those requests
		if (".".equals(request.trim()) || "".equals(request.trim())) {
			ctx.write(request + "\n");
			return;
		}

		log.info("[ANA Socket][key]" + key + "[request]\r\n"
				+ formatXml(request));
		// sync device
		Matcher matcher = REGISTER_PATTERN.matcher(request);
		if (matcher.find()) {
			saveCMStoXML(matcher);
			return;
		}
		// add cache exception
		try {
			response = processForSocket(key, request);
			if (StringUtils.isEmpty(response)) {
				response = ANAConstants.AnaErrorCode
						.getErrorMessage(ANAConstants.AnaErrorCode.REQUEST_PATTERN_NOT_RIGHT
								.toString());
			}
		} catch (Exception e) {
			log.error("[ANA Socket][Exception]" + e.getMessage() + "\r\n"
					+ e.getCause() + "\r\n" + e.getStackTrace());
			response = ANAConstants.AnaErrorCode
					.getErrorMessage(ANAConstants.AnaErrorCode.UNKNOWN_ERROR
							.toString());
		} finally {
			ctx.write(response + "\n");
		}
	}

	private static void saveCMStoXML(Matcher matcher) {
		boolean saveResult = false;
//		File file = new File(ANAConstants.ROOT_DIR
//				+ ANAConstants.ANA_SETTING_FILE);
				File file = new File(ANAConstants.ANA_PATH);
		String CMSIP = matcher.group(1).trim();
		String protocol = matcher.group(2).trim();
		String port = matcher.group(3).trim();
		SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(file);
			Element root = document.getRootElement();
			Element common = root.element("common");
			if (common == null) {
				throw new NullPointerException(
						"the element of common is missing in " + file.getName());
			}
			List<Element> clients = root.element("clients").elements();
			Map<String, Map<String, String>> cmsClientMap = new HashMap<String, Map<String, String>>();
			for (Element e : clients) {
				if (CMSIP.equals(e.elementText("ipaddress"))) {
					saveResult = true;
					e.element("protocol").setText(protocol);
					e.element("port").setText(port);
				}
			}
			if (!saveResult) {
				Element client = DocumentHelper.createElement("client");
				Element ipElement = DocumentHelper.createElement("ipaddress");
				ipElement.setText(CMSIP);
				Element protocolElement = DocumentHelper
						.createElement("protocol");
				protocolElement.setText(protocol);
				Element portElement = DocumentHelper.createElement("port");
				portElement.setText(port);
				client.add(ipElement);
				client.add(protocolElement);
				client.add(portElement);
				root.element("clients").add(client);
			}
			XMLWriter writer = new XMLWriter(new FileWriter(file));
			writer.write(document);
			writer.close();

			Map<String, String> map = new HashMap<String, String>();
			map.put("ipaddress", CMSIP);
			map.put("isSSL", "1".equals(protocol)?"true":"false");
			map.put("port", port);
			cmsClientMap.put(CMSIP, map);
			ANAProxyService.updateClients(cmsClientMap);
			ANAProxyService.syncClientDevice(true);
			clients = null;
		} catch (Exception e) {
			log.error("saveCMStoXML failed;"+e);
		} finally {
			document = null;
			reader = null;
			file = null;
		}
	}

	private static String formatXml(String str) {
		if (str == null || !str.startsWith("<?xml")) {
			return str;
		}
		Document document = null;
		try {
			document = DocumentHelper.parseText(str);
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("utf-8");
			StringWriter writer = new StringWriter();
			XMLWriter xmlWriter = new XMLWriter(writer, format);
			xmlWriter.write(document);
			xmlWriter.close();
			return writer.toString();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	
	private String returnXml(String response) {
		if (response == null) {
			response = "";
		}
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("IMO");
		root.addAttribute("type", "IMO");
		root.addAttribute("instance_id", "0");

		Element book = root.addElement("Output");
		book.addAttribute("type", "String");
		book.addText(response);
		OutputFormat format = OutputFormat.createCompactFormat();
		StringWriter writer = new StringWriter();
		XMLWriter output = new XMLWriter(writer, format);
		try {
			output.write(doc);
			writer.close();
			output.close();
			return writer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * Function:processForSocket<br/>
	 * 
	 * @author Tony Ben
	 * @param key
	 * @param request
	 * @return
	 * @since JDK 1.6
	 */
	private String processForSocket(String key, String request) {
		ANAProcessResult result = session.get(key);
		// first check whether the client is auth successful, when true can do
		// others
		if (result == null && !checkLogin(request, key)) {
			return response;
		}
		if (!request.contains("<?xml")) {
			return "success.";
		}
		// get the request command type, there will be GetWorkflowOutput and
		// RunWorkflow, if not , will not handle
		String name = null;
		try {
			Document doc = DocumentHelper.parseText(request);
			Element rootElt = doc.getRootElement();
			name = rootElt.attributeValue("name");
			doc = null;
			rootElt = null;
		} catch (DocumentException e) {
			log.error(e);
		}

		// Request
		if (name == null || "".equals(name.trim())) {
			result.setErrorInfo(ANAConstants.AnaErrorCode.REQUEST_PATTERN_NOT_RIGHT);
			return result.getResultStr();
		}
		return processRemote(key, request, result, name);
	}

	/**
	 * For Porxy call Remote Client
	 * 
	 * @param key
	 * @param request
	 * @param result
	 * @param name
	 * @return
	 */
	private String processRemote(String key, String request,
			ANAProcessResult result, String name) {
		log.info("[ANA][Socket][" + key + "][" + name
				+ "]");
		//if RunWorkflow return NE_NOT_EXISTING,GetWorkflowOutput return NE_NOT_EXISTING directly.
		if(ANAConstants.AnaErrorCode.NE_NOT_EXISTING.getErrorMessage().equals(result.getResultStr())){
			return returnXml(ANAConstants.AnaErrorCode.NE_NOT_EXISTING.getErrorMessage());
		}
		ANATcpSocketClient cacheClient = clientSession.get(key);
		// when cached socket client, just send command
		if (cacheClient != null) {
			if (!cacheClient.isOnline()) {
				result.setErrorInfo(ANAConstants.AnaErrorCode.CONNECTION_ERROR);
				return result.getResultStr();
			}
			return cacheClient.send(request);

		}

		// get the connection and cache the errors
		String deviceName = null;
		// Run
		if ("RunWorkflow".equals(name.trim())) {
			// get deviceName
			deviceName = getDeviceHostName(request);
			// have authenticated at first request,this time needn't login
			request = "from_proxy_to_CMS:["+result.getUsername()+"]" + request;
			if (StringUtils.isEmpty(deviceName)) {
				result.setErrorInfo(ANAConstants.AnaErrorCode.NE_NOT_EXISTING);
				return result.getResultStr();
			}
			ANASocketClientInfo info = ANAProxyService
					.getClientByDeviceName(deviceName);
			// device is not in cache
			if (info == null) {
				log.info("[ANA][Socket][" + key
						+ "]Device[" + deviceName + "]Not Cached CMS");
				for (ANASocketClientInfo tmpc : ANAProxyService.getAllClients()) {
//					if (tmpc.isConnect()) {
						ANATcpSocketClient client = tmpc.getClient();
						if(!client.connect()){
							log.info("[ANA][Socket]["
									+ key
									+ tmpc
									+ " can not be connected");
							continue;
						}
						log.info("CMS:"+client.getHost()+" handle the request");
						String response = executeRunWorkFlowCommand(key,client, request);
						if (response != null
								&& !response
										.equals(ANAConstants.AnaErrorCode.NE_NOT_EXISTING
												.getErrorMessage())) {
							clientSession.put(key, client);
							ANAProxyService.updateDeviceClient(deviceName,
									client);
							return response;
						}
					/*} else {
						log.info("[ANA][Socket][" + key
								+ tmpc
								+ " is not connected, ignore send messages ");
					}*/
				}
				result.setErrorInfo(ANAConstants.AnaErrorCode.NE_NOT_EXISTING);
				return result.getResultStr();
			}
			ANATcpSocketClient client = info.getNewClient();
			// need to connect to create new socket connect
			if (!client.isOnline()) {
				result.setErrorInfo(ANAConstants.AnaErrorCode.CONNECTION_ERROR);
				return result.getResultStr();
			}
			clientSession.put(key, client);
			log.info("CMS:"+client.getHost()+" handle the request");
			return executeRunWorkFlowCommand(key, client, request);
		}

		result.setErrorInfo(ANAConstants.AnaErrorCode.CONNECTION_ERROR);
		return result.getResultStr();
	}

	private String executeRunWorkFlowCommand(String key ,ANATcpSocketClient client,String request) {
		String response = "";
		String userIp = key.substring(1,key.indexOf(":"));
		response = client.send(request+"[CMS_clientIp:"+userIp+"]");
		log.info("[ANA][CMS:][" + client.getHost()+":"+client.getPort()
				+ ",response:\r\n"+response);
		return response;
	}

	private String getDeviceHostName(String request) {
		try {
			Document doc = DocumentHelper.parseText(request);
			Element root = doc.getRootElement();
			List<Element> list = root.elements("param");
			for (Element e : list) {
				String name = e.attributeValue("name").trim();
				// TemplateName
				if ("workflowAttributes".equals(name)) {
					List<Element> elist = e.elements();
					for (Element ee : elist) {
						if ("value".equals(ee.getName().toLowerCase())) {
							List<Element> vlist = ee.elements();
							for (Element eee : vlist) {
								String id = nameFind(eee.elementText("ID"));
								if (id.equalsIgnoreCase("device_host_name")) {
									return eee.elementText("Value");
								}
							}
						}
					}
				}
			}
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	private static String nameFind(String name) {
		if (CommonStringUtils.isEmpty(name)) {
			return name;
		}
		Matcher m = PARAM_PATTERN.matcher(name);
		if (m.find()) {
			return m.group(1);
		}
		return name;
	}

	/**
	 * Function:checkLogin<br/>
	 * 
	 * @author Tony Ben
	 * @param request
	 * @param key
	 * @return
	 * @since JDK 1.6
	 */
	private boolean checkLogin(String request, String key) {
		if (session.get(key) != null) {
			return true;
		}
		if (request != null && !request.trim().isEmpty()) {
			Matcher matcher = LOGIN_PATTERN.matcher(request);
			if (matcher.find()) {
				String userName = matcher.group(1).trim();
				ANATcpSocketClient client = ANAProxyService.getOnlineClient();
				if (client != null) {
					String msg = client.send(request);
					client.disconnect();
					if (msg.contains("success")) {
						response = msg;
						session.put(key, new ANAProcessResult(userName, key.split(":")[0]
								.substring(1), null));
						return true;
					}
				}
			}
		}
		session.remove(key);
		response = ANAConstants.AnaErrorCode.LOGIN_NOT_SUCCESSFUL
				.getErrorMessage() + ".\r\n";
		return false;
	}

	/**
	 * Function:nameFind<br/>
	 * 
	 * @author Tony Ben
	 * @return
	 * @since JDK 1.6
	 */

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		sessionClose(ctx);
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		sessionClose(ctx);
		super.channelInactive(ctx);
	}

	/**
	 * Function:sessionClose<br/>
	 * 
	 * @author Tony Ben
	 * @param ctx
	 * @since JDK 1.6
	 */
	private void sessionClose(ChannelHandlerContext ctx) {
		String key = ctx.channel().remoteAddress().toString();
		session.remove(key);
		ANATcpSocketClient client = clientSession.get(key);
		if (client != null) {
			client.disconnect();
			client = null;
		}
		clientSession.remove(key);
	}
}
