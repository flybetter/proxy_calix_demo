package com.cn.calix.server.service;

import com.cn.calix.server.dto.CMSServer;
import com.cn.calix.server.dto.Client;
import com.cn.calix.server.dto.ProxyResult;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.net.Socket;
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

    public static void addClient(String clinetIp, Integer clientPort,String userName){
        ClientService.clientList.add(new Client(clinetIp,clientPort,userName));
    }

    public static void removeClient(String clientIp,Integer clientPort){
        ClientService.clientList.removeIf(client -> client==new Client(clientIp,clientPort));
    }

    public static void updateClient(String clientIp, Integer clientPort, CMSServer cmsServer,Socket socket){
        for (Client client:ClientService.clientList){
            if (client.getIp().equals(clientIp)&&clientPort.equals(clientPort)){
                Client temp=new Client(clientIp,clientPort,client.getUserName());
                temp.setCmsServer(cmsServer);
                temp.setSocket(socket);
                ClientService.clientList.remove(client);
                ClientService.clientList.add(temp);
                break;
            }
        }
    }


    public ProxyResult checkClientList(){
        if (this.clientList.stream().anyMatch(client -> client.equals(new Client(clientIp,clientPort)))){
            return new ProxyResult(this.clientList.stream().filter(client -> client.equals(new Client(clientIp,clientPort))).findFirst().get(),ProxyResult.SUCCESS);
        }else {
            return new ProxyResult(ProxyResult.FAIL,CLIENTLIST_NOT_CLIENT);
        }
    }






}
