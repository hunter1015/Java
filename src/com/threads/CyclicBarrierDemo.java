package com.threads;
//举例：利用CyclicBarrier技术实现复杂大型任务进行分而治之，对大容量的数据集合内的数据求取平均值。


import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 
CyclicBarrier是多线程并发同步辅助类。
它允许一组线程相互等待，直到该组内的所有线程都到达某个公共barrier点，然后所有的这组线程再同步往后面执行。
CyclicBarrier的作用是：让一组线程到达一个barrier（也可以叫同步点）时被阻塞，直到最后一个线程到达barrier时，
barrier才会打开，此时所有被barrier拦截的线程才会继续运行。
 *
 */


//业务场景：对一个很大容量的数据集合里面的数据求取平均值
//目的：利用CyclicBarrier技术实现对一个复杂大型任务进行分而治之
public class CyclicBarrierDemo {
    static class Matrix{
	private int data[][];
		
	//构造函数：构造大数据集
	public Matrix(int row,int col){
	    data = new int[row][col];
			
	    //模拟构造该大型矩阵
	    Random rand = new Random();
	    for(int i=0;i<row;i++){
	        for(int j=0;j<col;j++){
		    data[i][j] = rand.nextInt(20);
	        }
	    }
	}
		
	//对数据进行切片,把二维数据切分成若干个一维数据
	//实现数据降维，形成数据子集
	public int[] getRow(int row){
	    if((row>=0) && (row<data.length)){
		return data[row];
	    }
	    return null;
	}
    }
	
    //用于存放每个数据子集对于的求和结果
    static class Results{
	private int data[];
		
	public Results(int row){
	    data = new int[row];
        }
	//用于存放第index个数据切片对于的数据求和
	public void setData(int index,int value){
	    data[index] = value;
	}
	//获取所有数据切片的数据和
	public int[] getData(){
	    return data;
	}
    }
	
    //统计从第startRow到第endRow行之间的数据集之和
    static class Sum implements Runnable{
	private Matrix matrix; //数据全集
	private int startRow; //起始行
	private int endRow; //终止行
		
	private Results results;
		
	private CyclicBarrier barrier;
		
	public Sum(Matrix m, int startRow, int endRow,Results results,CyclicBarrier barrier){
	    this.matrix = m;
	    this.startRow = startRow;
	    this.endRow = endRow;
	    this.results = results;
	    this.barrier = barrier;
	}
		
	//统计求和
	@Override
	public void run() {
	    int result = 0;
	    //统计求和
	    for(int i=startRow;i<endRow;i++){
		int[] row = matrix.getRow(i);
		result = 0;
		for(int j=0;j<row.length;j++){
		    result += row[j];
		}
		//把每一行的值都放入results中
		results.setData(i, result/row.length);
	    }
			
	    //等待求和完成
	    try{
		System.out.println(Thread.currentThread().getName()+",求和工作已结束，将被阻塞，需要等待其他线程兄弟完成");
		barrier.await();
		System.out.println(Thread.currentThread().getName()+"继续执行！");
	    }catch(InterruptedException ex){
		ex.printStackTrace();
	    }catch(BrokenBarrierException ex){
		ex.printStackTrace();
	    }
	}
    }
	
    //对所有数据切片的结果进行统计后求取平均值
    static class Average implements Runnable{
	private Results results;
		
	public Average(Results res){
	    this.results = res;
	}

	@Override
	public void run() {
	    System.out.println("求平均值的工作开始启动....");
	    int[] data = results.getData();
	    //对所有数据切片的结果进行统计求和
	    int sum = 0;
	    for(int d : data){
	        sum += d;
	    }
	    //求取平均值
	    double average = 1.0* sum / data.length ;
	    //打印最终的计算结果
	    System.out.println("Average of the total data is : "+average);
	}
    }
	
    public static void main(String[] args) {
	int rows = 10000; //数据集的行数目
	int cols = 10000; //数据集的列数目
	int max_thread = 5; //开启并发线程的数目
	int sub_data_rowSpan=rows/max_thread; //每个线程负责处理的数据集的行数
		
	Matrix matrix = new Matrix(rows,cols);
	Results results = new Results(rows);
		
	Average avg = new Average(results);
	//求和完成后再执行Average求平均值线程
	//这里用到CyclicBarrier第二种构造函数，即所有线程到达同步点后，先执行一个提前安排好的其他runnable任务，这里就是
	//求平均值
	CyclicBarrier barrier = new CyclicBarrier(max_thread,avg);
		
	Sum[] sumer = new Sum[max_thread];
	for(int i=0;i<max_thread;i++){
	    sumer[i] = new Sum(matrix,i*sub_data_rowSpan,i*sub_data_rowSpan+sub_data_rowSpan,results,barrier);
	    Thread t = new Thread(sumer[i]);
	    t.start();
	}
		
	System.out.println("主程序Main结束！");
    }
}
