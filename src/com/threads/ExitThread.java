package com.threads;

public class ExitThread {
	

	static class threadDemo implements Runnable{
		//����1���ñ�־λ���ж��Ƿ�Ҫֹͣ���̶߳��ڼ�鱾��־λ��
		/*
		//�ñ�ʶΪ��������Ϊvolatile����֤�ڴ�Ŀɼ���
		private volatile boolean cancled=false;
		
		public void cancle() {
			cancled=true;
		}
		

		@Override
		public void run() {
			// TODO �Զ����ɵķ������
			while(!cancled) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
			
		}
		*/
		
		/**
		 * Java�жϻ��ơ�Java�жϻ�����һ���̼߳��Э�����ƣ�Ҳ����˵ͨ���жϲ�����ֱ����ֹ����һ���̣߳�
		 * ������Ҫ���жϵ��߳��Լ������жϣ�����Ҳ�͸��˱��жϵ��߳��㹻�Ļ�����ʱ��ȥ�����Լ����жϵġ��ƺ󡱡�
		����������ʶһ��Java�߳��жϻ����ṩ����������API��
		public void interrupt()���ж��̣߳��޷���ֵ����Ψһ�ܽ��߳��ж�״̬����Ϊtrue�ķ���
		public boolean isInterrupted()���ж��߳��Ƿ��Ѿ��жϡ����жϷ���true�����򷵻�false���̵߳��ж�״ֵ̬���ܸú���Ӱ��
		public static boolean interrupted()���жϵ�ǰ�߳��Ƿ��Ѿ��жϡ���̬������
		�÷�����isInterrupted��ͬΪ�����ж�״̬���ܺ��������Ǳ����������������ܡ��̵߳��ж�״̬�ɸ÷��������
		����������ε��ø÷�������ڶ��ε��ý�����false����Ϊ��һ�ε��õ�ʱ����false->true���ڶ��ε��ú���true->false��
		*/
		@Override
		public void run() {
			// TODO �Զ����ɵķ������
			
			//����interrupt������ζ������ֹͣĿ���߳����ڽ��еĹ�������ֻ�Ǵ����������жϵ���Ϣ��
			//Ȼ�����߳�����һ�����ʵ�ʱ���ж��Լ�������������whileѭ��Ҫ���ж���Ϊ�������
			while(!Thread.currentThread().isInterrupted()) {
				//ֻҪû�б��жϣ���һֱ���Լ����£�
			}
			
		}
	}
	public static void main(String[] args) throws InterruptedException {
		threadDemo t1=new threadDemo();
		Thread thread1=new Thread(t1);
		thread1.start();
		Thread.sleep(1000);
		
		//�߳��ж�
		thread1.interrupt();
		System.out.println("Main: �߳��ж�״̬->"+thread1.isInterrupted());
		
		
	}
}
