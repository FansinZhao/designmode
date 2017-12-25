package com.fansin.designmode;

import com.xiaoleilu.hutool.aop.ProxyUtil;
import com.xiaoleilu.hutool.aop.aspects.TimeIntervalAspect;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.date.TimeInterval;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 17 -12-25 下午11:23
 */
public class ProxyDemo {
    /**
     * 代理模式
     * 静态代理模式:编译阶段的代理
     * 动态代理模式:运行时阶段的代理
     * <p>
     * 应用场景:
     * 对代码中任意的方法进行监控
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        System.out.println("------------静态代理的弊端就是代理类与目标类的绑定,导致类膨胀------------");
        ILogin real = new UserLogin();
        ILogin proxy = new StaticProxy(real);
        proxy.login("zhaofeng","123456");
        proxy.logout();
        System.out.println("-----------动态代理有两种:jdk/cglib--------------");
        System.out.println("-----jdk 要求目标类必须是接口实现类,利用反射生成的class是继承Proxy----------------");
        ILogin proxyClazz = (ILogin) Proxy.newProxyInstance(ILogin.class.getClassLoader(),new Class[]{ILogin.class},new JdkDynamicHandler(real));
        proxyClazz.login("Fansin","123456");
        proxyClazz.logout();
        System.out.println("---------------hutool工具类 本质还是jdk动态代理-------------------");
        ILogin htProxy = ProxyUtil.proxy(real, TimeIntervalAspect.class);
        htProxy.login("hutool","123456");
        htProxy.logout();
        System.out.println("-----------------cglib 借助帮助工具Enhancer----------------------");
        UserLogin userLogin = new CglibDynamicHandler<UserLogin>().newInstance(UserLogin.class);
        userLogin.login("cglib","123456");
        userLogin.logout();
    }
}

/**
 * The type Cglib dynamic handler.
 *
 * @param <T> the type parameter
 */
class CglibDynamicHandler<T> implements MethodInterceptor{
    private Enhancer enhancer = new Enhancer();

    /**
     * New instance t.
     *
     * @param clazz the clazz
     * @return the t
     */
    public T newInstance(Class<T> clazz) {
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return (T)enhancer.create();
    }


    /**
     * 
     * @param obj 目标对象
     * @param method jdk拦截方法
     * @param args 参数
     * @param proxy cglib快速拦截代理
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        TimeInterval timeInterval = DateUtil.timer();
        Object result;
        result = proxy.invokeSuper(obj,args);
        System.out.println("timeInterval.intervalSecond() = " + timeInterval.intervalSecond());
        System.out.println("result = " + result);
        return result;
    }
}


/**
 * The type Jdk dynamic handler.
 */
class JdkDynamicHandler implements InvocationHandler{

    private Object object;

    /**
     * Instantiates a new Jdk dynamic handler.
     *
     * @param object the object
     */
    public JdkDynamicHandler(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TimeInterval timeInterval = DateUtil.timer();
        Object result  = method.invoke(object,args);
        System.out.println("timeInterval.intervalSecond() = " + timeInterval.intervalSecond());
        System.out.println("result = " + result);
        return result;
    }
}


/**
 * The interface Login.
 */
interface ILogin{
    /**
     * Login.
     *
     * @param username the username
     * @param password the password
     */
    void login(String username, String password);

    /**
     * Logout.
     */
    void logout();
}


/**
 * The type User login.
 */
class UserLogin implements ILogin{
    @Override
    public void login(String username, String password) {
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("登入");
    }

    @Override
    public void logout() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("登出");
    }
}

/**
 * 实现接口是为了方便替换对象
 */
class StaticProxy  implements ILogin{
    private ILogin target;

    /**
     * Instantiates a new Static proxy.
     *
     * @param target the target
     */
    public StaticProxy(ILogin target) {
        this.target = target;
    }


    @Override
    public void login(String username, String password) {
        TimeInterval timeInterval = DateUtil.timer();
        target.login(username,password);
        System.out.println("timeInterval = " + timeInterval.intervalSecond());
    }

    @Override
    public void logout() {
        TimeInterval timeInterval = DateUtil.timer();
        target.logout();
        System.out.println("timeInterval = " + timeInterval.intervalSecond());

    }
}
