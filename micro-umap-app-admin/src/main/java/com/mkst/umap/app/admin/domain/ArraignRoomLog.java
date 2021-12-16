package com.mkst.umap.app.admin.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkst.mini.systemplus.common.annotation.Excel;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 提审室操作日志表 umap_arraign_room_log
 * 
 * @author wangyong
 * @date 2020-09-21
 */
@Data
public class ArraignRoomLog extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** id */
	private Long id;
	/** 提审室id */
	private String roomId;
	/** 使用人姓名 */
	@Excel(name = "*姓名")
	private String name;
	/** 开始时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "*开始时间")
	private Date startTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "*结束时间")
	private Date endTime;
	/** 逻辑删除标识（0：正常 1：删除）*/
	private String delFlag;


	private String roomName;

}
