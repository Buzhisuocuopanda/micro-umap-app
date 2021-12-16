package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 设备分组表 p_equipment_group
 * 
 * @author systemplus
 * @date 2019-08-13
 */
@Data
public class EquipmentGroup extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer groupId;
	/**  */
	private Integer parentId;
	/**  */
	private String ancestors;
	/**  */
	private String groupName;
	/**  */
	private String status;
	/**  */
	private String leader;
	/**  */
	private Integer orderNum;
	/**  */
	private String type;
	/** 创建者 */
	private String createBy;
	/** 创建时间 */
	private Date createTime;
	/** 更新者 */
	private String updateBy;
	/** 更新时间 */
	private Date updateTime;
	/**  */
	private String remark;
	/**  */
	private String parentGroupName;
}
