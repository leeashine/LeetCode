package designpattern.proxypattern.ProxySample;

//�����֤
public class AccessValidator {
    //ģ��ʵ�ֵ�¼��֤
    public boolean Validate(String userId)
    {
        System.out.println("�����ݿ�����֤�û�'" + userId + "'�Ƿ��ǺϷ��û���");
        if (userId.equals("���")) {
            System.out.println(userId+"��¼�ɹ���");
            return true;
        }
        else {
            System.out.println(userId+"��¼ʧ�ܣ�");
            return false;
        }
    }

}
