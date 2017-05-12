package com.cn.calix.server.Netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;


/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/5/1
 * Time: 下午2:55
 */
public class NettyServer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private int port;

    private int threadNum;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    public NettyServer(int port, int threadNum) {
        this.port = port;
        this.threadNum = threadNum;
    }

    public void start(){
        logger.info("Netty start...");
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(threadNum);
        try {
            ServerBootstrap sbs = new ServerBootstrap().group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("decoder", new StringDecoder());
                            ch.pipeline().addLast("encoder", new StringEncoder());
                            ch.pipeline().addLast(new ProxyServerHandler());
                        };

                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = sbs.bind(port).sync();
            logger.info("Netty  listen at " + port );
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void run() {
        this.start();
    }

    public void stop() {
        logger.info("destroy netty");
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        bossGroup = null;
        workerGroup = null;
    }

}
