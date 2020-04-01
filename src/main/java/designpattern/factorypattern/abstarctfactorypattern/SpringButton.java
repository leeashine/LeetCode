package designpattern.factorypattern.abstarctfactorypattern;

public class SpringButton implements Button{
    @Override
    public void display() {
        System.out.println("显示浅绿色按钮。");
    }
}
