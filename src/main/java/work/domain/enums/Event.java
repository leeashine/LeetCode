package work.domain.enums;

/**
 * 事件定义
 * 用来驱动流程流转
 */
public enum Event {
    NEXT_STEP("下一步"),
    PREVIOUS_STEP("上一步"),
    APPROVE("同意"),
    REFUSE("拒绝");

    private final String value;

    Event(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
