package com.mkst.umap.app.common.enums;

/**
 * @ClassName RoomTypeEnum
 * @Description 场地类型
 * @Author wangyong
 * @Date 2020-07-09 14:21
 */
public enum ApplyTypeEnum {

    RECEPTION("接待", "0"),
    OVERTIME("加班", "1"),
    VISITING("来访", "2"),
    OTHER("其他", "3");

    private String name;
    private String value;

    ApplyTypeEnum(String name, String value) {
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
