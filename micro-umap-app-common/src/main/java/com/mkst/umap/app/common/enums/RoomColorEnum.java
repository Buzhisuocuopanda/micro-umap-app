package com.mkst.umap.app.common.enums;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName RoomColorEnum
 * @Description 颜色的枚举(房间背景颜色专用，修改/添加之前请先向作者询问)
 * @Author wangyong
 * @Modified By:
 * @Date 2020-10-23 17:41
 */
public enum RoomColorEnum {

    COLOR_ROOM_BACK_A("房间背景色1","#285fda"),
    COLOR_ROOM_BACK_B("房间背景色2","#2caed9"),
    COLOR_ROOM_BACK_C("房间背景色3","#0399fd"),
    COLOR_ROOM_BACK_D("房间背景色4","#01d8b6"),
    COLOR_ROOM_BACK_E("房间背景色5","#91d000");


    private String name;
    private String value;

    RoomColorEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static String getName(String value) {
        for (RoomColorEnum roomColorEnum : RoomColorEnum.values()) {
            if (roomColorEnum.getValue() == value) {
                return roomColorEnum.name;
            }
        }
        return null;
    }

    //讲枚举转换成list格式，这样前台遍历的时候比较容易，列如 下拉框 后台调用toList方法，你就可以得到code 和name了
    public static List toList() {
        //Lists.newArrayList()其实和new ArrayList()几乎一模
        List list = Lists.newArrayList();
        //  一样, 唯一它帮你做的(其实是javac帮你做的), 就是自动推导(不是"倒")尖括号里的数据类型.
        for (RoomColorEnum airlineTypeEnum : RoomColorEnum.values()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", airlineTypeEnum.getValue());
            map.put("name", airlineTypeEnum.getName());
            list.add(map);
        }
        return list;
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
