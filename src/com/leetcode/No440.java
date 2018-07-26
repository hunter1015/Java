package com.leetcode;

import javax.xml.ws.AsyncHandler;

public class No440 {
    public static int findKthNumber(int n, int k) {


        int start=1,end=1;
        int startbig=1,endbig=1;
        int numbernow=0;
        k=k-1;
        boolean firstflag=true;
        while(k>-1){
            if(k==0){
                break;
            }
            end=start+1;
            startbig=start*10;
            endbig=end*10;
            
        while(startbig<=n||startbig<endbig){
            if(k==0){
                break;
            }
            /*
            if(n<startbig&&firstflag){
                k--;
                firstflag=false;
                break;
            }
            */
                
            if(n>=startbig&&n<endbig){
                k--;
                numbernow=startbig;
                startbig++;
                continue;
            }
            if(n>endbig&&k>9){
                k=k-9;
                break;
            }
         }

            if (k>0) {
            	 k--;
            	 start+=1;
                 numbernow=start;
			}
       	 
         
        }
        if(n==1)
            return 1;
        else
        return numbernow;
        
    }
    public static void main(String[] args) {
    	System.out.println(findKthNumber(13,6));
	}
}
