package com.cn.proxy.server.turnuptool.socket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;

class TcpSocketServiceInitializer extends ChannelInitializer<SocketChannel> {
	private static final StringDecoder DECODER = new StringDecoder();
	private static final StringEncoder ENCODER = new StringEncoder();

	private static final VNETcpSocketServiceHandler SERVER_HANDLER = new VNETcpSocketServiceHandler();
	private final SslContext sslCtx;

	public TcpSocketServiceInitializer(SslContext sslCtx) {
		this.sslCtx = sslCtx;
	}

	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		if (this.sslCtx != null) {
			pipeline.addLast(new ChannelHandler[] { this.sslCtx.newHandler(ch
					.alloc()) });
		}

		pipeline.addLast(new ChannelHandler[] { new DelimiterBasedFrameDecoder(
				8192, Delimiters.lineDelimiter()) });

		pipeline.addLast(new ChannelHandler[] { DECODER });
		pipeline.addLast(new ChannelHandler[] { ENCODER });

		pipeline.addLast(new ChannelHandler[] { SERVER_HANDLER });
	}
}