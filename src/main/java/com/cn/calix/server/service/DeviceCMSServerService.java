package com.cn.calix.server.service;

import com.cn.calix.server.dto.CMSServer;
import com.cn.calix.server.dto.Device_CMSServer;
import com.cn.calix.server.dto.ProxyResult;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/5/22
 * Time: 上午8:49
 */
public class DeviceCMSServerService {
    public static List<Device_CMSServer> device_cmsServers =new ArrayList<>();

    private String clientIp="";

    private Integer clientPort=0;

    private String DEVICE_PREF="NTWK-";

    private static final String NOT_EXISTENCE="CMSServer not existence";

    public DeviceCMSServerService() {
    }

    public DeviceCMSServerService(ChannelHandlerContext channelHandlerContext){

        this.clientIp=((InetSocketAddress)channelHandlerContext.channel().remoteAddress()).getAddress().getHostAddress();
        this.clientPort=((InetSocketAddress)channelHandlerContext.channel().remoteAddress()).getPort();
    }


    public ProxyResult getCMSServerByDeviceName(String deviceName){
         deviceName=this.getDeviceName(deviceName);
         for(Device_CMSServer device_cmsServer: device_cmsServers){

             CMSServer cmsServer= device_cmsServer.getCMSServerByDeviceName(deviceName);

             if (cmsServer!=null) {
                 return new ProxyResult(cmsServer,ProxyResult.SUCCESS);
             }
         }
        return new ProxyResult(ProxyResult.FAIL,NOT_EXISTENCE);
    };


    public String getDeviceName(String deivceName){
        deivceName=deivceName.startsWith(DEVICE_PREF)?deivceName:DEVICE_PREF+deivceName;
        return  deivceName;
    }


    public void addDeviceName(CMSServer cmsServer, String deviceName){
       Device_CMSServer device_cmsServer= DeviceCMSServerService.device_cmsServers.stream().filter(e->e.getCmsServer().equals(cmsServer)).findFirst().get();
       device_cmsServer.getDeviceSet().add(deviceName);
//        for (Device_CMSServer device_cmsServer:device_cmsServers){
//            if (device_cmsServer.getCmsServer().equals(cmsServer)){
//                device_cmsServer.getDeviceSet().add(deviceName);
//            }
//        }
    }

}
