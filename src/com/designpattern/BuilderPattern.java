package com.designpattern;

/*建造者模式（Builder Pattern）属于创建型模式的一种，将多个简单对象构建成一个复杂的对象，构建过程抽象化，
不同实现方法可以构造出不同表现（属性）的对象，还提供了一种更加优雅构建对象的方式…

概述
有时候构建一个复杂的对象，需要经过好几步的处理，比如常用的StringBuffer、StringBuilder、以及Swagger（一种接口文档），都是以这种模式构建对象的

优点
·建造者模式比较独立，将对象本身与构建过程解耦
·精准控制构建出的对象和内容，构造层和显示层是分离的
·写法上更加优雅

缺点
·范围受限，不适合差异较大的对象
·内部复杂多变，构造类相对会多

适用场景
·构建具有共同特性的复杂对象

相关模式
抽象工厂模式与建造者模式相似，因为它也可以创建复杂对象。主要的区别是建造者模式着重于一步步得构造出复杂对象。
而抽象工厂模式着重于多个系列的产品对象（简单的或是复杂的）。建造者是在最后的一步返回对象，而对于抽象工厂来说，对象是立即返回的。*/
class Summoner {

    private String name;
    private String type;
    private String innate;

    private Summoner(Builder builder) {
        this.name = builder.name;
        this.type = builder.type;
        this.innate = builder.innate;
        
    }
    @Override
    public String toString() {
    	// TODO 自动生成的方法存根
    	return super.toString()+this.name+" "+this.type+" "+this.innate;
    }

    protected static class Builder {
        private String name;
        private String type;
        private String innate;

        protected Builder name(String name) {
            this.name = name;
            return this;
        }

        protected Builder type(String type) {
            this.type = type;
            return this;
        }

        protected Builder innate(String innate) {
            this.innate = innate;
            return this;
        }
        protected Summoner build() {
            return new Summoner(this);
        }
    }
}

public class BuilderPattern {
	public static void main(String[] args) {
        Summoner monkey = new Summoner.Builder().name("齐天大圣 - 孙悟空").type("上单 - AD").innate("基石天赋 - 战争雷霆").build();
        System.out.println(monkey.toString());

        Summoner mouse = new Summoner.Builder().name("瘟疫之源 - 图奇").type("下路 - ADC").innate("基石天赋 - 战阵热诚").build();
        System.out.println(mouse.toString());

        Summoner diann = new Summoner.Builder().name("皎月女神 - 戴安娜").type("中单 - AP").build();
        System.out.println(diann.toString());
        
        Summoner test=new Summoner.Builder().name("111").type("shadiao").innate("杀掉眺望").build();
        System.out.println(test.toString());
        		
    }
}
