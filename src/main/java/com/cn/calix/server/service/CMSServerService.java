package com.cn.calix.server.service;

import com.cn.calix.server.dto.CMSServer;
import com.cn.calix.server.dto.Client;
import com.cn.calix.server.dto.ProxyResult;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
public class CMSServerService {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(CMSServerService.class);

    public static List<CMSServer> cmsServers=new ArrayList<>();

    private static String CMSSERVERS_IS_NULL="cmsServers is null";

    private static String CMSSERVER_LOGIN_SUCCESS="success";

    private String clientIp="";

    private Integer clientPort=0;

    private String userName="";

    private Socket socket=null;

    private static final String ACTION_RUNWORKFLOW="RunWorkflow";

    private static final String ACTION_GETWORKFLOWOUTPUT="GetWorkflowOutput";

    private static final String ACTION_CREATE="Create";

    private static final String ACTION_DELETE="Delete";

    private static final String NE_NOT_EXISTING="VNE Does Not Exist";


    public CMSServerService(ChannelHandlerContext channelHandlerContext){

        clientIp=((InetSocketAddress)channelHandlerContext.channel().remoteAddress()).getAddress().getHostAddress();

        clientPort=((InetSocketAddress) channelHandlerContext.channel().remoteAddress()).getPort();

    }

    public CMSServerService(ChannelHandlerContext channelHandlerContext,String userName){

        clientIp=((InetSocketAddress)channelHandlerContext.channel().remoteAddress()).getAddress().getHostAddress();

        clientPort=((InetSocketAddress) channelHandlerContext.channel().remoteAddress()).getPort();

        this.userName=userName;

    }


    public  ProxyResult login(String command){
        if (cmsServers.size() !=0 ) {
            ProxyResult proxyResult=send(cmsServers.stream().findFirst().get().getIp(),cmsServers.stream().findFirst().get().getPort(),command);
            if (proxyResult.isSuccess()) {
                proxyResult=checkLoginResponse(proxyResult.getData().toString());
            }
            return proxyResult;
        }else {
            return  new ProxyResult(ProxyResult.FAIL,CMSSERVERS_IS_NULL);
        }
    };


    public  ProxyResult send(String ip,Integer port,String command){
        logger.info("Proxy(ip:"+clientIp+" port"+clientPort+") send to CMS(ip:"+ip+" port:"+port+"):"+command);
        ProxyResult result=null;

        try{
            if(socket==null){
                this.socket=new Socket(ip,port);
            }
            BufferedReader in =new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out=new PrintWriter(socket.getOutputStream());
            out.println(command+"\n.\n");
            out.flush();
            String line=null;
            StringBuffer response=new StringBuffer("");
            while ((line=in.readLine())!=null){
                if(line!=null&&line.equals(".")){
                    break;
                }
                response.append(line);
            }
            logger.info("Proxy(ip:"+clientIp+" port:"+clientPort+") receive from CMS(ip:"+ip+" port:"+port+"):"+response.toString());
            return new ProxyResult(response.toString(),ProxyResult.SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("CMSServerService send",e);
            return new ProxyResult(ProxyResult.FAIL,e.toString());
        }

    };

    @Deprecated
    public void closeSocket(BufferedReader in,PrintWriter out,Socket socket){
        try {
            if (in!=null){
                in.close();
            }
            if (out!=null){
                out.close();
            }
            if (socket != null) {
               socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public  ProxyResult checkLoginResponse(String response){
        if (response .contains(CMSSERVER_LOGIN_SUCCESS)) {
             ClientService.addClient(clientIp,clientPort,userName);
            return new ProxyResult(response,ProxyResult.SUCCESS);
        }else{
            return new ProxyResult(ProxyResult.FAIL,response);
        }
    }


    public ProxyResult sendCommandByRequest(String action,String request){

        switch (action){
           case ACTION_RUNWORKFLOW: return RunWorkFlow(request);
           case ACTION_GETWORKFLOWOUTPUT: return GetWorkflowOutput(request);
           case ACTION_CREATE: return Create(request);
           default: return new ProxyResult(ProxyResult.FAIL,request);
        }
    };

    public ProxyResult RunWorkFlow(String request){
        XMLService xmlService=new XMLService();
        ProxyResult nameResult =xmlService.getDeviceNameByRequest(request);
        if(!nameResult.isSuccess()){
            return nameResult;
        }
        DeviceCMSServerService deviceCMSServerService=new DeviceCMSServerService();
        ProxyResult cmsServerResult=deviceCMSServerService.getCMSServerByDeviceName(nameResult.getData().toString());
        if (cmsServerResult.isSuccess()){
            CMSServer cmsServer=(CMSServer) cmsServerResult.getData();
            ClientService.updateClient(clientIp,clientPort,cmsServer,this.socket);
            ProxyResult sendResult=this.send(cmsServer.getIp(),cmsServer.getPort(),this.getRequestByProxy(request));
            return sendResult;
        }else{
            for(CMSServer cmsServer:CMSServerService.cmsServers){
                ProxyResult sendResult=this.send(cmsServer.getIp(),cmsServer.getPort(),this.getRequestByProxy(request));
                if (sendResult.isSuccess()&&!sendResult.getData().toString().equalsIgnoreCase(NE_NOT_EXISTING)){
                    deviceCMSServerService.addDeviceName(cmsServer,nameResult.getData().toString());
                    ClientService.updateClient(clientIp,clientPort,cmsServer,this.socket);
                    return sendResult;
                }
            }
            return  new ProxyResult(ProxyResult.FAIL,NE_NOT_EXISTING);
        }
    }

    public String getRequestByProxy(String request){
        Client client=ClientService.clientList.stream().filter((e)->e.equals(new Client(clientIp,clientPort))).findFirst().get();
        request="from_proxy_to_CMS:["+client.getUserName()+"]"+request;
        return  request;
    }

    public ProxyResult GetWorkflowOutput(String request){
        Client client=ClientService.clientList.stream().filter(e->e.equals(new Client(clientIp,clientPort))).findFirst().get();
        this.socket=client.getSocket();
        ProxyResult sendResult=this.send(client.getCmsServer().getIp(),client.getCmsServer().getPort(),request);
        ClientService.removeClient(clientIp,clientPort);
        return sendResult;
    }

    public ProxyResult Create(String request){
        CMSServer cmsServer=CMSServerService.cmsServers.stream().findFirst().get();
        ProxyResult sendResult=this.send(cmsServer.getIp(),CMSServer.turnupToolPort,this.getRequestByProxy(request));
        ClientService.removeClient(clientIp,clientPort);
        if (sendResult.isSuccess()&&!sendResult.getData().toString().equalsIgnoreCase(NE_NOT_EXISTING)) {
            return sendResult;
        }else {
            return new ProxyResult(ProxyResult.FAIL,sendResult.getData().toString()==null?sendResult.getError():sendResult.getData().toString());
        }
    };





}
