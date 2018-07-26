package com.callback;

import java.util.ArrayList;

interface ObserverInterface{
	public void update();
}

class ObserverConcrete implements ObserverInterface{

	@Override
	public void update() {
		// TODO 自动生成的方法存根
		System.out.println("观察者已更新");
	}
	
}
abstract class Subject{
	private ArrayList<ObserverInterface> observers=new ArrayList<>();
	
	public void addObserver(ObserverInterface _ob){
		observers.add(_ob);
	}
	public void deleteObserver(ObserverInterface _ob){
		observers.remove(_ob);
	}
	
	public void noticeToObs(String _noticeMsg) {
		for(ObserverInterface ob:observers) {
			//这里可以多线程异步
			ob.update();
		}
	}
	
}
class SubjectConcrete extends Subject{
	public void notifyToAll() {
		super.noticeToObs("目标已更新");
		//System.out.println("");
	}
	
}

public class Observer {
	public static void main(String[] args) {
		SubjectConcrete sub1 = new SubjectConcrete();
		ObserverConcrete ob1=new ObserverConcrete();
		ObserverConcrete ob2=new ObserverConcrete();
		sub1.addObserver(ob1);
		sub1.addObserver(ob2);
		sub1.notifyToAll();
		
	}


}
