package com.mkst.umap.app.admin.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import java.util.Date;

/**
 * 指导案例表 umap_case_guide
 * 
 * @author wangyong
 * @date 2021-06-25
 */
public class CaseGuide extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 案件类型 */
	private String caseTypeCode;
	/** 主标题 */
	private String maintitle;
	/** 副标题 */
	private String subhead;
	/** 页面地址 */
	private String detail;
	/** 创建人 */
	private String createBy;
	/** 创建时间 */
	private Date createTime;
	/** 修改人 */
	private String updateBy;
	/** 修改时间 */
	private Date updateTime;
	/** 回复 */
	private Integer replytype;
	/** 内容 */
	private String lable;

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setCaseTypeCode(String caseTypeCode) 
	{
		this.caseTypeCode = caseTypeCode;
	}

	public String getCaseTypeCode() 
	{
		return caseTypeCode;
	}
	public void setMaintitle(String maintitle) 
	{
		this.maintitle = maintitle;
	}

	public String getMaintitle() 
	{
		return maintitle;
	}
	public void setSubhead(String subhead) 
	{
		this.subhead = subhead;
	}

	public String getSubhead() 
	{
		return subhead;
	}
	public void setDetail(String detail) 
	{
		this.detail = detail;
	}

	public String getDetail() 
	{
		return detail;
	}
	public void setCreateBy(String createBy) 
	{
		this.createBy = createBy;
	}

	public String getCreateBy() 
	{
		return createBy;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}
	public void setUpdateBy(String updateBy) 
	{
		this.updateBy = updateBy;
	}

	public String getUpdateBy() 
	{
		return updateBy;
	}
	public void setUpdateTime(Date updateTime) 
	{
		this.updateTime = updateTime;
	}

	public Date getUpdateTime() 
	{
		return updateTime;
	}
	public void setReplytype(Integer replytype) 
	{
		this.replytype = replytype;
	}

	public Integer getReplytype() 
	{
		return replytype;
	}
	public void setLable(String lable) 
	{
		this.lable = lable;
	}

	public String getLable() 
	{
		return lable;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("caseTypeCode", getCaseTypeCode())
            .append("maintitle", getMaintitle())
            .append("subhead", getSubhead())
            .append("detail", getDetail())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("replytype", getReplytype())
            .append("lable", getLable())
            .toString();
    }
}
