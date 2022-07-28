package com.mkst.umap.app.admin.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 备勤间申请表 umap_apply_info
 *
 * @author lijinghui
 * @date 2020-06-17
 */
@Data
public class ApplyInfo extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/** 申请id */
	private Long applyId;
	/** 申请人id */
	private Long applicantId;
	/** 房间id */
	private Long roomId;
	/** 房间id */
	private Long deptId;
	/** 申请类型 */
	private Integer applyType;
	/** 申请原因 */
	private String applyReason;
	/** 入住开始时间 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startTime;
	/** 入住结束时间 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endTime;
	/** 申请状态 */
	private Integer applyStatus;
	/** 删除标识 */
	private String delFlag;

	private String deptName;

	private String applicant;
	private String applicantSex;
	private String applicantPhoneNumber;

	/** 使用人 */
	private List<BackUpGuest> backUpGuests;

	private String roomNum;

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

	private Long approvalUserId;
	private Integer approveStatus;
}
