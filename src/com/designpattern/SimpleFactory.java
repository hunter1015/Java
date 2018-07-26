package com.designpattern;


/*
 * 分析： 从上述代码中可以发现，简单工厂拥有一定判断能力，构建结果取决于入参，使用起来也十分的方便，
 * 也正因为使用太过方便而导致高耦合的情况，所有对象实例化都需要依赖它，一旦出问题，影响的会是整个系统

	使用场景： 创建简单，无复杂业务逻辑的对象
	
	工厂方法根据传入参数，return不同形状接口的实现类，圆或者方形
	问题是，所有对象实例化都需要这个工厂类，太累了！耦合度太高
 */
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
