package com.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.lang.model.type.PrimitiveType;
import javax.print.attribute.standard.DocumentName;

/**控制并发线程数的工具类——Semaphore（信号量）。
 * Semaphore主要用于控制同时访问特定资源的线程数量，它通过协调各个线程，始终保持一定数量内的线程去使用公共资源。
 *Semaphore维护了一个许可集合，集合的数量是有限的，其中集合中的元素就是一个许可，线程拿到许可，就好比是拿到了线程执行的“通行证”可以立即被执行。
 *如果某个线程试图去从空集合中去取许可，那么就会自动阻塞；而某个线程执行完毕后，需要主动把许可归还入集合，
 *唤醒被阻塞的线程，同时保证了线程的最大并发执行数始终控制在许可集合的数目内。
 */


/*Semaphore主要用于做流量控制，特别是排队访问公用资源有限的应用场景，比如线程池、网络连接池、数据库连接池等。
举例：公司共有3台打印机，实现一个文件队列，利用Semaphore实现最大化、有序、高效的利用公共打印资源。*/
public class SemaphoreDemo {
	
	//定义一个文件类，包括文件名和文件内容两个属性，这是个实体概念，因为打印机需要打印文件，
	static class Document {
		private String dname;
		private String dcontent;

		public Document(String dname, String dcontent) {
			this.dname = dname;
			this.dcontent = dcontent;
		}

		public String getDname() {
			return dname;
		}

		public void setDname(String dname) {
			this.dname = dname;
		}

		public String getDcontent() {
			return dcontent;
		}

		public void setDcontent(String dcontent) {
			this.dcontent = dcontent;
		}

	}

	//打印队列
/*	核心是PrintQueue，在该类中创建了信号量对象。通过acquire方法控制有限的线程数量（本例为5个）去访问临界资源（本例为打印机），
	其余的线程会被阻塞等待。当一个线程完成了对临界资源的访问并释放了信号量，另外一个线程才能获得该信号量，并得到执行的许可。*/
	static class PrinterQueue {
		//Boolean数组，用于描述每个打印机的空闲状态，true为空闲，false为工作状态
		private boolean freePrinter[];
		
		//用于同步访问空闲状态数组
		private Lock printerLock;
		
		//打印机最大数目
		private int MAX_PRINTER = 5;

		//定义信号量对象
		private final Semaphore semaphore;

		public PrinterQueue() {
			// TODO 自动生成的构造函数存根
			//初始化信号量对象，包含MAX_PRINTER=5个准入凭证
			semaphore = new Semaphore(MAX_PRINTER);
			//初始化空闲数组都为空闲
			freePrinter = new boolean[MAX_PRINTER];
			for (int i = 0; i < MAX_PRINTER; i++) {
				freePrinter[i] = true;

			}
			
			//初始化锁为可重入锁
			printerLock = new ReentrantLock();
		}
		
	//打印文件的方法，需要传入文件对象，其中操作为
	//利用信号量进行流量控制，
	public void print(Document _d) {
		
		try {
			//获取空闲打印机
			semaphore.acquire();
			int printerID=getPrinter();
			System.out.println("No."+printerID+" is working now: Title=>"+_d.getDname()+";"+"Content=>"+_d.getDcontent());
			//模拟打印耗时操作
			Thread.sleep(10);
			System.out.println("No."+printerID+"'s work is done!");
			//完成打印，修改队形序号打印机空闲状态
			freePrinter[printerID]=true;
		
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			//释放线程凭证
			semaphore.release();
		}

	}
		//获取当前空闲打印机，循环遍历空闲数组，需要锁住，以免在遍历过程中被其余线程用走
		//利用锁修改资源。
		private int getPrinter() {
			int canUseNum = -1;
			try {
				//锁住
				printerLock.lock();
				for (int i = 0; i < MAX_PRINTER; i++) {
					if (freePrinter[i] == true) {
						canUseNum = i;
						freePrinter[i] = false;
						break;
					}

				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				//解锁
				printerLock.unlock();
			}
			return canUseNum;
		}
	}
	//打印任务是线程执行的目标，需要传入打印队列和打印文件。这个任务是被多线程使用的，所以其中的共享资源就是打印队列了
	//所以打印队列中需要做好并发控制，尤其是对共享资源
	static class PrintTask implements Runnable{
		private PrinterQueue printerqueue;
		private Document document;
		
		public PrintTask(PrinterQueue printerqueue, Document document) {
			this.printerqueue = printerqueue;
			this.document = document;
		}

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			//打印队列启动打印方法，打印目标文件
			printerqueue.print(document);
			System.out.println("打印\n");
		}
		
	}
	public static void main(String[] args) {
		//打印队列对象初始化，始终有一个（体现多线程对共享资源的交互）
		PrinterQueue printerQueue=new PrinterQueue();
		int THREAD_COUNT=25;
		
		//建立线程池
		ExecutorService threadpool=Executors.newFixedThreadPool(THREAD_COUNT);
		
		//线程池起多个线程打印不同文件
		for (int i = 0; i < THREAD_COUNT; i++) {
			Document doc = new Document("Title"+i, "Contents"+i);
			threadpool.execute(new PrintTask(printerQueue, doc));
		}
		//关闭线程池
		threadpool.shutdown();
		
	}
}
