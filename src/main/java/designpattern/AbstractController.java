package designpattern;

import java.io.Serializable;
import java.util.function.Supplier;

public abstract class AbstractController {

    public <T extends Serializable> ResultDTO<T> wrapper(String resource, Supplier<T> supplier) {

        ResultDTO<T> resultDTO = new ResultDTO<>();
        try {

            supplier.get();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return resultDTO;
        }
    }

}
