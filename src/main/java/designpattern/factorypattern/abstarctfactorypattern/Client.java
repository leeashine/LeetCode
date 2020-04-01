package designpattern.factorypattern.abstarctfactorypattern;

//  抽象工厂模式的主要缺点如下：
//  增加新的产品等级结构麻烦，需要对原有系统进行较大的修改，甚至需要修改抽象层代码，
//  这显然会带来较大的不便，违背了“开闭原则”。
public class Client {
    public static void main(String args[]) {
        //使用抽象层定义
        SkinFactory factory;
        Button bt;
        TextField tf;
        ComboBox cb;
        factory = (SkinFactory)XMLUtil.getBean();
        assert factory != null;
        bt = factory.createButton();
        tf = factory.createTextField();
        cb = factory.createComboBox();
        bt.display();
        tf.display();
        cb.display();
    }

}
