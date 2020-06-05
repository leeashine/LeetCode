package designpattern.bridgepattern.practise;

public class MySqlDataBaseImp implements DataBase {
    @Override
    public void connect() {
        System.out.println("使用mysql数据库");
    }
}
