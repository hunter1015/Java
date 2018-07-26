package com.callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

interface IEvent1Listener {
	public void response(Object sender,EventObject Event1 ) ;
}



class Event1{
	public static final String MSG_CHAT="message chat";
	public static final String VOICE_CHAT="voice chat";
	public static final String VEDIO_CHAT="vedio chat";
	
}

class EventObject{
	protected String Event1Name;
	protected Map<String, Object> properties;
	
	
	public EventObject(String _name) {
		// TODO 自动生成的构造函数存根
		this(_name, (Object[]) null);

		
	}
	public EventObject(String _name,Object... args) {
		// TODO 自动生成的构造函数存根
		Event1Name=_name;
		properties=new HashMap<String, Object>();
		if(args!=null) {
			for (int i=0;i<args.length;i+=2) {
				if (args[i + 1] != null){
				    properties.put(String.valueOf(args[i]), args[i + 1]);
				}
			}
		}
		
		
	}
	public String getName() {
		return Event1Name;
	}
	
	public Map<String, Object> getProperties(){
		return properties;
	}
	
	public Object getProperty(String key) {
		return properties.get(key);
	}
	
}


class EventSource1{
    //用于存放该事件源所注册的监听器

    //由于特额定监听器往往与特定事件想关联

    //为此，该数据结构存放的内容设计为：

    //[事件名称1,监听器1,事件名称2,监听器2,......]

    protected  List<Object> Event1Listeners = null;
    
    //用于存放事件源
    protected Object Event1Source=null;
    
    //设置该事件源是否可以触发外部事件标识
    protected boolean Event1sEnabled = true;
    
    public EventSource1() {
    	this(null);
		
	}
    public EventSource1(Object _source) {
    	setEvent1Source(_source);		
	}
    
    //获取事件源对象

    public Object getEvent1Source(){

	return Event1Source;

    }

    //设置事件源对象

    public void setEvent1Source(Object value){

	this.Event1Source = value;
    }
    
    //获取该事件源能否触发外部事件响应

    public boolean isEvent1sEnabled(){

	return Event1sEnabled;

    }



    //设置该事件源能否触发外部事件响应

    public void setEvent1sEnabled(boolean Event1sEnabled){

	this.Event1sEnabled = Event1sEnabled;

    }
    
    //1 绑定事件名称与相应的事件监听器，

    //这样保证一旦特定事件发生，相应的监听器就被触发执行

    //2 如果没有给定事件名称，

    //那么该监听器就被注册到所有的事件中

    public void addListener(String Event1Name, IEvent1Listener listener){

	if (Event1Listeners == null){

	    Event1Listeners = new ArrayList<Object>();

	}
	Event1Listeners.add(Event1Name);

	Event1Listeners.add(listener);

    }



    //删除特定的所有事件监听器

    public void removeListener(IEvent1Listener listener){

	removeListener(listener, null);

    }
    

    //如果事件名称不为空，则删除与之绑定的所有事件监听器

    //如果事件名称为空，则删除特定的所有事件监听器

    public void removeListener(IEvent1Listener listener, String Event1Name){

	if (Event1Listeners != null){

	    for (int i = Event1Listeners.size() - 2; i > -1; i -= 2){

	        if (Event1Listeners.get(i + 1) == listener

		        && (Event1Name == null || String.valueOf(

			Event1Listeners.get(i)).equals(Event1Name))){

		    Event1Listeners.remove(i + 1);

		    Event1Listeners.remove(i);

		}

	    }

        }

    }
    
    //以this为事件源，触发监听器对象

    public void fireEvent1(EventObject evt){

	fireEvent1(evt, null);

    }



    //触发所有的绑定了特定事件对象evt的所有监听器

    public void fireEvent1(EventObject evt, Object sender){

        if (Event1Listeners != null && !Event1Listeners.isEmpty()

	        && isEvent1sEnabled()){

	    if (sender == null){

	        sender = getEvent1Source();

	    }



	    if (sender == null){

	        sender = this;

	    }



	    for (int i = 0; i < Event1Listeners.size(); i += 2){

	        String listen = (String) Event1Listeners.get(i);



		if (listen == null || listen.equals(evt.getName())){

		    ((IEvent1Listener) Event1Listeners.get(i + 1)).response(

				sender, evt);

		}

	    }

	}

    }

}

//自定义Andorid QQ监听对象

//实现了IEvent1Listener监听接口

class AndroidQQ implements IEvent1Listener {

  //对不同的事件进行响应

  @Override

  public void response(Object sender, EventObject evt) {

	//对事件对象进行解析

	String msgType = evt.getName();

	String msg = "Android设备 => ";

	if(msgType.equals(Event1.MSG_CHAT)){

	    msg += evt.getProperty("text");	

      }else if(msgType.equals(Event1.VOICE_CHAT)){

	    msg += evt.getProperty("voice");

	}else if (msgType.equals(Event1.VEDIO_CHAT)){

	    msg += evt.getProperty("voide");

	}

	System.out.println(msg);

  }

}

//自定义IPhone QQ监听对象

//实现了IEvent1Listener监听接口
class IPhoneQQ implements IEvent1Listener {

  //对不同的事件进行响应

  @Override

  public void response(Object sender, EventObject evt) {

	//对事件对象进行解析

	String msgType = evt.getName();

	String msg = "IPhone设备 => ";

	if(msgType.equals(Event1.MSG_CHAT)){

	    msg += evt.getProperty("text");	

      }else if(msgType.equals(Event1.VOICE_CHAT)){

	    msg += evt.getProperty("voice");

	}else if (msgType.equals(Event1.VEDIO_CHAT)){

	    msg += evt.getProperty("voide");

	}

	System.out.println(msg);

  }

}
//自定义QQ聊天事件源
//继承事件源对象Event1Source
class ChatQQ extends EventSource1 {

  //聊天回话名称

  private String name;


  //构造函数

  public ChatQQ(String n){

	this.name = n;

  }

	

  //发起文字聊天事件

  public void textChat(){

	EventObject text = new EventObject(

		Event1.MSG_CHAT,"text","text:Hello world!");

	this.fireEvent1(text);

  }

  //发起语音聊天事件

  public void voiceChat(){

	EventObject voice = new EventObject(

		Event1.VOICE_CHAT,"voice","voice:Hello world!");

	this.fireEvent1(voice);

  }

  //发起视频聊天事件

  public void voideChat(){

	EventObject voide = new EventObject(

		Event1.VEDIO_CHAT,"voide","voide:Hello world!");

	this.fireEvent1(voide);

  }

}
public class ListenerUseful {
	
	 public static void main(String[] args) {

			ChatQQ chatQQ = new ChatQQ("QQ会话");

			System.out.println("Android QQ与IPhone QQ上线了...");

				

			//可以把相同的事件绑定到不同的事件监听器对象上

			chatQQ.addListener(Event1.MSG_CHAT, 
		                                new IPhoneQQ());

			chatQQ.addListener(Event1.MSG_CHAT, 
		                                new AndroidQQ());

				

			chatQQ.addListener(Event1.VOICE_CHAT, 
		                                new IPhoneQQ());

			chatQQ.addListener(Event1.VOICE_CHAT, 
		                                new AndroidQQ());

				

			chatQQ.addListener(Event1.VEDIO_CHAT, 
		                                new IPhoneQQ());

			chatQQ.addListener(Event1.VEDIO_CHAT,
		                                new AndroidQQ());

				

			//事件源发生不同的事件

			chatQQ.textChat();

			chatQQ.voiceChat();

			chatQQ.voideChat();

		    }

}
