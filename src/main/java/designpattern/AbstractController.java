package designpattern;

import java.util.function.Supplier;

public abstract class AbstractController {

    public ResultDTO wrapper(String resource, Supplier supplier) {

        ResultDTO resultDTO = new ResultDTO();
        try {

            supplier.get();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return resultDTO;
        }
    }

}
