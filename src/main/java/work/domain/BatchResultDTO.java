package work.domain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author lee
 */
public class BatchResultDTO<T extends Serializable> extends ResultSupport {

    private static final long serialVersionUID = 6053625252440845453L;
    private List<T> model = new LinkedList<>();

    public BatchResultDTO() {
    }

    public BatchResultDTO(int code, String msg) {
        super(code, msg);
    }

    public BatchResultDTO(List<T> model) {
        this.model = model;
    }

    public List<T> getModel() {
        return model;
    }

    public void setModel(List<T> model) {
        this.model = model;
    }
}
