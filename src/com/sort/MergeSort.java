package com.sort;


/*
 * �鲢�����˼���ǽ�����ֳ������֣��ֱ��������Ȼ��鲢������
 */
public class MergeSort {
	
	protected static int[] a;
	public static int[] merge1(int in[], int low, int middle, int high) {
		int j=0,k=0;
		//int[] a = null;
		for(int i=low;i<=high;i++) {
			a[i]=in[i];
		}
		
		j=low;
		k=middle+1;
		for (int i =low; i <=high; i++) {
			if (j>middle) {
				in[i]=a[k++];
			}else if (k>high) {
				in[i]=a[j++];
			}else if (a[k]<a[j]) {
				in[i]=a[k++];
			}else {
				in[i]=a[j++];
			}
		}
		return  in;
		
	}
	public static void sort1(int[] a) {
		sort1(a, 0, a.length-1);
	}
	
	/*
	 * �Զ����¹鲢����,�ݹ����
	 */
	public static void sort1(int[] a,int low,int high) {
		if (high<=low) {
			return;
		}
			int middle=low+(high-low)/2;
			sort1(a, low, middle);
			sort1(a, middle+1, high);
			merge1(a, low, middle, high);
	}
	
	
	
	/*
	 * �Ե����Ϲ鲢����
�ȹ鲢��Щ΢�����飬Ȼ��ɶԹ鲢�õ���΢�����顣
	 */
	public static void sort2(int[] in) {
		int len=in.length;
		for (int space = 1; space < len; space+=space) {
			for (int l = 0; l < len; l+=2*space) {
				merge1(in, l, l+space-1, Math.min(l+2*space-1, len-1));
			}
			
		}
		
	}
	
	
	public static void main(String[] args) {
		int[] a= {3,-2,5,3,-5,4,8,1,-6,6};
		MergeSort.a=new int[a.length];
		//sort1(a);
		sort2(a);
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i]);
			System.out.print(",");
		}
	}
}
