package com.concurrent.framework;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


/*Future并发设计模式（服务端先返回调用凭证，让客户端别等服务端处理，利用等待时间做别的事）
future和realdata，应该是相同类型的类，实现了统一接口。用户请求后，先做三件事，
一是新建一个future对象，
二是新建一个线程，执行realdata的耗时操作，并且在线程中realdata处理完成后，会把自己结果传递给已经建好的future对象，更新future的信息。
三是返回给用户future凭证。三个步骤中，二是和一三处于两条线程中
用户瞬间会收到一个接口对象，其实是future对象，如果此时他不用realdata，这个时候用户可能做别的了，那新起的线程正在处理realdata，
等结果好后，默默调用了future对象的信息更新方法，这些用户都感知不到。等用户要用realdata的时候，他调用返回的接口对象（也就是瞬时返回的future），调用获取信息的方法，此时realdata可能已经执行并更新完了，所以就返回了正确数据。
（例外，如果用户使用数据的时候，realdata的线程还没处理完，也就是说future里面还是没有正确数据，那此时也没办法，只有返回空）
*/
public class FutureJDKDemo {
	static class RealData implements Callable<String>{
		private String data;
		public RealData(String _s) {
			// TODO 自动生成的构造函数存根
			data=_s+"123";
		}
		
		@Override
		public String call() throws Exception {
			// TODO 自动生成的方法存根
			Thread.sleep(3000);
			System.out.println("realdata操作");
			return data;
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		FutureTask<String> futureTask=new FutureTask<>(new RealData("AAAA"));
		ExecutorService threadpool=Executors.newFixedThreadPool(1);
		threadpool.submit(futureTask);
		System.out.println("迅速提交返回-此时真正的data没有结束");
		
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		System.out.println("等待一段时间后，真正数据为"+futureTask.get());
	}
}
