package aleetcode;

import java.util.ArrayList;
import java.util.List;

public class HeapTest {
    byte [] b=new byte[1024*100];//100KB
    public static void main(String[] args) throws InterruptedException {

        List list=new ArrayList();
        while (true){
            list.add(new HeapTest());
            Thread.sleep(10);
        }


    }
}
