package com.mkst.umap.app.admin.dto.room;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName RoomAppointmentDto
 * @Description 获取房间某时刻的进行的事件的详细信息
 * @Author wangyong
 * @Date 2020-07-03 11:44
 */
@Data
@ApiModel(value = "房间事件详情")
public class RoomAppointmentDto {

    @ApiModelProperty(value = "事件类型 (1:视频提审 2:专案)")
    private String type;

    @ApiModelProperty(value = "事件Id")
    private Long id;

    @ApiModelProperty(value = "事件详细信息")
    private Object eventDetailDto;
}
