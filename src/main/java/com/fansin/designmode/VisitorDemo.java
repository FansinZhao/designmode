package com.fansin.designmode;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 17 -12-25 下午11:23
 */
public class VisitorDemo {
    /**
     * The entry point of application.
     *
     * @param args the input arguments

    访问者模式:
    将数据结构和操作分离,在不改变数据结构的情况下,为其添加操作
    应用场景:
    1 多种数据结构的集合
    2 每种数据结构的操作不同
    业务场景:
    1 对老业务系统数据分析.
    2 业务系统有三种基本数据结构,银联数据,贷款行数据,机构数据,通过访问者模式来实现笔数/金额统计

    抽象visitor 定义对每个元素类型的处理接口
    多个具体visitor 实现每个元素类型的具体处理,比如日期,金额
    抽象元素Element(组件元素) 基本元素类型
    叶子元素
    容器组件元素

     */
    public static void main(String[] args) {
        CreditPartner creditEntry = new CreditPartner("t0授信平台");
        creditEntry.addEntry(new UnionpayEntry(100L,2));
        creditEntry.addEntry(new UnionpayEntry(300L,1));
        creditEntry.addEntry(new AcquirerEntry(520L,1));
        creditEntry.addEntry(new LoadBankEntry(600L,3));

        NumberStatistics numberStatistics = new NumberStatistics();
        creditEntry.accept(numberStatistics);
        AmtStatistics amtStatistics = new AmtStatistics();
        creditEntry.accept(amtStatistics);

        System.out.println("UNIONPAY 所有笔数:"+numberStatistics.getTotal(NumberStatistics.UNIONPAY));
        System.out.println("ACQUIRER 所有笔数:"+numberStatistics.getTotal(NumberStatistics.ACQUIRER));
        System.out.println("LOANBANK 所有笔数:"+numberStatistics.getTotal(NumberStatistics.LOANBANK));


        System.out.println("UNIONPAY 所有金额:"+amtStatistics.getTotal(NumberStatistics.UNIONPAY));
        System.out.println("ACQUIRER 所有金额:"+amtStatistics.getTotal(NumberStatistics.ACQUIRER));
        System.out.println("LOANBANK 所有金额:"+amtStatistics.getTotal(NumberStatistics.LOANBANK));

    }
}

/**
 * The type Abstract.
 * visitor
 */
abstract class Abstract {
    /**
     * Visit.
     *
     * @param entry the entry
     */
    abstract void visit(UnionpayEntry entry);

    /**
     * Visit.
     *
     * @param entry the entry
     */
    abstract void visit(AcquirerEntry entry);

    /**
     * Visit.
     *
     * @param entry the entry
     */
    abstract void visit(LoadBankEntry entry);
//    abstract void visit(BaseEntry entry);
}

/**
 * The type Number statistics.
 * 统计交易笔数
 */

class NumberStatistics extends Abstract {

    private ConcurrentHashMap<String,Integer> buf = new ConcurrentHashMap<>(10) ;
    /**
     * The constant UNIONPAY.
     */
    public static String UNIONPAY = "UNIONPAY";
    /**
     * The constant ACQUIRER.
     */
    public static String ACQUIRER = "ACQUIRER";
    /**
     * The constant LOANBANK.
     */
    public static String LOANBANK = "LOANBANK";


    /**
     * Instantiates a new Number statistics.
     */
    public NumberStatistics() {
        //init
        buf.put(UNIONPAY,0);
        buf.put(ACQUIRER,0);
        buf.put(LOANBANK,0);
    }

    @Override
    void visit(UnionpayEntry entry) {
        buf.replace(UNIONPAY,buf.get(UNIONPAY),buf.get(UNIONPAY)+entry.getTrans());
    }

    @Override
    void visit(AcquirerEntry entry) {
        buf.replace(ACQUIRER,buf.get(ACQUIRER),buf.get(ACQUIRER)+entry.getTrans());
    }

    @Override
    void visit(LoadBankEntry entry) {
        buf.replace(LOANBANK,buf.get(LOANBANK),buf.get(LOANBANK)+entry.getTrans());
    }

    /**
     * Get total int.
     *
     * @param type the type
     * @return the int
     */
    public int getTotal(String type){
        return buf.getOrDefault(type,0);
    }
}

