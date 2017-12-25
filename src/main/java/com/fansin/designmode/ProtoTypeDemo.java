package com.fansin.designmode;

import com.xiaoleilu.hutool.util.CollectionUtil;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 17-12-25 下午11:23
 */
public class ProtoTypeDemo {

    /**
     * 原型模型
     *  实现cloneable接口(接口本身并没有clone方法,RandomAccess接口类似)
     *  应用场景:
     *  浅克隆-针对基本类型其封装类或final类型对象,大部分集合类实现cloneable,但是必须要实例调用clone
     *  深度克隆-针对非基本类型对象类型克隆
     * */
    public static void main(String[] args) throws CloneNotSupportedException, InterruptedException {
        ArrayList list = CollectionUtil.newArrayList("aaa","bb");
        System.out.println("list = " + list);
        ArrayList cp = (ArrayList) list.clone();
        cp.add("ccc");
        System.out.println("cp = " + cp);


        ConcreteProtoType concreteProtoType = new ConcreteProtoType();
        concreteProtoType.setI(2);
        concreteProtoType.setL(20L);
        concreteProtoType.setDate(new Date());
        concreteProtoType.setPojo(new Pojo("aaa",22));
        concreteProtoType.setList(CollectionUtil.newArrayList(new Pojo("bbb",66)));
        concreteProtoType.setStringList(CollectionUtil.newArrayList("123456"));
        concreteProtoType.setName("不会被克隆");
        concreteProtoType.setStringBuffer(new StringBuffer("hahahah"));
        ConcreteProtoType copy = (ConcreteProtoType) concreteProtoType.clone();
        System.out.println("同一个对象么?"+(concreteProtoType == copy));
        System.out.println("concreteProtoType = " + concreteProtoType);
        System.out.println("copy = " + copy);
        copy.setName("修改对象内容");
        Thread.sleep(1000L);
        copy.setDate(new Date());
        //StringBuffer也属于浅克隆
        copy.getStringBuffer().append("lalalalal");
        //这个属于浅克隆
        copy.getList().add(new Pojo("dd",99));
        //这个属于浅克隆
        copy.getStringList().add("999999");
        System.out.println(" -------修改copy后----------");
        System.out.println("concreteProtoType = " + concreteProtoType);
        System.out.println("copy              = " + copy);
        System.out.println("concreteProtoType.getName().hashCode() = " + concreteProtoType.getName().hashCode());
        System.out.println("copy.getName().hashCode() = " + copy.getName().hashCode());

    }


}
class ProtoType implements Cloneable{

    @Override
    protected ProtoType clone() throws CloneNotSupportedException {
        return (ProtoType)super.clone();
    }
}

@Getter
@Setter
@ToString
class ConcreteProtoType extends ProtoType{
    private String name;

    public ConcreteProtoType() {
        System.out.println("ConcreteProtoType.ConcreteProtoType");
    }

    private int i;
    private Long l;
    private ArrayList<Pojo> list;
    /**list 实现了cloneable*/
    private ArrayList<String> stringList;
    private Date date;
    private Pojo pojo;
    private StringBuffer stringBuffer;
}

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
class Pojo{
    private String name;
    private int age;

}