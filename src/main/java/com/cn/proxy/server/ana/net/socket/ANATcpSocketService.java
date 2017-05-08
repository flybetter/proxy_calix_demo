
package com.cn.proxy.server.ana.net.socket;

import com.cn.proxy.server.ana.ANAConstants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;



/**
 * Simplistic telnet server.
 */
public class ANATcpSocketService implements Runnable{
	private static final Logger log= Logger.getLogger(ANATcpSocketService.class);
	private boolean needssl=false;
	private int port;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	
	public ANATcpSocketService(){
		this.port= ANAConstants.DEFAULT_SOCKET_PORT;
	}
	
	public ANATcpSocketService(int port){
		this.port=port;
	}
	
	public ANATcpSocketService(boolean needssl,int port){
		this.needssl=needssl;
		this.port=port;
	}
	public void startService() throws CertificateException, SSLException, InterruptedException {
		// Configure SSL.
		final SslContext sslCtx;
		if (needssl) {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContextBuilder.forServer(ssc.certificate(),
					ssc.privateKey()).build();
		} else {
			sslCtx = null;
		}

		bossGroup = new NioEventLoopGroup(1);
		workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new TcpSocketServiceInitializer(sslCtx));

			b.bind(port).sync().channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	@Override
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
	
	public void stopService(){
		if(bossGroup!=null){
			bossGroup.shutdownGracefully();	
		}
		if(workerGroup!=null){
			workerGroup.shutdownGracefully();	
		}
		
		
	}
}

class TcpSocketServiceInitializer extends ChannelInitializer<SocketChannel> {

	private static final StringDecoder DECODER = new StringDecoder();
	private static final StringEncoder ENCODER = new StringEncoder();

	private static final ANATcpSocketServiceHandler SERVER_HANDLER = new ANATcpSocketServiceHandler();

	private final SslContext sslCtx;

	public TcpSocketServiceInitializer(SslContext sslCtx) {
		this.sslCtx = sslCtx;
	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		if (sslCtx != null) {
			pipeline.addLast(sslCtx.newHandler(ch.alloc()));
		}

		// Add the text line codec combination first,
		pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		// the encoder and decoder are static as these are sharable
		pipeline.addLast(DECODER);
		pipeline.addLast(ENCODER);

		// and then business logic.
		pipeline.addLast(SERVER_HANDLER);
	}
}
