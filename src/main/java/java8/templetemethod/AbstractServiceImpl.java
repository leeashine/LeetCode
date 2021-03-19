package java8.templetemethod;

import java.io.Serializable;
import java.util.function.Supplier;

public abstract class AbstractServiceImpl {

    protected <T extends Serializable> T gwWrapper(Supplier<T> supplier,String name,T defaultValue){

        try {

            //操作
            System.out.println("权限校验......");
            System.out.println("封装参数......");
            if(true==true){
                return defaultValue;
            }else {
                return supplier.get();
            }
        }catch (Exception e){
            System.out.println("error");
            return defaultValue;
        }


    }

}
