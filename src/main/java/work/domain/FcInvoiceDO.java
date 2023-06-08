package work.domain;

import java.util.Date;

/**
 * Created by LIZIXUAN560 on 2020/11/3.
 *
 * @author LIZIXUAN560
 */
public class FcInvoiceDO extends BaseDO {

    private String name;
    private int age;
    /**
     * 自增主键
     **/
    private Long id;

    /**
     * 创建时间
     **/
    private Date gmtCreated;

    /**
     * 更新时间
     **/
    private Date gmtModified;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(Date gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}
