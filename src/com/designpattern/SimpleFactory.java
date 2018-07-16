package com.designpattern;


interface Shape {
	void draw();
}

class Circle implements Shape {
	public Circle() {
		System.out.println("����Բ��ģ��");
	}

	@Override
	public void draw() {
		System.out.println("����һ��Բ��");
	}
}

class Square implements Shape {
	public Square() {
		System.out.println("�����˷���ģ��");
	}

	@Override
	public void draw() {
		System.out.println("����һ������");
	}
}

public class SimpleFactory {
	private final static String CIRCLE = "CIRCLE";
	private final static String SQUARE = "SQUARE";

	public static Shape getShape(String _type) {
		switch (_type) {
		case CIRCLE:
			return new Circle();
		case SQUARE:
			return new Square();
		default:
			throw new NullPointerException("δ����κ�ͼ��");
		}
	}

	public static void main(String[] args) {
		Shape circle = SimpleFactory.getShape(CIRCLE);
		circle.draw();

		Shape square = SimpleFactory.getShape(SQUARE);
		square.draw();
	}
}
