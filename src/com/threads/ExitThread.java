package com.threads;

public class ExitThread {
	

	static class threadDemo implements Runnable{
		//方法1，用标志位来判断是否要停止，线程定期检查本标志位，
		/*
		//该标识为必须声明为volatile，保证内存的可见性
		private volatile boolean cancled=false;
		
		public void cancle() {
			cancled=true;
		}
		

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			while(!cancled) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			
		}
		*/
		
		/**
		 * Java中断机制。Java中断机制是一种线程简的协作机制，也就是说通过中断并不能直接终止另外一个线程，
		 * 而是需要被中断的线程自己处理中断，这样也就给了被中断的线程足够的机会与时间去处理自己被中断的“善后”。
		我们先来认识一下Java线程中断机制提供的三个核心API：
		public void interrupt()：中断线程，无返回值；是唯一能将线程中断状态设置为true的方法
		public boolean isInterrupted()：判断线程是否已经中断。已中断返回true，否则返回false。线程的中断状态值不受该函数影响
		public static boolean interrupted()：判断当前线程是否已经中断。静态方法。
		该方法与isInterrupted虽同为测试中断状态功能函数，但是本方法还有其他功能。线程的中断状态由该方法清除。
		如果连续两次调用该方法，则第二次调用将返回false，因为第一次调用的时候，是false->true，第二次调用后，是true->false。
		*/
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			
			//调用interrupt并不意味着立即停止目标线程正在进行的工作，而只是传递了请求中断的消息。
			//然后由线程在下一个合适的时刻中断自己。所以在这里while循环要把中断作为检查条件
			while(!Thread.currentThread().isInterrupted()) {
				//只要没有被中断，就一直做自己的事！
			}
			
		}
	}
	public static void main(String[] args) throws InterruptedException {
		threadDemo t1=new threadDemo();
		Thread thread1=new Thread(t1);
		thread1.start();
		Thread.sleep(1000);
		
		//线程中断
		thread1.interrupt();
		System.out.println("Main: 线程中断状态->"+thread1.isInterrupted());
		
		
	}
}
