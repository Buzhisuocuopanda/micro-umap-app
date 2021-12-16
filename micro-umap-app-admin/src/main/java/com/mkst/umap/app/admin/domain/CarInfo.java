package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.annotation.Excel;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;

/**
 * 车辆表 umap_car_info
 * 
 * @author wangyong
 * @date 2020-07-20
 */
@Data
public class CarInfo extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 车辆id */
	@Excel(
			name = "车辆序号"
	)
	private Long carId;
	/** 车牌号 */
	@Excel(
			name = "车牌号"
	)
	private String licensePlateNumber;
	/** 车辆类型 */
	@Excel(
			name = "车辆类型",combo= {"小轿车","豪华车","超跑"},readConverterExp = "0=小轿车,1=豪华车,2=超跑"
	)
	private String carType;
	/** 最大载客量 */
	@Excel(
			name = "最大载客量"
	)
	private Integer maxCarrying;

	/** 是否可用状态 */
	@Excel(
			name = "是否可用状态  （0：正常 1：禁用）",combo= {"正常","禁用"},readConverterExp = "0=正常,1=禁用"
	)
	private String status;
	/** 逻辑删除标识（0：正常 1：删除）
             */
	private String delFlag;

	private String createName;

	private String updateName;
}
