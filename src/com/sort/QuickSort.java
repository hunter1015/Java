package com.sort;

/*
 * ��������
1. �����㷨
�鲢���������Ϊ����������ֱ����򣬲��������������鲢ʹ��������������
��������ͨ��һ���з�Ԫ�ؽ������Ϊ���������飬��������С�ڵ����з�Ԫ�أ�
����������ڵ����з�Ԫ�أ�������������������Ҳ�ͽ��������������ˡ�
 */


/*
 * 4. �㷨�Ľ�
��һ���л�����������

��Ϊ����������С������Ҳ��ݹ�����Լ�������С���飬��������ȿ�����������ܸ��ã������С�����п����л�����������

����������ȡ��

��õ��������ÿ�ζ���ȡ�������λ����Ϊ�з�Ԫ�أ����Ǽ�����λ���Ĵ��ۺܸߡ����Ƿ���ȡ 3 ��Ԫ�ز�����С���е�Ԫ����Ϊ�з�Ԫ�ص�Ч����á�

�����������з�

�����д����ظ�Ԫ�ص����飬���Խ������з�Ϊ�����֣��ֱ��ӦС�ڡ����ںʹ����з�Ԫ�ء�

�����зֿ����������ֻ�����ɲ�ͬ����������������������ʱ�����������
 */

public class QuickSort {
	public static int partition(int[] a,int low,int high) {
		int index = -1;
		int lowindex=low,highindex=high+1;
		int flag=a[low];
		int temp;
		while(true){
			while(a[--highindex]>flag&&highindex!=low) {
		}
			while(a[++lowindex]<flag&&lowindex!=high) {
		}
			if (lowindex>=highindex) {
				break;
			}
			temp=a[highindex];
			a[highindex]=a[lowindex];
			a[lowindex]=temp;
		}
			temp=a[highindex];
			a[highindex]=flag;
			a[low]=temp;
		return highindex;
	}
	public static void sort(int[] a,int low,int high) {
		if (high<=low) {
			return;
		}
		int partition=partition(a,low,high);
		sort(a, low, partition-1);
		sort(a, partition+1, high);
	}
	public static void main(String[] args) {
		int[] a= {3,-2,5,3,-5,4,8,1,-6,6};
		sort(a, 0, a.length-1);
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i]);
			System.out.print(",");
		}
	}
}
