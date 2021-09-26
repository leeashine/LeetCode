package work.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author lee
 */
public class PageResultDTO<T extends Serializable> extends BatchResultDTO {

    private static final long serialVersionUID = 1171163892946733172L;
    private int totalCount;
    private int pageSize;
    private int pageNo;

    public PageResultDTO() {
    }

    public PageResultDTO(int code, String msg) {
        super(code, msg);
    }

    public PageResultDTO(List<T> model, int totalCount, int pageNo, int pageSize) {
        super(model);
        this.totalCount = totalCount;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
