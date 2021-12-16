package com.mkst.umap.app.admin.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkst.mini.systemplus.common.annotation.Excel;
import lombok.Data;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 食堂包厢餐次表 umap_box_meal
 * 
 * @author wangyong
 * @date 2020-08-31
 */
@Data
public class BoxMeal extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	@Excel(
			name = "序号"
	)
	private Long id;
	/** 包厢id */
	private String boxId;
	/** 餐次类型 */
	private String typeCode;
	/** 餐次类型名 */
	@Excel(
			name = "餐次类型名"
	)
	private String typeName;
	/**
	 * 开始时间
	 */
	@Excel(
			name = "开始时间"
	)
	@DateTimeFormat(pattern = "HH:mm:ss")
	@JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
	private String startTime;
	/**
	 * 结束时间
	 */
	@DateTimeFormat(pattern = "HH:mm:ss")
	@JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
	@Excel(
			name = "结束时间"
	)
	private String endTime;
	/** 逻辑删除标识（0：正常 1：删除） */
	private String delFlag;

	//包厢名
	@Excel(
			name = "包厢名"
	)
	private String name;
	//包厢地址
	@Excel(
			name = "包厢地址"
	)
	private String address;


	private String userName;
}
