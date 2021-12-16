package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 会议参与人员表 umap_meeting_attendee
 *
 * @author wangyong
 * @date 2020-07-31
 */
public class MeetingAttendee extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 会议id
	 */
	private Long meetingId;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 创建人
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
	 * 备注
	 */
	private String remark;
	/**
	 * 删除标识
	 */
	private String delFlag;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setMeetingId(Long meetingId) {
		this.meetingId = meetingId;
	}

	public Long getMeetingId() {
		return meetingId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
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

	@Override
    public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
    public String getRemark() {
		return remark;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getDelFlag() {
		return delFlag;
	}

	@Override
    public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId())
				.append("meetingId", getMeetingId())
				.append("userId", getUserId())
				.append("createBy", getCreateBy())
				.append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy())
				.append("updateTime", getUpdateTime())
				.append("remark", getRemark())
				.append("delFlag", getDelFlag())
				.toString();
	}
}
