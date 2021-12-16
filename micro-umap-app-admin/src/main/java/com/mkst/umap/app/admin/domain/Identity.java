package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import com.mkst.mini.systemplus.system.domain.SysFileUpload;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

/**
 * 申请身份表 umap_identity
 * 
 * @author wangyong
 * @date 2020-08-19
 */
public class Identity extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 身份类型 */
	private Integer userType;
	/** 姓名 */
	private String name;
	/**  */
	private Integer userId;
	/**  */
	private String certNo;
	/** 单位名称 */
	private String companyName;
	/**  */
	private Date applyTime;
	/**  */
	private Date aduitTime;
	/** 状态 */
	private String status;
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
	/** 审核原因 */
	private String aduitCause;

	private String postName;

	private String noPostCode;

	public String getNoPostCode() {
		return noPostCode;
	}

	public void setNoPostCode(String noPostCode) {
		this.noPostCode = noPostCode;
	}

	private List<SysFileUpload> sysFileUploads;

	public String getAduitCause() {
		return aduitCause;
	}

	public void setAduitCause(String aduitCause) {
		this.aduitCause = aduitCause;
	}

	public List<SysFileUpload> getSysFileUploads() {
		return sysFileUploads;
	}

	public void setSysFileUploads(List<SysFileUpload> sysFileUploads) {
		this.sysFileUploads = sysFileUploads;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setUserType(Integer userType) 
	{
		this.userType = userType;
	}

	public Integer getUserType() 
	{
		return userType;
	}
	public void setName(String name) 
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	public void setUserId(Integer userId) 
	{
		this.userId = userId;
	}

	public Integer getUserId() 
	{
		return userId;
	}
	public void setCertNo(String certNo) 
	{
		this.certNo = certNo;
	}

	public String getCertNo() 
	{
		return certNo;
	}
	public void setCompanyName(String companyName) 
	{
		this.companyName = companyName;
	}

	public String getCompanyName() 
	{
		return companyName;
	}
	public void setApplyTime(Date applyTime) 
	{
		this.applyTime = applyTime;
	}

	public Date getApplyTime() 
	{
		return applyTime;
	}
	public void setAduitTime(Date aduitTime) 
	{
		this.aduitTime = aduitTime;
	}

	public Date getAduitTime() 
	{
		return aduitTime;
	}
	public void setStatus(String status) 
	{
		this.status = status;
	}

	public String getStatus() 
	{
		return status;
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
	@Override
    public void setRemark(String remark)
	{
		this.remark = remark;
	}

	@Override
    public String getRemark()
	{
		return remark;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userType", getUserType())
            .append("name", getName())
            .append("userId", getUserId())
            .append("certNo", getCertNo())
            .append("companyName", getCompanyName())
            .append("applyTime", getApplyTime())
            .append("aduitTime", getAduitTime())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
