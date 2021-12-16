package com.mkst.umap.app.admin.dto.specialcase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.LinkedList;

/**
 * @ClassName ScheduleInfoDto
 * @Description 预约专案时展示已经预约的时间段
 * @Author wangyong
 * @Date 2020-07-06 16:09
 */
@Data
@ApiModel(value = "已经预约的时间段展示")
public class ScheduleInfoDto {

    @ApiModelProperty(value = "房间id")
    private String id;

    @ApiModelProperty(value = "房间名")
    private String name;

    @ApiModelProperty(value = "当前房间是否可用")
    private boolean roomAvailable;

    @ApiModelProperty(value = "已预约的时间段")
    private LinkedList<String> dateList;

}
