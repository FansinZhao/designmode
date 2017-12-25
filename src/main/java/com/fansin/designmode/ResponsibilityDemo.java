package com.fansin.designmode;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 17 -12-25 下午11:23
 */
public class ResponsibilityDemo {
    /*
    责任链模式:
    很多对象由一个对象对其下级对象的连接而连接成一个链.当链上有个请求时,会顺着链向下查找对象处理.
    发送请求的对象不知道处理对象.处理过程可以动态设置,很好的实现了组织和分配责任.
    应用场景:
    1 多个请求可以被多个类处理.
    2 动态的更换链上的处理类.
    异常捕获是常见的责任链

    业务场景:
    发起一笔交易,当交易选择付款时,有多个选择,可以选择深银联,总银联,广银联,迅联等等
    通过为每个银联处理类定义一个规则,当请求到达时,各个银联根据条件选择是否为其服务.
     */

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        //深银联-->广银联-->迅联
        AbstractHandler handler = new ShenZhenHandler();
        handler.setNextHandler(new GuangZhouHandler());
        CustomRequest customRequest = new CustomRequest();
        customRequest.setAmt(999);
        customRequest.setSite("sz");
        customRequest.setType("01");
        customRequest.setHandler(handler);
        System.out.println("-------正常消费--------");
        customRequest.process();
        customRequest.setSite("usa");
        System.out.println("------------消费失败----------");
        customRequest.process();
        handler.getNextHandler().setNextHandler(new XunLianHandler());
        customRequest.setHandler(handler);
        System.out.println("------------动态添加handle,处理成功----------");
        customRequest.process();
    }
}

/**
 * The type Abstract tran request.
 */
//将交易抽象化
@Data
abstract class AbstractTranRequest {
    /**
     * The Amt.
     */
    protected long amt;
    /**
     * The Site.
     */
    protected String site;
    /**
     * The Type.
     */
    protected String type;

    private AbstractHandler handler;


    /**
     * Process.
     * 模板方法
     */
    void process(){
        if(amt == 0){
            System.out.println("金额为空");
        }

        while (handler != null && !handler.handle(this)){
            handler = handler.nextHandler();
        }

        if (handler == null){
            failRequest();//处理失败
        }

    }

    /**
     * Fail request.
     */
    abstract void failRequest();
}

/**
 * The type Custom request.
 */
class CustomRequest extends AbstractTranRequest {


    @Override
    void failRequest() {
        System.out.println("普通消费处理失败,发起冲正....");
    }
}

/**
 * The type Abstract handler.
 */
abstract class AbstractHandler {

    /**
     * The Sz limit.
     */
    protected int szLimit = 10000;
    /**
     * The Gz limit.
     */
    protected int gzLimit = 5000;
    /**
     * The Xl limit.
     */
    protected int xlLimit = 5000;
    /**
     * The Sz.
     */
    protected String sz = "sz";
    /**
     * The Gz.
     */
    protected String gz = "gz";
    /**
     * The Xl.
     */
    protected String xl = "xl";

    private AbstractHandler nextHandler;

    /**
     * Sets next handler.
     *
     * @param nextHandler the next handler
     */
    public void setNextHandler(AbstractHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    /**
     * Gets next handler.
     *
     * @return the next handler
     */
    public AbstractHandler getNextHandler() {
        return nextHandler;
    }

    /**
     * Next handler abstract handler.
     *
     * @return the abstract handler
     */
    public AbstractHandler nextHandler(){
        return nextHandler;
    }

    /**
     * Handle boolean.
     *
     * @param request the request
     * @return the boolean
     */
    abstract boolean handle(AbstractTranRequest request);
}

/**
 * The type Shen zhen handler.
 */
class ShenZhenHandler extends AbstractHandler {
    @Override
    public boolean handle(AbstractTranRequest request) {

        if (request.getAmt() < szLimit && sz.equalsIgnoreCase(request.getSite())){
            System.out.println("发往深银联进行消费....成功");
            return true;
        }

        System.out.println("深银联处理不了!金额>1000或不是sz");
        return false;
    }
}

/**
 * The type Guang zhou handler.
 */
class GuangZhouHandler extends AbstractHandler {
    @Override
    public boolean handle(AbstractTranRequest request) {

        if (request.getAmt() < gzLimit && gz.equalsIgnoreCase(request.getSite())){
            System.out.println("发往广银联进行消费....成功");
            return true;
        }
        System.out.println("广银联处理不了!金额>2000或不是gz");
        return false;
    }
}

/**
 * The type Xun lian handler.
 */
class XunLianHandler extends AbstractHandler {
    @Override
    public boolean handle(AbstractTranRequest request) {

        if (request.getAmt() < xlLimit && xl.equalsIgnoreCase(request.getSite())){
            System.out.println("发往迅联进行消费....成功");
            return true;
        }
        System.out.println("迅联处理不了!金额>5000或不是usa");
        return false;
    }
}


