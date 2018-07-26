package com.networkprogram;

import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.util.*;
import java.io.IOException;

public class NIOEchoServer {
    public static int DEFAULT_PORT = 8000;
    
    public static void main(String[] args) {
        int port = DEFAULT_PORT;;
        
        System.out.println("Listening for connections on port " + port);
        ServerSocketChannel serverChannel;
        Selector selector;
       
        try {
            //获得表示服务端的SocketChannel实例
            serverChannel = ServerSocketChannel.open();
            
            //将Channel绑定在特定端口
            ServerSocket ss = serverChannel.socket();
            InetSocketAddress address = new InetSocketAddress(port);
            ss.bind(address);
           
            //将服务端的SocketChannel设置为非阻塞模式
            //只有在非阻塞模式，我们才能向Channel注册感兴趣的事件，
            //并且在数据准备好时，得到必要的通知
            serverChannel.configureBlocking(false);
            
            //获得一个Selector对象的实例
            selector = Selector.open();
            
            //将服务端的SocketChannel实例绑定在Selector实例上
            //并注册它感兴趣的事件为ACCEPT
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        //注意：此处为无穷循环，主要任务是等待-分发网络消息
        while (true) {
            try {
                //select()方法是一个阻塞方法；如果当前没有任何数据准备好，它就会等待
                //一旦有数据可读，它就会返回。它的返回值是已经准备就绪的SelectionKey的数量
                selector.select();
            } catch (IOException ex) {
                ex.printStackTrace();
                break;
            }
            
            //获取那些已经准备好的SelectionKey。因为Selector同时为多个Channel服务，
            //因此已经准备就绪的Channel就有可能为多个
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            
            //开始遍历准备就绪的Channel
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                
                //此处执行删除操作很重要，否则就会重复处理相同的SelectionKey
                iterator.remove();
                try {
                    //判断当前SelectionKey所代表的Channel是否处于Acceptable状态
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key
                                .channel();
                       
                        //生成的client表示和客户端通信的通道SocketChannel
                        SocketChannel client = server.accept();
                        System.out
                                .println("Accepted connection from " + client);
                        
                        //将这个SocketChannel设置为非阻塞模式
                        client.configureBlocking(false);
                        
                        //将新生成的SocketChannel注册到Selector选择器上，并告诉
                        //Selector对写操作、读操作事件感兴趣
                        SelectionKey clientKey = client.register(selector,
                                SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                        ByteBuffer buffer = ByteBuffer.allocate(1000);
                        clientKey.attach(buffer);
                    }
                    //判断当前SelectionKey所代表的Channel是否已经可以读取数据了
                    if (key.isReadable()) {
                    	//System.out.println("sever key.isReadable()");
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer output = (ByteBuffer) key.attachment();
                        client.read(output);
                       
                    }
                    //判断当前SelectionKey所代表的Channel是否准备好可以进行写数据了
                    if (key.isWritable()) {
                    	//System.out.println("sever key.isWritable()");
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer output = (ByteBuffer) key.attachment();
                        output.flip();
                        client.write(output);
                        output.compact();
                    }
                } catch (IOException ex) {
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException cex) {
                    }
                }
            }
        }
    }
}
