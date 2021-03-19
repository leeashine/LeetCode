package java8.templetemethod;

/**
 * Created by LIZIXUAN560 on 2020/11/18.
 *
 * @author LIZIXUAN560
 */
public class Test extends AbstractServiceImpl{
    public static void main(String[] args) {

        new Test().pageQueryUndeliveryOrderFile(100, 1000, 1000, 1, 50);
        System.out.println("End......");


    }
    public PageSupplierExportFileGW pageQueryUndeliveryOrderFile(long appId, long userId, long supplierId,
                                                                 int pageNo, int pageSize) {
        return gwWrapper(() -> doPageQueryUndeliveryOrderFile(supplierId, pageNo, pageSize),
                "name",null);
    }

    private PageSupplierExportFileGW doPageQueryUndeliveryOrderFile(long supplierId, int pageNo, int pageSize) {
        return new PageSupplierExportFileGW();
    }
}
