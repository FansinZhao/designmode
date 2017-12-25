package com.fansin.designmode;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 17 -12-25 下午11:23
 */
public class FactoryFunctionDemo {

    /***
     * 工厂方法模式:工厂可以有多个,真正生成实例时,由具体工厂类实现,从而实现对象延迟到工厂实例
     *
     * 项目应用:
     * 实时授信统计服务需要为三个角色提供统计结果
     * 产品父类:TransStatistics:共同参数:今日交易笔数,今日交易金额,昨日交易笔数,昨日交易金额
     * 产品子类:UnionPayTransStatistics:银联用户:今日贷款行数,今日机构数
     * 产品子类:LoanBankTransStatistics:贷款行用户:今日机构数
     * 产品子类:AcquirerTransStatistics:机构用户:今日提现用户数
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        AbstractTransStatisticsFactory unionPayFactory = new UnionPayTransStatisticsFactory();
        AbstractTransStatisticsFactory loanBankPayFactory = new LoanBankTransStatisticsFactory();
        AbstractTransStatisticsFactory acquirerFactory = new AcquirerTransStatisticsFactory();
        System.out.println("银联工厂类:"+JSONObject.toJSONString(unionPayFactory.getTransStatistics(null)));
        System.out.println("贷款行工厂类:"+JSONObject.toJSONString(loanBankPayFactory.getTransStatistics(null)));
        System.out.println("机构工厂类:"+JSONObject.toJSONString(acquirerFactory.getTransStatistics(null)));

    }
}

/**
 * The type Acquirer trans statistics factory.
 */
class AcquirerTransStatisticsFactory extends AbstractTransStatisticsFactory {

    @Override
    public void preProcess(){
        //TODO 业务处理,查询数据库等
        System.out.println("[业务]:生成贷款行交易统计信息!");
    }

    @Override
    public TransStatistics createTransStatistics(String type) {
//        preProcess();
        return new AcquirerTransStatistics(33,33,33,33,33);
    }

}


/**
 * The type Loan bank trans statistics factory.
 */
class LoanBankTransStatisticsFactory extends AbstractTransStatisticsFactory {

    @Override
    public void preProcess(){
        //TODO 业务处理,查询数据库等
        System.out.println("[业务]:生成贷款行交易统计信息!");
    }

    @Override
    public TransStatistics createTransStatistics(String type) {
//        preProcess();
        return new LoanBankTransStatistics(22,22,22,22,22);
    }

}

/**
 * The type Union pay trans statistics factory.
 */
class UnionPayTransStatisticsFactory extends AbstractTransStatisticsFactory {

    /**
     * The Leader.
     */
    protected String leader = "leader";

    @Override
    public void preProcess(){
        //TODO 业务处理,查询数据库等
        System.out.println("[业务]:生成银联交易统计信息!");
    }

    @Override
    public TransStatistics createTransStatistics(String type) {
//        preProcess();
        if(type == null){
                return new UnionPayTransStatistics(1,1,1,1,1,1);
        }
        switch (type){
            case "attendance":
                return new UnionPayAttendanceTransStatistics(101,101,101,101,101,101);
            case "clerk":
                return new UnionPayClerkTransStatistics(102,102,102,102,102,102);
            case "leader":
                return new UnionPayLeaderTransStatistics(103,103,103,103,103,103);

            default:
                return new UnionPayTransStatistics(1,1,1,1,1,1);
        }
    }

}

/**
 * The type Abstract trans statistics factory.
 */
abstract class AbstractTransStatisticsFactory {

    /**
     * Pre process.
     */
    public abstract void preProcess();

    /**
     * Get trans statistics trans statistics.
     * 统一创建对象方法
     *
     * @param type the type
     * @return the trans statistics
     */
    TransStatistics getTransStatistics(String type){
        preProcess();
        TransStatistics transStatistics =  createTransStatistics(type);
        transStatistics = postProcess(transStatistics);
        return transStatistics;
    }

    /**
     * Post process trans statistics.
     *
     * @param transStatistics the trans statistics
     * @return the trans statistics
     */
    public TransStatistics postProcess(TransStatistics transStatistics){
        System.out.println("结果验证!");
        if (transStatistics == null){
            return new TransStatistics(0L, 0L, 0L, 0L);
        }else {
            return transStatistics;
        }
    }

    /**
     * Create trans statistics trans statistics.
     *
     * @param type the type
     * @return the trans statistics
     */
    public abstract TransStatistics createTransStatistics(String type);
}


/**
 * The type Acquirer trans statistics.
 */
class AcquirerTransStatistics extends TransStatistics{


    private long merchantNum;


    /**
     * Instantiates a new Acquirer trans statistics.
     *
     * @param merchantNum the merchant num
     */
    public AcquirerTransStatistics(long merchantNum) {
        this.merchantNum = merchantNum;
    }

    /**
     * Instantiates a new Acquirer trans statistics.
     *
     * @param lastTransNum the last trans num
     * @param lastTransAmt the last trans amt
     * @param nowTransNum  the now trans num
     * @param nowTransAmt  the now trans amt
     * @param merchantNum  the merchant num
     */
    public AcquirerTransStatistics(long lastTransNum, long lastTransAmt, long nowTransNum, long nowTransAmt, long merchantNum) {
        super(lastTransNum, lastTransAmt, nowTransNum, nowTransAmt);
        this.merchantNum = merchantNum;
    }

