package com.threads;

import java.util.concurrent.ArrayBlockingQueue;

/**
 *ArrayBlockingQueue又称为数组阻塞队列。其特性如下：
有界。ArrayBlockingQueue是基于数组实现的有界阻塞队列；其能容纳的元素数量固定，一旦创建，就不能再增加其容量；
FIFO。队列的获取操作作用于队列头部，添加操作作用于队列尾部，满足FIFO特性；
自动阻塞唤醒。试图向已满队列放入元素将导致操作阻塞，试图从空队列中获取元素同样将导致操作阻塞等待。

适用场景：常用于生产者-消费者场景中的“有界缓存区”，即两者之间需要共享的元素数量有限的情况。
 */

public class ArrayBlockingQueueDemo {

	public static void main(String[] args) throws InterruptedException {
		//建立一个有限容量的数组阻塞队列
		final ArrayBlockingQueue<String> bQueue = new ArrayBlockingQueue<String>(12);
		//准备往队列里塞些内容
		for (int i = 0; i < 6; i++) {
			//将日志信息写入队列
			bQueue.put("log info is " + i);
		}
		
		//开始6个线程
		for (int i = 0; i < 6; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO 自动生成的方法存根
					while (true) {

						try {
							tolog(bQueue.take());
						} catch (InterruptedException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					}

				}
			}).start();
		}
	}

	//打印出来
	public static void tolog(String _msg) {
		System.out.println("Log-" + _msg);

	}

}
