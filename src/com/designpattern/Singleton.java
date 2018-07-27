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
    private Singleton(){
    	System.out.println("实例化完成");
    }
	
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
    
    
    
    
    
    /*
     * 方法2：Demand Holder （懒汉）线程安全，推荐使用
     * 分析：我们在类里增加一个静态(static)内部类，在该内部类中创建单例对象，
     * 再将该单例对象通过getInstance()方法返回给外部使用，
     * 由于静态单例对象没有作为Singleton的成员变量直接实例化，
     * 类加载时并不会实例化Singleton，因此既可以实现延迟加载，又可以保证线程安全，不影响系统性能
     */

    /**
     * 内部类，包含final static Singleton对象（初始化）
     * @author yangbei
     *
     */
    private static class LoadBalancerHolder {
        //在JVM中 final 对象只会被实例化一次,无法修改
        private final static Singleton INSTANCE = new Singleton();
    }
    
    /**
     * getInstance2() 方法，返回内部类的final 静态Singleton对象
     * @return
     */
    public static Singleton getInstance2() {
        return LoadBalancerHolder.INSTANCE;
    }
    
    
    
    
    /*
     * 方法3  枚举特性（懒汉）线程安全，推荐使用
     * 相比上一种，该方式同样是用到了JAVA特性：枚举类保证只有一个实例（即使使用反射机制也无法多次实例化一个枚举量）
     */
    enum Lazy {
        INSTANCE;
        private Singleton loadBalancer;

    	//枚举的特性,在JVM中只会被实例化一次
        Lazy() {
            loadBalancer = new Singleton();
        }

        public Singleton getInstance() {
            return loadBalancer;
        }
    }
    
    public static void main(String[] args) {
    	
    	//方法1 双重检查，lock
    	
    	//方法2  内部类初始化final static对象实例
    	
    	
    	//方法3 ，利用枚举来实例化单例对象
		Lazy.INSTANCE.getInstance();
	}
}
