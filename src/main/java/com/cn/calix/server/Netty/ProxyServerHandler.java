package com.cn.calix.server.Netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("channelRead receive:"+msg.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
