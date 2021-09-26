package work;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.domain.BatchResultDTO;
import work.domain.PageResultDTO;
import work.domain.ResultDTO;
import work.domain.ResultSupport;
import work.util.UserUtil;

import java.io.Serializable;
import java.util.function.Supplier;

public abstract class AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(AbstractController.class);


    public <T extends Serializable> ResultDTO<T> wrapper(Supplier<ResultDTO<T>> supplier, String name, String operation) {
        return wrapper(supplier, name, operation, new ResultDTO<>());
    }

    public <T extends Serializable> BatchResultDTO<T> batchWrapper(Supplier<BatchResultDTO<T>> supplier, String name, String operation) {
        return wrapper(supplier, name, operation, new BatchResultDTO<>());
    }

    public <T extends Serializable> PageResultDTO<T> pageWrapper(Supplier<PageResultDTO<T>> supplier, String name, String operation) {
        return wrapper(supplier, name, operation, new PageResultDTO<>());
    }

    public <T extends ResultSupport> T wrapper(Supplier<T> supplier, String name, String operation, T defaultValue) {

        ResultDTO<T> resultDTO = new ResultDTO<>();

        //这边要保证变量是线程安全或不可修改的！
        Long userId = UserUtil.getUserId();
        long appId = 100000L;
        try {
            //权限校验
            return supplier.get();

        } catch (Exception e) {
            logger.error("wrapper {}:{} Exception. userId={}, appId={}, e:",
                    name, operation, userId, appId, e);
            return defaultValue;
        }
    }

}
