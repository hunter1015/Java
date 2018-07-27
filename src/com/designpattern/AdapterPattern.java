package com.designpattern;

/*适配器模式（Adapter Pattern）属于结构型模式的一种，把一个类的接口变成客户端所期待的另一种接口，
从而使原本接口不匹配而无法一起工作的两个类能够在一起工作…

概述
当你想使用一个已经存在的类，而它的接口不符合你的需求，或者你想创建一个可重用的类（与不兼容接口无关的类），
这时候可以考虑使用适配器模式。同时它也是一种包装模式，它与装饰模式同样具有包装的功能。*/
class M4DataLine {
	public void connection() {
		System.out.println("使用小米4数据线连接...");
	}
}

interface Target {
	void connection();
}

class M5DataLine implements Target {
	@Override
	public void connection() {
		System.out.println("使用小米5数据线连接...");
	}
}
/**
 * 类适配器：对象继承的方式，静态的定义。
 * @author yangbei
 *
 */
class M5DataLineAdapter extends M4DataLine implements Target {

	@Override
	public void connection() {
		System.out.println("插入 type-c 转接头");
		super.connection();
	}
}

/**
 * 对象适配器：依赖于对象的组合，都是采用对象组合的方式，也就是对象适配器实现的方式。
 * 创建适配器类，实现标准接口，将这个调用委托给实现新接口的对象来处理
 * @author yangbei
 *
 */
class M5DataLineAdapter2 implements Target {

    private Target target;

    public M5DataLineAdapter2(Target target) {
        this.target = target;
    }

    @Override
    public void connection() {
        System.out.println("插入 type-c 转接头");
        target.connection();
    }
}


public class AdapterPattern {
	public static void main(String[] args) {
		Target target = new M5DataLine();
		target.connection();

		Target adapter = new M5DataLineAdapter();
		adapter.connection();
		
		
        // 使用特殊功能类，即适配类
        Target adapter2 = new M5DataLineAdapter2(new M5DataLine());
        adapter2.connection();
	}
}
