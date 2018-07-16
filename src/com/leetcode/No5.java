package com.leetcode;

/*
 * 5. ������Ӵ�
 * ����һ���ַ��� s���ҵ� s ����Ļ����Ӵ�������Լ��� s ����󳤶�Ϊ1000��

ʾ�� 1��

����: "babad"
���: "bab"
ע��: "aba"Ҳ��һ����Ч�𰸡�
ʾ�� 2��

����: "cbbd"
���: "bb"
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
