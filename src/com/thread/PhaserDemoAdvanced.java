package com.thread;

import java.util.concurrent.Phaser;

public class PhaserDemoAdvanced {
	static class MyPhaser extends Phaser{
		//覆盖onAdvance方法
		//该函数在phaser对象阶段改变时候被自动执行
		//可理解为CycleBarrier中的barrierAction
		//个人理解：所有线程某个阶段到达集合点，Phaser会识别这是第几阶段，数字表明，然后自动执行一些任务
		//这里是覆写了自动执行任务的方法，加入了自定义方法
		@Override
		protected boolean onAdvance(int phase, int registeredParties) {
			// TODO 自动生成的方法存根
			switch (phase) {
			case 0:
				return studentsArrived();
			case 1:
				return finishFirstExercise();
			case 2:
				return finishSecondExercise();
			case 3:
				return finishThirdExercise();
			case 4:
				return finishExam();
			default:
				return false;
			}
		}
		private boolean studentsArrived() {
			int phaserNum=this.getPhase();
			System.out.println("Phase"+phaserNum+"-全体学生已经集合");
			return false;
		}
		private boolean finishFirstExercise() {
			int phaserNum=this.getPhase();
			System.out.println("Phase"+phaserNum+"-全体学生完成第一题");
			return false;
		}
		private boolean finishSecondExercise() {
			int phaserNum=this.getPhase();
			System.out.println("Phase"+phaserNum+"-全体学生完成第二题");
			return false;
		}
		private boolean finishThirdExercise() {
			int phaserNum=this.getPhase();
			System.out.println("Phase"+phaserNum+"-全体学生完成第三题");
			return false;
		}
		private boolean finishExam() {
			int phaserNum=this.getPhase();
			System.out.println("Phase"+phaserNum+"-全体学生完成本次考试");
			return true;
			
		}
		}

	static class Student implements Runnable{
		private Phaser phaser;
		private int ID;
		public Student(int ID,Phaser phaser) {
			// TODO 自动生成的构造函数存根
			this.phaser=phaser;
			this.ID=ID;
		}
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			phaser.arriveAndAwaitAdvance();
			doFirst();
			phaser.arriveAndAwaitAdvance();
			doSecond();
			phaser.arriveAndAwaitAdvance();
			doThird();
			phaser.arriveAndAwaitAdvance();
			finishExam();
			
		}
		
		public void doFirst() {
			System.out.println("学生"+ID+"完成第一题");
		}
		public void doSecond() {
			System.out.println("学生"+ID+"完成第二题");
		}
		public void doThird() {
			System.out.println("学生"+ID+"完成第三题");
		}
		public void finishExam() {
			System.out.println("学生"+ID+"完成考试");
		}
	}
	public static void main(String[] args) {
		Phaser phaser=new Phaser(4);
		for (int i = 0; i < 4; i++) {
			new Thread(new Student(i, phaser)).start();
			
		}
	}
}
