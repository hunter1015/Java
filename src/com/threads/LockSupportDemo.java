package com.threads;

import java.util.concurrent.locks.LockSupport;

//如果不慎使用suspend，可能会出现线程被永久性挂起
public class LockSupportDemo {
	
  static class SynchronizedSub{
	//注意：此处被synchronized修饰
	//进入该方法，意味着必须先持有SynchronizedSub的对象锁
	public synchronized void print(){
	    String currentThreadName = Thread.currentThread().getName();
	    System.out.println("线程"+currentThreadName+"被suspend！");
	    //由于threadA先执行，为此执行suspend方法意味着threadA将被阻塞
	    //在未调用resume之前，threadA将一直保持SynchronizedSub的对象锁，
	    //其他尝试获取该锁的线程将被阻塞
	    
	    //使用suspend方法
	    //Thread.currentThread().suspend();
	    
	    //使用locksupprot进行阻塞
	    LockSupport.park();
	    System.out.println("线程"+currentThreadName+"被唤醒后继续执行！");
	}
  }
	
  public static void main(String[] args) throws Exception{
	final SynchronizedSub sub = new SynchronizedSub();
		
	//线程A
	final Thread threadA = new Thread(){
	    //如果进入run方法，意味着threadA已经持有SynchronizedSub对象锁
	    public void run(){
		sub.print();
	    }
	};
	threadA.setName("A");
	threadA.start();
		
	Thread.sleep(1000);

	//线程B
	Thread threadB = new Thread(){
	    public void run(){
		//调用该方法，threadB将去竞争获取SynchronizedSub对象锁
		sub.print();
	    }
	};
	threadB.setName("B");
	
	threadB.start();
		
	//相继唤醒threadA、threadB，这种suspend式的使用激活线程会导致B无法请求到SynchronizedSub对象锁
	//因为B先请求了锁，后才执行激活   导致死锁
	//threadB.resume();
	//threadA.resume();
	
	
	//LockSupport的方式唤醒线程
	LockSupport.unpark(threadA);
	LockSupport.unpark(threadB);
		
	threadA.join();
	threadB.join();
  }
}
