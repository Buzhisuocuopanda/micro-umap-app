package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 办公申请设备表 umap_office_apply_device
 * 
 * @author wangyong
 * @date 2020-11-17
 */
@Data
public class OfficeApplyDevice extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 主键 */
	private Long id;
	/** 父id */
	private Long parentId;
	/** 资产名称 */
	private String assetsName;
	/** 数量 */
	private Integer quantity;
	/** 单位 */
	private String unit;
	/** 规格型号 */
	private String model;
	/** 创建者 */
	private String createBy;
	/** 创建时间 */
	private Date createTime;
	/** 更新者 */
	private String updateBy;
	/** 更新时间 */
	private Date updateTime;
	/** 删除标志(0:正常 1:删除) */
	private String delFlag;
	/** 备注 */
	private String remark;
}
