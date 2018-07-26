package com.leetcode;

/*
 * 给定一个含有 n 个正数的数组 x。从点(0,0)开始，向北移动x[0]米，然后向西移动x[1]米，向南移动x[2]米，向东移动x[3]米，持续进行。换句话说，每次移动后你的方向都会逆时针变化。

以 O(1)的空间复杂度写一个一遍扫描算法，判断你的路径是否交叉。
 */
public class Code335 {
    public boolean isSelfCrossing(int[] x) {
        for(int i=3;i<x.length;i++){
            if(x[i]>x[i-2]&&x[i-1]<x[i-3])
                return true;
            if(i>4&&x[i-2]==x[i-4]&&x[i]>(x[i-2]-x[i-4]))
                return true;
            if(i>5&&x[i-1]+x[i-5]>x[i-3]&&x[i]+x[i-4]<x[i-2])
                return true;
            
        }
        return false;
    }
}
