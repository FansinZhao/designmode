package com.fansin.designmode;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 17-12-25 下午11:23
 */
public class SingletonDemo {

    public static void main(String[] args) {
        System.out.println("调用单例中的其他静态成员变量触发初始化过程!");
        System.out.println("调用单例静态变量:");
        System.out.println(UtilsSingleton.STATUS);
        System.out.println(NormalSingleton.STATUS);
        System.out.println("调用单例实例生成接口:");
        UtilsSingleton.getInstance();
        NormalSingleton.getIntance();

    }
}

class NormalSingleton{
    static int STATUS = 1;

    private NormalSingleton(){
        System.out.println("提前实例化:"+this.getClass().getName());
    }
    private static NormalSingleton singleton = new NormalSingleton();
    static NormalSingleton getIntance(){
        return singleton;
    }

}

class UtilsSingleton{

    static int STATUS = 1;

    private UtilsSingleton(){
        System.out.println("单例模式 调用静态方法时主动引用:只允许一次系统初始化,用户不能使用new!");
    }

    private static class UtilsSingletonHolder{
        /**只有调用此类时才初始化单例*/
        private static UtilsSingleton instance = new UtilsSingleton();
    }

    /**
     * 1 类初始化时,不会创建实例,只有调用此方法时才会调用
     * 2 无需添加同步锁
     * */
    static UtilsSingleton getInstance(){
        return UtilsSingletonHolder.instance;
    }

}