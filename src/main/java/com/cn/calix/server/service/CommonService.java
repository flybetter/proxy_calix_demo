package com.cn.calix.server.service;

import com.cn.calix.server.dto.ProxyResult;
import com.cn.calix.server.function.PatternFunction;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/5/15
 * Time: 下午1:59
 */
public  class  CommonService {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(CommonService.class);

    private static final String LOGIN_PATTERN_STR="user=(.*) password=(.*)";
    private static final Pattern LOGIN_PATTERN=Pattern.compile(LOGIN_PATTERN_STR);

    private ChannelHandlerContext channelHandlerContext;


    public CommonService(ChannelHandlerContext channelHandlerContext){
        this.channelHandlerContext=channelHandlerContext;
    }

    public  String getKeyInfo (){
        return  " IP: "+((InetSocketAddress)channelHandlerContext.channel().remoteAddress()).getAddress().getHostAddress()+" PORT: "+ ((InetSocketAddress) channelHandlerContext.channel().remoteAddress()).getPort();
    }

    public  String filter(String request){
        //login
        ProxyResult loginResult=  judge(LOGIN_PATTERN,request,((pattern, command) -> {
            Matcher matcher=pattern.matcher(command);
            if (matcher.find()){
                return new ProxyResult(matcher.group(1).trim(),ProxyResult.SUCCESS);
            }else{
                return new ProxyResult(ProxyResult.FAIL,request);
            }
        }));
        if (loginResult.isSuccess()) {
            CMSServerService cmsServerService=new CMSServerService(channelHandlerContext,loginResult.getData().toString());
            ProxyResult result=cmsServerService.login(request);
            return checkProxyResult(result);
        }

        //check login
        ClientService clientService=new ClientService(channelHandlerContext);
        ProxyResult result=clientService.checkClientList();
        if (!result.isSuccess()) {
            return checkProxyResult(result);
        }

        //getActionByRequest
        XMLService xmlService=new XMLService();
        ProxyResult actionResult=xmlService.getActionByRequest(request);
        if (!actionResult.isSuccess()) {
            return checkProxyResult(actionResult);
        }

        CMSServerService cmsServerService=new CMSServerService(channelHandlerContext);
        ProxyResult commandResult=cmsServerService.sendCommandByRequest(actionResult.getData().toString(),request);
        return  checkProxyResult(commandResult);
    };


    public  ProxyResult judge(Pattern pattern, String command, PatternFunction patternFunction){

        return patternFunction.check(pattern,command);

    }

    public  String checkProxyResult(ProxyResult proxyResult){

        if (proxyResult.isSuccess()){
            return  proxyResult.getData().toString();
        }else{
            logger.error("CommonService checkProxyResult"+this.getKeyInfo(),proxyResult.getError());
            return proxyResult.getError();
        }
    }



}
