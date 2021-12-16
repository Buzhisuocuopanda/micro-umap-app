package com.mkst.umap.app.common.enums;

public enum ApproveStatusEnum {

    Pending("待审批",0),
    SUCCESS("通过",1),
    FAIL("驳回",2),
    CANCEL("取消",3),
    KITCHEN("厨房已审批",4),
    LOGISTICS("后勤已审批",5),
    FINISH("已结束",6);

    private String name;
    private Integer value;

    ApproveStatusEnum(String name, Integer value){
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
