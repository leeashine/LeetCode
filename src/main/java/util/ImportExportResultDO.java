package util;

import java.io.Serializable;

public class ImportExportResultDO implements Serializable {

    private static final long serialVersionUID = 2804834343503874253L;

    /**
     * 成功条数
     */
    private Integer successCount;

    /**
     * 失败条数
     */
    private Integer failureCount;

    /**
     * 失败文件 tfs key
     */
    private String failureFile;

    /**
     * 导出文件 tfs key
     */
    private String exportFile;

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }

    public String getFailureFile() {
        return failureFile;
    }

    public void setFailureFile(String failureFile) {
        this.failureFile = failureFile;
    }

    public String getExportFile() {
        return exportFile;
    }

    public void setExportFile(String exportFile) {
        this.exportFile = exportFile;
    }
}
