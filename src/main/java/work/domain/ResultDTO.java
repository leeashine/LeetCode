package work.domain;

import java.io.Serializable;

/**
 * @author lee
 */
public class ResultDTO<T extends Serializable> extends ResultSupport {

    private static final long serialVersionUID = 2810963188255746714L;
    private T model;

    public ResultDTO() {

    }

    public ResultDTO(int code, String msg) {
        super(code,msg);
    }

    public ResultDTO(T t) {
        this.model = t;
    }

    public T getModel() {
        return this.model;
    }

    public void setModel(T model) {
        this.model = model;
    }

}
