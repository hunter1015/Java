package com.sort;

import java.util.Date;

public class BubbleSort {
	/*
	 * 双层循环，i从0开始，j从0开始，大数向后交换，优先填满尾部（大数），二级循环范围逐渐从尾部向头部缩短。头不动尾缩短
	 * 因为尾部一个一个已经填好了正确的数字
	 * 大气泡从前飘到了后面
	 * 
	 * i仅代表，搜索范围应该缩小的差值
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
		System.out.println("耗时"+(timeEnd-timeStart));
		return _in;
		
	}
	/*
	 * 双层循环，i从0开始，j从尾部len-1开始，两两比较，小数放在前面，优先填满头部。二级循环逐渐从头部向尾部缩短，头缩短尾不动
	 * i仅代表，搜索范围应该缩小的差值
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
		System.out.println("耗时"+(timeEnd-timeStart));
		return _in;
		
	}
	
	
	/*
	 * 双层循环，i从len-1开始，代表循环搜索范围的长度，j从0开始，在0到i之间，两两向后比较，大数放后面，优先填满尾部。
	 * 二级循环逐渐从尾部向头部缩短，头不动尾缩短
	 *i仅代表，当前搜索范围的长度！！！！
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
		System.out.println("耗时"+(timeEnd-timeStart));
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
