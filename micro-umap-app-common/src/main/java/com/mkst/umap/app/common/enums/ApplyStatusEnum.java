package com.mkst.umap.app.common.enums;

public enum ApplyStatusEnum {

    Pending("待审批",0),
    Approval("已审批", 1),

    Finish("已完成",2),
    CheckIn("可入住",4),
    CheckOut("已退房",5),

    NotStart("未开始",8),
    Ongoing("进行中",9),

    UnFinish("未完成",3),
    Fail("已驳回",6),
    Cancel("已取消",7);

    private String name;
    private Integer value;

    // 普通方法
    public static String getName(int value) {
        for (ApplyStatusEnum applyStatusEnum : ApplyStatusEnum.values()) {
            if (applyStatusEnum.getValue() == value) {
                return applyStatusEnum.name;
            }
        }
        return null;
    }

    ApplyStatusEnum(String name, Integer value){
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
