package com.mkst.umap.app.admin.dto.specialcase;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName SpecialCaseListDto
 * @Description 获取专案列表的展示用
 * @Author wangyong
 * @Date 2020-07-06 10:35
 */
@Data
@ApiModel(value = "获取专案列表的展示用")
public class SpecialCaseListDto {

    @ApiModelProperty(value = "专案申请ID")
    private Long id;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "使用者姓名")
    private String useBy;

    @ApiModelProperty(value = "房间名")
    private String roomName;
}
