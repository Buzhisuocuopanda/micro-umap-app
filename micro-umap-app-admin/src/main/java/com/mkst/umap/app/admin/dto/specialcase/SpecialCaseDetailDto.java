package com.mkst.umap.app.admin.dto.specialcase;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName SpecialCaseDetailDto
 * @Description 专案详细信息
 * @Author wangyong
 * @Date 2020-07-03 12:21
 */
@Data
@ApiModel(value = "专案详细信息")
public class SpecialCaseDetailDto {

    @ApiModelProperty(value = "专案预约Id")
    private Long id;

    @ApiModelProperty(value = "专案tittle")
    private String title;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "预约时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date applyDate;

    @ApiModelProperty(value = "提审室")
    private String roomName;

    @ApiModelProperty(value = "部门Id")
    private Long deptId;

    @ApiModelProperty(value = "部门")
    private String dept;

    @ApiModelProperty(value = "使用人")
    private String useBy;

    @ApiModelProperty(value = "使用人")
    private String prosecutorName;


    @ApiModelProperty(value = "使用者姓名")
    private String undertaker;

    @ApiModelProperty(value = "状态 (0:待审核 1:已审核 2:驳回)")
    private String status;

    @ApiModelProperty(value = "状态 (0:待审核 1:已审核 2:驳回)")
    private String auditStatus;

    @ApiModelProperty(value = "状态 (待审核 已审核 驳回)")
    private String auditName;

}
