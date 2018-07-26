package com.callback;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.jar.Attributes.Name;

interface EventListener{
	public void response(Object sender,Event event);
}




class Event{
	protected String eventName;
	protected Map<String, Object> properties;
	
	
	public Event(String _name) {
		// TODO 自动生成的构造函数存根
		this(_name, (Object[]) null);

		
	}
	public Event(String _name,Object... args) {
		// TODO 自动生成的构造函数存根
		eventName=_name;
		properties=new HashMap<String, Object>();
		if(args!=null) {
			for (int i=0;i<args.length;i+=2) {
				if (args[i + 1] != null){
				    properties.put(String.valueOf(args[i]), args[i + 1]);
				}
			}
		}
		
		
	}
	public String getEventName() {
		return eventName;
	}
	
	public Map<String, Object> getProperties(){
		return properties;
	}
	
	public Object gerPropertyByKey(String key) {
		return properties.get(key);
	}
}

class EventSource{
	private Vector<EventListener> eventListeners=new Vector<EventListener>();
	
	
	public void addListener(EventListener el) {
		
		eventListeners.add(el);
	}
	
	
	public void triggerEvent(Event e) {
		for(EventListener _eventListener:eventListeners) {
			_eventListener.response(this, e);
		}
		
	}
}

public class Listener {
public static void main(String[] args) {
	EventSource eSource=new EventSource();
	eSource.addListener(new EventListener() {
		
		@Override
		public void response(Object sender, Event event) {
			// TODO 自动生成的方法存根
			if(event.getEventName().equals("click")) {
				System.out.println("鼠标点击");
			}
		}
	});
	eSource.triggerEvent(new Event("click", "1",22,"2",12122));
}
}
