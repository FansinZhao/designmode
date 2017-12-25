package com.fansin.designmode;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 17 -12-25 下午11:23
 */
public class MementoDemo {
    /*
    备忘模式:
    在不破坏对象内部封装性的情况下,将对象的内部状态保存在外部.并在以后可能需要的时候对其恢复.
    应用场景:
    1 对象备份恢复 入归档
    2 持久化保存 序列化
    业务场景:
    涉及网络,就会有重发,使用备忘录模式实现重发

     */

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        MessageOriginator messageOriginator = new MessageOriginator("fansin","123456789", 10000L);
        MessageCareTaker messageCareTaker = new MessageCareTaker();
        messageCareTaker.setMemento(messageOriginator.createMemento());
        System.out.println("经过一次请求,多个参数发生");
        messageOriginator.setAmt(0L);
        //恢复备份
        messageOriginator.restoreMemento(messageCareTaker.getMemento());
        System.out.println("恢复后");
        System.out.println(messageOriginator.getAmt());
    }
}

/**
 * The type Abstract originator.
 */
abstract class AbstractOriginator implements Cloneable{

}

/**
 * The type Message originator.
 */
//组织者 需要备份的对象
@Data
@EqualsAndHashCode(callSuper = false)
class MessageOriginator extends AbstractOriginator {
    private String countName;
    private String countNumber;
    private Long amt;

    /**
     * Instantiates a new Message originator.
     *
     * @param countName   the count name
     * @param countNumber the count number
     * @param amt         the amt
     */
    public MessageOriginator(String countName, String countNumber, Long amt) {
        this.countName = countName;
        this.countNumber = countNumber;
        this.amt = amt;
    }

    /**
     * Instantiates a new Message originator.
     */
    public MessageOriginator() {
    }

    /**
     * Create memento memento.
     *
     * @return the memento
     */
    public Memento createMemento(){
        return new MessageMemento(this);
    }

    /**
     * Restore memento.
     *
     * @param memento the memento
     */
    public void  restoreMemento(Memento memento){
        MessageOriginator messageOriginator = (MessageOriginator)memento.getOriginator();
        setAmt(messageOriginator.getAmt());
        setCountName(messageOriginator.getCountName());
        setCountNumber(messageOriginator.getCountNumber());
    }


    @Override
    protected AbstractOriginator clone() throws CloneNotSupportedException {

        //TODO 本类中没有集合类对象,不需要深度复制
        return (AbstractOriginator)super.clone();
    }
}

/**
 * The interface Memento.
 */
interface Memento{
    /**
     * Sets originator.
     *
     * @param originator the originator
     */
    void setOriginator(AbstractOriginator originator);

    /**
     * Gets originator.
     *
     * @return the originator
     */
    AbstractOriginator getOriginator();
}

/**
 * The type Message memento.
 */
class MessageMemento implements Memento{

    private AbstractOriginator originator;

    /**
     * Instantiates a new Message memento.
     *
     * @param originator the originator
     */
    public MessageMemento(MessageOriginator originator) {
        try {
            this.originator = originator.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOriginator(AbstractOriginator originator) {
        this.originator = originator;
    }

    @Override
    public AbstractOriginator getOriginator() {
        return originator;
    }
}

/**
 * The type Message care taker.
 */
class MessageCareTaker{

    private Memento memento;

    /**
     * Gets memento.
     *
     * @return the memento
     */
    public Memento getMemento() {
        return memento;
    }

    /**
     * Sets memento.
     *
     * @param memento the memento
     */
    public void setMemento(Memento memento) {
        this.memento = memento;
    }
}

