package com.fansin.designmode;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 17 -12-25 下午11:23
 */
public class StrategyDemo {

    /*
    策略模式:
    将对象自身与算法分离.
    应用场景:
    1 拥有不同行为的一群类
    2 使用算法变种
    3 else if 比较多
    业务场景:
    测试代付三种场景,定时代付/平台自助提现/平台下提现
     */

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Payment payment = new Payment(new FixedTimeStrategy());
        payment.pay();
        payment = new Payment(new WithdrawStrategy());
        payment.pay();
        payment = new Payment(new AcquirerStrategy());
        payment.pay();
    }
}

/**
 * The interface Strategy.
 */
interface Strategy{
    /**
     * From.
     */
    void from();
}


/**
 * The type Fixed time strategy.
 */
class FixedTimeStrategy implements Strategy{
    @Override
    public void from() {
        System.out.println("定时代付....");
    }
}

/**
 * The type Withdraw strategy.
 */
class WithdrawStrategy implements Strategy{
    @Override
    public void from() {
        System.out.println("平台自助代付....");
    }
}

/**
 * The type Acquirer strategy.
 */
class AcquirerStrategy implements Strategy{
    @Override
    public void from() {
        System.out.println("机构独立代付....");
    }
}

/**
 * The type Payment.
 */
class  Payment{
    private Strategy strategy;

    /**
     * Instantiates a new Payment.
     *
     * @param strategy the strategy
     */
    public Payment(Strategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Pay.
     */
    public void pay(){
        strategy.from();
        System.out.println("发送代付请求....");
    }


}