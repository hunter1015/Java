package com.netty;

import java.nio.ByteBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;


/**
 * 服务端处理通道.这里只是打印一下请求的内容，并不对请求进行任何的响应 DiscardServerHandler 继承自
 * ChannelHandlerAdapter， 这个类实现了ChannelHandler接口， ChannelHandler提供了许多事件处理的接口方法，
 * 然后你可以覆盖这些方法。 现在仅仅只需要继承ChannelHandlerAdapter类而不是你自己去实现接口方法。
 *
 */
public class DiscardServerHandler extends ChannelHandlerAdapter {

	/*	/channelActive() 方法将会在连接被建立并且准备进行通信时被调用。
	因此让我们在这个方法里完成一个代表当前时间的32位整数消息的构建工作。*/
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO 自动生成的方法存根
		System.out.println("channelActive");
/**		为了发送一个新的消息，我们需要分配一个包含这个消息的新的缓冲。因为我们需要写入一个32位的整数，
		因此我们需要一个至少有4个字节的 ByteBuf。通过 ChannelHandlerContext.alloc() 得到一个当前的ByteBufAllocator，
		然后分配一个新的缓冲。*/
		final ByteBuf time = ctx.alloc().buffer(4); // (2)
		
		
/**		和往常一样我们需要编写一个构建好的消息。
		但是等一等，flip 在哪？难道我们使用 NIO 发送消息时不是调用 java.nio.ByteBuffer.flip() 吗？
		ByteBuf 之所以没有这个方法因为有两个指针，一个对应读操作一个对应写操作。
		当你向 ByteBuf 里写入数据的时候写指针的索引就会增加，同时读指针的索引没有变化。
		读指针索引和写指针索引分别代表了消息的开始和结束。
		比较起来，NIO 缓冲并没有提供一种简洁的方式来计算出消息内容的开始和结尾，除非你调用 flip 方法。
		当你忘记调用 flip 方法而引起没有数据或者错误数据被发送时，你会陷入困境。
		这样的一个错误不会发生在 Netty 上，因为我们对于不同的操作类型有不同的指针。
		你会发现这样的使用方法会让你过程变得更加的容易，因为你已经习惯一种没有使用 flip 的方式。
		*/
		time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
		
		/**
		 * 另外一个点需要注意的是 ChannelHandlerContext.write() (和 writeAndFlush() )方法会返回一个 ChannelFuture 对象，
		 * 一个 ChannelFuture 代表了一个[还没有发生]的 I/O 操作。这意味着任何一个请求操作都不会马上被执行，因为在 Netty 里所有的操作都是异步的。
		 *举个例子下面的代码中在消息被发送之前可能会先关闭连接。
    Channel ch = ...;
    ch.writeAndFlush(message);
    ch.close();
			因此你需要在 write() 方法返回的 ChannelFuture 完成后调用 close() 方法，然后当他的写操作已经完成他会通知他的监听者。请注意,close() 方法也可能不会立马关闭，他也会返回一个ChannelFuture。
		 */
		 final ChannelFuture f = ctx.writeAndFlush(time); // (3)
		 
		 
		 /**
		  * 当一个写请求已经完成是如何通知到我们？这个只需要简单地在返回的 ChannelFuture 上增加一个ChannelFutureListener。
		  * 这里我们构建了一个匿名的 ChannelFutureListener 类用来在操作完成时关闭 Channel。
		  * 或者，你可以使用简单的预定义监听器代码:f.addListener(ChannelFutureListener.CLOSE);
		  */
		 f.addListener(new ChannelFutureListener() {
	            @Override
	            public void operationComplete(ChannelFuture future) {
	                assert f == future;
	                ctx.close();
	            }
	        }); // (4)
	}
	
	
    /**
     * 这里我们覆盖了chanelRead()事件处理方法。 每当服务端从客户端收到新的数据时， 这个方法会在收到消息时被调用，
     * 这个例子中，收到的消息的类型是ByteBuf
     * 
     * @param ctx
     *            通道处理的上下文信息
     * @param msg
     *            接收的消息
     */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("channelRead");
		
		// TODO 自动生成的方法存根
		ctx.write(msg);
		ctx.flush();
		ByteBuf in=(ByteBuf) msg;
		// 打印客户端输入，传输过来的的字符
		System.out.println(in.toString(CharsetUtil.UTF_8));
		
        /**
         * ByteBuf是一个引用计数对象，这个对象必须显示地调用release()方法来释放。
         * 请记住处理器的职责是释放所有传递到处理器的引用计数对象。
         */
        // 抛弃收到的数据
		//in.release();(这个也是释放传递到处理器的引用计数对象)
		ReferenceCountUtil.release(msg);
	}
	
	
    /***
     * 这个方法会在发生异常时触发
     * 
     * @param ctx
     * @param cause
     */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO 自动生成的方法存根
        /**
         * exceptionCaught() 事件处理方法是当出现 Throwable 对象才会被调用，即当 Netty 由于 IO
         * 错误或者处理器在处理事件时抛出的异常时。在大部分情况下，捕获的异常应该被记录下来 并且把关联的 channel
         * 给关闭掉。然而这个方法的处理方式会在遇到不同异常的情况下有不 同的实现，比如你可能想在关闭连接之前发送一个错误码的响应消息。
         */
        // 出现异常就关闭
		cause.printStackTrace();
		ctx.close();
	}
}
