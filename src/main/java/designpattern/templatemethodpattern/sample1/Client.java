package designpattern.templatemethodpattern.sample1;

public class Client {
    public static void main(String[] args) {
        Account account;
        account=new CurrentAccount();
        account.Handle("张无忌","123456");
    }
}
