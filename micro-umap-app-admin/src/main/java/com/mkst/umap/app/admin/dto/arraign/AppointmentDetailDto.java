package com.mkst.umap.app.admin.dto.arraign;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName AppointmentDetailDto
 * @Description 我的预约-预约详情
 * @Author wangyong
 * @Date 2020-06-17 10:59
 */
@ApiModel(value = "预约列表")
@Data
public class AppointmentDetailDto {

    /**
     * 预约id
     */
    @ApiModelProperty(value = "预约ID")
    private Long id;
    /**
     * 经办人
     */
    @ApiModelProperty(value = "经办人")
    private String undertaker;
    @ApiModelProperty(value = "创建人")
    private String createBy;
    @ApiModelProperty(value = "部门")
    private String dept;
    @ApiModelProperty(value = "选择的部门")
    private String selectDept;
    @ApiModelProperty(value = "房间名")
    private String roomName;
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endTime;
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;
    /**
     * 罪犯姓名
     */
    @ApiModelProperty(value = "罪犯姓名")
    private String criminalName;
    /**
     * 罪犯生日
     */
    @ApiModelProperty(value = "罪犯生日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String criminalBirth;
    /**
     * 犯罪类型
     */
    @ApiModelProperty(value = "犯罪类型")
    private String criminalType;
    /**
     * 罪犯认罪认罚
     */
    @ApiModelProperty(value = "罪犯是否认罪认罚")
    private String criminalAdmit;
    /**
     * 需要法律援助
     */
    @ApiModelProperty(value = "罪犯是否需要法律援助")
    private String needLegalAid;
    /**
     * 案件进展：批捕、已抓捕
     */
    @ApiModelProperty(value = "案件进程")
    private String stage;
    @ApiModelProperty(value = "检察官姓名")
    private String prosecutorName;
    @ApiModelProperty(value = "检察官证件号")
    private String prosecutorId;
    @ApiModelProperty(value = "检察官用户Id")
    private Long prosecutorUserId;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "提审类型")
    private String arraignType;
    @ApiModelProperty(value = "提审场次")
    private Integer numOrder;
    @ApiModelProperty(value = "时间段1上午2下午")
    private String timePie;
    @ApiModelProperty(value = "取消原因")
    private String cancelReason;
    /**
     * 结束状态
     */
    @ApiModelProperty(value = "结束状态")
    private String finishStatus;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date finishTime;
    /**
     * 结束人
     */
    @ApiModelProperty(value = "结束人")
    private String finishBy;

}
