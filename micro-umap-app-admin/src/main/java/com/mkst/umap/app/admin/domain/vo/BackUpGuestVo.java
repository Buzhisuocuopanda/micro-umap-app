package com.mkst.umap.app.admin.domain.vo;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 备勤间使用人表 umap_bq_guest
 * 
 * @author lijinghui
 * @date 2020-06-17
 */
public class BackUpGuestVo
{
	/** 使用人名称 */
	private String guestName;
	/** 使用人性别（'0' 男  '1' 女） */
	private Integer guestSex;

	public void setGuestName(String guestName) 
	{
		this.guestName = guestName;
	}

	public String getGuestName() 
	{
		return guestName;
	}
	public void setGuestSex(Integer guestSex)
	{
		this.guestSex = guestSex;
	}

	public Integer getGuestSex()
	{
		return guestSex;
	}
}
