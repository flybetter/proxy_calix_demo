package com.cn.proxy.server.turnuptool.socket;


import com.cn.proxy.server.ana.ANAConstants;
import com.cn.proxy.server.ana.net.socket.ANATcpSocketClient;
import com.cn.proxy.server.ana.process.ANAProcessResult;
import com.cn.proxy.server.ana.process.proxy.ANAProxyService;
import com.cn.proxy.server.turnuptool.service.VNEProcessService;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles a server-side channel.
 */
@Sharable
public class VNETcpSocketServiceHandler extends
		SimpleChannelInboundHandler<String> {
	private static final Logger log = Logger
			.getLogger(VNETcpSocketServiceHandler.class);
	private static final String LOGIN_PATTERN_STR = "user=(.*) password=(.*)";
	private static final Pattern LOGIN_PATTERN = Pattern
			.compile(LOGIN_PATTERN_STR);

	private static Map<String, ANAProcessResult> session = new HashMap<String, ANAProcessResult>();
	private String response = "";

	public void channelRead0(ChannelHandlerContext ctx, String request)
			throws Exception {
		String key = ctx.channel().remoteAddress().toString();
		log.info("[key]" + key + "[request]" + request);
		if ((".".equals(request.trim())) || ("".equals(request.trim()))) {
			log.info("[key]" + key + "[response]" + request + "\n");
			ctx.write(request + "\n");
			return;
		}
		String response = "";
		response = processForSocket(key, request);
		if (StringUtils.isEmpty(response)) {
			response = ANAConstants.AnaErrorCode.getErrorMessage(
					ANAConstants.AnaErrorCode.REQUEST_PATTERN_NOT_RIGHT
							.toString(), new Object[0]);
		}

		log.info("[key]" + key + "[response]" + response);
		ctx.write(response + "\n");
	}

	private String processForSocket(String key, String request) {
		ANAProcessResult result = (ANAProcessResult) session.get(key);
		if ((result == null) && (!checkLoginFromCMS(request, key))) {
			return this.response;
		}
		if (!request.contains("</param>")) {
			return "success.";
		}
		String name = null;
		if (request.endsWith("."))
			request = request.substring(0, request.lastIndexOf("."));
		try {
			Document doc = DocumentHelper.parseText(request);
			Element rootElt = doc.getRootElement();
			name = rootElt.attributeValue("name");
			doc = null;
			rootElt = null;
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		if ((name == null) || ("".equals(name.trim()))) {
			return ANAConstants.AnaErrorCode.getErrorMessage(
					ANAConstants.AnaErrorCode.REQUEST_PATTERN_NOT_RIGHT
							.toString(), new Object[0]);
		}
		String responseStr = "";
		if ("Create".equals(name.trim())) {
			responseStr = VNEProcessService.getInstance().createVNE(request,result.getUsername());
		} else if ("Delete".equals(name.trim())) {
			responseStr = VNEProcessService.getInstance().deleteVNE(request,result.getUsername());
		}
		ANAProcessResult result1 = new ANAProcessResult(result.getUsername(),
				result.getIpaddress(), null);

		session.put(key, result1);
		result = null;
		return responseStr;
	}

	private boolean checkLoginFromCMS(String request, String key) {
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

	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		sessionClose(ctx);
		ctx.close();
	}

	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		sessionClose(ctx);
		super.channelInactive(ctx);
	}

	private void sessionClose(ChannelHandlerContext ctx) {
		String key = ctx.channel().remoteAddress().toString();
		session.remove(key);
	}
}