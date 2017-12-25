package com.fansin.designmode;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 17 -12-25 下午11:23
 */
public class TemplateDemo {

    /*

    模板方法:
    一种比较简单使用的模式,基本定义是:先将骨架模板写好,留有需要用户自行处理的接口.
    应用场景:
    1 多个子类有相同或相似的代码
    2 有比较重要或复杂的方法
    3 模板方法不可改变

    业务场景:
    报表导出:1获取数据 2处理数据 3写入文件
    2步是变化,将其抽象
     */

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        AuthReporterProcess reporterProcess = new AuthReporterProcess();
        reporterProcess.process();


        WithDrawReporterProcess withDrawReporterProcess = new WithDrawReporterProcess();
        withDrawReporterProcess.process();
    }

}

/**
 * The type Abstract process data template.
 */
abstract class AbstractProcessDataTemplate {

    /**
     * Process.
     */
    public final void process(){//不可篡改
        System.out.println("获取数据库资源");
        setProperties();
        doProcess();//建议使用doXXXX作为抽象方法
        System.out.println("写入文件");
    }

    /**
     * Sets properties.
     */
    protected abstract void setProperties();

    /**
     * Do process.
     */
    protected abstract void doProcess();

}

/**
 * The type Auth reporter process.
 */
class AuthReporterProcess extends AbstractProcessDataTemplate {
    @Override
    protected void setProperties() {
        System.out.println("设置需要获取的表信息:授信表");
    }

    @Override
    public void doProcess() {
        System.out.println("处理授信表信息");
    }
}

/**
 * The type With draw reporter process.
 */
class WithDrawReporterProcess extends AbstractProcessDataTemplate {
    @Override
    protected void setProperties() {
        System.out.println("设置需要获取的表信息:提现表");
    }

    @Override
    protected void doProcess() {
        System.out.println("处理提现表信息");
    }
}