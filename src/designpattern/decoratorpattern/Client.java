package designpattern.decoratorpattern;

//�����Ҫ��ԭ��ϵͳ������һ���µľ��幹��������µľ���װ���࣬�����޸���������
//�룬ֻ�轫���Ƿֱ���Ϊ���󹹼�����߳���װ��������༴�ɡ���ͼ12-2��ʾ�ļ̳нṹ��
//�ȣ�ʹ��װ��ģʽ֮�󽫴�����������ĸ�������ϵͳ��չ�������ӷ��㣬���Ҹ�����ά
//������ȡ���̳и��õ���Ч��ʽ֮һ��
public class Client {

    public static void main(String args[])
    {
//        Component component,componentSB; //ʹ�ó��󹹼�����
//        component = new Window(); //������幹��
//        componentSB = new ScrollBarDecorator(component); //����װ�κ�Ĺ���
//        componentSB.display();

//        �������
//        ϣ���õ�һ�����й��������к�ɫ�߿�Ĵ��壬����Ҫ��ԭ���������κ��޸ģ�ֻ�轫��
//        ���˴����޸�Ϊ������ʾ��
        Component component,componentSB,componentBB; //ȫ��ʹ�ó��󹹼�����
        component = new Window();
        componentSB = new ScrollBarDecorator(component);
        componentBB = new BlackBorderDecorator(componentSB); //��װ����һ��֮��Ķ������ע�뵽��һ��װ�����У����еڶ���װ��
        componentBB.display();


    }


}
