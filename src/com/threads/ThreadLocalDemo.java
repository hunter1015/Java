package com.threads;

/**
 * ThreadLocal�����̱߳�������һ����ThreadLocal����Ϊkey���������Ϊֵ�ļ�-ֵ�Դ洢�ṹ��
 * ����ṹ���������߳��ϣ�Ҳ����˵һ���߳̿��Ը���һ��ThreadLocal�����ѯ����������߳��ϵ�һ��ֵ��
 * һ��أ�����ͨ��set()����������һ��ֵ���ڵ�ǰ�߳�����ͨ��get()������ȡ��ԭ�����õ�ֵ��
 * ��Ϊ�߳��ṩ�˿���ӵ���Լ��ֲ������ļ���;�����ñ���ֻ�е�ǰ�̲߳��ܷ��ʣ�����Ҳ��֤���̰߳�ȫ�ģ�
 * ���ǵ��͵ġ��ÿռ任ʱ�䡱��˼·��
 * 
 * ����������ص㣬�Ǿ��Ƕ���߳�ʹ��ͬһ��ThreadLocal������������ж����ı�������
 * ThreadLocalΪÿ��ʹ�øñ������߳��ṩ�����ı���������ÿ��������ά�����Լ��ġ���ȫ������һ������������
 * ����ÿ���̶߳����Զ����ظı��Լ��ĸ�����������Ӱ�������߳�����Ӧ�ĸ�����
 * 
 * �����ð쿨��Ǯ������������߳��õ���ͬ��ThreadLocal
 * @author YYH
 *
 *
 *
 *������⣬ThreadLocal���߳�ʹ��ר�����õ���������������û�ʹ���̵߳�ʱ����ȡ��������ã������������
 *��˭������˭ֻ�������ĸ��������������ı��壬��������������ڵģ����ᱻ�ı䣬�����̸߳ı��ֻ�����ϵı���������
 *�ռ任ʱ�䣬�����߳�������������ṩ��һЩ���ݣ��������ڽ��ж��������������ʱ�䣬�����������ڴ�Ŀռ�����
 */

public class ThreadLocalDemo {

	/**
	 * //initialValue���ص�ǰ�ֲ߳̾������ĳ�ʼֵ���÷�����һ��protected��������Ϊ�������า�Ƕ���ƣ�
		//�÷�����һ���ӳٵ��÷��������̵߳�һ�ε���get()��set(Object)����ʱ��ִ�У�
		//��������ִֻ��һ�Ρ��÷���ȱʡʵ�ַ���Ϊnull
	 */
	//��ʼ�쿨�����Ҹ������ʼ����ֵ10��
	private static ThreadLocal<Integer> Card=new ThreadLocal<Integer>() {
		public Integer initialValue() {
			return 10;
		}
	};
	//��ÿ�
	public ThreadLocal getThreadLocal() {
		return Card;
	}
	
	//�����Ǯ��Ǯ���+��ֵ���=�����
	public int tranfer(int income) {
		Card.set(Card.get()+income);
		return Card.get();
	}
	
	
	//�����ڲ���-ģ�ⷢ���ʶ���
	private static class Payoff extends Thread{
		
		//��ʼ��������ʱ��������һ�ζ���õĿ�
		private ThreadLocalDemo demo;
		public Payoff(ThreadLocalDemo _d) {
			// TODO �Զ����ɵĹ��캯�����
			demo=_d;
		}
		
		//���濨�ﵱǰ���
		public void report(int total) {
			System.out.println(Thread.currentThread().getName()+"= ����"+total);
		}
		
		//��дrun������������������γ�ֵ���� ���ҷֱ�չʾ������
		@Override
		public void run() {
			// TODO �Զ����ɵķ������
			int total=demo.tranfer(1000);
			report(total);
			
			total=demo.tranfer(100);
			report(total);
			
			total=demo.tranfer(12);
			report(total);
			
			
			//����Ҫ��ÿ���߳������Ժ�Ҫִ��ɾ������
			//����ǰ�ֲ߳̾�������ֵɾ�������߳̽����󣬶�Ӧ���̵߳ľֲ��������Զ���GC����
			//���ԣ���ʽ���ø÷�������̵߳ľֲ����������Ǳ���Ĳ��������ǿ��Լӿ��ڴ���յ��ٶ�
			demo.getThreadLocal().remove();
			
		}
	}
	
	//���Կ�ʼ��
	public static void main(String[] args) {
		//���ȳ�ʼ����
		ThreadLocalDemo demo=new ThreadLocalDemo();
		
		//��ʼ�����ʸ��û�1
		Payoff user1=new Payoff(demo);
		user1.setName("�û�1");
		
		//��ʼ�����ʸ��û�2
		Payoff user2=new Payoff(demo);
		user2.setName("�û�2");
		
		//��ʼ�����ʸ��û�3
		Payoff user3=new Payoff(demo);
		user3.setName("�û�3");
		
		user1.start();
		user2.start();
		user3.start();
		
		
	}
}

/*
 * ���Խ��
�û�2= ����1010
�û�3= ����1010
�û�1= ����1010
�û�3= ����1110
�û�2= ����1110
�û�3= ����1122
�û�1= ����1110
�û�2= ����1122
�û�1= ����1122
 */
