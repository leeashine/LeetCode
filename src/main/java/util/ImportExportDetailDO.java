package util;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;


public class ImportExportDetailDO implements Serializable {

    private static final long serialVersionUID = -3656426740991355766L;

    /**
     * 成功条数
     */
    private Integer successCount;

    /**
     * 失败条数
     */
    private Integer failureCount;

    /**
     * 失败文件
     */
    private ByteArrayOutputStream failureOutputStream;

    /**
     * 导出文件
     */
    private ByteArrayOutputStream exportOutputStream;

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

    public ByteArrayOutputStream getFailureOutputStream() {
        return failureOutputStream;
    }

    public void setFailureOutputStream(ByteArrayOutputStream failureOutputStream) {
        this.failureOutputStream = failureOutputStream;
    }

    public ByteArrayOutputStream getExportOutputStream() {
        return exportOutputStream;
    }

    public void setExportOutputStream(ByteArrayOutputStream exportOutputStream) {
        this.exportOutputStream = exportOutputStream;
    }
}
