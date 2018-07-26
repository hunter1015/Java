package com.designpattern;


/*
 * 
 * 抽象工厂
抽象工厂模式是为创建【一组对象】提供提供的解决方案，与工厂方法模式相比，抽象工厂模式中的具体工厂不只是创建某一种产品，而是负责一组（产品族）。
抽象工厂模式（Abstract Factory Pattern）:提供了创建一系列相互依赖对象的接口，无需指定具体类
抽象工厂模式是【围绕着一个超级工厂工作，创造其它的工厂类】，也被称为工厂的工厂，这种类型的设计模式是创造性的模式，因为这种模式提供了创建对象的最佳方法之一。
 * 
 * 
 * 系统对产品对象的创建需求由一个工程的等级结构满足，其中有两个具体工程角色，即UnixFactory和WindowsFactory。UnixFactory对象负责创建Unix产品族中的产品，
 * 而WindowsFactory对象负责创建Windows产品族中的产品。这就是抽象工厂模式的应用
 * 
 * 超级工厂接口，包含有实例相重合的多个领域（win linux）操作
 * 
 * 具体工厂（生成具体实例，win linux业务1，业务2）
 * 
 * 实例接口-具体实例
 * 
 * 
 * 
 * 优点：

分离接口和实现：客户端使用抽象工厂来创建需要的对象，而客户端根本就不知道具体的实现是谁，客户端只是面向产品的接口编程而已。也就是说，客户端从具体的产品实现中解耦。
切换产品族变得容易：对于增加新的产品族，抽象工厂模式很好地支持了开闭原则，只需要增加具体产品并对应增加一个新的具体工厂，对已有代码无须做任何修改（如：新增一种手柄操作支持）。
	缺点：
不易扩展新产品：如果需要给整个产品族添加一个新的产品，那么就需要修改抽象工厂，这样就会导致修改所有的工厂实现类（如：新增一种操作系统的支持，那么Factory代码需要全部修改）。
	使用场景：
一个系统不应当依赖于产品类实例如何被创建、组合和表达的细节，这对于所有形态的工厂模式都是重要的。
这个系统的产品有多于一个的产品族，而系统只消费其中某一族的产品。
同属于同一个产品族的产品是在一起使用的，这一约束必须在系统的设计中体现出来。
系统提供一个产品类的库，所有的产品以同样的接口出现，从而使客户端不依赖于实现。
 * 
 */

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
