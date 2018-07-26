package com.leetcode;

import java.util.HashMap;

public class TwoSum {
	 public static int[] twoSum(int[] nums, int target) {
	      HashMap<Integer, Integer> map= new HashMap<>();
	      int[] result=new int[2];
	      for (int i = 0; i < nums.length; i++) {
			map.put(nums[i], i);
		}
	      for (int i = 0; i < nums.length; i++) {
	    	  int t=target-nums[i];
	    	  if(map.containsKey(t)) {
	    	  result[0]=i;
	    	  result[1]=map.get(t);
                 break;
	    	  }
	      }
	        return result;
	    }
	 public static void main(String[] args) {
		 int[] a= {3,2,4};
		 int[] b= {0,0};
		 int target=6;
		 //a=twoSum(a, 9);
		 b=twoSum(a, 6);
		 for (int i = 0; i < b.length; i++) {
			 System.out.println(b[i]+" ");
		}
		 
	}
}
