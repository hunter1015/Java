package com.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

/*Exchanger是一个用于线程间协作的同步控制工具类；可以在两个线程之间交换数据，并仅限于2个线程之间。安全、高效地实现两个线程间的数据传递
它提供一个同步点，在这个同步点，两个线程可以交换彼此的数据。
当线程A调用Exchange对象的exchange()方法后，它会陷入阻塞状态，直到线程B也调用了exchange()方法，此刻表示两个线程都达到了同步点
然后两个线程就会以线程安全的方式进行数据交换，数据交换完成后，线程A和B继续各自运行。

代码例子
生产者制造数据，并将生产的数据以线程安全的方式传递给消费者。
个人理解：必须得有两个线程，得有一个相同类型的数据进行交换或所谓更新*/

public class ExchangerDemo {
    //生产者
    static class Producer implements Runnable{
	//生产者制造的数据集
	private List<String> buffer;
		
	//用于同步生产者和消费者的交换对象
	private Exchanger<List<String>> exchanger;
		
	public Producer(List<String> buf,Exchanger<List<String>> ex){
	    this.buffer = buf;
	    this.exchanger = ex;
	}
		
	@Override
	public void run() {
	    //模拟生产者制造的数据
	    for(int i=0;i<5;i++){
		buffer.add("Produce Data : "+i);
	    }
			
	    System.out.println("与消费者交换数据前，生产者缓冲区数据条目数为:"+buffer.size());
			
	    try{
	        //调用exchange方法与消费者进行数据交换
		//注意：数据交换后，该缓冲区将被清空
		buffer = exchanger.exchange(buffer);
	    }catch(InterruptedException e){
		e.printStackTrace();
	    }
			
	    System.out.println("与消费者交换数据后，生产者缓冲区数据条目数为:"+buffer.size());
        }
    }
	
    //消费者
    static class Consumer implements Runnable{
	//消费者制造的数据集
	private List<String> buffer;
		
	//用于同步生产者和消费者的交换对象
	private Exchanger<List<String>> exchanger;
		
	public Consumer(List<String> buf,Exchanger<List<String>> ex){
	    this.buffer = buf;
	    this.exchanger = ex;
	}
		
	@Override
	public void run() {
			
	    System.out.println("与生产者交换数据前，消费者缓冲区数据条目数为:"+buffer.size());
			
	    try{
	        //调用exchange方法与生产者进行数据交换
		//注意：调用该方法后，该缓冲区的数据将被生产者的数据所填充
		buffer = exchanger.exchange(buffer);
	    }catch(InterruptedException e){
		e.printStackTrace();
	    }
			
	    System.out.println("与生产者交换数据后，消费者缓冲区数据条目数为:"+buffer.size()+";具体内容如下：");
			
	    for(int i=0;i<5;i++){
		String msg = buffer.get(i);
		System.out.println(msg);
	    }
			
	}
    }
	
    //客户端测试
    public static void main(String args[]){
	List<String> buf1 = new ArrayList<String>();
	List<String> buf2 = new ArrayList<String>();
	Exchanger<List<String>> exchanger = new Exchanger<List<String>>();
		
	//实例化生产者线程
	Producer producer = new Producer(buf1,exchanger);
	Thread producerThread = new Thread(producer);
		
	//实例化消费者线程
	Consumer consumer = new Consumer(buf2,exchanger);
	Thread consumerThread = new Thread(consumer);
		
	//开启生产者线程
	producerThread.start();
	//开启消费者线程
	consumerThread.start();
    }
}
