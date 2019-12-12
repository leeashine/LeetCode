package designpattern.decoratorpattern;
//��ɫ�߿�װ���ࣺ����װ����
public class BlackBorderDecorator extends ComponentDecorator{
    public BlackBorderDecorator(Component component)
    {
        super(component);
    }
    public void display()
    {
        this.setBlackBorder();
        super.display();
    }
    public void setBlackBorder()
    {
        System.out.println("Ϊ�������Ӻ�ɫ�߿�");
    }

}
