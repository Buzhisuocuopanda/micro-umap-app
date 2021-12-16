package com.mkst.umap.app.admin.dto.arraign;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName MyAppointmentDto
 * @Description
 * @Author wangyong
 * @Date 2020-06-19 16:07
 */
@Data
@ApiModel(value = "获取我的远程提审申请列表的参数")
public class MyAppointmentDto {

    @ApiModelProperty(value = "申请ID")
    private Long id;

    @ApiModelProperty(value = "房间名")
    private String roomName;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 这里指的是时间状态
     */
    @ApiModelProperty(value = "申请状态")
    private String status;

    @ApiModelProperty(value = "审核状态")
    private String auditStatus;

    @ApiModelProperty(value = "审核状态")
    private String auditName;

    @ApiModelProperty(value = "使用者")
    private String useBy;

    @ApiModelProperty(value = "部门名")
    private String deptName;

    @ApiModelProperty(value = "结束状态")
    private String finishStatus;
}
