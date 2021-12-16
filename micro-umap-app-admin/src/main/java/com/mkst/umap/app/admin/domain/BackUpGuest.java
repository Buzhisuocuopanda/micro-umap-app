package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 备勤间使用人表 umap_bq_guest
 *
 * @author lijinghui
 * @date 2020-06-17
 */
public class BackUpGuest extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/** 使用人id */
	private Long guestId;
	/** 申请id */
	private Long applyId;
	/** 使用人名称 */
	private String guestName;
	/** 使用人性别（'0' 男  '1' 女） */
	private Integer guestSex;
	/** 删除标识 */
	private String delFlag;

	public void setGuestId(Long guestId)
	{
		this.guestId = guestId;
	}

	public Long getGuestId()
	{
		return guestId;
	}
	public void setApplyId(Long applyId)
	{
		this.applyId = applyId;
	}

	public Long getApplyId()
	{
		return applyId;
	}
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

	public void setDelFlag(String delFlag)
	{
		this.delFlag = delFlag;
	}

	public String getDelFlag()
	{
		return delFlag;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
				.append("guestId", getGuestId())
				.append("applyId", getApplyId())
				.append("guestName", getGuestName())
				.append("guestSex", getGuestSex())
				.append("createBy", getCreateBy())
				.append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy())
				.append("updateTime", getUpdateTime())
				.append("delFlag", getDelFlag())
				.append("remark", getRemark())
				.toString();
	}
}
