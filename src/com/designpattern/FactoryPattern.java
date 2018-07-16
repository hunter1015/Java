package com.designpattern;


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
