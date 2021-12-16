package com.mkst.umap.app.admin.api.bean.param.meeting;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName MeetingParam
 * @Description MeetingParam
 * @Author wangyong
 * @Modified By:
 * @Date 2020-07-31 17:48
 */
@ApiModel(value = "会议相关的参数")
@Data
public class MeetingParam {

    @ApiModelProperty(value = "会议id", example = "3", required = true, name = "会议id")
    private Long id;

    @ApiModelProperty(value = "会议id数组", example = "[1,3,5]", required = true, name = "会议id")
    private Long[] ids;

    @ApiModelProperty(value = "房间id", example = "查询数据库获得", required = true, name = "房间id")
    private String roomId;

    @ApiModelProperty(value = "年份",example = "2020",name = "年份")
    private String year;

    @ApiModelProperty(value = "月份",example = "12",name = "月份")
    private String month;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间", example = "2020-07-05 15:30:00", required = true, name = "开始时间")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间", example = "2020-07-05 16:30:00", required = true, name = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "会议主题", example = "关于9.14重大案件的会议", required = true, name = "结束时间")
    private String subject;

    @ApiModelProperty(value = "使用部门id", example = "123", required = true, name = "使用部门id")
    private Long deptId;

    @ApiModelProperty(value = "申请人id", example = "1", required = true, name = "申请人id")
    private Long useBy;

    @ApiModelProperty(value = "参会人idArr", example = "[1,3,4,5]", required = true, name = "参会人idArr")
    private Long[] attendeeArr;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "申请时选择的开始时间", example = "2020-07-05 16:30:00", required = false, name = "申请时选择的开始时间")
    private Date chooseStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "日期", example = "2020-07-05", required = false, name = "日期")
    private Date checkDate;

    @ApiModelProperty(value = "审核状态", example = "2", required = false, name = "审核状态")
    private String auditStatus;

    @ApiModelProperty(value = "申请状态（0：正常 1：已取消）", example = "2", required = false, name = "申请状态")
    private String status;

    @ApiModelProperty(value = "审核状态", example = "[1,2]", required = false, name = "审核状态")
    private String[] statusArr;

    @ApiModelProperty(value = "审核状态", example = "[1,2]", required = false, name = "审核状态")
    private String[] auditStatusArr;

    @ApiModelProperty(value = "当前状态", example = "0", required = false, name = "当前状态")
    private String nowStatus;

    @ApiModelProperty(value = "审核理由", example = "不好", required = false, name = "审核理由")
    private String reason;

    @ApiModelProperty(value = "当前用户id", hidden = true)
    private Long nowUserId;

    @ApiModelProperty(value = "当前用户登录名",hidden = true)
    private String nowUserLoginName;

    @ApiModelProperty(value = "审核人登录名", hidden = false)
    private String target;

    @ApiModelProperty(value = "审核进度码", hidden = false)
    private Integer progress;
}
