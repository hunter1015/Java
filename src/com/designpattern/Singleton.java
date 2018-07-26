package com.designpattern;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * @author YYH
 *单例模式是23种基本设计模式之一，也是一个比较简单的模式；
 *所谓单例模式，就是确保某一个类只有一个实例，而且自行实例化并向整个系统提供这个实例。
	由于是只生成一个实例的模式，也被称作Singleton模式。(单身)
	通过使用private的构造函数确保了在一个应用中只产生一个实例，并且是自行实例化。
	
	不用synchronized是因为她是一个性能开销较高的解决方案
 */
public class Singleton {
    //定义为static是为了保证对象是共享的
    private static Singleton instance = null;
    private static ReentrantLock lock = new ReentrantLock();
	
    //如果定义为public，则就可创建任意数量对象
    private Singleton(){}
	
    public static Singleton getInstance(){
        if(null == instance){
	    //利用ReentrantLock替换Synchronized
	    lock.lock();
	    if(null == instance){
	        instance = new Singleton();
	    }
	    lock.unlock();
        }
	return instance;
    }
}
