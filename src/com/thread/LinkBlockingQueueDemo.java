package com.thread;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.lang.model.type.PrimitiveType;
import javax.xml.ws.AsyncHandler;

/**
 * 生产者-消费者模式的首选——LinkedBlockingQueue
 * LinkedBlockingQueue又称为链表阻塞队列。其特性如下：
	-有界，也可认为是无界。LinkedBlockingQueue是基于链表实现的阻塞队列；如果在构造函数不指定容量，则默认为一个类似无限大小的容量，
大小为Integer.MAX_VALUE，在此种情况下，如果生产者速度远远大于消费者速度，由于容量多大，那么系统内存有可能会被消耗殆尽；如果在构造函数指定了容量大小，则队列容纳的元素数量固定。
	-FIFO。队列的获取操作作用于队列头部，添加操作作用于队列尾部，满足FIFO特性
	-自动阻塞唤醒。put操作试图向已满队列放入元素将导致操作阻塞，直到其他线程从队列中获取元素，
队列出现空闲后再唤醒操作；take操作试图从空队列中获取元素同样将导致操作阻塞等待，直到其他线程从队列中添加元素，队列中存在元素后再唤醒操作。
	-锁分离机制。对添加数据的操作与获取数据的操作分别采用了独立的、不同的锁来控制数据同步
（ArrayBlockingQueue采用了同一把锁），即在高并发的情况下生产者和消费者可以并行的操作队列中的数据，
进而提高整个队列的并发性能。
 * @author yangbei
 *
 */
public class LinkBlockingQueueDemo {

	static class FileCrawler implements Runnable{
		private BlockingQueue<File> filequeue;
		private FileFilter fileFilter;
		private File file;
		
		public FileCrawler(BlockingQueue<File> _q,FileFilter _filter,File _f) {
			// TODO 自动生成的构造函数存根
			filequeue=_q;
			_filter=fileFilter;
			file=_f;
		}
		//递归遍历目录下所有符合筛选条件的文件，文件爬虫  筛选条件 filefilter
		public void crawler(File root) throws InterruptedException {
			
			File[] files=root.listFiles(this.fileFilter);//筛选符合条件的文件
			if (files!=null) {
				for(File _f:files) {
					if (_f.isDirectory()) {
						crawler(_f);//如果是目录，则递归进入
					}
					else {
						filequeue.put(_f);//符合条件的文件放入链表阻塞队列中
					}
				}
			}
		}

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			try {
				crawler(file);//启动文件系统爬虫
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	
	//建立文件索引
	static class Indexer implements Runnable{
		private BlockingQueue<File> fileQueue;
		
		public Indexer(BlockingQueue<File> fileQueue) {
			// TODO 自动生成的构造函数存根
			this.fileQueue=fileQueue;
		}
		//模拟对文件建立索引的操作
		private void indexFile(File _file) {
			System.out.println("Process for index file : " + _file.getAbsolutePath());
			
		}
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			try {
				while (true) {
					
						indexFile(fileQueue.take());
				}
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		
		
	}
	//同时启动文件系统爬虫和建立索引，基于链表阻塞队列的put和take使用两种锁，互不影响
	public static void doIndex(File root) {
		BlockingQueue<File> filequeue=new LinkedBlockingQueue<File>(100);
		//设置文件过滤器
		FileFilter filter=new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				// TODO 自动生成的方法存根
				if (pathname!=null&&pathname.getName().endsWith(".txt")) {
					return true;
				}
				else {
					return false;
				}
				
			}
		};
		//文件系统爬虫
		new Thread(new FileCrawler(filequeue, filter, root)).start();
		
		for (int i = 0; i < 10; i++) {
			new Thread(new Indexer(filequeue)).start();
			
		}
		
	}
	public static void main(String[] args) {
		File file=new File("/temp");
		System.out.println("start\n");
		doIndex(file);
	}
}
