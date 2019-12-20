package singletonpackage;

/**
 * ��̬�ڲ��൥��ģʽ
 * ֻ�е�һ�ε���getInstance����ʱ��������ż��� Inner ����ʼ��instance ��
 * ֻ��һ���߳̿��Ի�ö���ĳ�ʼ�����������߳��޷����г�ʼ������֤�����Ψһ�ԡ�
 * Ŀǰ�˷�ʽ�����е���ģʽ�����Ƽ���ģʽ�������廹�Ǹ�����Ŀѡ��
 *
 */
public class StaticSingleton {

    private StaticSingleton(){};

    private static class inner{
        private static StaticSingleton singleton=new StaticSingleton();
    }

    public static StaticSingleton getInstance(){
        return inner.singleton;
    }

}
