package designpattern.statuspatern.sample;

//银行账户 环境类
public class Account {

    private AccountState state;//维持一个对抽象状态对象的引用
    private String owner;//开户名
    private double balance=0;//账户余额

    public Account(String owner,double balance){

        this.owner=owner;
        this.balance=balance;
        this.state=new NormalState(this);//设置初始状态
        System.out.println(this.owner + "开户，初始金额为" + balance);
        System.out.println("-----------------------------------------");

    }

    public void deposit(double amount){
        System.out.println(this.owner + "存款" + amount);
        state.deposit(amount); //调用状态对象的deposit()方法
        System.out.println("现在余额为"+ this.balance);
        System.out.println("现在帐户状态为"+ this.state.getClass().getName());
        System.out.println("---------------------------------------------");
    }
    public void withdraw(double amount) {
        System.out.println(this.owner + "取款" + amount);
        state.withdraw(amount); //调用状态对象的withdraw()方法
        System.out.println("现在余额为"+ this.balance);
        System.out.println("现在帐户状态为"+ this. state.getClass().getName());
        System.out.println("---------------------------------------------");
    }


    public AccountState getState() {
        return state;
    }

    public void setState(AccountState state) {
        this.state = state;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void computeInterest() {
        System.out.println("计算利息！");
    }
}
