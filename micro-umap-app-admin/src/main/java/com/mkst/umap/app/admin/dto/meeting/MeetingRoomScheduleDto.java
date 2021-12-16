package com.mkst.umap.app.admin.dto.meeting;

import lombok.Data;

import java.util.List;

/**
 * @ClassName MeetingRoomScheduleDto
 * @Description MeetingRoomScheduleDto
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-03 10:07
 */
@Data
public class MeetingRoomScheduleDto {
    /**
     * 会议室Id
     */
    private String id;
    /**
     * 房间名
     */
    private String roomName;
    /**
     * 会议室容量
     */
    private Integer maxNumber;
    /**
     * 当前房间是否可用
     */
    private boolean roomAvailable;
    /**
     * 地址
     */
    private String address;
    /**
     * 已预约的时间段
     */
    private List<String> dateList;
}
