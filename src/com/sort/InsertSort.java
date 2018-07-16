package com.sort;

public class InsertSort {
	/*����1�����������С�ģ�����ǰ��
	 * ˫��ѭ����i��0��ʼ��i����ͷ�����Ҫ���̵ľ�������Ϊ����ѭ��ÿ�λ�ѡ����С�����ͷ��
	 * ͷ���������Ѿ��Ǵ�С��������j��i+1��ʼ����i+1��len֮�䣬��i��ֵΪ��Сֵ��ѭ������֮�Ƚϣ���ĩβ��¼��Сֵ�±�
	 * ��i���л�����
	 * ����ѭ���𽥴�β����ͷ�����̣�ͷ����β����
	 *i��������ǰ������Χ�ĳ��ȣ�������
	 */
	public static int[] sortMethod1(int[] _in ) {
		long timeStart=System.currentTimeMillis();
		int len=_in.length;
		boolean isChanged=true;
		int min=0,minindex=0;
		
		for (int i = 0; i<len-1; i++) {
			isChanged=false;
			min=_in[i];
			for (int j = i+1; j <len; j++) {
				if (_in[j]<min) {
					min=_in[j];
					minindex=j;
					isChanged=true;
				}
			}
			if (isChanged) {
				swap(_in, i, minindex);
			}
			
		}
		long timeEnd=System.currentTimeMillis();
		System.out.println("��ʱ"+(timeEnd-timeStart));
		return _in;
		
	}
	
	
	
	/*
	 * ����2���ú�һ��Ԫ�أ���ǰ�����źõ�˳���в���
	 * ������������ҽ��У�ÿ�ζ�����ǰԪ�ز��뵽����Ѿ�����������У�ʹ�ò���֮����������Ȼ����
		�� j Ԫ����ͨ����������Ƚϲ�������ʵ�ֲ�����̣����� j Ԫ��С�ڵ� j - 1 Ԫ�أ��ͽ����ǵ�λ�ý�����
		Ȼ���� j ָ�������ƶ�һ��λ�ã����Ͻ������ϲ�����
	 */
	public static int[] sortMethod2(int[] _in ) {
		int len=_in.length;
		boolean isChanged=true;
		int min=0,minindex=0;
		
		for (int i = 1; i<len; i++) {
			for (int j = i; j >0&&_in[j]<_in[j-1];j--) {
				swap(_in, j, j-1);
			}

		}
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
		int[] result=sortMethod2(old);
		for (int i = 0; i < old.length; i++) {
			System.out.print(result[i]);
			if (i+1<old.length) {
				System.out.print(',');
			}
			
		}
	}
}
