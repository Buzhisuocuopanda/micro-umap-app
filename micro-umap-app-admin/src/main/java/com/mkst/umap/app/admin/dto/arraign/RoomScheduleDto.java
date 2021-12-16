package com.mkst.umap.app.admin.dto.arraign;

import java.util.LinkedHashMap;

/**
 * @ClassName RoomScheduleDTO
 * @Description 预约时的可用房间及时间段
 * @Author hsw
 * @Date 2020-06-17 17:52
 */
public class RoomScheduleDto {
    private String id;
    private String name;
    private LinkedHashMap<String, Boolean> scheduleAm;
    private LinkedHashMap<String, Boolean> schedulePm;
    private boolean roomAvailable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedHashMap<String, Boolean> getScheduleAm() {
        return scheduleAm;
    }

    public void setScheduleAm(LinkedHashMap<String, Boolean> scheduleAm) {
        this.scheduleAm = scheduleAm;
    }

    public LinkedHashMap<String, Boolean> getSchedulePm() {
        return schedulePm;
    }

    public void setSchedulePm(LinkedHashMap<String, Boolean> schedulePm) {
        this.schedulePm = schedulePm;
    }

    public boolean isRoomAvailable() {
        return roomAvailable;
    }

    public void setRoomAvailable(boolean roomAvailable) {
        this.roomAvailable = roomAvailable;
    }
}
