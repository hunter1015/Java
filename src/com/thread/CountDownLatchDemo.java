package com.thread;

import java.util.concurrent.CountDownLatch;

/*CountDownLatch是一个同步控制工具类，也称为同步计数器。用来同步执行多个任务的一个或多个线程。到0后将不起作用，要重新
 * 创建新对象
其作用在于：在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。
如果想等待K个工作完成，在构造函数中就传入初始计数K。调用了countDown()方法，其计数会减少1，
await()方法使当前线程在计数器减少到0之前一直被阻塞等待，除非线程被中断。
一旦计数减少到零之后，会唤醒所有等待的线程，await的所有后续调用都将立即返回。（）*/

/** 本例子：某公司需要进行视频会议，参会人数为8人，需要8人到齐后才能开始开会。
 * 个人分析：会议室一个线程，等待、参会人员每个都是线程，和会议室线程有关联，触发会议室线程减少等待次数
 *
 */
public class CountDownLatchDemo {
	
    //视频会议厅类
    static class VideoMeeting implements Runnable{
	private CountDownLatch controller;
			
	public VideoMeeting(int number){
	    controller = new CountDownLatch(number);
	}
		
	//name参会者进入视频会议厅
	public void arrive(String name){
	    System.out.printf("%s has been arrived!\n",name);
	    //参会人员计数减少1
	    controller.countDown();
			
	    System.out.printf("VideoMeeting is waiting for " +
			"left %d peoples!\n",controller.getCount());
	}
		
	//视频会议厅工作线程
	//1 等待所有参会者到会
	//2 开始开会
	@Override
	public void run() {
	    System.out.println("VideoMeeting is begined! Wait " +
			"for " + controller.getCount() +" peoples!");
	    try{
	        //等待参会人员都必须到齐，
		//也就需要等待计数器归零，否则一直阻塞在此
		controller.await();
				
		System.out.println("All people is arrived! VideoMeeting is started!");
		System.out.println("Let us begin ......");
				
		//模拟一个开会过程的耗时操作
		Thread.sleep(1000);
				
	    }catch(InterruptedException ex){
		ex.printStackTrace();
	    }
        }	
    }
	
    //参会人员类
    static class People implements Runnable{
		
	private VideoMeeting meeting;
	private String name;
		
	public People(VideoMeeting m,String n){
	    this.meeting = m;
	    this.name = n;
	}
		
	//参会人员进入会议室
	@Override
	public void run() {
	    long time = (long)(Math.random()*10);
	    try{
		Thread.sleep(time);
	    }catch(InterruptedException ex){
		ex.printStackTrace();
	    }
	    meeting.arrive(name);
        }
    }

    public static void main(String[] args) {
	int MAX_PEOPLE_COUNT = 8;
	VideoMeeting meeting = new VideoMeeting(MAX_PEOPLE_COUNT);
		
	//开启会议厅工作线程
	Thread meetingThread = new Thread(meeting);
	meetingThread.start();
		
	for(int i=0;i<MAX_PEOPLE_COUNT;i++){
	    People people = new People(meeting,"People "+i);
	    Thread p = new Thread(people);
	    //开始参会人员到会工作线程
	    p.start();
	}
    }
}