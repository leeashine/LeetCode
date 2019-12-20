package designpattern.decoratorpattern;

//如果需要在原有系统中增加一个新的具体构件类或者新的具体装饰类，无须修改现有类库代
//码，只需将它们分别作为抽象构件类或者抽象装饰类的子类即可。与图12-2所示的继承结构相
//比，使用装饰模式之后将大大减少了子类的个数，让系统扩展起来更加方便，而且更容易维
//护，是取代继承复用的有效方式之一。
public class Client {

    public static void main(String args[])
    {
//        Component component,componentSB; //使用抽象构件定义
//        component = new Window(); //定义具体构件
//        componentSB = new ScrollBarDecorator(component); //定义装饰后的构件
//        componentSB.display();

//        如果我们
//        希望得到一个既有滚动条又有黑色边框的窗体，不需要对原有类库进行任何修改，只需将客
//        户端代码修改为如下所示：
        Component component,componentSB,componentBB; //全部使用抽象构件定义
        component = new Window();
        componentSB = new ScrollBarDecorator(component);
        componentBB = new BlackBorderDecorator(componentSB); //将装饰了一次之后的对象继续注入到另一个装饰类中，进行第二次装饰
        componentBB.display();


    }


}
