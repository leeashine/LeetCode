package designpattern.flyweightpattern;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

//围棋棋子工厂类：享元工厂类，使用单例模式进行设计
public class IgoChessmanFactory {
    private static IgoChessmanFactory instance = new IgoChessmanFactory();
    private static ConcurrentHashMap ht; //使用ConcurrentHashMap来存储享元对象，充当享元池
    private IgoChessmanFactory() {
        ht = new ConcurrentHashMap();
        IgoChessman black,white;
        black = new BlackIgoChessman();
        ht.put("b",black);
        white = new WhiteIgoChessman();
        ht.put("w",white);
    }
    //返回享元工厂类的唯一实例
    public static IgoChessmanFactory getInstance() {
        return instance;
    }
    //通过key来获取存储在Hashtable中的享元对象
    public static IgoChessman getIgoChessman(String color) {
        return (IgoChessman)ht.get(color);
    }

}
