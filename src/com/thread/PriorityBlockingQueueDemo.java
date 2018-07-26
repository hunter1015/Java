package com.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/*PriorityBlockingQueue又称为优先级阻塞队列。其特性如下：
	-无界。由于容量不存在限制(最大为Integer.MAX_VALUE - 8，可基本认为是无限制)，队列就不会阻塞生产者，
因为只要能生产数据，就可以把数据放入队列中；如果生产者速度远远大于消费者速度，由于容量无限，那么系统内存有可能会被消耗殆尽。
	-元素必须实现Comparable接口。在实现的CompareTo方法中，如果返回值为负数，则表明当前对象this的优先级越高；返回值为正数，则表明当前对象this的优先级越低。
	-实现了有序列表。优先级的判断通过构造函数传入的Compator对象实现队列元素的自定义排序；如果Compator为空，则按对象的比较方法进行排序。队列中元素的优先级依次降低，优先级最高的排在队首。
	-仅阻塞消费者。当队列中无数据时，会阻塞消费者。
	-队列元素不允许为null。*/

public class PriorityBlockingQueueDemo {

	//定义任务，任务包含名称和优先级两个属性
	static class Task implements Comparable<Task>{
		private String taskName;
		private int priority;
		public Task(String _name,int _priority) {
			// TODO 自动生成的构造函数存根
			taskName=_name;
			priority=_priority;
		}
		
		//重写compareTo方法，明确任务间如何比较优先级，记住：返回值为负说明优先级高！
		@Override
		public int compareTo(Task o) {
			// TODO 自动生成的方法存根
			if(this.priority>o.getPriority()) {
				return -1;
			}else {
				return 1;
			}
		}
		public String getTaskName() {
			return taskName;
		}
		public int getPriority() {
			return priority;
		}
		
		
	}
	
	//模拟任务生成器，给优先阻塞队列里塞进去各种任务
	static class TaskGenerator implements Runnable{
		private PriorityBlockingQueue<Task> pqueue;
		
		//定义队列任务最大数量
		private static final int MAX_TASK_NUMBER=100;
		
		public TaskGenerator(PriorityBlockingQueue<Task> pqueue) {
			// TODO 自动生成的构造函数存根
			this.pqueue=pqueue;
		}
		@Override
		public void run() {
			// TODO 自动生成的方法存根
		    for(int  i=0;i<MAX_TASK_NUMBER;i++ ){
		    	//新建任务对象，赋值属性
		        Task task = new Task("Task"+i, i %10);
		        //把任务对象塞入优先级阻塞队列里！
		        pqueue.put(task);
		    }
			
		}
		
	}
	
	//模拟任务调度系统，也就是线程循环执行任务队列的任务，取出来，操作，如此循环，注意实现runnable接口
	static class TaskScheduler implements Runnable{
		private BlockingQueue<Task> queue;
		public TaskScheduler(BlockingQueue<Task> queue) {
			// TODO 自动生成的构造函数存根
			this.queue=queue;
		}
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			try {
				while(true) {
					doTask(queue.take());
				}
			} catch (Exception e) {
				// TODO: handle exception
				Thread.currentThread().interrupt();
			}

		}
		//模拟处理任务的实际业务，这里仅仅是打印出来给大家看
		public void doTask(Task _t) {
			 System.out.println("DoTask = >TaskName: "+_t.getTaskName()+"; Priority: "+_t.getPriority());
		}
		
	}
	
	//任务系统，更上层业务系统，负责启动生成任务模块和任务调度模块
	public static void taskSys() throws Exception {
		
		//请一定注意，初始化队列一定要操作，置空不代表生成了对象
		PriorityBlockingQueue<Task> queue =new PriorityBlockingQueue<Task>();
		
		new Thread(new TaskGenerator(queue)).start();
		Thread.sleep(1);
		
		new Thread(new TaskScheduler(queue)).start();
	}
	public static void main(String[] args) throws Exception{
		taskSys();
	}
}
