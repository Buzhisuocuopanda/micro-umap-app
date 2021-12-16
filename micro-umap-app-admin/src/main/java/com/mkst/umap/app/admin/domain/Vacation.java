package com.mkst.umap.app.admin.domain;

import lombok.Data;
import java.util.Date;

/**
 * 请假表 umap_vacation
 * 
 * @author wangyong
 * @date 2020-08-24
 */
@Data
public class Vacation
{
	private static final long serialVersionUID = 1L;
	
	/** id */
	private Long id;
	/** 请假类型 */
	private String type;
	/** 开始时间 */
	private Date startTime;
	/** 结束时间 */
	private Date endTime;
	/** 事由 */
	private String content;
	/** 审核人 */
	private Long approveTo;
	/** 是否取消 */
	private String status;
	/** 审核状态 */
	private String auditStatus;
	/** 创建人 */
	private Long createBy;
	/** 创建时间 */
	private Date createTime;
	/** 更新者 */
	private Long updateBy;
	/** 更新时间 */
	private Date updateTime;
	/** 备注 */
	private String remark;
	/** 删除标识 */
	private String delFlag;

	private Date checkDate;

}
