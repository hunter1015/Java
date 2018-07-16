package com.designpattern;


interface Shape {
	void draw();
}

class Circle implements Shape {
	public Circle() {
		System.out.println("创建圆形模型");
	}

	@Override
	public void draw() {
		System.out.println("画了一个圆形");
	}
}

class Square implements Shape {
	public Square() {
		System.out.println("创建了方形模型");
	}

	@Override
	public void draw() {
		System.out.println("画了一个方形");
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
			throw new NullPointerException("未描绘任何图形");
		}
	}

	public static void main(String[] args) {
		Shape circle = SimpleFactory.getShape(CIRCLE);
		circle.draw();

		Shape square = SimpleFactory.getShape(SQUARE);
		square.draw();
	}
}
