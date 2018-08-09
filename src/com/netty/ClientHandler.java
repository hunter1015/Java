package com.netty;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ClientHandler extends ChannelHandlerAdapter{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO 自动生成的方法存根
		ByteBuf buf=(ByteBuf) msg;
		try {
		long currentTimeMillis=(buf.readUnsignedInt()-2208988800L)*1000L;
		System.out.println(new Date(currentTimeMillis));
		ctx.close();
		}finally {
			buf.release();
		}
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO 自动生成的方法存根
		cause.printStackTrace();
		ctx.close();
	}
}
