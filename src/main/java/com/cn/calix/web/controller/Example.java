package com.cn.calix.web.controller;

import com.cn.proxy.server.ana.process.proxy.ANAProxyService;
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/4/18
 * Time: 下午3:04
 */
@RestController
@EnableAutoConfiguration
public class Example {
    private static final Logger log = Logger.getLogger(Example.class);


//    @Value("${pathSettings.path}")
//    public  String path;

    @RequestMapping("/")
    String home(){
        Map<String,String> deviceNameMap=ANAProxyService.deviceNameMap;
        return deviceNameMap.toString();
    }

    ConcurrentHashMap<String,String> map=new ConcurrentHashMap<>();

}
