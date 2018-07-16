package com.leetcode;

/*
 * 5. 最长回文子串
 * 给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为1000。

示例 1：

输入: "babad"
输出: "bab"
注意: "aba"也是一个有效答案。
示例 2：

输入: "cbbd"
输出: "bb"
 */
public class No5 {
	public static String longestPalindrome(String s) {
		int len = s.length();
		boolean[][] map = new boolean[len][len];
		int left = 0, right =0;
		int finallen=0;
		for (int i = 0; i < len; i++) {
			for (int j = 0; j <= i; j++) {
				if (i ==j) {
					map[j][i] = true;
					continue;
				}
				if (s.charAt(i) == s.charAt(j)) {
					if (j+1==i) {
						map[j][i] = true;
					}
					if (s.charAt(i) == s.charAt(j) && i >= 1 &&j + 2<=i&&map[j + 1][i - 1]) {
						map[j][i] = true;
					}
					if (map[j][i]==true&&i-j+1>finallen) {
						finallen=i-j+1;
						left=j;
						right=i;
					}
				}
				
			}
		}
		return s.substring(left, right+1);
	}
	 public static void main(String[] args) {
		 System.out.println(longestPalindrome("abcda"));
	}
}
