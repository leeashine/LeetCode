package designpattern.statuspatern.work;

/**
 * XX流程的状态流
 */
public enum FlowState {
    INPUT_CONTACT_INFO(1000, "填写联系人信息", "填写联系人信息"){
        @Override
        public FlowState transition(Event event) {
            switch (event){
                case NEXT_STEP:
                    return INPUT_COMPANY_INFO;
                default:
                    throw new RuntimeException("非法操作");
            }
        }
    }
    , INPUT_COMPANY_INFO(1001, "填写公司信息", "填写公司信息"){
        @Override
        public FlowState transition(Event event) {
            switch (event) {
                case PREVIOUS_STEP:
                    return INPUT_CONTACT_INFO;
                case NEXT_STEP:
                    return INPUT_TAX_BANK_INFO;
                default:
                    throw new RuntimeException("非法操作");
            }
        }
    }
    , INPUT_TAX_BANK_INFO(1002, "填写税务和银行信息", "填写税务和银行信息") {
        @Override
        public FlowState transition(Event event) {
            switch (event) {
                case PREVIOUS_STEP:
                    return INPUT_COMPANY_INFO;
                case NEXT_STEP:
                    return SELLER_AUDIT_ING;
                default:
                    throw new RuntimeException("非法操作");
            }
        }
    }, SELLER_AUDIT_ING(1010, "招商审核中", "招商审核") {
        @Override
        public FlowState transition(Event event) {
            switch (event){
                case APPROVE:
                    return SIGN_CONTRACT;
                case REFUSE:
                    return SELLER_AUDIT_REFUSE;
                default:
                    throw new RuntimeException("非法操作");
            }
        }
    }, SELLER_AUDIT_REFUSE(1011, "招商审核拒绝", "招商审核拒绝") {
        @Override
        public FlowState transition(Event event) {
            switch (event){
                case NEXT_STEP:
                    return INPUT_CONTACT_INFO;
                default:
                    throw new RuntimeException("非法操作");
            }
        }
    },
    SIGN_CONTRACT(1030, "签署合同", "签署合同") {
        @Override
        public FlowState transition(Event event) {
            switch (event) {
                case NEXT_STEP:
                    return SUBMIT_PAYMENT;
                default:
                    throw new RuntimeException("非法操作");
            }
        }
    },
    SUBMIT_PAYMENT(1040, "提交服务费、保证金缴纳凭证", "提交服务费、保证金缴纳凭证") {
        @Override
        public FlowState transition(Event event) {
            switch (event) {
                case NEXT_STEP:
                    return BUSINESS_MANAGER_AUDIT_ING;
                default:
                    throw new RuntimeException("非法操作");
            }
        }
    },
    BUSINESS_MANAGER_AUDIT_ING(1050, "商管复核中", "商管复核") {
        @Override
        public FlowState transition(Event event) {
            switch (event) {
                case APPROVE:
                    return ENTER_SUCCESS;
                case REFUSE:
                    return BUSINESS_MANAGER_AUDIT_REFUSE;
                default:
                    throw new RuntimeException("非法操作");
            }
        }
    },
    BUSINESS_MANAGER_AUDIT_REFUSE(1051, "商管复核拒绝", "商管复核") {
        @Override
        public FlowState transition(Event event) {
            switch (event) {
                case NEXT_STEP:
                    return SUBMIT_PAYMENT;
                default:
                    throw new RuntimeException("非法操作");
            }
        }
    },
    ENTER_SUCCESS(1090, "入驻成功", "入驻成功") {
        @Override
        public FlowState transition(Event event) {
            switch (event) {
                default:
                    throw new RuntimeException("非法操作");
            }
        }
    },
    UNKNOWN(-9999, "异常状态", "异常状态") {
        @Override
        public FlowState transition(Event event) {
            switch (event) {
                default:
                    throw new RuntimeException("非法操作");
            }
        }
    };


    private Integer value;
    private String desc;
    private String phase;

    FlowState(Integer value, String desc, String phase) {
        this.value = value;
        this.desc = desc;
        this.phase = phase;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public String getPhase() {
        return phase;
    }

    /**
     * 状态流转的定义
     *
     * @param event 触发的事件
     * @return 流转后的状态
     */
    public abstract FlowState transition(Event event);

    public static FlowState valueOfCode(Integer code) {
        for (final FlowState type : FlowState.values()) {
            if (type.isEquals(code)) {
                return type;
            }
        }
        return UNKNOWN;

    }

    public boolean isEquals(Integer code) {
        return this.value.equals(code);
    }

    public boolean isEquals(FlowState state) {
        return state != null && isEquals(state.value);
    }

}
