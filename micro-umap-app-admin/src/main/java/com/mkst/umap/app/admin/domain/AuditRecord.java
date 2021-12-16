package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 申请审核记录表 umap_audit_record
 *
 * @author lijinghui
 * @date 2020-06-11
 */
@Data
public class AuditRecord extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
     * id
     */
    private Long id;
    /**
     * 申请id
     */
    private Long applyId;
    /**
     * 申请类型
     */
    private String applyType;
	/**
	 * 状态
	 */
	private String status;
	/**
	 * 审批理由
	 */
	private String reason;
	/**
	 * 创建人
	 */
	private String createBy;

	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新人
	 */
	private String updateBy;
    /**
     * 更新时间
	 */
	private Date updateTime;
	/**
	 * 删除标识
	 */
	private String delFlag;
	private String progress;
	/**
	 * 备注
	 */
	private String remark;

	private String auditUserNaem;

	public AuditRecord() {
	}

	public AuditRecord(Long applyId, String applyType, String status, String reason) {
		this.applyId = applyId;
		this.applyType = applyType;
		this.status = status;
		this.reason = reason;
	}
}
