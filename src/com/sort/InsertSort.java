package com.sort;

public class InsertSort {
	/*方法1：向后搜索最小的，放在前面
	 * 双层循环，i从0开始，i代表，头部向后要缩短的举例，因为二级循环每次回选出最小的数填到头部
	 * 头部的数字已经是从小向大的排序，j从i+1开始，在i+1到len之间，以i的值为最小值，循环中与之比较，到末尾记录最小值下标
	 * 和i进行互换。
	 * 二级循环逐渐从尾部向头部缩短，头不动尾缩短
	 *i仅代表，当前搜索范围的长度！！！！
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
		System.out.println("耗时"+(timeEnd-timeStart));
		return _in;
		
	}
	
	
	
	/*
	 * 方法2：拿后一个元素，向前面已排好的顺序中插入
	 * 插入排序从左到右进行，每次都将当前元素插入到左侧已经排序的数组中，使得插入之后左部数组依然有序。
		第 j 元素是通过不断向左比较并交换来实现插入过程：当第 j 元素小于第 j - 1 元素，就将它们的位置交换，
		然后令 j 指针向左移动一个位置，不断进行以上操作。
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
