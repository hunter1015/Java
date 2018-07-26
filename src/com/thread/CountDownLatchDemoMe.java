package com.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.lang.model.type.PrimitiveType;

public class CountDownLatchDemoMe {

	static class MeetingRoom implements Runnable{
		private CountDownLatch countdownlatch;
		private void setMeetingNum(int _num) {
			countdownlatch=new CountDownLatch(_num);
		}
/*		public MeetingRoom(CountDownLatch countdownlatch) {
			// TODO 自动生成的构造函数存根
			this.countdownlatch=countdownlatch;
		}*/
		public MeetingRoom(int totalNum) {
			// TODO 自动生成的构造函数存根
			setMeetingNum(totalNum);
		}
		public void arrive(String name) {
			countdownlatch.countDown();
			System.out.println("参会人员"+name+"到达会场\n");
		}
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			try {
				countdownlatch.await();
				System.out.println("所有人已到达会场");
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		
		
	}
	
	static class People implements Runnable{
		private String name;
		public MeetingRoom meetingroom;
		
		public People(String name, MeetingRoom meetingroom) {
			this.name = name;
			this.meetingroom = meetingroom;
		}

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			meetingroom.arrive(name);
		}
		
	}
	public static void main(String[] args) {
		//CountDownLatch countDownLatch=new c
		MeetingRoom meetingRoom=new MeetingRoom(8);
		int number=0;
		Thread meetingroomthread=new Thread(meetingRoom);
		meetingroomthread.start();
		for(int i=0;i<10;i++){
		    People people = new People("People "+i,meetingRoom);
		    Thread p = new Thread(people);
		    //开始参会人员到会工作线程
		    p.start();
		}
	}
}
