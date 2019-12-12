package designpattern.decoratorpattern;
//����װ���ࣺ����װ����
public class ComponentDecorator extends  Component{

    private Component component; //ά�ֶԳ��󹹼����Ͷ��������
    public ComponentDecorator(Component component) //ע����󹹼����͵Ķ���
    {
        this.component = component;
    }

    @Override
    public void display() {
        component.display();
    }
}
