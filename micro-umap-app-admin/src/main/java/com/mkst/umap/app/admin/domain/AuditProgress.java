package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 审核进度表 umap_audit_progress
 * 
 * @author wangyong
 * @date 2020-11-09
 */
@Data
public class AuditProgress extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 主键 */
	private Long id;
	/** 事件类型 */
	private String businessType;
	/** 事件id */
	private Long businessId;
	/** 审核记录id */
	private Long recordId;
	/** 审核进度 */
	private Integer progress;
	/** 目标类型（用户、角色等） */
	private String targetType;
	/** 负责人登录名 */
	private String target;
	/** 创建人 */
	private String createBy;
	/** 创建时间 */
	private Date createTime;
	/** 更新者 */
	private String updateBy;
	/** 更新时间 */
	private Date updateTime;
	/** 备注 */
	private String remark;
	/** 删除标识 */
	private String delFlag;
}
