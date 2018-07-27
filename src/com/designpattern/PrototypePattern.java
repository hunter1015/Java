package com.designpattern;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * 原型模式（Prototype Pattern）是创建模式的一种，其作用是提高创建效率，减少计算机资源开销，
 * 与工厂模式类似的是，都屏蔽了对象实例化的过程…
 * 其特点就是通过克隆/拷贝的方式来，节约创建成本和资源，被拷贝的对象模型就称之为原型。
 * 
 * JAVA中对原型模式提供了良好的支持，我们只需要实现Cloneable接口即可，它的目的就是将对象标记为可被复制

优点
	简化对象创建过程，通过拷贝的方式构建效率更高
	可运行时指定动态创建的对象
缺点
	需要实现 Cloneable接口，clone位于内部，不易扩展，容易违背开闭原则(程序扩展,不应该修改原有代码)
	默认的 clone 只是浅克隆，深度克隆需要额外编码(比如：统一实现Cloneable接口，或者序列化方式，还有org.apache.commons:commons-lang3.SerializationUtils.java)
 * 
 注意点
·通过内存拷贝的方式构建出来的，会忽略构造函数限制
·需要注意深拷贝和浅拷贝，默认Cloneable 是浅拷贝，只拷贝当前对象而不会拷贝引用对象，除非自己实现深拷贝
·与单例模式冲突，clone是直接通过内存拷贝的方式，绕过构造方法
·常用克隆不可变对象,如果你克隆的对象10个字段改9个还不如实例化算了
·clone只是一个语法，非强制方法命名
·很少单独出现，常与工厂模式相伴

适用场景

常用在初始化步骤繁琐，资源耗损严重的对象
 */



/*
 * 序列化实现深度克隆
 */

class Address implements Cloneable,Serializable {
	
    private static final long serialVersionUID = 783202091017893997L;

}
public class Customer implements Cloneable, Serializable {
	private static final long serialVersionUID = 783202091017893997L;

    protected Customer deepCopy() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(this);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        return (Customer) in.readObject();
    }
}
public class PrototypePattern {
    public static void main(String[] args) throws CloneNotSupportedException {
    	System.out.println("////////////////////////////////////////////////// 深浅克隆 - 开始 //////////////////////////////////////////////////");
        Customer deepCopy = original.deepCopy();
        original.setAddress(new Address("长沙市"));
        original.getHobbies().add("乒乓球");
        System.out.println("深克隆：" + deepCopy.toString());

        System.out.println("////////////////////////////////////////////////// 深浅克隆 - 结束 //////////////////////////////////////////////////\n\n");
	}
}


