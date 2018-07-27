package com.designpattern;
/*策略模式（Strategy Pattern）属于对象行为型模式的一种，其用意是针对一组算法，将每一个算法封装到具有共同接口的独立的类中，
从而使得它们可以相互替换。策略模式使得算法可以在不影响到客户端的情况下发生变化。
	策略模式： 是对算法的包装，是把使用算法的责任和算法本身分割开来，委派给不同的对象管理。
策略模式通常把一个系列的算法包装到一系列的策略类里面，作为一个抽象策略类的子类。
用一句话来说，就是：“准备一组算法，并将每一个算法封装起来，使得它们可以互换”。下面就以一个示意性的实现讲解策略模式实例的结构。

模式结构
`环境(Context)角色： 持有一个Strategy的引用。
`抽象策略(Strategy)角色： 这是一个抽象角色，通常由一个接口或抽象类实现。此角色给出所有的具体策略类所需的接口。
`具体策略(ConcreteStrategy)角色： 包装了相关的算法或行为。
*/
/**
 * 会员优惠策略
 */
interface MemberStrategy {
    /**
     * 计算图书的价格
     *
     * @param booksPrice 图书的原价
     * @return 计算出打折后的价格
     */
    double calcPrice(double booksPrice);
}


class PrimaryMemberStrategy implements MemberStrategy {

    @Override
    public double calcPrice(double booksPrice) {
        System.out.println("对于初级会员的没有折扣");
        return booksPrice;
    }

}

class IntermediateMemberStrategy implements MemberStrategy {

    @Override
    public double calcPrice(double booksPrice) {
        System.out.println("对于中级会员的折扣为10%");
        return booksPrice * 0.9;
    }

}

class AdvancedMemberStrategy implements MemberStrategy {

    @Override
    public double calcPrice(double booksPrice) {
        System.out.println("对于高级会员的折扣为20%");
        return booksPrice * 0.8;
    }
}

class Price {

    /**
     * 持有一个具体的策略对象
     */
    private MemberStrategy strategy;

    /**
     * 构造函数，传入一个具体的策略对象
     *
     * @param strategy 具体的策略对象
     */
    public Price(MemberStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * 计算图书的价格
     *
     * @param booksPrice 图书的原价
     * @return 计算出打折后的价格
     */
    public double quote(double booksPrice) {
        return this.strategy.calcPrice(booksPrice);
    }
    
    public void changeStrage(MemberStrategy strategy) {
		this.strategy=strategy;
	}
}

public class StrategyPattern {
	public static void main(String[] args) {

        Price price;
        //创建环境
        price = new Price(new PrimaryMemberStrategy());
        //计算价格
        System.out.println("图书的最终价格为：" + price.quote(300));

        //创建环境
        price = new Price(new AdvancedMemberStrategy());
        //计算价格
        System.out.println("图书的最终价格为：" + price.quote(300));
        
        //用户突然注销会员，修改优惠政策
        
        price.changeStrage(new PrimaryMemberStrategy());
        //计算价格
        System.out.println("高级会员注销后，图书的最终价格为：" + price.quote(300));

    }
}
