package com.mkst.umap.app.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkst.umap.app.admin.api.bean.result.car.AuditParam;
import com.mkst.umap.app.admin.domain.ApproveApply;
import com.mkst.umap.app.admin.domain.AuditRecord;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 备勤间申请表 umap_apply_info
 * 
 * @author lijinghui
 * @date 2020-06-17
 */
@Data
public class ApplyInfoVo
{
	/** 申请id */
	private Long applyId;
	/** 申请人 */
	private String applicant;
	/** 部门名 */
	private String deptName;
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
	/**入住天数*/
	private Long dayNum;
	/**入住人数*/
	private Integer peopleNum;
	/** 使用人 */
	private List<BackUpGuestVo> backUpGuests;
	/** 审核记录*/
	private List<AuditParam> auditParamList;
	/**创建时间*/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**房间号*/
	private String roomNum;
	/**头像地址*/
	private String avatar;

	private Integer approveStatus;

}
