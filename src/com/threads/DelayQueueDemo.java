package com.threads;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * DelayQueue又称为延时队列。其特性如下：
-队列容量无界；不能存放null元素。

-元素必须同时实现Delayed接口与Comparable接口。元素所属类中必须实现public int compareTo(To)和long getDelay(TimeUnit unit)方法。

其中，Delayed接口继承了Comparable接口，因此必须实现compareTo方法；在public int compareTo(To)方法中，如果当前对象的延迟值小于参数对象的值，将返回一个小于0的值；如果当前对象的延迟值大于参数对象的值，将返回一个大于0的值；如果两者的延迟值相等则返回0。该队列的头部是延迟期满后保存时间最长的 Delayed 元素（也就是保证先过期的元素排在头部）。如果延迟都还没有期满，则队列头部不会被返回，即队列的 poll 方法将返回null。

long getDelay(TimeUnit unit)方法中，是返回到激活日期的剩余时间；当返回值为0或者负数时，表面该元素已经可以被获取了；单位由单位参数指定。TimeUnit类是一个由下列常量组成的枚举类型：DAYS、HOURS、MICROSECONDS、MILLISECONDS、MINUTES、NANOSECONDS和SECONDES。

-只有在延迟期满时才能从队列中提取元素；Delayed接口使得对象成为了延迟对象，它使得队列中的元素对象具有了延迟期。由于DelayQueue是一个没有大小限制的队列，因此往队列中插入数据的操作永远不会被阻塞，而只有获取数据的操作才会被阻塞。

 */

/**
 * 利用DelayQueue模拟电商业务场景中： 
 * 至用户下单成功后某个时长不进行支付，则取消该订单
 * 
 * 个人理解：订单类是实体类，定义了订单的元素。订单管理类是负责处理订单的，包括新增，删除，和运行线程。
 * 订单管理类处理的就是一个订单延迟队列，这个队列会在订单管理类的要求和动作下加入新订单或者去掉新订单
 */
public class DelayQueueDemo {
	// 定义一个订单
	//订单分解，订单编号和订单到期的时间（到期时间=当前时间+设置好的可存活时间）
	static class Order implements Delayed{
		
		private int orderID; // 订单编号
		private long LeftTime; // 用于表征取消订单的时间点
		
		//构造设置编号和时间
		public Order(int _id,long _startTime) {
			// TODO 自动生成的构造函数存根
			setId(_id);
			 // 注意：以下单时间作为记时的起点
			this.LeftTime=_startTime*1000+System.currentTimeMillis();
		}
		
		// 用于优先级排序-必须实现
		//所有涉及到排序的线程都需要用实现compareTo方法，内部如果返回值为负说明越优先
		@Override
		public int compareTo(Delayed o) {
			// TODO 自动生成的方法存根
			long diff=getDelay(TimeUnit.NANOSECONDS)-o.getDelay(TimeUnit.NANOSECONDS);
			return diff>0?1:(diff==0?0:-1);
		}

		// 用于获取距离期限时间的剩下的时长-必须实现
		@Override
		public long getDelay(TimeUnit unit) {
			// TODO 自动生成的方法存根
			return this.LeftTime-System.currentTimeMillis();
		}
		// 获取订单编号
		public int getID() {
		    return orderID;
		}
		public void setId(int _id) {
			this.orderID=_id;
			
		}
		
	}
	// 订单管理类 负责订单的操作，包括生成订单，删除订单，要求只能有一个订单管理对象存在，所以得用单例模式
	//这里的单例模式是最推荐的，使用锁来确保不会被重复创建多个对象
	static class OrderMgr{
		
		private Thread overTimeThread;
		private DelayQueue<Order> orderQueue=new DelayQueue<Order>();
		// 创建订单队列，采用延迟队列
		
		// 订单自增编号
		private static AtomicInteger ID=new AtomicInteger();
		
		// 商家自定义规则，超过5秒未支付，删除订单 
		private static final long MAX_DELAY_TIME=5;
		
		// 用于实现对OrderMgr的单例
		private static OrderMgr instance=null;
		private static ReentrantLock lock=new ReentrantLock();
		
		
		public OrderMgr() {
			// TODO 自动生成的构造函数存根
		}
		
		// 单例模式
		public static OrderMgr getInstance() {
			if (instance==null) {
				lock.lock();
				if (instance==null) {
					instance=new OrderMgr();
				}
				lock.unlock();
			}
			return instance;
		}
		
		//订单管理类的初始化
		//初始化就是要新起线程，循环从队列中找到期的订单并做处理（请注意，这里是一个线程在无限循环检查）
		public void init() {
			 //初始工作线程
			overTimeThread=new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO 自动生成的方法存根
					while(true) {
						try {
							//从延迟队列中取订单,如果没有订单过期则线程一直等待
							Order order=orderQueue.take();
							//处理过期订单
							if (order!=null) {
								doDelayOrder(order);
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							break;
						}
					}
					
				}
			});
			overTimeThread.setDaemon(true);
			overTimeThread.setName("Task Queue Daemon Thread");
			overTimeThread.start();
		}
		
		//处理到期订单，这里仅仅是打印，让大家看是否真正做到了延迟队列的作用
		public void doDelayOrder(Order _order) {
			 System.out.println("Order ID : " + _order.getID() + " cancelled at " + new Date());
			
		}
		
		//新增订单，必须生成新的订单对象，并且加入延迟队列中
		public void addOrder() {
			Order newOrder=new Order(ID.getAndIncrement(),MAX_DELAY_TIME);
			orderQueue.put(newOrder);
			System.out.println("Order ID : " + newOrder.getID() + " added at "
					+ new Date());
		}
		
		public boolean removeOrder(Order _order) {
			return orderQueue.remove(_order);
			
		}
	}
	 public static void main(String[] args) throws InterruptedException {
		 //典型单例模式，创建1个订单管理对象
	    OrderMgr mgr = OrderMgr.getInstance();
		mgr.init();
		mgr.addOrder();
		Thread.sleep(3 * 1000);
		mgr.addOrder();
		Thread.sleep(2 * 1000);
		mgr.addOrder();
		Thread.sleep(3 * 1000);
		mgr.addOrder();
			
		//保证处理完最后一个订单
		Thread.sleep(8*1000);
	    }

}
