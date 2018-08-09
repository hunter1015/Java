package com.netty;

import java.net.SocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class DisgardDemo {
	public static void main(String[] args) {
		//String host = args[0];
        //int port = Integer.parseInt(args[1]);
		
		String host = "127.0.0.1";
        int port = 8080;
        
        //如果你只指定了一个 EventLoopGroup，那他就会即作为一个 boss group ，
        //也会作为一个 workder group，尽管客户端不需要使用到 boss worker 。
        EventLoopGroup workgroup=new NioEventLoopGroup();
        try {
        	
       //BootStrap 和 ServerBootstrap 类似,不过他是对非服务端的 channel 而言，比如客户端或者无连接传输模式的 channel。
        Bootstrap bootstrap= new Bootstrap();
        bootstrap.group(workgroup);
        
        //代替NioServerSocketChannel的是NioSocketChannel,这个类在客户端channel 被创建时使用。
        bootstrap.channel(NioSocketChannel.class);
        
        //不像在使用 ServerBootstrap 时需要用 childOption() 方法，因为客户端的 SocketChannel 没有父亲。
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				// TODO 自动生成的方法存根
				System.out.println("initChannel");
				socketChannel.pipeline().addLast(new ClientHandler());
				
			}
		});
        
        //用 connect() 方法代替了 bind() 方法。
        ChannelFuture future=bootstrap.connect(host,port).sync();
        
        future.channel().close().sync();
        } catch (Exception e) {
			// TODO: handle exception
		}finally {
			workgroup.shutdownGracefully();
		}
        
        
	}
}
