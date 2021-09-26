package work.util;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import aleetcode.util.ImportExportDetailDO;
import aleetcode.util.ImportExportResultDO;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class ExportUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExportUtil.class);

    private static ListeningExecutorService listeningExecutor;

    static {
        //真正干活的线程池
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
                5,
                20,
                0,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),
                new ThreadFactoryBuilder().setNameFormat("retryClient-pool-%d").build(),
                new ThreadPoolExecutor.DiscardPolicy());

        listeningExecutor = MoreExecutors.listeningDecorator(poolExecutor);

        //获得一个随着jvm关闭而关闭的线程池，通过Runtime.getRuntime().addShutdownHook(hook)实现
        //修改ThreadFactory为创建守护线程，默认jvm关闭时最多等待120秒关闭线程池，重载方法可以设置时间
        ExecutorService newPoolExecutor = MoreExecutors.getExitingExecutorService(poolExecutor);

        //只增加关闭线程池的钩子，不改变ThreadFactory
        //在线程池中的线程是守护线程(daemon thread)时有用，用户线程(user thread)执行完后，jvm不会立即关闭，而是等待一定时间。
        MoreExecutors.addDelayedShutdownHook(poolExecutor, 120, TimeUnit.SECONDS);
    }

    public static String handle(String operatorId, String fileManager,
                                String tairManager, int tairNamespace,
                                Supplier<ImportExportDetailDO> supplier) {

        String taskId = RandomStringUtils.randomAlphanumeric(10);

        //像线程池提交任务，并得到ListenableFuture
        ListenableFuture<Void> future = listeningExecutor.submit((Callable<Void>) () -> {
            ImportExportDetailDO importExportDetailDO = null;
            try {
                importExportDetailDO = supplier.get();
            } catch (Exception e) {
                logger.warn("导入或者导出操作失败", e);
                return null;
            }
            String tairKey = getTairKey(operatorId, taskId);
            String tfsKey = null;
            ImportExportResultDO resultDO = new ImportExportResultDO();
            resultDO.setSuccessCount(importExportDetailDO.getSuccessCount());
            resultDO.setFailureCount(importExportDetailDO.getFailureCount());
            try {
                if (importExportDetailDO.getFailureOutputStream() != null) {
                    System.out.println("保存文件......");
                    resultDO.setFailureFile(tfsKey);
                } else if (importExportDetailDO.getExportOutputStream() != null) {
                    System.out.println("保存文件......");
                    resultDO.setExportFile(tfsKey);
                }
            } catch (Exception e) {
                logger.warn("上传Excel到tfs失败", e);
                return null;
            } finally {
                IOUtils.closeQuietly(importExportDetailDO.getFailureOutputStream());
                IOUtils.closeQuietly(importExportDetailDO.getExportOutputStream());
            }
            return null;
        });

        return taskId;
    }

    public static String getTairKey(String operatorId, String taskId) {
        return "IMPORT_EXPORT_" + operatorId + "_" + taskId;
    }

    public static void main(String[] args) {

        String taskId = ExportUtil.handle(String.valueOf(100000L), "tfsManager", "tairManager", 1, () -> {
            ImportExportDetailDO importExportDetailDO = new ImportExportDetailDO();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            ExcelUtil.exportExcelXlsx(header, dataList, out, null);
            importExportDetailDO.setExportOutputStream(out);
            return importExportDetailDO;
        });

    }

}
