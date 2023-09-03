package java8.lambda;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.domain.FcInvoiceDO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Lee
 */
public class PageProcessHelper {

    private static final Logger logger = LoggerFactory.getLogger(PageProcessHelper.class);

    public static void main(String[] args) {

        PageProcessHelper helper = new PageProcessHelper();
        List<FcInvoiceDO> process = helper.processWithPagination(helper::queryCount, helper::pageQueryFcInvoiceDOList);
        logger.info(JSON.toJSONString(process));

    }


    private List<FcInvoiceDO> pageQueryFcInvoiceDOList() {
        FcInvoiceDO fcInvoiceDO = new FcInvoiceDO();
        fcInvoiceDO.setId(1L);
        return Lists.newArrayList(fcInvoiceDO);
    }

    /**
     * 分页查询数据
     * @param cntSupplier
     * @param supplier
     * @return
     * @param <T>
     */
    public <T> List<T> processWithPagination(Supplier<Integer> cntSupplier, Supplier<Collection<T>> supplier) {
        Objects.requireNonNull(cntSupplier, "cntSupplier must not be null");
        Objects.requireNonNull(supplier, "supplier must not be null");

        Integer cnt = cntSupplier.get();
        if (cnt == null || cnt <= 0) {
            throw new IllegalArgumentException("Invalid count");
        }
        int pageSize = 500;

        // 2.分页
        int total = (cnt + pageSize - 1) / pageSize;

        // 3.汇总
        List<T> result = new ArrayList<>(cnt);
        for (int i = 0; i < total; i++) {
            Collection<T> supp = supplier.get();
            result.addAll(supp);
        }

        // 4.处理
        return result;

    }

    /**
     * 并行分页查询数据库并汇总数据
     * @param cntSupplier
     * @param supplier
     * @return
     * @param <T>
     */
    public <T> List<T> processWithPaginationWithCompletableFuture(Supplier<Integer> cntSupplier, Supplier<Collection<T>> supplier) {
        Objects.requireNonNull(cntSupplier, "cntSupplier must not be null");
        Objects.requireNonNull(supplier, "supplier must not be null");

        Integer cnt = cntSupplier.get();
        if (cnt == null || cnt <= 0) {
            throw new IllegalArgumentException("Invalid count");
        }
        int pageSize = 500;

        // 2.分页
        int total = (cnt + pageSize - 1) / pageSize;

        // 3.汇总
        List<CompletableFuture<Collection<T>>> futures = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            int finalI = i;
            CompletableFuture<Collection<T>> future = CompletableFuture.supplyAsync(supplier);
            futures.add(future);
        }
        List<T> result = futures.stream()
                .map(future -> {
                    try {
                        // 等待CompletableFuture完成并获取结果
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return result;

    }

    public int queryCount() {
        return 2000;
    }

}
