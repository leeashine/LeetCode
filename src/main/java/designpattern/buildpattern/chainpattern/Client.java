package designpattern.buildpattern.chainpattern;

public class Client {
    public static void main(String[] args) {
        //链式编程
        StudentBean studentBean = StudentBean.builder().name("ly").age(11).build();
    }
}
