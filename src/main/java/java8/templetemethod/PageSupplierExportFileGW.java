package java8.templetemethod;

import com.sun.org.glassfish.gmbal.Description;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LIZIXUAN560 on 2020/11/18.
 *
 * @author LIZIXUAN560
 */
@Description("订单导出文件记录分页对象")
public class PageSupplierExportFileGW implements Serializable {

    private static final long serialVersionUID = 1654144800287436000L;

    @Description("分页总数")
    public int totalCount;

    @Description("订单导出文件记录列表")
    public List results = new ArrayList<>();

}