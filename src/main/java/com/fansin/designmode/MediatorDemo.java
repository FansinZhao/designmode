package com.fansin.designmode;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 17 -12-25 下午11:23
 */
public class MediatorDemo {

    /*
    调停者/中介者:
    所有的对象均与调停者交互,从而形成星型结构.
    应用场景:
    系统内部多个对象,并且多个对象具有交互性.
    jdk中Executor就是典型应用,所有的任务都集中在executor中.
    业务场景:
    机构--提现通知-->贷款行---确认放款-->银联--发送代付请求-->银联机构
    a 贷款行修改都需要通知银联
    b 机构修改通知贷款行
     */

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        WithdrawExecutor executor = new WithdrawExecutor();
        AcquirePlatform acquirePlatform = new AcquirePlatform(executor);
        LoanBankPlatform loanBankPlatform = new LoanBankPlatform(executor);
        UnionPayPlatform unionPayPlatform = new UnionPayPlatform(executor);
        executor.setAcquirePlatform(acquirePlatform);
        executor.setLoanBankPlatform(loanBankPlatform);
        executor.setUnionPayPlatform(unionPayPlatform);

        acquirePlatform.callPlatform();

    }
}


/**
 * The interface Executor.
 */
interface Executor {
    /**
     * Execute.
     *
     * @param platform the platform
     */
    void execute(AbstractPlatform platform);

}

/**
 * The type Withdraw executor.
 */
class WithdrawExecutor implements Executor {

    private UnionPayPlatform unionPayPlatform;
    private AcquirePlatform acquirePlatform;
    private LoanBankPlatform loanBankPlatform;


    /**
     * Sets union pay platform.
     *
     * @param unionPayPlatform the union pay platform
     */
    public void setUnionPayPlatform(UnionPayPlatform unionPayPlatform) {
        this.unionPayPlatform = unionPayPlatform;
    }

    /**
     * Sets acquire platform.
     *
     * @param acquirePlatform the acquire platform
     */
    public void setAcquirePlatform(AcquirePlatform acquirePlatform) {
        this.acquirePlatform = acquirePlatform;
    }

    /**
     * Sets loan bank platform.
     *
     * @param loanBankPlatform the loan bank platform
     */
    public void setLoanBankPlatform(LoanBankPlatform loanBankPlatform) {
        this.loanBankPlatform = loanBankPlatform;
    }

    @Override
    public void execute(AbstractPlatform platform) {
        if(platform instanceof AcquirePlatform){
            loanBankPlatform.callPlatform();
        }else if (platform instanceof LoanBankPlatform){
            unionPayPlatform.callPlatform();
        }else {
            System.out.println("代付请求发往......银联机构");
        }
    }
}


/**
 * The type Abstract platform.
 */
abstract class AbstractPlatform {

    private Executor executor;

    /**
     * Instantiates a new Abstract platform.
     *
     * @param executor the executor
     */
    public AbstractPlatform(Executor executor) {
        this.executor = executor;
    }

    /**
     * Gets executor.
     *
     * @return the executor
     */
    public Executor getExecutor() {
        return executor;
    }

    /**
     * Call platform.
     */
    abstract void callPlatform();
}

/**
 * The type Union pay platform.
 */
class UnionPayPlatform extends AbstractPlatform {

    /**
     * Instantiates a new Union pay platform.
     *
     * @param executor the executor
     */
    public UnionPayPlatform(Executor executor) {
        super(executor);
    }

    @Override
    void callPlatform() {
        System.out.println("银联发往代付机构....");
        getExecutor().execute(this);
    }
}

/**
 * The type Acquire platform.
 */
class AcquirePlatform extends AbstractPlatform {

    /**
     * Instantiates a new Acquire platform.
     *
     * @param executor the executor
     */
    public AcquirePlatform(Executor executor) {
        super(executor);
    }

    @Override
    void callPlatform() {
        System.out.println("机构提现通知....");
        getExecutor().execute(this);
    }
}

/**
 * The type Loan bank platform.
 */
class LoanBankPlatform extends AbstractPlatform {

    /**
     * Instantiates a new Loan bank platform.
     *
     * @param executor the executor
     */
    public LoanBankPlatform(Executor executor) {
        super(executor);
    }

    @Override
    void callPlatform() {
        System.out.println("贷款行确认放款....");
        getExecutor().execute(this);
    }
}

