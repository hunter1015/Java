package com.threads;
//����������CyclicBarrier����ʵ�ָ��Ӵ���������зֶ���֮���Դ����������ݼ����ڵ�������ȡƽ��ֵ��


import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 
CyclicBarrier�Ƕ��̲߳���ͬ�������ࡣ
������һ���߳��໥�ȴ���ֱ�������ڵ������̶߳�����ĳ������barrier�㣬Ȼ�����е������߳���ͬ��������ִ�С�
CyclicBarrier�������ǣ���һ���̵߳���һ��barrier��Ҳ���Խ�ͬ���㣩ʱ��������ֱ�����һ���̵߳���barrierʱ��
barrier�Ż�򿪣���ʱ���б�barrier���ص��̲߳Ż�������С�
 *
 */


//ҵ�񳡾�����һ���ܴ����������ݼ��������������ȡƽ��ֵ
//Ŀ�ģ�����CyclicBarrier����ʵ�ֶ�һ�����Ӵ���������зֶ���֮
public class CyclicBarrierDemo {
    static class Matrix{
	private int data[][];
		
	//���캯������������ݼ�
	public Matrix(int row,int col){
	    data = new int[row][col];
			
	    //ģ�⹹��ô��;���
	    Random rand = new Random();
	    for(int i=0;i<row;i++){
	        for(int j=0;j<col;j++){
		    data[i][j] = rand.nextInt(20);
	        }
	    }
	}
		
	//�����ݽ�����Ƭ,�Ѷ�ά�����зֳ����ɸ�һά����
	//ʵ�����ݽ�ά���γ������Ӽ�
	public int[] getRow(int row){
	    if((row>=0) && (row<data.length)){
		return data[row];
	    }
	    return null;
	}
    }
	
    //���ڴ��ÿ�������Ӽ����ڵ���ͽ��
    static class Results{
	private int data[];
		
	public Results(int row){
	    data = new int[row];
        }
	//���ڴ�ŵ�index��������Ƭ���ڵ��������
	public void setData(int index,int value){
	    data[index] = value;
	}
	//��ȡ����������Ƭ�����ݺ�
	public int[] getData(){
	    return data;
	}
    }
	
    //ͳ�ƴӵ�startRow����endRow��֮������ݼ�֮��
    static class Sum implements Runnable{
	private Matrix matrix; //����ȫ��
	private int startRow; //��ʼ��
	private int endRow; //��ֹ��
		
	private Results results;
		
	private CyclicBarrier barrier;
		
	public Sum(Matrix m, int startRow, int endRow,Results results,CyclicBarrier barrier){
	    this.matrix = m;
	    this.startRow = startRow;
	    this.endRow = endRow;
	    this.results = results;
	    this.barrier = barrier;
	}
		
	//ͳ�����
	@Override
	public void run() {
	    int result = 0;
	    //ͳ�����
	    for(int i=startRow;i<endRow;i++){
		int[] row = matrix.getRow(i);
		result = 0;
		for(int j=0;j<row.length;j++){
		    result += row[j];
		}
		//��ÿһ�е�ֵ������results��
		results.setData(i, result/row.length);
	    }
			
	    //�ȴ�������
	    try{
		System.out.println(Thread.currentThread().getName()+",��͹����ѽ�����������������Ҫ�ȴ������߳��ֵ����");
		barrier.await();
		System.out.println(Thread.currentThread().getName()+"����ִ�У�");
	    }catch(InterruptedException ex){
		ex.printStackTrace();
	    }catch(BrokenBarrierException ex){
		ex.printStackTrace();
	    }
	}
    }
	
    //������������Ƭ�Ľ������ͳ�ƺ���ȡƽ��ֵ
    static class Average implements Runnable{
	private Results results;
		
	public Average(Results res){
	    this.results = res;
	}

	@Override
	public void run() {
	    System.out.println("��ƽ��ֵ�Ĺ�����ʼ����....");
	    int[] data = results.getData();
	    //������������Ƭ�Ľ������ͳ�����
	    int sum = 0;
	    for(int d : data){
	        sum += d;
	    }
	    //��ȡƽ��ֵ
	    double average = 1.0* sum / data.length ;
	    //��ӡ���յļ�����
	    System.out.println("Average of the total data is : "+average);
	}
    }
	
    public static void main(String[] args) {
	int rows = 10000; //���ݼ�������Ŀ
	int cols = 10000; //���ݼ�������Ŀ
	int max_thread = 5; //���������̵߳���Ŀ
	int sub_data_rowSpan=rows/max_thread; //ÿ���̸߳���������ݼ�������
		
	Matrix matrix = new Matrix(rows,cols);
	Results results = new Results(rows);
		
	Average avg = new Average(results);
	//�����ɺ���ִ��Average��ƽ��ֵ�߳�
	//�����õ�CyclicBarrier�ڶ��ֹ��캯�����������̵߳���ͬ�������ִ��һ����ǰ���źõ�����runnable�����������
	//��ƽ��ֵ
	CyclicBarrier barrier = new CyclicBarrier(max_thread,avg);
		
	Sum[] sumer = new Sum[max_thread];
	for(int i=0;i<max_thread;i++){
	    sumer[i] = new Sum(matrix,i*sub_data_rowSpan,i*sub_data_rowSpan+sub_data_rowSpan,results,barrier);
	    Thread t = new Thread(sumer[i]);
	    t.start();
	}
		
	System.out.println("������Main������");
    }
}
