package designpattern;

import work.BaseDO;

import java.io.Serializable;

public class ResultDTO<T extends Serializable> extends BaseDO {

    private T model;
    private int code;
    private String msg;

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
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
}
