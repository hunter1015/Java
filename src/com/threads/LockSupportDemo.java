package com.threads;

import java.util.concurrent.locks.LockSupport;

//�������ʹ��suspend�����ܻ�����̱߳������Թ���
public class LockSupportDemo {
	
  static class SynchronizedSub{
	//ע�⣺�˴���synchronized����
	//����÷�������ζ�ű����ȳ���SynchronizedSub�Ķ�����
	public synchronized void print(){
	    String currentThreadName = Thread.currentThread().getName();
	    System.out.println("�߳�"+currentThreadName+"��suspend��");
	    //����threadA��ִ�У�Ϊ��ִ��suspend������ζ��threadA��������
	    //��δ����resume֮ǰ��threadA��һֱ����SynchronizedSub�Ķ�������
	    //�������Ի�ȡ�������߳̽�������
	    
	    //ʹ��suspend����
	    //Thread.currentThread().suspend();
	    
	    //ʹ��locksupprot��������
	    LockSupport.park();
	    System.out.println("�߳�"+currentThreadName+"�����Ѻ����ִ�У�");
	}
  }
	
  public static void main(String[] args) throws Exception{
	final SynchronizedSub sub = new SynchronizedSub();
		
	//�߳�A
	final Thread threadA = new Thread(){
	    //�������run��������ζ��threadA�Ѿ�����SynchronizedSub������
	    public void run(){
		sub.print();
	    }
	};
	threadA.setName("A");
	threadA.start();
		
	Thread.sleep(1000);

	//�߳�B
	Thread threadB = new Thread(){
	    public void run(){
		//���ø÷�����threadB��ȥ������ȡSynchronizedSub������
		sub.print();
	    }
	};
	threadB.setName("B");
	
	threadB.start();
		
	//��̻���threadA��threadB������suspendʽ��ʹ�ü����̻߳ᵼ��B�޷�����SynchronizedSub������
	//��ΪB���������������ִ�м���   ��������
	//threadB.resume();
	//threadA.resume();
	
	
	//LockSupport�ķ�ʽ�����߳�
	LockSupport.unpark(threadA);
	LockSupport.unpark(threadB);
		
	threadA.join();
	threadB.join();
  }
}
