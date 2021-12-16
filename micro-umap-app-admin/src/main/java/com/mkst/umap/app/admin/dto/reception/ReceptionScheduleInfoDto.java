package com.mkst.umap.app.admin.dto.reception;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.LinkedList;

/**
 * @ClassName MeetingRoomScheduleDto
 * @Description 房间排班情况
 * @Author wangyong
 * @Date 2020-07-09 14:13
 */
@Data
public class ReceptionScheduleInfoDto {
    @ApiModelProperty(value = "房间id")
    private String id;

    @ApiModelProperty(value = "房间名")
    private String name;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "当前房间是否可用")
    private boolean roomAvailable;

    @ApiModelProperty(value = "已预约的时间段")
    private LinkedList<String> dateList;

}
