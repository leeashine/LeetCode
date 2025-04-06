package dyaop;

public enum OperateEventEnum {

    ADD("add"),
    DELETE("delete");

    private String value;

    OperateEventEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
