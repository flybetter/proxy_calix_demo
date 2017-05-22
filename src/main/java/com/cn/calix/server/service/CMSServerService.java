package com.cn.calix.server.service;

import com.cn.calix.server.dto.CMSServer;
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

    private static final String ACTION_RUNWORKFLOW="RunWorkflow";

    private static final String ACTION_GETWORKFLOWOUTPUT="GetWorkflowOutput";

    private static final String NE_NOT_EXISTING="VNE Does Not Exist";

    public CMSServerService() {

    }

    public CMSServerService(ChannelHandlerContext channelHandlerContext){

        clientIp=((InetSocketAddress)channelHandlerContext.channel().remoteAddress()).getAddress().getHostAddress();

        clientPort=((InetSocketAddress) channelHandlerContext.channel().remoteAddress()).getPort();

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
        ProxyResult result=null;
        try {
            Socket socket=new Socket(ip,port);
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
            return new ProxyResult(response.toString(),ProxyResult.SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("CMSServerService send",e);
            return new ProxyResult(ProxyResult.FAIL,e.toString());
        }
    };


    public  ProxyResult checkLoginResponse(String response){
        if (response .contains(CMSSERVER_LOGIN_SUCCESS)) {
             ClientService.addClient(clientIp,clientPort);
            return new ProxyResult(response,ProxyResult.SUCCESS);
        }else{
            return new ProxyResult(ProxyResult.FAIL,response);
        }
    }


    public ProxyResult sendCommandByRequest(String action,String request){

        switch (action){
           case ACTION_RUNWORKFLOW: return RunWorkFlow(request);
           default: return new ProxyResult();
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
            ProxyResult sendResult=this.send(cmsServer.getIp(),cmsServer.getPort(),request);
            return sendResult;
        }else{
            for(CMSServer cmsServer:CMSServerService.cmsServers){
                ProxyResult sendResult=this.send(cmsServer.getIp(),cmsServer.getPort(),request);
                if (sendResult.isSuccess()&&!sendResult.getData().toString().equalsIgnoreCase(NE_NOT_EXISTING)){
                    deviceCMSServerService.addDeviceName(cmsServer,nameResult.getData().toString());
                    return sendResult;
                }
            }
            return  new ProxyResult(ProxyResult.FAIL,NE_NOT_EXISTING);
        }
    }

    public ProxyResult GetWorkflowOutput(String request){
        XMLService xmlService=new XMLService();
        ProxyResult nameResult =xmlService.getDeviceNameByRequest(request);
        if(!nameResult.isSuccess()){
            return nameResult;
        }
        DeviceCMSServerService deviceCMSServerService=new DeviceCMSServerService();
        ProxyResult cmsServerResult=deviceCMSServerService.getCMSServerByDeviceName(nameResult.getData().toString());
        if (cmsServerResult.isSuccess()){
            CMSServer cmsServer=(CMSServer) cmsServerResult.getData();
            ProxyResult sendResult=this.send(cmsServer.getIp(),cmsServer.getPort(),request);
            return sendResult;
        }else{
            return new ProxyResult(ProxyResult.FAIL,NE_NOT_EXISTING);
        }
    }


}
