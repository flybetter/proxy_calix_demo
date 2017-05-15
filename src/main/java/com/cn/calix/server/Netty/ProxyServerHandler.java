package com.cn.calix.server.Netty;

import com.cn.calix.server.service.CommonService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/5/1
 * Time: 下午3:37
 */
public class ProxyServerHandler extends SimpleChannelInboundHandler<String> {

    private static Logger logger= LoggerFactory.getLogger(ProxyServerHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        CommonService commonService=new CommonService(channelHandlerContext);
        String keyInfo=commonService.getKeyInfo();
        logger.info(keyInfo+" receive:"+s.toString());
        String response=commonService.filter(s);
        logger.info(keyInfo=" callback:"+response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
