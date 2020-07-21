package designpattern.templatemethodpattern.sample2;

public class XMLDataViewer extends DataViewer{

    Chart chart;

    public XMLDataViewer(Chart chart) {
        this.chart = chart;
    }

    //实现父类方法：获取数据
    @Override
    public void GetData() {
        System.out.println("从XML文件中获取数据。");
    }

    //实现父类方法：显示数据，默认以柱状图方式显示，可结合桥接模式来改进
    @Override
    public void DisplayData() {
        chart.display();
    }

    //覆盖父类的钩子方法
    @Override
    public boolean IsNotXMLData() {
        return false;
    }

}
