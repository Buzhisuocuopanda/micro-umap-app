package com.mkst.umap.app.admin.dto.arraign;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkst.umap.app.admin.api.bean.result.arraign.OngoingAppointmentResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName ArraignRoomResult
 * @Description 提审室
 * @Author wangyong
 * @Date 2020-07-02 17:54
 */
@Data
@ApiModel(value = "获取提审室列表的结果")
public class ArraignRoomListDto {
    private static final long serialVersionUID = 1L;

    /**
     * 房间id
     */
    @ApiModelProperty(value = "提审室ID")
    private String id;
    /**
     * 房间名
     */
    @ApiModelProperty(value = "提审室名")
    private String name;
    /**
     * 查询某个时间点的状况
     */
    @ApiModelProperty(value = "时间检查点 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkDatetime;
    /**
     * 提审室状态
     */
    @ApiModelProperty(value = "提审室当前状态（0：空闲 1：使用中）")
    private String nowStatus;
    /**
     * 当前的正在进行的事件详情
     */

    @ApiModelProperty
    private OngoingAppointmentResult ongoingAppointmentResult;
}
