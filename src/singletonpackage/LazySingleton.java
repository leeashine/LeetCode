package singletonpackage;

/**
 * ����ģʽ �̲߳���ȫ
 * ˫�ؼ������ ע�� volatile�ؼ���
 * ����volatile�ؼ��ֻ�����Java�����������һ
 * Щ�����Ż������ܻᵼ��ϵͳ����Ч�ʽ��ͣ���˼�ʹʹ��˫�ؼ��������ʵ�ֵ���ģʽҲ
 * ����һ��������ʵ�ַ�ʽ��
 */
public class LazySingleton {
    private volatile static LazySingleton instance = null;

    private LazySingleton() {
    }

    public static LazySingleton getInstance() {
        //��һ���ж�
        if (instance == null) {
            //���������
            synchronized (LazySingleton.class) {
                //�ڶ����ж�
                if (instance == null) {
                    instance = new LazySingleton(); //��������ʵ��
                }
            }
        }
        return instance;
    }

//    private LazySingleton() {
//        System.out.println("LazySingleton is create");
//    }
//
//    private static LazySingleton instance = null;
//
//    public static synchronized LazySingleton getInstance() {
//        if (instance == null) {
//            instance = new LazySingleton();
//        }
//        return instance;
//    }
}
