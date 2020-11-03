package work;

/**
 * Created by LIZIXUAN560 on 2020/11/3.
 *
 * @author LIZIXUAN560
 */
public class Test {

    public static void main(String[] args) {

        User user=new User("tom",10);
        Test test = new Test();
//        test.refer(user);
//        System.out.println(user.getName());

        User user2 = test.refer2(user);
        System.out.println(user.getName());
        System.out.println(user2.getName());


    }

    //测试 引用，传一个参数，最后这个参数会不会改变
    public int refer(User user){

        user.setName("leee");
        user.setAge(18);
        return 0;

    }

    //测试 引用2
    public User refer2(User user){
        return new User("leee2",user.getAge());
    }

}
