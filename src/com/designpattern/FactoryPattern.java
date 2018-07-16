package com.designpattern;


//1.����ImageReaderFactory���󹤳��ӿ�,������������Ķ���
interface ImageReader{
	void read();
}
interface ImageReaderFactory{
	ImageReader create();
}

//��ȵ�һʵ�����ļ򵥹���ģʽ���ԣ���������ģʽ���ӵ�����Բ�ͬ�Ĳ�Ʒ��ͼƬ��ȡ�����ṩ��ͬ�Ĺ�����
class JpgReader implements ImageReader {
    public JpgReader() {
        System.out.println("����Jpg��ȡ��");
    }

    @Override
    public void read() {
        System.out.println("��ȡJpg�ļ�");
    }
}

class PngReader implements ImageReader {
    public PngReader() {
        System.out.println("����Png��ȡ��");
    }

    @Override
    public void read() {
        System.out.println("��ȡPng�ļ�");
    }
}

class JpgFactory implements ImageReaderFactory {
    @Override
    public ImageReader create() {
        System.out.println("ʵ����Jpg�ļ�����");
        return new JpgReader();
    }
}

class PngFactory implements ImageReaderFactory {
    @Override
    public ImageReader create() {
        System.out.println("ʵ����Png�ļ�����");
        return new PngReader();
    }
}



//���������࣬��Ȼʵ��ʹ�ù����У�ʵ�ֹ����������˿���ʵ����������󣬻����Գ�ʼ��ĳЩ��Դ���ã��������ӳء������ļ���
public class FactoryPattern {

	public static void main(String[] args) {
		ImageReaderFactory jpgFactory=new JpgFactory();
		ImageReader jpgReader=jpgFactory.create();
		
		ImageReaderFactory pngFactory=new PngFactory();
		ImageReader pngReader=pngFactory.create();
	}
}
