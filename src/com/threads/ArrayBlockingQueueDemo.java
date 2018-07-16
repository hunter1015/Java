package com.threads;

import java.util.concurrent.ArrayBlockingQueue;

/**
 *ArrayBlockingQueue�ֳ�Ϊ�����������С����������£�
�н硣ArrayBlockingQueue�ǻ�������ʵ�ֵ��н��������У��������ɵ�Ԫ�������̶���һ���������Ͳ�����������������
FIFO�����еĻ�ȡ���������ڶ���ͷ������Ӳ��������ڶ���β��������FIFO���ԣ�
�Զ��������ѡ���ͼ���������з���Ԫ�ؽ����²�����������ͼ�ӿն����л�ȡԪ��ͬ�������²��������ȴ���

���ó�����������������-�����߳����еġ��н绺��������������֮����Ҫ�����Ԫ���������޵������
 */

public class ArrayBlockingQueueDemo {

	public static void main(String[] args) throws InterruptedException {
		//����һ������������������������
		final ArrayBlockingQueue<String> bQueue = new ArrayBlockingQueue<String>(12);
		//׼������������Щ����
		for (int i = 0; i < 6; i++) {
			//����־��Ϣд�����
			bQueue.put("log info is " + i);
		}
		
		//��ʼ6���߳�
		for (int i = 0; i < 6; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO �Զ����ɵķ������
					while (true) {

						try {
							tolog(bQueue.take());
						} catch (InterruptedException e) {
							// TODO �Զ����ɵ� catch ��
							e.printStackTrace();
						}
					}

				}
			}).start();
		}
	}

	//��ӡ����
	public static void tolog(String _msg) {
		System.out.println("Log-" + _msg);

	}

}
