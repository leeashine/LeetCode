package singletonpackage;

/**
 * ����ģʽ
 * �̰߳�ȫ���Ƚϳ��ã������ײ�����������Ϊһ��ʼ�ͳ�ʼ��
 * �ڸ��ڲ����ж�����һ��static���͵ı���
 * instance����ʱ�����ȳ�ʼ�������Ա��������Java���������֤���̰߳�ȫ�ԣ�ȷ���ó�Ա
 * ����ֻ�ܳ�ʼ��һ�Ρ�
 */
public class Singleton {
    private Singleton(){};
    private static Singleton singleton=new Singleton();
    public static Singleton getSingleton(){
        return singleton;
    }
}
