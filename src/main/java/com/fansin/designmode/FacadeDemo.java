package com.fansin.designmode;

import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.util.NumberUtil;

import java.util.Date;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 17 -12-25 下午11:23
 */
public class FacadeDemo {
    /*
    门面模式/外观模式:
    为外部提供简洁的接口,而接口功能可能是有多个子接口共同完成的.
    业务场景:
    项目中有三个模块,第一个web界面服务,第二个代付服务,第三个提现记账服务.
    用户流程:提现请求--->web界面--->提现记账/代付消费服务
     */

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        WebFacade webFacade = new WebFacade();
        System.out.println(webFacade.withDraw("中国人",500));
        System.out.println(webFacade.withDraw("中国人",1500));
        System.out.println(webFacade.withDraw("法国人",500));
        System.out.println(webFacade.withDraw("法国人",2500));
        System.out.println(webFacade.withDraw("日本人",500));
        System.out.println(webFacade.withDraw("美国人",500));

    }
}

/**
 * The type Web facade.
 */
class WebFacade{
    private WebService webService = new WebService();
    private WithDrawService withDrawService = new WithDrawService();
    private CollectionService collectionService = new CollectionService();


    /**
     * Instantiates a new Web facade.
     */
    public WebFacade() {

        System.out.println("连接系统平台成功!.....");
    }

    /**
     * With draw string.
     *
     * @param user the user
     * @param amt  the amt
     * @return the string
     */
    public String withDraw(String user, long amt){
        System.out.println("------------------开始提现");
        //1 验证业务时间
        if(!webService.validateTime()){
            return "返回结果:不在营业时间!";
        }
        //2 验证账户合法性(这个也是一个门面,为了简化,就认为普通子接口)
        if(!webService.validateUser(user)){
            return "返回结果:用户不合法!";
        }
        if(!webService.validate(user,amt)){
            return "返回结果:账号异常!";
        }
        //3 提现记账
        if(!withDrawService.freezeFunds(user,amt)){
            return "返回结果:资金余额不足!";
        }
        //4代付消费
        if(!collectionService.pay(user,amt)){
            withDrawService.rollbackFreezeFunds();
            System.out.println("------------------提现完成");
            return "返回结果:提现失败!";
        }else {
            withDrawService.clearFreezeFunds();
            System.out.println("------------------提现完成");
            return "返回结果:提现成功!";
        }
    }
}

/**
 * The type Collection service.
 */
class CollectionService{
    /**
     * Pay boolean.
     *
     * @param user the user
     * @param amt  the amt
     * @return the boolean
     */
    public  boolean  pay(String user,long amt){
        if(new Random().nextBoolean()){
            System.out.println("支付成功:"+user +" "+amt);
            return true;
        }else {
            System.out.println("消费超时.....");
            return false;
        }
    }

}


/**
 * The type With draw service.
 */
class WithDrawService{

    /**
     * The Web service.
     */
    WebService webService = new WebService();
    private int consumption = 100;

    /**
     * Freeze funds boolean.
     *
     * @param user the user
     * @param amt  the amt
     * @return the boolean
     */
    public boolean freezeFunds(String user ,long amt){
            if (webService.validate(user,amt+ consumption)){
            return true;
        }else {
            System.out.println("资金不足.....");
            return false;
        }
    }

    /**
     * Clear freeze funds.
     */
    public void clearFreezeFunds(){
        System.out.println("提现成功,额度减少...");
    }

    /**
     * Rollback freeze funds.
     */
    public void rollbackFreezeFunds(){
        System.out.println("回滚业务,解冻冻结资金,返回用户账户!");
    }

}

/**
 * The type Web service.
 */
class WebService{

    private String japanese = "日本人";
    private String chinese = "中国人";
    private String french = "法国人";
    private int smallLimit = 1000;
    private int largeLimit = 2000;

    /**
     * Validate time boolean.
     *
     * @return the boolean
     */
    public boolean validateTime(){

        if (DateUtil.isPM(new Date())){
            return true;
        }else {
            System.out.println("业务时间已过!!!");
            return false;
        }
    }

    /**
     * Validate user boolean.
     *
     * @param user the user
     * @return the boolean
     */
    public boolean validateUser(String user){
        //黑名单验证
        if(japanese.equalsIgnoreCase(user)){
            System.out.println("禁止日本人开展提现业务!");
            return false;
        }else{
            return true;
        }
    }

    /**
     * Validate boolean.
     *
     * @param user the user
     * @param amt  the amt
     * @return the boolean
     */
    public boolean validate(String user,long amt){
        if (chinese.equalsIgnoreCase(user)){
            if(NumberUtil.compare(amt,smallLimit) == 1){
                System.out.println("不能给中国人提现超过1000,他们太能花了!");
                return false;
            }else {
                return true;
            }
        }
        if (french.equalsIgnoreCase(user)){
            if (NumberUtil.compare(amt,largeLimit) == 1){
                System.out.println("买香水的钱太多了吧!");
                return false;
            }else {
                return true;
            }
        }
        System.out.println("未开展业务,请去柜台办理!");
        return false;
    }

}


