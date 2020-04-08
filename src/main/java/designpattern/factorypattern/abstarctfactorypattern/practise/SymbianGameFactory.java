package designpattern.factorypattern.abstarctfactorypattern.practise;

public class SymbianGameFactory implements GameFactory{
    @Override
    public InterfaceController createInterface() {
        return new SymbianInterfaceController();
    }

    @Override
    public OperationController createOpreation() {
        return new SymbianOperationController();
    }
}
