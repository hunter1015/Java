package com.sort;

import java.util.Date;

public class BubbleSort {
	/*
	 * ˫��ѭ����i��0��ʼ��j��0��ʼ��������󽻻�����������β����������������ѭ����Χ�𽥴�β����ͷ�����̡�ͷ����β����
	 * ��Ϊβ��һ��һ���Ѿ��������ȷ������
	 * �����ݴ�ǰƮ���˺���
	 * 
	 * i������������ΧӦ����С�Ĳ�ֵ
	 */
	public static int[] sortMethod1(int[] _in ) {
		long timeStart=System.currentTimeMillis();
		int len=_in.length;
		boolean isChanged=true;
		for (int i = 0; i < len&&isChanged; i++) {
			isChanged=false;
			for (int j = 0; j <len-i-1; j++) {
				if (_in[j]>_in[j+1]) {
					swap(_in, j, j+1);
					isChanged=true;
				}
			}
		}
		long timeEnd=System.currentTimeMillis();
		System.out.println("��ʱ"+(timeEnd-timeStart));
		return _in;
		
	}
	/*
	 * ˫��ѭ����i��0��ʼ��j��β��len-1��ʼ�������Ƚϣ�С������ǰ�棬��������ͷ��������ѭ���𽥴�ͷ����β�����̣�ͷ����β����
	 * i������������ΧӦ����С�Ĳ�ֵ
	 */
	public static int[] sortMethod2(int[] _in ) {
		long timeStart=System.currentTimeMillis();
		int len=_in.length;
		boolean isChanged=true;
		for (int i = 0; i < len&&isChanged; i++) {
			isChanged=false;
			for (int j = len-1; j >i; j--) {
				if (_in[j]<_in[j-1]) {
					swap(_in, j, j-1);
					isChanged=true;
				}
			}
		}
		long timeEnd=System.currentTimeMillis();
		System.out.println("��ʱ"+(timeEnd-timeStart));
		return _in;
		
	}
	
	
	/*
	 * ˫��ѭ����i��len-1��ʼ������ѭ��������Χ�ĳ��ȣ�j��0��ʼ����0��i֮�䣬�������Ƚϣ������ź��棬��������β����
	 * ����ѭ���𽥴�β����ͷ�����̣�ͷ����β����
	 *i��������ǰ������Χ�ĳ��ȣ�������
	 */
	public static int[] sortMethod3(int[] _in ) {
		long timeStart=System.currentTimeMillis();
		int len=_in.length;
		boolean isChanged=true;
		for (int i = len-1; i >-1&&isChanged; i--) {
			isChanged=false;
			for (int j = 0; j <i; j++) {
				if (_in[j]>_in[j+1]) {
					swap(_in, j, j+1);
					isChanged=true;
				}
			}
		}
		long timeEnd=System.currentTimeMillis();
		System.out.println("��ʱ"+(timeEnd-timeStart));
		return _in;
		
	}
	public static void swap(int[] in,int leftIndex,int rightIndex) {
		int tmp;
		tmp=in[leftIndex];
		in[leftIndex]=in[rightIndex];
		in[rightIndex]=tmp;
	}
	public static void main(String[] args) {
		int[] old= {8,5,9,2,14,7,1,3};
		

		//int[] result1=sortMethod1(old);
		int[] result=sortMethod3(old);
		for (int i = 0; i < old.length; i++) {
			System.out.print(result[i]);
			if (i+1<old.length) {
				System.out.print(',');
			}
			
		}
	}
}
