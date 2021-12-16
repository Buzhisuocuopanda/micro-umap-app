package com.mkst.umap.app.common.enums;

/**
 * @ClassName ArraignOperateType
 * @Description 提审操作功能
 * @Author
 * @Date 2020-07-09 14:21
 */
public enum ArraignOperateTypeEnum {

    ARRAIGN_CASE_CANCEL("案件取消", "1"),
    ARRAIGN_CASE_UPDATE("案件修改", "2"),
    ARRAIGN_PLUS_APPLICATION("加号申请", "3"),
    ARRAIGN_SHIFT_RECORD("换班记录", "4"),
    ARRAIGN_END_ARRAIGNMENT("结束提审", "5");

    private String name;
    private String value;

    ArraignOperateTypeEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
