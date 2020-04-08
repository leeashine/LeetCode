package designpattern.factorypattern.abstarctfactorypattern.practise;

public class AndroidOperationController implements OperationController{
    @Override
    public void say() {
        System.out.println("Android的OperationController执行say");
    }

    @Override
    public void dance() {
        System.out.println("Android的OperationController执行dance");
    }

    @Override
    public void rap() {
        System.out.println("Android的OperationController执行rap");
    }
}
