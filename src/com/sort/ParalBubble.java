package com.sort;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/*由于存在数据相关性，很难并行，因为不确定是和前还是和后。“分而治之”的思想又会排上用场。下面我们介绍一种被称为“奇偶交换排序”的算法来实现并行冒泡。
（其实就是要找一个与数据无关的规则，可以覆盖全部数字操作）
所谓“奇偶交换排序”就是把排序过程分为两个阶段——奇交换和偶交换。对于“奇交换”来说，就是比较奇数索引以及相邻的后续元素。而“偶交换”总是比较偶数索引以及相邻的后续元素。
并且，“奇交换”和“偶交换”是成对出现，这样才能保证比较和交换涉及到数组中的每一个元素。*/


//利用奇偶排序算法实现并行冒泡
public class ParalBubble {
    //用于标识当前迭代是否发生了数据交换
    static boolean exchanged = true;
    static ExecutorService pool = Executors.newCachedThreadPool();
    
    static synchronized void setExchanged(boolean v) {
        exchanged = v;
    }
    
    static synchronized boolean isExchanged() {
        return exchanged;
    }
    
    //按照奇偶交换策略，相邻的两个数进行交换
    public static class OddEventSortTask implements Runnable {
        int i;
        CountDownLatch latch;
        int[] array;
        
        public OddEventSortTask(int i, int[] array, CountDownLatch latch) {
            this.i = i;
            this.array = array;
            this.latch = latch;
        }
        
        public void run() {
            if (array[i] > array[i + 1]) {
                int temp = array[i];
                array[i] = array[i + 1];
                array[i + 1] = temp;
                setExchanged(true);
            }
            latch.countDown();
        }
    }
    
    public static void pOddEventSort(int[] arr) throws InterruptedException {
        //stage为0，表示进行偶交换阶段；stage为1，则表示进行奇交换阶段
        int stage = 0;
        while (isExchanged() == true || stage == 1) {
            setExchanged(false);
            // 偶数的数组长度,当为奇交换时,只有len/2-1 个线程
            CountDownLatch latch = new CountDownLatch(arr.length / 2
                    - (arr.length % 2 == 0 ? stage : 0));
            for (int i = stage; i < arr.length; i += 2) {
                pool.submit(new OddEventSortTask(i,arr, latch));
            }
            // 等待所有线程结束
            latch.await();
            //每次迭代结束后，切换stage的状态
            if (stage == 0) {
                stage = 1;
            } else {
                stage = 0;
            }
        }
    }
    
    // 打印数组内容
    public static void print(int[] array) {
        int length = (null == array) ? 0 : array.length;
        for (int i = 0; i < length; i++) {
            System.out.print(array[i] + ";");
        }
        System.out.println();
    }
    
    //传统冒泡排序
    public static void bubbleSort(int[] array){
        int length = (null == array) ? 0 : array.length;
        for (int i = length-1;i>0;i--){
            for(int j=0;j<i;j++){
                if(array[j] > array[j+1]){
                    int temp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = temp;
                }
            }
        }
    }
    
    // 排序功能测试
    public static void main(String[] args) throws InterruptedException {
        // 构造一个随机数组
    	long startTime;
    	long endTime;
        Random random = new Random();
        int size = 20;
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(9999);
        }
        int[] array2=array.clone();
        // 排序前的数组打印
        print(array);
        
        startTime = System.currentTimeMillis();
        bubbleSort(array);
        endTime = System.currentTimeMillis();
        System.out.println("传统冒泡排序耗时"+(endTime-startTime)+"ms");
        // 排序后的数组打印
        print(array);
        
        
        // 排序前的数组打印
        print(array2);
        // 对数组进行从小到大的排序
        startTime = System.currentTimeMillis();
        pOddEventSort(array2);
        endTime = System.currentTimeMillis();
        System.out.println("并行排序耗时"+(endTime-startTime)+"ms");
        

        // 排序后的数组打印
        print(array2);
    }
}