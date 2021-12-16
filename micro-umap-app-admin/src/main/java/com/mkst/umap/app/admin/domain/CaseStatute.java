package com.mkst.umap.app.admin.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import java.util.Date;

/**
 * 案件相关法规表 umap_case_statute
 * 
 * @author wangyong
 * @date 2021-06-22
 */
public class CaseStatute extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 案件类型 */
	private String caseTypeCode;
	/** 标题 */
	private String name;
	/** 内容地址 */
	private String content;
	/** 副标题 */
	private String disc;
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
	public void setName(String name) 
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	public void setContent(String content) 
	{
		this.content = content;
	}

	public String getContent() 
	{
		return content;
	}

	public void setDisc(String disc)
	{
		this.disc = disc;
	}

	public String getDisc() 
	{
		return disc;
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
            .append("name", getName())
            .append("content", getContent())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("disc", getDisc())
            .append("lable", getLable())
            .toString();
    }
}
