package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 拜访申请表 umap_visit_apply
 * 
 * @author wangyong
 * @date 2020-08-13
 */
public class VisitApply extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 姓名 */
	private String name;
	/** 电话 */
	private String phone;
	/** 证件类型 */
	private String certificateType;
	/** 证件号 */
	private String certificateCode;
	/** 身体状况 */
	private String bodyStatus;
	/** 体温 */
	private String temperature;
	/** 是否入境人员 */
	private String entry;
	/** 是否有接触史 */
	private String contact;
	/**  */
	private String userType;
	/** 邀请码 */
	private String invitationCode;
	/** 开始时间 */
	private Date startTime;
	/** 结束时间 */
	private Date endTime;
	/** 邀请人 */
	private Integer invitationUser;
	/** 状态 */
	private String status;
	/** 拜访事由 */
	private String reason;
	/** 创建人 */
	private String createBy;
	/** 创建时间 */
	private Date createTime;
	/** 更新者 */
	private String updateBy;
	/** 更新时间 */
	private Date updateTime;
	/** 备注 */
	private String reamrk;

	private String auditStatus;

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	private String company;

	private String type;

	private String visitPhone;

	public String getVisitPhone() {
		return visitPhone;
	}

	public void setVisitPhone(String visitPhone) {
		this.visitPhone = visitPhone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setName(String name) 
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	public void setPhone(String phone) 
	{
		this.phone = phone;
	}

	public String getPhone() 
	{
		return phone;
	}
	public void setCertificateType(String certificateType) 
	{
		this.certificateType = certificateType;
	}

	public String getCertificateType() 
	{
		return certificateType;
	}
	public void setCertificateCode(String certificateCode) 
	{
		this.certificateCode = certificateCode;
	}

	public String getCertificateCode() 
	{
		return certificateCode;
	}
	public void setBodyStatus(String bodyStatus) 
	{
		this.bodyStatus = bodyStatus;
	}

	public String getBodyStatus() 
	{
		return bodyStatus;
	}
	public void setTemperature(String temperature) 
	{
		this.temperature = temperature;
	}

	public String getTemperature() 
	{
		return temperature;
	}
	public void setEntry(String entry) 
	{
		this.entry = entry;
	}

	public String getEntry() 
	{
		return entry;
	}
	public void setContact(String contact) 
	{
		this.contact = contact;
	}

	public String getContact() 
	{
		return contact;
	}
	public void setUserType(String userType) 
	{
		this.userType = userType;
	}

	public String getUserType() 
	{
		return userType;
	}
	public void setInvitationCode(String invitationCode) 
	{
		this.invitationCode = invitationCode;
	}

	public String getInvitationCode() 
	{
		return invitationCode;
	}
	public void setStartTime(Date startTime) 
	{
		this.startTime = startTime;
	}

	public Date getStartTime() 
	{
		return startTime;
	}
	public void setEndTime(Date endTime) 
	{
		this.endTime = endTime;
	}

	public Date getEndTime() 
	{
		return endTime;
	}
	public void setInvitationUser(Integer invitationUser) 
	{
		this.invitationUser = invitationUser;
	}

	public Integer getInvitationUser() 
	{
		return invitationUser;
	}
	public void setStatus(String status) 
	{
		this.status = status;
	}

	public String getStatus() 
	{
		return status;
	}
	public void setReason(String reason) 
	{
		this.reason = reason;
	}

	public String getReason() 
	{
		return reason;
	}
	@Override
    public void setCreateBy(String createBy)
	{
		this.createBy = createBy;
	}

	@Override
    public String getCreateBy()
	{
		return createBy;
	}
	@Override
    public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	@Override
    public Date getCreateTime()
	{
		return createTime;
	}
	@Override
    public void setUpdateBy(String updateBy)
	{
		this.updateBy = updateBy;
	}

	@Override
    public String getUpdateBy()
	{
		return updateBy;
	}
	@Override
    public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	@Override
    public Date getUpdateTime()
	{
		return updateTime;
	}
	public void setReamrk(String reamrk) 
	{
		this.reamrk = reamrk;
	}

	public String getReamrk() 
	{
		return reamrk;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("phone", getPhone())
            .append("certificateType", getCertificateType())
            .append("certificateCode", getCertificateCode())
            .append("bodyStatus", getBodyStatus())
            .append("temperature", getTemperature())
            .append("entry", getEntry())
            .append("contact", getContact())
            .append("userType", getUserType())
            .append("invitationCode", getInvitationCode())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("invitationUser", getInvitationUser())
            .append("status", getStatus())
            .append("reason", getReason())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("reamrk", getReamrk())
            .toString();
    }
}
