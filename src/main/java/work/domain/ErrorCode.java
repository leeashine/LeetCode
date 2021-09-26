package work.domain;

import java.io.Serializable;

/**
 * @author lee
 */
public class ErrorCode implements Serializable {

    private static final long serialVersionUID = 3807839776983718411L;

    public static final int C_SUCCESS = 0;
    public static final ErrorCode SUCCESS = new ErrorCode(C_SUCCESS, "成功");

    public static final int C_PARAM_ERROR = 99999;
    public static final ErrorCode PARAM_ERROR = new ErrorCode(C_PARAM_ERROR, "参数错误");


    private int code;
    private String desc;

    public ErrorCode() {
    }

    public ErrorCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public boolean isSuccess() {
        return ErrorCode.SUCCESS.eq(this.code);
    }

    public boolean eq(ErrorCode obj) {
        return this.getCode() == obj.getCode();
    }

    public boolean eq(int code) {
        return this.getCode() == code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