    /**
     * Sets merchant num.
     *
     * @param merchantNum the merchant num
     */
    public void setMerchantNum(long merchantNum) {
        this.merchantNum = merchantNum;
    }
}

/**
 * The type Loan bank trans statistics.
 */
class LoanBankTransStatistics extends TransStatistics{
    private long acquirerNum;

    /**
     * Instantiates a new Loan bank trans statistics.
     *
     * @param acquirerNum the acquirer num
     */
    public LoanBankTransStatistics(long acquirerNum) {
        this.acquirerNum = acquirerNum;
    }

    /**
     * Instantiates a new Loan bank trans statistics.
     *
     * @param lastTransNum the last trans num
     * @param lastTransAmt the last trans amt
     * @param nowTransNum  the now trans num
     * @param nowTransAmt  the now trans amt
     * @param acquirerNum  the acquirer num
     */
    public LoanBankTransStatistics(long lastTransNum, long lastTransAmt, long nowTransNum, long nowTransAmt, long acquirerNum) {
        super(lastTransNum, lastTransAmt, nowTransNum, nowTransAmt);
        this.acquirerNum = acquirerNum;
    }

    /**
     * Gets acquirer num.
     *
     * @return the acquirer num
     */
    public long getAcquirerNum() {
        return acquirerNum;
    }

    /**
     * Sets acquirer num.
     *
     * @param acquirerNum the acquirer num
     */
    public void setAcquirerNum(long acquirerNum) {
        this.acquirerNum = acquirerNum;
    }
}

/**
 * The type Union pay trans statistics.
 */
class UnionPayTransStatistics extends TransStatistics{


    private long loanBankNum;
    private long acquirerNum;

    /**
     * Instantiates a new Union pay trans statistics.
     *
     * @param lastTransNum the last trans num
     * @param lastTransAmt the last trans amt
     * @param nowTransNum  the now trans num
     * @param nowTransAmt  the now trans amt
     * @param loanBankNum  the loan bank num
     * @param acquirerNum  the acquirer num
     */
    public UnionPayTransStatistics(long lastTransNum, long lastTransAmt, long nowTransNum, long nowTransAmt, long loanBankNum, long acquirerNum) {
        super(lastTransNum, lastTransAmt, nowTransNum, nowTransAmt);
        this.loanBankNum = loanBankNum;
        this.acquirerNum = acquirerNum;
    }


    /**
     * Gets loan bank num.
     *
     * @return the loan bank num
     */
    public long getLoanBankNum() {
        return loanBankNum;
    }

    /**
     * Sets loan bank num.
     *
     * @param loanBankNum the loan bank num
     */
    public void setLoanBankNum(long loanBankNum) {
        this.loanBankNum = loanBankNum;
    }

    /**
     * Gets acquirer num.
     *
     * @return the acquirer num
     */
    public long getAcquirerNum() {
        return acquirerNum;
    }

    /**
     * Sets acquirer num.
     *
     * @param acquirerNum the acquirer num
     */
    public void setAcquirerNum(long acquirerNum) {
        this.acquirerNum = acquirerNum;
    }
}

/**
 * The type Trans statistics.
 */
/* 接口或类 */
@Data
class  TransStatistics{
    /**昨日交易笔数*/
    private long lastTransNum;
    /**昨日交易金额 单位:分*/
    private long lastTransAmt;
    /**今日交易笔数*/
    private long nowTransNum;
    /**今日交易金额 单位:分*/
    private long nowTransAmt;


    /**
     * Instantiates a new Trans statistics.
     */
    public TransStatistics() {
    }

    /**
     * Instantiates a new Trans statistics.
     *
     * @param lastTransNum the last trans num
     * @param lastTransAmt the last trans amt
     * @param nowTransNum  the now trans num
     * @param nowTransAmt  the now trans amt
     */
    public TransStatistics(long lastTransNum, long lastTransAmt, long nowTransNum, long nowTransAmt) {
        this.lastTransNum = lastTransNum;
        this.lastTransAmt = lastTransAmt;
        this.nowTransNum = nowTransNum;
        this.nowTransAmt = nowTransAmt;
    }
//
//    public long getLastTransNum() {
//        return lastTransNum;
//    }
//
//    public void setLastTransNum(long lastTransNum) {
//        this.lastTransNum = lastTransNum;
//    }
//
//    public long getLastTransAmt() {
//        return lastTransAmt;
//    }
//
//    public void setLastTransAmt(long lastTransAmt) {
//        this.lastTransAmt = lastTransAmt;
//    }
//
//    public long getNowTransNum() {
//        return nowTransNum;
//    }
//
//    public void setNowTransNum(long nowTransNum) {
//        this.nowTransNum = nowTransNum;
//    }
//
//    public long getNowTransAmt() {
//        return nowTransAmt;
//    }
//
//    public void setNowTransAmt(long nowTransAmt) {
//        this.nowTransAmt = nowTransAmt;
//    }

}

