package com.mkst.umap.app.common.enums;

public enum DiningStatusEnum {


    NotStart("未开始",0),
    Ongoing("进行中",1),
    Finish("已结束",2);

    private String name;
    private Integer value;

    // 普通方法
    public static String getName(int value) {
        for (DiningStatusEnum applyStatusEnum : DiningStatusEnum.values()) {
            if (applyStatusEnum.getValue() == value) {
                return applyStatusEnum.name;
            }
        }
        return null;
    }

    DiningStatusEnum(String name, Integer value){
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
