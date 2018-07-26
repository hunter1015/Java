package com.concurrent.framework;

import javax.xml.crypto.Data;

public class FutureDemo {
	interface BackData{
	public String getData();
	}
	
	static class RealData implements BackData{
		private String data;
		public RealData(String _s) {
			// TODO 自动生成的构造函数存根
			try {
				Thread.sleep(5000);
				System.out.println("真正的操作");
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			data=_s+"123";
		}
		
		@Override
		public String getData() {
			// TODO 自动生成的方法存根
			
			return data;
		}
		
	}
	
	static class FutureData implements BackData{
		private String data;
		
		private void getRealData(RealData realData) {
			// TODO 自动生成的方法存根
			data=realData.getData();
		}
		@Override
		public String getData() {
			// TODO 自动生成的方法存根
			return data;
			
		}
		
	}
	

	
	static class Client{
		public BackData requestData(String _s) {
			FutureData futureData=new FutureData();
			new Thread() {
				public void run() {
					RealData realData=new RealData(_s);
					futureData.getRealData(realData);
				};
				
			}.start();
					return futureData;
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Client client=new Client();
		BackData backData=client.requestData("sscscsc");
		Thread.sleep(9000);
		System.out.println(backData.getData());
	}
}
