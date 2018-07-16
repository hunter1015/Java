package com.sort;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/*���ڴ�����������ԣ����Ѳ��У���Ϊ��ȷ���Ǻ�ǰ���Ǻͺ󡣡��ֶ���֮����˼���ֻ������ó����������ǽ���һ�ֱ���Ϊ����ż�������򡱵��㷨��ʵ�ֲ���ð�ݡ�
����ʵ����Ҫ��һ���������޹صĹ��򣬿��Ը���ȫ�����ֲ�����
��ν����ż�������򡱾��ǰ�������̷�Ϊ�����׶Ρ����潻����ż���������ڡ��潻������˵�����ǱȽ����������Լ����ڵĺ���Ԫ�ء�����ż���������ǱȽ�ż�������Լ����ڵĺ���Ԫ�ء�
���ң����潻�����͡�ż�������ǳɶԳ��֣��������ܱ�֤�ȽϺͽ����漰�������е�ÿһ��Ԫ�ء�*/


//������ż�����㷨ʵ�ֲ���ð��
public class ParalBubble {
    //���ڱ�ʶ��ǰ�����Ƿ��������ݽ���
    static boolean exchanged = true;
    static ExecutorService pool = Executors.newCachedThreadPool();
    
    static synchronized void setExchanged(boolean v) {
        exchanged = v;
    }
    
    static synchronized boolean isExchanged() {
        return exchanged;
    }
    
    //������ż�������ԣ����ڵ����������н���
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
        //stageΪ0����ʾ����ż�����׶Σ�stageΪ1�����ʾ�����潻���׶�
        int stage = 0;
        while (isExchanged() == true || stage == 1) {
            setExchanged(false);
            // ż�������鳤��,��Ϊ�潻��ʱ,ֻ��len/2-1 ���߳�
            CountDownLatch latch = new CountDownLatch(arr.length / 2
                    - (arr.length % 2 == 0 ? stage : 0));
            for (int i = stage; i < arr.length; i += 2) {
                pool.submit(new OddEventSortTask(i,arr, latch));
            }
            // �ȴ������߳̽���
            latch.await();
            //ÿ�ε����������л�stage��״̬
            if (stage == 0) {
                stage = 1;
            } else {
                stage = 0;
            }
        }
    }
    
    // ��ӡ��������
    public static void print(int[] array) {
        int length = (null == array) ? 0 : array.length;
        for (int i = 0; i < length; i++) {
            System.out.print(array[i] + ";");
        }
        System.out.println();
    }
    
    //��ͳð������
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
    
    // �����ܲ���
    public static void main(String[] args) throws InterruptedException {
        // ����һ���������
    	long startTime;
    	long endTime;
        Random random = new Random();
        int size = 20;
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(9999);
        }
        int[] array2=array.clone();
        // ����ǰ�������ӡ
        print(array);
        
        startTime = System.currentTimeMillis();
        bubbleSort(array);
        endTime = System.currentTimeMillis();
        System.out.println("��ͳð�������ʱ"+(endTime-startTime)+"ms");
        // �����������ӡ
        print(array);
        
        
        // ����ǰ�������ӡ
        print(array2);
        // ��������д�С���������
        startTime = System.currentTimeMillis();
        pOddEventSort(array2);
        endTime = System.currentTimeMillis();
        System.out.println("���������ʱ"+(endTime-startTime)+"ms");
        

        // �����������ӡ
        print(array2);
    }
}