package com.threads;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;


/*������Щ�ȴ�δ����̵߳��̡߳���������Щ��δ��ɵ��̵߳�����������ô����Ч��������ϵͳ��Դ��
Fork/Join�����Ҫ�����������๹��
1. ForkJoinTask
ʹ��Fork/Join��ܣ��������ȴ���һ��Fork/Join���񣻶���������һ������ForkJoinPool��ִ�е�����Ļ��ࡣ
ForkJoinTask������ʵ��Fork/Join��ܵ�����
Fork/Join����ṩ����һ��������ִ��fork��join�����Ļ��ƺͿ�������״̬�ķ�����ͨ����Ϊ��ʵ��Fork/Join������Ҫʵ��һ������������֮һ�����ࣨ�̳���ForkJoinTask����
RecursiveAction���ݹ����������������û�з��ؽ���ĳ�����
RecursiveTask���ݹ���ҵ�������������з��ؽ���ĳ�����
ForkJoinTask��ʵ��Future����һ���з��ؽ����ʵ�ַ�������Future����������Ҫ�ķ�����
fork(): �첽ִ�����񡣸÷����ڷ��������̳߳�ʱ������̳߳����п��еĹ������̻߳򴴽�һ���µ��̣߳���ʼִ��������񣬲��������ء�
join(): ͬ�����ã��÷���������������ɺ󷵻ؽ�����ȴ����͵��̳߳��е�����������ִ����ɡ�
invokeAll(ForkJoinTask<?>...tasks):  ͬ��ִ��ָ����ForkJoinTask�����ȴ�task��ɡ�
get(long,TimeUnit): �����ڼ̳���Futrue�ӿڣ��������Ľ��δ׼���ã����ȴ�ָ����ʱ�䣻����ȴ���ʱ�䳬�����������δ׼���ã��Ƿ����ͷ���null��

2. ForkJoinPool
�����ʵ����ExecutorService�ӿں͹�����ȡ�㷨�������������̣߳����ṩ�����״̬��Ϣ���Լ������ִ����Ϣ��
�����ṩ���������ڵ���������ķ�����
execute(): ���̳߳����첽ִ��ָ�����񡣣���execute�����У�����������runnable����ForkJoinPool��Ͳ����ù�����ȡ�㷨��ForkJoinPool�����ʹ��ForkJoinTask�ࣨcallable����ʱ��ʹ�ù�����ȡ�㷨����
submit(): ���̳߳����첽ִ��ָ�������񣬲���������һ��Futrue����
invoke(ForkJoinTask<T> task)��invokeAll(ForkJoinTask<?>...tasks): ���̳߳���ͬ��ִ��ָ�������񣬵ȴ�������ɲ����ؽ���� 


ForkJoinTask��Ҫͨ��ForkJoinPool��ִ�С�����ָ�������������ӵ���ǰ�����߳���ά����˫�˶����У�������е�ͷ������һ�������̵߳Ķ�������ʱû������ʱ��������������������̵߳Ķ��е�β����ȡһ������
*/

public class ForkJoinFrameWorkDemo {
	
    //����һ����͵�Fork/Join����ע�⣺����˵����ʵ��ҵ��������Ҫ�̳���ForkJoinTask������ʵ����֮һ
	//
    //���������������˸�����̳о��з���ֵ��RecursiveTask
	//
    static class SumTask extends RecursiveTask<Integer> {

	//�����и�Ĳο���С���������С��ģ��
	private int taskDefaultSize = 10;
	private int mStart, mEnd;

	public SumTask(int start, int end) {
	    mStart = start;
	    mEnd = end;
	}

	@Override
	protected Integer compute() {
	    int sum = 0;
			
	    //�ж������Ƿ��Ѿ��и��С��ģ������������ִ�м���
	    boolean canCompute = (mEnd - mStart) <= taskDefaultSize;

	    if (canCompute) {
		//��С�Ŀ�ִ������
		for (int i = mStart; i <= mEnd; i++) {
		    sum = sum + i;
		}
	    } else {
		// �������������
		int middle = (mStart + mEnd) / 2;
		//��ֺ����½��µĶ��󣨹��캯������ȷ������ֽ�ķ�Χ��
		SumTask firstTask = new SumTask(mStart, middle);
		SumTask secondTask = new SumTask(middle + 1, mEnd);
				
		//ע�⣡��������ֵ������������̳߳أ��첽ִ��  �ֽ�󶪸��̳߳�
		firstTask.fork(); 
		secondTask.fork();

		//����ʽ�Ļ�õ�һ����������,�õ������ż���ִ��
		//��ȡ�����������ʽ
		int firstResult = firstTask.join(); 
		int secondResult = secondTask.join();

		// �ϲ�����������Ľ����
		sum = firstResult + secondResult;
	    }
	    //�������յļ�����
	    return sum;
	}
    }
	
    //���Կͻ���
    public static void main(String[] args) throws InterruptedException,
        ExecutionException {

	//����Ĭ�ϵĹ��캯����������ִ�������ForkJoinPool����
	ForkJoinPool forkJoinPool = new ForkJoinPool();

	SumTask task = new SumTask(1, 20);
	
	//ע�⣡��forkJoinPool�̷߳��صĽ���������ForkJoinTask�ķ���ʵ�֣��Ǿ���Future���ͣ��첽��ȡ�����
	Future<Integer> result = forkJoinPool.submit(task);
	
	//����get����ʽ��ȡ������
	System.out.println("sum(1,20) = " + result.get());

	SumTask task2 = new SumTask(1, 100);
	Future<Integer> result2 = forkJoinPool.submit(task2);
	//����get����ʽ��ȡ������
	System.out.println("sum(1,100) = " + result2.get());
		
	forkJoinPool.shutdown();
    }
}