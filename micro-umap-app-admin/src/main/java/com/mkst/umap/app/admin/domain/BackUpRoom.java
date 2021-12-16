package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 备勤间表 umap_bq_room
 *
 * @author lijinghui
 * @date 2020-06-17
 */
public class BackUpRoom extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/** 房间id */
	private Long roomId;
	/**  房间号  */
	private String roomNum;
	/** 房间类型 ('0' 单人间    '1'  双人间)*/
	private Integer roomType;
	/** 状态 ('0' 0正常    '1'  停用)*/
	private String status;
	/** 删除标识 */
	private String delFlag;


	public void setRoomId(Long roomId)
	{
		this.roomId = roomId;
	}

	public Long getRoomId()
	{
		return roomId;
	}
	public void setRoomNum(String roomNum)
	{
		this.roomNum = roomNum;
	}

	public String getRoomNum()
	{
		return roomNum;
	}
	public void setRoomType(Integer roomType)
	{
		this.roomType = roomType;
	}

	public Integer getRoomType()
	{
		return roomType;
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
				.append("roomId", getRoomId())
				.append("roomNum", getRoomNum())
				.append("roomType", getRoomType())
				.append("createBy", getCreateBy())
				.append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy())
				.append("updateTime", getUpdateTime())
				.append("delFlag", getDelFlag())
				.append("remark", getRemark())
				.toString();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
