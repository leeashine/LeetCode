package designpattern.factorypattern.abstarctfactorypattern.practise;

public class AndroidGameFactory implements GameFactory{
    @Override
    public InterfaceController createInterface() {
        return new AndroidInterfaceController();
    }

    @Override
    public OperationController createOpreation() {
        return new AndroidOperationController();
    }
}
