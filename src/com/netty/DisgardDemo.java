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
        
        //�����ָֻ����һ�� EventLoopGroup�������ͻἴ��Ϊһ�� boss group ��
        //Ҳ����Ϊһ�� workder group�����ܿͻ��˲���Ҫʹ�õ� boss worker ��
        EventLoopGroup workgroup=new NioEventLoopGroup();
        try {
        	
       //BootStrap �� ServerBootstrap ����,�������ǶԷǷ���˵� channel ���ԣ�����ͻ��˻��������Ӵ���ģʽ�� channel��
        Bootstrap bootstrap= new Bootstrap();
        bootstrap.group(workgroup);
        
        //����NioServerSocketChannel����NioSocketChannel,������ڿͻ���channel ������ʱʹ�á�
        bootstrap.channel(NioSocketChannel.class);
        
        //������ʹ�� ServerBootstrap ʱ��Ҫ�� childOption() ��������Ϊ�ͻ��˵� SocketChannel û�и��ס�
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				// TODO �Զ����ɵķ������
				System.out.println("initChannel");
				socketChannel.pipeline().addLast(new ClientHandler());
				
			}
		});
        
        //�� connect() ���������� bind() ������
        ChannelFuture future=bootstrap.connect(host,port).sync();
        
        future.channel().close().sync();
        } catch (Exception e) {
			// TODO: handle exception
		}finally {
			workgroup.shutdownGracefully();
		}
        
        
	}
}
