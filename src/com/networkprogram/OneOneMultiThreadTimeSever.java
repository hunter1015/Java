package com.networkprogram;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OneOneMultiThreadTimeSever {
	public final static int port=8899;
	public static void main(String[] args) {
		ExecutorService threadpool=Executors.newFixedThreadPool(50);
		try (ServerSocket serverSocket=new ServerSocket(port)){
			System.out.println("Started server ... ");
			
			//永遠在監聽
			while (true) {
				Socket connection=serverSocket.accept();
				Callable<Void> task=new ServerTime(connection);
				threadpool.submit(task);
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}
	private static class ServerTime implements Callable<Void>{
		private Socket socket;
		public ServerTime(Socket _Socket) {
			// TODO 自动生成的构造函数存根
			socket=_Socket;
		}
		@Override
		public Void call() throws Exception {
			// TODO 自动生成的方法存根
			try {
				Writer writer=new OutputStreamWriter(socket.getOutputStream());
				Date now=new Date();
				writer.write(now.toString()+"\n");
				writer.flush();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}finally {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			return null;
		}
		
		
	}
}
