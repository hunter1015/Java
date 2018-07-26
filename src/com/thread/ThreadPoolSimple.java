package com.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolSimple {
    //构建一个线程池
	private static final ExecutorService thExecutorService=new ThreadPoolExecutor(1, 
			Runtime.getRuntime().availableProcessors(), 
			60, 
			TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), 
			new ThreadFactory() {
				
				@Override
				public Thread newThread(Runnable r) {
					// TODO 自动生成的方法存根
					//这里其实实在自定义接口工厂创建新线程的方法！！
					Thread t1=new Thread(r, "sendThread");
					return t1;
				}
			},new ThreadPoolExecutor.DiscardPolicy());
	
	public void sendTask(final String phoneNumber) {
		thExecutorService.submit(new Runnable() {
			public void run() {
				sendMessage(phoneNumber);
			}
		});
	}
	
	private void sendMessage(String phone_number) {
		
		System.out.println("发送手机号为"+phone_number);
	}
	
	public static void main(String[] args) {
		ThreadPoolSimple t1=new ThreadPoolSimple();
		t1.sendTask("11111");
		t1.sendTask("22222");
		t1.sendTask("33333");
		t1.sendTask("44444");
	}
}
