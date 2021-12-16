package com.mkst.umap.app.admin.api.bean.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @ClassName SpecialCaseListParam
 * @Description 请求专案列表的参数
 * @Author wangyong
 * @Date 2020-07-06 10:21
 */
@Data
@ApiModel(value = "请求专案列表的参数")
public class SpecialCaseListParam {

    @ApiModelProperty(value = "专案id")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "日期")
    private Date checkDate;

    @ApiModelProperty(value = "申请状态列表")
    private List<String> statusList;

    @ApiModelProperty(value = "申请(时间)状态列表")
    private String nowStatus;

    private Long deptId;

    private String status;

    /**
     * 创建者
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    private String createBy;

    private String useBy;

    private String year;
    private String month;
    private String nowUserLoginName;
}
