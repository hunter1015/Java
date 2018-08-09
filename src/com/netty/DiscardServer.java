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
 * �����κν�������� ��������˵�DiscardServerHandler
 */

public class DiscardServer {
    private int port;

    public DiscardServer(int port) {
        super();
        this.port = port;
    }
    
    public  void run() throws Exception {
        /***
         * NioEventLoopGroup ����������I/O�����Ķ��߳��¼�ѭ������
         * Netty�ṩ����಻ͬ��EventLoopGroup��ʵ����������ͬ����Э�顣 ���������������ʵ����һ������˵�Ӧ�ã�
         * ��˻���2��NioEventLoopGroup�ᱻʹ�á� ��һ��������������boss�����������ս��������ӡ�
         * �ڶ���������������worker�������������Ѿ������յ����ӣ� һ����boss�����յ����ӣ��ͻ��������Ϣע�ᵽ��worker���ϡ�
         * ���֪�����ٸ��߳��Ѿ���ʹ�ã����ӳ�䵽�Ѿ�������Channels�϶���Ҫ������EventLoopGroup��ʵ�֣�
         * ���ҿ���ͨ�����캯�����������ǵĹ�ϵ��
         */
        /*
         * ���÷���˵�NIO�߳��飬��������һ��NIO�̣߳�ר�����������¼��Ĵ���ʵ�������Ǿ���Reactor�߳��顣
         * ���ﴴ��������ԭ��һ�����ڷ���˽��ܿͻ��˵����ӣ�
         * ��һ�����ڽ���SocketChannel�������д��
         */
    	NioEventLoopGroup boss=new NioEventLoopGroup();
    	NioEventLoopGroup worker=new NioEventLoopGroup();
    	 System.out.println("׼�����ж˿ڣ�" + port);
    	 try { 
         /**
          * ServerBootstrap ��һ������NIO����ĸ��������� ����������������ֱ��ʹ��Channel
          * //ServerBootstrap����Netty��������NIO����˵ĸ��������࣬Ŀ���ǽ��ͷ���˵Ŀ������Ӷȡ�
          */
    	 ServerBootstrap bootstrap=new ServerBootstrap();
    	 
         /**
          * ��һ���Ǳ���ģ����û������group���ᱨjava.lang.IllegalStateException: group not
          * set�쳣
          */
    	 bootstrap=bootstrap.group(boss, worker);
    	 
         /***
          * ServerSocketChannel��NIO��selectorΪ��������ʵ�ֵģ����������µ�����
          * �������Channel��λ�ȡ�µ�����.
          */
    	 bootstrap=bootstrap.channel(NioServerSocketChannel.class);
    	 
         /***
          * ������¼������ྭ���ᱻ��������һ��������Ѿ����յ�Channel�� ChannelInitializer��һ������Ĵ����࣬
          * ����Ŀ���ǰ���ʹ��������һ���µ�Channel��
          * Ҳ������ͨ������һЩ���������NettyServerHandler������һ���µ�Channel
          * �������Ӧ��ChannelPipeline��ʵ������������ ����ĳ����ĸ���ʱ������������Ӹ���Ĵ����ൽpipline�ϣ�
          * Ȼ����ȡ��Щ�����ൽ�������ϡ�
          */
         /*
          * ��I/O�¼��Ĵ�����ChildChannelHandler����������������Reactorģʽ�е�handler�࣬
          * ��Ҫ���ڴ�������I/O�¼������磺��¼��־������Ϣ���б����ȡ�
          */
    	 bootstrap=bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				// TODO �Զ����ɵķ������
				socketChannel.pipeline().addLast(new DiscardServerHandler());
			}
		});
    	 
         /***
          * �������������ָ����ͨ��ʵ�ֵ����ò����� ��������дһ��TCP/IP�ķ���ˣ�
          * ������Ǳ���������socket�Ĳ���ѡ�����tcpNoDelay��keepAlive��
          * ��ο�ChannelOption����ϸ��ChannelConfigʵ�ֵĽӿ��ĵ��Դ˿��Զ�ChannelOptions����һ����ŵ���ʶ��
          */
    	 bootstrap=bootstrap.option(ChannelOption.SO_BACKLOG, 128);
    	 
    	 
         /***
          * option()���ṩ��NioServerSocketChannel�������ս��������ӡ�
          * childOption()���ṩ�� �� �� �ܵ�ServerChannel���յ������ӣ�
          * �����������Ҳ��NioServerSocketChannel��
          */
    	 bootstrap = bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
    	 
         /***
          * �󶨶˿ڲ�����ȥ���ս���������
          */
         /*
          * �󶨶˿ڣ�ͬ���ȴ��ɹ�����������bind�����󶨼����˿ڣ���󣬵�������ͬ����������sync�ȴ��󶨲�����ɡ�
          * ���֮��Netty�᷵��һ��ChannelFuture,���Ĺ���������JDK��java.util.concurrent.Future��
          * ��Ҫ�����첽������֪ͨ�ص�����
          */
    	 
    	 ChannelFuture future=bootstrap.bind(port).sync();
    	 
         /**
          * �����һֱ�ȴ���ֱ��socket���ر�
          * //�ȴ�����˼����˿ڹرգ�ʹ��f.channel().closeFuture().sync()���������������ȴ��������·�ر�֮��main�������˳�����
          */
    	 future.channel().closeFuture().sync();
    	 }finally {
    		//�����˳����ͷ��̳߳���Դ
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
