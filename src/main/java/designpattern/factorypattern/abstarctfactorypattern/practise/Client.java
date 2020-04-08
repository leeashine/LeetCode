package designpattern.factorypattern.abstarctfactorypattern.practise;

public class Client {
    public static void main(String[] args) {
        GameFactory factory=new SymbianGameFactory();
        InterfaceController anInterface = factory.createInterface();
        OperationController opreation = factory.createOpreation();
        anInterface.click();
        opreation.dance();
    }
}
