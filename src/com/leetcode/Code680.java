package com.leetcode;

public class Code680 {
	public static boolean validPalindrome(String s) {
		int len = s.length();
		int i = 0, j = len - 1;
		while (i < j) {
			if (s.charAt(i) != s.charAt(j)) {
				return isValid(s, i + 1, j) || isValid(s, i, j - 1);
			}
			i++;
			j--;
		}
		return true;
	}

	public static boolean isValid(String s, int i, int j) {
		while (i < j) {
			if (s.charAt(i) != s.charAt(j)) {
				return false;
			}
			i++;
			j--;
		}
		return true;
	}

	public static void main(String[] args) {
		if (validPalindrome("lcupuufxoohdfpgjdmysgvhmvffcnqxjjxqncffvmhvgsymdjgpfdhooxfuupucul")) {
			System.out.println("是");
		} else {
			System.out.println("否");
		}
	}
}