/**
 * The type Amt statistics.
 * 统计交易笔数
 */
class AmtStatistics extends Abstract {

    private ConcurrentHashMap<String,Long> buf = new ConcurrentHashMap<>(10) ;
    /**
     * The constant UNIONPAY.
     */
    public static String UNIONPAY = "UNIONPAY";
    /**
     * The constant ACQUIRER.
     */
    public static String ACQUIRER = "ACQUIRER";
    /**
     * The constant LOANBANK.
     */
    public static String LOANBANK = "LOANBANK";


    /**
     * Instantiates a new Amt statistics.
     */
    public AmtStatistics() {
        //init
        buf.put(UNIONPAY,0L);
        buf.put(ACQUIRER,0L);
        buf.put(LOANBANK,0L);
    }

    @Override
    void visit(UnionpayEntry entry) {
        buf.replace(UNIONPAY,buf.get(UNIONPAY),buf.get(UNIONPAY)+entry.getAmt());
    }

    @Override
    void visit(AcquirerEntry entry) {
        buf.replace(ACQUIRER,buf.get(ACQUIRER),buf.get(ACQUIRER)+entry.getAmt());
    }

    @Override
    void visit(LoadBankEntry entry) {
        buf.replace(LOANBANK,buf.get(LOANBANK),buf.get(LOANBANK)+entry.getAmt());
    }

    /**
     * Get total long.
     *
     * @param type the type
     * @return the long
     */
    public Long getTotal(String type){
        return buf.getOrDefault(type, 0L);
    }
}

/**
 * The type Base entry.
 *
 * @param <T> the type parameter
 */
//简化数据结构
@Data
abstract class BaseEntry<T>{

    /**
     * The Amt.
     */
    protected Long amt;
    /**
     * The Trans.
     */
    protected Integer trans;

    /**
     * Instantiates a new Base entry.
     */
    public BaseEntry() {
    }

    /**
     * Instantiates a new Base entry.
     *
     * @param amt   the amt
     * @param trans the trans
     */
    public BaseEntry(long amt, int trans) {
        this.amt = amt;
        this.trans =trans;
    }

    /**
     * Accept.
     *
     * @param statistics the statistics
     */
    abstract void accept(Abstract statistics);

    /**
     * Add entry.
     *
     * @param entry the entry
     */
    public void addEntry(BaseEntry<T> entry){}
}

/**
 * The type Credit partner.
 */
class  CreditPartner extends  BaseEntry{

    private CopyOnWriteArrayList<BaseEntry> list =new CopyOnWriteArrayList<>();

    private String name;

    /**
     * Instantiates a new Credit partner.
     *
     * @param name the name
     */
    public CreditPartner(String name) {

        this.name = name;
    }

    @Override
    public void addEntry(BaseEntry entry){
        list.add(entry);
    }


    @Override
    void accept(Abstract statistics) {
        for (BaseEntry entry : list) {
            entry.accept(statistics);
        }

    }
}

/**
 * The type Load bank entry.
 */
@EqualsAndHashCode(callSuper = true)
@Data
class LoadBankEntry extends BaseEntry{

    /**
     * Instantiates a new Load bank entry.
     *
     * @param amt   the amt
     * @param trans the trans
     */
    public LoadBankEntry(long amt,int trans) {
        super(amt,trans);
    }

    @Override
    void accept(Abstract statistics) {
        //双重分派
        statistics.visit(this);

    }
}

/**
 * The type Acquirer entry.
 */
@Data
class AcquirerEntry extends BaseEntry{


    /**
     * Instantiates a new Acquirer entry.
     *
     * @param amt   the amt
     * @param trans the trans
     */
    public AcquirerEntry(long amt,int trans) {
        super(amt,trans);
    }

    @Override
    void accept(Abstract statistics) {
        //双重分派
        statistics.visit(this);

    }
}

/**
 * The type Unionpay entry.
 */
@Data
class UnionpayEntry extends BaseEntry{


    /**
     * Instantiates a new Unionpay entry.
     *
     * @param amt   the amt
     * @param trans the trans
     */
    public UnionpayEntry(long amt,int trans) {
        super(amt,trans);
    }

    @Override
    void accept(Abstract statistics) {
        //双重分派
        statistics.visit(this);

    }
}
