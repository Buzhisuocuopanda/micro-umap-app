package com.mkst.umap.app.common.enums;

public enum NoticeTypeEnum {

    NOTICE("公告",2),
    ARRAIGN_NOTICE("远程提审公告", 3);

    private String name;
    private Integer value;

    NoticeTypeEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    // 普通方法
    public static String getName(int value) {
        for (NoticeTypeEnum noticeTypeEnum : NoticeTypeEnum.values()) {
            if (noticeTypeEnum.getValue() == value) {
                return noticeTypeEnum.name;
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
