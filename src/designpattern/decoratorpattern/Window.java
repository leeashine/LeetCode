package designpattern.decoratorpattern;
//窗体类：具体构件类
public class Window extends Component{
    @Override
    public void display() {
        System.out.println("显示窗体！");
    }
}
