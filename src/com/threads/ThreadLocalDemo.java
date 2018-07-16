package com.threads;

/**
 * ThreadLocal，即线程变量，是一个以ThreadLocal对象为key、任意对象为值的键-值对存储结构。
 * 这个结构被附带在线程上，也就是说一个线程可以根据一个ThreadLocal对象查询到绑定在这个线程上的一个值。
 * 一般地，可以通过set()方法来设置一个值，在当前线程下再通过get()方法获取到原先设置的值。
 * 是为线程提供了可以拥有自己局部变量的技术途径，该变量只有当前线程才能访问，进而也保证了线程安全的，
 * 这是典型的“用空间换时间”的思路。
 * 
 * 这里是理解重点，那就是多个线程使用同一个ThreadLocal变量，都会持有独立的变量副本
 * ThreadLocal为每个使用该变量的线程提供独立的变量副本，每个变量都维护了自己的、完全独立的一个变量副本，
 * 所以每个线程都可以独立地改变自己的副本，而不会影响其他线程所对应的副本。
 * 
 * 以下用办卡充钱来举例，多个线程用到相同的ThreadLocal
 * @author YYH
 *
 *
 *
 *个人理解，ThreadLocal是线程使用专门设置的特殊变量，允许用户使用线程的时候提取这个变量用，但是这个变量
 *是谁用他，谁只能用它的副本，不能用它的本体，所以他是特殊存在的，不会被改变，所有线程改变的只有其上的本变量副本
 *空间换时间，就是线程用这个变量，提供了一些数据，而不用在进行额外操作，减少了时间，但是增加了内存的空间消耗
 */

public class ThreadLocalDemo {

	/**
	 * //initialValue返回当前线程局部变量的初始值，该方法是一个protected方法，是为了让子类覆盖而设计；
		//该方法是一个延迟调用方法，在线程第一次调用get()或set(Object)方法时才执行，
		//并且有且只执行一次。该方法缺省实现返回为null
	 */
	//初始办卡，并且给卡里初始化充值10块
	private static ThreadLocal<Integer> Card=new ThreadLocal<Integer>() {
		public Integer initialValue() {
			return 10;
		}
	};
	//获得卡
	public ThreadLocal getThreadLocal() {
		return Card;
	}
	
	//向卡里充钱，钱余额+充值金额=新余额
	public int tranfer(int income) {
		Card.set(Card.get()+income);
		return Card.get();
	}
	
	
	//测试内部类-模拟发工资动作
	private static class Payoff extends Thread{
		
		//初始化发工资时，引入上一次定义好的卡
		private ThreadLocalDemo demo;
		public Payoff(ThreadLocalDemo _d) {
			// TODO 自动生成的构造函数存根
			demo=_d;
		}
		
		//报告卡里当前余额
		public void report(int total) {
			System.out.println(Thread.currentThread().getName()+"= 卡余额："+total);
		}
		
		//重写run函数，里面进行了三次充值操作 并且分别展示出来了
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			int total=demo.tranfer(1000);
			report(total);
			
			total=demo.tranfer(100);
			report(total);
			
			total=demo.tranfer(12);
			report(total);
			
			
			//很重要：每个线程用完以后都要执行删除操作
			//将当前线程局部变量的值删除。当线程结束后，对应该线程的局部变量将自动被GC回收
			//所以，显式调用该方法清除线程的局部变量并不是必须的操作，但是可以加快内存回收的速度
			demo.getThreadLocal().remove();
			
		}
	}
	
	//测试开始类
	public static void main(String[] args) {
		//首先初始化卡
		ThreadLocalDemo demo=new ThreadLocalDemo();
		
		//开始发工资给用户1
		Payoff user1=new Payoff(demo);
		user1.setName("用户1");
		
		//开始发工资给用户2
		Payoff user2=new Payoff(demo);
		user2.setName("用户2");
		
		//开始发工资给用户3
		Payoff user3=new Payoff(demo);
		user3.setName("用户3");
		
		user1.start();
		user2.start();
		user3.start();
		
		
	}
}

/*
 * 测试结果
用户2= 卡余额：1010
用户3= 卡余额：1010
用户1= 卡余额：1010
用户3= 卡余额：1110
用户2= 卡余额：1110
用户3= 卡余额：1122
用户1= 卡余额：1110
用户2= 卡余额：1122
用户1= 卡余额：1122
 */
