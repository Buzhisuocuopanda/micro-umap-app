package com.mkst.umap.app.admin.dto.apply;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@ApiModel(value = "申请表 数据传递对象")
@Data
public class ApplyInfoDto {

    /** 申请id */
    @ApiModelProperty(value = "申请id")
    private Long applyId;

    /** 部门id */
    @ApiModelProperty(value = "部门id")
    private Long deptId;

    /** 申请人id */
    @ApiModelProperty(value = "申请人id")
    private Long applicantId;
    @ApiModelProperty(value = "申请人姓名")
    private String applicant;
    @ApiModelProperty(value = "申请人性别")
	private String applicantSex;

    /** 房间id */
    @ApiModelProperty(value = "房间id")
    private Long roomId;

    /** 审核人id */
    @ApiModelProperty(value = "审核人id")
    private Long approverId;

    /** 申请类型 */
    @ApiModelProperty(value = "申请类型")
    private Integer applyType;

    /** 申请状态 */
    @ApiModelProperty(value = "申请状态")
    private List<Integer> applyStatusList;

    /** 申请原因 */
    @ApiModelProperty(value = "申请原因")
    private String applyReason;

    /** 入住开始时间 */
    @ApiModelProperty(value = "入住开始时间 yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /** 入住结束时间 */
    @ApiModelProperty(value = "入住结束时间 yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    @ApiModelProperty(value = "审核结果状态 0:直接审批 1:提交领导审批 2：驳回  3: 取消")
    private String approverStatus;

    @ApiModelProperty(value = "使用人数据传递对象集合")
    private List<BackUpGuestDto> guests;

    /*@ApiModelProperty(value = "角色编码")
    private List<String> roleKey;*/

    @ApiModelProperty(value = "查询类型 0：我的申请  1：我的审批")
    private String selectType;

    /**
     * 男性入住人数
     */
    private Integer femaleNum;
    /**
     * 女性入住人数
     */
    private Integer maleNum;
    /**
     * 单间数量
     */
    private Integer singleRoomNum;
    /**
     * 标准间数量
     */
    private Integer standardRoomNum;
}
