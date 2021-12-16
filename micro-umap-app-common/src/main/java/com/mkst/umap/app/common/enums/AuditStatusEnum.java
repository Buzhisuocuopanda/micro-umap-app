package com.mkst.umap.app.common.enums;

/**
 * @author wangyong
 */
public enum AuditStatusEnum {

    EVENT_AUDIT_STATUS_WAIT("待审核", 0),
    EVENT_AUDIT_STATUS_PASS("已通过", 1),
    EVENT_AUDIT_STATUS_FAIL("未通过", 2),
    EVENT_AUDIT_STATUS_CANCEL("已取消", 4),
    EVENT_AUDIT_STATUS_PLUS("加号", 5);


    private String name;
    private Integer value;

    AuditStatusEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    // 普通方法
    public static String getName(int value) {
        for (AuditStatusEnum auditStatusEnum : AuditStatusEnum.values()) {
            if (auditStatusEnum.getValue() == value) {
                return auditStatusEnum.name;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
