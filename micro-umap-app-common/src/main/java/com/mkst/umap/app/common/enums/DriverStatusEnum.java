package com.mkst.umap.app.common.enums;

public enum DriverStatusEnum {

    //车辆管理司机接单用状态
    DRIVER_NOT_ORDER("未接单",0),
    DRIVER_ORDER("已接单,进行中",1),
    //DRIVER_COMPLETE("已完成",2),

    DRIVER_PENDING("待处理",1),
    DRIVER_COMPLETE("已完成",2),
    DRIVER_REIMBURSEMENT("已报销",3);

    private String name;
    private Integer value;

    DriverStatusEnum(String name, Integer value){
        this.name = name;
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
