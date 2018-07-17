package com.sort;

/*
 * 快速排序
1. 基本算法
归并排序将数组分为两个子数组分别排序，并将有序的子数组归并使得整个数组排序；
快速排序通过一个切分元素将数组分为两个子数组，左子数组小于等于切分元素，
右子数组大于等于切分元素，将这两个子数组排序也就将整个数组排序了。
 */


/*
 * 4. 算法改进
（一）切换到插入排序

因为快速排序在小数组中也会递归调用自己，对于小数组，插入排序比快速排序的性能更好，因此在小数组中可以切换到插入排序。

（二）三数取中

最好的情况下是每次都能取数组的中位数作为切分元素，但是计算中位数的代价很高。人们发现取 3 个元素并将大小居中的元素作为切分元素的效果最好。

（三）三向切分

对于有大量重复元素的数组，可以将数组切分为三部分，分别对应小于、等于和大于切分元素。

三向切分快速排序对于只有若干不同主键的随机数组可以在线性时间内完成排序。
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
