package com.fansin.designmode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 17 -12-25 下午11:23
 */
public class ObserverDemo {

    /*
    观察者模式:
    一对多模式,主题方发生变化,通知所有订阅方,订阅方可以进行相应操作
    适用场景:一个对象的变化会引起其他对象操作.
    业务场景:
    当代付返回成功结果时,需要通知提现服务和监控服务.

     */

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        AbstractPayStatusSubject payStatusSubject = new SuccessSubject();
        payStatusSubject.addObserver(new StatisticObserver());
        payStatusSubject.addObserver(new WithdrawObserver());
        payStatusSubject.noticeObservers();

    }
}

/**
 * The type Abstract pay status subject.
 */
abstract class AbstractPayStatusSubject {
    private List<Observer> observerList = Collections.synchronizedList(new ArrayList<Observer>());

    /**
     * Add observer.
     *
     * @param observer the observer
     */
    public void addObserver(Observer observer){
        observerList.add(observer);
    }

    /**
     * Remove observer.
     *
     * @param observer the observer
     */
    public void removeObserver(Observer observer){
        observerList.remove(observer);
    }

    /**
     * Notice observers.
     */
    public void noticeObservers(){
        System.out.println("已注册观察者数量:"+observerList.size());
        for (Observer observer : observerList) {
            observer.update(notice());
        }
    }

    /**
     * Notice string.
     *
     * @return the string
     */
    abstract String notice();
}

/**
 * The type Success subject.
 */
class SuccessSubject extends AbstractPayStatusSubject {
    @Override
    String notice() {
        return "支付成功";
    }
}

/**
 * The interface Observer.
 */
interface Observer{
    /**
     * Update.
     *
     * @param msg the msg
     */
    void update(String msg);
}

/**
 * The type Statistic observer.
 */
class StatisticObserver implements Observer{
    @Override
    public void update(String msg) {
        System.out.println("StatisticObserver.update 收到通知:"+msg);
        //TODO 业务处理
        System.out.println("[交易统计服务]更新交易统计信息......");
    }
}

/**
 * The type Withdraw observer.
 */
class WithdrawObserver implements Observer{
    @Override
    public void update(String msg) {
        System.out.println("WithdrawObserver.update 收到通知:"+msg);
        //TODO 业务处理
        System.out.println("[提现服务]完成提现入账交易.....");
    }
}