package com.callback;


interface CallbackMessage{
	public void sendCallbackMessage(String msg);
}

class AppPlatform{
	
	public void sendToPhone(String _msg,CallbackMessage _phone){
		System.out.println("平台响应"+_msg);
		_phone.sendCallbackMessage(_msg);
	}
}

class Phone1 implements CallbackMessage{

	@Override
	public void sendCallbackMessage(String msg) {
		// TODO 自动生成的方法存根
		System.out.println("phone1响应"+msg);
		
	}
	public void connectToPlatform(AppPlatform _pAppPlatform,String _phone1msg) {
		// TODO 自动生成的方法存根
		_pAppPlatform.sendToPhone(_phone1msg, Phone1.this);
	}
}

//匿名内部类的实现
class Phone2{

	public void connectToPlatform(AppPlatform _pAppPlatform,String _phone1msg) {
		// TODO 自动生成的方法存根
		_pAppPlatform.sendToPhone(_phone1msg, new CallbackMessage() {

			@Override
			public void sendCallbackMessage(String msg) {
				// TODO 自动生成的方法存根
				System.out.println("phone2匿名内部类响应"+msg);
			}
			
		});
	}
}

public class CallbackBasic {

	public static void main(String[] args) {
		AppPlatform appPlatform=new AppPlatform();
		Phone1 phone1=new Phone1();
		phone1.connectToPlatform(appPlatform, "this is phone1 say his");
		new Phone2().connectToPlatform(appPlatform, "this is phone2 say his");
	}
}
