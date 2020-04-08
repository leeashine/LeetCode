package designpattern.factorypattern.abstarctfactorypattern.practise;

public class SymbianOperationController implements OperationController{
    @Override
    public void say() {
        System.out.println("Symbian的OperationController执行say");
    }

    @Override
    public void dance() {
        System.out.println("Symbian的OperationController执行dance");
    }

    @Override
    public void rap() {
        System.out.println("Symbian的OperationController执行rap");
    }
}
