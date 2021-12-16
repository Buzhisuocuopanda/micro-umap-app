package com.mkst.umap.app.admin.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import java.util.Date;

/**
 * 提审预约加号记录表 umap_arraign_appointment_add_log
 * 
 * @author wangyong
 * @date 2021-01-13
 */
@Data
public class ArraignAppointmentAddLog extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 主键ID */
	private Integer id;
	/** 预约人员ID */
	private Long appointmentUserId;
	/** 预约日期 */
	private Date appointmentDate;
	/** 时间段 */
	private String timePie;
	/** 提审室ID */
	private String roomId;
	/** 使用状态（0未使用，1已使用） */
	private String useStatus;
	/**  */
	private String createBy;
	/** 创建时间 */
	private Date createTime;
	/** 使用时间 */
	private Date useTime;

}
