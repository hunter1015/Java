package com.threads;



/*Phaser是一个更强大的、更复杂的同步辅助类，可以完全覆盖CyclicBarrier、CountDownLatch工具栏的功能，更强大灵活 
当一个任务被分解成了多个步骤，Phaser类机制是在每一步骤结束的位置对线程进行同步，当所有的线程都完成了这一步骤，才能继续进行下一步骤。

有并发任务并且需要将任务分解成多个步骤执行的场景，Phaser机制就非常适合。 */


/*应用场景：适用于有并发任务并且需要将任务分解成多个步骤执行的业务场景。

举例：在特定文件夹下查找所有过去3个小时内，被修改过的后缀名为.log的文件集合。

分析：为了提高查找效率，可以开启多个线程去并发查找目标下的子目录，该项任务可以分解为以下三个步骤：
·在指定的文件夹中获得后缀名为.log的文件集合；
·对第一阶段获取的文件集合进行过滤，筛选出过去3个小时内被修改的文件集合；
·将上述筛选后的文件集合信息进行记录、分析。*/

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

//从安全审计的需要出发：
//需要在特定目录下，找到最近3个小时被修改的以.log为后缀的文件
public class PhaserDemo {
	
    //查找特定目录下，具有suffix后缀名的文件集合
    static class FileSearch implements Runnable{
	//查找的目标路径
	private String targetPath;
	//后缀名
	private String suffix;
	//查找到的目标集合
	private List<String> results;
	//用于控制任务不同阶段的同步
	private Phaser phaser;
		
	public FileSearch(String t,String s,Phaser p){
	    this.targetPath = t;
	    this.suffix = s;
	    this.phaser = p;
	    this.results = new ArrayList<String>();
        }
		
	//第1阶段： 查找特定目录下，具有suffix后缀名的文件集合
	//递归获取特定目录下的所有文件
	private void directoryProcess(File file){
	    File list[] = file.listFiles();
	    if(list != null){
		for(int i=0;i<list.length;i++){
		    File f = list[i];
		    if(f.isDirectory()){
		        directoryProcess(f);
		    }else{
			fileProcess(f);
		    }
		}
	    }
	}
		
	//判断特定文件的文件名称是否以suffix为后缀名
	private void fileProcess(File file){
	    if(file.getName().endsWith(suffix)){
		results.add(file.getAbsolutePath());
	    }
	}
		
	//第2阶段： 在符合文件名规范的文件集合（results）中进行筛选
	//筛选出来的文件在过去的3小时内被修改过
	private void filterResults(){
	    List<String> newResults = new ArrayList<String>();
	    long currentDate = new Date().getTime();
	    for(int i=0;i<results.size();i++){
		File file = new File(results.get(i));
		long fileDate = file.lastModified();
		if(currentDate-fileDate < TimeUnit.MILLISECONDS.convert(3, TimeUnit.HOURS)){
		    newResults.add(results.get(i));
		}
	    }
	    results=newResults;
	}
		
	//该方法在第一个阶段和第二阶段结束的时候被调用
	//用来检查结果集是不是为空
	private boolean checkResults(){
	    if(results.isEmpty()){
		System.out.printf("%s: Phase %d: 0 results.\n",
		    Thread.currentThread().getName(),phaser.getPhase());
		System.out.printf("%s: Phase %d: End.\n",
		    Thread.currentThread().getName(),phaser.getPhase());
		//结果集为空，调用phaser的arriveAndDeregister方法，
		//来通知phaser对象当前线程已经结束了这个阶段，
		//并且将不再参与接下来的阶段操作
		phaser.arriveAndDeregister();
		return false;
	    }else{
		//结果集不为空，则将查找到文件数进行打印
		System.out.printf("%s: Phase %d: %d results.\n",
		    Thread.currentThread().getName(),phaser.getPhase(),results.size());
		//调用phaser的arriveAndAwaitAdvance方法，
		//来通知phaser对象当前线程已经完成了当前阶段，
		//需要被阻塞直到其他线程也都完成当前阶段
		phaser.arriveAndAwaitAdvance();
		return true;
	    }
	}
		
	//第3阶段： 将结果集元素打印到控制台 
	private void showInfo(){
	    System.out.printf("%s: Phase %d: %d results.\n",
		Thread.currentThread().getName(),phaser.getPhase(),results.size());
	    for(int i=0;i<results.size();i++){
	        File file = new File(results.get(i));
		System.out.printf("%s: %s\n",
		    Thread.currentThread().getName(),file.getAbsolutePath());
	    }
	    //调用phaser的arriveAndAwaitAdvance方法，
	    //来通知phaser对象当前线程已经完成了当前阶段，
	    //需要被阻塞直到其他线程也都完成当前阶段
	    phaser.arriveAndAwaitAdvance();
	}
		
	@Override
	public void run() {
	    //调用phaser的arriveAndAwaitAdvance方法，
	    //使得查找工作在所有线程被创建之后再开始
	    phaser.arriveAndAwaitAdvance();
	    System.out.printf("%s: Starting.\n",
	        Thread.currentThread().getName());
			
	    //执行阶段1的任务：查找结果集
	    File file = new File(this.targetPath);
	    if(file.isDirectory()){
		directoryProcess(file);
	    }
			
	    if(!checkResults()){
		return;
	    }
			
	    //执行阶段2的任务：过滤结果集
	    filterResults();
			
	    //方法内部里面有调用phaser的arriveAndAwaitAdvance（等待同步）或者arriveAndDeregister（发出通知本线程停止参与同步）
	    if(!checkResults()){
		return;
	    }
			
	    //执行阶段3的任务：打印结果
	    showInfo();
			
	    //撤销线程的注册
	    phaser.arriveAndDeregister();
			
	    System.out.printf("%s: Work completed.\n",
	        Thread.currentThread().getName());
	}
		
    }

    //测试客户端
    public static void main(String[] args) {
        //指定参与阶段同步的线程为3个
	Phaser phaser = new Phaser(3);
	FileSearch home = new FileSearch("/home","log",phaser);
	FileSearch tmp = new FileSearch("/tmp","log",phaser);
	FileSearch etc = new FileSearch("/etc","log",phaser);
		
	//三个线程标识，有三个这样的工作一起进行，但每个工作都包含三个阶段的内容，包括检索、筛选、等
	Thread homeThread = new Thread(home,"home");
	homeThread.start();
		
	Thread tmpThread = new Thread(tmp,"tmp");
	tmpThread.start();
		
	Thread etcThread = new Thread(etc,"var");
	etcThread.start();
		
	try{
	    homeThread.join();
	    tmpThread.join();
	    etcThread.join();
	}catch(InterruptedException ex){
	    ex.printStackTrace();
	}
		
	System.out.println("Terminated: " + phaser.isTerminated());
    }
}