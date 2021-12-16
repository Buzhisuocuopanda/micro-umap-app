package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 办公申请审批表 umap_office_apply_audit
 *
 * @author wangyong
 * @date 2020-08-07
 */
public class OfficeApplyAudit extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	private Long id;
	/**
	 * 请求事件ID
	 */
	private Long applyId;
	/**
	 * 更新后状态
	 */
	private String status;
	/**
	 * 原因描述
	 */
	private String reason;
	/**
	 * 创建者
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
	 * 删除标志(0:正常 1:删除)
	 */
	private String delFlag;
	/**
	 * 备注
	 */
	private String remark;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setApplyId(Long applyId) {
		this.applyId = applyId;
	}

	public Long getApplyId() {
		return applyId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

	@Override
    public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Override
    public String getCreateBy() {
		return createBy;
	}

	@Override
    public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
    public Date getCreateTime() {
		return createTime;
	}

	@Override
    public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@Override
    public String getUpdateBy() {
		return updateBy;
	}

	@Override
    public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
    public Date getUpdateTime() {
		return updateTime;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getDelFlag() {
		return delFlag;
	}

	@Override
    public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
    public String getRemark() {
		return remark;
	}

	@Override
    public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId())
				.append("applyId", getApplyId())
				.append("status", getStatus())
				.append("reason", getReason())
				.append("createBy", getCreateBy())
				.append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy())
				.append("updateTime", getUpdateTime())
				.append("delFlag", getDelFlag())
				.append("remark", getRemark())
				.toString();
	}
}
