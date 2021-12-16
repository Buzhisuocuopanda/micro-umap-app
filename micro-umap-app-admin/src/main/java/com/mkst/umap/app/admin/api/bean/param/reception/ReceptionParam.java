package com.mkst.umap.app.admin.api.bean.param.reception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName ReceptionParam
 * @Description 接待预约的相关参数
 * @Author wangyong
 * @Date 2020-07-08 17:59
 */
@Data
@ApiModel(value = "接待申请相关参数")
public class ReceptionParam {

    @ApiModelProperty(value = "接待申请id")
    private Long id;

    @ApiModelProperty(value = "房间id")
    private String roomId;

    @ApiModelProperty(value = "房间名")
    private String roomName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "接待类型")
    private String type;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "使用人id")
    private Long useBy;

    @ApiModelProperty(value = "接待状态")
    private String status;

    @ApiModelProperty(value = "接待当前状态")
    private String nowStatus;

    @ApiModelProperty(value = "接待状态组")
    private String[] statusArr;

    @ApiModelProperty(value = "年份",example = "2020",name = "年份")
    private String year;

    @ApiModelProperty(value = "月份",example = "12",name = "月份")
    private String month;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "日期")
    private Date checkDate;

    @ApiModelProperty(value = "选择的时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date chooseStartTime;

    private String createBy;

    @ApiModelProperty(value = "当前用户id", hidden = true)
    private Long nowUserId;

    @ApiModelProperty(value = "当前用户登录名",hidden = true)
    private String nowUserLoginName;
}
