package com.threads;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * DelayQueue�ֳ�Ϊ��ʱ���С����������£�
-���������޽磻���ܴ��nullԪ�ء�

-Ԫ�ر���ͬʱʵ��Delayed�ӿ���Comparable�ӿڡ�Ԫ���������б���ʵ��public int compareTo(To)��long getDelay(TimeUnit unit)������

���У�Delayed�ӿڼ̳���Comparable�ӿڣ���˱���ʵ��compareTo��������public int compareTo(To)�����У������ǰ������ӳ�ֵС�ڲ��������ֵ��������һ��С��0��ֵ�������ǰ������ӳ�ֵ���ڲ��������ֵ��������һ������0��ֵ��������ߵ��ӳ�ֵ����򷵻�0���ö��е�ͷ�����ӳ������󱣴�ʱ����� Delayed Ԫ�أ�Ҳ���Ǳ�֤�ȹ��ڵ�Ԫ������ͷ����������ӳٶ���û�������������ͷ�����ᱻ���أ������е� poll ����������null��

long getDelay(TimeUnit unit)�����У��Ƿ��ص��������ڵ�ʣ��ʱ�䣻������ֵΪ0���߸���ʱ�������Ԫ���Ѿ����Ա���ȡ�ˣ���λ�ɵ�λ����ָ����TimeUnit����һ�������г�����ɵ�ö�����ͣ�DAYS��HOURS��MICROSECONDS��MILLISECONDS��MINUTES��NANOSECONDS��SECONDES��

-ֻ�����ӳ�����ʱ���ܴӶ�������ȡԪ�أ�Delayed�ӿ�ʹ�ö����Ϊ���ӳٶ�����ʹ�ö����е�Ԫ�ض���������ӳ��ڡ�����DelayQueue��һ��û�д�С���ƵĶ��У�����������в������ݵĲ�����Զ���ᱻ��������ֻ�л�ȡ���ݵĲ����Żᱻ������

 */

/**
 * ����DelayQueueģ�����ҵ�񳡾��У� 
 * ���û��µ��ɹ���ĳ��ʱ��������֧������ȡ���ö���
 * 
 * ������⣺��������ʵ���࣬�����˶�����Ԫ�ء������������Ǹ��������ģ�����������ɾ�����������̡߳�
 * ���������ദ��ľ���һ�������ӳٶ��У�������л��ڶ����������Ҫ��Ͷ����¼����¶�������ȥ���¶���
 */
public class DelayQueueDemo {
	// ����һ������
	//�����ֽ⣬������źͶ������ڵ�ʱ�䣨����ʱ��=��ǰʱ��+���úõĿɴ��ʱ�䣩
	static class Order implements Delayed{
		
		private int orderID; // �������
		private long LeftTime; // ���ڱ���ȡ��������ʱ���
		
		//�������ñ�ź�ʱ��
		public Order(int _id,long _startTime) {
			// TODO �Զ����ɵĹ��캯�����
			setId(_id);
			 // ע�⣺���µ�ʱ����Ϊ��ʱ�����
			this.LeftTime=_startTime*1000+System.currentTimeMillis();
		}
		
		// �������ȼ�����-����ʵ��
		//�����漰��������̶߳���Ҫ��ʵ��compareTo�������ڲ��������ֵΪ��˵��Խ����
		@Override
		public int compareTo(Delayed o) {
			// TODO �Զ����ɵķ������
			long diff=getDelay(TimeUnit.NANOSECONDS)-o.getDelay(TimeUnit.NANOSECONDS);
			return diff>0?1:(diff==0?0:-1);
		}

		// ���ڻ�ȡ��������ʱ���ʣ�µ�ʱ��-����ʵ��
		@Override
		public long getDelay(TimeUnit unit) {
			// TODO �Զ����ɵķ������
			return this.LeftTime-System.currentTimeMillis();
		}
		// ��ȡ�������
		public int getID() {
		    return orderID;
		}
		public void setId(int _id) {
			this.orderID=_id;
			
		}
		
	}
	// ���������� ���𶩵��Ĳ������������ɶ�����ɾ��������Ҫ��ֻ����һ���������������ڣ����Ե��õ���ģʽ
	//����ĵ���ģʽ�����Ƽ��ģ�ʹ������ȷ�����ᱻ�ظ������������
	static class OrderMgr{
		
		private Thread overTimeThread;
		private DelayQueue<Order> orderQueue=new DelayQueue<Order>();
		// �����������У������ӳٶ���
		
		// �����������
		private static AtomicInteger ID=new AtomicInteger();
		
		// �̼��Զ�����򣬳���5��δ֧����ɾ������ 
		private static final long MAX_DELAY_TIME=5;
		
		// ����ʵ�ֶ�OrderMgr�ĵ���
		private static OrderMgr instance=null;
		private static ReentrantLock lock=new ReentrantLock();
		
		
		public OrderMgr() {
			// TODO �Զ����ɵĹ��캯�����
		}
		
		// ����ģʽ
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
		
		//����������ĳ�ʼ��
		//��ʼ������Ҫ�����̣߳�ѭ���Ӷ������ҵ��ڵĶ�������������ע�⣬������һ���߳�������ѭ����飩
		public void init() {
			 //��ʼ�����߳�
			overTimeThread=new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO �Զ����ɵķ������
					while(true) {
						try {
							//���ӳٶ�����ȡ����,���û�ж����������߳�һֱ�ȴ�
							Order order=orderQueue.take();
							//������ڶ���
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
		
		//�����ڶ�������������Ǵ�ӡ���ô�ҿ��Ƿ������������ӳٶ��е�����
		public void doDelayOrder(Order _order) {
			 System.out.println("Order ID : " + _order.getID() + " cancelled at " + new Date());
			
		}
		
		//�������������������µĶ������󣬲��Ҽ����ӳٶ�����
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
		 //���͵���ģʽ������1�������������
	    OrderMgr mgr = OrderMgr.getInstance();
		mgr.init();
		mgr.addOrder();
		Thread.sleep(3 * 1000);
		mgr.addOrder();
		Thread.sleep(2 * 1000);
		mgr.addOrder();
		Thread.sleep(3 * 1000);
		mgr.addOrder();
			
		//��֤���������һ������
		Thread.sleep(8*1000);
	    }

}
