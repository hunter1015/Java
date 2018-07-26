package com.basic;


class A {
	
	public void service() {
		System.out.println("调用了A类的Service");
		System.out.println(this.getClass());
		this.doPost();
	}
	
	public void doPost() {
		System.out.println("A");
	}
	
}


class B extends A {

	@Override
	public void service() {
		System.out.println("进入B service");
		super.service();
	}
	
	@Override
	public void doPost() {
		super.doPost();
		System.out.println("B");
	}
	
}




public class ExtendDemo extends B {
	@Override
	public void service() {
		System.out.println("进入ExtendDemo service");
		super.service();
	}
	
	@Override
	public void doPost() {
		super.doPost();
		System.out.println("ExtendDemo");
	}
	public static void main(String[] args) {
		B b = new B();
		b.service();
		ExtendDemo extendDemo=new ExtendDemo();
		extendDemo.service();
}
}
