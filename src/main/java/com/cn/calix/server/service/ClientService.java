package com.cn.calix.server.service;

import com.cn.calix.server.dto.Client;
import com.cn.calix.server.dto.ProxyResult;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/5/10
 * Time: 上午11:04
 */
public class ClientService {

    public static List<Client> clientList=new ArrayList<>();

    private String clientIp="";

    private Integer clientPort=0;

    private static String CLIENTLIST_NOT_CLIENT="no client in clientList";

    public ClientService(ChannelHandlerContext channelHandlerContext){

        clientIp=((InetSocketAddress)channelHandlerContext.channel().remoteAddress()).getAddress().getHostAddress();


        clientPort=((InetSocketAddress) channelHandlerContext.channel().remoteAddress()).getPort();

    }

    public static void addClient(String clinetIp, Integer clientPort){
        ClientService.clientList.add(new Client(clinetIp,clientPort));
    }

    public static void removeClient(String clientIp,Integer clientPort){
        ClientService.clientList.removeIf(client -> client==new Client(clientIp,clientPort));
    }


    public ProxyResult checkClientList(){
        if (this.clientList.stream().anyMatch(client -> client.equals(new Client(clientIp,clientPort)))){
            return new ProxyResult(this.clientList.stream().filter(client -> client.equals(new Client(clientIp,clientPort))).findFirst().get(),ProxyResult.SUCCESS);
        }else {
            return new ProxyResult(CLIENTLIST_NOT_CLIENT,ProxyResult.FAIL);
        }
    }






}
