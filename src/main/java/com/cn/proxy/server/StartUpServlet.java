package com.cn.proxy.server;

import com.cn.proxy.server.ana.ANAService;
import com.cn.proxy.server.iprange.IpRangeServive;
import com.cn.proxy.server.turnuptool.TurnUptoolService;

import javax.servlet.*;
import java.io.IOException;



public class StartUpServlet implements Servlet {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(ServletConfig arg0) throws ServletException {
    	ANAService.startService();
		TurnUptoolService.startService();
		IpRangeServive.init();
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

}
