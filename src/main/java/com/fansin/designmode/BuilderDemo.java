package com.fansin.designmode;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 17 -12-25 下午11:23
 */
public class BuilderDemo {

    /**
     * 建造者模式:产品,建造者接口,具体建造者,指挥者
     * 应用场景,根据零部件组装玩具:
     * 产品:玩具:Toy 模型材料+外观+灯光+音乐
     * 建造者接口::ToyCreator 选择材料/外观/灯光/音乐
     * 具体建造者: XiYangYangToyCreator 接口实现
     * 指挥者:ToyManager:流水线
     * <p>
     * -----------门面比较-----------
     * 门面:对接口进行统一
     * 建造者:多部件对象统一创建过程
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        ToyManager manager = new ToyManager(new XiYangYangToyCreator());
        Toy toy = manager.createToy();
        System.out.println("toy = " + toy);
    }
}

/**
 * The type Toy.
 */
@Data
class Toy{
    private String material;
    private String appearance;
    private String light;
    private String music;
}

/**
 * The interface Toy creator.
 */
interface ToyCreator{

    /**
     * Select material.
     */
    void selectMaterial();

    /**
     * Select appearance.
     */
    void selectAppearance();

    /**
     * Select light.
     */
    void selectLight();

    /**
     * Select music.
     */
    void selectMusic();

    /**
     * To toy toy.
     *
     * @return the toy
     */
    Toy toToy();
}

/**
 * The type Xi yang yang toy creator.
 */
class XiYangYangToyCreator implements ToyCreator{

    private Toy toy = new Toy();

    @Override
    public void selectMaterial() {
        toy.setMaterial("塑料");
    }

    @Override
    public void selectAppearance() {
        toy.setAppearance("鸡年外形");
    }

    @Override
    public void selectLight() {
        toy.setLight("五彩");
    }

    @Override
    public void selectMusic() {
        toy.setMusic("过年发大财");
    }

    @Override
    public Toy toToy() {
        return toy;
    }
}

/**
 * The type Toy manager.
 */
@AllArgsConstructor
class ToyManager{
    private ToyCreator creator;

    /**
     * Create toy toy.
     *
     * @return the toy
     */
    public Toy createToy(){
        creator.selectMaterial();
        creator.selectAppearance();
        creator.selectLight();
        creator.selectMusic();
        return creator.toToy();
    }
}