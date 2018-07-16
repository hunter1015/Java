package com.sort;

public class ShellInsertSort {
	/*
	 * ϣ�������ǲ��������һ�����֣���ԭ��һ��������Ĺ��̣���Ϊ��ν���
	 * ���ڴ��ģ�����飬���������������Ϊ��ֻ�ܽ������ڵ�Ԫ�أ�ÿ��ֻ�ܽ������������� 1��
ϣ������ĳ��־���Ϊ�˸Ľ�������������־����ԣ���ͨ�����������ڵ�Ԫ�أ�ÿ�ο��Խ������������ٴ��� 1��
ϣ������ʹ�ò�������Լ�� h �����н�������ͨ�����ϼ�С h������� h=1���Ϳ���ʹ����������������ġ�

	 * ����2���ú�һ��Ԫ�أ���ǰ�����źõ�˳���в���
	 * ������������ҽ��У�ÿ�ζ�����ǰԪ�ز��뵽����Ѿ�����������У�ʹ�ò���֮����������Ȼ����
		�� j Ԫ����ͨ����������Ƚϲ�������ʵ�ֲ�����̣����� j Ԫ��С�ڵ� j - 1 Ԫ�أ��ͽ����ǵ�λ�ý�����
		Ȼ���� j ָ�������ƶ�һ��λ�ã����Ͻ������ϲ�����
	 */
	public static int[] shellSort1(int[] _in ) {
		int len=_in.length;
		boolean isChanged=true;
		int min=0,minindex=0;
		int h=1;
		
		/*
		 * ���h��������ֵ���
		 */
		while(h<len/3) {
			h=3*h+1;
		}
		
		
		while (h>=1) {
		for (int i = h; i<len; i++) {
			for (int j = i; j >=h&&_in[j]<_in[j-h];j=j-h) {
				    swap(_in, j, j-h);
				}
			}
			h=h/3;
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
		//int[] old= {8,5,9,2,14,7,1,3};
		int[] old= {4,2,1,6,3,6,0,-5,1,1};

		//int[] result1=sortMethod1(old);
		int[] result=shellSort1(old);
		for (int i = 0; i < old.length; i++) {
			System.out.print(result[i]);
			if (i+1<old.length) {
				System.out.print(',');
			}
			
		}
	}
}
