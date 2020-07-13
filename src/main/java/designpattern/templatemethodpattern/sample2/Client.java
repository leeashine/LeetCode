package designpattern.templatemethodpattern.sample2;

import java.io.Console;

public class Client {
    public static void main(String[] args) {
        DataViewer dv;
        Chart chart=new ZhuzhuangChart();
        dv = new XMLDataViewer(chart);
        dv.Process();
    }
}
