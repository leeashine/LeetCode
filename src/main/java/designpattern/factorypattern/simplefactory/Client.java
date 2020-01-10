package designpattern.factorypattern.simplefactory;

//简单工厂模式(Simple Factory Pattern)：定义一个工厂类，它可以根据参数的不同返回不同类的
//        实例，被创建的实例通常都具有共同的父类。因为在简单工厂模式中用于创建实例的方法是
//        静态(static)方法，因此简单工厂模式又被称为静态工厂方法(Static Factory Method)模式，它属
//        于类创建型模式。
//        简单工厂模式的要点在于：当你需要什么，只需要传入一个正确的参数，就可以获取你所需
//        要的对象，而无须知道其创建细节。
public class Client {

    public static void main(String args[]) {
        Chart chart;
        String type = XMLUtil.getChartType(); //读取配置文件中的参数
        chart = ChartFactory.getChart(type); //创建产品对象
        chart.display();
    }




}
