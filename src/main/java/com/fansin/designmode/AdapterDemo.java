package com.fansin.designmode;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 17 -12-25 下午11:23
 */
public class AdapterDemo {
    /**
     * The entry point of application.
     *
     *
     适配器模式:
     实现两个对象的转换,被转换对象可以是类或接口.
     1当src是类时,使用对象关联,避免继承. 偏重数据
     2当src是接口时,使用接口实现模式. 偏重结果

     业务场景:在模块开发过程中,难免会出现模块交互,这时候用到适配器模式
     比如:报文解析服务提供的报文中并不包含一些金额计算的值,而授信服务需要这些信息
     *
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        System.out.println("对象关联适配器适合数据转换");
        Message message = new Message();
        message.setIncomeAmt(0);
        message.setOutcomeAmt(100);
        message.setMerchantName("商户a");
        message.setAcquirerName("机构a");
        MessageAdapter adapter = new MessageAdapter(message);
        System.out.println("适配结果:"+adapter.getAuthAmt());

        System.out.println("接口适配模式:适合功能转换");
        ActionWebServiceAdapterImpl actionWebServiceAdapter = new ActionWebServiceAdapterImpl();
        actionWebServiceAdapter.validateUser("Fansin","123456");

    }
}


/**
 * The type Message adapter.
 */
class MessageAdapter{
    private Message message;

    /**
     * Instantiates a new Message adapter.
     *
     * @param message the message
     */
    public MessageAdapter(Message message) {
        this.message = message;
    }

    /**
     * Get auth amt long.
     *获取可提现金额
     * @return the long
     */
    public long getAuthAmt(){
        return (long) ((message.getOutcomeAmt() -message.getIncomeAmt())*0.5);
    }

}

/**
 * The type Message.
 */
@Data
class Message{
    private String merchantName;
    private String acquirerName;
    private long incomeAmt;
    private long outcomeAmt;
}

/**
 * The interface Web service.
 */
interface IWebService{
    /**
     * 登录
     *
     * @param user     the user
     * @param password the password
     */
    void login(String user, String password);

    /**
     * 登出
     *
     * @param user the user
     */
    void logout(String user);
}

/**
 * The type Simple web service.
 */
class SimpleWebServiceImpl implements IWebService{

    @Override
    public void login(String user, String password) {
        System.out.println("登录成功!"+user+" "+ password);
    }

    @Override
    public void logout(String user) {
        System.out.println(user+" 登出成功!");
    }
}

/**
 * The type Web service adapter.
 */
class WebServiceAdapterImpl implements IWebService{

    @Override
    public void login(String user, String password) {
        System.out.println("适配器:"+user+" "+ password);
    }

    @Override
    public void logout(String user) {
        System.out.println("适配器:"+user+" 登出成功!");
    }
}

/**
 * The type Action web service adapter.
 */
class ActionWebServiceAdapterImpl extends WebServiceAdapterImpl {

    /**
     * Validate user.
     *
     * @param user     the user
     * @param password the password
     */
    public void validateUser(String user, String password){
        System.out.println("新增适配器方法,可以在适配器中适配需要的数据");
        login(user,password);
        logout(user);
    }

}