package com.cn.calix.server.service;

import com.cn.calix.server.Netty.NettyServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/5/10
 * Time: 下午4:51
 */
@Component
public class NettyService {

    @Value("${Proxy.port}")
    private int port;

    @Value("${Proxy.threadNum}")
    private int threadNum;

    @PostConstruct
    public void start(){
        ExecutorService cacheService= Executors.newSingleThreadExecutor();
        cacheService.execute(new NettyServer(port,threadNum));
    }
}
