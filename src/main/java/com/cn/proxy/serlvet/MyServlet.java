package com.cn.proxy.serlvet;

import com.cn.proxy.server.ana.ANAService;
import com.cn.proxy.server.iprange.IpRangeServive;
import com.cn.proxy.server.turnuptool.TurnUptoolService;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/4/19
 * Time: 上午10:28
 */
public class MyServlet implements Servlet {

//    private static final Logger logger = LoggerFactory.getLogger(App.class);

//    @Value("${pathSettings.path}")
//    public String path;


    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        ANAService.startService();
        TurnUptoolService.startService();
        IpRangeServive.init();
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
    }
}
