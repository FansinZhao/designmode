package com.fansin.designmode;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 17 -12-25 下午11:23
 */
public class CommandDemo {
    /*
    命令模式:
    将请求封装成一个对象,请求队列或记录日志,并提供撤销功能.
    应用场景:
    1 多级回退
    2 原子事务
    3 状态条
    4 导航
    5 ThreadPool
    业务场景:
    模仿一下Thread的使用

     */

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        System.out.println("------使用Executor-------");
        ExecutorService service = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(1), r -> new Thread(r,"ThreadFactory newThread"));
        service.execute(() -> {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" runnable:执行业务处理1");
        });
        service.execute(() -> System.out.println(Thread.currentThread().getName()+" runnable:执行业务处理2"));
        service.shutdown();

        CommandClient commandClient = new CommandClient();
        AbstractCommand command1 = new CommandThread(new CommandReceiver(),"command1");
        AbstractCommand command2 = new CommandThread(new CommandReceiver(),"command2");
        commandClient.execute(command1);
        commandClient.execute(command2);
    }
}

//command接口 interface Runnable

/**
 * The type Abstract command.
 * Runnable 和 Thread 简单合体
 */
abstract class AbstractCommand {

    private String name;

    /**
     * Instantiates a new Abstract command.
     *
     * @param name the name
     */
    public AbstractCommand(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * Run.
     */
    abstract void run();

    /**
     * Start.
     */
    public void start(){

        try {
        Thread.sleep(Math.round(Math.random()*3000));
        run();//Thread的run是根据cpu空闲时间觉定的,这里使用random模拟
        } catch (InterruptedException e) {
        e.printStackTrace();
        }
        }
}

/**
 * The type Command thread.
 * command具体实现 Thread implements Runnable
 */
class CommandThread extends AbstractCommand {
    private CommandReceiver receiver;

    /**
     * Instantiates a new Command thread.
     *
     * @param receiver the receiver
     * @param name     the name
     */
    public CommandThread(CommandReceiver receiver,String name) {
        super(name);
        this.receiver = receiver;
    }

    @Override
    public void run() {
        receiver.doSomething();
    }
}

/**
 * The type Command receiver.
 * Receiver Thread中的任务
 */
class CommandReceiver {

    /**
     * Do something.
     */
    public void doSomething(){
        System.out.println(Thread.currentThread().getName()+":处理一些业务");
    }

}

/**
 * The type Command client.
 * invoker 线程池,调用execute()/submit()
 */
class  CommandClient{
    private Set<AbstractCommand> set = Collections.synchronizedSet(new HashSet<>());

    /**
     * Execute.
     *
     * @param command the command
     */
    public void execute(AbstractCommand command){
        set.add(command);
        command.start();
    }

    /**
     * Cancel.
     *
     * @param command the command
     */
    public void cancel(AbstractCommand command){
        set.remove(command);
    }





}