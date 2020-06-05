package designpattern.bridgepattern.practise;

public class OracleDataBaseImp implements DataBase{
    @Override
    public void connect() {
        System.out.println("使用Oracle数据库");
    }
}
