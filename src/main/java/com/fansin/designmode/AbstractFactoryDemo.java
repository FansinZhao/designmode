package com.fansin.designmode;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 17-12-25 下午11:23
 */
public class AbstractFactoryDemo {



    /**
     *
     * 抽象工厂模式
     * 产品具有产品簇,工程有多个具体工厂
     *
     * 在工厂方法实现的统计对象的基础上,例如银联用户又分为值班人员,业务员,领导,对于他们每个人展示的结果又是不同,即一簇产品
     * 项目应用:
     * 实时授信统计服务需要为三个角色提供统计结果
     * 产品父类:TransStatistics:共同参数:今日交易笔数,今日交易金额,昨日交易笔数,昨日交易金额
     * 产品子类:UnionPayTransStatistics:银联用户:今日贷款行数,今日机构数
     *     值班人员:UnionPayAttendanceTransStatistics
     *      业务员:UnionPayClerkTransStatistics
     *       领导:UnionPayLeaderTransStatistics
     * 产品子类:LoanBankTransStatistics:贷款行用户:今日机构数
     *      省略
     * 产品子类:AcquirerTransStatistics:机构用户:今日提现用户数
     *      省略
     *
     * */


    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        AbstractTransStatisticsFactory unionPayFactory = new UnionPayTransStatisticsFactory();
        System.out.println("银联工厂类:值班人员:"+JSONObject.toJSONString(unionPayFactory.getTransStatistics("attendance")));
        System.out.println("银联工厂类:业务员:"+JSONObject.toJSONString(unionPayFactory.getTransStatistics("clerk")));
        System.out.println("银联工厂类:领导:"+JSONObject.toJSONString(unionPayFactory.getTransStatistics("leader")));
    }


}

class UnionPayAttendanceTransStatistics extends UnionPayTransStatistics{
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date nowTime;

    UnionPayAttendanceTransStatistics(long lastTransNum, long lastTransAmt, long nowTransNum, long nowTransAmt, long loanBankNum, long acquirerNum) {
        super(lastTransNum, lastTransAmt, nowTransNum, nowTransAmt, loanBankNum, acquirerNum);
        this.nowTime = new Date();
    }

    public Date getNowTime() {
        return nowTime;
    }

    public void setNowTime(Date nowTime) {
        this.nowTime = nowTime;
    }
}
@Data
class UnionPayClerkTransStatistics extends UnionPayTransStatistics{
    private String status;

    UnionPayClerkTransStatistics(long lastTransNum, long lastTransAmt, long nowTransNum, long nowTransAmt, long loanBankNum, long acquirerNum) {
        super(lastTransNum, lastTransAmt, nowTransNum, nowTransAmt, loanBankNum, acquirerNum);
        this.status = "已启用";
    }

}

class UnionPayLeaderTransStatistics extends UnionPayTransStatistics{

    UnionPayLeaderTransStatistics(long lastTransNum, long lastTransAmt, long nowTransNum, long nowTransAmt, long loanBankNum, long acquirerNum) {
        super(lastTransNum, lastTransAmt, nowTransNum, nowTransAmt, loanBankNum, acquirerNum);
    }
}

