package com.cn.calix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {


//    @Bean
//    public ServletRegistrationBean MyServlet() {
//        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new MyServlet(),"/example");
//        servletRegistrationBean.setLoadOnStartup(1);
//        return servletRegistrationBean;
//    }


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }



}
