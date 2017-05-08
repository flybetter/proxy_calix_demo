package com.cn.proxy.server.turnuptool.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLException;
import org.apache.log4j.Logger;

public class VNETcpSocketService implements Runnable {
	private static final Logger log = Logger
			.getLogger(VNETcpSocketService.class);

	private boolean needssl = false;
	private int port;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	public VNETcpSocketService() {
		this.port = 9000;
	}

	public VNETcpSocketService(int port) {
		this.port = port;
	}

	public VNETcpSocketService(boolean needssl, int port) {
		this.needssl = needssl;
		this.port = port;
	}

	public void startService() throws CertificateException, SSLException,
			InterruptedException {
		SslContext sslCtx;
		if (this.needssl) {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContextBuilder.forServer(ssc.certificate(),
					ssc.privateKey()).build();
		} else {
			sslCtx = null;
		}

		this.bossGroup = new NioEventLoopGroup(1);
		this.workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			((ServerBootstrap) ((ServerBootstrap) b.group(this.bossGroup,
					this.workerGroup).channel(NioServerSocketChannel.class))
					.handler(new LoggingHandler(LogLevel.INFO)))
					.childHandler(new TcpSocketServiceInitializer(sslCtx));

			b.bind(this.port).sync().channel().closeFuture().sync();
		} finally {
			this.bossGroup.shutdownGracefully();
			this.workerGroup.shutdownGracefully();
		}
	}

	public void run() {
		try {
			startService();
		} catch (CertificateException e) {
			log.error("Socket Server CertificateException", e);
		} catch (SSLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void stopService() {
		if (this.bossGroup != null) {
			this.bossGroup.shutdownGracefully();
		}
		if (this.workerGroup != null)
			this.workerGroup.shutdownGracefully();
	}
}