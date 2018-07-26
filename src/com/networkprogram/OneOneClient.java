package com.networkprogram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

public class OneOneClient {
	public static void main(String[] args) {
		Socket socket=null;
		BufferedReader bufferedReader=null;
		
		socket=new Socket();
		
		try {
			socket.connect(new InetSocketAddress("localhost",8899));
			bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("Form Server : " + bufferedReader.readLine());
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		finally {
			try {
				if (bufferedReader!=null) {
					
					bufferedReader.close();
				} 
			if (socket!=null) {
			
					socket.close();
				} 
			}catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}
	}
