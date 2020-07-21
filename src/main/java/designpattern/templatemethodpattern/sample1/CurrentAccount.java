package designpattern.templatemethodpattern.sample1;

import java.io.Console;

public class CurrentAccount extends Account{
    @Override
    public void CalculateInterest() {
        //覆盖父类的抽象基本方法
        System.out.println("按活期利率计算利息！");
    }
}
