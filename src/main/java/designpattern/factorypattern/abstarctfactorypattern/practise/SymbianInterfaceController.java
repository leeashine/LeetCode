package designpattern.factorypattern.abstarctfactorypattern.practise;

public class SymbianInterfaceController implements InterfaceController{
    @Override
    public void select() {
        System.out.println("Symbian的InterfaceController的select");
    }

    @Override
    public void cancel() {
        System.out.println("Symbian的InterfaceController的cancel");
    }

    @Override
    public void click() {
        System.out.println("Symbian的InterfaceController的click");
    }
}
