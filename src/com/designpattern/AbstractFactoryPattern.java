package com.designpattern;


// 开发一款《王者荣耀》，支持多操作系统和多控制方式操作控制和界面控制，并提供相应的工厂类来封装这些类的初始化过程
//创建不同的操作系统接口
interface Linux{
	void controller();
}
interface Windows{
	void controller();
}


//基于不同操作系统实现控制逻辑
class LinuxController implements Linux {
    @Override
    public void controller() {
        System.out.println("Linux 控制 《王者荣耀》");
    }
}

class WindowsController implements Windows {
    @Override
    public void controller() {
        System.out.println("Windows 控制 《王者荣耀》");
    }
}



//创建一个工厂类，基于接口分别实现操作控制和界面控制两种方式的工厂


interface AbstractFactory {
    Linux installLinux();

    Windows installWindows();
}

class OperationFactory implements AbstractFactory {

    @Override
    public Linux installLinux() {
        System.out.println("安装Linux操作控制系统");
        return new LinuxController();
    }

    @Override
    public Windows installWindows() {
        System.out.println("安装Windows操作控制系统");
        return new WindowsController();
    }
}

class InterfaceFactory implements AbstractFactory {
    @Override
    public Linux installLinux() {
        System.out.println("安装Linux界面控制系统");
        return new LinuxController();
    }

    @Override
    public Windows installWindows() {
        System.out.println("安装Windows界面控制系统");
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
