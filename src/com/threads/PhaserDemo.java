package com.threads;



/*Phaser��һ����ǿ��ġ������ӵ�ͬ�������࣬������ȫ����CyclicBarrier��CountDownLatch�������Ĺ��ܣ���ǿ����� 
��һ�����񱻷ֽ���˶�����裬Phaser���������ÿһ���������λ�ö��߳̽���ͬ���������е��̶߳��������һ���裬���ܼ���������һ���衣

�в�����������Ҫ������ֽ�ɶ������ִ�еĳ�����Phaser���ƾͷǳ��ʺϡ� */


/*Ӧ�ó������������в�����������Ҫ������ֽ�ɶ������ִ�е�ҵ�񳡾���

���������ض��ļ����²������й�ȥ3��Сʱ�ڣ����޸Ĺ��ĺ�׺��Ϊ.log���ļ����ϡ�

������Ϊ����߲���Ч�ʣ����Կ�������߳�ȥ��������Ŀ���µ���Ŀ¼������������Էֽ�Ϊ�����������裺
����ָ�����ļ����л�ú�׺��Ϊ.log���ļ����ϣ�
���Ե�һ�׶λ�ȡ���ļ����Ͻ��й��ˣ�ɸѡ����ȥ3��Сʱ�ڱ��޸ĵ��ļ����ϣ�
��������ɸѡ����ļ�������Ϣ���м�¼��������*/

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

//�Ӱ�ȫ��Ƶ���Ҫ������
//��Ҫ���ض�Ŀ¼�£��ҵ����3��Сʱ���޸ĵ���.logΪ��׺���ļ�
public class PhaserDemo {
	
    //�����ض�Ŀ¼�£�����suffix��׺�����ļ�����
    static class FileSearch implements Runnable{
	//���ҵ�Ŀ��·��
	private String targetPath;
	//��׺��
	private String suffix;
	//���ҵ���Ŀ�꼯��
	private List<String> results;
	//���ڿ�������ͬ�׶ε�ͬ��
	private Phaser phaser;
		
	public FileSearch(String t,String s,Phaser p){
	    this.targetPath = t;
	    this.suffix = s;
	    this.phaser = p;
	    this.results = new ArrayList<String>();
        }
		
	//��1�׶Σ� �����ض�Ŀ¼�£�����suffix��׺�����ļ�����
	//�ݹ��ȡ�ض�Ŀ¼�µ������ļ�
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
		
	//�ж��ض��ļ����ļ������Ƿ���suffixΪ��׺��
	private void fileProcess(File file){
	    if(file.getName().endsWith(suffix)){
		results.add(file.getAbsolutePath());
	    }
	}
		
	//��2�׶Σ� �ڷ����ļ����淶���ļ����ϣ�results���н���ɸѡ
	//ɸѡ�������ļ��ڹ�ȥ��3Сʱ�ڱ��޸Ĺ�
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
		
	//�÷����ڵ�һ���׶κ͵ڶ��׶ν�����ʱ�򱻵���
	//������������ǲ���Ϊ��
	private boolean checkResults(){
	    if(results.isEmpty()){
		System.out.printf("%s: Phase %d: 0 results.\n",
		    Thread.currentThread().getName(),phaser.getPhase());
		System.out.printf("%s: Phase %d: End.\n",
		    Thread.currentThread().getName(),phaser.getPhase());
		//�����Ϊ�գ�����phaser��arriveAndDeregister������
		//��֪ͨphaser����ǰ�߳��Ѿ�����������׶Σ�
		//���ҽ����ٲ���������Ľ׶β���
		phaser.arriveAndDeregister();
		return false;
	    }else{
		//�������Ϊ�գ��򽫲��ҵ��ļ������д�ӡ
		System.out.printf("%s: Phase %d: %d results.\n",
		    Thread.currentThread().getName(),phaser.getPhase(),results.size());
		//����phaser��arriveAndAwaitAdvance������
		//��֪ͨphaser����ǰ�߳��Ѿ�����˵�ǰ�׶Σ�
		//��Ҫ������ֱ�������߳�Ҳ����ɵ�ǰ�׶�
		phaser.arriveAndAwaitAdvance();
		return true;
	    }
	}
		
	//��3�׶Σ� �������Ԫ�ش�ӡ������̨ 
	private void showInfo(){
	    System.out.printf("%s: Phase %d: %d results.\n",
		Thread.currentThread().getName(),phaser.getPhase(),results.size());
	    for(int i=0;i<results.size();i++){
	        File file = new File(results.get(i));
		System.out.printf("%s: %s\n",
		    Thread.currentThread().getName(),file.getAbsolutePath());
	    }
	    //����phaser��arriveAndAwaitAdvance������
	    //��֪ͨphaser����ǰ�߳��Ѿ�����˵�ǰ�׶Σ�
	    //��Ҫ������ֱ�������߳�Ҳ����ɵ�ǰ�׶�
	    phaser.arriveAndAwaitAdvance();
	}
		
	@Override
	public void run() {
	    //����phaser��arriveAndAwaitAdvance������
	    //ʹ�ò��ҹ����������̱߳�����֮���ٿ�ʼ
	    phaser.arriveAndAwaitAdvance();
	    System.out.printf("%s: Starting.\n",
	        Thread.currentThread().getName());
			
	    //ִ�н׶�1�����񣺲��ҽ����
	    File file = new File(this.targetPath);
	    if(file.isDirectory()){
		directoryProcess(file);
	    }
			
	    if(!checkResults()){
		return;
	    }
			
	    //ִ�н׶�2�����񣺹��˽����
	    filterResults();
			
	    //�����ڲ������е���phaser��arriveAndAwaitAdvance���ȴ�ͬ��������arriveAndDeregister������֪ͨ���߳�ֹͣ����ͬ����
	    if(!checkResults()){
		return;
	    }
			
	    //ִ�н׶�3�����񣺴�ӡ���
	    showInfo();
			
	    //�����̵߳�ע��
	    phaser.arriveAndDeregister();
			
	    System.out.printf("%s: Work completed.\n",
	        Thread.currentThread().getName());
	}
		
    }

    //���Կͻ���
    public static void main(String[] args) {
        //ָ������׶�ͬ�����߳�Ϊ3��
	Phaser phaser = new Phaser(3);
	FileSearch home = new FileSearch("/home","log",phaser);
	FileSearch tmp = new FileSearch("/tmp","log",phaser);
	FileSearch etc = new FileSearch("/etc","log",phaser);
		
	//�����̱߳�ʶ�������������Ĺ���һ����У���ÿ�����������������׶ε����ݣ�����������ɸѡ����
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