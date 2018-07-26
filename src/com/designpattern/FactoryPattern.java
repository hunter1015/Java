package com.designpattern;


/*
 * 工厂方法
 * 简单工厂模式存在耦合，且违反了开闭原则，那么这一问题在工厂方法模式中可以很容易的解决掉，
 * 它可以做到添加新的产品而不破坏已有代码
 * 
 * 工厂方法模式：定义一个创建对象的接口，由它的实现类来决定具体实现，其模式又被称为工厂模式（Factory Pattern）。
 * 
 * 抽象工厂接口-具体实例工厂
 * +
 * 实体接口-实例
 * 
 *  优点：屏蔽了客户端实例化对象的细节，用户只需要关心自己使用的工厂即可。
加入新的产品（图片读取器），无需更改现有代码，提高系统扩展性，符合开闭原则
具备多态性，又被称为多态工厂模式
	缺点： 每次需要编写新的对象和对象工厂类，随业务发展，一定程度上增加了系统复杂度
 * 
 */

//1.新增ImageReaderFactory抽象工厂接口,用来构建具体的对象
interface ImageReader{
	void read();
}
interface ImageReaderFactory{
	ImageReader create();
}

//相比单一实例化的简单工厂模式而言，方法工厂模式更加的灵活，针对不同的产品（图片读取器）提供不同的工厂。
class JpgReader implements ImageReader {
    public JpgReader() {
        System.out.println("创建Jpg读取器");
    }

    @Override
    public void read() {
        System.out.println("读取Jpg文件");
    }
}

class PngReader implements ImageReader {
    public PngReader() {
        System.out.println("创建Png读取器");
    }

    @Override
    public void read() {
        System.out.println("读取Png文件");
    }
}

class JpgFactory implements ImageReaderFactory {
    @Override
    public ImageReader create() {
        System.out.println("实例化Jpg文件工厂");
        return new JpgReader();
    }
}

class PngFactory implements ImageReaderFactory {
    @Override
    public ImageReader create() {
        System.out.println("实例化Png文件工厂");
        return new PngReader();
    }
}



//创建测试类，当然实际使用过程中，实现工厂方法除了可以实例化具体对象，还可以初始化某些资源配置，比如连接池、创建文件等
public class FactoryPattern {

	public static void main(String[] args) {
		ImageReaderFactory jpgFactory=new JpgFactory();
		ImageReader jpgReader=jpgFactory.create();
		
		ImageReaderFactory pngFactory=new PngFactory();
		ImageReader pngReader=pngFactory.create();
	}
}
