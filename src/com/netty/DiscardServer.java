package com.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 丢弃任何进入的数据 启动服务端的DiscardServerHandler
 */

public class DiscardServer {
    private int port;

    public DiscardServer(int port) {
        super();
        this.port = port;
    }
    
    public  void run() throws Exception {
        /***
         * NioEventLoopGroup 是用来处理I/O操作的多线程事件循环器，
         * Netty提供了许多不同的EventLoopGroup的实现用来处理不同传输协议。 在这个例子中我们实现了一个服务端的应用，
         * 因此会有2个NioEventLoopGroup会被使用。 第一个经常被叫做‘boss’，用来接收进来的连接。
         * 第二个经常被叫做‘worker’，用来处理已经被接收的连接， 一旦‘boss’接收到连接，就会把连接信息注册到‘worker’上。
         * 如何知道多少个线程已经被使用，如何映射到已经创建的Channels上都需要依赖于EventLoopGroup的实现，
         * 并且可以通过构造函数来配置他们的关系。
         */
        /*
         * 配置服务端的NIO线程组，它包含了一组NIO线程，专门用于网络事件的处理，实际上它们就是Reactor线程组。
         * 这里创建两个的原因：一个用于服务端接受客户端的连接，
         * 另一个用于进行SocketChannel的网络读写。
         */
    	NioEventLoopGroup boss=new NioEventLoopGroup();
    	NioEventLoopGroup worker=new NioEventLoopGroup();
    	 System.out.println("准备运行端口：" + port);
    	 try { 
         /**
          * ServerBootstrap 是一个启动NIO服务的辅助启动类 你可以在这个服务中直接使用Channel
          * //ServerBootstrap对象，Netty用于启动NIO服务端的辅助启动类，目的是降低服务端的开发复杂度。
          */
    	 ServerBootstrap bootstrap=new ServerBootstrap();
    	 
         /**
          * 这一步是必须的，如果没有设置group将会报java.lang.IllegalStateException: group not
          * set异常
          */
    	 bootstrap=bootstrap.group(boss, worker);
    	 
         /***
          * ServerSocketChannel以NIO的selector为基础进行实现的，用来接收新的连接
          * 这里告诉Channel如何获取新的连接.
          */
    	 bootstrap=bootstrap.channel(NioServerSocketChannel.class);
    	 
         /***
          * 这里的事件处理类经常会被用来处理一个最近的已经接收的Channel。 ChannelInitializer是一个特殊的处理类，
          * 他的目的是帮助使用者配置一个新的Channel。
          * 也许你想通过增加一些处理类比如NettyServerHandler来配置一个新的Channel
          * 或者其对应的ChannelPipeline来实现你的网络程序。 当你的程序变的复杂时，可能你会增加更多的处理类到pipline上，
          * 然后提取这些匿名类到最顶层的类上。
          */
         /*
          * 绑定I/O事件的处理类ChildChannelHandler，它的作用类似于Reactor模式中的handler类，
          * 主要用于处理网络I/O事件，例如：记录日志、对消息进行编解码等。
          */
    	 bootstrap=bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				// TODO 自动生成的方法存根
				socketChannel.pipeline().addLast(new DiscardServerHandler());
			}
		});
    	 
         /***
          * 你可以设置这里指定的通道实现的配置参数。 我们正在写一个TCP/IP的服务端，
          * 因此我们被允许设置socket的参数选项比如tcpNoDelay和keepAlive。
          * 请参考ChannelOption和详细的ChannelConfig实现的接口文档以此可以对ChannelOptions的有一个大概的认识。
          */
    	 bootstrap=bootstrap.option(ChannelOption.SO_BACKLOG, 128);
    	 
    	 
         /***
          * option()是提供给NioServerSocketChannel用来接收进来的连接。
          * childOption()是提供给 由 父 管道ServerChannel接收到的连接，
          * 在这个例子中也是NioServerSocketChannel。
          */
    	 bootstrap = bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
    	 
         /***
          * 绑定端口并启动去接收进来的连接
          */
         /*
          * 绑定端口，同步等待成功（调用它的bind方法绑定监听端口，随后，调用它的同步阻塞方法sync等待绑定操作完成。
          * 完成之后Netty会返回一个ChannelFuture,它的功能类似于JDK的java.util.concurrent.Future，
          * 主要用于异步操作的通知回调。）
          */
    	 
    	 ChannelFuture future=bootstrap.bind(port).sync();
    	 
         /**
          * 这里会一直等待，直到socket被关闭
          * //等待服务端监听端口关闭（使用f.channel().closeFuture().sync()方法进行阻塞，等待服务端链路关闭之后main函数才退出。）
          */
    	 future.channel().closeFuture().sync();
    	 }finally {
    		//优雅退出，释放线程池资源
			worker.shutdownGracefully();
			boss.shutdownGracefully();
			
		}
    	 
	}
    
    public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
		 new DiscardServer(port).run();
		 System.out.println("server:run()");
	}
}
