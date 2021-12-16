package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 资源排程表 umap_arraign_room_schedule
 *
 * @author lijinghui
 * @date 2020-06-11
 */
public class ArraignRoomSchedule extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private Long id;
	/**
	 * 提审房id
	 */
	private String roomId;
	/**
	 * 起始时间
	 */
	private Date startTime;
	/**
	 * 结束时间
	 */
	private Date endTime;
	/**
	 * 最大同时预约数
	 */
	private Integer capacity;
	/**
	 * 创建者
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


	/** 用于数据库参数的属性 */
	/**
	 * 预约时间，用于查找某个处于当前排期的预约申请
	 */
	private String appointmentTime;
	/**
	 * 日期：数据库中是储存着日期和时间，这个参数用来判断某个日期的排期
	 * ’like 2019-12-14‘这样也能使用索引
	 */
	private String scheduleDate;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public Integer getCapacity() {
		return capacity;
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

	public String getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public String getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	@Override
    public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId())
				.append("roomId", getRoomId())
				.append("startTime", getStartTime())
				.append("endTime", getEndTime())
				.append("capacity", getCapacity())
				.append("createBy", getCreateBy())
				.append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy())
				.append("updateTime", getUpdateTime())
				.append("remark", getRemark())
				.append("delFlag", getDelFlag())
				.toString();
	}
}
