package chapter5.parallel_compute;

/**
 * Created by 13 on 2017/5/9.
 */
public class PStreamMain {
    public static void main(String args[]) {

        new Thread(new Plus()).start();
        new Thread(new Multiply()).start();
        new Thread(new Div()).start();


        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 10; j++) {
                Msg msg = new Msg();
                msg.i = i;
                msg.j = j;
                msg.orgStr = "((" + i + "+" + j + ")*" + i + ")/2";
                Plus.blockingDeque.add(msg);
            }
        }
    }
}
