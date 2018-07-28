package com.designpattern;

import java.util.ArrayList;
import java.util.List;

/*观察者模式（Observer Pattern）属于对象行为型模式的一种，定义对象之间的一种一对多依赖关系，使得每当一个对象状态发生改变时，
其相关依赖对象皆得到通知并被自动更新。

概述
观察者模式是一种使用率极高的模式，用于建立一种对象与对象之间的依赖关系，一个对象发生改变时将自动通知其他对象，
其他对象将相应作出反应。在观察者模式中，发生改变的对象称为观察目标，而被通知的对象称为观察者，一个观察目标可以对应多个观察者，
而且这些观察者之间可以没有任何相互联系，可以根据需要增加和删除观察者，使得系统更易于扩展。

观察者模式的别名包括
发布-订阅（Publish/Subscribe）模式、
模型-视图（Model/View）模式、
源-监听器（Source/Listener）模式或从属者（Dependents）模式。


推模型： 主题对象向观察者推送主题的详细信息，不管观察者是否需要，推送的信息通常是主题对象的全部或部分数据。
该模式下如果推送数据变了观察者都得改

拉模型（适应范围更广）： 主题对象在通知观察者的时候，只传递少量信息。如果观察者需要更具体的信息，由观察者主动到主题对象中获取，
相当于是观察者从主题对象中拉数据。一般这种 模型的实现中，会把主题对象自身通过update()方法传递给观察者，
这样在观察者需要获取数据的时候，就可以通过这个引用来获取了。

简单说，主动推-PUSH应用窄的原因是，观察目标要主动向所有观察者推送发布更新的内容，让观察者门不用动，接受变化信息即可
但 主动提醒（传递观察目标对象）+由观察者门自己根据实际情况更新的PULL模式，解放了观察目标，让观察目标自己更自由的做自己的事情，而不用主动
向外界释放自己的变化内容，数据变更情况会很多，而且不用根据观察者的不同而变化信息。所以PULL方法更有效

推模型是假定主题对象知道观察者需要的数据，这种模型下如果数据发生变更会造成极大的影响；
而拉模型是主题对象不知道观察者具体需要什么数据，没有办法的情况下，干脆把自身传递给观察者，让观察者自己去按需要取值。
由此可见：拉模式的适用范围更广；


*/

class SubjectPush {
    /**
     * 用来保存注册的观察者对象
     */
    private List<ObserverPush> observers = new ArrayList<>();

    /**
     * 注册观察者对象
     *
     * @param observer 观察者对象
     */
    void attach(ObserverPush observer) {
        observers.add(observer);
    }

    /**
     * 通知所有注册的观察者对象
     */
    void notifyObservers(String newState) {
        for (ObserverPush observer : observers) {
            observer.update(newState);
        }
    }
}
class ConcreteSubject extends SubjectPush {

    private String subjectState;

    public String getSubjectState() {
        return subjectState;
    }

    public void change(String subjectState) {
        this.subjectState = subjectState;
        //状态发生改变，通知各个观察者
        this.notifyObservers(subjectState);
    }
}


interface ObserverPush {
    /**
     * 更新的接口
     *
     * @param subject 传入目标对象，好获取相应的目标对象的状态
     */
    void update(String subject);
}

class ConcreteObserver implements ObserverPush {

    @Override
    public void update(String newState) {
        //具体的更新实现
        //这里可能需要更新观察者的状态，使其与目标的状态保持一致
        System.out.println("接收到：" + newState);
    }
}

/**
 * 拉模式，pull，这样更有利于观察目标的单纯性，只需要把自己对象传给观察者门，让他们自己判断是否要更新
 * @author YYH
 *
 */

class SubjectPull {
    /**
     * 用来保存注册的观察者对象
     */
    private List<ObserverPull> observers = new ArrayList<>();

    /**
     * 注册观察者对象
     *
     * @param observer 观察者对象
     */
    void attach(ObserverPull observer) {
        observers.add(observer);
    }

    /**
     * 通知所有注册的观察者对象
     */
    void notifyObservers() {
        for (ObserverPull observer : observers) {
            observer.update(this);
        }
    }
}
class ConcreteSubjectPull extends SubjectPull {

    private String subjectState;

    public String getSubjectState() {
        return subjectState;
    }

    public void change(String subjectState) {
        this.subjectState = subjectState;
        //状态发生改变，通知各个观察者
        this.notifyObservers();
    }
}


interface ObserverPull {
    /**
     * 更新的接口
     *
     * @param subject 传入目标对象，好获取相应的目标对象的状态
     */
    void update(SubjectPull subject);
}

class ConcreteObserverPull implements ObserverPull {

    @Override
    public void update(SubjectPull subject) {
        //具体的更新实现
        //这里可能需要更新观察者的状态，使其与目标的状态保持一致
        System.out.println("接收到：" + ((ConcreteSubjectPull)subject).getSubjectState());
    }
}
public class ObseverPattern {
    public static void main(String[] args) {
        //主动推模式
    	//创建主题对象
        ConcreteSubject subject = new ConcreteSubject();
        //创建观察者对象
        ObserverPush observer = new ConcreteObserver();
        //将观察者对象登记到主题对象上
        subject.attach(observer);
        //改变主题对象的状态
        subject.change("push state");
        
        System.out.println("主动推-PUSH 模式结束");
        System.out.println("---------------------------------");
        
        
        //主动拉模式
        //创建主题对象
        ConcreteSubjectPull subjectPull = new ConcreteSubjectPull();
        //创建观察者对象
        ObserverPull observerPull = new ConcreteObserverPull();
        //将观察者对象登记到主题对象上
        subjectPull.attach(observerPull);
        //改变主题对象的状态
        subjectPull.change("pull state");
        
        System.out.println("主动提醒+被动拉-PULL 模式结束");
        
    }
}
