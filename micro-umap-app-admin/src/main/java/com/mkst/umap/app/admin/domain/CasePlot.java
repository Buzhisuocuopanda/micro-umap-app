package com.mkst.umap.app.admin.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import java.util.Date;

/**
 * 案件计量表 umap_case_plot
 * 
 * @author wangyong
 * @date 2021-06-22
 */
public class CasePlot extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 案件类型 */
	private Integer caseTypeCode;
	/** 名称 */
	private String plotName;
	/** 类型 */
	private Integer plotType;
	/** 单位 */
	private String dataformat;
	/** 开始计量 */
	private Integer offset;
	/** 排序 */
	private Integer orders;
	/** 创建人 */
	private String createBy;
	/** 创建时间 */
	private Date createTime;
	/** 修改人 */
	private String updateBy;
	/** 修改时间 */
	private Date updateTime;

	private String valuess;

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
	public void setPlotName(String plotName) 
	{
		this.plotName = plotName;
	}

	public String getPlotName() 
	{
		return plotName;
	}
	public void setPlotType(Integer plotType) 
	{
		this.plotType = plotType;
	}

	public Integer getPlotType() 
	{
		return plotType;
	}
	public void setDataformat(String dataformat) 
	{
		this.dataformat = dataformat;
	}

	public String getDataformat() 
	{
		return dataformat;
	}
	public void setOffset(Integer offset) 
	{
		this.offset = offset;
	}

	public Integer getOffset() 
	{
		return offset;
	}
	public void setOrders(Integer orders) 
	{
		this.orders = orders;
	}

	public Integer getOrders() 
	{
		return orders;
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

	public String getValuess() {
		return valuess;
	}

	public void setValuess(String valuess) {
		this.valuess = valuess;
	}

	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("caseTypeCode", getCaseTypeCode())
            .append("plotName", getPlotName())
            .append("plotType", getPlotType())
            .append("dataformat", getDataformat())
            .append("offset", getOffset())
            .append("orders", getOrders())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
			.append("valuess", getValuess())
            .toString();
    }
}
