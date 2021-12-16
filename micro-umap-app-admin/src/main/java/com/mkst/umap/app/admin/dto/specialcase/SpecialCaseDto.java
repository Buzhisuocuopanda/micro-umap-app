package com.mkst.umap.app.admin.dto.specialcase;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName SpecialCaseDto
 * @Description 专案的申请信息
 * @Author wangyong
 * @Date 2020-07-02 10:22
 */
@ApiModel(value = "专案申请信息提交")
@Data
public class SpecialCaseDto {

    @ApiModelProperty(value = "申请id")
    private Long id;

    @ApiModelProperty(value = "房间id")
    private String roomId;

    @ApiModelProperty(value = "房间名")
    private String roomName;

    @ApiModelProperty(value = "专案标题")
    private String title;

    @ApiModelProperty(value = "部门Id")
    private Long deptId;

    @ApiModelProperty(value = "部门")
    private Long dept;

    @ApiModelProperty(value = "使用人")
    private String useBy;

    @ApiModelProperty(value = "开始时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "审核结果状态 0：待审核 1：已审核 2：未通过")
    private String status;

    @ApiModelProperty(value = "当前状态 0：未开始 1：进行中 2：已结束")
    private String nowStatus;
}
