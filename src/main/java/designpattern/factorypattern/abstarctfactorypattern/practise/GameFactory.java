package designpattern.factorypattern.abstarctfactorypattern.practise;

public interface GameFactory {

    InterfaceController createInterface();
    OperationController createOpreation();

}
