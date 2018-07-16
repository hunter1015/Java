package com.designpattern;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * @author YYH
 *����ģʽ��23�ֻ������ģʽ֮һ��Ҳ��һ���Ƚϼ򵥵�ģʽ��
 *��ν����ģʽ������ȷ��ĳһ����ֻ��һ��ʵ������������ʵ������������ϵͳ�ṩ���ʵ����
	������ֻ����һ��ʵ����ģʽ��Ҳ������Singletonģʽ��(����)
	ͨ��ʹ��private�Ĺ��캯��ȷ������һ��Ӧ����ֻ����һ��ʵ��������������ʵ������
	
	����synchronized����Ϊ����һ�����ܿ����ϸߵĽ������
 */
public class Singleton {
    //����Ϊstatic��Ϊ�˱�֤�����ǹ����
    private static Singleton instance = null;
    private static ReentrantLock lock = new ReentrantLock();
	
    //�������Ϊpublic����Ϳɴ���������������
    private Singleton(){}
	
    public static Singleton getInstance(){
        if(null == instance){
	    //����ReentrantLock�滻Synchronized
	    lock.lock();
	    if(null == instance){
	        instance = new Singleton();
	    }
	    lock.unlock();
        }
	return instance;
    }
}
