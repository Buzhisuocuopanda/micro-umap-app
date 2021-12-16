package com.mkst.umap.app.common.enums;

/**
 * @ClassName RoomTypeEnum
 * @Description 场地类型
 * @Author wangyong
 * @Date 2020-07-09 14:21
 */
public enum RoomTypeEnum {

    ARRAIGN_ROOM("提审室", "0"),
    RECEPTION_ROOM("接待室", "1"),
    CANTEEN_ROOM("食堂包间", "2"),
    BACKUP_ROOM("备勤间", "2"),
    MEETING_ROOM("会议室", "5");

    private String name;
    private String value;

    RoomTypeEnum(String name, String value) {
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
