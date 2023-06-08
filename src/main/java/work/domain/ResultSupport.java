package work.domain;

/**
 * @author lee
 */
public class ResultSupport extends BaseDO {

    private static final long serialVersionUID = -5721502231078478262L;
    private int code;
    private String msg;

    public ResultSupport() {
        this.code = 0;
        this.msg = "成功";
    }

    public ResultSupport(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return 0 == this.getCode();
    }

}
