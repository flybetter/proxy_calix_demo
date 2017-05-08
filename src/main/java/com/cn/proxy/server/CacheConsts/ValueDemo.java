package com.cn.proxy.server.CacheConsts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/4/20
 * Time: 下午2:35
 */
@Component
public class ValueDemo {
    @Value("${pathSettings}")
    private String driver;

    @PostConstruct
    public void run(){
        System.out.println(1111);
        System.out.println(driver);
    }

}
