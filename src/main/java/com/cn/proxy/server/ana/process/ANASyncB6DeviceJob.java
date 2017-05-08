/** 
 * Project Name:socket_netty 
 * File Name:ANASyncB6DeviceJob.java 
 * Package Name:com.calix.bseries.server.ana.process 
 * Date:6 Dec, 2016
 * Copyright (c) 2016, Calix All Rights Reserved. 
 * 
*/ 
package com.cn.proxy.server.ana.process;

import com.cn.proxy.server.ana.process.proxy.ANAProxyService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/** 
 * ClassName:ANASyncB6DeviceJob <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     6 Dec, 2016 <br/> 
 * @author   Tony Ben 
 * @version  
 * @since    JDK 1.6 
 * @see       
 */
public class ANASyncB6DeviceJob implements Job {
	private static final Logger log=Logger.getLogger(ANASyncB6DeviceJob.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
			log.info("[ANA device sync job]");
			ANAProxyService.syncClientDevice(true);
	}

}
