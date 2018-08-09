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
 * ����˴���ͨ��.����ֻ�Ǵ�ӡһ����������ݣ���������������κε���Ӧ DiscardServerHandler �̳���
 * ChannelHandlerAdapter�� �����ʵ����ChannelHandler�ӿڣ� ChannelHandler�ṩ������¼�����Ľӿڷ�����
 * Ȼ������Ը�����Щ������ ���ڽ���ֻ��Ҫ�̳�ChannelHandlerAdapter����������Լ�ȥʵ�ֽӿڷ�����
 *
 */
public class DiscardServerHandler extends ChannelHandlerAdapter {

	/*	/channelActive() �������������ӱ���������׼������ͨ��ʱ�����á�
	�����������������������һ������ǰʱ���32λ������Ϣ�Ĺ���������*/
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO �Զ����ɵķ������
		System.out.println("channelActive");
/**		Ϊ�˷���һ���µ���Ϣ��������Ҫ����һ�����������Ϣ���µĻ��塣��Ϊ������Ҫд��һ��32λ��������
		���������Ҫһ��������4���ֽڵ� ByteBuf��ͨ�� ChannelHandlerContext.alloc() �õ�һ����ǰ��ByteBufAllocator��
		Ȼ�����һ���µĻ��塣*/
		final ByteBuf time = ctx.alloc().buffer(4); // (2)
		
		
/**		������һ��������Ҫ��дһ�������õ���Ϣ��
		���ǵ�һ�ȣ�flip ���ģ��ѵ�����ʹ�� NIO ������Ϣʱ���ǵ��� java.nio.ByteBuffer.flip() ��
		ByteBuf ֮����û�����������Ϊ������ָ�룬һ����Ӧ������һ����Ӧд������
		������ ByteBuf ��д�����ݵ�ʱ��дָ��������ͻ����ӣ�ͬʱ��ָ�������û�б仯��
		��ָ��������дָ�������ֱ��������Ϣ�Ŀ�ʼ�ͽ�����
		�Ƚ�������NIO ���岢û���ṩһ�ּ��ķ�ʽ���������Ϣ���ݵĿ�ʼ�ͽ�β����������� flip ������
		�������ǵ��� flip ����������û�����ݻ��ߴ������ݱ�����ʱ���������������
		������һ�����󲻻ᷢ���� Netty �ϣ���Ϊ���Ƕ��ڲ�ͬ�Ĳ��������в�ͬ��ָ�롣
		��ᷢ��������ʹ�÷�����������̱�ø��ӵ����ף���Ϊ���Ѿ�ϰ��һ��û��ʹ�� flip �ķ�ʽ��
		*/
		time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
		
		/**
		 * ����һ������Ҫע����� ChannelHandlerContext.write() (�� writeAndFlush() )�����᷵��һ�� ChannelFuture ����
		 * һ�� ChannelFuture ������һ��[��û�з���]�� I/O ����������ζ���κ�һ������������������ϱ�ִ�У���Ϊ�� Netty �����еĲ��������첽�ġ�
		 *�ٸ���������Ĵ���������Ϣ������֮ǰ���ܻ��ȹر����ӡ�
    Channel ch = ...;
    ch.writeAndFlush(message);
    ch.close();
			�������Ҫ�� write() �������ص� ChannelFuture ��ɺ���� close() ������Ȼ������д�����Ѿ��������֪ͨ���ļ����ߡ���ע��,close() ����Ҳ���ܲ�������رգ���Ҳ�᷵��һ��ChannelFuture��
		 */
		 final ChannelFuture f = ctx.writeAndFlush(time); // (3)
		 
		 
		 /**
		  * ��һ��д�����Ѿ���������֪ͨ�����ǣ����ֻ��Ҫ�򵥵��ڷ��ص� ChannelFuture ������һ��ChannelFutureListener��
		  * �������ǹ�����һ�������� ChannelFutureListener �������ڲ������ʱ�ر� Channel��
		  * ���ߣ������ʹ�ü򵥵�Ԥ�������������:f.addListener(ChannelFutureListener.CLOSE);
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
     * �������Ǹ�����chanelRead()�¼��������� ÿ������˴ӿͻ����յ��µ�����ʱ�� ������������յ���Ϣʱ�����ã�
     * ��������У��յ�����Ϣ��������ByteBuf
     * 
     * @param ctx
     *            ͨ���������������Ϣ
     * @param msg
     *            ���յ���Ϣ
     */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("channelRead");
		
		// TODO �Զ����ɵķ������
		ctx.write(msg);
		ctx.flush();
		ByteBuf in=(ByteBuf) msg;
		// ��ӡ�ͻ������룬��������ĵ��ַ�
		System.out.println(in.toString(CharsetUtil.UTF_8));
		
        /**
         * ByteBuf��һ�����ü�������������������ʾ�ص���release()�������ͷš�
         * ���ס��������ְ�����ͷ����д��ݵ������������ü�������
         */
        // �����յ�������
		//in.release();(���Ҳ���ͷŴ��ݵ������������ü�������)
		ReferenceCountUtil.release(msg);
	}
	
	
    /***
     * ����������ڷ����쳣ʱ����
     * 
     * @param ctx
     * @param cause
     */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO �Զ����ɵķ������
        /**
         * exceptionCaught() �¼��������ǵ����� Throwable ����Żᱻ���ã����� Netty ���� IO
         * ������ߴ������ڴ����¼�ʱ�׳����쳣ʱ���ڴ󲿷�����£�������쳣Ӧ�ñ���¼���� ���Ұѹ����� channel
         * ���رյ���Ȼ����������Ĵ���ʽ����������ͬ�쳣��������в� ͬ��ʵ�֣�������������ڹر�����֮ǰ����һ�����������Ӧ��Ϣ��
         */
        // �����쳣�͹ر�
		cause.printStackTrace();
		ctx.close();
	}
}
