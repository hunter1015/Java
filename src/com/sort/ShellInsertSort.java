package com.sort;

public class ShellInsertSort {
	/*
	 * 希尔排序，是插入排序的一个变种，将原来一个个插入的过程，分为多段进行
	 * 对于大规模的数组，插入排序很慢，因为它只能交换相邻的元素，每次只能将逆序数量减少 1。
希尔排序的出现就是为了改进插入排序的这种局限性，它通过交换不相邻的元素，每次可以将逆序数量减少大于 1。
希尔排序使用插入排序对间隔 h 的序列进行排序。通过不断减小 h，最后令 h=1，就可以使得整个数组是有序的。

	 * 方法2：拿后一个元素，向前面已排好的顺序中插入
	 * 插入排序从左到右进行，每次都将当前元素插入到左侧已经排序的数组中，使得插入之后左部数组依然有序。
		第 j 元素是通过不断向左比较并交换来实现插入过程：当第 j 元素小于第 j - 1 元素，就将它们的位置交换，
		然后令 j 指针向左移动一个位置，不断进行以上操作。
	 */
	public static int[] shellSort1(int[] _in ) {
		int len=_in.length;
		boolean isChanged=true;
		int min=0,minindex=0;
		int h=1;
		
		/*
		 * 算出h代表的最大值间隔
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
