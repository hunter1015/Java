package com.designpattern;

import java.lang.annotation.Target;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/*代理模式（Proxy Pattern）属于结构型模式的一种，给某个对象提供一个代理对象，并由代理对象控制对于原对象的访问，
即客户不直接操控原对象，而是通过代理对象间接地操控原对象。
	客户端不想或不能直接访问一个对象，此时可以通过一个称之为代理的第三者来实现间接访问，该方案对应的设计模式被称为代理模式。
代理模式是一种广泛应用的结构型设计模式，常见的代理形式包括远程代理、安全代理、虚拟代理、缓冲代理、智能引用代理等。

	静态代理
	Subject（抽象主题角色）：声明了RealSubject和ProxySubject的共同接口,客户端通常需要针对接口角色进行编程
	ProxySubject（代理主题角色）：包含了对真实（委托）对象(RealSubject)的引用，可以控制对RealSubject的使用，负责在需要的时候创建和删除，并对RealSubject的使用加以约束
	RealSubject（真实主题角色）：代理对象所代表的真实对象，也是最终引用的对象
	
	
	RealSubject（真实主题角色）必须是事先创建好在的，并将其作为ProxySubject（代理主题角色）的内部属性。
	但是实际使用时，一个RealSubject（真实主题角色）色必须对应一个ProxySubject（代理主题角色），大量使用会导致类的急剧膨胀；
	此外，如果事先并不知道RealSubject角色，该如何使用ProxySubject呢？这个问题可以通过Java的动态代理类来解决。
	
	动态代理
	实现了Subject接口的代理类，代理类的内部逻辑由 SubjectHandler决定。生成代理类后，由 newProxyInstance()方法返回该代理类的一个实例。至此，一个完整的动态代理完成了。
	
	*/


interface Subject{
	void request();
}
class RealSubject implements Subject{
	@Override
	public void request() {
		// TODO 自动生成的方法存根
		System.out.println("触发真正业务对象，谷歌搜索");
	}
}
class ProxySubject implements Subject{
	RealSubject real=null;
	@Override
	public void request() {
		System.out.println("向代理发请求");
		// TODO 自动生成的方法存根
		
		//用到时才加载，延迟加载
		if (real==null) {
			real=new RealSubject();
		}
		real.request();
		
		System.out.println("代理响应请求");
	}
	
}




/**
 * 动态代理类
 * @author yangbei
 *
 */

class SubjectHandle implements InvocationHandler{
	private Subject subject;
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO 自动生成的方法存根
		System.out.println("向代理服务器发起请求");
		if (subject==null) {
			//这里根据业务需求，可以根据参数动态调整实例
			subject=new RealSubject();
		}
		subject.request();
		//返回真实主题完成实际的操作
        System.out.println("代理服务器响应请求");
		//如果返回值可以直接 return subject.request();
		return null;
	}
	public static Subject createSubject() {
		return (Subject)Proxy.newProxyInstance(
				ClassLoader.getSystemClassLoader(), new Class[]{Subject.class}, new SubjectHandle());
	}
	
}



/*
 * CGLIB动态代理
在Java中，动态代理类的生成主要涉及对 ClassLoader 的使用。
以 CGLIB 为例，使用 CGLIB 生成动态代理，首先需要生成 Enhancer类实例，并指定用于处理代理业务的回调类。
在 Enhancer.create() 方法中，会使用 DefaultGeneratorStrategy.Generate() 方法生成动态代理类的字节码，
并保存在 byte 数组中。接着使用 ReflectUtils.defineClass() 方法，通过反射，调用ClassLoader.defineClass() 方法，
将字节码装载到 ClassLoader 中，完成类的加载。最后使用 ReflectUtils.newInstance() 方法，通过反射，生成动态类的实例，并返回该实例。
基本流程是根据指定的回调类生成 Class 字节码—通过 defineClass() 将字节码定义为类—使用反射机制生成该类的实例。
 */

class CGLIBProxy implements MethodInterceptor{
	private Object target;
	
	@Override
	public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3) throws Throwable {
		// TODO 自动生成的方法存根
        System.out.println("向代理服务器发起请求");
        arg3.invokeSuper(arg0, arg2);
        System.out.println("代理服务器响应请求");
		return null;
	}
	 /**
     * 创建代理对象
     *
     * @param target 目标对象
     * @return
     */
	public Object getInstance(Object target) {
		this.target=target;
		Enhancer enhancer=new Enhancer();
		enhancer.setSuperclass(this.target.getClass());
		// 回调方法
		enhancer.setCallback(this);
		System.out.println("代理即将生成目标");
		// 创建代理对象
		return enhancer.create();
		
	}
	
}
public class ProxyPattern {
	public static void main(String[] args) {
		Subject a=new ProxySubject();
		a.request();
		
		/**
		 * java原生动态代理调用
		 */
		Subject proxy=SubjectHandle.createSubject();
		proxy.request();
		
		/**
		 * CGLIB动态代理调用
		 */
		CGLIBProxy cglibproxy = new CGLIBProxy();
        Subject subject = (RealSubject) cglibproxy.getInstance(new RealSubject());
        subject.request();
		
	}
}
