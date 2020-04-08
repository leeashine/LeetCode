package designpattern.factorypattern.abstarctfactorypattern.practise;

public class AndroidInterfaceController implements InterfaceController{
    @Override
    public void select() {
        System.out.println("Android的InterfaceController的select");
    }

    @Override
    public void cancel() {
        System.out.println("Android的InterfaceController的cancel");
    }

    @Override
    public void click() {
        System.out.println("Android的InterfaceController的click");
    }
}
