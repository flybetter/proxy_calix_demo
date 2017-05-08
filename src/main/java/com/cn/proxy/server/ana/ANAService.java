package com.cn.proxy.server.ana;

import com.cn.proxy.server.ana.net.socket.ANATcpSocketService;
import com.cn.proxy.server.ana.process.ANASyncB6DeviceJob;
import com.cn.proxy.server.ana.process.proxy.ANAProxyService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.File;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * ClassName: ANAService <br/>
 * Function: this class to start service for ANA project <br/>
 * date: 13 Sep, 2016 <br/>
 * 
 * @author Tony Ben
 * @version
 * @since JDK 1.6
 */
public final class ANAService {
	private static final Logger log = Logger.getLogger(ANAService.class);
	private static final int DEFAULT_SOCKET_PROTOCOL = 0;// 0:TCP 1:SSL
	private static final int DEFAULT_SOCKET_PORT = 9002;// TCP 9002 SSL 9003
	private static ExecutorService cachedThreadPool = Executors
			.newCachedThreadPool();
	private static ANATcpSocketService service;
	private static int socketPort = DEFAULT_SOCKET_PORT;
	private static int socketProtocol = DEFAULT_SOCKET_PROTOCOL;

	/**
	 * Function:startService<br/>
	 * Conditions:None<br/>
	 * WorkFlow:TODO<br/>
	 * UserGuide:TODO<br/>
	 * Remark:TODO<br/>
	 * 
	 * @author Tony Ben
	 * @since JDK 1.6
	 */
	public static void startService() {
		// get configure for ANA
		loadANASettings();
		// StartSocketService
		startANASocketService(socketProtocol, socketPort);
		// start delete service
		startSyncDeviceJob();
	}

	public static void loadANASettings() {
//		File file = new File(ANAConstants.ROOT_DIR
//				+ ANAConstants.ANA_SETTING_FILE);
		File file = new File(ANAConstants.ANA_PATH);

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
			// get common settings
			// load from configure file
			socketProtocol = Integer.parseInt(common.element("protocol")
					.getTextTrim());
			socketPort = Integer.parseInt(common.element("port").getTextTrim());
			// load CMS information
			List<Element> clients = root.element("clients").elements();
			Map<String, Map<String, String>> cmsClientMap = new HashMap<String, Map<String, String>>();
			for (Element e : clients) {
				if (StringUtils.isNumeric(e.elementText("port"))) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("ipaddress", e.elementText("ipaddress"));
					map.put("port", e.elementText("port"));
					map.put("isSSL", "false");
					cmsClientMap.put(map.get("ipaddress"), map);
				}
			}
			ANAProxyService.updateClients(cmsClientMap);
			ANAProxyService.syncClientDevice(true);
			clients = null;
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			document = null;
			reader = null;
			file = null;
		}

	}

	private static void stopSocketService() {
		log.info("[ANA] stop socket service start");
		if (service != null) {
			service.stopService();
		}
		service = null;
		log.info("[ANA] stop socket service successful");
	}

	private static void startSyncDeviceJob() {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched;
		try {
			sched = sf.getScheduler();
			JobDetail job = new JobDetail("ANASyncB6DeviceJob",
					"ANASyncB6DeviceJobGroup", ANASyncB6DeviceJob.class);
			CronTrigger trigger = new CronTrigger("ANASyncB6DeviceJobTrigger",
					"ANASyncB6DeviceJobGroup", "ANASyncB6DeviceJob",
					"ANASyncB6DeviceJobGroup", "0 0 1 * * ?");
			sched.addJob(job, true);
			sched.scheduleJob(trigger);
			sched.start();
		} catch (SchedulerException e) {
			log.error("[ANA] start sync device job error", e);
		} catch (ParseException e) {
			log.error("[ANA] start sync device error", e);
		}
	}

	/**
	 * Function:startANASocketService<br/>
	 * 
	 * @author Tony Ben
	 * @since JDK 1.6
	 */
	private static void startANASocketService(int protocol, int port) {
		log.info("[ANA] start socket service protocol:" + protocol + " port:"
				+ port);
		socketPort = port;
		socketProtocol = protocol;
		boolean ssl = protocol == 0 ? false : true;
		service = new ANATcpSocketService(ssl, socketPort);
		cachedThreadPool.execute(service);
	}

	/**
	 * Function:stopService<br/>
	 * Conditions:TODO<br/>
	 * WorkFlow:TODO<br/>
	 * UserGuide:TODO<br/>
	 * Remark:TODO<br/>
	 * 
	 * @author Tony Ben
	 * @since JDK 1.6
	 */
	public void stopService() {
		// stop socket service
		stopSocketService();
		// stop archive service
	}

}
