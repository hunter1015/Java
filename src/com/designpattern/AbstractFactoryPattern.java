package com.designpattern;


// ����һ�������ҫ����֧�ֶ����ϵͳ�Ͷ���Ʒ�ʽ�������ƺͽ�����ƣ����ṩ��Ӧ�Ĺ���������װ��Щ��ĳ�ʼ������
//������ͬ�Ĳ���ϵͳ�ӿ�
interface Linux{
	void controller();
}
interface Windows{
	void controller();
}


//���ڲ�ͬ����ϵͳʵ�ֿ����߼�
class LinuxController implements Linux {
    @Override
    public void controller() {
        System.out.println("Linux ���� ��������ҫ��");
    }
}

class WindowsController implements Windows {
    @Override
    public void controller() {
        System.out.println("Windows ���� ��������ҫ��");
    }
}



//����һ�������࣬���ڽӿڷֱ�ʵ�ֲ������ƺͽ���������ַ�ʽ�Ĺ���


interface AbstractFactory {
    Linux installLinux();

    Windows installWindows();
}

class OperationFactory implements AbstractFactory {

    @Override
    public Linux installLinux() {
        System.out.println("��װLinux��������ϵͳ");
        return new LinuxController();
    }

    @Override
    public Windows installWindows() {
        System.out.println("��װWindows��������ϵͳ");
        return new WindowsController();
    }
}

class InterfaceFactory implements AbstractFactory {
    @Override
    public Linux installLinux() {
        System.out.println("��װLinux�������ϵͳ");
        return new LinuxController();
    }

    @Override
    public Windows installWindows() {
        System.out.println("��װWindows�������ϵͳ");
        return new WindowsController();
    }
}


public class AbstractFactoryPattern {
public static void main(String[] args) {
    AbstractFactory operationFactory = new OperationFactory();
    operationFactory.installLinux().controller();
    operationFactory.installWindows().controller();
    System.out.println("========================================================");
    AbstractFactory interfaceFactory = new InterfaceFactory();
    interfaceFactory.installLinux().controller();
    interfaceFactory.installWindows().controller();
}
	
}
