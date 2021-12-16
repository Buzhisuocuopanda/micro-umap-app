package com.mkst.umap.app.admin.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import java.util.Date;

/**
 * 案件表 umap_case_base
 * 
 * @author wangyong
 * @date 2021-06-22
 */
public class CaseBase extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 案件类型代码 */
	private Integer caseTypeCode;
	/** 案件类型名称 */
	private String caseTypeName;
	/** 案件标题 */
	private String title;
	/** 判刑时长（单位：月） */
	private Integer length;
	/** 罚金（单位：元） */
	private Integer penalty;
	/** 缓刑时间（单位：月） */
	private Integer reprieve;
	/** 详细地址 */
	private String details;
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
	public void setCaseTypeCode(Integer caseTypeCode) 
	{
		this.caseTypeCode = caseTypeCode;
	}

	public Integer getCaseTypeCode() 
	{
		return caseTypeCode;
	}
	public void setCaseTypeName(String caseTypeName) 
	{
		this.caseTypeName = caseTypeName;
	}

	public String getCaseTypeName() 
	{
		return caseTypeName;
	}
	public void setTitle(String title) 
	{
		this.title = title;
	}

	public String getTitle() 
	{
		return title;
	}
	public void setLength(Integer length) 
	{
		this.length = length;
	}

	public Integer getLength() 
	{
		return length;
	}
	public void setPenalty(Integer penalty)
	{
		this.penalty = penalty;
	}

	public Integer getPenalty()
	{
		return penalty;
	}
	public void setReprieve(Integer reprieve)
	{
		this.reprieve = reprieve;
	}

	public Integer getReprieve()
	{
		return reprieve;
	}
	public void setDetails(String details) 
	{
		this.details = details;
	}

	public String getDetails() 
	{
		return details;
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
            .append("caseTypeName", getCaseTypeName())
            .append("title", getTitle())
            .append("length", getLength())
            .append("penalty", getPenalty())
            .append("reprieve", getReprieve())
            .append("details", getDetails())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("lable", getLable())
            .toString();
    }
}
