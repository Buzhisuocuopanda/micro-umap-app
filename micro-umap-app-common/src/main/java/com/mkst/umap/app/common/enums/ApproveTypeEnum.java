package com.mkst.umap.app.common.enums;

public enum ApproveTypeEnum {

    APPROVE_KITCHEN("厨房审批","kitchen_approve"),
    APPROVE_CAR_EXTRA("车辆申请审批流程（额外审批）","car_approve_extra"),
    APPROVE_CAR_SIMPLE("车辆申请审批流程（简单审批）","car_approve_simple"),
    APPROVE_BACKUP("备勤间审批","backup_approve"),
    APPROVE_LOGISTICS("后勤审批","logistics_approve");



    private String name;
    private String value;

    ApproveTypeEnum(String name, String value){
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
