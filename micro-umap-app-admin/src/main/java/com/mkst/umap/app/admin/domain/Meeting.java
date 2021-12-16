package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 会议表 umap_meeting
 *
 * @author wangyong
 * @date 2020-07-31
 */
@Data
public class Meeting extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 会议id
	 */
	private Long id;
	/**
	 * 会议室id
	 */
	private String roomId;
	/**
	 * 开始时间
	 */
	private Date startTime;
	/**
	 * 结束时间
	 */
	private Date endTime;
	/**
	 * 会议主题
	 */
	private String subject;
	/**
	 * 部门id
	 */
	private Long deptId;
	/**
	 * 申请人id
	 */
	private Long useBy;
	/**
	 * 申请状态
	 */
	private String status;
	/**
	 * 审核状态
	 */
	private String auditStatus;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新者
	 */
	private String updateBy;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 删除标识
	 */
	private String delFlag;


	/**
	 * 查询开始时间是否存在
	 */
	private Date reqStartTime;
	/**
	 * 查询开始时间是否存在
	 */
	private Date reqEndTime;

	/**
	 * checkDate
	 */
	private Date checkDate;

	private String[] auditStatusArr;

	private String target;
	private Integer auditProgress;

}
